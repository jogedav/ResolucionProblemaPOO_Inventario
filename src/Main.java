import interfaz.MainInventario;
import negocio.Inventario;


public class Main {
    public static void main(String[] args) {
        MainInventario interfaz = new MainInventario();

        System.out.println("INVENTARIO - TIENDA LA ESQUINA");
        int fechaActual = interfaz.leerFechaTrabajo();
        Inventario inventario = new Inventario(100000, 500, fechaActual);
        System.out.println("Presupuesto: 1000 USD / Capacidad: 500 / Maximo: 100 productos");

        int opcion;
        do {
            interfaz.mostrarMenu();
            opcion = interfaz.leerEntero("Opcion: ");

            switch (opcion) {
                case 1:
                    interfaz.registrarProducto(inventario);
                    break;
                case 2:
                    interfaz.registrarEntrada(inventario);
                    break;
                case 3:
                    interfaz.registrarSalida(inventario);
                    break;
                case 4:
                    interfaz.buscarProducto(inventario);
                    break;
                case 5:
                    interfaz.listarInventario(inventario);
                    break;
                case 6:
                    interfaz.verAlertas(inventario);
                    break;
                case 7:
                    interfaz.verResumen(inventario);
                    break;
                case 0:
                    System.out.println("Hasta pronto. Los datos de esta sesion permanecian en memoria.");
                    break;
                default:
                    System.out.println("ERROR Opcion no valida.");
                    break;
            }
        } while (opcion != 0);
    }
}
