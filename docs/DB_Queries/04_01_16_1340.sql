ALTER TABLE `bookmaker`.`condition`
ADD COLUMN `occurred` BIT(1) NULL DEFAULT NULL COMMENT '' AFTER `odd`;

ALTER TABLE `bookmaker`.`game` 
ADD COLUMN `closed` BIT(1) NULL DEFAULT FALSE COMMENT '' AFTER `owner`;
