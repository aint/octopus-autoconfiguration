<p align="center">
	<img src="https://i.imgur.com/FX44gqx.png" alt="Octopus autoconfiguration" width="500">
</p>
<h2 align="center">Microservice Architecture Visualization Tool</h3>

<p align="center">
    <a href="https://jitpack.io/#aint/octopus-autoconfiguration"><img src="https://jitpack.io/v/aint/octopus-autoconfiguration.svg"></a>
    <a href="https://github.com/aint/octopus-autoconfiguration/graphs/commit-activity"><img src="https://img.shields.io/maintenance/yes/2019.svg"></a>
    <a href="https://travis-ci.org/aint/octopus-autoconfiguration"><img src="https://travis-ci.org/aint/octopus-autoconfiguration.svg?branch=master"></a>
    <a href="https://codecov.io/gh/aint/octopus-autoconfiguration"><img src="https://codecov.io/gh/aint/octopus-autoconfiguration/branch/master/graph/badge.svg"></a>
	<br>
    <a href="https://github.com/aint"><img src="https://img.shields.io/badge/ask%20me-anything-1abc9c.svg"></a>
    <a href="https://spring.io/projects/spring-boot"><img src="https://img.shields.io/badge/made%20with-Java/Spring-9f425f.svg"></a>
    <a href="https://github.com/aint/octopus-autoconfiguration/blob/master/LICENSE.txt"><img src="https://img.shields.io/github/license/aint/octopus-autoconfiguration.svg"></a>

</p>


# Octopus: Spring Boot autoconfiguration

Spring Boot autoconfiguration supports for Octopus project.

![Nothing is beyoun our reach](https://lindaivespol.files.wordpress.com/2018/06/pol.jpg)

# Features

This lib supports detection of:
- SQL databases:
    - [x] MySQL/MariaDB
    - [x] PostgreSql
    - [x] MS SQL Server
    - [x] Oracle Database
    - [x] DB2
    - [x] Sybase
    - [x] Teradata
- NoSQL databases:
    - [x] Redis
    - [x] Memcached
    - [x] Hazelcast
    - [x] Infinispan
- Spring
    - [x] Cache
    - [ ] Security
    - [ ] REST/MVC
- AWS
    - [ ] Lambda
    - [ ] S3/SQS/SES/etc
- [ ] git origin url
- [ ] read project's description from build tool

# How it works

```yaml
integration:
  services:
    devaron:
      url: http://devaron.service:8080
      timeout: 10
    coruscant:
      url: http://coruscant.service:8080
      timeout: 20
    corellia:
      url: http://corellia.service:8080
      timeout: 30
  lambdas:
    bespin:
      name: fully-qualified-name
      qualifier: RELEASE
  third-party:
    tatooine: https://some.service

octopus.integration.prefix: integration
octopus-server.url: http://localhost:5000/consume
```


# Installation

**Step 1.** Add the JitPack repository to your pom.xml file 

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

**Step 2.** Add the dependency

```xml
<dependency>
    <groupId>com.github.aint</groupId>
    <artifactId>octopus-autoconfiguaration</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```   

**That's it!**

# Dependencies
 - Java 8
 - Any build tool using Maven Central, or direct download
 - Spring 4.x or 5.x
 - Spring Boot 2.x (optional)



https://github.com/zalando/logbook

<a href="https://libraries.io/aint/octopus-autoconfiguration"><img src="https://img.shields.io/librariesio/github/aint/octopus-autoconfiguration.svg"></a>
<a href="https://sourcegraph.com/github.com/aint/octopus-autoconfiguration" title="Octopus on Sourcegraph"><img src="https://img.shields.io/sourcegraph/rrc/github.com/aint/octopus-autoconfiguration.svg" alt="Octopus on Sourcegraph"></a>

