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
import com.matthewgalloway.stats.db.UpdateDatapoint;
import com.matthewgalloway.stats.domain.Stream;
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
	public void beginFetchFollowers(Stream stream, @Header("datapointId") final long datapointId) {
		
		List<String> usernames = client.getChatMembers(stream.getStreamer());
		if (usernames != null) {
			for(String name : usernames) {
				final Viewer viewer = new Viewer(name, stream.getStreamer(), false);			
				
				this.jmsTemplate.send(this.lookupQueue, new MessageCreator() {
		            public Message createMessage(Session session) throws JMSException {
		                ObjectMessage message = session.createObjectMessage(viewer);
		                message.setLongProperty("datapointId", datapointId);
		                return message;
		            }
		        });
			}
		}
	}
	
	/**
	 * Handle viewer follower requests. Called once per viewer
	 * @param params
	 */
	@JmsListener(destination = "chatstat.viewers.viewer", concurrency="20")
	public void receiveFollowersMessage(Viewer viewer, @Header("datapointId") final long datapointId) {
		
		try {
			viewer.setFollower(db.query(new IsViewerFollowing(viewer)));
			
			// User is following, update the data point
			db.execute(new UpdateDatapoint(viewer, datapointId));
			
			// And the UI via websocket
			this.wsTemplate.convertAndSend("/topic/" + viewer.getStreamer(), viewer);
			
		} catch (EmptyResultDataAccessException ex) {
			final Viewer viewerCopy = viewer;	// Thanks Java
			
			this.jmsTemplate.send(this.apiLookupQueue, new MessageCreator() {
	            public Message createMessage(Session session) throws JMSException {
	                ObjectMessage message = session.createObjectMessage(viewerCopy);
	                message.setLongProperty("datapointId", datapointId);
	                return message;
	            }
	        });
			
			//this.jmsTemplate.convertAndSend(this.apiLookupQueue, viewer);
		}
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
		db.execute(new InsertViewer(viewer), new UpdateDatapoint(viewer, datapointId));
		
		this.wsTemplate.convertAndSend("/topic/" + viewer.getStreamer(), viewer);
	}
}
