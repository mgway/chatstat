package com.matthewgalloway.stats.db;
import org.springframework.jdbc.core.JdbcTemplate;

import com.matthewgalloway.stats.framework.Queryable;

public class IsViewerFollowing implements Queryable<Boolean> {

	private String username;
	private String streamerName;
	
	public IsViewerFollowing(String username, String streamerName) {
		this.username = username;
	}
	
	@Override
	public Boolean query(JdbcTemplate template) {
		return template.queryForObject("SELECT is_follower from viewer where viewer_name = ? and streamer_name = ?", 
			new Object[] {this.username, this.streamerName}, Boolean.class);
	}
}
