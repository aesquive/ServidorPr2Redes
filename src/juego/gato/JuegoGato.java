/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.gato;

import servidor.ayudante.AyudanteServidor;
import util.Util;

/**
 * El signo del jugador1 siempre es X , y del jugador2 siempre es O
 *
 * @author Alberto
 */
class JuegoGato {

    private JugadorGato jugador1;
    private JugadorGato jugador2;
    private char[] arregloGato = {'_', '_', '_', '_', '_', '_', '_', '_', '_'};

    public JuegoGato( JugadorGato jugador1, JugadorGato jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
    }

    /**
     * @return the jugador1
     */
    public JugadorGato getJugador1() {
        return jugador1;
    }

    /**
     * @param jugador1 the jugador1 to set
     */
    public void setJugador1(JugadorGato jugador1) {
        this.jugador1 = jugador1;
    }

    /**
     * @return the jugador2
     */
    public JugadorGato getJugador2() {
        return jugador2;
    }

    /**
     * @param jugador2 the jugador2 to set
     */
    public void setJugador2(JugadorGato jugador2) {
        this.jugador2 = jugador2;
    }

    /**
     * en caso de que seamos jugador 1 nos va a atender todo el tiempo este
     * metodo
     */
    public void atiendeJugador1() {
        while (jugador1.isJuegoActivo()) {
            String mensajeRecibido = jugador1.getAyudanteServidor().leerMensaje();
            if (mensajeRecibido == null) {
                jugador1.setJuegoActivo(false);
            }
            if (jugador1.isJuegoActivo() && mensajeRecibido != null) {
                generarRespuesta(jugador1, mensajeRecibido);
                revisarJuegoTerminado();
            }
        }
    }

    /**
     * en caso de que seamos jugador 2 nos va a atender todo el tiempo este
     * metodo
     */
    void atiendeJugador2() {
        while (jugador2.isJuegoActivo()) {
            String mensajeRecibido = jugador2.getAyudanteServidor().leerMensaje();
            if (mensajeRecibido == null) {
                jugador2.setJuegoActivo(false);
            }
            if (jugador2.isJuegoActivo() && mensajeRecibido != null) {
                revisarJuegoTerminado();
            
                generarRespuesta(jugador2, mensajeRecibido);
            
            }
        }
    }
    
    public static void main(String[] args) {
        String cadena="012";
        System.out.println(cadena.charAt(0));
    }

