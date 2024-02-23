import os
import pickle
from google.auth.transport.requests import Request
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build

# id таблицы, привязанной к нашей анкете:
FORM_SHEET_ID = '1CGuT15SM1F_Sk13IGLtHLwagEaJc4rYS5fxSFWczRRU'
# Имя листа с ответами:
sheet_name = 'Ответы на форму (1)'
# Номер (и буква) столбца с флагом:
flag_index = 19 
flag_symbol = 'S'
# id шаблона портфолио:
PORTFOLIO_PATTERN_ID = '10nMAmyV3aTOQG5B3JDu1AIKuSN1MrkxUZ1GOmC-Xo0w'
# id папки, в которой будут папки участников конкурса портфолио:
PARENT_FOLDER_ID = '1Hq7niJPHWg2WJJQDZj8gM8YjiBWmdW_x'


# Список необходимых разрешений:
SCOPES = ['https://www.googleapis.com/auth/documents', 
          'https://www.googleapis.com/auth/drive']

# Путь к файлу токена:
TOKEN_PICKLE = 'token.pickle'



def authenticate():
    creds = None
    # Проверка наличия файла с учетными данными:
    if os.path.exists(TOKEN_PICKLE):
        with open(TOKEN_PICKLE, 'rb') as token:
            creds = pickle.load(token)
    # Если учетные данные не действительны или отсутствуют, создаем новые:
    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())
        else:
            flow = InstalledAppFlow.from_client_secrets_file(
                'credentials.json', SCOPES)
            creds = flow.run_local_server(port=0)
        # Сохранение учетных данных для следующего запуска:
        with open(TOKEN_PICKLE, 'wb') as token:
            pickle.dump(creds, token)
    return creds


def get_google_sheets_data(creds):
    service = build('sheets', 'v4', credentials=creds) 
    result = service.spreadsheets().values().get(
        spreadsheetId=FORM_SHEET_ID, range=sheet_name).execute()
    values = result.get('values', [])
    print(values)
    return values 


def update_portfolio_with_data(creds, data, portfolio_id):
    docs_service = build('docs', 'v1', credentials=creds)

    # Получаем содержимое документа:
    document = docs_service.documents().get(documentId=portfolio_id).execute()
    content = document['body']['content']

    # Заменяем ключи в тексте на значения из словаря:
    for row_dict in data:
        for paragraph in content:
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

    docs_service.documents().batchUpdate(documentId=portfolio_id, body={'requests': requests}).execute()
    #print("Document updated successfully.") #для проверки работы


def create_folder(creds, parent_folder_id, folder_name):
    service = build('drive', 'v3', credentials=creds)

    # Создание метаданных для новой папки:
    folder_metadata = {
        'name': folder_name,
        'parents': [parent_folder_id],
        'mimeType': 'application/vnd.google-apps.folder'
    }

    # Создание запроса на создание новой папки:
    folder = service.files().create(body=folder_metadata, fields='id').execute()
    #print(f'Папка "{folder_name}" успешно создана с ID: {folder.get("id")}')
    return folder.get("id")


def copy_doc_to_folder(creds, destination_folder_id, document_id, copy_name):
    service = build('drive', 'v3', credentials=creds)

    # Создание метаданных для копии документа:
    copy_metadata = {
        'name': copy_name,
        'parents': [destination_folder_id]
    }

    # Копирование документа в указанную папку:
    copied_document = service.files().copy(fileId=document_id, body=copy_metadata).execute()
    #print(f'Документ успешно скопирован в папку с ID: {destination_folder_id}. Новый ID документа: {copied_document.get("id")}')
    return copied_document.get("id")


def update_flag_in_sheet(creds, row_index):
    service = build('sheets', 'v4', credentials=creds)
    range_name = f'{sheet_name}!{flag_symbol}{row_index}'  
    print(range_name)
    value_range_body = {'values': [[str(True)]]}  # Обновляем значение флага
    service.spreadsheets().values().update(
        spreadsheetId=FORM_SHEET_ID, range=range_name,
        valueInputOption='RAW', body=value_range_body).execute()


def main():
    creds = authenticate()

    values = get_google_sheets_data(creds)
    for row in values:
        if len(row) == flag_index - 1: 
            # Выполняем действия только для строк, где флаг пустой:
            data = {values[0][i]: row[i] for i in range(len(values[0]) - 1)}
            applicant_name = data.get('Фамилия') + ' ' + data.get('Имя') + ' ' + data.get('Отчество')
            print(applicant_name)

            folder_id = create_folder(creds, PARENT_FOLDER_ID, applicant_name)
            portfolio_id = copy_doc_to_folder(creds, folder_id, PORTFOLIO_PATTERN_ID, applicant_name)
            update_portfolio_with_data(creds, [data], portfolio_id)

            row_index = values.index(row) + 1  # Получаем индекс строки (нумерация с 1)
            # Обновляем значение флага в текущей строке:
            update_flag_in_sheet(creds, row_index)



if __name__ == '__main__':
    main()
