package connections;

import java.util.ArrayList;

import objectClasses.Correo;

public interface ObtenerCorreosCallback {
    void onCorreosObtenidos(ArrayList<Correo> correos, String errorMessage);
}
