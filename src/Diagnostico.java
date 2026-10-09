public class Diagnostico extends OrdenServicio {

    public Diagnostico(String codigo, String cliente, String direccion,
                       double horasEstimadas, Zona zona) {
        super(codigo, cliente, direccion, horasEstimadas, zona);

        if (horasEstimadas > 1.5) {
            throw new IllegalArgumentException(
                "Un diagnóstico no puede superar 1.5 horas."
            );
        }
    }

    @Override
    public String getCategoria() {
        return "Diagnóstico";
    }

    @Override
    public double calcularCosto() {
        return getZona() == Zona.METROPOLITANA ? 150.0 : 225.0;
    }

    @Override
    public int diasAtencion() {
        return getZona() == Zona.METROPOLITANA ? 1 : 2;
    }

    @Override
    public int maxVisitas() {
        return 3;
    }
}