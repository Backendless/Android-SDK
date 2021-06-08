# First Configuration

[API host configuration](#api_conf)<br>
[Mysql connection configuration](#mysql_conf)<br>
[Redis connection configuration](#redis_conf)<br>
[Mongo connection configuration](#mongo_conf)<br>
[Backendless console host configuration](#console_conf)<br>

### <a name="api_conf">API host configuration</a>

- go to `http://<consul-host>/ui/#/dc1/kv/config/server/publicHost/edit` provide yor public domain or IP that you will use for API, for example `api.my-domain.com` 
- if you use `https` for API then change `http://<consul-host>/ui/#/dc1/kv/config/server/publicProtocol/edit` value to `https`
- go to `http://<consul-host>/ui/#/dc1/kv/config/server/publicPort/edit` provide port that you will use for API, by default it is 80, if you use `https` protocol, then change the port to `443`

### <a name="mysql_conf">Mysql connection configuration</a>

- go to `http://<consul-host>/ui/#/dc1/kv/config/connection/main/host/edit` and provide a domain or IP address of mysql 
- go to `http://<consul-host>/ui/#/dc1/kv/config/connection/main/port/edit` and provide port, default value is 3306
- go to `http://<consul-host>/ui/#/dc1/kv/config/connection/main/user/edit` and provide mysql user
- go to `http://<consul-host>/ui/#/dc1/kv/config/connection/main/password/edit` and provide password for the user

### <a name="redis_conf">Redis connection configuration</a>

Backendless can store all data in one redis or split to number of redises. You can find redis section by the following path `http://<consul-host>/ui/#/dc1/kv/config/redis/`. 
The easiest way is to configure 2 redises for coderunner debug and default redis for other parts of backendless.

- go to `http://<consul-host>/ui/#/dc1/kv/config/redis/default/host/edit` and provide domain or ip of the redis server
- go to `http://<consul-host>/ui/#/dc1/kv/config/redis/default/port/edit` and provide port of the redis server
- if redis is a password-protected go to `http://<consul-host>/ui/#/dc1/kv/config/redis/default/password/edit` and provide a password

do the same for other sections by the following path `http://<consul-host>/ui/#/dc1/kv/config/redis/`


Most of the users will not use it, but If you use redis sentinel, you should enable it. To do that create key and change value for `http://<consul-host>/ui/#/dc1/kv/config/redis/sentinel/enabled/edit`

- add sentinel servers addresses `http://<consul-host>/ui/#/dc1/kv/config/redis/sentinel/addresses/edit` and provide domain or ip of the redis sentinel. Example: redis-sentinel1:26380;redis-sentinel2:26380;redis-sentinel3:26380

- go to `http://<consul-host>/ui/#/dc1/kv/config/redis/sentinel/default/master_name/edit` add for else redis servers

- if redis is a password-protected go to `http://<consul-host>/ui/#/dc1/kv/config/redis/sentinel/analytics/password/edit` and provide a password

### <a name="mongo_conf">Mongo connection configuration</a>

Create 3 databases with the following names:
- analytics
- push_templates
- application_settings

Provide connection parameters for each db:

- go to `http://<consul-host>/ui/#/dc1/kv/config/mongo/connection/analytics/` and provide `host`, `port` and if there is user and password create `user` key and `password` key for the `analytics` database

- go to `http://<consul-host>/ui/#/dc1/kv/config/mongo/connection/billing/` and provide `host`, `port` and if there is user and password create `user` key and `password` key for the `billing  ` database

- go to `http://<consul-host>/ui/#/dc1/kv/config/mongo/connection/pushtemplates/` and provide `host`, `port` and if there is user and password create `user` key and `password` key for the `push_templates` database

- go to `http://<consul-host>/ui/#/dc1/kv/config/mongo/connection/settings/` and provide `host`, `port` and if there is user and password create `user` key and `password` key for the `application_settings  ` database

  
### <a name="console_conf">Backendless console host configuration</a>

Go to `http://<consul-host>/ui/#/dc1/kv/config/console/rootUrl/edit` and provide a full url to the backendless console. 
For example you would like to have `dev.my-domain.com` as a domain for the backendless console, then you have to put the following value: `https://dev.my-domain.com`. 
If you would like to use IP for example `10.0.3.16` and port `31900` then you should put the following value: `http://10.0.3.16:31900`