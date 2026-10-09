import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SistemaTecniHogar sistema = new SistemaTecniHogar();
        sistema.cargarDatosIniciales();

        Scanner teclado = new Scanner(System.in);
        EntradaOrden entrada = new EntradaOrden(teclado);
        String opcion;

        do {
            System.out.println("\n=== TECNIHOGAR ===");
            System.out.println("1. Consultar órdenes");
            System.out.println("2. Consultar cuadrillas");
            System.out.println("3. Ver reportes");
            System.out.println("4. Cotizar");
            System.out.println("5. Registrar orden");
            System.out.println("6. Registrar cuadrilla");
            System.out.println("7. Crear jornada");
            System.out.println("8. Asignar orden a jornada");
            System.out.println("9. Retirar orden de jornada");
            System.out.println("10. Consultar jornadas");
            System.out.println("11. Iniciar jornada");
            System.out.println("12. Registrar resultado de visita");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            if (!teclado.hasNextLine()) {
                break;
            }
            opcion = teclado.nextLine().trim();

            try {
                switch (opcion) {
                    case "1":
                        for (OrdenServicio orden : sistema.getOrdenes()) {
                            System.out.printf(
                                "%s | %s | %s | %s | Visitas: %d | Q%.2f%n",
                                orden.getCodigo(),
                                orden.getCategoria(),
                                orden.getCliente(),
                                orden.getEstado(),
                                orden.getVisitasRealizadas(),
                                orden.calcularCosto()
                            );
                        }
                        break;

                    case "2":
                        for (Cuadrilla cuadrilla
                                : sistema.getCuadrillas()) {
                            System.out.printf(
                                "%s | Capacidad: %.2f h"
                                + " | Refrigeración: %s%n",
                                cuadrilla.getCodigo(),
                                cuadrilla.getCapacidadHoras(),
                                cuadrilla.isCertificadaRefrigeracion()
                                    ? "Sí" : "No"
                            );
                        }
                        break;

                    case "3":
                        System.out.println(sistema.generarReporte());
                        break;

                    case "4": {
                        OrdenServicio orden = entrada.leerOrden();
                        System.out.println(
                            "Cotización: " + sistema.cotizar(orden)
                        );
                        break;
                    }

                    case "5": {
                        OrdenServicio orden = entrada.leerOrden();
                        sistema.registrarOrden(orden);
                        System.out.printf(
                            "Orden registrada. Cobrado: Q%.2f%n",
                            orden.calcularCosto()
                        );
                        break;
                    }

                    case "6": {
                        System.out.print("Código de la cuadrilla: ");
                        String codigo = teclado.nextLine().trim();

                        System.out.print("Capacidad en horas: ");
                        double capacidad = Double.parseDouble(
                            teclado.nextLine().trim().replace(',', '.')
                        );

                        System.out.print(
                            "¿Certificada en refrigeración? (1 = sí, 2 = no): "
                        );
                        String respuesta = teclado.nextLine().trim();

                        if (!respuesta.equals("1") && !respuesta.equals("2")) {
                            throw new IllegalArgumentException(
                                "La respuesta debe ser 1 o 2."
                            );
                        }

                        Cuadrilla nueva = new Cuadrilla(
                            codigo, capacidad, respuesta.equals("1")
                        );
                        sistema.registrarCuadrilla(nueva);
                        System.out.println("Cuadrilla registrada: " + codigo);
                        break;
                    }

                    case "7": {
                        System.out.print("Código de la jornada: ");
                        String codigo = teclado.nextLine().trim();
                        System.out.print("Código de la cuadrilla: ");
                        String codigoCuadrilla = teclado.nextLine().trim();

                        sistema.crearJornada(codigo, codigoCuadrilla);
                        System.out.println("Jornada creada: " + codigo);
                        break;
                    }

                    case "8": {
                        System.out.print("Código de la jornada: ");
                        String jornada = teclado.nextLine().trim();
                        System.out.print("Código de la orden: ");
                        String orden = teclado.nextLine().trim();

                        sistema.asignarOrden(jornada, orden);
                        System.out.println("Orden asignada.");
                        break;
                    }

                    case "9": {
                        System.out.print("Código de la jornada: ");
                        String jornada = teclado.nextLine().trim();
                        System.out.print("Código de la orden: ");
                        String orden = teclado.nextLine().trim();

                        sistema.retirarOrden(jornada, orden);
                        System.out.println("Orden retirada; vuelve a pendiente.");
                        break;
                    }

                    case "10":
                        for (Jornada jornada : sistema.getJornadas()) {
                            System.out.printf(
                                "%s | Cuadrilla: %s | %s"
                                + " | Asignadas: %.2f h"
                                + " | Disponibles: %.2f h%n",
                                jornada.getCodigo(),
                                jornada.getCuadrilla().getCodigo(),
                                jornada.getEstado(),
                                jornada.getHorasAsignadas(),
                                jornada.getCapacidadDisponible()
                            );

                            for (OrdenServicio orden
                                    : jornada.getOrdenes()) {
                                System.out.println(
                                    "  Orden: " + orden.getCodigo()
                                );
                            }
                        }
                        break;

                    case "11": {
                        System.out.print("Código de la jornada: ");
                        String codigo = teclado.nextLine().trim();

                        double cargo = sistema.iniciarJornada(codigo);
                        System.out.printf(
                            "Jornada iniciada. Cargos por visitas"
                            + " adicionales: Q%.2f%n",
                            cargo
                        );
                        break;
                    }

                    case "12": {
                        System.out.print("Código de la jornada: ");
                        String jornada = teclado.nextLine().trim();
                        System.out.print("Código de la orden: ");
                        String orden = teclado.nextLine().trim();
                        System.out.print(
                            "Resultado (1 = completada, 2 = fallida): "
                        );
                        String resultado = teclado.nextLine().trim();

                        if (!resultado.equals("1")
                                && !resultado.equals("2")) {
                            throw new IllegalArgumentException(
                                "El resultado debe ser 1 o 2."
                            );
                        }

                        sistema.registrarResultado(
                            jornada, orden, resultado.equals("1")
                        );
                        System.out.println("Resultado registrado.");
                        break;
                    }

                    case "0":
                        System.out.println("Hasta luego.");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (IllegalArgumentException
                     | IllegalStateException error) {
                System.out.println("Error: " + error.getMessage());
            }
        } while (!opcion.equals("0"));

        teclado.close();
    }
}