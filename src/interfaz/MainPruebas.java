package interfaz;

import modelo.Fecha;
import modelo.Producto;
import modelo.ProductoPerecedero;
import modelo.ProductoNoPerecedero;
import negocio.Inventario;


public class MainPruebas {
    private static int comprobaciones;
    private static int fallos;

    private static Inventario nuevo() {
        return new Inventario(100000, 500, 20261122);
    }

    private static ProductoNoPerecedero seco(int codigo, int cantidad, int costo, int espacio) {
        return new ProductoNoPerecedero(codigo, "Arroz", cantidad, costo, 3, espacio, 20261120, "Estante A");
    }

    private static ProductoPerecedero leche(int codigo, int vence) {
        return new ProductoPerecedero(codigo, "Leche", 5, 100, 2, 1, 20261120, vence);
    }

    private static void comprobar(boolean condicion, String detalle) {
        comprobaciones++;
        if (condicion) {
            System.out.println("OK: " + detalle);
        } else {
            fallos++;
            System.out.println("FALLO: " + detalle);
        }
    }

    private static void iniciarCaso(String nombre) {
        System.out.println("\n=== " + nombre + " ===");
    }

    public static void main(String[] args) {
        iniciarCaso("CP01 Registro y encapsulamiento");
        Inventario i = nuevo();
        Producto original = seco(1, 10, 150, 1);
        System.out.println(i.registrar(original));
        System.out.println(i.registrar(leche(2, 20261127)));
        original.agregarStock(20);
        comprobar(i.getTotalProductos() == 2 && i.consultarCantidad(1) == 10,
                "Dos productos registrados; cambiar el original no cambia el inventario.");

        iniciarCaso("CP02 Datos obligatorios e invalidos");
        i = nuevo();
        System.out.println(i.registrar(new Producto(1, "", 10, 100, 0, 1, 20261120)));
        System.out.println(i.registrar(seco(0, 10, 100, 1)));
        System.out.println(i.registrar(seco(1, -1, 100, 1)));
        System.out.println(i.registrar(seco(1, 10, 0, 1)));
        System.out.println(i.registrar(seco(1, 10, 100, 0)));
        comprobar(i.getTotalProductos() == 0, "Ningun registro invalido fue insertado.");

        iniciarCaso("CP03 Codigo duplicado");
        i = nuevo();
        i.registrar(seco(1, 10, 100, 1));
        String resultado = i.registrar(seco(1, 20, 100, 1));
        System.out.println(resultado);
        comprobar(resultado.equals("ERROR: El codigo ya esta registrado.")
                && i.getTotalProductos() == 1 && i.consultarCantidad(1) == 10,
                "Codigo duplicado rechazado sin modificar el stock.");

        iniciarCaso("CP04 Fechas invalidas y bisiesto");
        i = nuevo();
        System.out.println(i.registrar(new Producto(1, "Dato", 1, 100, 0, 1, 20260231)));
        System.out.println(i.registrar(new Producto(1, "Futuro", 1, 100, 0, 1, 20261123)));
        System.out.println(i.registrar(leche(1, 20261119)));
        Fecha valida = new Fecha(20240229);
        Fecha invalida = new Fecha(20230229);
        comprobar(i.getTotalProductos() == 0 && valida.esValida() && invalida.esValida() == false,
                "Se rechazan fechas invalidas y se reconoce el anio bisiesto.");

        iniciarCaso("CP05 Presupuesto");
        i = new Inventario(1000, 500, 20261122);
        System.out.println(i.registrar(seco(1, 10, 100, 1)));
        resultado = i.registrar(seco(2, 1, 100, 1));
        System.out.println(resultado);
        comprobar(resultado.equals("ERROR: Se supera el presupuesto."), "Registro que excede presupuesto rechazado.");
        resultado = i.registrarEntrada(1, 1);
        System.out.println(resultado);
        comprobar(resultado.equals("ERROR: Se supera el presupuesto.") && i.calcularValorTotal() == 1000,
                "Entrada rechazada; el limite exacto de 10 USD se conserva.");

        iniciarCaso("CP06 Capacidad");
        i = new Inventario(100000, 20, 20261122);
        System.out.println(i.registrar(seco(1, 10, 100, 2)));
        System.out.println(i.registrar(seco(2, 1, 100, 1)));
        resultado = i.registrarEntrada(1, 1);
        System.out.println(resultado);
        comprobar(resultado.equals("ERROR: Se supera la capacidad.") && i.calcularEspacioTotal() == 20
                && i.getTotalProductos() == 1, "Capacidad exacta 20; ningun exceso aceptado.");

        iniciarCaso("CP07 Entradas");
        i = nuevo();
        i.registrar(seco(1, 10, 150, 1));
        System.out.println(i.registrarEntrada(1, 5));
        System.out.println(i.registrarEntrada(1, 0));
        System.out.println(i.registrarEntrada(1, -1));
        comprobar(i.consultarCantidad(1) == 15 && i.calcularValorTotal() == 2250,
                "Stock final 15; valor 22.5 USD; cero y negativos rechazados.");

        iniciarCaso("CP08 Salidas y existencias");
        i = nuevo();
        i.registrar(seco(1, 10, 150, 1));
        System.out.println(i.registrarSalida(1, 4));
        System.out.println(i.registrarSalida(1, 7));
        System.out.println(i.registrarSalida(1, 0));
        comprobar(i.consultarCantidad(1) == 6, "Stock 6; salidas sin existencias o de cero rechazadas.");
        System.out.println(i.registrarSalida(1, 6));
        comprobar(i.consultarCantidad(1) == 0 && i.calcularEspacioTotal() == 0, "Venta del saldo deja stock y espacio cero.");

        iniciarCaso("CP09 Lotes vencidos");
        i = nuevo();
        i.registrar(leche(1, 20261121));
        System.out.println(i.registrarSalida(1, 1));
        System.out.println(i.registrarEntrada(1, 1));
        comprobar(i.consultarCantidad(1) == 5, "Lote vencido: no se puede vender ni reponer.");
        i.registrar(leche(2, 20261122));
        System.out.println(i.registrarSalida(2, 1));
        comprobar(i.consultarCantidad(2) == 4, "Un producto que vence hoy se puede vender hoy.");

        iniciarCaso("CP10 Consulta y listado vacio");
        i = nuevo();
        System.out.println(i.listar());
        comprobar(i.listar().equals("Inventario vacio."), "Listado vacio reconocido.");
        i.registrar(seco(1, 10, 100, 1));
        System.out.println(i.consultar(99));
        comprobar(i.consultar(1).contains("Arroz") && i.consultarCantidad(99) == -1,
                "Consulta existente correcta; codigo ausente reconocido.");

        iniciarCaso("CP11 Listado polimorfico");
        i = nuevo();
        i.registrar(seco(1, 10, 100, 1));
        i.registrar(leche(2, 20261127));
        resultado = i.listar();
        System.out.println(resultado);
        comprobar(resultado.contains("Vencimiento: 27/11/2026") && resultado.contains("Ubicacion: Estante A"),
                "mostrarDatos ejecuta la version de cada subclase.");

        iniciarCaso("CP12 Alertas y cambio de mes");
        i = nuevo();
        i.registrar(seco(1, 3, 100, 1));
        i.registrar(leche(2, 20261121));
        i.registrar(leche(3, 20261122));
        i.registrar(leche(4, 20261129));
        i.registrar(leche(5, 20261130));
        resultado = i.generarAlertas();
        System.out.println(resultado);
        comprobar(resultado.contains("1: stock bajo") && resultado.contains("2: VENCIDO")
                && resultado.contains("3: Vence hoy") && resultado.contains("4: Por vencer")
                && resultado.contains("5:") == false, "Alertas correctas: minimo, vencido, hoy, 7 y 8 dias.");
        ProductoPerecedero p = leche(6, 20261203);
        comprobar(p.obtenerEstado(20261129).contains("Por vencer"), "El intervalo de 7 dias cruza el fin de mes.");
        Fecha finAnio = new Fecha(20261231);
        finAnio.avanzarDia();
        comprobar(finAnio.getValor() == 20270101, "El calendario avanza correctamente al anio siguiente.");

        iniciarCaso("CP13 Resumen y dinero");
        i = nuevo();
        i.registrar(seco(1, 3, 10, 2));
        i.registrar(seco(2, 1, 20, 3));
        System.out.println(i.generarResumen());
        comprobar(i.calcularValorTotal() == 50 && i.calcularEspacioTotal() == 9,
                "3 x 10 + 1 x 20 = 50 centavos; espacio 9.");

        iniciarCaso("CP14 Limite de registros");
        i = nuevo();
        for (int n = 1; n <= 100; n++) {
            i.registrar(seco(n, 0, 100, 1));
        }
        resultado = i.registrar(seco(101, 0, 100, 1));
        System.out.println(resultado);
        comprobar(i.getTotalProductos() == 100 && resultado.contains("100 registros"), "El producto 101 fue rechazado.");

        System.out.println("\nCOMPROBACIONES: " + comprobaciones);
        System.out.println("FALLOS: " + fallos);
    }
}
