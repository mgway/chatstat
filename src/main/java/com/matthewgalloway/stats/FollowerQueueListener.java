package com.matthewgalloway.stats;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.matthewgalloway.stats.db.InsertViewer;
import com.matthewgalloway.stats.db.IsViewerFollowing;
import com.matthewgalloway.stats.db.UpdateDatapointCommand;
import com.matthewgalloway.stats.db.AddToDatapointCommand;
import com.matthewgalloway.stats.domain.Datapoint;
import com.matthewgalloway.stats.domain.Viewer;
import com.matthewgalloway.stats.framework.DatabaseService;

@Component
public class FollowerQueueListener {

	@Autowired
	private SimpMessagingTemplate wsTemplate;
    
	@Autowired
	private transient TwitchApiClient client;

	@Autowired
	private transient DatabaseService db;
	
	@Autowired
	@Qualifier("lookupViewer")
	private Queue lookupQueue;
	
	@Autowired
	@Qualifier("lookupViewerFromApi")
	private Queue apiLookupQueue;
	
	@Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * Handle viewer follower requests. Called once per refresh
	 * @param params
	 */
	@JmsListener(destination = "chatstat.viewers")
	public void beginFetchFollowers(Datapoint point) {
		
		List<String> usernames = client.getChatMembers(point.getStreamer());
		if (usernames != null) {
			for(String name : usernames) {
				final Viewer viewer = new Viewer(name, point.getStreamer(), false);			
				final Datapoint pointCopy = point;
				
				this.jmsTemplate.send(this.lookupQueue, new MessageCreator() {
		            public Message createMessage(Session session) throws JMSException {
		                ObjectMessage message = session.createObjectMessage(viewer);
		                message.setLongProperty("datapointId", pointCopy.getId());
		                return message;
		            }
		        });
			}
			point.setChatterCount(usernames.size());
			db.execute(new UpdateDatapointCommand(point));
		}	
	} 
	
	/**
	 * Handle viewer follower requests. Called once per viewer
	 * @param params
	 */
	@JmsListener(destination = "chatstat.viewers.viewer", concurrency="30")
	public void receiveFollowersMessage(Viewer viewer, @Header("datapointId") final long datapointId) {
		
		try {
			viewer.setFollower(db.query(new IsViewerFollowing(viewer)));
			
			// User is following, update the data point
			if(viewer.isFollower()) {
				db.execute(new AddToDatapointCommand(viewer, datapointId));
				
				// And the UI via websocket
				this.wsTemplate.convertAndSend("/topic/" + viewer.getStreamer(), viewer);
				return;
			}
			
		} catch (EmptyResultDataAccessException ex) {
			// Pass
		}
		
		// Otherwise, check the API. TODO make this not call the API every time? 
		final Viewer viewerCopy = viewer;	// Thanks Java
		this.jmsTemplate.send(this.apiLookupQueue, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage(viewerCopy);
                message.setLongProperty("datapointId", datapointId);
                return message;
            }
        });
		
	}
	
	/**
	 * Handle the Twitch API call. Lower concurrency to (hopefully) reduce API
	 * rate limiting
	 * 
	 * @param viewer
	 */
	@JmsListener(destination = "chatstat.followers.viewer.call", concurrency="5")
	public void receiveApiRequestMessage(Viewer viewer, @Header("datapointId") final long datapointId) {
		
		viewer.setFollower(client.isFollowing(viewer.getStreamer(), viewer.getName()));
		db.execute(new InsertViewer(viewer), new AddToDatapointCommand(viewer, datapointId));
		
		this.wsTemplate.convertAndSend("/topic/" + viewer.getStreamer(), viewer);
	}
}
