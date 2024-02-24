# EnrollEase_Backend

## Setup environment

- You need to replace `ADMIN_EMAIL` variable from `docker-compose.yml` with your email. Email must
  be added into Google Cloud Console.
- Any new user must be added as Test User into Google Cloud Console.
- You need to replace `INDIVIDUAL_ACHIEVEMENTS_FOLDER_ID` and `PORTFOLIO_FOLDER_ID`
  from `scripts/googler/Google.py` with your Google Drive folders.
- You need to generate tokens for accessing API. Temporally it could be done by 3 times
  running `__main__` from `scripts/googler/Google.py`.
- You need to replace `scripts/googler/credentials.json` with yours obtained from Google Cloud Console.

## Spring profiles

In `boot.sh` you can choose one of available profiles:
- `prod` - setting up services with admin profile
- `test_data` - the same as `prod` but with pre-created data

## Deploy

We need the jar file of the application. It could be done with `mvn clean install`.  
After we rename it to `app.jar` and put with `Dockerfile`, `docker-compose.yml`, `boot.sh` in the
same directory.  
Then we can start docker containers with `docker-compose up --build`.