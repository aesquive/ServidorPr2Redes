package juego.ahorcado;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.ayudante.ManejadorCliente;
import util.Util;

/**
 *
 * @author Alberto Emmanuel Esquivel Vega
 * Clase que se encarga de administrar el juego de ahorcado de un cliente 
 */
public class AdministradorAhorcado {

    private static final String nombreArchivo="diccionario.dic";
    /**
     * El manejador mas general del cliente
     */
    private ManejadorCliente manejadorClientes;
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
    public AdministradorAhorcado(ManejadorCliente ayudanteServidor) {
        this.manejadorClientes = ayudanteServidor;
        this.juegoActivo = true;
        generarCadenaAhorcado();
    }

    /**
     * metodo que inicia un juego de ahorcado
     */
    public void iniciarJuego() {
        manejadorClientes.enviarMensaje("<OK>");
        atenderJuego();
    }

    /**
     * metodo que se dedica a la interaccion con el cliente al jugar gato
     */
    private void atenderJuego() {
        while (juegoActivo) {
            String mensajeRecibido = manejadorClientes.leerMensaje();
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
        if (mensaje.contains("<EXIT")) {
            manejadorClientes.setOnline(false);
            juegoActivo = false;
            return;
        }
        if (mensaje.contains("<QUIT GAME")) {
            juegoActivo = false;
            manejadorClientes.enviarMensaje("<OK>");
            return;
        }
        if (mensaje.contains("<STATUS GAME")) {
            manejadorClientes.enviarMensaje("<" + Util.arrayToString(cadenaOculta) + " " + (numeroMaximoIntentos - numeroIntentosActuales) + ">");
            return;
        }
        if (mensaje.contains("<TRY_CHAR")) {
            tryChar(mensaje);
            return;
        }
        if (mensaje.contains("<TRY_WORD")) {
            tryWord(mensaje);
            return;
        }

        manejadorClientes.enviarMensaje("<FAIL 400>");
    }

    /**
     * metodo que genera una cadena aleatoria 
     */
    private void generarCadenaAhorcado() {
        cadenaVisible = obtenerPalabra().toCharArray();
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
        if (split.length != 2) {
            manejadorClientes.enviarMensaje("<FAIL 301>");
            return;
        }
        String segundaParte = split[1].trim();
        if(segundaParte.length()==0){
            manejadorClientes.enviarMensaje("<FAIL 301>");
            return;
        }
        for (int t = 0; t < this.cadenaVisible.length; t++) {
            if (segundaParte.charAt(0) == cadenaVisible[t]) {
                cadenaOculta[t] = cadenaVisible[t];
            }
        }
        this.numeroIntentosActuales++;
        manejadorClientes.enviarMensaje("<OK>");
    }

    /**
     * metodo que intenta el comando TRY_WORD
     * @param mensaje 
     */
    private void tryWord(String mensaje) {
        String[] split = mensaje.split("<TRY_WORD");
        if (split.length != 2) {
            manejadorClientes.enviarMensaje("<FAIL 300>");
            return;
        }
        String segundaParte = split[1].trim().substring(0, split[1].trim().length() - 1);
        if(segundaParte.length()==0){
            manejadorClientes.enviarMensaje("<FAIL 300>");
            return;
        }
        for (int t = 0; t < segundaParte.toCharArray().length; t++) {
            if (segundaParte.charAt(t) == cadenaVisible[t]) {
                cadenaOculta[t] = segundaParte.charAt(t);
            }
        }
        manejadorClientes.enviarMensaje("<OK>");
    }

    /**
     * metodo que revisa si el juego ya se acabo o no
     */
    private void revisarJuegoTerminado() {
        if (!Util.arregloContiene(this.cadenaOculta, '_') && this.numeroIntentosActuales < numeroMaximoIntentos) {
            manejadorClientes.setStatusUltimoJuego("<YOU_WIN>");
            juegoActivo=false;
            return;
        } else if (numeroIntentosActuales > numeroMaximoIntentos) {
            manejadorClientes.setStatusUltimoJuego("<YOU_LOSE>");
            juegoActivo=false;
            return;
        }

    }

    private String obtenerPalabra() {
        try {
            BufferedReader reader=new BufferedReader(new FileReader(nombreArchivo));
            String linea=reader.readLine();
            int random=(int)(Math.random()*15);
            String anterior="";
            int contador=0;
            while(linea!=null && contador!=random){
                anterior=linea;
                linea=reader.readLine();
                contador++;
            }
            return anterior;
        } catch (IOException ex) {
            Logger.getLogger(AdministradorAhorcado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
