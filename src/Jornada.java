import java.util.ArrayList;
import java.util.List;

public class Jornada {
    private final String codigo;
    private final Cuadrilla cuadrilla;
    private final List<OrdenServicio> ordenes;
    private EstadoJornada estado;

    public Jornada(String codigo, Cuadrilla cuadrilla) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                "El código de la jornada no puede estar vacío."
            );
        }
        if (cuadrilla == null) {
            throw new IllegalArgumentException("Debe indicar una cuadrilla.");
        }

        this.codigo = codigo;
        this.cuadrilla = cuadrilla;
        this.ordenes = new ArrayList<>();
        this.estado = EstadoJornada.EN_PREPARACION;
        cuadrilla.registrarJornada(this);
    }

    public String getCodigo() {
        return codigo;
    }

    public Cuadrilla getCuadrilla() {
        return cuadrilla;
    }

    public EstadoJornada getEstado() {
        return estado;
    }

    public List<OrdenServicio> getOrdenes() {
        return List.copyOf(ordenes);
    }

    public double getHorasAsignadas() {
        double total = 0;
        for (OrdenServicio orden : ordenes) {
            total += orden.getHorasEstimadas();
        }
        return total;
    }

    public double getCapacidadDisponible() {
        return cuadrilla.getCapacidadHoras() - getHorasAsignadas();
    }

    public void asignar(OrdenServicio orden) {
        if (estado != EstadoJornada.EN_PREPARACION) {
            throw new IllegalStateException("La jornada ya inició.");
        }
        if (orden == null) {
            throw new IllegalArgumentException("Debe indicar una orden.");
        }
        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            throw new IllegalStateException("La orden no está pendiente.");
        }
        if (!orden.esCompatibleCon(cuadrilla)) {
            throw new IllegalArgumentException(
                "La cuadrilla no puede atender esta orden."
            );
        }
        if (orden.getHorasEstimadas() > getCapacidadDisponible() + 0.000001) {
            throw new IllegalArgumentException(
                "La orden supera la capacidad disponible."
            );
        }

        orden.asignar();
        ordenes.add(orden);
    }

    public void retirar(OrdenServicio orden) {
        if (estado != EstadoJornada.EN_PREPARACION) {
            throw new IllegalStateException(
                "Solo se pueden retirar órdenes antes de iniciar."
            );
        }
        if (!ordenes.contains(orden)) {
            throw new IllegalArgumentException(
                "La orden no pertenece a esta jornada."
            );
        }

        orden.retirar();
        ordenes.remove(orden);
    }
    public double iniciar() {
    if (estado != EstadoJornada.EN_PREPARACION) {
        throw new IllegalStateException("La jornada no está en preparación.");
    }
    if (ordenes.isEmpty()) {
        throw new IllegalStateException("No se puede iniciar una jornada vacía.");
    }

    double cargosAdicionales = 0;
    for (OrdenServicio orden : ordenes) {
        cargosAdicionales += orden.iniciarVisita();
    }

    estado = EstadoJornada.INICIADA;
    return cargosAdicionales;
}

public void registrarResultado(OrdenServicio orden, boolean completada) {
    if (estado != EstadoJornada.INICIADA) {
        throw new IllegalStateException("La jornada no está iniciada.");
    }
    if (!ordenes.contains(orden)) {
        throw new IllegalArgumentException(
            "La orden no pertenece a esta jornada."
        );
    }

    orden.registrarResultado(completada);

    boolean todasConResultado = true;
    for (OrdenServicio actual : ordenes) {
        if (actual.getEstado() == EstadoOrden.EN_ATENCION) {
            todasConResultado = false;
            break;
        }
    }

    if (todasConResultado) {
        estado = EstadoJornada.CERRADA;
        cuadrilla.liberarJornada(this);
    }
}
}