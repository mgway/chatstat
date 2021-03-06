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
import com.matthewgalloway.stats.domain.Datapoint;
import com.matthewgalloway.stats.exception.ApiException;

@Service
public class TwitchApiClient {
	
	@Value("${api.kraken.url}")
	private String API_ROOT;
	
	@Value("${api.tmi.url}")
	private String TMI_ROOT;
	
	@Value("${api.clientid}")
	private String API_CLIENT_ID;
	
	/**
	 * Check if the viewer is a follower of the given streamer.
	 * 
	 * @param streamer
	 *            name of streamer
	 * @param viewer
	 *            name of viewer
	 * @return true if the user is a follower, false otherwise
	 */
	public boolean isFollowing(String streamer, String viewer) {
		String url = String.format("%s/users/%s/follows/channels/%s/", API_ROOT, viewer, streamer);

		try {
			HttpResponse<JsonNode> response = krakenApiCall(url);
			
			if (response.getStatus() == 200) {
				return true;
			}
		} catch (ApiException e) {
			// Pass
		}
		
		return false;
	}
	
	/**
	 * Check if the viewer is a subscriber of the given streamer.
	 * 
	 * @param streamer
	 *            name of streamer
	 * @param viewer
	 *            name of viewer
	 * @return true if the user is a subscriber, false otherwise
	 */
	public boolean isSubscribing(String streamer, String viewer) {
		String url = String.format("%s/channels/%s/subscriptions/%s/", API_ROOT, streamer, viewer);
		
		try {
			HttpResponse<JsonNode> response = krakenApiCall(url);
			if (response.getStatus() == 200) {
				return true;
			}
		} catch (ApiException e) {
			// Pass
		}
		
		return false;
	}
	
	public Datapoint getStreamData(String streamer) {
		String url = String.format("%s/streams/%s", API_ROOT, streamer);	
		HttpResponse<JsonNode> response = null;
		try {
			response = krakenApiCall(url);
			
			if (response.getStatus() == 200) {
				Datapoint datapoint = new Datapoint(streamer);
				
				if (!response.getBody().getObject().isNull("stream")) {
					JSONObject obj = response.getBody().getObject().getJSONObject("stream");
					datapoint.setGame(obj.getString("game"));
					if (!obj.isNull("channel")) {
						datapoint.setPartner(obj.getJSONObject("channel").getBoolean("partner"));
						datapoint.setStatus(obj.getJSONObject("channel").getString("status"));
					}
					datapoint.setViewerCount(obj.getInt("viewers"));
					return datapoint;
				} else {
					return null;
				}
			} else {
				System.out.println(response.getStatusText());
				throw new ApiException("Twitch API returned an error code, view the console for more details");
			}
		} catch (RuntimeException e) {
			throw new ApiException(e);
		}
	}
	
	private HttpResponse<JsonNode> krakenApiCall(String url) {
		try {
			HttpResponse<JsonNode> response = Unirest.get(url)
					.header("Accept", "application/vnd.twitchtv.v3+json")
					.header("Client-ID", API_CLIENT_ID)
					.asJson();
			if (response.getStatus() == 200) {
				return response;
			} else if (response.getStatus() == 404) {
				throw new ApiException("404 Response");
			} else {
				System.out.println(response.getBody());
				throw new ApiException(String.format("Got response code %d", response.getStatus()));
			}
		} catch (UnirestException | RuntimeException e) {
			throw new ApiException(e);
		}
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
				
				// Get staff viewers
				int count = chatters.getJSONArray("staff").length();
				for (int i = 0; i < count; i++) {
					names.add(chatters.getJSONArray("staff").get(i).toString());
				}
				
				// Get admin viewers
				count = chatters.getJSONArray("admins").length();
				for (int i = 0; i < count; i++) {
					names.add(chatters.getJSONArray("admins").get(i).toString());
				}
				
				// Get global moderator viewers
				count = chatters.getJSONArray("global_mods").length();
				for (int i = 0; i < count; i++) {
					names.add(chatters.getJSONArray("global_mods").get(i).toString());
				}
				
				// Get moderator viewers
				count = chatters.getJSONArray("moderators").length();
				for (int i = 0; i < count; i++) {
					names.add(chatters.getJSONArray("moderators").get(i).toString());
				}
				
				// Get normal viewers
				count = chatters.getJSONArray("viewers").length();
				for (int i = 0; i < count; i++) {
					names.add(chatters.getJSONArray("viewers").get(i).toString());
				}
				
				return names;
			} 
		} catch (UnirestException | RuntimeException e) {
			e.printStackTrace();
		}
		
		throw new ApiException("Error fetching chatter list from TMI");
	}
}
