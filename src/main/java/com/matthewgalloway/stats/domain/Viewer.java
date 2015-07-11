package com.matthewgalloway.stats.domain;

public class Viewer {
	private String name; 
	private boolean isFollower;
	private String streamer;

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

	public String getStreamer() {
		return streamer;
	}

	public void setStreamer(String streamer) {
		this.streamer = streamer;
	}
}
