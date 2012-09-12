package juego.gato;

import util.Util;

/**
 * Un juego de gato donde son 2 jugadores 
 * El signo del jugador1 siempre es X , y del jugador2 siempre es O
 *
 * @author Alberto Emmanuel Esquivel Vega
 */
class JuegoGato {

    private JugadorGato jugador1;
    private JugadorGato jugador2;
    private int numeroJugadores;
    private char[] arregloGato = {'_', '_', '_', '_', '_', '_', '_', '_', '_'};

    public JuegoGato(int numeroJugadores, JugadorGato jugador1, JugadorGato jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.numeroJugadores = numeroJugadores;
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
    public void atiendeJugador(JugadorGato jugador) {

        while (jugador.isJuegoActivo()) {
            String mensajeRecibido = jugador.getAyudanteServidor().leerMensaje();
            if (mensajeRecibido == null) {
                jugador.setJuegoActivo(false);
            }
            if (jugador.isJuegoActivo() && mensajeRecibido != null) {
                generarRespuesta(jugador, mensajeRecibido);
                revisarJuegoTerminado();
            }
        }
    }

    /**
     * Revisa si el juego actualmente tiene una jugada ya terminada o no
     */
    private void revisarJuegoTerminado() {
        String[] jugadasGanadoras = new String[]{"012", "036", "048", "147", "246", "258", "345", "678"};
        for (String jugada : jugadasGanadoras) {
            //vemos si gano el jugador 1
            if (arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(0)))] == arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(1)))]
                    && arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(0)))] == arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(2)))]
                    && arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(0)))] == 'X') {
                jugador1.setStatus(JugadorGato.STATUS_GANADO);
                if (jugador2 != null) {

                    jugador2.setStatus(JugadorGato.STATUS_PERDIDO);
                }
                return;
            }
            //vemos si gano el jugador 2
            if (arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(0)))] == arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(1)))]
                    && arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(0)))] == arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(2)))]
                    && arregloGato[Integer.parseInt(String.valueOf(jugada.charAt(0)))] == 'O') {
                jugador1.setStatus(JugadorGato.STATUS_PERDIDO);
                if (jugador2 != null) {
                    jugador2.setStatus(JugadorGato.STATUS_GANADO);
                }
                return;
            }
        }
        //checamos el empate , es decir vemos si ya se acabo el tablero
        for (int t = 0; t < arregloGato.length; t++) {
            //no se ha acabado el tablero
            if (arregloGato[t] == '_') {
                return;
            }
        }

        jugador1.setStatus(JugadorGato.STATUS_EMPATADO);
        if (jugador2 != null) {

            jugador2.setStatus(JugadorGato.STATUS_EMPATADO);
        }
    }

    /**
     * Genera la respuesta dependiendo del mensaje que esta reciendo , el
     * jugador es el que esta llamando al metodo
     *
     * @param jugadorSolicitante
     * @param mensaje
     */
    private void generarRespuesta(JugadorGato jugadorSolicitante, String mensaje) {
        JugadorGato competidor = jugadorSolicitante.equals(jugador1) ? jugador2 : jugador1;
        if (mensaje.contains("STATUS GAME")) {
            statusGame(jugadorSolicitante, competidor, mensaje);
            return;
        }

        if (mensaje.contains("COLOCAR")) {
            colocarNoSolo(jugadorSolicitante, competidor, mensaje);
            return;
        }
        if (mensaje.contains("EXIT")) {
            jugadorSolicitante.setJuegoActivo(false);
            jugadorSolicitante.getAyudanteServidor().setOnline(false);
            return;
        }
        if (mensaje.contains("QUIT GAME")) {
            jugadorSolicitante.getAyudanteServidor().enviarMensaje("<OK>");
            jugadorSolicitante.setJuegoActivo(false);
            return;
        }
        jugadorSolicitante.getAyudanteServidor().enviarMensaje("<FAIL 400>");
    }

    /**
     * metodo que responde al comando STATUS GAME hecho por el
     * jugadorSolicitante
     *
     * @param jugadorSolicitante
     * @param competidor
     * @param mensaje
     */
    private void statusGame(JugadorGato jugadorSolicitante, JugadorGato competidor, String mensaje) {
        
        if (jugadorSolicitante.getStatus() == JugadorGato.STATUS_GANADO) {
            jugadorSolicitante.getAyudanteServidor().enviarMensaje("<YOU_WIN>");
            jugadorSolicitante.getAyudanteServidor().setStatusUltimoJuego("<YOU_WIN>");
            jugadorSolicitante.setJuegoActivo(false);
            return;
        }
        if (jugadorSolicitante.getStatus() == JugadorGato.STATUS_PERDIDO) {
            jugadorSolicitante.getAyudanteServidor().enviarMensaje("<YOU_LOSE>");
            jugadorSolicitante.getAyudanteServidor().setStatusUltimoJuego("<YOU_LOSE>");
            jugadorSolicitante.setJuegoActivo(false);
            return;
        }
        if (jugadorSolicitante.getStatus() == JugadorGato.STATUS_EMPATADO) {
            jugadorSolicitante.getAyudanteServidor().enviarMensaje("<DRAW>");
            jugadorSolicitante.getAyudanteServidor().setStatusUltimoJuego("<DRAW>");
            jugadorSolicitante.setJuegoActivo(false);
            return;
        }
        if (numeroJugadores == 2 && competidor == null) {
            jugadorSolicitante.getAyudanteServidor().enviarMensaje("<WAIT_PLAYER>");
            return;
        }
        //en el caso que algun jugador haya terminado el juego
        if (numeroJugadores == 2 && !competidor.isJuegoActivo()) {
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

    /**
     * Metodo que responde al comando COLOCAR hecho por el jugadorSolicitante
     *
     * @param jugadorSolicitante
     * @param competidor
     * @param mensaje
     */
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
            String segundaParte = split[1].trim().substring(0, split[1].trim().length() - 1);
            Double numero = Util.parseDouble(segundaParte);
            if (Double.isNaN(numero) || numero < 1 || numero > 9) {
                jugadorSolicitante.getAyudanteServidor().enviarMensaje("<FAIL 200>");
                return;
            }
            if (this.arregloGato[numero.intValue() - 1] != '_') {
                jugadorSolicitante.getAyudanteServidor().enviarMensaje("<FAIL 200>");
                return;
            }
            if (jugadorSolicitante.equals(jugador1)) {
                this.arregloGato[numero.intValue() - 1] = 'X';
                jugadorSolicitante.setTieneTurno(false);
                jugadorSolicitante.getAyudanteServidor().enviarMensaje("<OK>");
                //verificamos si el juego es de 1 jugador
                if(numeroJugadores==1){
                    //colocamos el valor de la IA y pasamos el turno
                    colocarIA();
                    jugadorSolicitante.setTieneTurno(true);
                }
                else {
                    competidor.setTieneTurno(true);
                }
                return;
            }
            if (jugadorSolicitante.equals(jugador2)) {
                this.arregloGato[numero.intValue() - 1] = 'O';
                jugadorSolicitante.setTieneTurno(false);
                competidor.setTieneTurno(true);
                jugadorSolicitante.getAyudanteServidor().enviarMensaje("<OK>");
                return;
            }

        }
    }

    
    /**
     * la IA coloca un valor en alguna posicion del gato
     */
    private void colocarIA() {
        boolean jugada=false;
        while(!jugada){
            
            int posicion = (int)(Math.random() * 9);
            if(arregloGato[posicion]=='_'){
                jugada=true;
                arregloGato[posicion]='O';
            }
        }
    }
}
