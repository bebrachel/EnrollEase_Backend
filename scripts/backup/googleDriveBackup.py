import os
import time
import pickle
import shutil
import zipfile
import schedule
import datetime
import mimetypes
from googleapiclient.discovery import build
from google.oauth2.credentials import Credentials
from google.auth.transport.requests import Request
from googleapiclient.http import MediaIoBaseDownload
from google_auth_oauthlib.flow import InstalledAppFlow


# Папка, которую сохраняем:
google_drive_folder_id = "1Hq7niJPHWg2WJJQDZj8gM8YjiBWmdW_x"
# Папка, куда сохраняем:
local_folder_path = "D:\\"


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


def save_folder_from_google_drive(creds, folder_id, folder_p):
    service = build('drive', 'v3', credentials=creds)

    folder = service.files().get(fileId=folder_id).execute()
    folder_name = folder['name']

    folder_path = os.path.join(folder_p, folder_name)
    os.makedirs(folder_path, exist_ok=True)

    # Получаем список файлов в папке на Google Диске:
    results = service.files().list(
        q=f"'{folder_id}' in parents and trashed=false",
        fields="files(id, name, mimeType)").execute()
    items = results.get('files', [])

    for item in items:
        if item['mimeType'] != 'application/vnd.google-apps.folder':
            file_id = item['id']
            file_name = item['name']
            file_path = os.path.join(folder_path, file_name)
            # Определение MIME-типа файла:
            if 'application/vnd.google-apps.document' in item['mimeType']:
                export_mime_type = 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'  
                extension = '.docx'
            elif 'application/vnd.google-apps.spreadsheet' in item['mimeType']:
                export_mime_type = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'  
                extension = '.xlsx'
            elif 'application/vnd.google-apps.presentation' in item['mimeType']:
                export_mime_type = 'application/vnd.openxmlformats-officedocument.presentationml.presentation'  
                extension = '.pptx'
            else:
                export_mime_type = None
                extension = mimetypes.guess_extension(item['mimeType'])
            
            # Экспорт файла в нужный формат:
            if export_mime_type:
                request = service.files().export_media(fileId=file_id, mimeType=export_mime_type) 
                with open(file_path + extension, 'wb') as f:
                    downloader = MediaIoBaseDownload(f, request)
                    done = False
                    while done is False:
                        status, done = downloader.next_chunk()
            else:
                # Если формат не определен, сохраняем как есть:
                request = service.files().get_media(fileId=file_id)
                with open(file_path + extension, 'wb') as f:
                    downloader = MediaIoBaseDownload(f, request)
                    done = False
                    while done is False:
                        status, done = downloader.next_chunk()
        else:
            new_local_path = folder_p + folder_name + "\\"
            save_folder_from_google_drive(creds, item['id'], new_local_path)


def zip_folder(folder_path, zip_file_path):
    with zipfile.ZipFile(zip_file_path, 'w', zipfile.ZIP_DEFLATED) as zipf:
        for root, dirs, files in os.walk(folder_path):
            for file in files:
                file_path = os.path.join(root, file)
                zipf.write(file_path, os.path.relpath(file_path, folder_path))
            for dir in dirs:
                dir_path = os.path.join(root, dir)
                zipf.write(dir_path, os.path.relpath(dir_path, folder_path))


def delete_folder(folder_path):
    try:
        shutil.rmtree(folder_path)
    except Exception as e:
        print(f"Error in {folder_path}: {e}")


def create_backup_from_google_drive():
    creds = authenticate()
    service = build('drive', 'v3', credentials=creds)

    save_folder_from_google_drive(creds, google_drive_folder_id, local_folder_path)

    folder = service.files().get(fileId=google_drive_folder_id).execute()
    folder_name = folder['name']

    current_date = datetime.date.today()
    current_time = datetime.datetime.now().time()
    formatted_date = current_date.strftime("%d.%m.%Y")
    formatted_time = current_time.strftime("%H-%M-%S")

    zip_file_name = f"{folder_name} backup_{formatted_date}_{formatted_time}.zip"
    zip_file_path = os.path.join(local_folder_path, zip_file_name)
    zip_folder(local_folder_path + folder_name + "\\", zip_file_path)
    
    delete_folder(local_folder_path + folder_name + "\\")



def main():
    schedule.every().day.at("21:10").do(create_backup_from_google_drive)
    # Ниже ежеминутный бэкап для проверки работы скрипта:
    #schedule.every().minutes.do(create_backup_from_google_drive)

    while True:
        schedule.run_pending()
        time.sleep(1)
    


if __name__ == '__main__':
    main()
