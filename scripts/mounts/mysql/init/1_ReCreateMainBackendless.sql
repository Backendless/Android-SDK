SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `main_backendless` ;
CREATE SCHEMA IF NOT EXISTS `main_backendless` ;
USE `main_backendless` ;

-- -----------------------------------------------------
-- Table `main_backendless`.`Application`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `main_backendless`.`Application` ;

CREATE TABLE IF NOT EXISTS `main_backendless`.`Application` (
  `id` VARCHAR(100) NOT NULL,
  `linuxUserId` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `created` DATETIME NULL,
  `subscriptionId` VARCHAR(100) NULL,
  `fpSubscriptionId` VARCHAR(100) NULL,
  `originDomains` VARCHAR(2000) NOT NULL DEFAULT '*',
  `customerDomain` VARCHAR(500) NULL,
  `showKitApiKey` VARCHAR(100) NULL,
  `gitSupport` TINYINT(1) NULL DEFAULT 0,
  `version` VARCHAR(45) NOT NULL,
  `dbVersion` INT NOT NULL,
  `lastDayOfUse` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ownerDeveloperId` VARCHAR(36) NOT NULL,
  `dbReadonly` BOOLEAN NOT NULL DEFAULT false,
  `shardName` VARCHAR(100) NULL,
  `zoneId` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `fk_Application_ClusterZone` (`zoneId`),
  CONSTRAINT `fk_Application_ClusterZone` FOREIGN KEY (`zoneId`) REFERENCES `ClusterZone` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  UNIQUE INDEX `fpSubscriptionId_UNIQUE` (`fpSubscriptionId` ASC),
  UNIQUE INDEX `customerDomain_UNIQUE` (`customerDomain` ASC),
  UNIQUE INDEX `linuxUserId_UNQIDX` (`linuxUserId` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `main_backendless`.`ClusterZone`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `main_backendless`.`ClusterZone`;

CREATE TABLE IF NOT EXISTS `main_backendless`.`ClusterZone` (
  `id` INT NOT NULL,
  `code` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `domain` VARCHAR(255) NOT NULL,
  `description` TEXT NULL,
  `iconURL` VARCHAR(500),
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `main_backendless`.`DeveloperStatus`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `main_backendless`.`DeveloperStatus` ;

CREATE TABLE IF NOT EXISTS `main_backendless`.`DeveloperStatus` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `main_backendless`.`Developer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `main_backendless`.`Developer` ;

CREATE TABLE IF NOT EXISTS `main_backendless`.`Developer` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(60) NULL,
  `developerStatusId` VARCHAR(100) NOT NULL,
  `lastLogin` DATETIME NULL,
  `registrationDate` DATETIME NULL,
  `companyName` VARCHAR(45) NULL,
  `phone` VARCHAR(45) NULL,
  `system` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_Developer_DeveloperStatus1_idx` (`developerStatusId` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  CONSTRAINT `fk_Developer_DeveloperStatus1`
    FOREIGN KEY (`developerStatusId`)
    REFERENCES `main_backendless`.`DeveloperStatus` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `main_backendless`.`AppToDeveloper`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `main_backendless`.`AppToDeveloper` ;

CREATE TABLE IF NOT EXISTS `main_backendless`.`AppToDeveloper` (
  `applicationId` VARCHAR(100) NOT NULL,
  `developerId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`developerId`, `applicationId`),
  INDEX `fk_Application_has_Developer_Developer1_idx` (`developerId` ASC),
  INDEX `fk_Application_has_Developer_Application_idx` (`applicationId` ASC),
  CONSTRAINT `fk_Application_has_Developer_Application0`
    FOREIGN KEY (`applicationId`)
    REFERENCES `main_backendless`.`Application` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Application_has_Developer_Developer10`
    FOREIGN KEY (`developerId`)
    REFERENCES `main_backendless`.`Developer` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `main_backendless`.`DeveloperOperation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `main_backendless`.`DeveloperOperation` ;

