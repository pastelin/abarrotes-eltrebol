# ?? Abarrotes "El Trébol" ? Sistema POS e Inventario

## Descripción del Proyecto
Solución web empresarial desarrollada en **Java 17 y Spring Boot** para la gestión integral de ventas en mostrador (POS), control de inventarios en tiempo real y corte de caja diario de la PyME **Abarrotes "El Trébol"**.

## Problemática y Análisis
Anteriormente, el establecimiento llevaba el control de ventas, precios y entradas de mercancía en un cuaderno físico, provocando desabasto inesperado, errores manuales de cálculo en horas pico y falta de visibilidad financiera diaria.

## Solución Desarrollada
Aplicación web modular basada en arquitectura orientada a servicios independientes:
- **Módulo de Seguridad (`auth`):** Autenticación mediante **JWT**, cifrado de contraseñas con **BCrypt** y control de acceso basado en roles (`ADMIN` y `CAJERO`).
- **Módulo de Inventario (`inventory`):** Gestión CRUD de productos, búsqueda rápida por nombre/código, registro de resurtidos y alertas automáticas de stock mínimo.
- **Módulo de Ventas (`sales`):** Carrito de punto de venta, cálculo automático de cambio, descuento transaccional de existencias y consulta de corte de caja diario.

## Requisitos Funcionales Cubiertos
- **RF-01:** Inicio de sesión con credenciales institucionales.
- **RF-02:** Control de acceso por roles (`ADMIN` / `CAJERO`).
- **RF-03:** Alta, consulta y gestión de cuentas de empleados.
- **RF-04:** CRUD completo de catálogo de productos.
- **RF-05:** Búsqueda rápida por nombre o código de barras.
- **RF-06:** Registro de resurtido de mercancía.
- **RF-07:** Alertas visuales de productos con stock bajo.
- **RF-08:** Carrito de venta en mostrador y cálculo de cambio.
- **RF-09:** Validación y descuento automático de inventario al confirmar venta.
- **RF-10:** Historial de transacciones y corte de caja diario.

## Tecnologías Utilizadas
- Java 17 (JDK 17)
- Spring Boot 3.3.5 (Web, Security, Data JPA, Validation)
- Base de Datos H2 (persistencia en archivo local compatible con MySQL)
- JUnit 5 & Spring Security Test
- GitHub Actions (Integración Continua) & GitHub Projects (Kanban)

## Ejecución Local
```bash
# Ejecutar pruebas automatizadas
mvn clean test

# Iniciar servidor web en http://localhost:8080
mvn spring-boot:run