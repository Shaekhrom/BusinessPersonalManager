package objectClasses;

/**
 * Clase para controlar las sesiones
 */
public class Estatica {

    static Usuario usuarioEstatico = null;
    static Empresa empresaEstatica = null;

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
