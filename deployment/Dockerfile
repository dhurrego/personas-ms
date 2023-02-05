FROM amazoncorretto:17-alpine

WORKDIR /app

COPY /applications/app-service/build/libs/personas-ms.jar /app/personas-ms.jar

ARG PORT_APP=8080

ENV PORT $PORT_APP

EXPOSE $PORT

ENTRYPOINT [ "sh", "-c", "java -jar /app/personas-ms.jar" ]
