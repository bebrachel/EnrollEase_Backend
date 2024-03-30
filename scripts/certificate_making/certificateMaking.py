import os
import pickle
import shutil
from docx import Document
from docx.shared import Pt
from enum import Enum, auto
from googleapiclient.discovery import build
from googleapiclient.http import MediaFileUpload
from google.auth.transport.requests import Request
from google_auth_oauthlib.flow import InstalledAppFlow

# Список необходимых разрешений:
SCOPES = ['https://www.googleapis.com/auth/documents', 
          'https://www.googleapis.com/auth/drive']

# Путь к файлу токена:
TOKEN_PICKLE = 'token.pickle'

# Пути до шаблонов сертификатов:
WINNER_TEMPLATE_FILE_PATH = 'D:/winner_certificate_template.docx'
FINALIST_TEMPLATE_FILE_PATH = 'D:/finalist_certificate_template.docx'


class Role(Enum):
    WINNER = auto()
    FINALIST = auto()


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


def upload_docx_to_google_drive(creds, file_path, folder_id):
    service = build('drive', 'v3', credentials=creds)

    folder_path, file_name = os.path.split(file_path)
    file_metadata = {
        'name': os.path.basename(file_name),
        'parents': [folder_id]
    }
    media = MediaFileUpload(file_path, mimetype='application/vnd.openxmlformats-officedocument.wordprocessingml.document')
    file = service.files().create(body=file_metadata, media_body=media, fields='id').execute()


def copy_docx_to_same_folder(original_file_path, copy_name):
    try:
        folder_path, file_name = os.path.split(original_file_path)
        new_file_path = os.path.join(folder_path, copy_name)

        shutil.copyfile(original_file_path, new_file_path)
        return new_file_path
    except Exception as e:
        print(f"Error copying the file: {e}")
        return None


def make_certificate(folder_id, dict, role):
    if role == Role.WINNER:
        certificate_name = "Сертификат победителя " + dict["Фамилия"] + " " + dict["Имя"] + " " + dict["Отчество"]
        original_file_path = WINNER_TEMPLATE_FILE_PATH
    if role == Role.FINALIST:
        certificate_name = "Сертификат финалиста " + dict["Фамилия"] + " " + dict["Имя"] + " " + dict["Отчество"]
        original_file_path = FINALIST_TEMPLATE_FILE_PATH

    copy_file_path = copy_docx_to_same_folder(original_file_path, certificate_name)

    doc = Document(copy_file_path)
    for paragraph in doc.paragraphs:
        for search_text, replace_text in dict.items():
            if ("#" + search_text + "#") in paragraph.text:
                paragraph.text = paragraph.text.replace("#" + search_text + "#", replace_text)
                for run in paragraph.runs:
                    run.font.size = Pt(14)  
    doc.save(copy_file_path)

    creds = authenticate()
    upload_docx_to_google_drive(creds, copy_file_path, folder_id)
    os.remove(copy_file_path)



def main():

    test_data1 = {
        "Фамилия": "Счусанеску",
        "Имя": "Вампирбек",
        "Отчество": "Баланович",
        "СНИЛС": "480-953-512 08",
        "Год": "2024"
    }
    test_data2 = {
        "Фамилия": "Иванов",
        "Имя": "Генрих",
        "Отчество": "Иванович",
        "СНИЛС": "980-923-352 09",
        "Год": "2024"
    }
    destination_folder_id = "ВСТАВЬ_ID"

    make_certificate(destination_folder_id, test_data1, Role.WINNER)
    make_certificate(destination_folder_id, test_data2, Role.FINALIST)

    

if __name__ == '__main__':
    main()