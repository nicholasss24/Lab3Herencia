import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SistemaTecniHogar {

    private final Map<String, OrdenServicio> ordenes = new LinkedHashMap<>();
    private final Map<String, Double> ingresosPorCategoria =
        new LinkedHashMap<>();
        private final Map<String, Cuadrilla> cuadrillas = new LinkedHashMap<>();
private final Map<String, Jornada> jornadas = new LinkedHashMap<>();

    private double ingresosRegistro;
    private double ingresosVisitas;

    public String cotizar(OrdenServicio orden) {
        if (orden == null) {
            throw new IllegalArgumentException("Debe indicar una orden.");
        }

        return String.format(Locale.US, "%s: Q%.2f, %d día(s)",
            orden.getCategoria(),
            orden.calcularCosto(),
            orden.diasAtencion());
    }

    public void registrarOrden(OrdenServicio orden) {
        guardarOrden(orden, true);
    }

    public void cargarOrdenInicial(OrdenServicio orden) {
        guardarOrden(orden, false);
    }

    private void guardarOrden(OrdenServicio orden, boolean cobrar) {
        if (orden == null) {
            throw new IllegalArgumentException("Debe indicar una orden.");
        }
        if (ordenes.containsKey(orden.getCodigo())) {
            throw new IllegalArgumentException(
                "Ya existe una orden con ese código."
            );
        }
        if (orden.getEstado() != EstadoOrden.PENDIENTE
                || orden.getVisitasRealizadas() != 0) {
            throw new IllegalArgumentException(
                "Solo se pueden registrar órdenes nuevas."
            );
        }

        double costo = orden.calcularCosto();
        ordenes.put(orden.getCodigo(), orden);

        if (cobrar) {
            ingresosRegistro += costo;
            ingresosPorCategoria.merge(
                orden.getCategoria(), costo, Double::sum
            );
        }
    }

    public List<OrdenServicio> getOrdenes() {
        return List.copyOf(ordenes.values());
    }

    public double getIngresosRegistro() {
        return ingresosRegistro;
    }

    public double getIngresosVisitas() {
        return ingresosVisitas;
    }

    public double getIngresoTotal() {
        return ingresosRegistro + ingresosVisitas;
    }

    public Map<String, Double> getIngresosPorCategoria() {
        return Map.copyOf(ingresosPorCategoria);
        
    }
    public void registrarCuadrilla(Cuadrilla cuadrilla) {
    if (cuadrilla == null) {
        throw new IllegalArgumentException("Debe indicar una cuadrilla.");
    }
    if (cuadrillas.containsKey(cuadrilla.getCodigo())) {
        throw new IllegalArgumentException(
            "Ya existe una cuadrilla con ese código."
        );
    }

    cuadrillas.put(cuadrilla.getCodigo(), cuadrilla);
}

public Jornada crearJornada(String codigo, String codigoCuadrilla) {
    if (codigo == null || codigo.isBlank()) {
        throw new IllegalArgumentException(
            "El código de la jornada no puede estar vacío."
        );
    }
    if (jornadas.containsKey(codigo)) {
        throw new IllegalArgumentException(
            "Ya existe una jornada con ese código."
        );
    }

    Cuadrilla cuadrilla = cuadrillas.get(codigoCuadrilla);
    if (cuadrilla == null) {
        throw new IllegalArgumentException(
            "No existe la cuadrilla indicada."
        );
    }

    Jornada jornada = new Jornada(codigo, cuadrilla);
    jornadas.put(codigo, jornada);
    return jornada;
}

public void asignarOrden(String codigoJornada, String codigoOrden) {
    Jornada jornada = jornadas.get(codigoJornada);
    OrdenServicio orden = ordenes.get(codigoOrden);

    if (jornada == null || orden == null) {
        throw new IllegalArgumentException(
            "No existe la jornada o la orden indicada."
        );
    }

    jornada.asignar(orden);
}

public void retirarOrden(String codigoJornada, String codigoOrden) {
    Jornada jornada = jornadas.get(codigoJornada);
    OrdenServicio orden = ordenes.get(codigoOrden);

    if (jornada == null || orden == null) {
        throw new IllegalArgumentException(
            "No existe la jornada o la orden indicada."
        );
    }

    jornada.retirar(orden);
}

public List<Cuadrilla> getCuadrillas() {
    return List.copyOf(cuadrillas.values());
}

public List<Jornada> getJornadas() {
    return List.copyOf(jornadas.values());
}
public double iniciarJornada(String codigoJornada) {
    Jornada jornada = jornadas.get(codigoJornada);
    if (jornada == null) {
        throw new IllegalArgumentException("No existe esa jornada.");
    }

    double cargos = jornada.iniciar();

    for (OrdenServicio orden : jornada.getOrdenes()) {
        if (orden.getVisitasRealizadas() > 1) {
            ingresosPorCategoria.merge(
                orden.getCategoria(), 40.0, Double::sum
            );
        }
    }

    ingresosVisitas += cargos;
    return cargos;
}

public void registrarResultado(String codigoJornada, String codigoOrden,
                               boolean completada) {
    Jornada jornada = jornadas.get(codigoJornada);
    OrdenServicio orden = ordenes.get(codigoOrden);

    if (jornada == null || orden == null) {
        throw new IllegalArgumentException(
            "No existe la jornada o la orden indicada."
        );
    }

    jornada.registrarResultado(orden, completada);
}
public String generarReporte() {
    StringBuilder reporte = new StringBuilder();
    Map<String, Map<EstadoOrden, Integer>> conteos =
        new LinkedHashMap<>();

    for (OrdenServicio orden : ordenes.values()) {
        Map<EstadoOrden, Integer> estados =
            conteos.computeIfAbsent(
                orden.getCategoria(),
                categoria -> new EnumMap<>(EstadoOrden.class)
            );
        estados.merge(orden.getEstado(), 1, Integer::sum);
    }

    reporte.append("ÓRDENES POR CATEGORÍA Y ESTADO\n");
    for (Map.Entry<String, Map<EstadoOrden, Integer>> entrada
            : conteos.entrySet()) {
        reporte.append(entrada.getKey()).append(":\n");

        for (EstadoOrden estado : EstadoOrden.values()) {
            int cantidad = entrada.getValue().getOrDefault(estado, 0);
            reporte.append("  ").append(estado)
                .append(": ").append(cantidad).append('\n');
        }
    }

    reporte.append(String.format(Locale.US,
        "%nINGRESOS%nRegistro: Q%.2f%nVisitas adicionales: Q%.2f%n"
        + "Total: Q%.2f%n",
        ingresosRegistro, ingresosVisitas, getIngresoTotal()));

    reporte.append("Ingresos por categoría:\n");
    for (String categoria : conteos.keySet()) {
        reporte.append(String.format(Locale.US,
            "  %s: Q%.2f%n",
            categoria,
            ingresosPorCategoria.getOrDefault(categoria, 0.0)));
    }

    reporte.append("\nJORNADAS SIN CERRAR\n");
    for (Jornada jornada : jornadas.values()) {
        if (jornada.getEstado() != EstadoJornada.CERRADA) {
            reporte.append(String.format(Locale.US,
                "  %s | Cuadrilla: %s | Asignadas: %.2f h"
                + " | Disponibles: %.2f h%n",
                jornada.getCodigo(),
                jornada.getCuadrilla().getCodigo(),
                jornada.getHorasAsignadas(),
                jornada.getCapacidadDisponible()));
        }
    }

    return reporte.toString();
}

public void cargarDatosIniciales() {
    cargarOrdenInicial(new Diagnostico(
        "D001", "Ana", "Zona 10", 1.0, Zona.METROPOLITANA));
    cargarOrdenInicial(new Diagnostico(
        "D002", "Luis", "Mixco", 1.5, Zona.PERIFERICA));

    cargarOrdenInicial(new Instalacion(
        "I001", "Marta", "Zona 1", 2.5, Zona.METROPOLITANA, 5));
    cargarOrdenInicial(new Instalacion(
        "I002", "Pedro", "Villa Nueva", 1.2, Zona.PERIFERICA, 2));

    cargarOrdenInicial(new InstalacionElectricaCertificada(
        "E001", "Carlos", "Zona 4", 2.5,
        Zona.METROPOLITANA, 5, 2000.0));
    cargarOrdenInicial(new InstalacionElectricaCertificada(
        "E002", "Rosa", "Amatitlán", 2.0,
        Zona.PERIFERICA, 2, 800.0));

    cargarOrdenInicial(new MantenimientoAire(
        "A001", "Sofía", "Zona 14", 1.5,
        Zona.METROPOLITANA, 12000));
    cargarOrdenInicial(new MantenimientoAire(
        "A002", "Diego", "Zona 15", 2.0,
        Zona.METROPOLITANA, 24000));

    registrarCuadrilla(new Cuadrilla("C001", 8.0, false));
    registrarCuadrilla(new Cuadrilla("C002", 6.0, true));
    registrarCuadrilla(new Cuadrilla("C003", 4.0, false));
}
}