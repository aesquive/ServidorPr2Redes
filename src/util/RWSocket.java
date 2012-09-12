package util;

import java.io.*;
import java.net.Socket;

/**
 *
 * @author Alberto Emmanuel Esquivel Vega
*   RWSocket=Read and Write Socket
* Clase que se encarga de escribir y leer la informacion en el canal de un socket , asi como cerrar un socket
*/
public class RWSocket {
 
    private static String caracterFinal="/&";
    
    /**
     * Lee la cadena de texto que esta en el canal que se escucha
     * @return 
     */
    public static String leerCanal(Socket socket) throws IOException{
        StringBuilder builder=new StringBuilder();
            InputStream inputStream = socket.getInputStream();
            DataInputStream dis=new DataInputStream(inputStream);
            String cadena=dis.readLine();
            while(cadena!=null && !cadena.contains(caracterFinal)){
                builder.append(cadena+"\n");
                cadena=dis.readLine();
            }
        return builder.toString();
    }
    
    
    /**
     * Envia un mensaje el socket por el canal
     * @param mensaje 
     */
    public static void enviarMensaje(Socket socket,String mensaje) throws IOException {
            OutputStream outputStream = socket.getOutputStream();
            PrintStream dos=new PrintStream(outputStream);
            dos.println(mensaje+"\n"+caracterFinal);
    }

    /**
     * cierra el socket que se pasa como parametro
     * @param socket 
     */
    public static void cerrarSocket(Socket socket) throws IOException {
            socket.close();
     }
    
    
}
