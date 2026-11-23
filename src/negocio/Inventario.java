package negocio;

import modelo.Producto;
import modelo.Fecha;


public class Inventario {
    private Producto[] productos;
    private int totalProductos;
    private int presupuestoCentavos;
    private int capacidad;
    private int fechaActual;

    public Inventario(int presupuestoCentavos, int capacidad, int fechaActual) {
        productos = new Producto[100];
        totalProductos = 0;
        this.presupuestoCentavos = presupuestoCentavos;
        this.capacidad = capacidad;
        this.fechaActual = fechaActual;
    }

    public int getTotalProductos() {
        return totalProductos;
    }

    public int getFechaActual() {
        return fechaActual;
    }

    private int buscarPosicion(int codigo) {
        for (int i = 0; i < totalProductos; i++) {
            if (productos[i].getCodigo() == codigo) {
                return i;
            }
        }
        return -1;
    }

    private String validarAumento(Producto producto, int unidades) {
        int saldo = presupuestoCentavos - calcularValorTotal();
        int espacioLibre = capacidad - calcularEspacioTotal();

        if (unidades > saldo / producto.getCostoCentavos()) {
            return "ERROR Se supera el presupuesto.";
        } else if (unidades > espacioLibre / producto.getEspacioUnitario()) {
            return "ERROR Se supera la capacidad.";
        }
        return "";
    }

    public String registrar(Producto producto) {
        Fecha fecha = new Fecha(fechaActual);
        if (presupuestoCentavos <= 0 || presupuestoCentavos > 1000000
                || capacidad <= 0 || capacidad > 1000000 || fecha.esValida() == false) {
            return "ERROR Configuracion de inventario no valida.";
        } else if (producto == null) {
            return "ERROR El producto es obligatorio.";
        }
        String mensaje = producto.validarDatos();
        if (mensaje.isEmpty() == false) {
            return mensaje;
        } else if (totalProductos == productos.length) {
            return "ERROR: Limite de 100 registros alcanzado.";
        } else if (buscarPosicion(producto.getCodigo()) != -1) {
            return "ERROR: El codigo ya esta registrado.";
        } else if (producto.getFechaIngreso() > fechaActual) {
            return "ERROR: El ingreso no puede ser futuro.";
        }
        mensaje = validarAumento(producto, producto.getCantidad());
        if (mensaje.isEmpty() == false) {
            return mensaje;
        }
        productos[totalProductos] = producto.copiar();
        totalProductos++;
        return "Producto registrado: " + producto.getCodigo();
    }

    public String registrarEntrada(int codigo, int unidades) {
        int posicion = buscarPosicion(codigo);
        if (posicion == -1) {
            return "ERROR Producto no encontrado.";
        }
        Producto producto = productos[posicion];
        if (unidades <= 0 || unidades > 1000000 - producto.getCantidad()) {
            return "ERROR Cantidad de entrada no valida.";
        } else if (producto.estaVencido(fechaActual)) {
            return "ERROR No se puede reponer un lote vencido.";
        }
        String mensaje = validarAumento(producto, unidades);
        if (mensaje.isEmpty() == false) {
            return mensaje;
        }
        producto.agregarStock(unidades);
        return "Entrada registrada Stock actual: " + producto.getCantidad();
    }

    public String registrarSalida(int codigo, int unidades) {
        int posicion = buscarPosicion(codigo);
        if (posicion == -1) {
            return "ERROR Producto no encontrado.";
        }
        Producto producto = productos[posicion];
        if (producto.estaVencido(fechaActual)) {
            return "ERROR No se puede vender un lote vencido.";
        } else if (producto.retirarStock(unidades)) {
            return "Salida registrada Stock actual: " + producto.getCantidad();
        } else {
            return "ERROR La salida debe ser positiva y no superar las existencias.";
        }
    }

    public String consultar(int codigo) {
        int posicion = buscarPosicion(codigo);
        if (posicion == -1) {
            return "ERROR Producto no encontrado.";
        }
        return productos[posicion].mostrarDatos();
    }

    public int consultarCantidad(int codigo) {
        int posicion = buscarPosicion(codigo);
        if (posicion == -1) {
            return -1;
        }
        return productos[posicion].getCantidad();
    }

    public String listar() {
        if (totalProductos == 0) {
            return "Inventario vacio.";
        }
        String texto = "";
        for (int i = 0; i < totalProductos; i++) {
            texto = texto + productos[i].mostrarDatos() + "\n--------------------\n";
        }
        return texto;
    }

    public String generarAlertas() {
        String texto = "";
        for (int i = 0; i < totalProductos; i++) {
            Producto producto = productos[i];
            if (producto.getCantidad() <= producto.getStockMinimo()) {
                texto = texto + producto.getCodigo() + ": stock bajo.\n";
            }
            String estado = producto.obtenerEstado(fechaActual);
            if (producto.getCantidad() > 0 && estado.equals("Sin vencimiento") == false
                    && estado.equals("Vigente") == false) {
                texto = texto + producto.getCodigo() + ": " + estado + ".\n";
            }
        }
        if (texto.isEmpty()) {
            return "Sin alertas";
        }
        return texto;
    }

    public int calcularValorTotal() {
        int total = 0;
        for (int i = 0; i < totalProductos; i++) {
            total = total + productos[i].calcularValor();
        }
        return total;
    }

    public int calcularEspacioTotal() {
        int total = 0;
        for (int i = 0; i < totalProductos; i++) {
            total = total + productos[i].calcularEspacio();
        }
        return total;
    }

    public String generarResumen() {
        return "Registros: " + totalProductos + "/100"
                + "\nValor a costo: " + calcularValorTotal() / 100.0 + " USD"
                + "\nPresupuesto disponible: " + (presupuestoCentavos - calcularValorTotal()) / 100.0 + " USD"
                + "\nEspacio usado: " + calcularEspacioTotal() + "/" + capacidad;
    }
}
