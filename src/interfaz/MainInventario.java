package interfaz;

import modelo.Fecha;
import modelo.Producto;
import modelo.ProductoPerecedero;
import modelo.ProductoNoPerecedero;
import negocio.Inventario;
import java.util.Scanner;


public class MainInventario {
    private Scanner teclado;

    public MainInventario() {
        teclado = new Scanner(System.in);
    }


    public int leerEntero(String mensaje) {
        int valor = 0;
        boolean valido = false;
        while (valido == false) {
            System.out.print(mensaje);
            String texto = teclado.nextLine();
            Scanner dato = new Scanner(texto);
            if (dato.hasNextInt()) {
                valor = dato.nextInt();
                if (dato.hasNext() == false) {
                    valido = true;
                }
            }
            if (valido == false) {
                System.out.println("ERROR El campo es obligatorio y debe contener un solo entero.");
            }
            dato.close();
        }
        return valor;
    }

    public int leerFechaTrabajo() {
        int fechaActual = leerEntero("Fecha de trabajo AAAAMMDD (ejemplo 20261122): ");
        Fecha fecha = new Fecha(fechaActual);
        while (fecha.esValida() == false) {
            System.out.println("ERROR La fecha de trabajo no es valida.");
            fechaActual = leerEntero("Fecha de trabajo AAAAMMDD: ");
            fecha = new Fecha(fechaActual);
        }
        return fechaActual;
    }

    public void mostrarMenu() {
        System.out.println("\n1 Registrar producto\n2 Registrar entrada\n3 Registrar salida"
                + "\n4 Buscar producto\n5 Listar inventario\n6 Ver alertas\n7 Ver resumen\n0 Salir");
    }

    public void registrarProducto(Inventario inventario) {
        int tipo = leerEntero("Tipo (1 perecedero / 2 no perecedero): ");
        if (tipo != 1 && tipo != 2) {
            System.out.println("ERROR Tipo de producto no valido.");
            return;
        }
        int codigo = leerEntero("Codigo numerico: ");
        System.out.print("Nombre: ");
        String nombre = teclado.nextLine();
        int cantidad = leerEntero("Cantidad: ");
        int costo = leerEntero("Costo en centavos (150 = 1.50 USD): ");
        int minimo = leerEntero("Stock minimo: ");
        int espacio = leerEntero("Espacio por unidad: ");
        int ingreso = leerEntero("Fecha de ingreso AAAAMMDD: ");
        Producto producto;
        if (tipo == 1) {
            int vencimiento = leerEntero("Fecha de vencimiento AAAAMMDD: ");
            producto = new ProductoPerecedero(codigo, nombre, cantidad, costo,
                    minimo, espacio, ingreso, vencimiento);
        } else {
            System.out.print("Ubicacion: ");
            String ubicacion = teclado.nextLine();
            producto = new ProductoNoPerecedero(codigo, nombre, cantidad, costo,
                    minimo, espacio, ingreso, ubicacion);
        }
        System.out.println(inventario.registrar(producto));
    }

    public void registrarEntrada(Inventario inventario) {
        int codigo = leerEntero("Codigo: ");
        int unidades = leerEntero("Unidades de entrada: ");
        System.out.println(inventario.registrarEntrada(codigo, unidades));
    }

    public void registrarSalida(Inventario inventario) {
        int codigo = leerEntero("Codigo: ");
        int unidades = leerEntero("Unidades de salida: ");
        System.out.println(inventario.registrarSalida(codigo, unidades));
    }

    public void buscarProducto(Inventario inventario) {
        int codigo = leerEntero("Codigo: ");
        System.out.println(inventario.consultar(codigo));
    }

    public void listarInventario(Inventario inventario) {
        System.out.println(inventario.listar());
    }

    public void verAlertas(Inventario inventario) {
        System.out.println(inventario.generarAlertas());
    }

    public void verResumen(Inventario inventario) {
        System.out.println(inventario.generarResumen());
    }
}
