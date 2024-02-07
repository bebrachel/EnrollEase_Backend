def export_from_excel(filename):
    import pandas as pd
    from pymongo import MongoClient
    excel_file = filename
    data_frame = pd.read_excel(excel_file)
    data_frame.dropna(axis=1, how='all', inplace=True)

    # Подключение к MongoDB
    client = MongoClient("mongodb://mongodb",
                         27017)  # замените на свои параметры подключения
    db = client['proj']  # замените на название вашей базы данных
    collection = db['applicants']  # замените на название вашей коллекции

    # Установите имя столбца, который вы используете в качестве primary_key
    primary_key = 'ФизическоеЛицоСНИЛС'

    s = ""
    with open('../googler/send_message/templates/АнкетаГуглдискИнструкция.txt') as openfileobject:
        s = openfileobject.read()
    # Импорт данных в MongoDB
    for _, row in data_frame.iterrows():
        document = row.to_dict()
        c_d = document.copy()
        for i in document:
            if str(document[i]) == 'nan' or i == primary_key:
                c_d.pop(i)
        doc = {'_id': document[primary_key], 'data': c_d, 'status': 'Подано заявление'}
        result = collection.update_one({'_id': doc['_id']}, {"$set": doc}, upsert=True).raw_result
        if not result.get('updatedExisting'):
            mail = document['Email']
            name = document['ФИО']
            import googler.create_achievements_folder.script
            import googler.send_message.script
            googler.create_achievements_folder.script.create(name, mail)
            googler.send_message.script.gmail_create("НГУ Магистратура", s, [mail])


if __name__ == '__main__':
    import sys

    sys.path.append('/scripts')
    args = sys.argv
    print("Import has started.")
    export_from_excel(args[1])
    print("Import has ended.")
