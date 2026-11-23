package modelo;

public class ProductoNoPerecedero extends Producto {
    private String ubicacion;

    public ProductoNoPerecedero(int codigo, String nombre, int cantidad, int costoCentavos,
                               int stockMinimo, int espacioUnitario, int fechaIngreso, String ubicacion) {
        super(codigo, nombre, cantidad, costoCentavos, stockMinimo, espacioUnitario, fechaIngreso);
        this.ubicacion = ubicacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    @Override
    public String validarDatos() {
        String mensaje = super.validarDatos();
        if (mensaje.isEmpty() == false) {
            return mensaje;
        } else if (ubicacion == null || ubicacion.trim().isEmpty()) {
            return "ERROR La ubicacion es obligatoria.";
        }
        return "";
    }

    @Override
    public String mostrarDatos() {
        return super.mostrarDatos() + "\nTipo No perecedero\nUbicacion: " + ubicacion;
    }

    @Override
    public Producto copiar() {
        return new ProductoNoPerecedero(getCodigo(), getNombre(), getCantidad(), getCostoCentavos(),
                getStockMinimo(), getEspacioUnitario(), getFechaIngreso(), ubicacion);
    }
}
