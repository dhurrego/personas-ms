# Microservicio Personas (API)

## Arquitectura

Aplicación creada bajo Arquitectura hexagonal utilizando el plugin [Scaffolding Bancolombia](https://github.com/bancolombia/scaffold-clean-architecture)

Basado en esta arquitectura se crearon las capas

* Dominio (Casos de uso y modelo)
* Infraestructura (Entrypoint, adapters y helpers)
* Aplicación (Ensamblador de modulos, creacion de beans principales)

## Técnologias utilizadas

* SpringBoot 3.0.1
* Spring WebFlux
* Java 17
* Base de datos PostgreSQL
* Cuenta de almacenamiento de azure (Storage Account)
* Broker de mensajeria (Azure ServiceBus)
* Docker
* Kubernetes

### Algunas librerias
* Swagger (Documentación de servicios)
* Apache POI (Procesamiento de archivos Excel)

## Analisis de código estatico

Se utilizo la herramienta de SonarQube para identificar Code Smell
y calcular la cobertura

![Cobertura](https://res.cloudinary.com/dn4mmllzs/image/upload/c_pad,b_auto:predominant,fl_preserve_transparency/v1675629439/sofka/Cobertura_personas-ms_lyitmb.jpg)

## Documentación API

Se crea la documentación a través de Swagger, se anexa enlace de la misma: 
[Documentación Swagger](http://52.226.240.196/swagger-doc/swagger-ui.html)

## Despliegue

Se crea el respectivo Pipeline y Release usando Azure DevOps para el despliegue 
de la aplicación, el cual se encarga de publicar la imagén de docker en el
Azure Container Registry y luego desplegarla en el AKS. 

El release esta enlazado al pipeline y se ejecutará de manera automatica una 
vez termina el pipeline, por tanto para el despliegue, solo es necesario correr 
el pipeline

![Pipeline](https://res.cloudinary.com/dn4mmllzs/image/upload/c_pad,b_auto:predominant,fl_preserve_transparency/v1675629690/sofka/Pipeline_personas-ms_phzier.jpg)
![Release](https://res.cloudinary.com/dn4mmllzs/image/upload/c_pad,b_auto:predominant,fl_preserve_transparency/v1675629690/sofka/Release_personas-ms_bhrkgi.jpg)

## Endpoint principal

Usando Azure API Managament se crea el API Gateway o puerta principal para el front
la URL correspondiente a este microservicio es: 
https://adminsofka-apimanagement.azure-api.net/personas

## Arquitectura general del proyecto 

![Arquitectura global](https://res.cloudinary.com/dn4mmllzs/image/upload/c_pad,b_auto:predominant,fl_preserve_transparency/v1675629439/sofka/Arquitectura_aplicaci%C3%B3n_ADMIN_SOFKA-Arquitectura.drawio_axsobv.jpg)