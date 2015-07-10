package com.matthewgalloway.stats.framework;

import org.springframework.jdbc.core.JdbcTemplate;

public interface Executable {
	public void execute(JdbcTemplate template);
}
