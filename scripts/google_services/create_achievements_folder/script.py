from google_services.Google import create_service


def folder_create(service, name):
    file_meta = {
        'name': f"ПриемнаяКампания2023/{name}",
        'mimeType': 'application/vnd.google-apps.folder',
        'parents': ['1l6hMMlzH17LWlxhWVPu3FTg4612dT_S4']
    }
    return service.files().create(body=file_meta, fields='id').execute()


def give_user_permission(service, file_id, user_address):
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
    service.permissions().create(fileId=file_id, body=permission).execute()


if __name__ == '__main__':
    import sys

    args = sys.argv[1:]
    if len(args) != 2:
        print('Usage: python script.py <folder_name> <user_gmail_address>')
        exit(1)
    service = create_service('drive', 'v3', ['https://www.googleapis.com/auth/drive'])
    folder_name = args[0]
    user_gmail = args[1]
    folder_id = folder_create(service, folder_name)["id"]
    give_user_permission(service, folder_id, user_gmail)
