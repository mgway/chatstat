package com.matthewgalloway.stats.db;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.matthewgalloway.stats.domain.Viewer;
import com.matthewgalloway.stats.framework.Queryable;

public class ViewerQuery implements Queryable<Viewer> {

	private Viewer viewer;
	
	public ViewerQuery(Viewer viewer) {
		this.viewer = viewer;
	}
	
	@Override
	public Viewer query(JdbcTemplate template) {
		this.viewer = template.queryForObject("SELECT * from viewer where viewer_name = ? and streamer_name = ?", 
			new Object[] {viewer.getName(), viewer.getStreamer()}, new RowMapper<Viewer>() {

				@Override
				public Viewer mapRow(ResultSet rs, int rowNum) throws SQLException {
					Viewer viewer = new Viewer();
					viewer.setName(rs.getString("viewer_name"));
					viewer.setStreamer(rs.getString("streamer_name"));
					viewer.setFollower(rs.getBoolean("is_follower"));
					viewer.setSubscriber(rs.getBoolean("is_subscriber"));
					viewer.setCreateDate(rs.getTimestamp("create_date"));
					viewer.setUpdateDate(rs.getTimestamp("update_date"));
					return viewer;
				}
			});
		return this.viewer;
	}
}
