package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import servidor.ayudante.AyudanteServidor;
/**
 * 
 * @author Alberto Emmanuel Esquivel Vega
 * 
 * Clase que se encarga de ser servidor de Juegos Gato y Ahorcado
 */
public class Servidor {

    /**
     * socket servidor
     */
    private ServerSocket socket;
    /**
     * puerto por el cual escucha el servidor
     */
    private int puerto ;
    /**
     * numero de sesiones maximas simultaneas
     */
    private final int sesionesMaximas = 50;
    /**
     * numero de sesiones que estan actualmente conectados
     */
    public static int sesionesActuales;
    /**
     * el mapeo de todos los clientes que van entrando a el servidor
     */
    private Map<Integer,AyudanteServidor> mapeoClientes;

    /**
     * Constructor
     * @param puerto 
     */
    public Servidor(int puerto) {
        this.puerto=puerto;
        sesionesActuales = 0;
        mapeoClientes=new HashMap<Integer, AyudanteServidor>();
        crearSocket();
        System.out.println("Servidor comienza a escuchar...");
    }

    /**
     * crea el socket servidor en el puerto mencionado
     */
    private void crearSocket() {
        try {
            socket=new ServerSocket(puerto);
        } catch (IOException ex) {
            System.out.println("[Serv]Error al abrir el socket servidor");
        }
    }

    /**
     * metodo que se encarga de escuchar el canal y distribuir el trabajo a los threads
     */
    public void iniciarServicio() {
        int idCliente=1;
        while (sesionesActuales < sesionesMaximas) {
            Socket receptor = escucharCanal();
            sesionesActuales++;
            AyudanteServidor ayudante=new AyudanteServidor(idCliente++,"Thread-"+(sesionesActuales),receptor);
            mapeoClientes.put(idCliente,ayudante);
            Thread th = new Thread(ayudante);
            th.start();
        }
    }

    /**
     * metodo que hace que el socket servidor escuche el canal
     * @return 
     */
    private Socket escucharCanal() {
        Socket receptor = null;
        try {
            receptor = socket.accept();
        } catch (IOException ex) {
            System.out.println("[Serv]Error al escuchar");
        }
        return receptor;
    }

    public static void main(String[] args) {
        Servidor server = new Servidor(9999);
        server.iniciarServicio();
    }

    

    /**
     * regresa las sesiones que actualmente atiende el servidor
     * @return 
     */
    public int getSesionesActuales() {
        return sesionesActuales;
    }

    /**
     * pone las secciones que estaran actualmente en el servidor
     * @param sesionesActuales 
     */
    public void setSesionesActuales(int sesionesActuales) {
        this.sesionesActuales = sesionesActuales;
    }

}
