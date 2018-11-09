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
    
    private TextField nameTf;
    private TextField itemLevelTf;
    private TextField levelRequiredTf;
    
    private TextField healthPowertf;
    private TextField attackPowertf;
    private TextField defenceRatingtf;
    private TextField criticalStriketf;
    
    private TextField usernameTf;
    
    private Button createItemBt;
    private Button playBt;
    
    private XmlHandler xmlh;
    
    private String labelParams;
    private String titleParams;
    
    ComboBox<String> itemCategoryCb;
    
    
    private Separator separator;
    final Label itemLabel = new Label("ITEM:");
    final Label userStatLabel = new Label("USER STATISTICS:");
    final Label healthPowerLabel = new Label("Health Power:");
    final Label attackPowerLabel = new Label("Attack Power:");
    final Label defenceRatingLabel = new Label("Defence Rating:");
    final Label criticalStrikeLabel = new Label("Critical Strike:");
    
    //02
    public void start(Stage primaryStage) {
 
        xmlh = new XmlHandler();
        configurazione = xmlh.executeXmlDeserialize();
       
        db = new DbManager(configurazione.confDB.ip, configurazione.confDB.port); 
       
        playerTable = new RankTable();
        playerTable.updateTableList(db.caricaListaPlayer(configurazione.confDate.date, configurazione.confNum.num));
       
       db = new DbManager(configurazione.confDB.ip, configurazione.confDB.port); 
       
       playerTable = new RankTable();
       playerTable.updateTableList(db.caricaListaPlayer(configurazione.confDate.date, configurazione.confNum.num));
        
        grafico = new PieChart(db.caricaPlayerStats(configurazione.confDate.date, configurazione.confNum.num));
        grafico.setLayoutX(0); grafico.setLayoutY(0);
        grafico.setLegendSide(Side.RIGHT);
        
      
        
        
       
       
        //simple separator line
        Separator separator = new Separator();
        separator.setPrefWidth(1150);
        separator.setLayoutX(25);separator.setLayoutY(450);
        


        //do stuff
        //
        //Left interface section
        //
        
        grafico = new PieChart(db.caricaPlayerStats(configurazione.confDate.date, configurazione.confNum.num));
        grafico.setLayoutX(0); grafico.setLayoutY(0);
        grafico.setLegendSide(Side.RIGHT);
        
        labelParams = "-fx-font-size: "+configurazione.confStyle.dimFontNormal+"; -fx-font-weight: "+configurazione.confStyle.fontWeight+";-fx-font-family: "+configurazione.confStyle.font+";";
        titleParams = "-fx-font-size: "+configurazione.confStyle.dimFontLabel+"; -fx-font-weight: "+configurazione.confStyle.fontWeight+";-fx-font-family: "+configurazione.confStyle.font+";";
        
        //simple separator line
        separator = new Separator();

        //------------------------------ITEM SECTION--------------------------//
        //Item Name Textfield input
        nameTf = new TextField();
        
        //DropDown ComboBox Input for category selection, items loaded from the observableList 
        itemCategoryCb = new ComboBox(ItemCategoryList);

        //Item Level textfield input
        itemLevelTf = new TextField();
 
        //LevelRequired textfield input
        levelRequiredTf = new TextField();

        //button for item creation submit
        createItemBt = new Button("Create Item");

        //
        //------------------------CENTRAL INTERFACE ELEMENTS------------------//
        //labels are initialized as final variables in main class
        
        //healthPower numerical section
        healthPowertf = new TextField();
         //AttackPower numerical section
        attackPowertf = new TextField();
        //DefenceRating numerical section
        defenceRatingtf = new TextField();
        //CriticalStrike numerical section
        criticalStriketf = new TextField();
       
        //
        //---------------------------RIGHT INPUT INTERFACE--------------------//
        //
        
        //Username Input Textfield
        usernameTf = new TextField();
        //Play Button
        playBt = new Button("Play");

        
        //03

        
        
        

        createItemBt.setOnAction((ActionEvent e) -> { 
            Item oggetto = new Item(nameTf.getText(), itemCategoryCb.getValue() , parseInt(itemLevelTf.getText()), parseInt(levelRequiredTf.getText()));
           if ( db.inviaItem(oggetto) == 1){
               nameTf.clear();
               itemCategoryCb.setValue("Category");
               itemLevelTf.clear();
               levelRequiredTf.clear();
           }
        });

        
        playBt.setOnAction((ActionEvent e) -> {sendServerRequest();});
        
        setLayoutParams();

        Group itemBox = new Group(itemLabel, nameTf, itemCategoryCb, itemLevelTf, levelRequiredTf, separator, createItemBt,
                                    userStatLabel, healthPowerLabel, attackPowerLabel, defenceRatingLabel, criticalStrikeLabel,
                                    healthPowertf, attackPowertf, defenceRatingtf, criticalStriketf, usernameTf, playBt, playerTable, grafico);
       
       

        
       Scene mainScene = new Scene(itemBox, 1200, 780, Color.DARKGRAY);
       primaryStage.setTitle("Registro di Gioco: MAGMA");
       primaryStage.setScene(mainScene);
       primaryStage.show();
       
       prelevaCacheBinaria();

       primaryStage.setOnCloseRequest(ev ->{salvaCacheBinaria();});
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
        }catch(IOException ioe){System.out.println("ERRORE: "+ ioe.getMessage());}
         catch(ClassNotFoundException cnfe){System.out.println("ERRORE: "+ cnfe.getMessage());}       
    }
    
    //05
    private void salvaCacheBinaria(){
        try(FileOutputStream scriviFile = new FileOutputStream("./Cache.bin");
            ObjectOutputStream oggettoScritto = new ObjectOutputStream(scriviFile);){
            cache = new CacheBinaria(usernameTf.getText(), nameTf.getText(), itemLevelTf.getText(), levelRequiredTf.getText());
            oggettoScritto.writeObject(cache);
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
        
       
    }
    
    private void sendServerRequest(){
        try(Socket s = new Socket(configurazione.confServer.ip, configurazione.confServer.port);
                ObjectOutputStream oout = new ObjectOutputStream (s.getOutputStream());
                ){
                    oout.writeObject(usernameTf.getText());
                    System.out.println("server sent");
                    
                   
                try (ObjectInputStream oin = new ObjectInputStream(s.getInputStream())) {
                    String response[] = (String[]) oin.readObject();
                    System.out.println("Server answer: "+response[0]);
                    
                    healthPowertf.setText(response[0]);
                    attackPowertf.setText(response[1]);
                    defenceRatingtf.setText(response[2]);
                    criticalStriketf.setText(response[3]);
                    
                    oout.close();
                    oin.close();
                } 
                    
                }catch(Exception err){System.err.println("Errore invio partita"+err.getMessage());}
    }
   
}
   
