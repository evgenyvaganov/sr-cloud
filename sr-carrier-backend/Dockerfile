FROM openjdk:8-jdk

COPY build/libs/sr-carrier-backend-*.jar /opt/sr-carrier-backend.jar

CMD ["java", "-cp", "/opt/sr-carrier-backend.jar", "com.guidewire.devconnect.EntryPoint"]