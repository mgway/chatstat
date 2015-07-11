CREATE TABLE IF NOT EXISTS viewer (
	viewer_name varchar(30) not null primary key,
	streamer_name varchar(30) not null,
	is_follower boolean
);
