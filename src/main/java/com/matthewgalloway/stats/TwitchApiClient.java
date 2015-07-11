package com.matthewgalloway.stats;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class TwitchApiClient {
	
	@Value("${api.kraken.url}")
	private String API_ROOT;
	
	@Value("${api.tmi.url}")
	private String TMI_ROOT;
	
	@Value("${api.clientid}")
	private String API_CLIENT_ID;
	
	
	public boolean isFollowing(String streamer, String follower) {
		String url = String.format("%s/users/%s/follows/channels/%s/", API_ROOT, follower, streamer);
		
		try {
			HttpResponse<JsonNode> response = Unirest.get(url)
					.header("Accept", "application/vnd.twitchtv.v3+json")
					.header("Client-ID", API_CLIENT_ID)
					.asJson();
			if (response.getStatus() == 200) {
				return true;
			} else if (response.getStatus() == 404) {
				return false;
			} else {
				System.out.println(response.getBody());
				return false;
			}
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	public List<String> getChatMembers(String streamer) {
		String url = String.format("%s/group/user/%s/chatters", TMI_ROOT, streamer);
		List<String> names = new ArrayList<String>();

		HttpResponse<JsonNode> response;
		try {
			response = Unirest.get(url)
					.header("Accept", "application/json")
					.asJson();
			
			if (response.getStatus() == 200) {
				JSONObject chatters = response.getBody().getObject().getJSONObject("chatters");
				
				// Get mods
				int count = chatters.getJSONArray("moderators").length();
				for (int i = 0; i < count; i++) {
					names.add(chatters.getJSONArray("moderators").get(i).toString());
				}
				
				// Get normal viewers
				count = chatters.getJSONArray("viewers").length();
				for (int i = 0; i < count; i++) {
					names.add(chatters.getJSONArray("viewers").get(i).toString());
				}
				
				return names;
			} else if (response.getStatus() == 404) {
				return null;
			} else {
				System.out.println(response.getBody());
				return null;
			}
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
}
