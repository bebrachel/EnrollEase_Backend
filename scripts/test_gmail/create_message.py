from __future__ import print_function

import base64
from email.message import EmailMessage

import os.path
from google.auth.transport.requests import Request
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build
from googleapiclient.errors import HttpError

SCOPES = ['https://www.googleapis.com/auth/gmail.compose']


def gmail_create_draft(sender, subject, content, recipients):
    creds = None
    # The file token.json stores the user's access and refresh tokens, and is
    # created automatically when the authorization flow completes for the first
    # time.
    if os.path.exists('token.json'):
        creds = Credentials.from_authorized_user_file('token.json', SCOPES)
    # If there are no (valid) credentials available, let the user log in.
    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())
            # print("refresh token")
        else:
            flow = InstalledAppFlow.from_client_secrets_file('credentials.json', SCOPES)
            creds = flow.run_local_server(port=0)
        # Save the credentials for the next run
        with open('token.json', 'w') as token:
            token.write(creds.to_json())
    try:
        # create gmail api client
        service = build('gmail', 'v1', credentials=creds)

        message = EmailMessage()

        message.set_content(content)

        message['To'] = sender
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
        draft = service.users().drafts().create(userId="me",
                                                body=create_message).execute()

        service.users().drafts().send(userId="me", body=draft).execute()

    except HttpError as error:
        print(F'An error occurred: {error}')
        draft = None

    return draft


if __name__ == '__main__':
    import sys

    args = sys.argv

    try:
        from ast import literal_eval

        gmail_create_draft(args[1], args[2], args[3], literal_eval(args[4]))
    except ValueError as err:
        print(f'Recipients cannot be parsed\n'
              f'Format must be like ["lolipop@gmail.com", "blacksparrow@ramble.rr"')