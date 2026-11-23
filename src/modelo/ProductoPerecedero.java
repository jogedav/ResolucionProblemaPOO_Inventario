package modelo;

public class ProductoPerecedero extends Producto {
    private int fechaVencimiento;

    public ProductoPerecedero(int codigo, String nombre, int cantidad, int costoCentavos,
                             int stockMinimo, int espacioUnitario, int fechaIngreso, int fechaVencimiento) {
        super(codigo, nombre, cantidad, costoCentavos, stockMinimo, espacioUnitario, fechaIngreso);
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getFechaVencimiento() {
        return fechaVencimiento;
    }

    @Override
    public String validarDatos() {
        String mensaje = super.validarDatos();
        Fecha fecha = new Fecha(fechaVencimiento);
        if (mensaje.isEmpty() == false) {
            return mensaje;
        } else if (fecha.esValida() == false || fechaVencimiento < getFechaIngreso()) {
            return "ERROR La fecha de vencimiento no es valida.";
        }
        return "";
    }

    @Override
    public boolean estaVencido(int fechaActual) {
        return fechaVencimiento < fechaActual;
    }

    @Override
    public String obtenerEstado(int fechaActual) {
        Fecha limite = new Fecha(fechaActual);
        for (int i = 0; i < 7; i++) {
            limite.avanzarDia();
        }
        if (fechaVencimiento < fechaActual) {
            return "VENCIDO";
        } else if (fechaVencimiento == fechaActual) {
            return "Vence hoy";
        } else if (fechaVencimiento <= limite.getValor()) {
            return "Por vencer en los proximos 7 dias";
        } else {
            return "Vigente";
        }
    }

    @Override
    public String mostrarDatos() {
        Fecha fecha = new Fecha(fechaVencimiento);
        return super.mostrarDatos() + "\nTipo Perecedero\nVencimiento: " + fecha.mostrarDatos();
    }

    @Override
    public Producto copiar() {
        return new ProductoPerecedero(getCodigo(), getNombre(), getCantidad(), getCostoCentavos(),
                getStockMinimo(), getEspacioUnitario(), getFechaIngreso(), fechaVencimiento);
    }
}
