FROM maven AS build  

COPY ./src /usr/src/app/src
COPY ./pom.xml /usr/src/app

RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk

COPY --from=build /usr/src/app/target/app-0.0.1-SNAPSHOT.jar /usr/app/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/usr/app/app.jar"]  
