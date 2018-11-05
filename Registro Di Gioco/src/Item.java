
import javafx.beans.property.*;

//01
public class Item {
   final private SimpleStringProperty name; 
   final private SimpleStringProperty category;
   final private SimpleIntegerProperty itemLevel;
   final private SimpleIntegerProperty levelRequired;
   
   public Item(String name, String category, int itemLevel, int levelRequired){
       this.name = new SimpleStringProperty(name);
       this.category = new SimpleStringProperty(category);
       this.itemLevel = new SimpleIntegerProperty(itemLevel);
       this.levelRequired = new SimpleIntegerProperty(levelRequired);
   }
   
   public String getName() {return name.get();}
   public String getCategory() {return category.get();}
   public int getItemLevel() {return itemLevel.get();}
   public int getLevelRequired() {return levelRequired.get();}
   
   public void setName(String name) {this.name.set(name);}
   public void setCategory(String category) {this.category.set(category);}
   public void setItemLevel(int itemLevel) {this.itemLevel.set(itemLevel);}
   public void setLevelRequired(int  levelRequired) {this.levelRequired.set(levelRequired);}
}

/*
Note:
    [01] Classe bean con la quale si gestisce l'invio dei dati al database

*/