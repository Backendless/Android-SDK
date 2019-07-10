CREATE USER 'nssSystem'@'%' IDENTIFIED BY 'nssPassword' ;
GRANT Usage ON *.* TO 'nssSystem'@'%';
GRANT Select ON main_backendless.Application TO 'nssSystem'@'%';
FLUSH PRIVILEGES;

