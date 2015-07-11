package com.matthewgalloway.stats.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseService {

	@Autowired
	private transient JdbcTemplate template;
	
	public <T> T query(Queryable<T> obj){
		return obj.query(this.template);
	}
	
	@Transactional(readOnly = false)
	public void execute(Executable obj){
		obj.execute(this.template);
	}
}
