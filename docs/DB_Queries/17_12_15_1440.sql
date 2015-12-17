ALTER TABLE `bookmaker`.`condition`
DROP COLUMN `oddBet`,
CHANGE COLUMN `oddGain` `odd` INT(11) NULL DEFAULT NULL COMMENT '' ;
