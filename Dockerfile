FROM ubuntu:20.04

ENV TZ=Asia/Novosibirsk
ENV DEBIAN_FRONTEND=noninteractive


RUN apt-get update
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
    chmod +x ./boot.sh

EXPOSE 8088
COPY ./app.jar ./app.jar
ENTRYPOINT ["./boot.sh"]