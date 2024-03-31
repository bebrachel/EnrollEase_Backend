import base64
import os
import shutil
from enum import Enum, auto

from docx import Document
from docx.shared import Pt
from googleapiclient.http import MediaFileUpload

from Google import GOOGLE_GEN_SERTIFICATES, PORTFOLIO_SERTIFICATES_FOLDER_ID, give_user_permission

# Список необходимых разрешений:

# Путь к файлу токена:
TOKEN_PICKLE = 'token.pickle'

# Пути до шаблонов сертификатов:
WINNER_TEMPLATE_FILE_PATH = "/scripts/googler/certificate_making/finalist_certificate_template.docx"  # '/scripts/googler/certificate_making/templates/winner.docx'
FINALIST_TEMPLATE_FILE_PATH = "/scripts/googler/certificate_making/finalist_certificate_template.docx"  # '/scripts/googler/finalist_certificate_template.docx'


class Role(Enum):
    WINNER = auto()
    FINALIST = auto()


def upload_docx_to_google_drive(file_path):
    folder_path, file_name = os.path.split(file_path)
    file_metadata = {
        'name': os.path.basename(file_name),
        'parents': [PORTFOLIO_SERTIFICATES_FOLDER_ID]
    }
    media = MediaFileUpload(file_path,
                            mimetype='application/vnd.openxmlformats-officedocument.wordprocessingml.document')
    return GOOGLE_GEN_SERTIFICATES.files().create(body=file_metadata, media_body=media,
                                                  fields='id').execute()


def copy_docx_to_same_folder(original_file_path, copy_name):
    try:
        folder_path, file_name = os.path.split(original_file_path)
        new_file_path = os.path.join(folder_path, copy_name)

        shutil.copyfile(original_file_path, new_file_path)
        return new_file_path
    except Exception as e:
        print(f"Error copying the file: {e}")
        return None


def make_certificate(dict, role):
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

    f = upload_docx_to_google_drive(copy_file_path)
    os.remove(copy_file_path)
    return f


def main(data):
    names = data['name'].split(" ")
    if len(names) > 0:
        data['Фамилия'] = names[0]
        if len(names) > 1:
            data['Имя'] = names[1]
            if len(names) > 2:
                data['Отчество'] = names[2]
    data['Год'] = "2024"
    data['СНИЛС'] = data['iian']

    if data['role'] == 'WINNER':
        return make_certificate(data, Role.WINNER)
    elif data['role'] == 'FINALIST':
        return make_certificate(data, Role.FINALIST)


if __name__ == '__main__':
    import json

    # Check if an argument was passed
    # The first argument after the script name is the JSON data
    with open('/sertificates_pipe.json', 'r') as f:
        data = json.loads(f.read())
        # Deserialize the JSON data to a Python object (dict)
        # data = json.loads(data)
        f = main(data)
