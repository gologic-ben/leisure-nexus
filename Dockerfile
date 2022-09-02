FROM maven AS build  

COPY ./src /usr/src/app/src
COPY ./pom.xml /usr/src/app

RUN mvn -f /usr/src/app/pom.xml clean package -Dmaven.test.skip

FROM openjdk:18-alpine

WORKDIR /app

COPY --from=build /usr/src/app/target/app-0.0.1-SNAPSHOT.jar /app/app.jar

RUN addgroup -S leisure && \
    adduser -S leisure -G leisure -s /bin/false && \
    chown -R leisure:leisure /app

USER leisure

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]  
