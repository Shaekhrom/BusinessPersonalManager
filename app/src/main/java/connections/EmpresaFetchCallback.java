package connections;

import objectClasses.Empresa;

public interface EmpresaFetchCallback {
    void onEmpresaFetched(Empresa empresa);
    void onEmpresaFetchFailure(String errorMessage);
}

