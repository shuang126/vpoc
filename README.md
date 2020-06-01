# vpoc
visualized prove of conception (vPoC)

# start kafka
```bash
cd /Users/Shared/kafka_2.12-2.5.0
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties
```

# create a topic for jvm
```bash
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic jvm
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

# install mongodb
```bash
brew tap mongodb/brew
brew install mongodb-community@4.2
```

# start and stop mongodb
https://docs.mongodb.com/manual/tutorial/install-mongodb-on-os-x/
```bash
brew services start mongodb-community@4.2
brew services stop mongodb-community@4.2
ps aux | grep -v grep | grep mongod
/usr/local/var/log/mongodb/mongo.log
```