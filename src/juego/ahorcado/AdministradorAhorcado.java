/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package juego.ahorcado;

import servidor.ayudante.AyudanteServidor;
import util.Util;

/**
 *
 * @author Alberto
 * Clase que se encarga de administrar el juego de ahorcado de un cliente 
 */
public class AdministradorAhorcado {

    /**
     * El manejador mas general del cliente
     */
    private AyudanteServidor ayudanteServidor;
    /**
     * Variable que nos dice si el usuario sigue jugando o no
     */
    private boolean juegoActivo;
    /**
     * Cadena de caracteres con la palabra a adivinar
     */
    private char[] cadenaVisible;
    /**
     * Cadena que contiene los caracteres ocultos de la palabra a adivinar
     */
    private char[] cadenaOculta;
    /**
     * Numero maximo de intentos para adivinar la palabra
     */
    private static final int numeroMaximoIntentos = 15;
    /**
     * Numero de intentos actuales para adivinar la palabra
     */
    private int numeroIntentosActuales;

    /**
     * Constructor
     * @param ayudanteServidor 
     */
    public AdministradorAhorcado(AyudanteServidor ayudanteServidor) {
        this.ayudanteServidor = ayudanteServidor;
        this.juegoActivo = true;
        generarCadenaAhorcado();
    }

    /**
     * metodo que inicia un juego de ahorcado
     */
    public void iniciarJuego() {
        ayudanteServidor.enviarMensaje("<OK>");
        atenderJuego();
    }

    /**
     * metodo que se dedica a la interaccion con el cliente al jugar gato
     */
    private void atenderJuego() {
        while (juegoActivo) {
            String mensajeRecibido = ayudanteServidor.leerMensaje();
            if (mensajeRecibido == null) {
                juegoActivo = false;
            }
            if (juegoActivo && mensajeRecibido != null) {
                generarRespuesta(mensajeRecibido);
                revisarJuegoTerminado();
            }
        }
    }

    /**
     * Metodo que genera la respuesta de acuerdo al mensaje recibido
     * @param mensaje 
     */
    private void generarRespuesta(String mensaje) {
        if (mensaje.contains("EXIT")) {
            ayudanteServidor.setOnline(false);
            juegoActivo = false;
            return;
        }
        if (mensaje.contains("QUIT GAME")) {
            juegoActivo = false;
            ayudanteServidor.enviarMensaje("<OK>");
            return;
        }
        if (mensaje.contains("STATUS GAME")) {
            ayudanteServidor.enviarMensaje("<" + Util.arrayToString(cadenaOculta) + " " + (numeroMaximoIntentos - numeroIntentosActuales) + ">");
            return;
        }
        if (mensaje.contains("TRY_CHAR")) {
            tryChar(mensaje);
            return;
        }
        if (mensaje.contains("TRY_WORD")) {
            tryWord(mensaje);
            return;
        }

        ayudanteServidor.enviarMensaje("<FAIL 400>");
    }

    /**
     * metodo que genera una cadena aleatoria 
     */
    private void generarCadenaAhorcado() {
        cadenaVisible = "algo".toCharArray();
        cadenaOculta = new char[cadenaVisible.length];
        for (int t = 0; t < cadenaVisible.length; t++) {
            cadenaOculta[t] = '_';
        }
    }

    /**
     * metodo que intenta el coman TRY_CHAR
     * @param mensaje 
     */
    private void tryChar(String mensaje) {
        String[] split = mensaje.split("<TRY_CHAR");
        System.out.println(" * " + split);
        if (split.length != 2) {
            ayudanteServidor.enviarMensaje("<FAIL 301>");
            return;
        }
        String segundaParte = split[1].trim();
        if(segundaParte.length()==0){
            ayudanteServidor.enviarMensaje("<FAIL 301>");
            return;
        }
        for (int t = 0; t < this.cadenaVisible.length; t++) {
            if (segundaParte.charAt(0) == cadenaVisible[t]) {
                cadenaOculta[t] = cadenaVisible[t];
            }
        }
        this.numeroIntentosActuales++;
        ayudanteServidor.enviarMensaje("<OK>");
    }

    /**
     * metodo que intenta el comando TRY_WORD
     * @param mensaje 
     */
    private void tryWord(String mensaje) {
        String[] split = mensaje.split("<TRY_WORD");
        if (split.length != 2) {
            ayudanteServidor.enviarMensaje("<FAIL 300>");
            return;
        }
        String segundaParte = split[1].trim().substring(0, split[1].trim().length() - 1);
        if(segundaParte.length()==0){
            ayudanteServidor.enviarMensaje("<FAIL 300>");
            return;
        }
        for (int t = 0; t < segundaParte.toCharArray().length; t++) {
            if (segundaParte.charAt(t) == cadenaVisible[t]) {
                cadenaOculta[t] = segundaParte.charAt(t);
            }
        }
        ayudanteServidor.enviarMensaje("<OK>");
    }

    /**
     * metodo que revisa si el juego ya se acabo o no
     */
    private void revisarJuegoTerminado() {
        if (!Util.arregloContiene(this.cadenaOculta, '_') && this.numeroIntentosActuales < numeroMaximoIntentos) {
            ayudanteServidor.setStatusUltimoJuego("<YOU_WIN>");
            juegoActivo=false;
            return;
        } else if (numeroIntentosActuales > numeroMaximoIntentos) {
            ayudanteServidor.setStatusUltimoJuego("<YOU_LOSE>");
            juegoActivo=false;
            return;
        }

    }
}