/*
Note: 
    [01]: Classe principale dell'applicativo, vengono istanziate tutte le variabili
          necessarie per l'uso delle classi da usare e delle liste osservabili,
          anche oggetti TextField e Bottoni sono inizializzati all'inizio.
    [02]: Funziona Start che istanzia TextField, bottoni, la tabella e il grafico.
          Vengono avviate Scene e Stage incorporando tutti gli elementi di interfaccia.
    [03]: Evento impostato sul bottone createItemBt che invia i valori dei TextField
          di interesse Item alla funzione della classe DbManager che elabora una
          query di tipo INSERT. Se la query va a buon fine i TextField sono clearati.
    [04]: Funzione invocata all'avvio che preleva da un file .bin informazioni 
          pari al contenuto di vari TextField lasciati prima dell'ultima chiusura
          dell'applicativo. I valori sono inseriti nei rispettivi TextField.
    [05]: Funzione invocata durante la chiusura dell'applicativo. Preleva i valori
          dei TextField nella sezione Item e username per poi salvarli su un file 
          di tipo .bin
    [06]: funzione invocata dopo l'istanziamento di tutti gli elementi grafici,
          raccoglie le istruzioni per settare correttamente le posizioni e caratteristiche
          grafiche degli elementi d'interfaccia.
*/

