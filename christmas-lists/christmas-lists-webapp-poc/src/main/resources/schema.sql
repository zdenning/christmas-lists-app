drop table USERS if exists;
create table USERS (
	USERNAME varchar NOT NULL,
	PASSWORD varchar NOT NULL,
	FIRST_NAME varchar NOT NULL,
	PRIMARY KEY (USERNAME));

drop table ITEMS if exists;
create table ITEMS (USERNAME varchar NOT NULL,
	NAME varchar NOT NULL,
	NOTES varchar,
	BOUGHT boolean NOT NULL default 0);
	
create unique index UNQ_IDX_ITEMS on ITEMS (USERNAME, NAME);
