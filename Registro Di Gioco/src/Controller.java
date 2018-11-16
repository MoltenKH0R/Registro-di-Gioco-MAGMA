
import java.io.*;
import java.net.*;
import java.util.List;
import javafx.collections.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;

//01
public class Controller {
    
    public ConfigurazioneXML configurazione;
    private final DbManager db;
    private final XmlHandler xmlh;
    private CacheBinaria cache;
    public List lista;
    public ObservableList<PieChart.Data> chartData;
    public Controller(){
        xmlh = new XmlHandler();
        configurazione = xmlh.executeXmlDeserialize();
        db = new DbManager(configurazione.confDB.ip, configurazione.confDB.port);
        updateTableContent();
        updateChartData();
 
    }
    
    //02
    private void updateTableContent(){
        try{String query = "SELECT usernameID, faction, class, level, rating, matches from playertab WHERE lastPlayed >= '"
                        +configurazione.confDate.date+"' ORDER BY rating DESC LIMIT "
                        +configurazione.confNum.num+";";
        lista = db.loadPlayerList(query);
        }catch(Exception e){
            System.out.println("Impossibile caricare Lista player da Database "+ e.getMessage());
        }
        
    }
    
    //03
    private void updateChartData(){
       try{ String query ="SELECT usernameID, matches FROM playertab WHERE lastPlayed >= '"
                     +configurazione.confDate.date+"' ORDER BY rating DESC LIMIT "
                     +configurazione.confNum.num+";" ;
        chartData = FXCollections.observableArrayList(db.loadPlayerStats(query));
       }catch(Exception e){
           System.out.println("Impossibile caricare dati grafico da Database "+ e.getMessage()); 
       }
    }

    //04
    public int sendDatabaseItem(String name, String category, int itemLevel, int levelRequired){
        String query = "INSERT INTO itemtab (name, category, itemLevel, levelRequired) VALUES ('"+name+"','"+category+"',"+itemLevel+","+levelRequired+");";
       if(db.sendItem(query) == 1){
           return 1;
       }
       return 0;
    }
    
    //05
    public void getCacheBinary(TextField usernameTf,TextField nameTf, TextField itemLevelTf, TextField levelRequiredTf){
        try(
            FileInputStream leggiFile = new FileInputStream("./Cache.bin");
            ObjectInputStream oggettoLetto = new ObjectInputStream(leggiFile);){
            cache = (CacheBinaria) oggettoLetto.readObject();
            usernameTf.setText(cache.username);
            nameTf.setText(cache.itemName);
            itemLevelTf.setText(cache.itemItemLevel);
            levelRequiredTf.setText(cache.itemLevelRequired);
           }catch(IOException ioe){System.out.println("ERRORE caricamento ioe: "+ ioe.getMessage());}
         catch(ClassNotFoundException cnfe){System.out.println("ERRORE caricamento cnfe: "+ cnfe.getMessage());} 

    }
    
    //06
    public void saveCacheBinary(TextField usernameTf, TextField nameTf, TextField itemLevelTf, TextField levelRequiredTf){
         try(FileOutputStream scriviFile = new FileOutputStream("./Cache.bin");
            ObjectOutputStream oggettoScritto = new ObjectOutputStream(scriviFile);){
            cache = new CacheBinaria(usernameTf.getText(), nameTf.getText(), itemLevelTf.getText(), levelRequiredTf.getText());
            oggettoScritto.writeObject(cache);
        }catch(IOException ioe){ System.out.println(ioe.getMessage());}
         
    }
    
    //07
    public void sendServerRequest(String username, TextField healthPowertf,TextField attackPowertf, TextField defenceRatingtf, TextField criticalStriketf){
            try(Socket s = new Socket(configurazione.confServer.ip, configurazione.confServer.port);
            ObjectOutputStream oout = new ObjectOutputStream (s.getOutputStream());
            ){
                oout.writeObject(username);    
                try (ObjectInputStream oin = new ObjectInputStream(s.getInputStream())) {
                    String response[] = (String[]) oin.readObject();
                    System.out.println("Server Responded successfuly...");
                    
                    healthPowertf.setText(response[0]);
                    attackPowertf.setText(response[1]);
                    defenceRatingtf.setText(response[2]);
                    criticalStriketf.setText(response[3]);
                    
                    oout.close();
                    oin.close();
                    System.out.println("Server connection terminated...");
                }         
            }catch(Exception err){System.err.println("Error "+err.getMessage());}
    }
    
}

/*
Note:
    [01]: La classe Controller ha lo scopo di implementare metodi di funzionalità
          per la classe principale
    [02]: Il metodo updateTableContent ha lo scopo di richiedere al database una List<Player>
          e inoltrare i dati ad un metodo specifico della classe RankTable
    [03]: Metodo che invia una query al DbManager per ottenere i dati da mostrare
          poi nel grafico      
    [04]: Il metodo sendDatabaseItem riceve valori dai TextField della classe RegistroDiGioco
          formula una String per la query e invia alla classe DbManager la query.
          ritorna 1 o 0 a seconda del risultato della query.
    [05]: getCacheBinary prende in ingresso i TextField dalla classe principale e
          assegna ad essi i valori contenuti nel file di cache.bin
    [06]: saveCacheBinary preleva i valori dei TextField dalla classe principale
          e li salva su una struttura dati CacheBinaria per poi salvarli sul file
          cache.bin
    [07]: sendServerRequest inoltra al server una stringa e attende la risposta,
          impostando i valori ricevuti nei TextField interessati.

    
*/
