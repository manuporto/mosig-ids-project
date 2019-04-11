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
3. Run `./<path were the zip was unzipped>/overlay/bin/overlay <virtual ID (starting from 0)> <path to network file> <log level> <log file (optional)>`. An example command would look like, `./overlay/bin/overlay 0 ./res/ringNetwork.json TRACE log0.txt`.

Note: The last operation must be done as many times as nodes are in the network file (changing the virtual ID). If not, the network will not have all the required nodes running and it will not function properly.

Note 2: Log levels must be written in CAPS. The following levels are supported: TRACE, DEBUG, INFO, WARN, ERROR.

Note 3: The network files are located in the `res` folder.

### Sending messages to the network
Since each node has an HTTP server running listening for new messages, first it's necessary to send an HTTP message to the server (the port in which is listening is communicated in the terminal). The payload of the HTTP message needs to be JSON and have the following structure:

```json
{
  "<virtual destination>": "<message to be sent>"
}
```
An example payload coulde be:

```json
{
  "2": "This is a message to virtual node number 2"
}
```

### Other commands
* For generating javadocs: `./gradlew javadocs`
