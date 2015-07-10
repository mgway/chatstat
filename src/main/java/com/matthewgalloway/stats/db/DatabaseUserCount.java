package com.matthewgalloway.stats.db;
import org.springframework.jdbc.core.JdbcTemplate;

import com.matthewgalloway.stats.framework.Queryable;

public class DatabaseUserCount implements Queryable<Long> {

	@Override
	public Long query(JdbcTemplate template) {
		return template.queryForObject("SELECT count(*) from follower", Long.class); 
	}
}
