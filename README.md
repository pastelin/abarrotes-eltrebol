# ?? Abarrotes "El Tr�bol" ? Sistema POS e Inventario

## Descripci�n del Proyecto
Soluci�n web empresarial desarrollada en **Java 17 y Spring Boot** para la gesti�n integral de ventas en mostrador (POS), control de inventarios en tiempo real y corte de caja diario de la PyME **Abarrotes "El Tr�bol"**.

## Problem�tica y An�lisis
Anteriormente, el establecimiento llevaba el control de ventas, precios y entradas de mercanc�a en un cuaderno f�sico, provocando desabasto inesperado, errores manuales de c�lculo en horas pico y falta de visibilidad financiera diaria.

## Soluci�n Desarrollada
Aplicaci�n web modular basada en arquitectura orientada a servicios independientes:
- **M�dulo de Seguridad (`auth`):** Autenticaci�n mediante **JWT**, cifrado de contrase�as con **BCrypt** y control de acceso basado en roles (`ADMIN` y `CAJERO`).
- **M�dulo de Inventario (`inventory`):** Gesti�n CRUD de productos, b�squeda r�pida por nombre/c�digo, registro de resurtidos y alertas autom�ticas de stock m�nimo.
- **M�dulo de Ventas (`sales`):** Carrito de punto de venta, c�lculo autom�tico de cambio, descuento transaccional de existencias y consulta de corte de caja diario.

## Requisitos Funcionales Cubiertos
- **RF-01:** Inicio de sesi�n con credenciales institucionales.
- **RF-02:** Control de acceso por roles (`ADMIN` / `CAJERO`).
- **RF-03:** Alta, consulta y gesti�n de cuentas de empleados.
- **RF-04:** CRUD completo de cat�logo de productos.
- **RF-05:** B�squeda r�pida por nombre o c�digo de barras.
- **RF-06:** Registro de resurtido de mercanc�a.
- **RF-07:** Alertas visuales de productos con stock bajo.
- **RF-08:** Carrito de venta en mostrador y c�lculo de cambio.
- **RF-09:** Validaci�n y descuento autom�tico de inventario al confirmar venta.
- **RF-10:** Historial de transacciones y corte de caja diario.

## Tecnolog�as Utilizadas
- Java 17 (JDK 17)
- Spring Boot 3.3.5 (Web, Security, Data JPA, Validation)
- Base de Datos H2 (persistencia en archivo local compatible con MySQL)
- JUnit 5 & Spring Security Test
- GitHub Actions (Integraci�n Continua) & GitHub Projects (Kanban)

## Ejecuci�n Local
```bash
# Ejecutar pruebas automatizadas
mvn clean test

# Iniciar servidor web en http://localhost:8080
mvn spring-boot:run

Pruueba de flujo Gitflow y Pull request hacia develop