package servidor.ayudante;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import juego.ahorcado.AdministradorAhorcado;
import juego.gato.AdministradorGato;
import servidor.Servidor;
import util.RWSocket;

/**
 *
 * @author Alberto Emmanuel Esquivel Vega
 *
 * Clase que se encarga de realizar las tareas de interaccion entre cliente y
 * servidor
 */
public class ManejadorCliente implements Runnable {

    /**
     * Id del Ayudante
     */
    private int id;
    /**
     * Nombre del Ayudante
     */
    private String nombre;
    /**
     * Socket por el cual se escucha al cliente
     */
    private Socket socket;
    /**
     * Variable que nos dice si el cliente sigue conectado
     */
    private boolean online;
    /**
     * Numero de instrucciones que ha ejecutado el cliente actualmente
     */
    private int numeroInstruccionesActuales;
    /**
     * variable que guarda el resultado del ultimo juego
     */
    private String statusUltimoJuego;
    
    /**
     * Constructor
     * @param id
     * @param nombre
     * @param socket 
     */
    public ManejadorCliente(int id,String nombre, Socket socket) {
        this.statusUltimoJuego="<FAIL 400>";
        this.id=id;
        this.nombre = nombre;
        this.socket = socket;
        this.online = true;
        this.numeroInstruccionesActuales = 0;
    }

    @Override
    public void run() {
        System.out.println("el thread[" + nombre + "] atiende a " + socket.getInetAddress().getHostName());
        atender();
        reducirContador();
    }

    /**
     * metodo que se encarga de atender al cliente mientras este no presione la
     * 
     */
    private void atender() {
        String mensajeBienvenida = "Bienvenido , mi nombre es " + nombre + " Que puedo hacer por ti ? \n ";
        enviarMensaje(mensajeBienvenida);
        while (online) {
            String mensajeRecibido = leerMensaje();
            if (mensajeRecibido == null) {
                online = false;
            }
            if (online && mensajeRecibido != null) {
                generarRespuesta(mensajeRecibido);
            }
        }
    }

    /**
     * una vez que se termina de atender al cliente reduce el contador del
     * numero de conexiones disponibles por el servidor
     */
    private synchronized void reducirContador() {
        Servidor.sesionesActuales--;
        System.out.println("el thread[" + nombre + "] libero a un cliente ");
    }

    /**
     * envia un mensaje por medio del socket
     *
     * @param mensaje
     */
    public void enviarMensaje(String mensaje) {
        try {
            RWSocket.enviarMensaje(socket, mensaje);
        } catch (SocketException soex) {
            online = false;
            System.out.println("el cliente se ha desconectado");
        } catch (IOException ex) {
            online = false;
        }

    }

    /**
     * lee un mensaje por medio del socket
     *
     * @return
     */
    public String leerMensaje() {
        try {
            String lecturaOperacion = RWSocket.leerCanal(socket);
            numeroInstruccionesActuales++;
            return lecturaOperacion;
        } catch (SocketException soex) {
            online = false;
            System.out.println("el cliente se ha desconectado");
        } catch (IOException ex) {
            online = false;
        }
        return null;
    }

    /**
     * genera la respuesta dado el mensaje recibido
     * @param mensaje 
     */
    private void generarRespuesta(String mensaje) {
        if(numeroInstruccionesActuales==1){
            if(mensaje.contains("HELLO")){
                enviarMensaje("<ID "+getId()+">");
                return;
            }else{
                enviarMensaje("<FAIL 100>");
                return;
            }
        }
        if(mensaje.contains("PLAY")){
            administrarJuegos(mensaje);
            return;
        }
        if(mensaje.contains("EXIT")){
            online=false;
            return;
        }
        if(mensaje.contains("LIST GAMES")){
            enviarMensaje("<gato,gato2,ahorcado>");
            return;
        }
        if(mensaje.contains("STATUS GAME")){
            enviarMensaje(this.statusUltimoJuego);
            return;
        }
        enviarMensaje("<FAIL 400>");
    }

    /**
     * en el caso de que el usuario decida iniciar un juego , empezamos a administrar su juego con otro
     * administrador
     * @param mensaje 
     */
    private void administrarJuegos(String mensaje) {
        if(mensaje.contains("ahorcado")){
            AdministradorAhorcado ahorcado=new AdministradorAhorcado(this);
            ahorcado.iniciarJuego();
            return;
        }
        if(mensaje.contains("gato2")){
            AdministradorGato gato=new AdministradorGato(this,false);
            gato.iniciarJuego();
            return;
        }
        if(mensaje.contains("gato")){
            AdministradorGato gato=new AdministradorGato(this, true);
            gato.iniciarJuego();
            return;
        }
    }

    /**
     * @return the online
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * @param online the online to set
     */
    public void setOnline(boolean online) {
        this.online = online;
    }

    /**
     * @return the statusUltimoJuego
     */
    public String getStatusUltimoJuego() {
        return statusUltimoJuego;
    }

    /**
     * @param statusUltimoJuego the statusUltimoJuego to set
     */
    public void setStatusUltimoJuego(String statusUltimoJuego) {
        this.statusUltimoJuego = statusUltimoJuego;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

}
