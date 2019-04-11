# Master of Sciences in Informatics at Grenoble
## Introduction to Distributed Systems
### Final project: Overlay

[![Build Status](https://travis-ci.com/manuporto/mosig-ids-project.svg?token=jPjbUCGgpHyA6zpLgNLq&branch=master)](https://travis-ci.com/manuporto/mosig-ids-project)

---

### Requirements
* Java 11
* RabbitMQ >= 3.7

### Installation and run instructions
1. Generate a distribution with the Gradle wrapper: `./gradlew distZip`
2. Unzip the distribution created at `./build/distributions/` in the directory of your choice
3. Run `./overlay/bin/overlay <virtual ID> <path to network file> <log level> <log file (optional)>`

Note: The last operation must be done as many times as nodes are in the network file (changing the virtual ID). If not, the network will not have all the required nodes running and it will not function properly.
