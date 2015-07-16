package com.matthewgalloway.stats.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class Datapoint implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String streamer;
	private String status;
	private String game;
	private String comment;
	private int followerCount;
	private int subscriberCount;
	private int chatterCount;
	private int viewerCount;
	private boolean isPartner;
	private Timestamp createDate;

	public Datapoint() {}
	
	public Datapoint(String streamer) {
		this.streamer = streamer;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(int followerCount) {
		this.followerCount = followerCount;
	}

	public int getSubscriberCount() {
		return subscriberCount;
	}

	public void setSubscriberCount(int subscriberCount) {
		this.subscriberCount = subscriberCount;
	}

	public int getChatterCount() {
		return chatterCount;
	}

	public void setChatterCount(int chatterCount) {
		this.chatterCount = chatterCount;
	}

	public int getViewerCount() {
		return viewerCount;
	}

	public void setViewerCount(int viewerCount) {
		this.viewerCount = viewerCount;
	}

	public boolean isPartner() {
		return isPartner;
	}

	public void setPartner(boolean isPartner) {
		this.isPartner = isPartner;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

}
