package connections;

import objectClasses.Usuario;

public interface UserFetchCallback {
    // Método llamado cuando se obtiene el usuario correctamente
    void onUserFetched(Usuario usuario);

    // Método llamado cuando ocurre un error al intentar obtener el usuario
    void onUserFetchFailure(String errorMessage);
}

