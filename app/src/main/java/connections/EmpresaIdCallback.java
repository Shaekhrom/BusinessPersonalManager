package connections;

public interface EmpresaIdCallback {
    // Método llamado cuando se obtiene el ID de la empresa correctamente
    void onIdFetched(String idEmpresa);

    // Método llamado cuando ocurre un error al intentar obtener el ID de la empresa
    void onIdFetchFailure(String errorMessage);
}

