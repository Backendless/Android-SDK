# Real time service configuration

Each instance of `bl-rt-server` should have their own public host or public IP address. 
Clients get direct connection host and port to communicate with the server, by calling the `lookup` on the `bl-server` service.


### Kubernetes configuration

Following documentation suppose that you have kubernetes and created 3 workers:
- `kube-worker-1`
- `kube-worker-2`
- `kube-worker-3` 

and port `443` is free and available from your client application

##### Create domains
Create the same the number of domains as number of kube workers that you have. For example, if you have 3 workers, create 3 domains:

- `rt1-project.backendless.com` should be pointed to `kube-worker-1`
- `rt2-project.backendless.com` should be pointed to `kube-worker-2`
- `rt3-project.backendless.com` should be pointed to `kube-worker-3`

*Note*: using the `backendless.com` domain is not required

***

##### Add configuration  
Each worker should contains the `config.env` file with following variables:
```
export BL_RT_SERVER_ID=rt-server-1
export BL_RT_SERVER_CONNECTION_HOST=rt1-project.backendless.com
```
for `kube-worker-1`

```
export BL_RT_SERVER_ID=rt-server-2
export BL_RT_SERVER_CONNECTION_HOST=rt2-project.backendless.com
```
for `kube-worker-2`

```
export BL_RT_SERVER_ID=rt-server-3
export BL_RT_SERVER_CONNECTION_HOST=rt3-project.backendless.com
```
for `kube-worker-3`


`config.env` file should be mounted to the service as persistence storage. Path in the service should be `/opt/backendless/config.env`. 
[Rt server yml file](./services/yml/bl-rt-server.yml) already contains the mount to the `/opt/backendless/rt/config/config.env` file that is located
on the worker. 
If you want to change the location of the file, do not forget to change the mount.

The following env variables rewrite following keys from consul
- `BL_RT_SERVER_ID` rewrites `config/rt-server/socketServer/id`
- `BL_RT_SERVER_CONNECTION_HOST` rewrites `config/rt-server/socketServer/connection-host`

Also there are additional variables that can rewrite consul variables:
- `BL_RT_SERVER_CONNECTION_PROTOCOL` rewrites `config/rt-server/socketServer/connection-port`
- `BL_RT_SERVER_CONNECTION_PORT` rewrites `config/rt-server/socketServer/connection-protocol`
  

##### Create `bl-rt-server`
Create a service with the following [yml file](./services/yml/bl-rt-server.yml)  change image version before import

##### Port configuration

There are two options for how to change the port that will be available for your client application:
- in the consul, by changing `config/rt-server/socketServer/connection-port` key
- create the env variable `BL_RT_SERVER_CONNECTION_PORT`

[Rt server yml file](./services/yml/bl-rt-server.yml) contains configuration that map 443 port to 5000
Go to consul web interface and set following values:
- 443 for `config/rt-server/socketServer/connection-port` key
- 5000 for `config/rt-server/socketServer/port` key




