#IMAGEN MODELO
FROM eclipse-temurin:21-jdk

#EXPONER PUERTO
EXPOSE 8080

#DEFINIR DIRECTORIO RAIZ
WORKDIR /root

#COPIAR Y PEGAR ARCHIVOS DEL CONTENEDOR
COPY ./pom.xml /root
COPY ./.mvn /root/.mvn
COPY ./mvnw /root

#DESCARGAR DEPENDENCIAS PROYECTO
RUN ./mvnw dependency:go-offline

#COPIAR EL CÓDIGO FUENTE DENTRO DEL CONTENEDOR
COPY ./src /root/src

#CONSTRUIR APLICACIÓN
RUN ./mvnw clean package -DskipTests

#LEVANTAR APLICACIÓN CUANDO INICIE EL CONTENEDOR
ENTRYPOINT ["java","-jar", "/root/target/prueba-practica-backend-nequi-0.0.1-SNAPSHOT.jar"]