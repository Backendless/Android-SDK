# Backendless Pro
This is a repo for the distribution of Backendless Pro - the self-hosted version of the product. Backendless Pro is a complete technology stack, it includes all Backendless features without any limitations whatsoever. Backendless Pro is based on the docker architecture and thus can run in any environment supported by Docker. Backendless Pro consists of multiple components, such as a web server, the database, application engine, etc. Each component runs as a separate docker image thus allowing for horizontal scalability of each type of component. All Backendless docker images are published into [DockerHub](https://hub.docker.com/u/backendless/).

## Getting Started
To get started:
1. Download the `quickstart` [archive](TBD), which contains all the management scripts for the product. 
1. Expand the archive into a directory.
1. Open a command prompt/terminal window and change the current directory to the one from the item above.
1. Run the following command, which will download and install all the required images:
    ```
    ./backendless_install.sh
    ```

1. When the installation script runs, it will prompt you to enter port numbers for the following Backendless components:
    * MySQL (default port - 3306)
    * MongoDB (default port - 27017)
    * Redis (default port - 6380)
    * The Backendless server, which is the app server (default port 9000) 
    * The web console server (default port 80)
    * RT (Real Time) server (default port 5000)
   Each prompt will show the default port number, to accept it, simply press **Enter**, otherwise, enter a port number.
1. When the command finishes, make sure Docker is running on your machine.
1. Run Backendless Pro using the following command:
    ```
    ./backendless_start.sh
    ```
1. During the installation you may see some errors, for example:
    ```
    [ERROR] WaiterService - Consul is accessible, but there is no version
    ```
    You can ignore errors like that, they occur because various product components start in random order, but eventually auto-discover each other. Depending how fast your computer is and how much memory it has, the complete launch may take between 3 to 10 minutes. You will know it is done when you see the following message in the script output:
    ````
    Backendless server is ready
    ````
1. Now you can open and login to Backendless Console by openning `http://localhost:80` (if you specified a different port for `the web console server`, make sure to use it in the URL). You will see the Backendless Console login screen:
1. While the command is running, make sure to request an evaluation license for Backendless Pro. To do this open the [Backendless Pro License Request page](https://backendless.com/products/pro/license-request/), fill out and submit the form - you will receive your license key via email right away.
1. 
