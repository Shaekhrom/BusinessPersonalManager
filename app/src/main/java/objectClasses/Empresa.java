package objectClasses;

import java.util.ArrayList;

public class Empresa {
    //atributos de objeto
    private String id;
    private String nombre;
    private ArrayList<String>listaAdministradores;
    private ArrayList<String>listaEmpleados;

    //constructor del objeto empresa
    public Empresa(String id, String nombre, ArrayList<String> listaAdministradores, ArrayList<String> listaEmpleados) {
        this.id = id;
        this.nombre = nombre;
        this.listaAdministradores = listaAdministradores;
        this.listaEmpleados = listaEmpleados;
    }

    //toString
    @Override
    public String toString() {
        return "Empresa{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", listaAdministradores=" + listaAdministradores +
                ", listaEmpleados=" + listaEmpleados +
                '}';
    }

    //getters y setters

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

    public ArrayList<String> getListaAdministradores() {
        return listaAdministradores;
    }

    public void setListaAdministradores(ArrayList<String> listaAdministradores) {
        this.listaAdministradores = listaAdministradores;
    }

    public ArrayList<String> getListaEmpleados() {
        return listaEmpleados;
    }

    public void setListaEmpleados(ArrayList<String> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }
}
