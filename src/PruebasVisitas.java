public class PruebasVisitas {
    private static int superadas = 0;

    private static void verificar(String caso, double esperado,
                                 double obtenido) {
        if (Math.abs(esperado - obtenido) > 0.000001) {
            throw new AssertionError(
                caso + ": esperado " + esperado + ", obtenido " + obtenido
            );
        }

        superadas++;
        System.out.printf(
            "OK | %s | Esperado: %.2f | Obtenido: %.2f%n",
            caso, esperado, obtenido
        );
    }

    private static OrdenServicio buscar(SistemaTecniHogar sistema,
                                        String codigo) {
        for (OrdenServicio orden : sistema.getOrdenes()) {
            if (orden.getCodigo().equals(codigo)) {
                return orden;
            }
        }
        throw new AssertionError("No se encontró la orden " + codigo);
    }

    private static void probarFallos(SistemaTecniHogar sistema,
                                    String codigoOrden,
                                    String codigoCuadrilla,
                                    int visitasPermitidas) {
        OrdenServicio orden = buscar(sistema, codigoOrden);

        for (int numero = 1; numero <= visitasPermitidas; numero++) {
            String codigoJornada = "J-" + codigoOrden + "-" + numero;
            sistema.crearJornada(codigoJornada, codigoCuadrilla);

            double ingresoAntes = sistema.getIngresoTotal();
            sistema.asignarOrden(codigoJornada, codigoOrden);

            verificar(codigoOrden + " visita " + numero
                + ": asignar no cobra",
                ingresoAntes, sistema.getIngresoTotal());

            double cargoEsperado = numero == 1 ? 0 : 40;
            double cargoObtenido = sistema.iniciarJornada(codigoJornada);

            verificar(codigoOrden + " visita " + numero
                + ": cargo al iniciar",
                cargoEsperado, cargoObtenido);

            verificar(codigoOrden + " visita " + numero
                + ": ingreso tras iniciar",
                ingresoAntes + cargoEsperado,
                sistema.getIngresoTotal());

            sistema.registrarResultado(
                codigoJornada, codigoOrden, false
            );

            EstadoOrden estadoEsperado =
                numero == visitasPermitidas
                    ? EstadoOrden.CANCELADA
                    : EstadoOrden.PENDIENTE;

            if (orden.getEstado() != estadoEsperado) {
                throw new AssertionError(
                    codigoOrden + ": esperado " + estadoEsperado
                    + ", obtenido " + orden.getEstado()
                );
            }
            superadas++;
            System.out.println(
                "OK | " + codigoOrden + " visita " + numero
                + ": estado esperado y obtenido = " + estadoEsperado
            );

            verificar(codigoOrden + ": visitas realizadas",
                numero, orden.getVisitasRealizadas());
        }
    }

    public static void main(String[] args) {
        SistemaTecniHogar sistema = new SistemaTecniHogar();
        sistema.cargarDatosIniciales();

        probarFallos(sistema, "D001", "C001", 3);
        probarFallos(sistema, "I001", "C001", 3);
        probarFallos(sistema, "E001", "C001", 2);
        probarFallos(sistema, "A001", "C002", 1);

        // Cinco visitas adicionales en total: 2 + 2 + 1.
        verificar("Ingresos por visitas adicionales",
            200, sistema.getIngresosVisitas());
        verificar("Ingresos por registro de datos iniciales",
            0, sistema.getIngresosRegistro());

        try {
            sistema.crearJornada("J-EXTRA", "C003");
            sistema.asignarOrden("J-EXTRA", "D001");
            throw new AssertionError(
                "Se permitió asignar un diagnóstico cancelado."
            );
        } catch (IllegalArgumentException | IllegalStateException esperado) {
            superadas++;
            System.out.println(
                "OK | Orden cancelada no se puede asignar nuevamente"
            );
        }

        verificar("Rechazo no altera ingresos",
            200, sistema.getIngresoTotal());

        System.out.println("\nPruebas superadas: " + superadas);
    }
}