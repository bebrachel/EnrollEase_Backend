def export_from_excel(filename):
    import pandas as pd
    import os
    from pymongo import MongoClient
    excel_file = filename
    data_frame = pd.read_excel(excel_file)
    data_frame.dropna(axis=1, how='all', inplace=True)

    # Подключение к MongoDB
    client = MongoClient(os.environ.get("MONGODBURL"),
                         27017)  # замените на свои параметры подключения
    db = client['proj']  # замените на название вашей базы данных
    collection = db['applicants']  # замените на название вашей коллекции

    # Установите имя столбца, который вы используете в качестве primary_key
    primary_key = 'ФизическоеЛицоСНИЛС'

    # Импорт данных в MongoDB
    for _, row in data_frame.iterrows():
        document = row.to_dict()
        collection.update_one({primary_key: document[primary_key]}, {"$set": document}, upsert=True)


if __name__ == '__main__':
    import sys

    args = sys.argv
    print("Import has started.")
    export_from_excel(args[1])
    print("Import has ended.")