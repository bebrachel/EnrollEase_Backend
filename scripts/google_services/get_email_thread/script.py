from googleapiclient.errors import HttpError
import base64

from google_services.Google import create_service


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


def show_chatty_threads(service, companion):
    try:
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

    service = create_service('gmail', 'v1', ['https://www.googleapis.com/auth/gmail.readonly'])
    args = sys.argv[1:]
    if len(args) != 1:
        print("Usage: python script.py <user_gmail_adress>")
        exit(1)
    companion = args[0]
    show_chatty_threads(service, companion)
