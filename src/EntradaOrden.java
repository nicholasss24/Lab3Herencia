import java.util.Scanner;

public class EntradaOrden {
    private final Scanner teclado;

    public EntradaOrden(Scanner teclado) {
        this.teclado = teclado;
    }

    private String preguntar(String mensaje) {
        System.out.print(mensaje);

        if (!teclado.hasNextLine()) {
            throw new IllegalStateException("No se recibieron datos.");
        }

        return teclado.nextLine().trim();
    }

    public OrdenServicio leerOrden() {
        System.out.println("\nCategoría:");
        System.out.println("1. Diagnóstico");
        System.out.println("2. Instalación");
        System.out.println("3. Instalación eléctrica certificada");
        System.out.println("4. Mantenimiento de aire acondicionado");

        String categoria = preguntar("Elige una categoría: ");
        if (!categoria.equals("1") && !categoria.equals("2")
                && !categoria.equals("3") && !categoria.equals("4")) {
            throw new IllegalArgumentException("Categoría no válida.");
        }

        String codigo = preguntar("Código: ");
        String cliente = preguntar("Cliente: ");
        String direccion = preguntar("Dirección: ");
        double horas = Double.parseDouble(
            preguntar("Horas estimadas: ").replace(',', '.')
        );

        String opcionZona = preguntar(
            "Zona (1 = metropolitana, 2 = periférica): "
        );
        Zona zona;

        if (opcionZona.equals("1")) {
            zona = Zona.METROPOLITANA;
        } else if (opcionZona.equals("2")) {
            zona = Zona.PERIFERICA;
        } else {
            throw new IllegalArgumentException("Zona no válida.");
        }

        switch (categoria) {
            case "1":
                return new Diagnostico(
                    codigo, cliente, direccion, horas, zona
                );

            case "2": {
                int puntos = Integer.parseInt(
                    preguntar("Cantidad de puntos: ")
                );
                return new Instalacion(
                    codigo, cliente, direccion, horas, zona, puntos
                );
            }

            case "3": {
                int puntos = Integer.parseInt(
                    preguntar("Cantidad de puntos: ")
                );
                double materiales = Double.parseDouble(
                    preguntar("Valor de materiales (Q): ")
                        .replace(',', '.')
                );
                return new InstalacionElectricaCertificada(
                    codigo, cliente, direccion, horas, zona,
                    puntos, materiales
                );
            }

            case "4": {
                int btu = Integer.parseInt(
                    preguntar("Capacidad del equipo (BTU): ")
                );
                return new MantenimientoAire(
                    codigo, cliente, direccion, horas, zona, btu
                );
            }

            default:
                throw new IllegalArgumentException("Categoría no válida.");
        }
    }
}