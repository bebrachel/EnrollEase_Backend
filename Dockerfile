FROM ubuntu:20.04
RUN apt-get update && apt install -y openjdk-17-jre-headless
RUN apt install -y python3-pip
RUN apt install -y build-essential libssl-dev libffi-dev python3-dev
RUN pip3 install numpy && pip3 install opencv-python

EXPOSE 8080
COPY ./scripts ./scripts
ARG JAR_FILE=enrollease/target/*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]