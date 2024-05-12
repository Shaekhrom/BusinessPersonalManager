package objectClasses;

import java.util.Date;

public class Evento {
    private String idEvento;
    private String nombreEvento;
    private String detallesEvento;
    private Date fecha;
    private String idEmpresa;

    public Evento(String nombreEvento, String detallesEvento, Date fecha, String idEmpresa) {
        this.nombreEvento = nombreEvento;
        this.detallesEvento = detallesEvento;
        this.fecha = fecha;
        this.idEmpresa = idEmpresa;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getDetallesEvento() {
        return detallesEvento;
    }

    public void setDetallesEvento(String detallesEvento) {
        this.detallesEvento = detallesEvento;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }


}
