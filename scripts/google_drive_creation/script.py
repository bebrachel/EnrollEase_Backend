class Script(object):
    from Google import Create_Service
    CLIENT_SECRET_FILE = 'credentials.json'
    API_NAME = 'drive'
    API_VERSION = 'v3'
    SCOPES = ['https://www.googleapis.com/auth/drive']
    service = Create_Service(CLIENT_SECRET_FILE, API_NAME, API_VERSION, SCOPES)

    @classmethod
    def folder_create(cls, name):
        file_meta = {
            'name': name,
            'mimeType': 'application/vnd.google-apps.folder'
        }
        return Script.service.files().create(body=file_meta, fields='id').execute()

    @classmethod
    def give_user_permission(cls, file_id, user_address):
        """
        на почту автоматически приходит уведомленме
        :param user_address:
        :param file_id:
        :return:
        """
        permission = {
            'type': 'user',
            'role': 'writer',
            'emailAddress': user_address,  # Please set your email of Google account.
        }
        Script.service.permissions().create(fileId=file_id, body=permission).execute()


if __name__ == '__main__':
    import sys

    args = sys.argv
    folder_name = args[1]
    user_gmail = args[2]
    folder_id = Script.folder_create(folder_name)["id"]
    Script.give_user_permission(folder_id, user_gmail)
