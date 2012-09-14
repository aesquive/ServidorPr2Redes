package juego.gato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import servidor.ayudante.ManejadorCliente;

/**
 * Clase que se encarga de administrar todos los juegos de gato
 * @author Alberto
 */
public class AdministradorGato {

    /**
     * Jugador que esta intentando acceder al juego
     */
    private JugadorGato jugadorGato;
    /**
     * Variable que nos dice si el jugador va a jugar solo
     */
    private boolean modoSolitario;
    /**
     * lista de juegos disponibles 
     */
    private static List<JuegoGato> listaJuegos = new ArrayList<JuegoGato>();

    /**
     * Administra el juego de gato
     * @param ayudanteServidor
     * @param modoSolitario 
     */
    public AdministradorGato(ManejadorCliente ayudanteServidor, boolean modoSolitario) {
        this.jugadorGato = new JugadorGato(ayudanteServidor);
        this.modoSolitario = modoSolitario;

    }

    /**
     * metodo que inicia un juego , clasifica si es que se juega contra la IA o con otra persona
     */
    public void iniciarJuego() {
        jugadorGato.getAyudanteServidor().enviarMensaje("<OK>");
        if (modoSolitario) {
            jugadorGato.setTieneTurno(true);
            JuegoGato juegoGato = new JuegoGato( 1,jugadorGato, null);
            juegoGato.atiendeJugador(jugadorGato);
            return;
        }
        modoCooperativo();
    }

    /**
     * metodo que atiende cuando se juega de 2 jugadores
     */
    private void modoCooperativo() {
        JuegoGato juegoGato = obtenerJuegoGato();
        if (juegoGato == null) {
            jugadorGato.setTieneTurno(true);
            juegoGato=new JuegoGato(2, jugadorGato, null);
            listaJuegos.add(juegoGato);
            juegoGato.atiendeJugador(jugadorGato);
            return;
        }
        if (juegoGato != null) {
            jugadorGato.setTieneTurno(false);
            juegoGato.setJugador2(jugadorGato);
            juegoGato.atiendeJugador(jugadorGato);
            return;
        }
    }

    /**
     * metodo que saca un juego de gato que se encuentre en la lista ,
     * el juego que regresa debe de tener un jugador activo 
     * @return 
     */
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

}
