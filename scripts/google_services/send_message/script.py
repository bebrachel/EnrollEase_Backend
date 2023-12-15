from __future__ import print_function

import base64
from email.message import EmailMessage

from googleapiclient.errors import HttpError

from google_services.Google import create_service


def gmail_create_draft(subject, content, recipients):
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
        draft = service.users().drafts().create(userId="me",
                                                body=create_message).execute()

        service.users().drafts().send(userId="me", body=draft).execute()

    except HttpError as error:
        print(F'An error occurred: {error}')
        draft = None

    return draft


if __name__ == '__main__':
    import sys

    args = sys.argv[1:]
    if len(args) != 3:
        print('Usage: python script.py <subject> <body> <["recipient"*]>')
        exit(1)

    service = create_service('gmail', 'v1', ['https://www.googleapis.com/auth/gmail.compose'])

    try:
        from ast import literal_eval

        gmail_create_draft(args[0], args[1], literal_eval(args[2]))
    except ValueError as err:
        print(f'Recipients cannot be parsed\n'
              f'Format must be like ["lolipop@gmail.com", "blacksparrow@ramble.rr"')
