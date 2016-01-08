ALTER TABLE `bookmaker`.`bet`
CHANGE COLUMN `amount` `amount` DECIMAL(12,2) NULL DEFAULT NULL COMMENT '' ;

ALTER TABLE `bookmaker`.`user`
CHANGE COLUMN `balance` `balance` DECIMAL(12,2) NULL DEFAULT NULL COMMENT '' ;
