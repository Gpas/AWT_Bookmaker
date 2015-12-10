

ALTER TABLE `bookmaker`.`t_condition`
RENAME TO  `bookmaker`.`condition` ;

ALTER TABLE `bookmaker`.`bet`
DROP FOREIGN KEY `condition`;
ALTER TABLE `bookmaker`.`bet`
CHANGE COLUMN `conditionId` `condition` INT(11) NULL DEFAULT NULL COMMENT '' ;
ALTER TABLE `bookmaker`.`bet`
ADD CONSTRAINT `condition`
FOREIGN KEY (`condition`)
REFERENCES `bookmaker`.`condition` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
-- Wieder zurückbenannt, da ein Weg gefunden mit Hibernate -> name=[name]