    private void revisarJuegoTerminado() {
        String[] jugadasGanadoras=new String[]{"012","036","048","147","246","258","345","678"};
        for(String jugada:jugadasGanadoras){
            //vemos si gano el jugador 1
            if(arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(0)))]==arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(1)))]
                    && arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(0)))]==arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(2)))] &&
                    arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(0)))]=='X'){
                jugador1.setStatus(JugadorGato.STATUS_GANADO);
                jugador2.setStatus(JugadorGato.STATUS_PERDIDO);
                return;
            }
            //vemos si gano el jugador 2
            if(arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(0)))]==arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(1)))]
                    && arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(0)))]==arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(2)))] &&
                    arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(0)))]=='O'){
                jugador2.setStatus(JugadorGato.STATUS_GANADO);
                jugador1.setStatus(JugadorGato.STATUS_PERDIDO);
                return;
            }
        }
        //checamos el empate , es decir vemos si ya se acabo el tablero
        for(int t=0;t<arregloGato.length;t++){
            //no se ha acabado el tablero
            if(arregloGato[t]=='_'){
                return;
            }
        }
        
                jugador1.setStatus(JugadorGato.STATUS_EMPATADO);
                jugador2.setStatus(JugadorGato.STATUS_EMPATADO);
    }

    private void generarRespuesta(JugadorGato jugadorSolicitante, String mensaje) {
        JugadorGato competidor = jugadorSolicitante.equals(jugador1) ? jugador2 : jugador1;
        if (mensaje.contains("STATUS GAME")) {
            statusGameNoSolo(jugadorSolicitante, competidor, mensaje);
            return;
        }

        if (mensaje.contains("COLOCAR")) {
            colocarNoSolo(jugadorSolicitante, competidor, mensaje);
            return;
        }
        if(mensaje.contains("EXIT")){
            jugadorSolicitante.setJuegoActivo(false);
            jugadorSolicitante.getAyudanteServidor().setOnline(false);
            return;
        }
        if(mensaje.contains("QUIT GAME")){
            jugadorSolicitante.getAyudanteServidor().enviarMensaje("<OK>");
            jugadorSolicitante.setJuegoActivo(false);
            return;
        }
        jugadorSolicitante.getAyudanteServidor().enviarMensaje("<FAIL 400>");
    }

    void juegaSolo() {
    }

    private void statusGameNoSolo(JugadorGato jugadorSolicitante, JugadorGato competidor, String mensaje) {
        //en caso en que aun no exista rival
        if(jugadorSolicitante.getStatus()==JugadorGato.STATUS_GANADO){
            jugadorSolicitante.getAyudanteServidor().enviarMensaje("<YOU_WIN>");
            jugadorSolicitante.getAyudanteServidor().setStatusUltimoJuego("<YOU_WIN>");
            jugadorSolicitante.setJuegoActivo(false);
            return;
        }
        if(jugadorSolicitante.getStatus()==JugadorGato.STATUS_PERDIDO){
            jugadorSolicitante.getAyudanteServidor().enviarMensaje("<YOU_LOSE>");
            jugadorSolicitante.getAyudanteServidor().setStatusUltimoJuego("<YOU_LOSE>");
            jugadorSolicitante.setJuegoActivo(false);
            return;
        }
        if(jugadorSolicitante.getStatus()==JugadorGato.STATUS_EMPATADO){
            jugadorSolicitante.getAyudanteServidor().enviarMensaje("<DRAW>");
            jugadorSolicitante.getAyudanteServidor().setStatusUltimoJuego("<DRAW>");
            jugadorSolicitante.setJuegoActivo(false);
            return;
        }
        if (competidor == null) {
            jugadorSolicitante.getAyudanteServidor().enviarMensaje("<WAIT_PLAYER>");
            return;
        }
        //en el caso que algun jugador haya terminado el juego
        if (!competidor.isJuegoActivo()) {
            jugadorSolicitante.getAyudanteServidor().enviarMensaje("<FINISHED_GAME_BY_USER " + competidor.getAyudanteServidor().getId() + ">");
            jugadorSolicitante.getAyudanteServidor().setStatusUltimoJuego("<FINISHED_GAME_BY_USER " + competidor.getAyudanteServidor().getId() + ">");
            jugadorSolicitante.setJuegoActivo(false);
            return;
        }
        //en el caso en el que el solicitante tenga el turno
        if (jugadorSolicitante.isTieneTurno()) {
            jugadorSolicitante.getAyudanteServidor().enviarMensaje("<true " + Util.arrayToString(this.arregloGato) + ">");
            return;
        }
        jugadorSolicitante.getAyudanteServidor().enviarMensaje("<false " + Util.arrayToString(arregloGato) + ">");
        return;
    }

    private void colocarNoSolo(JugadorGato jugadorSolicitante, JugadorGato competidor, String mensaje) {
        if (!jugadorSolicitante.isTieneTurno()) {
            jugadorSolicitante.getAyudanteServidor().enviarMensaje("<FAIL 200>");
            return;
        }

        if (jugadorSolicitante.isJuegoActivo()) {
                String[] split = mensaje.split("<COLOCAR");
                if (split.length != 2) {
                    jugadorSolicitante.getAyudanteServidor().enviarMensaje("<FAIL 200>");
                    return;
                }
                String segundaParte = split[1].trim().substring(0,split[1].trim().length()-1);
                Double numero=Util.parseDouble(segundaParte);
                if(Double.isNaN(numero) || numero<1 ||numero>9){
                    jugadorSolicitante.getAyudanteServidor().enviarMensaje("<FAIL 200>");
                    return;
                }
                if(this.arregloGato[numero.intValue()-1]!='_'){
                    jugadorSolicitante.getAyudanteServidor().enviarMensaje("<FAIL 200>");
                    return;
                }
                if(jugadorSolicitante.equals(jugador1)){
                    this.arregloGato[numero.intValue()-1]='X';
                    jugadorSolicitante.setTieneTurno(false);
                    competidor.setTieneTurno(true);
                    jugadorSolicitante.getAyudanteServidor().enviarMensaje("<OK>");
                    return;
                }
                if(jugadorSolicitante.equals(jugador2)){
                    this.arregloGato[numero.intValue()-1]='O';
                    jugadorSolicitante.setTieneTurno(false);
                    competidor.setTieneTurno(true);
                    jugadorSolicitante.getAyudanteServidor().enviarMensaje("<OK>");
                    return;
                }
                
        }
    }
}
