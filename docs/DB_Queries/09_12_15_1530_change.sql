-- leadingTeamId ist nötig
ALTER TABLE `bookmaker`.`condition`
ADD COLUMN `leadingTeamId` INT(11) NULL DEFAULT NULL COMMENT '' AFTER `textId`;

-- condition ist ein sql befehl, kann zu fehler führen, darum t_condtion
ALTER TABLE `bookmaker`.`condition`
RENAME TO  `bookmaker`.`t_condition` ;

-- Um Duplicates mit diesen Namen zu löschen
SET foreign_key_checks = 0;
DELETE FROM user where username='Testuser';
DELETE FROM user where username='test@user.com';
SET foreign_key_checks = 1;

-- username ist nun unique, damit nicht zwei user den gleichen namen haben können
ALTER TABLE `bookmaker`.`user`
ADD UNIQUE INDEX `username_UNIQUE` (`username` ASC)  COMMENT '';
