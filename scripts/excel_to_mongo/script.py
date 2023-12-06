def export_from_excel(filename):
    import pandas as pd
    from pymongo import MongoClient
    excel_file = filename
    data_frame = pd.read_excel(excel_file)
    data_frame.dropna(axis=1, how='all', inplace=True)

    # Подключение к MongoDB
    client = MongoClient("",
                         27017)  # замените на свои параметры подключения
    db = client['proj']  # замените на название вашей базы данных
    collection = db['applicants']  # замените на название вашей коллекции

    # Установите имя столбца, который вы используете в качестве primary_key
    primary_key = 'ФизическоеЛицоСНИЛС'

    # Импорт данных в MongoDB
    for _, row in data_frame.iterrows():
        document = row.to_dict()
        c_d = document.copy()
        for i in document:
            if str(document[i]) == 'nan' or i == primary_key:
                c_d.pop(i)
        doc = {primary_key: document[primary_key], 'data': c_d}
        result = collection.update_one({primary_key: document[primary_key]}, {"$set": doc}, upsert=True).raw_result
        if not result.get('updatedExisting'):
            mail = document['Email']
            name = document['ФИО']
            import os
            os.system(
                f'cd ../google_drive_creation && python script.py "Индивидуальные достижения {name}" "{mail}"')


if __name__ == '__main__':
    import sys

    args = sys.argv
    print("Import has started.")
    export_from_excel(args[1])
    print("Import has ended.")
