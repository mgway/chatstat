package com.matthewgalloway.stats.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.matthewgalloway.stats.framework.Executable;

public class InsertDatapointStub implements Executable {
	final String INSERT_SQL = "INSERT INTO datapoint (streamer_name, create_date) VALUES (?, CURRENT_TIMESTAMP)";

	private String streamerName;
	private long id;
	
	public InsertDatapointStub(String streamerName) {
		this.streamerName = streamerName;
	}
	
	public String getStreamerName() {
		return streamerName;
	}

	public void setStreamerName(String streamerName) {
		this.streamerName = streamerName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void execute(JdbcTemplate template) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_SQL, new String[] {"datapoint_id"});
	            ps.setString(1, streamerName);
	            return ps;
			}
	    },
	    keyHolder);
		
		this.id = keyHolder.getKey().longValue();
	}
}
