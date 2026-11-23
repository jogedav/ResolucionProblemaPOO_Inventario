package modelo;


public class Producto {
    private int codigo;
    private String nombre;
    private int cantidad;
    private int costoCentavos;
    private int stockMinimo;
    private int espacioUnitario;
    private int fechaIngreso;

    public Producto(int codigo, String nombre, int cantidad, int costoCentavos,
                    int stockMinimo, int espacioUnitario, int fechaIngreso) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.costoCentavos = costoCentavos;
        this.stockMinimo = stockMinimo;
        this.espacioUnitario = espacioUnitario;
        this.fechaIngreso = fechaIngreso;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getCostoCentavos() {
        return costoCentavos;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public int getEspacioUnitario() {
        return espacioUnitario;
    }

    public int getFechaIngreso() {
        return fechaIngreso;
    }

    public String validarDatos() {
        Fecha fecha = new Fecha(fechaIngreso);
        if (codigo <= 0) {
            return "ERROR El codigo debe ser positivo";
        } else if (nombre == null || nombre.trim().isEmpty()) {
            return "ERROR El nombre es obligatorio";
        } else if (cantidad < 0 || cantidad > 1000000) {
            return "ERROR Cantidad fuera del intervalo 0 a 1000000";
        } else if (costoCentavos <= 0 || costoCentavos > 1000000) {
            return "ERROR Costo fuera del intervalo 1 a 1000000 centavos";
        } else if (stockMinimo < 0 || stockMinimo > 1000000) {
            return "ERROR Stock minimo fuera del intervalo permitido";
        } else if (espacioUnitario <= 0 || espacioUnitario > 1000000) {
            return "ERROR El espacio debe estar entre 1 y 1000000";
        } else if (fecha.esValida() == false) {
            return "ERROR La fecha de ingreso no es valida";
        }
        return "";
    }

    public boolean agregarStock(int unidades) {
        if (unidades > 0 && unidades <= 1000000 - cantidad) {
            cantidad = cantidad + unidades;
            return true;
        }
        return false;
    }

    public boolean retirarStock(int unidades) {
        if (unidades > 0 && unidades <= cantidad) {
            cantidad = cantidad - unidades;
            return true;
        }
        return false;
    }


    public int calcularValor() {
        return cantidad * costoCentavos;
    }

    public int calcularEspacio() {
        return cantidad * espacioUnitario;
    }

    public boolean estaVencido(int fechaActual) {
        return false;
    }

    public String obtenerEstado(int fechaActual) {
        return "Sin vencimiento";
    }

    public String mostrarDatos() {
        Fecha fecha = new Fecha(fechaIngreso);
        return "Codigo: " + codigo + "\nNombre: " + nombre + "\nCantidad: " + cantidad
                + "\nCosto unitario: " + costoCentavos / 100.0 + " USD"
                + "\nStock minimo: " + stockMinimo + "\nEspacio unitario: " + espacioUnitario
                + "\nIngreso: " + fecha.mostrarDatos();
    }


    public Producto copiar() {
        return new Producto(codigo, nombre, cantidad, costoCentavos,
                stockMinimo, espacioUnitario, fechaIngreso);
    }
}
