package com.matthewgalloway.stats.db;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.matthewgalloway.stats.framework.Queryable;

public class IsUserFollowingQuery implements Queryable<Boolean> {

	private String username;
	
	public IsUserFollowingQuery(String username) {
		this.username = username;
	}
	
	@Override
	public Boolean query(JdbcTemplate template) {
		try {
			template.queryForObject("SELECT 1 from follower where follower_name = ?", 
				new Object[] {this.username}, Long.class);
			return true;
		} catch (EmptyResultDataAccessException ex) {
			return false;
		}
	}
}
