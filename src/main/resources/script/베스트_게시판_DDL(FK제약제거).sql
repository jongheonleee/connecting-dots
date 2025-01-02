CREATE TABLE `best_view_board` (
	`seq`	int	NOT NULL,
	`bno`	int	NOT NULL,
	`appl_begin`	datetime	NOT NULL,
	`appl_end`	datetime	NOT NULL,
	`comt`	varchar(500)	NULL,
	`reg_date`	datetime	NULL,
	`reg_id`	varchar(50)	NULL,
	`up_date`	datetime	NULL,
	`up_id`	varchar(50)	NULL
);

CREATE TABLE `best_comment_board` (
	`seq`	int	NOT NULL,
	`bno`	int	NOT NULL,
	`appl_begin`	datetime	NOT NULL,
	`appl_end`	datetime	NOT NULL,
	`comt`	varchar(2500)	NULL,
	`reg_date`	datetime	NULL,
	`reg_id`	varchar(50)	NULL,
	`up_date`	datetime	NULL,
	`up_id`	varchar(50)	NULL
);

CREATE TABLE `best_like_board` (
	`seq`	int	NOT NULL,
	`bno`	int	NOT NULL,
	`appl_begin`	datetime	NOT NULL,
	`appl_end`	datetime	NOT NULL,
	`comt`	varchar(500)	NULL,
	`reg_date`	datetime	NULL,
	`reg_id`	varchar(50)	NULL,
	`up_date`	datetime	NULL,
	`up_id`	varchar(50)	NULL
);

ALTER TABLE `best_view_board` ADD CONSTRAINT `PK_BEST_VIEW_BOARD` PRIMARY KEY (
	`seq`,
	`bno`
);

ALTER TABLE `best_comment_board` ADD CONSTRAINT `PK_BEST_COMMENT_BOARD` PRIMARY KEY (
	`seq`,
	`bno`
);

ALTER TABLE `best_like_board` ADD CONSTRAINT `PK_BEST_LIKE_BOARD` PRIMARY KEY (
	`seq`,
	`bno`
);

