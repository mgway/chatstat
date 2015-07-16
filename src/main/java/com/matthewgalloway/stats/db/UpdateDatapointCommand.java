package com.matthewgalloway.stats.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.matthewgalloway.stats.domain.Datapoint;
import com.matthewgalloway.stats.framework.Executable;

public class UpdateDatapointCommand implements Executable {
	
	private Datapoint point;
	
	public UpdateDatapointCommand(Datapoint point) {
		this.point = point;
	}
	
	public void execute(JdbcTemplate template) {
		template.update("UPDATE datapoint SET viewer_count = ? WHERE datapoint_id = ?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, point.getViewerCount());
				ps.setLong(2, point.getId());
			}
		});
	}
}
