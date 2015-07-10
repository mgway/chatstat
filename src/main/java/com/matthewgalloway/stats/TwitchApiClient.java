package com.matthewgalloway.stats;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.matthewgalloway.stats.domain.Follower;

@Service
public class TwitchApiClient {
	
	@Value("${api.url}")
	private String API_ROOT;
	
	@Value("${api.clientid}")
	private String API_CLIENT_ID;
	
	public long getFollowerCount(String username) {
		try {
			HttpResponse<JsonNode> response = doFollowerQuery(username, "0", 1);
			if (response.getStatus() == 200) {
				return response.getBody().getObject().getLong("_total");
			} else {
				System.out.println(response.getBody());
			}
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1L;
	}
	
	public List<Follower> getFollowerNames(String username, String offset, int limit) {
		try {
			System.out.println("Offset " + offset);
			HttpResponse<JsonNode> response = doFollowerQuery(username, offset, limit);
			if (response.getStatus() == 200) {
				return parseFollowersResponse(response.getBody());
			} else {
				System.out.println(response.getBody());
			}
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private List<Follower> parseFollowersResponse(JsonNode node) {
		ArrayList<Follower> names = new ArrayList<Follower>(100);
		
		JSONArray followers = node.getObject().getJSONArray("follows");
		
		int length = followers.length();
		for(int i = 0; i < length; i++) {
			Follower follower = new Follower();
			follower.setName(followers.getJSONObject(i).getJSONObject("user").getString("name"));
			follower.setId(followers.getJSONObject(i).getJSONObject("user").getLong("_id"));
			names.add(follower);
		}
		
		return names;
	}
	
	private HttpResponse<JsonNode> doFollowerQuery(String username, String offset, int limit) throws UnirestException {
		String url = String.format("%s/channels/%s/follows", API_ROOT, username);

		return Unirest.get(url)
				.header("Accept", "application/vnd.twitchtv.v3+json")
				.header("Client-ID", API_CLIENT_ID)
				.queryString("limit", limit)
				.queryString("offset", offset)
				.queryString("direction", "desc")
				.asJson();
	}
	
	public boolean isFollowing(String streamer, String follower) {
		try {
			HttpResponse<JsonNode> response = doSingleFollowerQuery(streamer, follower);
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
	
	private HttpResponse<JsonNode> doSingleFollowerQuery(String streamer, String follower) throws UnirestException {
		String url = String.format("%s/users/%s/follows/channels/%s/", API_ROOT, follower, streamer);

		return Unirest.get(url)
				.header("Accept", "application/vnd.twitchtv.v3+json")
				.header("Client-ID", API_CLIENT_ID)
				.asJson();
	}
}
