package juego.gato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import servidor.ayudante.AyudanteServidor;

/**
 *
 * @author Alberto
 */
public class AdministradorGato {

    private JugadorGato jugadorGato;
    private boolean modoSolitario;
    private static List<JuegoGato> listaJuegos = new ArrayList<JuegoGato>();

    public AdministradorGato(AyudanteServidor ayudanteServidor, boolean modoSolitario) {
        this.jugadorGato = new JugadorGato(ayudanteServidor);
        this.modoSolitario = modoSolitario;

    }

    public void iniciarJuego() {
        jugadorGato.getAyudanteServidor().enviarMensaje("<OK>");
        if (modoSolitario) {
            jugadorGato.getAyudanteServidor().enviarMensaje("<OK>");
            JuegoGato juegoGato = new JuegoGato( jugadorGato, null);
            juegoGato.juegaSolo();
            return;
        }
        atenderCliente();
    }

    private void atenderCliente() {
        JuegoGato juegoGato = obtenerJuegoGato();
        if (juegoGato == null) {
            jugadorGato.setTieneTurno(true);
            juegoGato=new JuegoGato( jugadorGato, null);
            listaJuegos.add(juegoGato);
            juegoGato.atiendeJugador1();
            return;
        }
        if (juegoGato != null) {
            jugadorGato.setTieneTurno(false);
            juegoGato.setJugador2(jugadorGato);
            juegoGato.atiendeJugador2();
            return;
        }
    }

    private synchronized JuegoGato obtenerJuegoGato() {
        for (int t = 0; t < listaJuegos.size(); t++) {
            JuegoGato juego = listaJuegos.get(t);
            //si el jugador que esta activo 
            if (juego != null && juego.getJugador1().isJuegoActivo()) {
                listaJuegos.remove(t);
                return juego;
            }
        }
        return null;
    }

    private void atenderPeticionesCliente() {
    }
}
