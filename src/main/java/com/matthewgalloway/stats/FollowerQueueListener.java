package com.matthewgalloway.stats;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.matthewgalloway.stats.db.InsertViewer;
import com.matthewgalloway.stats.db.IsViewerFollowing;
import com.matthewgalloway.stats.domain.Viewer;
import com.matthewgalloway.stats.framework.DatabaseService;

@Component
public class FollowerQueueListener {

	@Autowired
	private SimpMessagingTemplate template;
    
	@Autowired
	private transient TwitchApiClient client;

	@Autowired
	private transient DatabaseService db;

	@JmsListener(destination = "chatstat.followers", concurrency="5")
	public void receiveFollowersMessage(Map<String, String> params) {
		Viewer viewer = new Viewer(params.get("username"), params.get("streamer"), false);
		
		try {
			viewer.setFollower(db.query(new IsViewerFollowing(viewer.getName(), viewer.getStreamer())));
		} catch (EmptyResultDataAccessException ex) {
			viewer.setFollower(client.isFollowing(viewer.getStreamer(), viewer.getName()));
			db.execute(new InsertViewer(viewer));
		}
		
		this.template.convertAndSend("/topic/viewers", viewer);
	}
}
