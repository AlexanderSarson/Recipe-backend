#!/bin/bash
mvn -Dremote.user=$REMOTE_USER -Dremote.password=$REMOTE_PW tomcat7:deploy
