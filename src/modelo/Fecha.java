package modelo;

public class Fecha {
    private int valor;

    public Fecha(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    private int diasDelMes(int mes, int anio) {
        if (mes == 2) {
            if (anio % 400 == 0 || (anio % 4 == 0 && anio % 100 != 0)) {
                return 29;
            } else {
                return 28;
            }
        } else if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
            return 30;
        } else {
            return 31;
        }
    }

    public boolean esValida() {
        int anio = valor / 10000;
        int mes = valor / 100 % 100;
        int dia = valor % 100;
        if (anio < 1900 || anio > 9999 || mes < 1 || mes > 12) {
            return false;
        }
        return dia >= 1 && dia <= diasDelMes(mes, anio);
    }

    public void avanzarDia() {
        if (esValida()) {
            int anio = valor / 10000;
            int mes = valor / 100 % 100;
            int dia = valor % 100;
            dia++;
            if (dia > diasDelMes(mes, anio)) {
                dia = 1;
                mes++;
                if (mes > 12) {
                    mes = 1;
                    anio++;
                }
            }
            valor = anio * 10000 + mes * 100 + dia;
        }
    }

    public String mostrarDatos() {
        int anio = valor / 10000;
        int mes = valor / 100 % 100;
        int dia = valor % 100;
        return dia + "/" + mes + "/" + anio;
    }
}
