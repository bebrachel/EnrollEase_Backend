from googleapiclient.errors import HttpError
import base64

from google_services.Google import create_service


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


def get_messages_by_user(companion):
    try:
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


if __name__ == '__main__':
    import sys

    args = sys.argv[1:]
    if len(args) != 1:
        print("Usage: python script.py <user_gmail_adress>")
        exit(1)
    companion = args[0]
    service = create_service('gmail', 'v1', ['https://www.googleapis.com/auth/gmail.readonly'])
    get_messages_by_user(companion)
