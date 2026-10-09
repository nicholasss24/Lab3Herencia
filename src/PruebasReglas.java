public class PruebasReglas {
    private static int superadas = 0;

    private static void comprobar(String caso, boolean condicion) {
        if (!condicion) {
            throw new AssertionError("Falló: " + caso);
        }
        superadas++;
        System.out.println("OK | " + caso);
    }

    private static void debeRechazar(String caso, Runnable operacion) {
        try {
            operacion.run();
            throw new AssertionError("Se aceptó una operación inválida: " + caso);
        } catch (IllegalArgumentException | IllegalStateException esperado) {
            superadas++;
            System.out.println("OK | Rechazada: " + caso);
        }
    }

    public static void main(String[] args) {
        SistemaTecniHogar sistema = new SistemaTecniHogar();
        sistema.cargarDatosIniciales();

        comprobar("Datos iniciales: ingresos Q0",
            sistema.getIngresoTotal() == 0);

        Diagnostico nueva = new Diagnostico(
            "D100", "Elena", "Zona 2", 1.0, Zona.METROPOLITANA
        );

        sistema.cotizar(nueva);
        comprobar("Cotizar: ingresos siguen en Q0",
            sistema.getIngresoTotal() == 0);

        sistema.registrarOrden(nueva);
        comprobar("Registrar diagnóstico: ingreso Q150",
            sistema.getIngresosRegistro() == 150);

        debeRechazar("Código de orden repetido", () ->
            sistema.registrarOrden(new Diagnostico(
                "D100", "Otro", "Zona 3", 1.0, Zona.METROPOLITANA
            ))
        );
        comprobar("Registro rechazado: ingreso sigue en Q150",
            sistema.getIngresoTotal() == 150);

        debeRechazar("Diagnóstico de más de 1.5 horas", () ->
            new Diagnostico(
                "D200", "Ana", "Zona 1", 2.0, Zona.METROPOLITANA
            )
        );

        debeRechazar("Aire en zona periférica", () ->
            new MantenimientoAire(
                "A200", "Ana", "Mixco", 1.0, Zona.PERIFERICA, 12000
            )
        );

        debeRechazar("BTU fuera de rango", () ->
            new MantenimientoAire(
                "A201", "Ana", "Zona 1", 1.0,
                Zona.METROPOLITANA, 8000
            )
        );

        // Orden inicial de 7 h: no genera ingreso.
        sistema.cargarOrdenInicial(new Instalacion(
            "I100", "Luis", "Zona 1", 7.0, Zona.METROPOLITANA, 1
        ));

        Jornada jornada = sistema.crearJornada("J100", "C001");
        sistema.asignarOrden("J100", "I001"); // 2.5 horas

        debeRechazar("Aire con cuadrilla sin certificación", () ->
            sistema.asignarOrden("J100", "A001")
        );
        debeRechazar("Capacidad: 2.5 + 7 supera 8 h", () ->
            sistema.asignarOrden("J100", "I100")
        );

        comprobar("Asignaciones rechazadas: permanece solo I001",
            jornada.getOrdenes().size() == 1
            && jornada.getHorasAsignadas() == 2.5);
        comprobar("Asignaciones rechazadas: ingreso sigue Q150",
            sistema.getIngresoTotal() == 150);

        sistema.retirarOrden("J100", "I001");
        comprobar("Retiro: I001 vuelve a pendiente",
            sistema.getOrdenes().stream().anyMatch(orden ->
                orden.getCodigo().equals("I001")
                && orden.getEstado() == EstadoOrden.PENDIENTE
            ));
        comprobar("Retiro: ingreso sigue Q150",
            sistema.getIngresoTotal() == 150);

        debeRechazar("Iniciar jornada vacía", () ->
            sistema.iniciarJornada("J100")
        );
        debeRechazar("Segunda jornada abierta para C001", () ->
            sistema.crearJornada("J101", "C001")
        );

        comprobar("Operaciones rechazadas: jornada sigue en preparación",
            jornada.getEstado() == EstadoJornada.EN_PREPARACION);
        comprobar("Operaciones rechazadas: ingreso sigue Q150",
            sistema.getIngresoTotal() == 150);

        System.out.println("\nPruebas superadas: " + superadas);
    }
}