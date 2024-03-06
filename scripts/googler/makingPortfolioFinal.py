from itertools import product

from Google import folder_create, PORTFOLIO_FOLDER_ID, PORTFOLIO_FORM_SHEET_ID, PORTFOLIO_FORM_SHEET_LIST_NAME, \
    PORTFOLIO_DOC_TEMPLATE_ID, GOOGLE_SHEETS_SERVICE, GOOGLE_DOCS_SERVICE, GOOGLE_DRIVE_SERVICE, give_user_permission

flag_index = 19
flag_symbol = '1'


def get_google_sheets_data():
    # what is range?
    result = GOOGLE_SHEETS_SERVICE.spreadsheets().values().get(
        spreadsheetId=PORTFOLIO_FORM_SHEET_ID, range=PORTFOLIO_FORM_SHEET_LIST_NAME).execute()
    values = result.get('values', [])
    # print(values)
    return values


def update_portfolio_with_data(data, portfolio_id):
    # Получаем содержимое документа:
    document = GOOGLE_DOCS_SERVICE.documents().get(documentId=portfolio_id).execute()
    content = document['body']['content']

    # Заменяем ключи в тексте на значения из словаря:
    for row_dict, paragraph in product(data, content):
        if 'paragraph' in paragraph:
            elements = paragraph['paragraph']['elements']
            for element in elements:
                if 'textRun' in element:
                    text = element['textRun']['content']
                    for header, value in row_dict.items():
                        if f'#{header}#' in text:
                            text = text.replace(f'#{header}#', value)
                            element['textRun']['content'] = text

    # Обновляем содержимое документа:
    requests = [
        {
            'replaceAllText': {
                'containsText': {
                    'text': f'#{header}#',
                    'matchCase': True
                },
                'replaceText': value
            }
        } for row_dict in data for header, value in row_dict.items()
    ]

    GOOGLE_DOCS_SERVICE.documents().batchUpdate(documentId=portfolio_id, body={'requests': requests}).execute()
    # print("Document updated successfully.") #для проверки работы


def copy_doc_to_folder(destination_folder_id, document_id, copy_name):
    # Создание метаданных для копии документа:
    copy_metadata = {
        'name': copy_name,
        'parents': [destination_folder_id]
    }

    # Копирование документа в указанную папку:
    copied_document = GOOGLE_DRIVE_SERVICE.files().copy(fileId=document_id, body=copy_metadata).execute()
    # print(f'Документ успешно скопирован в папку с ID: {destination_folder_id}. Новый ID документа: {copied_document.get("id")}')
    return copied_document.get("id")


def update_flag_in_sheet(row_index):
    range_name = f'{PORTFOLIO_FORM_SHEET_LIST_NAME}!{flag_symbol}{row_index}'
    # print(range_name)
    value_range_body = {'values': [[str(True)]]}  # Обновляем значение флага
    GOOGLE_SHEETS_SERVICE.spreadsheets().values().update(
        spreadsheetId=PORTFOLIO_FORM_SHEET_ID, range=range_name,
        valueInputOption='RAW', body=value_range_body).execute()


def main():
    values = get_google_sheets_data()
    for row in values:
        if len(row) == flag_index - 1:
            # Выполняем действия только для строк, где флаг пустой:
            data = {values[0][i]: row[i] for i in range(len(values[0]) - 1)}
            applicant_name = data.get('Фамилия') + ' ' + data.get('Имя') + ' ' + data.get('Отчество')
            # print(applicant_name)

            folder_id = folder_create(applicant_name, PORTFOLIO_FOLDER_ID)
            portfolio_id = copy_doc_to_folder(folder_id, PORTFOLIO_DOC_TEMPLATE_ID, applicant_name)
            update_portfolio_with_data([data], portfolio_id)

            row_index = values.index(row) + 1  # Получаем индекс строки (нумерация с 1)
            # Обновляем значение флага в текущей строке:
            update_flag_in_sheet(row_index)
            give_user_permission(folder_id, data.get('Электронная почта'))


if __name__ == '__main__':
    main()
