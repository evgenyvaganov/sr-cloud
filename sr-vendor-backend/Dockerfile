FROM openjdk:8-jdk

COPY build/libs/sr-vendor-backend-*.jar /opt/sr-vendor-backend.jar

CMD ["java", "-cp", "/opt/sr-vendor-backend.jar", "com.guidewire.devconnect.EntryPoint"]