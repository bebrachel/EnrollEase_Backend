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


def print_message_text(message):
    if 'payload' in message and 'body' in message['payload']:
        body = message['payload']['body']
        if 'data' in body:
            decoded_body = base64.urlsafe_b64decode(body['data']).decode('utf-8')
            print(decoded_body)
            print("-----")
    if 'payload' in message and 'parts' in message['payload']:
        body = message['payload']['parts'][0]['body']
        if 'data' in body:
            decoded_body = base64.urlsafe_b64decode(body['data']).decode('utf-8').split('>')[0]
            print(decoded_body)
            print("-----------------------------------")


if __name__ == '__main__':
    import sys

    args = sys.argv[1:]
    companion = args[0]
    creds = authenticate_gmail_api()
    try:
        service = build("gmail", "v1", credentials=creds)
        response = service.users().messages().list(userId='me', q=companion).execute()
        messages = response.get('messages', [])
        count = 1
        if messages:
            for message in messages:
                print(f'Message {count}')
                count += 1
                if count == 30:
                    break
                print_message_text(service.users().messages().get(userId='me', id=message['id']).execute())
    except HttpError as error:
        print(f"An error occurred: {error}", file=sys.stderr)
