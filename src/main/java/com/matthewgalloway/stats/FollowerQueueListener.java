package com.matthewgalloway.stats;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.matthewgalloway.stats.db.InsertFollower;
import com.matthewgalloway.stats.db.IsUserFollowingQuery;
import com.matthewgalloway.stats.framework.DatabaseService;

@Component
public class FollowerQueueListener {

	@Autowired
	private transient TwitchApiClient client;

	@Autowired
	private transient DatabaseService db;

	@JmsListener(destination = "chatstat.followers", concurrency="2")
	public void receiveFollowersMessage(Map<String, String> params) {
		
		if (!db.query(new IsUserFollowingQuery(params.get("username")))) {
			boolean isFollowing = client.isFollowing(params.get("streamer"), params.get("username"));
			db.execute(new InsertFollower(params.get("username"), isFollowing));
		}
	}
}
