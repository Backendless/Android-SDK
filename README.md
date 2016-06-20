Backendless SDK for Java and Android [![Build Status](https://travis-ci.org/Backendless/Android-SDK.svg)](https://travis-ci.org/Backendless/Android-SDK)
====================================
Welcome to Backendless! In this document you will find the instructions for getting up and running with Backendless quickly. The SDK you downloaded contains a library [(jar file)](https://github.com/Backendless/Android-SDK/blob/master/out/backendless.jar?raw=true) with the APIs, which provide access to the Backendless services. These services enable the server-side functionality for developing and running mobile and desktop applications. Follow the steps below to get started with Backendless:

1. **Create Developer Account.** An account is required in order to create and manage your Backendless backend. You can login to our console at: http://backendless.com/develop
2. **Locate Application ID and Secret Key.** The console is where you can manage the applications, their configuration settings and data. Before you start using any of the APIs, make sure to select an application in the console and open the "Manage" section. The "App Settings" screen contains the application ID and secret API keys, which you will need to use in your code.
3. **Open Backendless Examples.** The SDK includes several examples demonstrating some of the Backendless functionality. The /samples folder contains an IDEA project file (AndroidSampleApps.ipr) combining all the samples. 
4. **Copy/Paste Application ID and Secret Key.**  Each example must be configured with the application ID and secret key generated for your application. 
5. **Run Sample Apps.**

Setting up your app
====================================
Maven Integration
------------------------------------
The backendless client library for Android and Java is available through the central [Maven](http://mvnrepository.com/artifact/com.backendless/backendless) repository. Since the version of Backendless deployed to maven changes frequently, make sure to lookup the latest version number from Maven Central. To add a dependency for the  library, add the following to pom.xml (make sure to replace "VERSION FROM MAVEN" with a specific version number):
>     <dependency>
>       <groupId>com.backendless</groupId>
>       <artifactId>backendless</artifactId>
>       <version>VERSION FROM MAVEN</version> 
>     </dependency>

Gradle Configuration
------------------------------------
To configure Backendless library in Gradle, add the following line into the "dependencies" element in gradle.build ((make sure to replace "VERSION FROM MAVEN" with a specific version number):
>    dependencies {

>      compile 'com.backendless:backendless:VERSION FROM MAVEN'

>    }
