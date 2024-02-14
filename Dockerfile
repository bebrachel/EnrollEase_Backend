FROM ubuntu:20.04

ENV TZ=Asia/Novosibirsk
ENV DEBIAN_FRONTEND=noninteractive

COPY ./scripts ./scripts
COPY ./boot.sh ./boot.sh

RUN apt-get update
#apt-get install -y openjdk-17-jre-headless && \
RUN apt-get install -y python3-pip && \
    apt-get install -y inotify-tools && \
    apt install -y build-essential libssl-dev libffi-dev python3-dev
RUN pip3 install numpy && \
    pip3 install opencv-python && \
    pip3 install pandas && \
    pip3 install pymongo && \
    pip3 install openpyxl
RUN apt install -y openjdk-17-jdk openjdk-17-jre
RUN pip3 install -r ./scripts/googler/requirements.txt
RUN chmod +x ./scripts/excel_to_mongo/trigger.sh && \
    chmod +x ./boot.sh

EXPOSE 8088

ARG JAR_FILE=enrollease/target/*.jar
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["./boot.sh"]