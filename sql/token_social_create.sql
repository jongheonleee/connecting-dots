DROP TABLE  IF EXISTS `user_token`;
CREATE TABLE `user_token` (
                              `token_id`	            VARCHAR(255)	NOT NULL,
                              `user_id`	                bigint	NOT NULL,
                              `access_token`	        VARCHAR(255)	NULL,
                              `refresh_token`	        VARCHAR(255)	NULL,
                              `access_token_expires_at`	datetime	NULL,
                              `refresh_token_expires_at`datetime	NULL,
                              `created_at`	            datetime	NULL,
                              `created_by`	            VARCHAR(255)	NULL,
                              `modified_at`         	datetime	NULL,
                              `modified_by`	            VARCHAR(255)	NULL
);

CREATE TABLE `social_user` (
                               `social_user_id`	VARCHAR(255)	NOT NULL	COMMENT 'uuid로 저장',
                               `user_name`	        VARCHAR(255)	NULL,
                               `provider`	        VARCHAR(255)	NULL	COMMENT '구글, 카카오, 네이버 ... 등',
                               `provider_id`	    VARCHAR(255)	NULL,
                               `created_at`	    datetime	NULL,
                               `created_by`	    VARCHAR(255)	NULL,
                               `modified_at`	    datetime	NULL,
                               `modified_by`	    VARCHAR(255)	NULL
);

ALTER TABLE `user_token` ADD CONSTRAINT `PK_TOKENS` PRIMARY KEY (
                                                                 `token_id`
    );

ALTER TABLE `social_user` ADD CONSTRAINT `PK_SOCIAL_USERS` PRIMARY KEY (
                                                                        `social_user_id`
    );