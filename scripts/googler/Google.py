import pickle
import os
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build
from google.auth.transport.requests import Request

INDIVIDUAL_ACHIEVEMENTS_FOLDER_ID = '1dml8sdNr5Bh_oJeU9-gxWHkWHYpuMjCw'
PORTFOLIO_FOLDER_ID = '1o4sRpoNdq8eL06tZAKnJRe5jjWRxb17t'


def create_service(api_name, api_version, *scopes):
    API_SERVICE_NAME = api_name
    API_VERSION = api_version
    SCOPES = [scope for scope in scopes[0]]
    CLIENT_SECRET_FILE = './googler/credentials.json'

    cred = None

    pickle_file = f'./googler/tokens/token_{API_SERVICE_NAME}_{API_VERSION}_{str(*SCOPES).replace(":", "").replace("/", "")}.pickle'

    if os.path.exists(pickle_file):
        with open(pickle_file, 'rb') as token:
            cred = pickle.load(token)

    if not cred or not cred.valid:
        if cred and cred.expired and cred.refresh_token:
            cred.refresh(Request())
        else:
            flow = InstalledAppFlow.from_client_secrets_file(CLIENT_SECRET_FILE, SCOPES)
            cred = flow.run_local_server()

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
GMAIL_SERVICE = create_service('gmail', 'v1', ['https://www.googleapis.com/auth/gmail.compose'])


def folder_create(service, name, parents):
    file_meta = {
        'name': f"{name}",
        'mimeType': 'application/vnd.google-apps.folder',
        'parents': parents
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


def gmail_create(subject, content, recipients):
    from googleapiclient.errors import HttpError
    try:
        # create gmail api client

        from email.message import EmailMessage
        message = EmailMessage()

        message.set_content(content)

        # message['To'] = sender
        # message['From'] = 'gduser2@workspacesamples.dev'
        message['Subject'] = subject

        message.add_header('Bcc', "%s\r\n" % ",".join(recipients))

        # encoded message
        import base64
        encoded_message = base64.urlsafe_b64encode(message.as_bytes()).decode()

        create_message = {
            'message': {
                'raw': encoded_message
            }
        }
        # pylint: disable=E1101
        draft = GMAIL_SERVICE.users().drafts().create(userId="me",
                                                      body=create_message).execute()

        GMAIL_SERVICE.users().drafts().send(userId="me", body=draft).execute()

    except HttpError as error:
        print(F'An error occurred: {error}')
        draft = None

    return draft
