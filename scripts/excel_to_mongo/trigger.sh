#!/bin/bash

DIRECTORY="/scripts/excel_to_mongo/xlsx"
PROCESSED_DIR="/scripts/excel_to_mongo/processed"

inotifywait -m -e create --format '%f' "$DIRECTORY" | while read FILENAME
do
    FILE=$(find "$DIRECTORY" -type f -printf '%T+ %p\n' | sort -r | head -n 1 | cut -d' ' -f2-)
    if [[ "$FILE" == *.xlsx ]] && [ ! -f "$PROCESSED_DIR/$FILENAME" ]; then
        echo "New .xlsx file detected: $FILE"
        python3 "/scripts/excel_to_mongo/script.py" "$FILE"

        # Move or rename the file to indicate it has been processed
        mv "$FILE" "$PROCESSED_DIR"
    fi
done
