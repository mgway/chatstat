package com.matthewgalloway.stats.db;
import org.springframework.jdbc.core.JdbcTemplate;

import com.matthewgalloway.stats.framework.Queryable;

public class FollowersInChatQuery implements Queryable<Long> {
	
	
	@Override
	public Long query(JdbcTemplate template) {
		return template.queryForObject("SELECT count(*) from follower WHERE is_follower = true AND was_seen = true", Long.class);
	}
}
