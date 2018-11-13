import static java.lang.Integer.parseInt;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.stage.*;

//01
public class RegistroDiGioco extends Application {
    //lista predefinita utilizzata da un elemento drop-down nell'interfaccia
    private final ObservableList<String> ItemCategoryList = FXCollections.observableArrayList("Helm", "Shoulders", "Shield", "Weapon", "Ring");

    private RankTable playerTable;
    private PieChart pieGraph;
    
    private TextField nameTf ,itemLevelTf,levelRequiredTf, healthPowertf ,attackPowertf, defenceRatingtf, criticalStriketf, usernameTf;

    private Button createItemBt, playBt;
    
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
    
    private Controller controller;
    
    //02
    public void start(Stage primaryStage) {
        
        //classe middleware per RegistroDiGioco
        controller = new Controller();
        //Tabella per la lista giocatori
        playerTable= new RankTable();
        //Metodo per l'aggiornamento della tabella tramite controller
        controller.updateTableContent(playerTable);
        //grafico per la visualizzazione delle partite dei giocatori, dati presi tramite controller
        pieGraph = new PieChart(controller.getChartData());
        //semplice linea
        separator = new Separator();
        //due stringhe standard per lo stile dei label in formato normale e titolo
        labelParams = "-fx-font-size: "+controller.configurazione.confStyle.dimFontNormal+"; -fx-font-weight: "+controller.configurazione.confStyle.fontWeight+";-fx-font-family: "+controller.configurazione.confStyle.font+";";
        titleParams = "-fx-font-size: "+controller.configurazione.confStyle.dimFontLabel+"; -fx-font-weight: "+controller.configurazione.confStyle.fontWeight+";-fx-font-family: "+controller.configurazione.confStyle.font+";";
     
        //Titoli per Grafico e Tabella
        graphTitle = new Label("Number of Matches from TOP-"+controller.configurazione.confNum.num+" Players:");
        rankTableTitle = new Label("TOP-"+controller.configurazione.confNum.num+" Player List:");

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

        createItemBt.setOnAction((ActionEvent e) -> { if(controller.sendDatabaseItem(nameTf.getText(), itemCategoryCb.getValue() , parseInt(itemLevelTf.getText()), parseInt(levelRequiredTf.getText())) ==1){ clearTf();} });
        
        playBt.setOnAction((ActionEvent e) -> { controller.sendServerRequest(usernameTf.getText(), healthPowertf, attackPowertf, defenceRatingtf, criticalStriketf);});
        
        setLayoutParams();

        Group itemBox = new Group(itemLabel, nameTf, itemCategoryCb, itemLevelTf, levelRequiredTf, separator, createItemBt,
                                    userStatLabel, healthPowerLabel, attackPowerLabel, defenceRatingLabel, criticalStrikeLabel,
                                    healthPowertf, attackPowertf, defenceRatingtf, criticalStriketf, usernameTf, playBt, playerTable, pieGraph, graphTitle, rankTableTitle);

       Scene mainScene = new Scene(itemBox, 1200, 780, Color.DARKGRAY);
       primaryStage.setTitle("Registro di Gioco: MAGMA");
       primaryStage.setScene(mainScene);
       primaryStage.show();
       
       //caricamento cache binarya
       controller.getCacheBinary(usernameTf, nameTf, itemLevelTf, levelRequiredTf);
       //salvataggio cache binaria alla chiusura
       primaryStage.setOnCloseRequest(ev ->{controller.saveCacheBinary(usernameTf, nameTf, itemLevelTf, levelRequiredTf); });
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    //03
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
        
        pieGraph.setLayoutX(0); pieGraph.setLayoutY(0);
        pieGraph.setLegendSide(Side.RIGHT);
        
        graphTitle.setLayoutX(40);graphTitle.setLayoutY(10); 
        graphTitle.setStyle(titleParams);
        
        rankTableTitle.setLayoutX(550); rankTableTitle.setLayoutY(10);
        rankTableTitle.setStyle(titleParams);
        
       
    }
    
    //04
    private void clearTf(){
               nameTf.clear();
               itemCategoryCb.setValue("Category");
               itemLevelTf.clear();
               levelRequiredTf.clear();
               System.out.println("Item Correctly added to Database");
           }

}
   
/*
Note: 
    [01]: Classe principale dell'applicativo, vengono istanziate tutte le variabili
          necessarie per l'uso delle classi da usare e delle liste osservabili,
          anche oggetti TextField e Bottoni sono inizializzati all'inizio.
    [02]: Funziona Start che istanzia TextField, bottoni, la tabella e il pieGraph.
          Vengono avviate Scene e Stage incorporando tutti gli elementi di interfaccia.

    [03]: funzione invocata dopo l'instanziamento di tutti gli elementi grafici,
          raccoglie le istruzioni per settare correttamente le posizioni e caratteristiche
          grafiche degli elementi d'interfaccia.
    [04]: Semplice metodo che pulisce il value dei TextField utilizzati per l'invio
          di un item nel Database.
*/

