
import java.io.*;


//01
public class CacheBinaria implements Serializable {
    public String username;
    public String itemName;
    public int itemItemLevel;
    public int itemLevelRequired;
    
    public CacheBinaria(String u, String in, int il, int lr){
        username = u;
        itemName = in;
        itemItemLevel = il;
        itemLevelRequired = lr;
    }
}

/*
Note:
    [01] La classe implementa l'interfaccia Serializable che ne permette la 
         serializzazione per l'inserimento dei dati su un file in formato .bin
*/