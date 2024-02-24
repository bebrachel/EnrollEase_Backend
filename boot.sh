#!/bin/bash
java -jar -Dspring.profiles.active=prod ./enrollease.jar &
cd ./scripts/excel_to_mongo/
./trigger.sh 
