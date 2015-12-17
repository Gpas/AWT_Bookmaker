ALTER TABLE `bookmaker`.`condition`
ADD COLUMN `params` VARCHAR(32) NULL DEFAULT NULL COMMENT '' AFTER `leadingTeamId`;
