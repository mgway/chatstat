package com.matthewgalloway.stats.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.matthewgalloway.stats.domain.Viewer;
import com.matthewgalloway.stats.framework.Executable;

public class InsertViewer implements Executable {
	
	private Viewer viewer;
	
	public InsertViewer(Viewer viewer) {
		this.viewer = viewer;
	}
	
	public void execute(JdbcTemplate template) {
		template.update("MERGE INTO viewer (viewer_name, is_follower, streamer_name) VALUES (?, ?, ?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, viewer.getName());
				ps.setBoolean(2, viewer.isFollower());
				ps.setString(3, viewer.getStreamer());
			}
		});
	}
}
