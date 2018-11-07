import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

public class RankTable extends TableView<Player> {
    private ObservableList<Player> list;
    
    public RankTable(){
    
        
        TableColumn nameColumn = new TableColumn("Username");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setMinWidth(120);
        
        TableColumn factionColumn = new TableColumn("Faction");
        factionColumn.setCellFactory(new PropertyValueFactory<>("faction"));
        factionColumn.setMinWidth(120);
        
        TableColumn classColumn = new TableColumn("Class");
        classColumn.setCellFactory(new PropertyValueFactory<>("class"));
        classColumn.setMinWidth(120);
        
        TableColumn levelColumn = new TableColumn("Level");
        levelColumn.setCellFactory(new PropertyValueFactory<>("level"));
        levelColumn.setMinWidth(120);
        
        TableColumn ratingColumn = new TableColumn("Rating");
        ratingColumn.setCellFactory(new PropertyValueFactory<>("rating"));
        ratingColumn.setMinWidth(120);
        
        list = FXCollections.observableArrayList();
        setItems(list);
        getColumns().addAll(nameColumn, factionColumn, classColumn, levelColumn, ratingColumn);
        
        lookup(".scoll-bar:vertical");
    }
    
    public void updateTableList(ObservableList<Player> newList){
        list.clear();
        list.addAll(newList);
    }
}
