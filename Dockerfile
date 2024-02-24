FROM ubuntu:20.04

ENV TZ=Asia/Novosibirsk
ENV DEBIAN_FRONTEND=noninteractive


RUN apt-get update && apt-get install -y cron
#apt-get install -y openjdk-17-jre-headless && \
RUN apt-get install -y python3-pip
RUN apt-get install -y inotify-tools
RUN apt install -y build-essential libssl-dev libffi-dev python3-dev
RUN pip3 install numpy && \
    pip3 install opencv-python && \
    pip3 install pandas && \
    pip3 install pymongo && \
    pip3 install openpyxl
RUN apt install -y openjdk-17-jdk openjdk-17-jre
COPY ./scripts ./scripts
RUN pip3 install -r ./scripts/googler/requirements.txt
COPY ./boot.sh ./
RUN chmod +x ./scripts/excel_to_mongo/trigger.sh && \
    chmod +x ./boot.sh && \
    chmod +x /scripts/googler/folder_observation.py
COPY cron_folder_observ /etc/cron.d/mycron
RUN chmod 0744 /etc/cron.d/mycron
RUN crontab /etc/cron.d/mycron
RUN touch /var/log/cron.log
RUN cron

#RUN #echo "* * * * * root python3 /scripts/googler/folder_observation.py >> /var/log/daily-backup.log 2>&1" >> /etc/crontab
# RUN #cron


EXPOSE 8088
COPY ./enrollease.jar ./enrollease.jar
ENTRYPOINT ["/bin/sh", "-c", "cron && ./boot.sh"]