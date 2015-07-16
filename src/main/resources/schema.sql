CREATE TABLE IF NOT EXISTS viewer (
	viewer_name varchar(30) not null,
	streamer_name varchar(30) not null,
	is_follower boolean default false,
	is_subscriber boolean default false,
	create_date timestamp,
	update_date timestamp,
	PRIMARY KEY(viewer_name, streamer_name)
);

CREATE TABLE IF NOT EXISTS datapoint (
	datapoint_id identity not null,
	streamer_name varchar(30) not null,
	viewer_count int default 0,
	chatter_count int default 0,
	follower_count int default 0,
	subscriber_count int default 0,
	game_name varchar(200),
	status varchar(200),
	comment varchar(200),
	create_date timestamp
);

CREATE TABLE IF NOT EXISTS streamer (
	streamer_name varchar(30) not null primary key,
	is_partner boolean default false,
	api_token varchar(50),
	create_date timestamp,
	update_date timestamp,
);
