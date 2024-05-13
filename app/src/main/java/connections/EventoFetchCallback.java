package connections;

import java.util.List;

import objectClasses.Evento;

public interface EventoFetchCallback {
    void onEventosFetched(List<Evento> eventos);
    void onEventosFetchFailure(String errorMessage);
}

