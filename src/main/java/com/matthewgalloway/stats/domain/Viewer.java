package com.matthewgalloway.stats.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Viewer implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name; 
	private String streamer;
	private boolean isSubscriber;
	private boolean isFollower;
	private Timestamp createDate;
	private Timestamp updateDate;

	public Viewer() {}
	
	public Viewer(String name, String streamer, boolean isFollower) {
		this.name = name;
		this.isFollower = isFollower;
		this.streamer = streamer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFollower() {
		return isFollower;
	}

	public void setFollower(boolean isFollower) {
		this.isFollower = isFollower;
	}

	@JsonIgnore
	public String getStreamer() {
		return streamer;
	}

	public void setStreamer(String streamer) {
		this.streamer = streamer;
	}

	public boolean isSubscriber() {
		return isSubscriber;
	}

	public void setSubscriber(boolean isSubscriber) {
		this.isSubscriber = isSubscriber;
	}

	@JsonIgnore
	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	@JsonIgnore
	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	
}
