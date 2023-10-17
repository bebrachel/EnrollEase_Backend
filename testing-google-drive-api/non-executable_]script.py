from Google import Create_Service

CLIENT_SECRET_FILE = 'credentials.json'
API_NAME = 'drive'
API_VERSION = 'v3'
SCOPES = ['https://www.googleapis.com/auth/drive']

service = Create_Service(CLIENT_SECRET_FILE, API_NAME, API_VERSION, SCOPES)


def folder_create(name):
    # default_format =
    file_meta = {
        'name': name,
        'mimeType': 'application/vnd.google-apps.folder'
    }
    return service.files().create(body=file_meta, fields='id').execute()


def give_user_permission(file_id):
    """
    на почту автоматически приходит уведомленме
    :param file_id:
    :return:
    """
    permission = {
        'type': 'user',
        'role': 'writer',
        'emailAddress': 'gudvin0203@gmail.com',  # Please set your email of Google account.
    }
    service.permissions().create(fileId=file_id, body=permission).execute()


file = folder_create("test_folder")
print(file)
give_user_permission(file['id'])
