package juego.gato;

import servidor.ayudante.AyudanteServidor;

/**
 *
 * @author Alberto Emmanuel Esquivel Vega
 */
public class JugadorGato {

    /**
     * variable que nos dice si el jugador tiene el turno
     */
    private boolean tieneTurno;
    /**
     * variable que nos dice si el jugador sigue activo en el juego
     */
    private boolean juegoActivo;
    /**
     * variable que nos dice el status del jugador
     */
    private int status;
    /**
     * ayudante de servidor que mantiene la conexion con el cliente
     */
    private AyudanteServidor ayudanteServidor;

    public final static int STATUS_ENJUEGO=1;
    public final static int STATUS_GANADO=2;
    public final static int STATUS_PERDIDO=3;
    public final static int STATUS_EMPATADO=4;
    
    /**
     * Constructor
     * @param ayudanteServidor 
     */
    public JugadorGato(AyudanteServidor ayudanteServidor) {
        this.status=STATUS_ENJUEGO;
        this.ayudanteServidor=ayudanteServidor;
        this.juegoActivo=true;
    }
    

    /**
     * @return the tieneTurno
     */
    public boolean isTieneTurno() {
        return tieneTurno;
    }

    /**
     * @param tieneTurno the tieneTurno to set
     */
    public void setTieneTurno(boolean tieneTurno) {
        this.tieneTurno = tieneTurno;
    }

    /**
     * @return the juegoActivo
     */
    public boolean isJuegoActivo() {
        return juegoActivo;
    }

    /**
     * @param juegoActivo the juegoActivo to set
     */
    public void setJuegoActivo(boolean juegoActivo) {
        this.juegoActivo = juegoActivo;
    }

    /**
     * @return the ayudanteServidor
     */
    public AyudanteServidor getAyudanteServidor() {
        return ayudanteServidor;
    }

    /**
     * @param ayudanteServidor the ayudanteServidor to set
     */
    public void setAyudanteServidor(AyudanteServidor ayudanteServidor) {
        this.ayudanteServidor = ayudanteServidor;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    
}
