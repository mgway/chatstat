package com.matthewgalloway.stats.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.matthewgalloway.stats.framework.Executable;

public class InsertFollower implements Executable {
	
	private String username;
	private boolean isFollowing;
	
	public InsertFollower(String username, boolean isFollowing) {
		this.username = username;
		this.isFollowing = isFollowing;
	}
	
	public void execute(JdbcTemplate template) {
		final String username = this.username;
		final boolean isFollowing = this.isFollowing;
		
		template.update("MERGE INTO follower (follower_name, is_follower) VALUES (?, ?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, username);
				ps.setBoolean(2, isFollowing);
			}
		});
	}
}
