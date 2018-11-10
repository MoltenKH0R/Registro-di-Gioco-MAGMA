import java.io.*;
import static java.lang.Integer.parseInt;
import java.net.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

//01
public class RegistroDiGioco extends Application {
    private ObservableList<Player> playerList;
    private final ObservableList<String> ItemCategoryList = FXCollections.observableArrayList("Helm", "Shoulders", "Shield", "Weapon", "Ring");
    private ConfigurazioneXML configurazione;
    private CacheBinaria cache;
    private DbManager db;
    private RankTable playerTable;
    private PieChart grafico;
    
    private TextField nameTf ,itemLevelTf,levelRequiredTf, healthPowertf ,attackPowertf, defenceRatingtf, criticalStriketf, usernameTf;

    private Button createItemBt, playBt;

    private XmlHandler xmlh;
    
    private String labelParams, titleParams;

    private Label graphTitle, rankTableTitle;

    ComboBox<String> itemCategoryCb;
    
    private Separator separator;
    
    final Label itemLabel = new Label("ITEM:");
    final Label userStatLabel = new Label("USER STATISTICS:");
    final Label healthPowerLabel = new Label("Health Power:");
    final Label attackPowerLabel = new Label("Attack Power:");
    final Label defenceRatingLabel = new Label("Defence Rating:");
    final Label criticalStrikeLabel = new Label("Critical Strike:");
    
    private Logger logger = new Logger();
    
