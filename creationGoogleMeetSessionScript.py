import os
import datetime
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from google.auth.transport.requests import Request
from googleapiclient.discovery import build

# Определим область видимости API:
SCOPES = ['https://www.googleapis.com/auth/calendar.events']

def create_meeting():
    creds = None

    # Проверка, есть ли сохраненные учетные данные (token.json):
    if os.path.exists('token.json'):
        creds = Credentials.from_authorized_user_file('token.json')

    # Если учетных данных нет или они устарели, запрашиваем их у пользователя:
    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())
        else:
            flow = InstalledAppFlow.from_client_secrets_file(
                'credentials.json', SCOPES)
            creds = flow.run_local_server(port=0)

        # Сохранение учетных данных для следующего запуска:
        with open('token.json', 'w') as token:
            token.write(creds.to_json())

    # Подключение к Google Calendar API:
    service = build('calendar', 'v3', credentials=creds)

    # Определение параметров для нового события:
    event = {
        'summary': 'Google Meet',
        'location': 'Online',
        'description': 'Meeting created using Google Calendar API',
        'start': {
            'dateTime': '2023-11-15T10:00:00',
            'timeZone': 'UTC',
        },
        'end': {
            'dateTime': '2024-12-01T11:00:00',
            'timeZone': 'UTC',
        },
        'attendees': [
            {'email': 'o.pogibelnaya@g.nsu.ru'},
        ],
        'reminders': {
            'useDefault': False,
            'overrides': [
                {'method': 'email', 'minutes': 10},
            ],
        },
        'conferenceData': {
            'createRequest': {
                'requestId': 'example',
                'conferenceSolutionKey': {
                    'type': 'hangoutsMeet',
                },
            },
        },
    }

    # Создание события:
    event = service.events().insert(calendarId='primary', body=event, conferenceDataVersion=1).execute()

    print(f'Meeting created: {event.get("htmlLink")}')

if __name__ == '__main__':
    create_meeting()
