CREATE TABLE IF NOT EXISTS viewer (
	viewer_name varchar(30) not null,
	streamer_name varchar(30) not null,
	is_follower boolean default false,
	PRIMARY KEY(viewer_name, streamer_name)
);
