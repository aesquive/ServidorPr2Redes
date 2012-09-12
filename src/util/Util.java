package util;

/**
 *
 * @author Alberto Emmanuel Esquivel Vega
 * Clase de Utilidades
 */
public class Util {
    
    /**
     * metodo que transforma la cadena de texto en double , en caso de que no se pueda hacer la transformacion 
     * regresa un NaN(Not a Number)
     * @param numero
     * @return 
     */
    public static Double parseDouble(String numero){
        try{
            return Double.parseDouble(numero);
        }
        catch(NumberFormatException num){
            return Double.NaN;
        }
    }

    /**
     * metodo que convierte un arreglo de caracteres en un arreglo
     * @param arreglo
     * @return 
     */
    public static String arrayToString(char[] arreglo) {
        StringBuilder builder=new StringBuilder();
        for(int t=0;t<arreglo.length;t++){
            builder.append(String.valueOf(arreglo[t])+",");
        }
        return builder.toString().substring(0,builder.toString().length()-1);
    }

    /**
     * nos dice si el arreglo de caracteres contiene al elemento
     * @param arreglo
     * @param elemento
     * @return 
     */
    public static boolean arregloContiene(char[] arreglo, char elemento) {
        for(int t=0;t<arreglo.length;t++){
            if(arreglo[t]==elemento){
                return true;
            }
        }
        return false;
    }
    
}
