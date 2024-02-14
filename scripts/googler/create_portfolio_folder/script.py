from ..Google import *


def create(folder_name, user_gmail):
    folder_id = folder_create(folder_name, PORTFOLIO_FOLDER_ID)["id"]
    give_user_permission(folder_id, user_gmail)


if __name__ == '__main__':
    import sys

    args = sys.argv[1:]
    if len(args) != 2:
        print('Usage: python script.py <folder_name> <user_gmail_address>')
        exit(1)
    folder_name = args[0]
    user_gmail = args[1]

    create(folder_name, user_gmail)
