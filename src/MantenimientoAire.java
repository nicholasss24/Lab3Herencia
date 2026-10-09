public class MantenimientoAire extends OrdenServicio {
    private final int capacidadBtu;

    public MantenimientoAire(String codigo, String cliente, String direccion,
                             double horasEstimadas, Zona zona, int capacidadBtu) {
        super(codigo, cliente, direccion, horasEstimadas, zona);

        if (zona != Zona.METROPOLITANA) {
            throw new IllegalArgumentException(
                "El mantenimiento de aire solo se ofrece en zona metropolitana."
            );
        }
        if (capacidadBtu < 9000 || capacidadBtu > 60000) {
            throw new IllegalArgumentException(
                "La capacidad debe estar entre 9000 y 60000 BTU."
            );
        }

        this.capacidadBtu = capacidadBtu;
    }

    public int getCapacidadBtu() {
        return capacidadBtu;
    }

    @Override
    public String getCategoria() {
        return "Mantenimiento de aire acondicionado";
    }

    @Override
    public double calcularCosto() {
        return 100.0 + 110.0 * getHorasEstimadas() + 80.0;
    }

    @Override
    public int diasAtencion() {
        return 2;
    }

    @Override
    public int maxVisitas() {
        return 1;
    }
    @Override
public boolean esCompatibleCon(Cuadrilla cuadrilla) {
    return cuadrilla.isCertificadaRefrigeracion();
}
}