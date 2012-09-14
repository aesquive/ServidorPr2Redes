package juego.gato;

import servidor.ayudante.ManejadorCliente;

/**
 * Clase que es un jugador de gato que mantiene turno , status y saber si aun sigue activo
 * @author Alberto Emmanuel Esquivel Vega
 * 
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
    private ManejadorCliente manejadorClientes;

    public final static int STATUS_ENJUEGO=1;
    public final static int STATUS_GANADO=2;
    public final static int STATUS_PERDIDO=3;
    public final static int STATUS_EMPATADO=4;
    
    /**
     * Constructor
     * @param ayudanteServidor 
     */
    public JugadorGato(ManejadorCliente ayudanteServidor) {
        this.status=STATUS_ENJUEGO;
        this.manejadorClientes=ayudanteServidor;
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
    public ManejadorCliente getAyudanteServidor() {
        return manejadorClientes;
    }

    /**
     * @param ayudanteServidor the ayudanteServidor to set
     */
    public void setAyudanteServidor(ManejadorCliente ayudanteServidor) {
        this.manejadorClientes = ayudanteServidor;
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
