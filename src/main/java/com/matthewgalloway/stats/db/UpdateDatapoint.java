package com.matthewgalloway.stats.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.matthewgalloway.stats.domain.Viewer;
import com.matthewgalloway.stats.framework.Executable;

public class UpdateDatapoint implements Executable {
	
	private String sql_ammendment;
	private long datapointId;
	
	public UpdateDatapoint(Viewer viewer, long datapointId) {
		this.datapointId = datapointId;
		
		sql_ammendment = "";
		if (viewer.isFollower()) {
			sql_ammendment += ", follower_count = follower_count + 1";
		}
		
		if (viewer.isSubscriber()) {
			sql_ammendment += ", subscriber_count = subscriber_count + 1";
		}
	}
	
	public void execute(JdbcTemplate template) {
		template.update("UPDATE datapoint SET viewer_count = viewer_count + 1 " + sql_ammendment + " WHERE datapoint_id = ?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, datapointId);
			}
		});
	}
}
