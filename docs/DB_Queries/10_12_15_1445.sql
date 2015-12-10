ALTER TABLE `bookmaker`.`bet`
DROP FOREIGN KEY `condition`;
ALTER TABLE `bookmaker`.`bet`
CHANGE COLUMN `condition` `conditionId` INT(11) NULL DEFAULT NULL COMMENT '' ;
ALTER TABLE `bookmaker`.`bet`
ADD CONSTRAINT `condition`
  FOREIGN KEY (`conditionId`)
  REFERENCES `bookmaker`.`t_condition` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;