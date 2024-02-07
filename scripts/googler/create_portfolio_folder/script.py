from ..Google import *


if __name__ == '__main__':
    import sys

    args = sys.argv[1:]
    if len(args) != 2:
        print('Usage: python script.py <folder_name> <user_gmail_address>')
        exit(1)
    service = create_service('drive', 'v3', ['https://www.googleapis.com/auth/drive'])
    folder_name = args[0]
    user_gmail = args[1]
    folder_id = folder_create(service, folder_name, PORTFOLIO_FOLDER_ID)["id"]
    give_user_permission(service, folder_id, user_gmail)
