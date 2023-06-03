# Privilegium - Sistema de Gestión de Acceso Privilegiado (PAM, por sus siglas en inglés)

Trabajo final de máster, desarrollado por Carlos Camacho, como requisito para el master de Ciberseguridad de la 
Universidad José Camilo Cela año 2021-2022, tiene como objetivo desarrollar un sistema para la gestión de acceso privilegiado
(PAM, por sus siglas en inglés, Privileged Access Management) vía una plataforma web. La aplicación permite administrar
los usuarios y servidores, brindado acceso mediante el protocolo SSH vía Web,ocultando las credenciales de acceso al
usuario final y aplicando políticas de rotado de contraseñas.

El PAM permite a los administradores de infraestructura mantener un control de los usuarios que acceden a los
servidores o servicios sin exponer las credenciales y manteniendo un registro de toda la actividad realizada 
por el usuario para fines de auditoría.

![Logo Universidad](/img/logo-ucjc.png)

## Versiones utilizadas

La aplicación utiliza las siguientes versiones de software:

* Java 17
* Apache Maven, versión 3.8.1
* Spring Boot, versión 2.7.2
* Vaadin, versión 23.1.6

## Ejecución de la aplicación

Para ejecutar el proyecto utilizando la herramienta estandar de Maven ejecute en la línea de comando
la sigueinte instrucción: `mvnw` (Windows), o `./mvnw` (Mac & Linux), luego acceda a la dirección
http://localhost:8080 en su navegador.

Si estará importando el proyecto en un IDE que trabaje con Maven, puede ir al siguiente enlace: 
[how to import Vaadin projects to different IDEs](https://vaadin.com/docs/latest/flow/guide/step-by-step/importing) (Eclipse, IntelliJ IDEA, NetBeans, and VS Code).

## Despliegue en producción

Para crear el build para producció, ejecutar el siguiente comando: `mvnw clean package -Pproduction` (Windows),
o `./mvnw clean package -Pproduction` (Mac & Linux).
Se estará generando un archivo JAR cque incluirá todas las dependencias del back-end y front-end con sus recursos,
listo para producción. El archivo generado puede ser encontrado en la carpeta `target`.

Una vez generado el JAR puede ejecutar el siguiente comando:
`java -jar target/privilegium-1.0-SNAPSHOT.jar`

## Despliegue usando Docker

Para crear la imagen de Docker ejecutar el siguiente comando:

```
docker build . -t privilegium:latest
```

Una vez creada la imagen de Docker, puede probar de forma local el inicio del contenedor con el siguiente comando:

```
docker run -p 8080:8080 privilegium:latest
```

## Despliegue usuando Docker Compose

Para fines de demostración, tenemos disponible el script de Docker Compose para un escenario de prueba, mostrado en la siguiente imagen:
![Esquema Docker Compose](/img/esquema-docker-compose.png)

Para la ejecucion:

```
docker build -f Dockerfile -t web-pam .
docker-compose down && docker-compose up --env-file env-demo.env -d
```

Los puertos disponibles en el host:

* 8080: Para la aplicación web desarrollada.
* 8079: Aplicación de PhpMyadmin para el acceso remoto de la base de datos. 
* 2222: Puerto SSH del Servidor #1 en el ambiente de prueba. 
* 2223: Puerto SSH del Servidor #2 en el ambiente de prueba. 
* 2224: Puerto SSH del Servidor #3 en el ambiente de prueba.

Los puertos de los sistemas dentro del ambiente en Docker Compose, que tienen comunicación directa 
sin interactuar con el host, son:

* 3306: Base de datos en Mysql.
* 80: Servidor PhpMyAdmin. 
* 2222: Todas las instancias de los servidores SSH de prueba.

### Usuarios

* admin / admin -> Perfil Administrador, todo el acceso al sistema.
* usuario / usuario -> Perfil de Usuario, acceso únicamente a las terminales con permiso.
* demo / demo -> Perfil de Usuario, acceso únicamente a las terminales con permiso.

## Imágenes

![Login aplicación](/img/login.png)

![Entrada de la aplicación](/img/entrada.png)

![Acceso de terminal](/img/terminal.png)

![Edición de terminal](/img/edicion-terminal.png)

![Usuario edición](/img/usuario-edicion.png)

