FROM amazoncorretto:17-alpine as builder

WORKDIR /app

COPY . /app

RUN ./gradlew build

FROM amazoncorretto:17-alpine

ARG PORT_APP=8080
ENV PORT $PORT_APP

EXPOSE $PORT

COPY --from=builder /app/applications/app-service/build/libs/personas-ms.jar /app/personas-ms.jar

ENTRYPOINT [ "sh", "-c", "java -jar /app/personas-ms.jar" ]
