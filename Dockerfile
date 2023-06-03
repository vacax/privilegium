# Incluyendo la caracteristica de Stage Builds para la compilación de las depedencias
FROM maven:3-openjdk-17-slim as build
RUN curl -sL https://deb.nodesource.com/setup_14.x | bash -
RUN apt-get update -qq && apt-get install -qq --no-install-recommends nodejs

# Cambiando el usuario de ejecución en e contenedor para evitar temas de seguridad.
RUN useradd -m myuser
WORKDIR /usr/src/app/
RUN chown myuser:myuser /usr/src/app/
USER myuser

# Copiando del archivo pom.xml para obtener la dependencia de la aplicación en la etapa de compiliación.
COPY --chown=myuser pom.xml ./
RUN mvn dependency:go-offline -Pproduction

# Copiando todos los archivos necesarios del proyecto con el usuario sin privilegios.
COPY --chown=myuser:myuser src src
COPY --chown=myuser:myuser frontend frontend
COPY --chown=myuser:myuser package.json ./

# Copiando los archivos propios del framework de Vaadin
COPY --chown=myuser:myuser package-lock.json* pnpm-lock.yaml* webpack.config.js* ./


# Creando el paquete de producción
RUN mvn clean package -DskipTests -Pproduction

# Creando la etapa de ejecución con el archivo compilado.
FROM eclipse-temurin:17.0.7_7-jre-jammy
COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
RUN useradd -m myuser
USER myuser
EXPOSE 8080
CMD java -jar /usr/app/app.jar
