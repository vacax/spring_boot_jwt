# Ejemplo de JSON Web Token con Spring Boot.

Proyecto que implementa JWT y Spring Boot, manteniendo una URL para el API si estado y
la otra con estado. 

## Versiones 
* Java 21
* Spring Boot 3.3.4
* Spring Security 6.

Para ejecutar directamente de la imagen Docker con el siguiente comando:
```
docker run --rm -it -p 8080:8080 vacax/springboot-jwt
```

o ejecutar la tarea de Gradle:
```
./gradlew bootjar
```

## Ejemplo de uso

Autenticación:

```
curl --location 'localhost:8080/auth/auth' \
--form 'username="admin"' \
--form 'password="admin"'
```

Resultado es una estructura de la siguiente forma:

```
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6IlJPTEVfQURNSU4sUk9MRV9VU0VSLFJPTEVfQ09OU1VMVEEiLCJzdWIiOiJhZG1pbiIsImV4cCI6MTcyODE2MDY4M30.EmNitjcNY-zntst50UWS8m5VOVniumx747CakpryGWY",
    "expiresIn": 1728160683152
}
```

Llamada a API:

EL valor de <<TOKEN_GENERADO>> debe ser con el valor retornado de la estructura token, de la
respuesta anterior sería: **eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6IlJPTEVfQURNSU4sUk9MRV9VU0VSLFJPTEVfQ09OU1VMVEEiLCJzdWIiOiJhZG1pbiIsImV4cCI6MTcyODE2MDY4M30.EmNitjcNY-zntst50UWS8m5VOVniumx747CakpryGWY**

```
curl --location 'localhost:8080/api/estudiante/' \
--header 'Authorization: Bearer <<TOKEN_GENERADO>>' \
--header 'Cookie: JSESSIONID=299C141BC7564AC5DCC4F305EEAF85F1'
```

El resultado sería:

```
[
    {
        "matricula": 11959608,
        "nombre": "Jordan Mraz"
    },
    {
        "matricula": 12154078,
        "nombre": "Miss Claudia Braun"
    },
    {
        "matricula": 13386408,
        "nombre": "Joleen Huel"
    },
    {
        "matricula": 18234186,
        "nombre": "Harry Abbott"
    }
]
```