CREATE TABLE IF NOT EXISTS `main_backendless`.`DeveloperOperation` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `main_backendless`.`MailAction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `main_backendless`.`MailAction` ;

CREATE TABLE IF NOT EXISTS `main_backendless`.`MailAction` (
  `id` VARCHAR(100) NOT NULL,
  `actionType` INT NOT NULL,
  `creationTime` DATETIME NULL,
  `developerId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_MailAction_Developer1_idx` (`developerId` ASC),
  CONSTRAINT `fk_MailAction_Developer10`
    FOREIGN KEY (`developerId`)
    REFERENCES `main_backendless`.`Developer` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `main_backendless`.`SocialDeveloper`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `main_backendless`.`SocialDeveloper` ;

CREATE TABLE IF NOT EXISTS `main_backendless`.`SocialDeveloper` (
  `socialId` VARCHAR(100) NOT NULL,
  `userTypeId` INT NOT NULL,
  `developerId` VARCHAR(100) NOT NULL,
  INDEX `fk_SocialDeveloper_Developer1_idx` (`developerId` ASC),
  PRIMARY KEY (`userTypeId`, `socialId`),
  CONSTRAINT `fk_SocialDeveloper_Developer10`
    FOREIGN KEY (`developerId`)
    REFERENCES `main_backendless`.`Developer` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `main_backendless`.`DataBase`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `main_backendless`.`DataBase` ;

CREATE TABLE IF NOT EXISTS `main_backendless`.`DataBase` (
  `id` INT NOT NULL,
  `host` VARCHAR(45) NULL,
  `port` INT NULL,
  `username` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `main_backendless`.`DeveloperPermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `main_backendless`.`DeveloperPermission` ;

CREATE TABLE IF NOT EXISTS `main_backendless`.`DeveloperPermission` (
  `developerId` VARCHAR(100) NOT NULL,
  `developerOperationId` VARCHAR(100) NOT NULL,
  `applicationId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  `visible` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`developerId`, `developerOperationId`, `applicationId`),
  INDEX `fk_Developer_has_DeveloperOperation_DeveloperOperation1` (`developerOperationId` ASC),
  INDEX `fk_Developer_has_DeveloperOperation_Developer1` (`developerId` ASC),
  INDEX `fk_DeveloperPermission_Application1` (`applicationId` ASC),
  CONSTRAINT `fk_Developer_has_DeveloperOperation_Developer1`
    FOREIGN KEY (`developerId`)
    REFERENCES `main_backendless`.`Developer` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Developer_has_DeveloperOperation_DeveloperOperation1`
    FOREIGN KEY (`developerOperationId`)
    REFERENCES `main_backendless`.`DeveloperOperation` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_DeveloperPermission_Application1`
    FOREIGN KEY (`applicationId`)
    REFERENCES `main_backendless`.`Application` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `main_backendless`.`VisibilityGroupToDeveloper`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `main_backendless`.`VisibilityGroupToDeveloper` ;

CREATE TABLE IF NOT EXISTS `main_backendless`.`VisibilityGroupToDeveloper` (
  `developerId` VARCHAR(100) NOT NULL,
  `groupName` VARCHAR(45) NOT NULL,
  `applicationId` VARCHAR(100) NOT NULL,
  `visible` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`groupName`, `developerId`, `applicationId`),
  CONSTRAINT `fk_developerId_developer1`
      FOREIGN KEY (`developerId`)
      REFERENCES `main_backendless`.`AppToDeveloper` (`developerId`)
      ON DELETE CASCADE
      ON UPDATE NO ACTION,
  CONSTRAINT `fk_applicationId_application1`
      FOREIGN KEY (`applicationId`)
      REFERENCES `main_backendless`.`AppToDeveloper` (`applicationId`)
      ON DELETE CASCADE
      ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `visibilityGroup_UNIQUE` ON `main_backendless`.`VisibilityGroupToDeveloper` (`groupName`, `developerId`, `applicationId`);



-- -----------------------------------------------------
-- Table `main_backendless`.`Version`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `main_backendless`.`Version` ;

CREATE TABLE IF NOT EXISTS `main_backendless`.`Version` (
  `main` BIGINT NOT NULL,
  `application` BIGINT NULL,
  PRIMARY KEY (`main`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `main_backendless`.`ExternalHostBlack`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `main_backendless`.`ExternalHostBlack` ;

CREATE TABLE IF NOT EXISTS `main_backendless`.`ExternalHostBlack` (
  `url` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`url`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `main_backendless`.`ExternalHostWhite`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `main_backendless`.`ExternalHostWhite` ;

CREATE TABLE IF NOT EXISTS `main_backendless`.`ExternalHostWhite` (
  `url` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`url`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `main_backendless`.`Referral`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `main_backendless`.`Referral` ;

CREATE TABLE IF NOT EXISTS `main_backendless`.`Referral` (
  `developerId` VARCHAR(100) NOT NULL,
  `code` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`developerId`),
  CONSTRAINT `fk_Referral_Developer1`
    FOREIGN KEY (`developerId`)
    REFERENCES `main_backendless`.`Developer` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
