#!/bin/bash

DIRECTORY="./scripts/excel_to_mongo/xlsx"

inotifywait -m -e create --format '%f' "$DIRECTORY" | while read FILE
do
    if [[ "$FILE" == *.xlsx ]]; then
        echo "New .xlsx file detected: $FILE"
        python3 "./scripts/excel_to_mongo/script.py" "$DIRECTORY/$FILE"
    fi
done