    //02
    public void start(Stage primaryStage) {
        
        //Instanziamento della classe XmlHandler per la gestione file XML e XSD
        xmlh = new XmlHandler();
        
        //creazione della classe configurazione tramite l'esecuzione del parsing da XML
        configurazione = xmlh.executeXmlDeserialize();
        
        //Instanziamento del database Manager
        db = new DbManager(configurazione.confDB.ip, configurazione.confDB.port); 
       
        //Titoli per Grafico e Tabella
        graphTitle = new Label("Number of Matches from TOP"+configurazione.confNum.num+" Players:");
        rankTableTitle = new Label("TOP"+configurazione.confNum.num+" Player List:");
        
        //TableView instanziata e funzione update per il caricamento dei dati dal database
        playerTable = new RankTable();
        playerTable.updateTableList(db.caricaListaPlayer(configurazione.confDate.date, configurazione.confNum.num));
        
        //Instanziamento del grafico, caricamento immediato dei dati dal database manager
        grafico = new PieChart(db.caricaPlayerStats(configurazione.confDate.date, configurazione.confNum.num));

        //Semplice linea per separare la parte alta e bassa dell'interfaccia
        separator = new Separator();
        
        //Salvataggio locale della configurazione di stile dei titoli e testi normali
        labelParams = "-fx-font-size: "+configurazione.confStyle.dimFontNormal+"; -fx-font-weight: "+configurazione.confStyle.fontWeight+";-fx-font-family: "+configurazione.confStyle.font+";";
        titleParams = "-fx-font-size: "+configurazione.confStyle.dimFontLabel+"; -fx-font-weight: "+configurazione.confStyle.fontWeight+";-fx-font-family: "+configurazione.confStyle.font+";";

        //------------------------------ITEM SECTION--------------------------//
        
        //Elementi di interfaccia per la sezione invio Item
        nameTf = new TextField();

        itemCategoryCb = new ComboBox(ItemCategoryList);

        itemLevelTf = new TextField();

        levelRequiredTf = new TextField();

        createItemBt = new Button("Create Item");

        //------------------------CENTRAL INTERFACE ELEMENTS------------------//
        //labels are initialized as final variables in main class
        
        //elementi di interfaccia per la visualizzazione dei risultati di partita
        healthPowertf = new TextField();

        attackPowertf = new TextField();
        
        defenceRatingtf = new TextField();

        criticalStriketf = new TextField();

        //---------------------------RIGHT INPUT INTERFACE--------------------//
        
        //Elementi di interfaccia per l'invio di una richiesta al server di partita
        usernameTf = new TextField();

        playBt = new Button("Play");

        createItemBt.setOnAction((ActionEvent e) -> { logger.createLog(); sendDatabaseItem(); });
        
        playBt.setOnAction((ActionEvent e) -> { logger.playLog(); sendServerRequest();});
        
        setLayoutParams();

        Group itemBox = new Group(itemLabel, nameTf, itemCategoryCb, itemLevelTf, levelRequiredTf, separator, createItemBt,
                                    userStatLabel, healthPowerLabel, attackPowerLabel, defenceRatingLabel, criticalStrikeLabel,
                                    healthPowertf, attackPowertf, defenceRatingtf, criticalStriketf, usernameTf, playBt, playerTable, grafico, graphTitle, rankTableTitle);

       Scene mainScene = new Scene(itemBox, 1200, 780, Color.DARKGRAY);
       primaryStage.setTitle("Registro di Gioco: MAGMA");
       primaryStage.setScene(mainScene);
       primaryStage.show();
       
       prelevaCacheBinaria();

       primaryStage.setOnCloseRequest(ev ->{salvaCacheBinaria(); logger.closeLog();});
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    //04
    private void prelevaCacheBinaria(){
        try(
            FileInputStream leggiFile = new FileInputStream("./Cache.bin");
            ObjectInputStream oggettoLetto = new ObjectInputStream(leggiFile);){
            cache = (CacheBinaria) oggettoLetto.readObject();
            usernameTf.setText(cache.username);
            nameTf.setText(cache.itemName);
            itemLevelTf.setText(cache.itemItemLevel);
            levelRequiredTf.setText(cache.itemLevelRequired);
            logger.cacheLoadLog();
        }catch(IOException ioe){System.out.println("ERRORE: "+ ioe.getMessage());}
         catch(ClassNotFoundException cnfe){System.out.println("ERRORE: "+ cnfe.getMessage());}       
    }
    
    //05
    private void salvaCacheBinaria(){
        try(FileOutputStream scriviFile = new FileOutputStream("./Cache.bin");
            ObjectOutputStream oggettoScritto = new ObjectOutputStream(scriviFile);){
            cache = new CacheBinaria(usernameTf.getText(), nameTf.getText(), itemLevelTf.getText(), levelRequiredTf.getText());
            oggettoScritto.writeObject(cache);
            logger.cacheSaveLog();
        }catch(IOException ioe){ System.out.println(ioe.getMessage());}
    }

    //06
    private void setLayoutParams(){
      
        itemLabel.setPrefWidth(300);
        itemLabel.setLayoutX(175);itemLabel.setLayoutY(485);
        itemLabel.setStyle(titleParams);
        
        nameTf.setPromptText("Name");
        nameTf.setPrefWidth(300);
        nameTf.setLayoutX(50);nameTf.setLayoutY(555);
    
        itemCategoryCb.setPromptText("Category");
        itemCategoryCb.setPrefWidth(300);
        itemCategoryCb.setLayoutX(50);itemCategoryCb.setLayoutY(590);
        
        createItemBt.setPrefWidth(200);
        createItemBt.setLayoutX(100);createItemBt.setLayoutY(710);
        
        levelRequiredTf.setPromptText("Level Required");
        levelRequiredTf.setPrefWidth(300);
        levelRequiredTf.setLayoutX(50);levelRequiredTf.setLayoutY(660);
        
        itemLevelTf.setPromptText("Item Level");
        itemLevelTf.setPrefWidth(300);
        itemLevelTf.setLayoutX(50);itemLevelTf.setLayoutY(625);

        userStatLabel.setPrefWidth(300);
        userStatLabel.setLayoutX(520);userStatLabel.setLayoutY(485);
        userStatLabel.setStyle(titleParams);
        
        healthPowerLabel.setLayoutX(450);healthPowerLabel.setLayoutY(555);
        healthPowerLabel.setStyle(labelParams);

        attackPowerLabel.setLayoutX(450);attackPowerLabel.setLayoutY(590);
        attackPowerLabel.setStyle(labelParams);

        defenceRatingLabel.setLayoutX(450);defenceRatingLabel.setLayoutY(625);
        defenceRatingLabel.setStyle(labelParams);

        criticalStrikeLabel.setLayoutX(450);criticalStrikeLabel.setLayoutY(660);
        criticalStrikeLabel.setStyle(labelParams);
 
        healthPowertf.setEditable(false);
        healthPowertf.setPrefWidth(150);
        healthPowertf.setLayoutX(600);healthPowertf.setLayoutY(555);

        attackPowertf.setEditable(false);
        attackPowertf.setPrefWidth(150);
        attackPowertf.setLayoutX(600);attackPowertf.setLayoutY(590);

        defenceRatingtf.setEditable(false);
        defenceRatingtf.setPrefWidth(150);
        defenceRatingtf.setLayoutX(600);defenceRatingtf.setLayoutY(625);

        criticalStriketf.setEditable(false);
        criticalStriketf.setPrefWidth(150);
        criticalStriketf.setLayoutX(600);criticalStriketf.setLayoutY(660);
        
        usernameTf.setPromptText("Username");
        usernameTf.setPrefWidth(300);
        usernameTf.setLayoutX(850);usernameTf.setLayoutY(555);
        
        playBt.setPrefWidth(100);
        playBt.setLayoutX(950);playBt.setLayoutY(605);
        
        separator.setPrefWidth(1150);
        separator.setLayoutX(25);separator.setLayoutY(450);
        
        grafico.setLayoutX(0); grafico.setLayoutY(0);
        grafico.setLegendSide(Side.RIGHT);
        
        graphTitle.setLayoutX(40);graphTitle.setLayoutY(10); 
        graphTitle.setStyle(titleParams);
        
        rankTableTitle.setLayoutX(550); rankTableTitle.setLayoutY(10);
        rankTableTitle.setStyle(titleParams);
        
       
    }
    
    //07
    private void sendServerRequest(){
        try(Socket s = new Socket(configurazione.confServer.ip, configurazione.confServer.port);
            ObjectOutputStream oout = new ObjectOutputStream (s.getOutputStream());
            ){
                oout.writeObject(usernameTf.getText());    
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
    
    //03
    private void sendDatabaseItem(){
         Item oggetto = new Item(nameTf.getText(), itemCategoryCb.getValue() , parseInt(itemLevelTf.getText()), parseInt(levelRequiredTf.getText()));
           if ( db.inviaItem(oggetto) == 1){
               nameTf.clear();
               itemCategoryCb.setValue("Category");
               itemLevelTf.clear();
               levelRequiredTf.clear();
               System.out.println("Item Correctly added to Database");
           }
    }
    
    //08
    class Logger{
       public Logger() {System.out.println("Logger initialized...");}
       
       public void playLog(){
           System.out.println("Play Button triggered...");
           System.out.println("["+usernameTf.getText()+"] value sent at server on "+configurazione.confServer.ip+" address & "+configurazione.confServer.port+" port.");
       }
       public void createLog(){
           System.out.println("CreateItem Button triggered...");
           System.out.println("["+nameTf.getText()+"] item sent to "+configurazione.confDB.ip+" address & "+configurazione.confDB.port+" port.");
       }
       
       public void cacheLoadLog(){
           System.out.println("Cache Successfully loaded from file bin...");
       }
       public void cacheSaveLog(){
           System.out.println("Cache successfully saved.");
       }
       public void closeLog(){
           System.out.println("Closing Request.");
       }
    }
}
   
/*
Note: 
    [01]: Classe principale dell'applicativo, vengono istanziate tutte le variabili
          necessarie per l'uso delle classi da usare e delle liste osservabili,
          anche oggetti TextField e Bottoni sono inizializzati all'inizio.
    [02]: Funziona Start che istanzia TextField, bottoni, la tabella e il grafico.
          Vengono avviate Scene e Stage incorporando tutti gli elementi di interfaccia.
    [03]: Funzione impostata sul bottone createItemBt che invia i valori dei TextField
          di interesse Item alla funzione della classe DbManager che elabora una
          query di tipo INSERT. Se la query va a buon fine i TextField sono clearati.
    [04]: Funzione invocata all'avvio che preleva da un file .bin informazioni 
          pari al contenuto di vari TextField lasciati prima dell'ultima chiusura
          dell'applicativo. I valori sono inseriti nei rispettivi TextField.
    [05]: Funzione invocata durante la chiusura dell'applicativo. Preleva i valori
          dei TextField nella sezione Item e username per poi salvarli su un file 
          di tipo .bin
    [06]: funzione invocata dopo l'instanziamento di tutti gli elementi grafici,
          raccoglie le istruzioni per settare correttamente le posizioni e caratteristiche
          grafiche degli elementi d'interfaccia.
    [07]: Funzione impostata sul bottone PlayBt che effettua una connessione socket
          ad un server con ip e porta presa dalla configurazione, invia il valore
          del TextField usernameTf e attende la risposta dal server, impostanto
          il valore dei TextField nella sezione Player Stats con i risultati
          ricevuti.
    [08]: semplice classe Logger che comprende metodi per la stampa di messaggi 
          nel commandLine per tenere traccia delle operazioni effettuate nell'applicativo
          e del corretto svolgimento dei metodi.
*/

