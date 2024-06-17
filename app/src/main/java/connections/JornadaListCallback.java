// JornadaListCallback.java
package connections;

import java.util.ArrayList;
import objectClasses.Jornada;

public interface JornadaListCallback {
    void onJornadaListReceived(ArrayList<Jornada> jornadasList);
}
