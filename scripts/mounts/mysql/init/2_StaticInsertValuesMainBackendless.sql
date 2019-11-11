USE `main_backendless`;

INSERT INTO `Version` (`main`, `application`) values (20, 57);

INSERT INTO `DeveloperStatus` (`id`, `name`) VALUES ('1', 'ACTIVE');
INSERT INTO `DeveloperStatus` (`id`, `name`) VALUES ('2', 'SUSPENDED');
INSERT INTO `DeveloperStatus` (`id`, `name`) VALUES ('3', 'INVITED');

INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('1', 'ADD');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('2', 'REMOVE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('3', 'MODIFY_PERMISSION');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('4', 'REGENERATE_API_KEYS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('5', 'CHANGE_SOCIAL_SETTINGS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('6', 'CHANGE_MOBILE_SETTINGS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('7', 'CHANGE_EMAIL_SETTINGS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('8', 'CHANGE_CORS_DOMAIN_CONTROL');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('9', 'CHANGE_CUSTOM_DOMAIN');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('10', 'ENABLE_DISABLE_GIT');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('11', 'DELETE_APPLICATION');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('12', 'SET_GOOGLE_KEY');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('13', 'APP_RESET');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('14', 'CHANGE_LIMIT_NOTIFICATION');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('16', 'CREATE_REPOSITORY');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('17', 'CHANGE_LOG_CONFIG');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('18', 'DELETE_LOGGERS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('19', 'CHANGE_LOG_INTEGRATIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('20', 'ADD_UPDATE_CREDIT_CARD');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('21', 'CHANGE_BILLING_PLAN');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('22', 'MARKETPLACE_PURCHASE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('23', 'MARKETPLACE_DELETE_PURCHASE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('24', 'EXPORT_APP');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('25', 'IMPORT_ARCHIVE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('26', 'IMPORT_FILE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('27', 'CLONE_APP');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('28', 'IMPORT_FROM_3X');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('29', 'MODIFY_LANDING_PAGE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('30', 'PUBLISH_LANDING_PAGE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('31', 'CHANGE_USER_REGISTRATION_PROPS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('32', 'CHANGE_LOGIN_PROPS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('33', 'MODIFY_EMAIL_TEMPLATE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('34', 'CREATE_EMAIL_TEMPLATE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('35', 'SEND_TEST_EMAIL');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('36', 'DELETE_EMAIL_TEMPLATE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('37', 'ADD_DELETE_ROLE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('38', 'MODIFY_ROLE_PERMISSIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('39', 'ASSIGN_ROLE_PERMISSIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('40', 'CREATE_TABLE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('41', 'RENAME_DELETE_TABLE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('42', 'CREATE_TABLE_COLUMN');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('43', 'CHANGE_DELETE_TABLE_COLUMN');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('44', 'CHANGE_TABLE_PERMISSIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('45', 'CHANGE_OBJECT_ACL_PERMISSIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('46', 'CREATE_DELETE_UPDATE_OBJECTS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('47', 'CHANGE_DYNAMIC_SCHEMA_DEFINITION');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('48', 'CHANGE_DATA_RELATIONSHIP');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('49', 'CHANGE_GEO_RELATIONSHIP');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('50', 'CREATE_DATA_RELATIONSHIP');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('51', 'CREATE_GEO_RELATIONSHIP');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('52', 'DELETE_TABLE_PERMISSIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('53', 'DELETE_OBJECT_ACL_PERMISSIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('54', 'CHANGE_GLOBAL_OWNER_PERMISSIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('55', 'CHANGE_OWNER_PERMISSIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('56', 'DELETE_OWNER_PERMISSIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('57', 'CREATE_DIRECTORY');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('58', 'RENAME_DELETE_DIRECTORY_FILES');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('59', 'UPLOAD_CREATE_FILES');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('60', 'CHANGE_FILE_PERMISSIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('61', 'ZIP_DIRECTORY');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('62', 'UNZIP_FILE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('63', 'EDIT_FILE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('64', 'MOVE_FILE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('65', 'COPY_FILE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('66', 'DELETE_FILE_PERMISSIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('67', 'CREATE_MESSAGING_CHANNEL');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('68', 'RENAME_DELETE_MESSAGING_CHANNEL');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('69', 'SEND_MESSAGE_TO_CHANNEL');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('70', 'CHANGE_MESSAGING_CHANNEL_PERMISSIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('71', 'CREATE_PUSH_TEMPLATE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('72', 'CHANGE_DELETE_PUSH_TEMPLATE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('73', 'CREATE_PUSH_BUTTON_OPTIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('74', 'CHANGE_DELETE_PUSH_BUTTON_OPTIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('75', 'CREATE_MESSAGING_CHANNEL_OPTIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('76', 'CHANGE_DELETE_MESSAGING_CHANNEL_OPTIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('77', 'SEND_PUSH_BY_TEMPLATE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('78', 'DELETE_DEVICES');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('79', 'DELETE_MESSAGING_CHANNEL_PERMISSIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('80', 'CREATE_GEO_CATEGORY');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('81', 'RENAME_DELETE_GEO_CATEGORY');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('82', 'SETUP_SAMPLE_GEO_DATA');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('83', 'CHANGE_GEO_CATEGORY_PERMISSIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('84', 'MODIFY_GEO_POINT_METADATA');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('85', 'CREATE_GEOFENCE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('86', 'MODIFY_DELETE_GEOFENCE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('87', 'ADD_CATEGORY');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('88', 'REMOVE_GEO_POINTS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('89', 'COPY_GEO_POINTS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('90', 'REMOVE_ALL_GEO_POINTS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('91', 'IMPORT_GEO_POINTS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('92', 'DELETE_GEO_CATEGORY_PERMISSIONS');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('93', 'MODIFY_BL');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('94', 'PUBLISH_TO_MARKETPLACE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('95', 'CREATE_MARKETPLACE_CONFIGURATION');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('96', 'RUN_TIMER');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('97', 'INVOKE_EVENT_WITH_MODEL');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('98', 'DELETE_FROM_MARKETPLACE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('99', 'ACTIVATE_DATACONNECTOR');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('100', 'CHANGE_DATACONNECTOR');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('101', 'DELETE_DATACONNECTOR');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('102', 'STORED_PROCEDURE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('103', 'ADD_CHANGE_KEY');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('104', 'DELETE_KEY');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('105', 'DOWNLOAD_FILE');
INSERT INTO `DeveloperOperation` (`id`, `name`) VALUES ('106', 'VIEW_DIRECTORY_CONTENT');


-- MySQL Script generated by MySQL Workbench
-- Mon 11 Dec 2017 02:23:23 PM EET
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

-- -----------------------------------------------------
-- Schema main_application
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `main_application` ;
CREATE SCHEMA IF NOT EXISTS `main_application` ;

USE `main_application` ;

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema main_application
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table `UserType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `UserType` ;

CREATE TABLE IF NOT EXISTS `UserType` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `UserStatus`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `UserStatus` ;

CREATE TABLE IF NOT EXISTS `UserStatus` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `User` ;

CREATE TABLE IF NOT EXISTS `User` (
  `id` VARCHAR(100) NOT NULL,
  `loggedIn` TINYINT(1) NOT NULL DEFAULT 0,
  `failedLoginCount` INT NOT NULL DEFAULT 0,
  `logsCount` INT NOT NULL DEFAULT 0,
  `default` TINYINT(1) NOT NULL DEFAULT 0,
  `activationKey` VARCHAR(100) NULL,
  `userTypeId` INT NOT NULL DEFAULT 1,
  `userStatusId` INT NULL DEFAULT 1,
  `lastLogin` DATETIME NULL,
  `lastTimeReturningCount` DATETIME NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_User_UserType1`
  FOREIGN KEY (`userTypeId`)
  REFERENCES `UserType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_UserStatus1`
  FOREIGN KEY (`userStatusId`)
  REFERENCES `UserStatus` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_User_UserType1_idx` ON `User` (`userTypeId` ASC);

CREATE INDEX `fk_User_UserStatus1_idx` ON `User` (`userStatusId` ASC);


-- -----------------------------------------------------
-- Table `AppHosting`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `AppHosting` ;

CREATE TABLE IF NOT EXISTS `AppHosting` (
  `id` VARCHAR(100) NOT NULL,
  `rootPath` VARCHAR(200) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DeploymentType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DeploymentType` ;

CREATE TABLE IF NOT EXISTS `DeploymentType` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Deployment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Deployment` ;

CREATE TABLE IF NOT EXISTS `Deployment` (
  `id` VARCHAR(100) NOT NULL,
  `deploymentTypeId` VARCHAR(100) NOT NULL,
  `username` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `deploymentURL` VARCHAR(45) NULL,
  `appHostingId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Deployment_DeploymentType1`
  FOREIGN KEY (`deploymentTypeId`)
  REFERENCES `DeploymentType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Deployment_AppHosting1`
  FOREIGN KEY (`appHostingId`)
  REFERENCES `AppHosting` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_Deployment_DeploymentType1_idx` ON `Deployment` (`deploymentTypeId` ASC);

CREATE INDEX `fk_Deployment_AppHosting1_idx` ON `Deployment` (`appHostingId` ASC);


-- -----------------------------------------------------
-- Table `DataColumnType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DataColumnType` ;

CREATE TABLE IF NOT EXISTS `DataColumnType` (
  `id` VARCHAR(100) NOT NULL COMMENT 'possible values:\ninteger\nstring\nboolean\ndatetime\none to many relationship\nmany to many relationsip\none to one relationship',
  `columnType` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DefaultUserProperty`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DefaultUserProperty` ;

CREATE TABLE IF NOT EXISTS `DefaultUserProperty` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `preSelected` TINYINT(1) NOT NULL DEFAULT 0,
  `required` TINYINT(1) NOT NULL DEFAULT 0,
  `identity` TINYINT(1) NOT NULL DEFAULT 0,
  `dataTypeId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_DefaultUserProperty_DataType1`
  FOREIGN KEY (`id`)
  REFERENCES `DataColumnType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ExternalAuthProtocolType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ExternalAuthProtocolType` ;

CREATE TABLE IF NOT EXISTS `ExternalAuthProtocolType` (
  `id` VARCHAR(100) NOT NULL COMMENT 'there are two options Backendless REST and OAth',
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `LoginSettings`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LoginSettings` ;

CREATE TABLE IF NOT EXISTS `LoginSettings` (
  `id` VARCHAR(100) NOT NULL,
  `enableExternalAuth` TINYINT(1) NOT NULL DEFAULT 0,
  `externalAuthURL` VARCHAR(512) NULL,
  `externalAuthProtocolTypeId` VARCHAR(100) NULL,
  `enableMultipleLogin` TINYINT(1) NOT NULL DEFAULT 0,
  `logoutLastUser` TINYINT(1) NOT NULL DEFAULT 1,
  `enableSessionExpiration` TINYINT(1) NOT NULL DEFAULT 0,
  `sessionTimeout` INT NULL,
  `failedLoginsLock` INT NOT NULL DEFAULT 0 COMMENT 'value of 0 indicated unlimited failed logins',
  `unlockWaitingTime` INT NULL,
  `enableLogin` TINYINT(1) NOT NULL DEFAULT 1,
  `mailUserForFirstTimeLogin` TINYINT(1) NOT NULL DEFAULT 0,
  `mailUserForXTimeLogin` INT NOT NULL DEFAULT 0 COMMENT 'if mailUserForXTimeLogin == 0 then do not mail user',
  `maxConcurrentLogins` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_LoginSettings_ExternalAuthTypes1`
  FOREIGN KEY (`externalAuthProtocolTypeId`)
  REFERENCES `ExternalAuthProtocolType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_LoginSettings_ExternalAuthTypes1_idx` ON `LoginSettings` (`externalAuthProtocolTypeId` ASC);


-- -----------------------------------------------------
-- Table `UserRegistrationSettings`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `UserRegistrationSettings` ;

CREATE TABLE IF NOT EXISTS `UserRegistrationSettings` (
  `id` VARCHAR(100) NOT NULL,
  `userRegistration` TINYINT(1) NOT NULL DEFAULT 1 COMMENT 'when false, new user registration is disabled',
  `userEmailConfirmation` TINYINT(1) NOT NULL DEFAULT 1 COMMENT 'when true, email address for new user registration must be confirmed',
  `enableDynamicUserProperties` TINYINT(1) NOT NULL DEFAULT 1,
  `userIdentityColumnId` VARCHAR(36) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Event` ;

CREATE TABLE IF NOT EXISTS `Event` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `subject` VARCHAR(100) NULL,
  `body` TEXT NULL,
  `triggersEmail` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `MessagingChannel`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `MessagingChannel` ;

CREATE TABLE IF NOT EXISTS `MessagingChannel` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `webSocketPort` INT NULL COMMENT 'a special value of 0 sets WebSockets as disabled ',
  `enablePollingAccess` TINYINT(1) NULL,
  `rtmpPort` INT NULL COMMENT 'a special value of 0 sets RTMP as disabled ',
  `connectorId` VARCHAR(45) NOT NULL DEFAULT 'internal',
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;

CREATE UNIQUE INDEX `name_UNIQUE` ON `MessagingChannel` (`name` ASC);


-- -----------------------------------------------------
-- Table `Role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Role` ;

CREATE TABLE IF NOT EXISTS `Role` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(70) NOT NULL,
  `systemRole` TINYINT(1) NOT NULL DEFAULT 0,
  `longValue` BIGINT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;

CREATE UNIQUE INDEX `name_UNIQUE` ON `Role` (`name` ASC);


-- -----------------------------------------------------
-- Table `RoleToUser`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RoleToUser` ;

CREATE TABLE IF NOT EXISTS `RoleToUser` (
  `roleId` VARCHAR(100) NOT NULL,
  `userId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`roleId`, `userId`),
  CONSTRAINT `fk_Role_has_User_Role1`
  FOREIGN KEY (`roleId`)
  REFERENCES `Role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Role_has_User_User1`
  FOREIGN KEY (`userId`)
  REFERENCES `User` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_Role_has_User_User1_idx` ON `RoleToUser` (`userId` ASC);

CREATE INDEX `fk_Role_has_User_Role1_idx` ON `RoleToUser` (`roleId` ASC);


-- -----------------------------------------------------
-- Table `ChannelOperation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ChannelOperation` ;

CREATE TABLE IF NOT EXISTS `ChannelOperation` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PermissionType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PermissionType` ;

CREATE TABLE IF NOT EXISTS `PermissionType` (
  `id` VARCHAR(100) NOT NULL COMMENT 'there are three types: Grant, Deny, Inherit\n',
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `UserChannelPermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `UserChannelPermission` ;

CREATE TABLE IF NOT EXISTS `UserChannelPermission` (
  `userId` VARCHAR(100) NOT NULL,
  `channelOperationId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  `messagingChannelId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`userId`, `channelOperationId`, `messagingChannelId`),
  CONSTRAINT `fk_UserChannelPermission_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserChannelPermission_MessagingChannel1`
  FOREIGN KEY (`messagingChannelId`)
  REFERENCES `MessagingChannel` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserChannelPermission_User1`
  FOREIGN KEY (`userId`)
  REFERENCES `User` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserChannelPermission_ChannelPermission1`
  FOREIGN KEY (`channelOperationId`)
  REFERENCES `ChannelOperation` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_UserChannelPermission_PermissionType1_idx` ON `UserChannelPermission` (`permissionTypeId` ASC);

CREATE INDEX `fk_UserChannelPermission_MessagingChannel1_idx` ON `UserChannelPermission` (`messagingChannelId` ASC);

CREATE INDEX `fk_UserChannelPermission_User1_idx` ON `UserChannelPermission` (`userId` ASC);

CREATE INDEX `fk_UserChannelPermission_ChannelPermission1_idx` ON `UserChannelPermission` (`channelOperationId` ASC);


-- -----------------------------------------------------
-- Table `RoleChannelPermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RoleChannelPermission` ;

CREATE TABLE IF NOT EXISTS `RoleChannelPermission` (
  `roleId` VARCHAR(100) NOT NULL,
  `channelOperationId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  `messagingChannelId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`roleId`, `channelOperationId`, `messagingChannelId`),
  CONSTRAINT `fk_RoleChannelPermission_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RoleChannelPermission_MessagingChannel1`
  FOREIGN KEY (`messagingChannelId`)
  REFERENCES `MessagingChannel` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RoleChannelPermission_Role1`
  FOREIGN KEY (`roleId`)
  REFERENCES `Role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RoleChannelPermission_ChannelPermission1`
  FOREIGN KEY (`channelOperationId`)
  REFERENCES `ChannelOperation` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_RoleChannelPermission_PermissionType1_idx` ON `RoleChannelPermission` (`permissionTypeId` ASC);

CREATE INDEX `fk_RoleChannelPermission_MessagingChannel1_idx` ON `RoleChannelPermission` (`messagingChannelId` ASC);

CREATE INDEX `fk_RoleChannelPermission_Role1_idx` ON `RoleChannelPermission` (`roleId` ASC);

CREATE INDEX `fk_RoleChannelPermission_ChannelPermission1_idx` ON `RoleChannelPermission` (`channelOperationId` ASC);


-- -----------------------------------------------------
-- Table `MediaTube`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `MediaTube` ;

CREATE TABLE IF NOT EXISTS `MediaTube` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Media`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Media` ;

CREATE TABLE IF NOT EXISTS `Media` (
  `id` VARCHAR(100) NOT NULL,
  `mediaType` INT NULL COMMENT '0 - recorded media\n1 - live streams',
  `publishTimestamp` DATETIME NULL,
  `length` INT NULL,
  `thumbnailPath` VARCHAR(256) NULL,
  `publisherId` VARCHAR(100) NULL,
  `URL` VARCHAR(256) NULL,
  `mediaName` VARCHAR(255) NOT NULL,
  `streamName` VARCHAR(256) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Media_User1`
  FOREIGN KEY (`publisherId`)
  REFERENCES `User` (`id`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_Media_User1_idx` ON `Media` (`publisherId` ASC);


-- -----------------------------------------------------
-- Table `MediaTubeOperation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `MediaTubeOperation` ;

CREATE TABLE IF NOT EXISTS `MediaTubeOperation` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `UserMediaTubePermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `UserMediaTubePermission` ;

CREATE TABLE IF NOT EXISTS `UserMediaTubePermission` (
  `userId` VARCHAR(100) NOT NULL,
  `mediaTubeOperationId` VARCHAR(100) NOT NULL,
  `mediaTubeId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`userId`, `mediaTubeOperationId`, `mediaTubeId`),
  CONSTRAINT `fk_UserMediaTubePermission_MediaTube1`
  FOREIGN KEY (`mediaTubeId`)
  REFERENCES `MediaTube` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserMediaTubePermission_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserMediaTubePermission_User1`
  FOREIGN KEY (`userId`)
  REFERENCES `User` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserMediaTubePermission_MediaTubePermission1`
  FOREIGN KEY (`mediaTubeOperationId`)
  REFERENCES `MediaTubeOperation` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_UserMediaTubePermission_MediaTube1_idx` ON `UserMediaTubePermission` (`mediaTubeId` ASC);

CREATE INDEX `fk_UserMediaTubePermission_PermissionType1_idx` ON `UserMediaTubePermission` (`permissionTypeId` ASC);

CREATE INDEX `fk_UserMediaTubePermission_User1_idx` ON `UserMediaTubePermission` (`userId` ASC);

CREATE INDEX `fk_UserMediaTubePermission_MediaTubePermission1_idx` ON `UserMediaTubePermission` (`mediaTubeOperationId` ASC);


-- -----------------------------------------------------
-- Table `RoleMediaTubePermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RoleMediaTubePermission` ;

CREATE TABLE IF NOT EXISTS `RoleMediaTubePermission` (
  `roleId` VARCHAR(100) NOT NULL,
  `mediaTubeOperationId` VARCHAR(100) NOT NULL,
  `mediaTubeId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`roleId`, `mediaTubeOperationId`, `mediaTubeId`),
  CONSTRAINT `fk_RoleMediaTubePermission_MediaTube1`
  FOREIGN KEY (`mediaTubeId`)
  REFERENCES `MediaTube` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RoleMediaTubePermission_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RoleMediaTubePermission_Role1`
  FOREIGN KEY (`roleId`)
  REFERENCES `Role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RoleMediaTubePermission_MediaTubePermission1`
  FOREIGN KEY (`mediaTubeOperationId`)
  REFERENCES `MediaTubeOperation` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_RoleMediaTubePermission_MediaTube1_idx` ON `RoleMediaTubePermission` (`mediaTubeId` ASC);

CREATE INDEX `fk_RoleMediaTubePermission_PermissionType1_idx` ON `RoleMediaTubePermission` (`permissionTypeId` ASC);

CREATE INDEX `fk_RoleMediaTubePermission_Role1_idx` ON `RoleMediaTubePermission` (`roleId` ASC);

CREATE INDEX `fk_RoleMediaTubePermission_MediaTubePermission1_idx` ON `RoleMediaTubePermission` (`mediaTubeOperationId` ASC);


-- -----------------------------------------------------
-- Table `MediaSettings`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `MediaSettings` ;

CREATE TABLE IF NOT EXISTS `MediaSettings` (
  `isRecordingEnabled` TINYINT(1) NULL,
  `isBroadcastEnabled` TINYINT(1) NULL,
  `recordingLimit` INT NULL,
  `broadcastLimit` INT NULL,
  `id` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DataConnector`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DataConnector` ;

CREATE TABLE IF NOT EXISTS `DataConnector` (
  `id` VARCHAR(36) NOT NULL,
  `templateId` INT NOT NULL DEFAULT 0,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`));

CREATE UNIQUE INDEX `name_UNIQUE` ON `DataConnector` (`name` ASC);


-- -----------------------------------------------------
-- Table `UserDataTable`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `UserDataTable` ;

CREATE TABLE IF NOT EXISTS `UserDataTable` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `originalName` VARCHAR(45) NULL,
  `isView` TINYINT(1) NOT NULL DEFAULT 0,
  `connectorId` VARCHAR(36) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_data_table_to_data_connector_id`
  FOREIGN KEY (`connectorId`)
  REFERENCES `DataConnector` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

CREATE INDEX `fk_user_data_table_to_data_connector_id_idx` ON `UserDataTable` (`connectorId` ASC);

CREATE UNIQUE INDEX `name_UNIQUE` ON `UserDataTable` (`name` ASC);


-- -----------------------------------------------------
-- Table `UserDataTableColumn`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `UserDataTableColumn` ;

CREATE TABLE IF NOT EXISTS `UserDataTableColumn` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NULL,
  `originalName` VARCHAR(45) NULL,
  `dataColumnTypeId` VARCHAR(100) NOT NULL,
  `userDataTableId` VARCHAR(100) NOT NULL,
  `defaultValue` VARCHAR(500) NULL,
  `required` TINYINT(1) NOT NULL DEFAULT 0,
  `dataSize` INT NULL,
  `autoLoad` TINYINT(1) NULL,
  `customRegex` VARCHAR(2000) NULL,
  `isPrimaryKey` TINYINT(1) NULL,
  `unique` TINYINT(1) NOT NULL DEFAULT 0,
  `indexed` TINYINT(1) NOT NULL DEFAULT 0,
  `metaInfo` json NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_UserDataTable_UserDataColumnTypes1`
  FOREIGN KEY (`dataColumnTypeId`)
  REFERENCES `DataColumnType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserDataTableColumn_UserDataTable1`
  FOREIGN KEY (`userDataTableId`)
  REFERENCES `UserDataTable` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
  ENGINE = InnoDB;

CREATE INDEX `fk_UserDataTable_UserDataColumnTypes1_idx` ON `UserDataTableColumn` (`dataColumnTypeId` ASC);

CREATE INDEX `fk_UserDataTableColumn_UserDataTable1_idx` ON `UserDataTableColumn` (`userDataTableId` ASC);

CREATE UNIQUE INDEX `name_UNIQUE` ON `UserDataTableColumn` (`name` ASC, `userDataTableId` ASC);


-- -----------------------------------------------------
-- Table `DataTableOperation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DataTableOperation` ;

CREATE TABLE IF NOT EXISTS `DataTableOperation` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `UserDataTablePermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `UserDataTablePermission` ;

CREATE TABLE IF NOT EXISTS `UserDataTablePermission` (
  `userId` VARCHAR(100) NOT NULL,
  `userDataTableId` VARCHAR(100) NOT NULL,
  `dataTableOperationId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`userId`, `userDataTableId`, `dataTableOperationId`),
  CONSTRAINT `fk_UserDataTablePermission_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserDataTablePermission_User1`
  FOREIGN KEY (`userId`)
  REFERENCES `User` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserDataTablePermission_DataTablePermission1`
  FOREIGN KEY (`dataTableOperationId`)
  REFERENCES `DataTableOperation` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserDataTablePermission_UserDataTable1`
  FOREIGN KEY (`userDataTableId`)
  REFERENCES `UserDataTable` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_UserDataTablePermission_PermissionType1_idx` ON `UserDataTablePermission` (`permissionTypeId` ASC);

CREATE INDEX `fk_UserDataTablePermission_User1_idx` ON `UserDataTablePermission` (`userId` ASC);

CREATE INDEX `fk_UserDataTablePermission_DataTablePermission1_idx` ON `UserDataTablePermission` (`dataTableOperationId` ASC);

CREATE INDEX `fk_UserDataTablePermission_UserDataTable1_idx` ON `UserDataTablePermission` (`userDataTableId` ASC);


-- -----------------------------------------------------
-- Table `RoleDataTablePermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RoleDataTablePermission` ;

CREATE TABLE IF NOT EXISTS `RoleDataTablePermission` (
  `roleId` VARCHAR(100) NOT NULL,
  `dataTableOperationId` VARCHAR(100) NOT NULL,
  `userDataTableId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`roleId`, `dataTableOperationId`, `userDataTableId`),
  CONSTRAINT `fk_RoleDataTablePermission_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RoleDataTablePermission_Role1`
  FOREIGN KEY (`roleId`)
  REFERENCES `Role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RoleDataTablePermission_DataTablePermission1`
  FOREIGN KEY (`dataTableOperationId`)
  REFERENCES `DataTableOperation` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RoleDataTablePermission_UserDataTable1`
  FOREIGN KEY (`userDataTableId`)
  REFERENCES `UserDataTable` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_RoleDataTablePermission_PermissionType1_idx` ON `RoleDataTablePermission` (`permissionTypeId` ASC);

CREATE INDEX `fk_RoleDataTablePermission_Role1_idx` ON `RoleDataTablePermission` (`roleId` ASC);

CREATE INDEX `fk_RoleDataTablePermission_DataTablePermission1_idx` ON `RoleDataTablePermission` (`dataTableOperationId` ASC);

CREATE INDEX `fk_RoleDataTablePermission_UserDataTable1_idx` ON `RoleDataTablePermission` (`userDataTableId` ASC);


-- -----------------------------------------------------
-- Table `GeoPoint`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GeoPoint` ;

CREATE TABLE IF NOT EXISTS `GeoPoint` (
  `id` VARCHAR(100) NOT NULL,
  `latitude` DOUBLE NULL,
  `longitude` DOUBLE NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;

CREATE INDEX `coordinate` ON `GeoPoint` (`latitude` ASC, `longitude` ASC);


-- -----------------------------------------------------
-- Table `GeoPointMetadata`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GeoPointMetadata` ;

CREATE TABLE IF NOT EXISTS `GeoPointMetadata` (
  `id` VARCHAR(100) NOT NULL,
  `key` VARCHAR(1000) NULL,
  `value` VARCHAR(1000) NULL,
  `geoPointId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_GeoPointMetadata_GeoPoint1`
  FOREIGN KEY (`geoPointId`)
  REFERENCES `GeoPoint` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_GeoPointMetadata_GeoPoint1_idx` ON `GeoPointMetadata` (`geoPointId` ASC);


-- -----------------------------------------------------
-- Table `GeoPointsCategory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GeoPointsCategory` ;

CREATE TABLE IF NOT EXISTS `GeoPointsCategory` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;

CREATE UNIQUE INDEX `name_UNIQUE` ON `GeoPointsCategory` (`name` ASC);


-- -----------------------------------------------------
-- Table `GeoOperation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GeoOperation` ;

CREATE TABLE IF NOT EXISTS `GeoOperation` (
  `id` VARCHAR(100) NOT NULL COMMENT 'geo permissions include: searching for data points, adding data points',
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `RoleGeoPointsCategoryPermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RoleGeoPointsCategoryPermission` ;

CREATE TABLE IF NOT EXISTS `RoleGeoPointsCategoryPermission` (
  `roleId` VARCHAR(100) NOT NULL,
  `geoOperationId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  `getPointsCategoryId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`roleId`, `geoOperationId`, `getPointsCategoryId`),
  CONSTRAINT `fk_RoleGeoPointsCategoryPermission_Role1`
  FOREIGN KEY (`roleId`)
  REFERENCES `Role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RoleGeoPointsCategoryPermission_GeoPermission1`
  FOREIGN KEY (`geoOperationId`)
  REFERENCES `GeoOperation` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RoleGeoPointsCategoryPermission_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RoleGeoPointsCategoryPermission_GetPointsCategory1`
  FOREIGN KEY (`getPointsCategoryId`)
  REFERENCES `GeoPointsCategory` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_RoleGeoPointsCategoryPermission_Role1_idx` ON `RoleGeoPointsCategoryPermission` (`roleId` ASC);

CREATE INDEX `fk_RoleGeoPointsCategoryPermission_GeoPermission1_idx` ON `RoleGeoPointsCategoryPermission` (`geoOperationId` ASC);

CREATE INDEX `fk_RoleGeoPointsCategoryPermission_PermissionType1_idx` ON `RoleGeoPointsCategoryPermission` (`permissionTypeId` ASC);

CREATE INDEX `fk_RoleGeoPointsCategoryPermission_GetPointsCategory1_idx` ON `RoleGeoPointsCategoryPermission` (`getPointsCategoryId` ASC);


-- -----------------------------------------------------
-- Table `UserGeoPointsCategoryPermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `UserGeoPointsCategoryPermission` ;

CREATE TABLE IF NOT EXISTS `UserGeoPointsCategoryPermission` (
  `userId` VARCHAR(100) NOT NULL,
  `geoOperationId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  `getPointsCategoryId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`userId`, `geoOperationId`, `getPointsCategoryId`),
  CONSTRAINT `fk_UserGeoPointsCategoryPermission_User1`
  FOREIGN KEY (`userId`)
  REFERENCES `User` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserGeoPointsCategoryPermission_GeoPermission1`
  FOREIGN KEY (`geoOperationId`)
  REFERENCES `GeoOperation` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserGeoPointsCategoryPermission_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserGeoPointsCategoryPermission_GeoPointsCategory1`
  FOREIGN KEY (`getPointsCategoryId`)
  REFERENCES `GeoPointsCategory` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_UserGeoPointsCategoryPermission_User1_idx` ON `UserGeoPointsCategoryPermission` (`userId` ASC);

CREATE INDEX `fk_UserGeoPointsCategoryPermission_GeoPermission1_idx` ON `UserGeoPointsCategoryPermission` (`geoOperationId` ASC);

CREATE INDEX `fk_UserGeoPointsCategoryPermission_PermissionType1_idx` ON `UserGeoPointsCategoryPermission` (`permissionTypeId` ASC);

CREATE INDEX `fk_UserGeoPointsCategoryPermission_GetPointsCategory1_idx` ON `UserGeoPointsCategoryPermission` (`getPointsCategoryId` ASC);


-- -----------------------------------------------------
-- Table `UserDataTableRelation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `UserDataTableRelation` ;

CREATE TABLE IF NOT EXISTS `UserDataTableRelation` (
  `foreignUserDataTableId` VARCHAR(100) NOT NULL,
  `userDataTableColumnId` VARCHAR(100) NOT NULL,
  `relationType` ENUM('ONE_TO_ONE', 'ONE_TO_MANY') NOT NULL,
  PRIMARY KEY (`userDataTableColumnId`),
  CONSTRAINT `fk_UserDataTableRelation_UserDataTable1`
  FOREIGN KEY (`foreignUserDataTableId`)
  REFERENCES `UserDataTable` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserDataTableRelation_UserDataTableColumn1`
  FOREIGN KEY (`userDataTableColumnId`)
  REFERENCES `UserDataTableColumn` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT )
  ENGINE = InnoDB;

CREATE INDEX `fk_UserDataTableRelation_UserDataTable1_idx` ON `UserDataTableRelation` (`foreignUserDataTableId` ASC);

CREATE INDEX `fk_UserDataTableRelation_UserDataTableColumn1_idx` ON `UserDataTableRelation` (`userDataTableColumnId` ASC);


-- -----------------------------------------------------
-- Table `GeoPointToCategory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GeoPointToCategory` ;

CREATE TABLE IF NOT EXISTS `GeoPointToCategory` (
  `geoPointId` VARCHAR(100) NOT NULL,
  `geoPointsCategoryId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`geoPointId`, `geoPointsCategoryId`),
  CONSTRAINT `fk_GeoPoint_has_GeoPointsCategory_GeoPoint1`
  FOREIGN KEY (`geoPointId`)
  REFERENCES `GeoPoint` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GeoPoint_has_GeoPointsCategory_GeoPointsCategory1`
  FOREIGN KEY (`geoPointsCategoryId`)
  REFERENCES `GeoPointsCategory` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_GeoPoint_has_GeoPointsCategory_GeoPointsCategory1_idx` ON `GeoPointToCategory` (`geoPointsCategoryId` ASC);

CREATE INDEX `fk_GeoPoint_has_GeoPointsCategory_GeoPoint1_idx` ON `GeoPointToCategory` (`geoPointId` ASC);


-- -----------------------------------------------------
-- Table `udt.DeviceRegistration`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `udt.DeviceRegistration` ;

CREATE TABLE IF NOT EXISTS `udt.DeviceRegistration` (
  `objectId` VARCHAR(100) NOT NULL,
  `channelName` VARCHAR(45) NOT NULL DEFAULT 'default',
  `deviceToken` VARCHAR(256) NOT NULL,
  `deviceId` VARCHAR(45) NOT NULL,
  `operatingSystemName` CHAR(15) NOT NULL,
  `operatingSystemVersion` VARCHAR(45) NULL,
  `expiration` DATETIME NULL,
  `created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` DATETIME NULL,
  `ownerId` VARCHAR(100) NULL,
  PRIMARY KEY (`objectId`),
  CONSTRAINT `fk_channelName_MessagingChannel_name`
  FOREIGN KEY (`channelName`)
  REFERENCES `MessagingChannel` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `deviceId` ON `udt.DeviceRegistration` (`deviceId` ASC);

CREATE INDEX `deviceToken` ON `udt.DeviceRegistration` (`deviceToken`(100) ASC);

CREATE INDEX `fk_channelName_MessagingChannel_name_idx` ON `udt.DeviceRegistration` (`channelName` ASC);

CREATE UNIQUE INDEX `channelName_deviceId` ON `udt.DeviceRegistration` (`channelName` ASC , `deviceId` ASC );


-- -----------------------------------------------------
-- Table `GlobalRoleDataPermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GlobalRoleDataPermission` ;

CREATE TABLE IF NOT EXISTS `GlobalRoleDataPermission` (
  `roleId` VARCHAR(100) NOT NULL,
  `dataTableOperationId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`roleId`, `dataTableOperationId`),
  CONSTRAINT `fk_GlobalRoleDataPermission_Role1`
  FOREIGN KEY (`roleId`)
  REFERENCES `Role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GlobalRoleDataPermission_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GlobalRoleDataPermission_DataTableOperation1`
  FOREIGN KEY (`dataTableOperationId`)
  REFERENCES `DataTableOperation` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_GlobalRoleDataPermission_Role1_idx` ON `GlobalRoleDataPermission` (`roleId` ASC);

CREATE INDEX `fk_GlobalRoleDataPermission_PermissionType1_idx` ON `GlobalRoleDataPermission` (`permissionTypeId` ASC);


-- -----------------------------------------------------
-- Table `GlobalRoleGeoPermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GlobalRoleGeoPermission` ;

CREATE TABLE IF NOT EXISTS `GlobalRoleGeoPermission` (
  `roleId` VARCHAR(100) NOT NULL,
  `geoOperationId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`roleId`, `geoOperationId`),
  CONSTRAINT `fk_GlobalRoleGeoPermission_Role1`
  FOREIGN KEY (`roleId`)
  REFERENCES `Role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GlobalRoleGeoPermission_GeoOperation1`
  FOREIGN KEY (`geoOperationId`)
  REFERENCES `GeoOperation` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GlobalRoleGeoPermission_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_GlobalRoleGeoPermission_Role1_idx` ON `GlobalRoleGeoPermission` (`roleId` ASC);

CREATE INDEX `fk_GlobalRoleGeoPermission_GeoOperation1_idx` ON `GlobalRoleGeoPermission` (`geoOperationId` ASC);

CREATE INDEX `fk_GlobalRoleGeoPermission_PermissionType1_idx` ON `GlobalRoleGeoPermission` (`permissionTypeId` ASC);


-- -----------------------------------------------------
-- Table `GlobalRoleMessagingPermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GlobalRoleMessagingPermission` ;

CREATE TABLE IF NOT EXISTS `GlobalRoleMessagingPermission` (
  `roleId` VARCHAR(100) NOT NULL,
  `channelOperationId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`roleId`, `channelOperationId`),
  CONSTRAINT `fk_GlobalRoleMessagingPermission_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GlobalRoleMessagingPermission_Role1`
  FOREIGN KEY (`roleId`)
  REFERENCES `Role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GlobalRoleMessagingPermission_ChannelOperation1`
  FOREIGN KEY (`channelOperationId`)
  REFERENCES `ChannelOperation` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_GlobalRoleMessagingPermission_PermissionType1_idx` ON `GlobalRoleMessagingPermission` (`permissionTypeId` ASC);

CREATE INDEX `fk_GlobalRoleMessagingPermission_Role1_idx` ON `GlobalRoleMessagingPermission` (`roleId` ASC);

CREATE INDEX `fk_GlobalRoleMessagingPermission_ChannelOperation1_idx` ON `GlobalRoleMessagingPermission` (`channelOperationId` ASC);


-- -----------------------------------------------------
-- Table `GlobalRoleMediaPermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GlobalRoleMediaPermission` ;

CREATE TABLE IF NOT EXISTS `GlobalRoleMediaPermission` (
  `roleId` VARCHAR(100) NOT NULL,
  `mediaTubeOperationId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`roleId`, `mediaTubeOperationId`),
  CONSTRAINT `fk_GlobalRoleMediaPermission_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GlobalRoleMediaPermission_Role1`
  FOREIGN KEY (`roleId`)
  REFERENCES `Role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GlobalRoleMediaPermission_MediaTubeOperation1`
  FOREIGN KEY (`mediaTubeOperationId`)
  REFERENCES `MediaTubeOperation` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_GlobalRoleMediaPermission_Role1_idx` ON `GlobalRoleMediaPermission` (`roleId` ASC);

CREATE INDEX `fk_GlobalRoleMediaPermission_MediaTubeOperation1_idx` ON `GlobalRoleMediaPermission` (`mediaTubeOperationId` ASC);


-- -----------------------------------------------------
-- Table `ApplicationType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ApplicationType` ;

CREATE TABLE IF NOT EXISTS `ApplicationType` (
  `id` VARCHAR(100) NOT NULL,
  `deviceType` VARCHAR(20) NOT NULL,
  `secretKey` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;

CREATE UNIQUE INDEX `secretKey_UNIQUE` ON `ApplicationType` (`secretKey` ASC);


-- -----------------------------------------------------
-- Table `MediaToMediaTube`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `MediaToMediaTube` ;

CREATE TABLE IF NOT EXISTS `MediaToMediaTube` (
  `Media_id` VARCHAR(100) NOT NULL,
  `MediaTube_id` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`Media_id`, `MediaTube_id`),
  CONSTRAINT `fk_Media_has_MediaTube_Media1`
  FOREIGN KEY (`Media_id`)
  REFERENCES `Media` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Media_has_MediaTube_MediaTube1`
  FOREIGN KEY (`MediaTube_id`)
  REFERENCES `MediaTube` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_Media_has_MediaTube_MediaTube1_idx` ON `MediaToMediaTube` (`MediaTube_id` ASC);

CREATE INDEX `fk_Media_has_MediaTube_Media1_idx` ON `MediaToMediaTube` (`Media_id` ASC);


-- -----------------------------------------------------
-- Table `LoggedInUser`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LoggedInUser` ;

CREATE TABLE IF NOT EXISTS `LoggedInUser` (
  `id` VARCHAR(100) NOT NULL,
  `userId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_LoggedInUsers_User1`
  FOREIGN KEY (`userId`)
  REFERENCES `User` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_LoggedInUsers_User1_idx` ON `LoggedInUser` (`userId` ASC);


-- -----------------------------------------------------
-- Table `EmailSettings`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `EmailSettings` ;

CREATE TABLE IF NOT EXISTS `EmailSettings` (
  `id` VARCHAR(100) NOT NULL,
  `smtpServer` VARCHAR(100) NULL,
  `smtpPort` INT NULL,
  `protocol` INT NOT NULL DEFAULT 1,
  `userid` VARCHAR(255) NULL,
  `password` VARCHAR(100) NULL,
  `sentFrom` VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `emailFrom` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SocialUser`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `SocialUser` ;

CREATE TABLE IF NOT EXISTS `SocialUser` (
  `socialId` VARCHAR(100) NOT NULL,
  `displayName` VARCHAR(100) NOT NULL,
  `userTypeId` INT NOT NULL DEFAULT 1,
  `userId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`userId`),
  CONSTRAINT `fk_SocialUser_UserType1`
  FOREIGN KEY (`userTypeId`)
  REFERENCES `UserType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SocialUser_User1`
  FOREIGN KEY (`userId`)
  REFERENCES `User` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_SocialUser_UserType1_idx` ON `SocialUser` (`userTypeId` ASC);


-- -----------------------------------------------------
-- Table `ApplicationSocialSettings`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ApplicationSocialSettings` ;

CREATE TABLE IF NOT EXISTS `ApplicationSocialSettings` (
  `id` VARCHAR(100) NOT NULL,
  `socialParameterName` VARCHAR(100) NOT NULL,
  `socialParameterValue` VARCHAR(100) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `EventHandler`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `EventHandler` ;

CREATE TABLE IF NOT EXISTS `EventHandler` (
  `id` VARCHAR(100) NOT NULL,
  `context` VARCHAR(45) NOT NULL,
  `async` TINYINT(1) NOT NULL,
  `enabled` TINYINT(1) NOT NULL,
  `eventId` INT NOT NULL,
  `provider` VARCHAR(500) NOT NULL,
  `modelName` VARCHAR(45) NOT NULL,
  `mode` INT NOT NULL,
  `created` DATETIME NULL,
  `lang` INT NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;

CREATE UNIQUE INDEX `modelName_UNIQUE` ON `EventHandler` (`modelName` ASC, `eventId` ASC, `mode` ASC, `context` ASC, `lang` ASC);


-- -----------------------------------------------------
-- Table `Timer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Timer` ;

CREATE TABLE IF NOT EXISTS `Timer` (
  `id` VARCHAR(100) NOT NULL,
  `timername` VARCHAR(100) NOT NULL,
  `startdate` DATETIME NULL,
  `expire` DATETIME NULL,
  `type` ENUM('once','daily','weekly','monthly','custom') NOT NULL,
  `frequency` VARCHAR(1000) NULL,
  `startup` BIGINT NULL,
  `nextStartup` BIGINT NULL,
  `modelName` VARCHAR(45) NOT NULL,
  `lang` INT NOT NULL,
  `mode` INT NOT NULL,
  `provider` VARCHAR(500) NOT NULL,
  `enabled` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;

CREATE UNIQUE INDEX `timername_UNIQUE` ON `Timer` (`timername` ASC, `modelName` ASC, `mode` ASC);


-- -----------------------------------------------------
-- Table `FileOperations`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `FileOperations` ;

CREATE TABLE IF NOT EXISTS `FileOperations` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `FileUserPermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `FileUserPermission` ;

CREATE TABLE IF NOT EXISTS `FileUserPermission` (
  `fileOperationId` VARCHAR(100) NOT NULL,
  `userId` VARCHAR(100) NOT NULL,
  `resourceId` VARCHAR(200) BINARY NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`fileOperationId`, `userId`, `resourceId`),
  CONSTRAINT `fk_FileUserPermission_FileOperations1`
  FOREIGN KEY (`fileOperationId`)
  REFERENCES `FileOperations` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_FileUserPermission_User1`
  FOREIGN KEY (`userId`)
  REFERENCES `User` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_FileUserPermission_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_FileUserPermission_User1_idx` ON `FileUserPermission` (`userId` ASC);

CREATE INDEX `fk_FileUserPermission_PermissionType1_idx` ON `FileUserPermission` (`permissionTypeId` ASC);


-- -----------------------------------------------------
-- Table `FileRolePermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `FileRolePermission` ;

CREATE TABLE IF NOT EXISTS `FileRolePermission` (
  `resourceId` VARCHAR(200) BINARY NOT NULL,
  `roleId` VARCHAR(100) NOT NULL,
  `fileOperationId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`roleId`, `fileOperationId`, `resourceId`),
  CONSTRAINT `fk_FileRolePermission_Role1`
  FOREIGN KEY (`roleId`)
  REFERENCES `Role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_FileRolePermission_FileOperations1`
  FOREIGN KEY (`fileOperationId`)
  REFERENCES `FileOperations` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_FileRolePermission_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_FileRolePermission_Role1_idx` ON `FileRolePermission` (`roleId` ASC);

CREATE INDEX `fk_FileRolePermission_FileOperations1_idx` ON `FileRolePermission` (`fileOperationId` ASC);

CREATE INDEX `fk_FileRolePermission_PermissionType1_idx` ON `FileRolePermission` (`permissionTypeId` ASC);


-- -----------------------------------------------------
-- Table `GlobalFileRolePermissions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GlobalFileRolePermissions` ;

CREATE TABLE IF NOT EXISTS `GlobalFileRolePermissions` (
  `fileOperationId` VARCHAR(100) NOT NULL,
  `roleId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`fileOperationId`, `roleId`),
  CONSTRAINT `fk_GlobalFileRolePermissions_FileOperations1`
  FOREIGN KEY (`fileOperationId`)
  REFERENCES `FileOperations` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GlobalFileRolePermissions_Role1`
  FOREIGN KEY (`roleId`)
  REFERENCES `Role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GlobalFileRolePermissions_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_GlobalFileRolePermissions_Role1_idx` ON `GlobalFileRolePermissions` (`roleId` ASC);

CREATE INDEX `fk_GlobalFileRolePermissions_PermissionType1_idx` ON `GlobalFileRolePermissions` (`permissionTypeId` ASC);


-- -----------------------------------------------------
-- Table `DataOwnerAcl`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DataOwnerAcl` ;

CREATE TABLE IF NOT EXISTS `DataOwnerAcl` (
  `id` VARCHAR(100) NOT NULL,
  `operationId` VARCHAR(100) NOT NULL,
  `userDataTableId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_DataOwnerAcl_DataTableOperation1`
  FOREIGN KEY (`operationId`)
  REFERENCES `DataTableOperation` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DataOwnerAcl_UserDataTable1`
  FOREIGN KEY (`userDataTableId`)
  REFERENCES `UserDataTable` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DataOwnerAcl_PermissionType1`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_DataOwnerAcl_DataTableOperation1_idx` ON `DataOwnerAcl` (`operationId` ASC);

CREATE INDEX `fk_DataOwnerAcl_UserDataTable1_idx` ON `DataOwnerAcl` (`userDataTableId` ASC);

CREATE INDEX `fk_DataOwnerAcl_PermissionType1_idx` ON `DataOwnerAcl` (`permissionTypeId` ASC);


-- -----------------------------------------------------
-- Table `GlobalDataOwnerAcl`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GlobalDataOwnerAcl` ;

CREATE TABLE IF NOT EXISTS `GlobalDataOwnerAcl` (
  `id` VARCHAR(100) NOT NULL,
  `operationId` VARCHAR(100) NOT NULL,
  `permissionTypeId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_DataOwnerAcl_DataTableOperation10`
  FOREIGN KEY (`operationId`)
  REFERENCES `DataTableOperation` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DataOwnerAcl_PermissionType10`
  FOREIGN KEY (`permissionTypeId`)
  REFERENCES `PermissionType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_DataOwnerAcl_DataTableOperation1_idx` ON `GlobalDataOwnerAcl` (`operationId` ASC);

CREATE INDEX `fk_DataOwnerAcl_PermissionType1_idx` ON `GlobalDataOwnerAcl` (`permissionTypeId` ASC);


-- -----------------------------------------------------
-- Table `ExternalConnection`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ExternalConnection` ;

CREATE TABLE IF NOT EXISTS `ExternalConnection` (
  `id` VARCHAR(100) NOT NULL,
  `servertype` VARCHAR(45) NOT NULL,
  `hostname` VARCHAR(500) NOT NULL,
  `port` MEDIUMINT NOT NULL,
  `login` VARCHAR(200) NULL,
  `password` VARCHAR(200) NULL,
  `SID` VARCHAR(200) NULL,
  `database` VARCHAR(200) NULL,
  `isActive` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `LocalService`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LocalService` ;

CREATE TABLE IF NOT EXISTS `LocalService` (
  `id` VARCHAR(36) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `created` DATETIME NOT NULL,
  `type` INT NOT NULL,
  `lang` INT NOT NULL DEFAULT 0,
  `pathToSchema` VARCHAR(255) NULL DEFAULT NULL,
  `host` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Must be like\nprotocol://host.domain:port',
  `basePath` VARCHAR(255) NULL DEFAULT NULL,
  `className` VARCHAR(255) NULL DEFAULT NULL,
  `configured` TINYINT(1) NOT NULL DEFAULT 0,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `updateNotes` TEXT NULL DEFAULT NULL,
  `internalOnly` TINYINT(1) NOT NULL DEFAULT 1,
  `modelName` VARCHAR(45) NOT NULL,
  `mode` INT NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;

CREATE UNIQUE INDEX `name_UNIQUE` ON `LocalService` (`name` ASC, `mode` ASC);


-- -----------------------------------------------------
-- Table `LocalServiceMethod`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LocalServiceMethod` ;

CREATE TABLE IF NOT EXISTS `LocalServiceMethod` (
  `id` VARCHAR(36) NOT NULL,
  `serviceVersionId` VARCHAR(36) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `httpType` INT NOT NULL DEFAULT 2,
  `returnType` VARCHAR(255) NULL DEFAULT NULL,
  `path` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_local_service_method_service_id`
  FOREIGN KEY (`serviceVersionId`)
  REFERENCES `LocalService` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_local_service_method_service_id_idx` ON `LocalServiceMethod` (`serviceVersionId` ASC);

CREATE UNIQUE INDEX `ui_serviceVersionId_httpType_path` ON `LocalServiceMethod` (`serviceVersionId` ASC, `httpType` ASC, `path` ASC);

CREATE UNIQUE INDEX `ui_serviceVersionId_name` ON `LocalServiceMethod` (`serviceVersionId` ASC, `name` ASC);


-- -----------------------------------------------------
-- Table `LocalServiceMethodUserACL`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LocalServiceMethodUserACL` ;

CREATE TABLE IF NOT EXISTS `LocalServiceMethodUserACL` (
  `userId` VARCHAR(36) NOT NULL,
  `methodId` VARCHAR(36) NOT NULL,
  `permissionType` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`userId`, `methodId`),
  CONSTRAINT `fk_localservicemethod_acl_user`
  FOREIGN KEY (`userId`)
  REFERENCES `User` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_localservicemethod_acl_method`
  FOREIGN KEY (`methodId`)
  REFERENCES `LocalServiceMethod` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_localservicemethod_acl_method_idx` ON `LocalServiceMethodUserACL` (`methodId` ASC);

CREATE INDEX `fk_localservicemethod_acl_user` ON `LocalServiceMethodUserACL` (`userId` ASC);


-- -----------------------------------------------------
-- Table `LocalServiceMethodRoleACL`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LocalServiceMethodRoleACL` ;

CREATE TABLE IF NOT EXISTS `LocalServiceMethodRoleACL` (
  `roleId` VARCHAR(36) NOT NULL,
  `methodId` VARCHAR(36) NOT NULL,
  `permissionType` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`roleId`, `methodId`),
  CONSTRAINT `fk_localservicemethodrole_acl_role`
  FOREIGN KEY (`roleId`)
  REFERENCES `Role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_localservicemethodrole_acl_method`
  FOREIGN KEY (`methodId`)
  REFERENCES `LocalServiceMethod` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_localservicemethodroleacl_rol_idx` ON `LocalServiceMethodRoleACL` (`roleId` ASC);

CREATE INDEX `fk_localservicemethodrole_acl_method_idx` ON `LocalServiceMethodRoleACL` (`methodId` ASC);


-- -----------------------------------------------------
-- Table `LocalServiceMethodArg`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LocalServiceMethodArg` ;

CREATE TABLE IF NOT EXISTS `LocalServiceMethodArg` (
  `id` VARCHAR(36) NOT NULL,
  `methodId` VARCHAR(36) NOT NULL,
  `name` VARCHAR(45) BINARY NULL DEFAULT NULL,
  `type` VARCHAR(255) NULL DEFAULT NULL,
  `targetPlace` INT NULL DEFAULT NULL,
  `position` SMALLINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fl_local_service_method_arg_methodId`
  FOREIGN KEY (`methodId`)
  REFERENCES `LocalServiceMethod` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fl_local_service_method_arg_methodId_idx` ON `LocalServiceMethodArg` (`methodId` ASC);

CREATE UNIQUE INDEX `ui_name_methodId` ON `LocalServiceMethodArg` (`name` ASC, `methodId` ASC);


-- -----------------------------------------------------
-- Table `GlobalRoleServiceVersionPermission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GlobalRoleServiceVersionPermission` ;

CREATE TABLE IF NOT EXISTS `GlobalRoleServiceVersionPermission` (
  `roleId` VARCHAR(36) NOT NULL,
  `serviceVersionId` VARCHAR(36) NOT NULL,
  `permissionType` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`roleId`, `serviceVersionId`),
  CONSTRAINT `fk_GlobalRoleServiceVersionPermission_role`
  FOREIGN KEY (`roleId`)
  REFERENCES `Role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GlobalRoleServiceVersionPermission_serviceVersion`
  FOREIGN KEY (`serviceVersionId`)
  REFERENCES `LocalService` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_GlobalRoleServiceVersionPermission_role_idx` ON `GlobalRoleServiceVersionPermission` (`roleId` ASC);

CREATE INDEX `fk_GlobalRoleServiceVersionPermission_serviceVersion_idx` ON `GlobalRoleServiceVersionPermission` (`serviceVersionId` ASC);


-- -----------------------------------------------------
-- Table `Fence`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Fence` ;

CREATE TABLE IF NOT EXISTS `Fence` (
  `id` VARCHAR(100) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `nwPointId` VARCHAR(100) NOT NULL,
  `sePointId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Fence_GeoPoint1`
  FOREIGN KEY (`nwPointId`)
  REFERENCES `GeoPoint` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Fence_GeoPoint2`
  FOREIGN KEY (`sePointId`)
  REFERENCES `GeoPoint` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_Fence_GeoPoint2_idx` ON `Fence` (`sePointId` ASC);

CREATE INDEX `fk_Fence_GeoPoint1_idx` ON `Fence` (`nwPointId` ASC);


-- -----------------------------------------------------
-- Table `GeoFence`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GeoFence` ;

CREATE TABLE IF NOT EXISTS `GeoFence` (
  `id` VARCHAR(100) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `qualCriteria` MEDIUMTEXT NULL DEFAULT NULL,
  `isActive` TINYINT(1) NOT NULL,
  `fenceId` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_GeoFence_Fence1`
  FOREIGN KEY (`fenceId`)
  REFERENCES `Fence` (`id`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `active` ON `GeoFence` (`isActive` DESC);

CREATE INDEX `fk_GeoFence_Fence1_idx` ON `GeoFence` (`fenceId` ASC);

CREATE INDEX `name` ON `GeoFence` (`name` ASC);


-- -----------------------------------------------------
-- Table `GeoFenceAction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GeoFenceAction` ;

CREATE TABLE IF NOT EXISTS `GeoFenceAction` (
  `id` VARCHAR(100) NOT NULL,
  `geoFenceId` VARCHAR(100) NOT NULL,
  `rule` VARCHAR(45) NOT NULL COMMENT 'onenter, onstay, onexit',
  `type` VARCHAR(45) NOT NULL COMMENT 'push, pubsub, event, url',
  `duration` INT NULL DEFAULT NULL COMMENT 'in seconds',
  `channel` VARCHAR(255) NULL DEFAULT NULL,
  `body` MEDIUMTEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_GeoFenceAction_GeoFence1`
  FOREIGN KEY (`geoFenceId`)
  REFERENCES `GeoFence` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_GeoFenceAction_GeoFence1` ON `GeoFenceAction` (`geoFenceId` ASC);


-- -----------------------------------------------------
-- Table `GeoFenceActionExtended`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GeoFenceActionExtended` ;

CREATE TABLE IF NOT EXISTS `GeoFenceActionExtended` (
  `geoFenceActionId` VARCHAR(100) NOT NULL,
  `topic` VARCHAR(255) NULL DEFAULT NULL,
  `query` MEDIUMTEXT NULL DEFAULT NULL,
  `isAssociated` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`geoFenceActionId`),
  CONSTRAINT `fk_DeliverToPushData_GeoFenceAction1`
  FOREIGN KEY (`geoFenceActionId`)
  REFERENCES `GeoFenceAction` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_DeliverToPushData_GeoFenceAction1_idx` ON `GeoFenceActionExtended` (`geoFenceActionId` ASC);


-- -----------------------------------------------------
-- Table `Header`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Header` ;

CREATE TABLE IF NOT EXISTS `Header` (
  `id` VARCHAR(100) NOT NULL,
  `key` VARCHAR(255) NULL DEFAULT NULL,
  `value` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `HeaderToGeoFenceAction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HeaderToGeoFenceAction` ;

CREATE TABLE IF NOT EXISTS `HeaderToGeoFenceAction` (
  `headerId` VARCHAR(100) NOT NULL,
  `geoFenceActionId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`geoFenceActionId`, `headerId`),
  CONSTRAINT `fk_HeaderToGeoFenceAction_Header1`
  FOREIGN KEY (`headerId`)
  REFERENCES `Header` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_HeaderToGeoFenceAction_GeoFenceAction1`
  FOREIGN KEY (`geoFenceActionId`)
  REFERENCES `GeoFenceAction` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_HeaderToGeoFenceAction_Header1_idx` ON `HeaderToGeoFenceAction` (`headerId` ASC);

CREATE INDEX `fk_HeaderToGeoFenceAction_GeoFenceAction1_idx` ON `HeaderToGeoFenceAction` (`geoFenceActionId` ASC);


-- -----------------------------------------------------
-- Table `GeoFencePoint`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GeoFencePoint` ;

CREATE TABLE IF NOT EXISTS `GeoFencePoint` (
  `fenceId` VARCHAR(100) NOT NULL,
  `geoPointId` VARCHAR(100) NOT NULL,
  `order` INT NULL DEFAULT NULL,
  PRIMARY KEY (`fenceId`, `geoPointId`),
  CONSTRAINT `fk_GeoFencePoint_GeoPoint1`
  FOREIGN KEY (`geoPointId`)
  REFERENCES `GeoPoint` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GeoFencePoint_Fence1`
  FOREIGN KEY (`fenceId`)
  REFERENCES `Fence` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `order` ON `GeoFencePoint` (`order` ASC);

CREATE INDEX `fk_GeoFencePoint_GeoPoint1_idx` ON `GeoFencePoint` (`geoPointId` ASC);


-- -----------------------------------------------------
-- Table `DataConnectorConfig`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DataConnectorConfig` ;

CREATE TABLE IF NOT EXISTS `DataConnectorConfig` (
  `id` VARCHAR(36) NOT NULL,
  `configId` VARCHAR(255) NULL DEFAULT NULL,
  `value` VARCHAR(255) NULL DEFAULT NULL,
  `type` INT NULL DEFAULT NULL,
  `required` TINYINT(1) NULL DEFAULT NULL,
  `validator` VARCHAR(255) NULL DEFAULT NULL,
  `dataConnectorId` VARCHAR(36) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_B9F490B1-05C1-4FCE-93F9-D97E62F3F965`
  FOREIGN KEY (`dataConnectorId`)
  REFERENCES `DataConnector` (`id`)
    ON DELETE CASCADE);

CREATE INDEX `dk_data_connector_config_dc_id_idx` ON `DataConnectorConfig` (`dataConnectorId` ASC);


-- -----------------------------------------------------
-- Table `DataConnectorProcedure`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DataConnectorProcedure` ;

CREATE TABLE IF NOT EXISTS `DataConnectorProcedure` (
  `id` VARCHAR(36) NOT NULL,
  `dataConnectorId` VARCHAR(36) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `originalName` VARCHAR(255) NOT NULL,
  `type` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  CONSTRAINT `Fk_procedure_data_connector_id_idx`
  FOREIGN KEY (`dataConnectorId`)
  REFERENCES `DataConnector` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

CREATE INDEX `Fk_procedure_data_connector_id_idx_idx` ON `DataConnectorProcedure` (`dataConnectorId` ASC);


-- -----------------------------------------------------
-- Table `DataConnectorProcedureColumn`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DataConnectorProcedureColumn` ;

CREATE TABLE IF NOT EXISTS `DataConnectorProcedureColumn` (
  `id` VARCHAR(36) NOT NULL,
  `procedureId` VARCHAR(36) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `originalName` VARCHAR(255) NOT NULL,
  `type` TINYINT NOT NULL DEFAULT 0,
  `routineColumnType` VARCHAR(45) NOT NULL,
  `ordinal` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_procedure_column_procedure_id_idx`
  FOREIGN KEY (`procedureId`)
  REFERENCES `DataConnectorProcedure` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB;

CREATE INDEX `fk_procedure_column_procedure_id_idx_idx` ON `DataConnectorProcedureColumn` (`procedureId` ASC);


-- -----------------------------------------------------
-- Table `InvocationChain`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `InvocationChain` ;

CREATE TABLE IF NOT EXISTS `InvocationChain` (
  `id` VARCHAR(36) NOT NULL,
  `context` VARCHAR(45) NOT NULL,
  `eventId` INT NOT NULL,
  `orderExecution` INT NOT NULL,
  `handlerId` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_InvocationChain_EventBinding1`
  FOREIGN KEY (`handlerId`)
  REFERENCES `EventHandler` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE INDEX `fk_InvocationChain_EventBinding1_idx` ON `InvocationChain` (`handlerId` ASC);

CREATE UNIQUE INDEX `context_UNIQUE` ON `InvocationChain` (`context` ASC, `eventId` ASC, `handlerId` ASC);


-- -----------------------------------------------------
-- Placeholder table for view `DefaultUser`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DefaultUser` (`id` INT, `loggedIn` INT, `failedLoginCount` INT, `logsCount` INT, `default` INT, `activationKey` INT, `userTypeId` INT, `userStatusId` INT, `lastLogin` INT, `lastTimeReturningCount` INT);

-- -----------------------------------------------------
-- function dist
-- -----------------------------------------------------
DROP function IF EXISTS `dist`;

DELIMITER $$


CREATE FUNCTION `dist`(f1 Double,l1 Double,f2 Double,l2 Double) RETURNS double
DETERMINISTIC
  BEGIN
    RETURN 6378000 * acos( sin(RADIANS(f1)) * sin(RADIANS(f2)) + cos(RADIANS(f1)) * cos(RADIANS(f2) ) * cos(RADIANS(l1 - l2)) );
  END$$

DELIMITER ;

-- -----------------------------------------------------
-- function getTotalEventHandlers
-- -----------------------------------------------------
DROP function IF EXISTS `getTotalEventHandlers`;

DELIMITER $$
CREATE FUNCTION `getTotalEventHandlers` (runnerMode INT) RETURNS int(11)
READS SQL DATA
  BEGIN
    DECLARE total_count INT;
    DECLARE cur CURSOR FOR
      SELECT COUNT(EventBinding.id) FROM EventBinding
        JOIN EventModel on EventModel.id = EventBinding.modelId
      WHERE EventModel.mode = runnerMode;

    OPEN cur;

    FETCH cur INTO total_count;

    RETURN total_count;
  END$$

DELIMITER ;

-- -----------------------------------------------------
-- function isPointInRectangular
-- -----------------------------------------------------
DROP function IF EXISTS `isPointInRectangular`;

DELIMITER $$
CREATE FUNCTION `isPointInRectangular`( nwlat Double, nwlon Double, selat Double, selon Double, lat Double, lon Double ) RETURNS boolean
DETERMINISTIC
  BEGIN
    if( selat <= lat AND lat <= nwlat ) then
      if( nwlon - selon <= 0 AND nwlon < lon AND lon < selon ) then
        return true;
      elseif ( nwlon - selon > 0 AND (nwlon < lon OR lon < selon) ) then
        return true;
      end if;
    end if;

    RETURN false;
  END$$

DELIMITER ;

-- -----------------------------------------------------
-- function isPointInCircle
-- -----------------------------------------------------
DROP function IF EXISTS `isPointInCircle`;

DELIMITER $$
CREATE FUNCTION `isPointInCircle`(clat Double,clon Double,r Double,lat Double,lon Double) RETURNS boolean
DETERMINISTIC
  BEGIN
    return dist(clat, clon, lat, lon) <= r;
  END$$

DELIMITER ;

-- -----------------------------------------------------
-- View `DefaultUser`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DefaultUser`;
DROP VIEW IF EXISTS `DefaultUser` ;
CREATE  OR REPLACE VIEW `DefaultUser` AS
  SELECT u.*
  FROM User as u
  WHERE u.default = 1
;

DELIMITER $$

DROP TRIGGER IF EXISTS `Fence_AFTER_DELETE` $$
CREATE TRIGGER `Fence_AFTER_DELETE` AFTER DELETE ON `Fence` FOR EACH ROW
  begin
    delete GeoPoint.* FROM GeoPoint where id = OLD.nwPointId or id = OLD.sePointId;
    delete GeoPoint.* FROM GeoPoint where id in (Select geoPointId from GeoFencePoint where fenceId = OLD.id and `order` is not null);
  end;$$


DROP TRIGGER IF EXISTS `GeoFence_AFTER_DELETE` $$
CREATE TRIGGER `GeoFence_AFTER_DELETE` AFTER DELETE ON `GeoFence` FOR EACH ROW
  DELETE FROM Fence WHERE Fence.id = OLD.fenceId;$$


DROP TRIGGER IF EXISTS `GeoFenceAction_BEFORE_DELETE` $$
CREATE TRIGGER `GeoFenceAction_BEFORE_DELETE` BEFORE DELETE ON `GeoFenceAction` FOR EACH ROW
  DELETE FROM Header WHERE id in (SELECT headerId FROM HeaderToGeoFenceAction WHERE geoFenceActionId = OLD.id)$$


DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;