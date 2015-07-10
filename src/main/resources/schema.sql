CREATE TABLE IF NOT EXISTS follower (
	follower_name varchar(30) not null primary key,
	is_follower boolean, 
	was_seen boolean default true
);

CREATE TABLE IF NOT EXISTS datapoints (
	point_timestamp timestamp,
	chat_count int default 0,
	follower_count bigint default 0,
	subscriber_count int default 0
);