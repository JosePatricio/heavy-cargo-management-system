CREATE TABLE request
(
  request_id                  INT AUTO_INCREMENT PRIMARY KEY,
  request_remote_ip           VARCHAR(100) NULL,
  request_method              VARCHAR(100) NULL,
  request_operation_name      VARCHAR(100) NULL,
  request_path                VARCHAR(100) NULL,
  request_uri                 VARCHAR(300) NULL,
  request_user                VARCHAR(100) NULL,
  request_user_agent          VARCHAR(300) NULL,
  request_date                DATETIME NULL
);