CREATE TABLE dynamic_process_record (
	id varchar(100) NOT NULL,
	process_type varchar(100) NOT NULL COMMENT '流程图的类型区分',
	business_key TEXT NOT NULL COMMENT '业务主键，可以区分不同的流程图',
	process_definition_id varchar(100) NOT NULL,
	create_time DATETIME NOT NULL,
	update_time DATETIME NOT NULL,
	CONSTRAINT dynamic_process_info_pk PRIMARY KEY (id)
) ;
