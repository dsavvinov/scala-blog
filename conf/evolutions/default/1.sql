# Initial tables creation

# --- !Ups
CREATE TABLE `post` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `title` TEXT NOT NULL,
  `author` BIGINT NOT NULL,
  `publish_date` DATE NOT NULL,
  `content` TEXT NOT NULL,
  `description` TEXT NOT NULL
);

CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` TEXT NOT NULL,
  `login` TEXT NOT NULL,
  `password` TEXT NOT NULL
);

# --- !Downs
DROP TABLE IF EXISTS `post`;
DROP TABLE IF EXISTS `user`;
