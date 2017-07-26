FROM openjdk:8u131-jre

COPY target/scala-2.12/bidder-assembly-1.0.jar /home/bidder-assembly-1.0.jar
CMD ["java","-jar","/home/bidder-assembly-1.0.jar"]
EXPOSE 8080