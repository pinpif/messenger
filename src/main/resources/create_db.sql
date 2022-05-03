create table account(
    id bigserial primary key,
    login text unique not null,
	password text not null,
	registration_date timestamp not null,
	status bool not null,
	mail text
);

create table users(
    id bigserial primary key,
	name text,
	age int,
	account_id bigint references account(id) not null,
	status_id bigint references user_status(id) not null
);

create table user_status(
    id bigserial primary key,
	date timestamp not null,
	state bool default false not null
);

create table recovery_password(
    id bigserial primary key,
	code text not null,
	account_id bigint references account(id) not null,
	created_date timestamp not null default current_timestamp
);

create table chat_user(
    id bigserial primary key,
	chat_id bigint references chat(id) not null,
	user_id bigint references users(id) not null,
	added_date timestamp not null default current_timestamp
);

create table session(
    id bigserial primary key,
    added_date timestamp not null,
    session_id text not null,
    user_id bigint references users(id) not null
);

create table chat(
    id bigserial primary key,
	created timestamp not null,
	title text,
	author_id bigserial references users(id) not null,
	type text not null
);

create table message(
    id bigserial primary key,
	chat_id bigint references chat(id) not null,
	author_id bigint references users(id) not null,
	message text,
	content bigint references content(id),
	date timestamp not null
);

create table content(
    id bigserial primary key,
	name text not null,
	content bytea not null
);

create table message_state(
    id bigserial primary key,
	state bool not null default false,
	message_id bigint references message(id) not null,
	user_id bigint references users(id) not null
);

create table contacts(
    id bigserial primary key,
	user_id bigint references users(id),
	contact_id bigint references users(id)
);

create table block_list(
    id bigserial primary key,
	user_id bigint not null references users(id),
	blocked_user_id bigint not null references users(id),
	block_date timestamptz not null default current_timestamp
);