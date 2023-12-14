from googleapiclient.discovery import build
from googleapiclient.errors import HttpError
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from google.auth.transport.requests import Request
import os.path
import base64

# Области API
SCOPES = ['https://www.googleapis.com/auth/gmail.readonly']


def authenticate_gmail_api():
    creds = None
    token_file = 'token.json'

    if os.path.exists(token_file):
        creds = Credentials.from_authorized_user_file(token_file)

    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())
        else:
            flow = InstalledAppFlow.from_client_secrets_file('credentials.json', SCOPES)
            creds = flow.run_local_server(port=0)

        with open(token_file, 'w') as token:
            token.write(creds.to_json())

    return creds


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
    creds = authenticate_gmail_api()

    try:
        service = build("gmail", "v1", credentials=creds)
        threads = (service.users().threads().list(userId="me",
                                                  q=companion).execute().get(
            "threads", []))

        # достаем самую свежую нить
        tdata = (service.users().threads().get(userId="me", id=threads[0]["id"]).execute())

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


if __name__ == "__main__":
    import sys

    args = sys.argv[1:]
    companion = args[0]
    show_chatty_threads(companion)
