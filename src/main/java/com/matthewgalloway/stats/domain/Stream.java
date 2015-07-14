package com.matthewgalloway.stats.domain;

import java.io.Serializable;

public class Stream implements Serializable {

	private static final long serialVersionUID = 1L;

	private String streamer;
	private String status;
	private String game;
	private String startTime;
	private int totalViewerCount;
	private int totalFollowerCount;
	private boolean isPartner;
	private boolean isMature;

	public Stream(String streamer) {
		this.streamer = streamer;
	}
	
	public String getStreamer() {
		return streamer;
	}

	public void setStreamer(String streamer) {
		this.streamer = streamer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public int getTotalViewerCount() {
		return totalViewerCount;
	}

	public void setTotalViewerCount(int totalViewerCount) {
		this.totalViewerCount = totalViewerCount;
	}

	public int getTotalFollowerCount() {
		return totalFollowerCount;
	}

	public void setTotalFollowerCount(int totalFollowerCount) {
		this.totalFollowerCount = totalFollowerCount;
	}

	public boolean isPartner() {
		return isPartner;
	}

	public void setPartner(boolean isPartner) {
		this.isPartner = isPartner;
	}

	public boolean isMature() {
		return isMature;
	}

	public void setMature(boolean isMature) {
		this.isMature = isMature;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

}
