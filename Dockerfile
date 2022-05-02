FROM openjdk:17.0.1-oracle as build

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY src src
RUN ./mvnw -B package

FROM openjdk:17.0.1-oracle

RUN mkdir resources
COPY --from=build target/musify-0.0.1-SNAPSHOT.jar /service.jar
COPY --from=build target/classes/* /resources/

EXPOSE 8081

ENTRYPOINT ["java","-jar","service.jar"]
