package com.matthewgalloway.stats;

import java.util.HashMap;
import java.util.List;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.matthewgalloway.stats.db.FollowersInChatQuery;
import com.matthewgalloway.stats.db.ResetSeenFlag;
import com.matthewgalloway.stats.framework.DatabaseService;

@Controller
public class FollowerController {

	@Value("${streamer.name}")
	private String USERNAME;
	
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@Autowired
	private Queue queue;
	
	@Autowired
	private transient DatabaseService db;
	
	@Autowired
	private transient TwitchApiClient client;

	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "Click <a href='/fetch/'>here</a> fetch follower information. Click <a href='/stats/'>here</a> to view follower stats";
	}
	
	@RequestMapping("/stats")
	@ResponseBody
	String stats() {
		long followerCount = db.query(new FollowersInChatQuery());
		return String.format("There are %d followers in chat. Click <a href='/fetch/'>here</a> to poll again", followerCount);
	}
	
	@RequestMapping("/fetch")
	@ResponseBody
	String fetch() {
		List<String> usernames = client.getChatMembers(USERNAME);
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("streamer", USERNAME);
		
		db.execute(new ResetSeenFlag());
		
		for(String name : usernames) {
			params.put("username", name);
			this.jmsMessagingTemplate.convertAndSend(this.queue, params);
		}
		
		return String.format("%d users in channel. Click <a href='/stats/'>here</a> in ~2 minutes to view follower stats", usernames.size());
	}
}
