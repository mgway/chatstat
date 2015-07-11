package com.matthewgalloway.stats;

import java.util.HashMap;
import java.util.List;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.matthewgalloway.stats.domain.Viewer;

@Controller
public class FollowerController {
	
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@Autowired
	private Queue queue;
	
	@Autowired
	private transient TwitchApiClient client;
	
	
	@MessageMapping("/hello")
    public Viewer handle(String streamerName) {
		
		if (streamerName.trim().isEmpty()) {
			return null;
		}
		
		streamerName = streamerName.toLowerCase();
		
		List<String> usernames = client.getChatMembers(streamerName);
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("streamer", streamerName);
				
		for(String name : usernames) {
			params.put("username", name);
			this.jmsMessagingTemplate.convertAndSend(this.queue, params);
		}
		
        return null;
    }
}
