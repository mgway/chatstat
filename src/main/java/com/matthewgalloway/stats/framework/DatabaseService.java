package com.matthewgalloway.stats.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

	@Autowired
	private transient JdbcTemplate template;
	
	public <T> T query(Queryable<T> obj){
		return obj.query(this.template);
	}
	
	public void execute(Executable obj){
		obj.execute(this.template);
	}
}
