public class Instalacion extends OrdenServicio {
    private final int puntos;

    public Instalacion(String codigo, String cliente, String direccion,
                       double horasEstimadas, Zona zona, int puntos) {
        super(codigo, cliente, direccion, horasEstimadas, zona);

        if (puntos <= 0) {
            throw new IllegalArgumentException(
                "La cantidad de puntos debe ser positiva."
            );
        }

        this.puntos = puntos;
    }

    public int getPuntos() {
        return puntos;
    }

    public int getHorasFacturables() {
        double horasPorPuntos = puntos * 0.75;
        double mayorCantidad = Math.max(getHorasEstimadas(), horasPorPuntos);
        return (int) Math.ceil(mayorCantidad);
    }

    @Override
    public String getCategoria() {
        return "Instalación";
    }

    @Override
    public double calcularCosto() {
        double tarifaBase = getZona() == Zona.METROPOLITANA ? 100.0 : 175.0;
        return tarifaBase + 90.0 * getHorasFacturables();
    }

    @Override
    public int diasAtencion() {
        return getZona() == Zona.METROPOLITANA ? 3 : 5;
    }

    @Override
    public int maxVisitas() {
        return 3;
    }
}