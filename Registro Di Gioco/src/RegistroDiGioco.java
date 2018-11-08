import com.thoughtworks.xstream.*;
import javafx.scene.paint.*;
import java.io.*;
import static java.lang.Integer.parseInt;
import java.net.*;
import java.nio.file.*;
import java.sql.*;
import java.util.Collections;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import org.w3c.dom.Document;


public class RegistroDiGioco extends Application {
    private ObservableList<Player> playerList;
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
    
    
    
    
    @Override
    public void start(Stage primaryStage) {
       deserializeXML();
       
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
        
        //Item Label
        final Label itemLabel = new Label("ITEM:");
        itemLabel.setPrefWidth(300);
        itemLabel.setLayoutX(175);itemLabel.setLayoutY(485);
        itemLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: Arial;");
        
        
        //Item Name Textfield input
        nameTf = new TextField();
        nameTf.setPromptText("Name");
        nameTf.setPrefWidth(300);
        nameTf.setLayoutX(50);nameTf.setLayoutY(555);
        
        
         //Observabale list of item category for drop-down category selection 
        ObservableList<String> ItemCategoryList = FXCollections.observableArrayList("Helm", "Shoulders", "Shield", "Weapon", "Ring");
        //DropDown ComboBox Input for category selection, items loaded from the observableList 
        ComboBox<String> itemCategoryCb = new ComboBox<String>(ItemCategoryList);
        itemCategoryCb.setPromptText("Category");
        itemCategoryCb.setPrefWidth(300);
        itemCategoryCb.setLayoutX(50);itemCategoryCb.setLayoutY(590);
        
        
        //Item Level textfield input
        itemLevelTf = new TextField();
        itemLevelTf.setPromptText("Item Level");
        itemLevelTf.setPrefWidth(300);
        itemLevelTf.setLayoutX(50);itemLevelTf.setLayoutY(625);
        
        
        //LevelRequired textfield input
        levelRequiredTf = new TextField();
        levelRequiredTf.setPromptText("Level Required");
        levelRequiredTf.setPrefWidth(300);
        levelRequiredTf.setLayoutX(50);levelRequiredTf.setLayoutY(660);
        
        
        //button for item creation submit
        createItemBt = new Button("Create Item");
        createItemBt.setPrefWidth(200);
        createItemBt.setLayoutX(100);createItemBt.setLayoutY(710);
        
   
       //
        //CENTRAL INTERFACE ELEMENTS:
        //
        
        //User Statistics central Label
        final Label userStatLabel = new Label("USER STATISTICS:");
        userStatLabel.setPrefWidth(300);
        userStatLabel.setLayoutX(520);userStatLabel.setLayoutY(485);
        userStatLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        
        //HealthPower label
        final Label healthPowerLabel = new Label("Health Power:");
        healthPowerLabel.setLayoutX(450);healthPowerLabel.setLayoutY(555);
        healthPowerLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        
        
        //AttackPower label
        final Label attackPowerLabel = new Label("Attack Power:");
        attackPowerLabel.setLayoutX(450);attackPowerLabel.setLayoutY(590);
        attackPowerLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        
        
        //DefenceRating label
        final Label defenceRatingLabel = new Label("Defence Rating:");
        defenceRatingLabel.setLayoutX(450);defenceRatingLabel.setLayoutY(625);
        defenceRatingLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        
        
        //CriticalStrike label
        final Label criticalStrikeLabel = new Label("Critical Strike:");
        criticalStrikeLabel.setLayoutX(450);criticalStrikeLabel.setLayoutY(660);
        criticalStrikeLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        
        
        //HealthPower numerical section
        healthPowertf = new TextField();
        healthPowertf.setEditable(false);
        healthPowertf.setPrefWidth(150);
        healthPowertf.setLayoutX(600);healthPowertf.setLayoutY(555);
        
        
        //AttackPower numerical section
        attackPowertf = new TextField();
        attackPowertf.setEditable(false);
        attackPowertf.setPrefWidth(150);
        attackPowertf.setLayoutX(600);attackPowertf.setLayoutY(590);
        
        
        //DefenceRating numerical section
        defenceRatingtf = new TextField();
        defenceRatingtf.setEditable(false);
        defenceRatingtf.setPrefWidth(150);
        defenceRatingtf.setLayoutX(600);defenceRatingtf.setLayoutY(625);
        
        
        //CriticalStrike numerical section
        criticalStriketf = new TextField();
        criticalStriketf.setEditable(false);
        criticalStriketf.setPrefWidth(150);
        criticalStriketf.setLayoutX(600);criticalStriketf.setLayoutY(660);
        
        
        
        
        //
        //RIGHT INPUT INTERFACE
        //
        
        //Username Input Textfield
        usernameTf = new TextField();
        usernameTf.setPromptText("Username");
        usernameTf.setPrefWidth(300);
        usernameTf.setLayoutX(850);usernameTf.setLayoutY(555);
        
        
        //Play Button
        playBt = new Button("Play");
        playBt.setPrefWidth(100);
        playBt.setLayoutX(950);playBt.setLayoutY(605);
        
        
        
        createItemBt.setOnAction((ActionEvent e) -> { 
            Item oggetto = new Item(nameTf.getText(), itemCategoryCb.getValue() , parseInt(itemLevelTf.getText()), parseInt(levelRequiredTf.getText()));
           if ( db.inviaItem(oggetto) == 1){
               nameTf.clear();
               itemCategoryCb.setValue("Category");
               itemLevelTf.clear();
               levelRequiredTf.clear();
           }
        });
       
       
        Group itemBox = new Group(itemLabel, nameTf, itemCategoryCb, itemLevelTf, levelRequiredTf, separator, createItemBt,
                                    userStatLabel, healthPowerLabel, attackPowerLabel, defenceRatingLabel, criticalStrikeLabel,
                                    healthPowertf, attackPowertf, defenceRatingtf, criticalStriketf, usernameTf, playBt, playerTable, grafico);
       
       
        
       Scene mainScene = new Scene(itemBox, 1200, 780);
       primaryStage.setTitle("Registro di Gioco: MAGMA");
       primaryStage.setScene(mainScene);
       primaryStage.show();
       
       prelevaCacheBinaria();
       
       
       
       
       
       primaryStage.setOnCloseRequest(ev ->{salvaCacheBinaria();});
    }

   
    public static void main(String[] args) {
        launch(args);
    }
    
    
    private void deserializeXML(){
        validateXML();
        XStream xs = new XStream();
        try{
            configurazione = (ConfigurazioneXML) xs.fromXML(new String(Files.readAllBytes(Paths.get("./ConfigurazioneXML.xml"))));
        }catch(IOException e){System.err.println("ERRORE deserializzazione: "+ e.getMessage());}
    }
    
    private void validateXML(){
        try{
            DocumentBuilder docb = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Document d = (Document) docb.parse(new File("./ConfigurazioneXML.xml"));
            Schema s = sf.newSchema(new StreamSource(new File("./ConfigurazioneXML.xsd")));
            s.newValidator().validate(new DOMSource(d));
        }catch(Exception e){System.out.println("Errore validazione: "+ e.getMessage());}
    }
    
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
    
    private void salvaCacheBinaria(){
        try(FileOutputStream scriviFile = new FileOutputStream("./Cache.bin");
            ObjectOutputStream oggettoScritto = new ObjectOutputStream(scriviFile);){
            cache = new CacheBinaria(usernameTf.getText(), nameTf.getText(), itemLevelTf.getText(), levelRequiredTf.getText());
            oggettoScritto.writeObject(cache);
        }catch(IOException ioe){ System.out.println(ioe.getMessage());}
    }
    
    private void setupTable(){
       
    
    }
    
 
}


