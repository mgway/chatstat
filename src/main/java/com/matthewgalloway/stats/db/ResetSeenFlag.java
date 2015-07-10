package com.matthewgalloway.stats.db;

import org.springframework.jdbc.core.JdbcTemplate;

import com.matthewgalloway.stats.framework.Executable;

public class ResetSeenFlag implements Executable {
	
	public void execute(JdbcTemplate template) {
		template.update("UPDATE follower SET was_seen = false");
	}
}
