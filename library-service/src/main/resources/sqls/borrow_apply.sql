CREATE TABLE study.borrow_apply (
	id varchar(100) not null primary key,
	user_id varchar(100) not NULL,
	book_count INT not null,
	apply_days INT NOT NULL,
	status varchar(100) NULL default 'init',
	create_time DATETIME NOT NULL,
	update_time DATETIME NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;
