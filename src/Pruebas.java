public class Pruebas {
    private static int pruebasSuperadas = 0;

    private static void verificar(String caso, double esperado,
                                 double obtenido) {
        if (Math.abs(esperado - obtenido) > 0.000001) {
            throw new AssertionError(
                caso + ": esperado " + esperado + ", obtenido " + obtenido
            );
        }

        pruebasSuperadas++;
        System.out.printf(
            "OK | %s | Esperado: %.2f | Obtenido: %.2f%n",
            caso, esperado, obtenido
        );
    }

    public static void main(String[] args) {
        Diagnostico diagnosticoMetro = new Diagnostico(
            "D1", "Ana", "Zona 10", 1.0, Zona.METROPOLITANA
        );
        Diagnostico diagnosticoPeriferico = new Diagnostico(
            "D2", "Luis", "Mixco", 1.5, Zona.PERIFERICA
        );

        Instalacion instalacionMetro = new Instalacion(
            "I1", "Marta", "Zona 1", 2.5, Zona.METROPOLITANA, 5
        );
        Instalacion instalacionPeriferica = new Instalacion(
            "I2", "Pedro", "Villa Nueva", 1.2, Zona.PERIFERICA, 2
        );

        InstalacionElectricaCertificada electricaMetro =
            new InstalacionElectricaCertificada(
                "E1", "Carlos", "Zona 4", 2.5,
                Zona.METROPOLITANA, 5, 2000.0
            );
        InstalacionElectricaCertificada electricaPeriferica =
            new InstalacionElectricaCertificada(
                "E2", "Rosa", "Amatitlán", 2.0,
                Zona.PERIFERICA, 2, 800.0
            );

        MantenimientoAire aire = new MantenimientoAire(
            "A1", "Sofía", "Zona 14", 1.5,
            Zona.METROPOLITANA, 12000
        );

        verificar("Diagnóstico metropolitano: costo",
            150, diagnosticoMetro.calcularCosto());
        verificar("Diagnóstico periférico: costo",
            225, diagnosticoPeriferico.calcularCosto());

        verificar("Instalación: horas por puntos",
            4, instalacionMetro.getHorasFacturables());
        verificar("Instalación metropolitana: costo",
            460, instalacionMetro.calcularCosto());
        verificar("Instalación: fracción de hora",
            2, instalacionPeriferica.getHorasFacturables());
        verificar("Instalación periférica: costo",
            355, instalacionPeriferica.calcularCosto());

        verificar("Eléctrica metropolitana: garantía y certificado",
            680, electricaMetro.calcularCosto());
        verificar("Eléctrica periférica: costo",
            515, electricaPeriferica.calcularCosto());
        verificar("Aire: horas sin redondear y gas",
            345, aire.calcularCosto());

        verificar("Días diagnóstico metropolitano",
            1, diagnosticoMetro.diasAtencion());
        verificar("Días diagnóstico periférico",
            2, diagnosticoPeriferico.diasAtencion());
        verificar("Días instalación metropolitana",
            3, instalacionMetro.diasAtencion());
        verificar("Días instalación periférica",
            5, instalacionPeriferica.diasAtencion());
        verificar("Días eléctrica metropolitana",
            5, electricaMetro.diasAtencion());
        verificar("Días eléctrica periférica",
            7, electricaPeriferica.diasAtencion());
        verificar("Días mantenimiento de aire",
            2, aire.diasAtencion());

        System.out.println(
            "\nPruebas superadas: " + pruebasSuperadas
        );
    }
}