package com.matthewgalloway.stats.framework;

import org.springframework.jdbc.core.JdbcTemplate;

public interface Queryable<T> {
	public T query(JdbcTemplate template); 
}
