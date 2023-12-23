import os
import pickle
from google_auth_oauthlib.flow import InstalledAppFlow
from google.auth.transport.requests import Request
from googleapiclient.discovery import build

SCOPES = ['https://www.googleapis.com/auth/forms', 'https://www.googleapis.com/auth/spreadsheets.readonly']

TOKEN_PICKLE = 'token.pickle'

def authenticate():
    creds = None
    # Проверка наличия файла с учетными данными
    if os.path.exists(TOKEN_PICKLE):
        with open(TOKEN_PICKLE, 'rb') as token:
            creds = pickle.load(token)
    # Если учетные данные не действительны или отсутствуют, создаем новые
    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())
        else:
            flow = InstalledAppFlow.from_client_secrets_file(
                'credentials.json', SCOPES)
            creds = flow.run_local_server(port=0)
        # Сохранение учетных данных для следующего запуска
        with open(TOKEN_PICKLE, 'wb') as token:
            pickle.dump(creds, token)
    return creds

def main():
    creds = authenticate()

    # Создание объекта для работы с Google Sheets API
    service = build('sheets', 'v4', credentials=creds)

    # ID таблицы Google Sheets, содержащей ответы на анкету
    spreadsheet_id = '1N1PKpouDO3wVq9UEDuTbLfPWMW3RdLWOAI2NMM1syVg'

    # Имя листа с ответами
    sheet_name = 'Ответы на форму (1)'

    # Получение данных из таблицы
    result = service.spreadsheets().values().get(
        spreadsheetId=spreadsheet_id, range=sheet_name).execute()

    values = result.get('values', [])

    if not values:
        print('No data found.')
    else:
        print('Responses:')
        for row in values:
            print(', '.join(row))

if __name__ == '__main__':
    main()