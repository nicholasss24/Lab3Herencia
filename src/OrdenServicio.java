public abstract class OrdenServicio {
    private final String codigo;
    private final String cliente;
    private final String direccion;
    private final double horasEstimadas;
    private final Zona zona;

    private EstadoOrden estado;
    private int visitasRealizadas;

    public OrdenServicio(String codigo, String cliente, String direccion,
                         double horasEstimadas, Zona zona) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        if (cliente == null || cliente.isBlank()) {
            throw new IllegalArgumentException("El cliente no puede estar vacío.");
        }
        if (direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía.");
        }
        if (!Double.isFinite(horasEstimadas) || horasEstimadas <= 0) {
            throw new IllegalArgumentException("Las horas deben ser positivas.");
        }
        if (zona == null) {
            throw new IllegalArgumentException("Debe indicar una zona.");
        }

        this.codigo = codigo;
        this.cliente = cliente;
        this.direccion = direccion;
        this.horasEstimadas = horasEstimadas;
        this.zona = zona;
        this.estado = EstadoOrden.PENDIENTE;
        this.visitasRealizadas = 0;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getCliente() {
        return cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public double getHorasEstimadas() {
        return horasEstimadas;
    }

    public Zona getZona() {
        return zona;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public int getVisitasRealizadas() {
        return visitasRealizadas;
    }

    public abstract String getCategoria();

    public abstract double calcularCosto();

    public abstract int diasAtencion();

    public abstract int maxVisitas();

    public boolean esCompatibleCon(Cuadrilla cuadrilla) {
    return true;
}

void asignar() {
    if (estado != EstadoOrden.PENDIENTE) {
        throw new IllegalStateException("La orden no está pendiente.");
    }
    estado = EstadoOrden.ASIGNADA;
}

void retirar() {
    if (estado != EstadoOrden.ASIGNADA) {
        throw new IllegalStateException("La orden no está asignada.");
    }
    estado = EstadoOrden.PENDIENTE;
}

double iniciarVisita() {
    if (estado != EstadoOrden.ASIGNADA) {
        throw new IllegalStateException("La orden no está asignada.");
    }

    visitasRealizadas++;
    estado = EstadoOrden.EN_ATENCION;

    return visitasRealizadas > 1 ? 40.0 : 0.0;
}

void registrarResultado(boolean completada) {
    if (estado != EstadoOrden.EN_ATENCION) {
        throw new IllegalStateException("La orden no está en atención.");
    }

    if (completada) {
        estado = EstadoOrden.COMPLETADA;
    } else if (visitasRealizadas >= maxVisitas()) {
        estado = EstadoOrden.CANCELADA;
    } else {
        estado = EstadoOrden.PENDIENTE;
    }
}
}