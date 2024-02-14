#!/bin/bash
java -jar -Dspring.profiles.active=prod ./app.jar &
cd ./scripts/excel_to_mongo/
./trigger.sh 
