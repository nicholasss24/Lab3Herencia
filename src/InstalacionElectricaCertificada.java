public class InstalacionElectricaCertificada extends Instalacion {
    private final double valorMateriales;

    public InstalacionElectricaCertificada(
            String codigo, String cliente, String direccion,
            double horasEstimadas, Zona zona, int puntos,
            double valorMateriales) {

        super(codigo, cliente, direccion, horasEstimadas, zona, puntos);

        if (!Double.isFinite(valorMateriales) || valorMateriales <= 0) {
            throw new IllegalArgumentException(
                "El valor de los materiales debe ser positivo."
            );
        }

        this.valorMateriales = valorMateriales;
    }

    @Override
    public String getCategoria() {
        return "Instalación eléctrica certificada";
    }

    @Override
    public double calcularCosto() {
        return super.calcularCosto() + 120.0 + valorMateriales * 0.05;
    }

    @Override
    public int diasAtencion() {
        return super.diasAtencion() + 2;
    }

    @Override
    public int maxVisitas() {
        return 2;
    }
}