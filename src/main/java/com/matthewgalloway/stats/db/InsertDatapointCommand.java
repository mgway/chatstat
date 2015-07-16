package com.matthewgalloway.stats.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.matthewgalloway.stats.domain.Datapoint;
import com.matthewgalloway.stats.framework.Executable;

public class InsertDatapointCommand implements Executable {
	final String INSERT_SQL = "INSERT INTO datapoint (streamer_name, game_name, status, create_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

	private Datapoint datapoint;
	
	public InsertDatapointCommand(Datapoint datapoint) {
		this.datapoint = datapoint;
	}

	public void execute(JdbcTemplate template) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_SQL, new String[] {"datapoint_id"});
				ps.setString(1, datapoint.getStreamer());
				ps.setString(2, datapoint.getGame());
				ps.setString(3, datapoint.getStatus());
	            return ps;
			}
	    },
	    keyHolder);
		
		this.datapoint.setId(keyHolder.getKey().longValue());
	}
}
