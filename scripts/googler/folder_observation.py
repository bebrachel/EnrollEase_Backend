from Google import PORTFOLIO_FOLDER_ID, GOOGLE_DRIVE_SERVICE_METADATA
from pymongo import MongoClient

# MongoDB setup
client = MongoClient("mongodb://mongodb",
                     27017)  # замените на свои параметры подключения
db = client['proj']  # замените на название вашей базы данных
collection = db['applicants_portfolio']  # замените на название вашей коллекции


def check_for_updates():
    print("folder observation script started")
    # 'folder_id' should be replaced with the actual Google Drive folder ID you're monitoring
    query = f"parents = '{PORTFOLIO_FOLDER_ID[0]}'"
    results = (GOOGLE_DRIVE_SERVICE_METADATA.files().list(q=query, spaces='drive',
                                                          fields='files(id, name, modifiedTime)', )
               # ,
               # spaces='drive',
               # fields='files(id, name, modifiedTime)',
               # orderBy='modifiedTime desc')
               .execute())
    items = results.get('files', [])
    nextPageToken = results.get('nextPageToken')
    while nextPageToken:
        res = GOOGLE_DRIVE_SERVICE_METADATA.files().list(q=query, spaces='drive',
                                                         fields='files(id, name, modifiedTime)',
                                                         pageToken=nextPageToken).execute()

        items.extend(res.get('files', []))
        nextPageToken = res.get('nextPageToken')
    for item in items:
        # Check if this file has been updated since last check
        # print(collection.find_raw_batches().)
        existing_file = collection.find_one({"folderId": item['id']})
        if existing_file['folderUpdatedAt'] != item['modifiedTime']:
            # Update the database with the new or updated file info
            collection.update_one({"folderId": item['id']}, {"$set": {"folderUpdatedAt": item['modifiedTime']}},
                                  upsert=True)
            print(item['id'] + " has new value " + item['modifiedTime'])
    print("folder observation script ended")


if __name__ == '__main__':
    check_for_updates()
