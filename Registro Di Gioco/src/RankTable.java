import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import java.util.*;

public class RankTable extends TableView<Player> {
    private ObservableList<Player> list;
    private DbManager db;
    private ConfigurazioneXML configurazione;
    
    public RankTable(){
        
        TableColumn nameColumn = new TableColumn("Username");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setMinWidth(120);
        
        TableColumn factionColumn = new TableColumn("Faction");
        factionColumn.setCellValueFactory(new PropertyValueFactory<>("faction"));
        factionColumn.setMinWidth(120);
        
        TableColumn classColumn = new TableColumn("Class");
        classColumn.setCellValueFactory(new PropertyValueFactory<>("classe"));
        classColumn.setMinWidth(120);
        
        TableColumn levelColumn = new TableColumn("Level");
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        levelColumn.setMinWidth(120);
        
        TableColumn ratingColumn = new TableColumn("Rating");
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingColumn.setMinWidth(120);
        
        setItems(list);
        
        getColumns().addAll(nameColumn, factionColumn, classColumn, levelColumn, ratingColumn);
        
        lookup(".scoll-bar:vertical");
        
        this.setLayoutX(550);
        this.setLayoutY(40);
    }
    
    public void updateTableList(List<Player> newList){
       ObservableList<Player> list = FXCollections.observableArrayList (newList);
        setItems(list);
    }
}
