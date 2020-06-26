# OHDMImportTool

This tool is a TomCat Servlet and allows you to import [Shapefiles](https://wiki.openstreetmap.org/wiki/Shapefiles) into a cache database. The user can access the cached data to update the meta data of shapes like valid dates, the name or the classification of the shape.

## Information

You can find more information about how the tool works, on the [Wiki](https://github.com/OpenHistoricalDataMap/OHDMImportTool/wiki). The source code is [available on GitHub](https://github.com/OpenHistoricalDataMap/OHDMImportTool).

## Installation

### Requirements

- [TomCat Webserver](https://cwiki.apache.org/confluence/display/TOMCAT/HowTo)
- [PostgresSQL](https://www.postgresql.org/download/) database with [PostGIS Extension](http://postgis.net/install/)
 
### Installation

Clone this project:
```
$ git clone https://github.com/OpenHistoricalDataMap/OHDMImportTool.git
```

### Run with IntelliJ IDE

- open the project with IntelliJ 
- click in 'Run' on 'Edit Configuration'
- add 'TomCat Server' to configuration
- set URL to root path (http://localhost:8080/)
- before lunch: add 'OHDMInformation.war' as Build Artifact

![IntelliJ TomCat configuration 1](img/image_01.png?raw=true)


#### deployment
- set 'Application context' to root path

![IntelliJ TomCat configuration 2](img/image_02.png?raw=true)
