-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema bookmaker
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema bookmaker
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bookmaker` DEFAULT CHARACTER SET utf8 ;
USE `bookmaker` ;

-- -----------------------------------------------------
-- Table `bookmaker`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookmaker`.`user` (
  `id` INT(11) NOT NULL COMMENT '',
  `balance` FLOAT NULL DEFAULT NULL COMMENT '',
  `username` VARCHAR(64) NULL DEFAULT NULL COMMENT '',
  `password` VARCHAR(256) NULL DEFAULT NULL COMMENT '',
  `firstname` VARCHAR(64) NULL DEFAULT NULL COMMENT '',
  `lastname` VARCHAR(64) NULL DEFAULT NULL COMMENT '',
  `isBookmaker` BIT(1) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '')
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bookmaker`.`game`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookmaker`.`game` (
  `id` INT(11) NOT NULL COMMENT '',
  `startTime` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '',
  `homeTeam` INT(11) NULL DEFAULT NULL COMMENT '',
  `guestTeam` INT(11) NULL DEFAULT NULL COMMENT '',
  `owner` INT(11) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  INDEX `owner_idx` (`owner` ASC)  COMMENT '',
  CONSTRAINT `owner`
    FOREIGN KEY (`owner`)
    REFERENCES `bookmaker`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bookmaker`.`condition`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookmaker`.`condition` (
  `id` INT(11) NOT NULL COMMENT '',
  `textId` INT(11) NULL DEFAULT NULL COMMENT '',
  `oddGain` INT(11) NULL DEFAULT NULL COMMENT '',
  `oddBet` INT(11) NULL DEFAULT NULL COMMENT '',
  `game` INT(11) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  INDEX `game_idx` (`game` ASC)  COMMENT '',
  CONSTRAINT `game`
    FOREIGN KEY (`game`)
    REFERENCES `bookmaker`.`game` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bookmaker`.`bet`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookmaker`.`bet` (
  `id` INT(11) NOT NULL COMMENT '',
  `amount` FLOAT NULL DEFAULT NULL COMMENT '',
  `user` INT(11) NULL DEFAULT NULL COMMENT '',
  `condition` INT(11) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  INDEX `condition_idx` (`condition` ASC)  COMMENT '',
  INDEX `user_idx` (`user` ASC)  COMMENT '',
  CONSTRAINT `condition`
    FOREIGN KEY (`condition`)
    REFERENCES `bookmaker`.`condition` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user`
    FOREIGN KEY (`user`)
    REFERENCES `bookmaker`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
