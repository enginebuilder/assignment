#!/bin/sh

MY_PATH="`dirname \"$0\"`"
cd "$MY_PATH/.."

java \
-server -Xms256m -Xmx512m -Xss10m -cp config:target/dependency/*:target/* \
com.freecharge.wordcount.WordCountServer
