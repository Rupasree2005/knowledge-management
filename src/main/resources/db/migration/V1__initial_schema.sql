CREATE TABLE knowledge_documents (
                                     id BIGINT NOT NULL AUTO_INCREMENT,
                                     category VARCHAR(255) DEFAULT NULL,
                                     content TEXT NOT NULL,
                                     created_at DATETIME(6) DEFAULT NULL,
                                     title VARCHAR(255) NOT NULL,
                                     updated_at DATETIME(6) DEFAULT NULL,
                                     PRIMARY KEY (id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(255) NOT NULL,
                       username VARCHAR(255) NOT NULL,
                       PRIMARY KEY (id),
                       UNIQUE KEY UK_users_username (username)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;