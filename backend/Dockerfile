#build stage
FROM maven:3.9.8-amazoncorretto-21 AS build 
WORKDIR /app
COPY . .
RUN mvn install -DskipTests=true

#run stage
FROM amazoncorretto:21.0.4 
WORKDIR /run
COPY --from=build /app/target/Ecommerce-0.0.1-SNAPSHOT.jar /run/Ecommerce-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/run/Ecommerce-0.0.1-SNAPSHOT.jar"]
