ALTER TABLE `bookmaker`.`condition`
ADD COLUMN `occurred` BIT(1) NULL DEFAULT NULL COMMENT '' AFTER `odd`;
