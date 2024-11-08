FROM openjdk:17
EXPOSE 8087
ADD target/Foyer-1.0.2.jar Foyer-1.0.2.jar
ENTRYPOINT ["java","-jar","/Foyer-1.0.2.jar"]