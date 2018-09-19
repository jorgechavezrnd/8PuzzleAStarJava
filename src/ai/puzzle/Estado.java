package ai.puzzle;

import java.util.Arrays;

public class Estado implements Comparable<Estado> {
    
    private Integer[][] estado;
    private double g;
    private double h;
    private double f;
    private String padre;

    public Estado() {
        estado = null;
        g = 0.0;
        h = 0.0;
        f = 0.0;
        padre = null;
    }

    public Estado(Integer[][] estado) {
        this.estado = estado;
        g = 0.0;
        h = 0.0;
        f = 0.0;
        padre = null;
    }

    public Integer[][] getEstado() {
        return estado;
    }

    public void setEstado(Integer[][] estado) {
        this.estado = estado;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public String getPadre() {
        return padre;
    }

    public void setPadre(String padre) {
        this.padre = padre;
    }

    @Override
    public String toString() {
        String estadoString = null;

        if (estado != null) {
            estadoString = "";
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    estadoString += estado[i][j];
                }
            }
        }

        return  "{" +
                "estado=" + estadoString +
                ", g=" + g +
                ", h=" + h +
                ", f=" + f +
                ", padre=" + padre +
                "}";
    }

    @Override
    public int compareTo(Estado estado) {
        return Double.compare(f, estado.f);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estado other = (Estado) obj;
		if (!Arrays.deepEquals(estado, other.estado))
			return false;
		return true;
    }

}