package objectClasses;

import java.util.ArrayList;

public class Empresa {
    //atributos de objeto
    private String id;
    private String nombre;
    private String contrasenaEmpresa;
    private String sector;
    private String detalles;


    public Empresa(String id, String nombre, String contrasenaEmpresa, String sector, String detalles) {
        this.id = id;
        this.nombre = nombre;
        this.contrasenaEmpresa = contrasenaEmpresa;
        this.sector = sector;
        this.detalles = detalles;
    }

    public Empresa(String nombre, String contrasenaEmpresa, String sector, String detalles) {
        this.nombre = nombre;
        this.contrasenaEmpresa = contrasenaEmpresa;
        this.sector = sector;
        this.detalles = detalles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenaEmpresa() {
        return contrasenaEmpresa;
    }

    public void setContrasenaEmpresa(String contrasenaEmpresa) {
        this.contrasenaEmpresa = contrasenaEmpresa;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
}
