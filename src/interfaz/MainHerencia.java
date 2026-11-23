package interfaz;

import modelo.Producto;
import modelo.ProductoPerecedero;
import modelo.ProductoNoPerecedero;


public class MainHerencia {
    public static void main(String[] args) {
        ProductoPerecedero p1 = new ProductoPerecedero(
                1, "Leche", 10, 150, 3, 1, 20261122, 20261127);
        ProductoNoPerecedero p2 = new ProductoNoPerecedero(
                2, "Arroz", 20, 100, 5, 1, 20261122, "Estante A");

        System.out.println("***Producto Perecedero***");
        System.out.println(p1.mostrarDatos());
        if (p1.agregarStock(5)) {
            System.out.println("Se han ingresado las unidades");
        } else {
            System.out.println("No se han podido ingresar las unidades");
        }
        System.out.println(p1.mostrarDatos());

        System.out.println("\nProducto No Perecedero");
        System.out.println(p2.mostrarDatos());
        if (p2.retirarStock(4)) {
            System.out.println("Se han retirado las unidades");
        } else {
            System.out.println("No se han podido retirar las unidades");
        }
        System.out.println(p2.mostrarDatos());

        System.out.println("\nPolimorfismo");
        Producto producto = p1;
        System.out.println(producto.mostrarDatos());
        producto = p2;
        System.out.println(producto.mostrarDatos());
    }
}
