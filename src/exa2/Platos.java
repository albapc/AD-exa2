package exa2;

import java.io.Serializable;


/**
 *
 * @author oracle
 */
public class Platos implements Serializable {

    private String codigop;
    private String nomep;
    private int total;

    
    public Platos() {
        this("", "", 0);
    }

    public Platos(String codigo, String nome, int total) {
        this.codigop = codigo;
        this.nomep = nome;
        this.total = total;
    }

    public void setCodigop(String code) {
        this.codigop = code;
    }

    public String getCodigop() {
        return codigop;
    }

    public void setNomep(String nome) {
        this.nomep = nome;
    }

    public String getNomep() {
        return nomep;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "codigo plato : " + codigop + "\n"
                + "nome plato  : " + nomep + "\n"
                + "graxa total do plato : " + total + "\n";
    }
    
}
