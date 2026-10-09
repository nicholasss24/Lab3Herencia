# TecniHogar — Laboratorio 3: Herencia

Aplicación de consola en Java para cotizar y administrar órdenes de servicio, cuadrillas y jornadas.

## Requisitos

Tener instalado un JDK y disponer de los comandos `javac` y `java` en la terminal.

## Compilar y ejecutar

Abre una terminal en la carpeta `Lab3Herencia` y ejecuta:

```bash
mkdir -p out
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

El menú permanece abierto hasta elegir `0. Salir`.

## Ejecutar las pruebas

Después de compilar, ejecuta cada conjunto de pruebas:

```bash
java -cp out Pruebas
java -cp out PruebasReglas
java -cp out PruebasVisitas
```

Cada prueba imprime sus resultados esperados y obtenidos, o indica si una operación inválida fue rechazada.

## Datos iniciales

El programa comienza con dos órdenes pendientes de cada categoría y tres cuadrillas. Una cuadrilla está certificada en refrigeración y dos no lo están. Los datos iniciales no generan ingresos.

## Categorías

- Diagnóstico
- Instalación
- Instalación eléctrica certificada
- Mantenimiento de aire acondicionado

## Persistencia

Los datos permanecen únicamente en memoria. Al salir y ejecutar de nuevo el programa, se restablecen los datos iniciales.