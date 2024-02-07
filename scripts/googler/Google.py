import pickle
import base64
import os
from googleapiclient.errors import HttpError
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build
from google.auth.transport.requests import Request
from email.message import EmailMessage

INDIVIDUAL_ACHIEVEMENTS_FOLDER_ID = ['1dml8sdNr5Bh_oJeU9-gxWHkWHYpuMjCw']
PORTFOLIO_FOLDER_ID = ['1o4sRpoNdq8eL06tZAKnJRe5jjWRxb17t']


def create_service(api_name, api_version, *scopes):
    API_SERVICE_NAME = api_name
    API_VERSION = api_version
    SCOPES = [scope for scope in scopes[0]]
    CLIENT_SECRET_FILE = '/scripts/googler/credentials.json'

    cred = None

    pickle_file = f'/scripts/googler/tokens/token_{API_SERVICE_NAME}_{API_VERSION}_{str("".join(SCOPES)).replace(":", "").replace("/", "")}.pickle'

    if os.path.exists(pickle_file):
        with open(pickle_file, 'rb') as token:
            cred = pickle.load(token)

    if not cred or not cred.valid:
        if cred and cred.expired and cred.refresh_token:
            cred.refresh(Request())
        else:
            flow = InstalledAppFlow.from_client_secrets_file(CLIENT_SECRET_FILE, SCOPES)
            cred = flow.run_local_server(port=8083)

        with open(pickle_file, 'wb') as token:
            pickle.dump(cred, token)

    try:
        service = build(API_SERVICE_NAME, API_VERSION, credentials=cred)
        return service
    except Exception as e:
        print('Unable to connect.')
        print(e)
        return None


GOOGLE_DRIVE_SERVICE = create_service('drive', 'v3', ['https://www.googleapis.com/auth/drive'])
GMAIL_SERVICE_COMPOSE = create_service('gmail', 'v1', ['https://www.googleapis.com/auth/gmail.compose'])
GMAIL_SERVICE_READONLY = create_service('gmail', 'v1', ['https://www.googleapis.com/auth/gmail.readonly'])


def folder_create(name, parents):
    file_meta = {
        'name': f"{name}",
        'mimeType': 'application/vnd.google-apps.folder',
        'parents': parents
    }
    return GOOGLE_DRIVE_SERVICE.files().create(body=file_meta, fields='id').execute()


def give_user_permission(file_id, user_address):
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
    GOOGLE_DRIVE_SERVICE.permissions().create(fileId=file_id, body=permission).execute()


def gmail_create(subject, content, recipients):
    try:
        # create gmail api client

        message = EmailMessage()

        message.set_content(content)

        # message['To'] = sender
        # message['From'] = 'gduser2@workspacesamples.dev'
        message['Subject'] = subject

        message.add_header('Bcc', "%s\r\n" % ",".join(recipients))

        # encoded message
        encoded_message = base64.urlsafe_b64encode(message.as_bytes()).decode()

        create_message = {
            'message': {
                'raw': encoded_message
            }
        }
        # pylint: disable=E1101
        draft = GMAIL_SERVICE_COMPOSE.users().drafts().create(userId="me",
                                                              body=create_message).execute()

        GMAIL_SERVICE_COMPOSE.users().drafts().send(userId="me", body=draft).execute()

    except HttpError as error:
        print(F'An error occurred: {error}')
        draft = None

    return draft


def get_message_body(message):
    if 'parts' in message['payload']:
        parts = message['payload']['parts']
        for part in parts:
            if 'body' in part:
                body = part['body']
                if 'data' in body:
                    return base64.urlsafe_b64decode(body['data']).decode('utf-8')
    elif 'body' in message['payload']:
        body = message['payload']['body']
        if 'data' in body:
            return base64.urlsafe_b64decode(body['data']).decode('utf-8')
    return None


def show_chatty_threads(companion):
    try:
        threads = (GMAIL_SERVICE_READONLY.users().threads().list(userId="me",
                                                                 q=companion).execute().get(
            "threads", []))

        # достаем самую свежую нить
        tdata = (GMAIL_SERVICE_READONLY.users().threads().get(userId="me", id=threads[0]["id"]).execute())

        # печатаем кол-во сообщений в нити
        for message in tdata["messages"]:
            if message:
                body = get_message_body(message)
                if body:
                    print(f"Message ID: {message['id']}")
                    print(f"Full Text:\n{body}")
                    print("-----")
    except HttpError as error:
        print(f"An error occurred: {error}")
