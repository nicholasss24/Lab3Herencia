public class Cuadrilla {
    private final String codigo;
    private final double capacidadHoras;
    private final boolean certificadaRefrigeracion;
    private Jornada jornadaAbierta;

    public Cuadrilla(String codigo, double capacidadHoras,
                     boolean certificadaRefrigeracion) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                "El código de la cuadrilla no puede estar vacío."
            );
        }
        if (!Double.isFinite(capacidadHoras) || capacidadHoras <= 0) {
            throw new IllegalArgumentException(
                "La capacidad debe ser un número positivo."
            );
        }

        this.codigo = codigo;
        this.capacidadHoras = capacidadHoras;
        this.certificadaRefrigeracion = certificadaRefrigeracion;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getCapacidadHoras() {
        return capacidadHoras;
    }

    public boolean isCertificadaRefrigeracion() {
        return certificadaRefrigeracion;
    }

    void registrarJornada(Jornada jornada) {
    if (jornadaAbierta != null) {
        throw new IllegalStateException(
            "La cuadrilla ya tiene una jornada sin cerrar."
        );
    }
    jornadaAbierta = jornada;
}

void liberarJornada(Jornada jornada) {
    if (jornadaAbierta != jornada) {
        throw new IllegalStateException(
            "Esta jornada no pertenece a la cuadrilla."
        );
    }
    jornadaAbierta = null;
}
}