package objectClasses;

/**
 * Clase para controlar las sesiones
 */
public class Estatica {

    static Usuario usuarioEstatico;
    static Empresa empresaEstatica;

    public static void setUsuarioEstatico(Usuario usuarioEstatico) {
        Estatica.usuarioEstatico = usuarioEstatico;
    }

    public static void setEmpresaEstatica(Empresa empresaEstatica) {
        Estatica.empresaEstatica = empresaEstatica;
    }

    public static Usuario getUsuarioEstatico (){
        return usuarioEstatico;
    }

    public static Empresa getEmpresaEstatica() {
        return empresaEstatica;
    }
}
