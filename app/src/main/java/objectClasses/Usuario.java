package objectClasses;

import java.util.Date;

public class Usuario {
    //atributos de usuario
    private int id;
    private int idEmpresa;
    private String email;
    private String nombre;
    private String contrasegna;
    private int edad;
    private String genero;
    private boolean esAdmin;
    private double salario;
    private double puntuacion;


    //constructors


    public Usuario(int id, int idEmpresa, String email, String nombre, String contrasegna, int edad, String genero, boolean esAdmin, double salario, double puntuacion) {
        this.id = id;
        this.idEmpresa = idEmpresa;
        this.email = email;
        this.nombre = nombre;
        this.contrasegna = contrasegna;
        this.edad = edad;
        this.genero = genero;
        this.esAdmin = esAdmin;
        this.salario = salario;
        this.puntuacion = puntuacion;
    }

    //este es usado para registrar un usuario
    public Usuario(String email, String nombre, String contrasegna, int edad, String genero) {
        this.email = email;
        this.nombre = nombre;
        this.contrasegna = contrasegna;
        this.edad = edad;
        this.genero = genero;

    }


    //toString

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", idEmpresa=" + idEmpresa +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", contrasegna='" + contrasegna + '\'' +
                ", edad=" + edad +
                ", genero='" + genero + '\'' +
                ", esAdmin=" + esAdmin +
                ", salario=" + salario +
                ", puntuacion=" + puntuacion +
                '}';
    }


    //gettersAndSetters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasegna() {
        return contrasegna;
    }

    public void setContrasegna(String contrasegna) {
        this.contrasegna = contrasegna;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public boolean isEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }
}
