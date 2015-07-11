package com.matthewgalloway.stats.db;
import org.springframework.jdbc.core.JdbcTemplate;

import com.matthewgalloway.stats.domain.Viewer;
import com.matthewgalloway.stats.framework.Queryable;

public class IsViewerFollowing implements Queryable<Boolean> {

	private Viewer viewer;
	
	public IsViewerFollowing(Viewer viewer) {
		this.viewer = viewer;
	}
	
	@Override
	public Boolean query(JdbcTemplate template) {
		return template.queryForObject("SELECT is_follower from viewer where viewer_name = ? and streamer_name = ?", 
			new Object[] {viewer.getName(), viewer.getStreamer()}, Boolean.class);
	}
}
