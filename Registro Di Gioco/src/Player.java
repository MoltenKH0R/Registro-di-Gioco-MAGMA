
import javafx.beans.property.*;


//01
public class Player {
    final private SimpleStringProperty name;
    final private SimpleStringProperty faction;
    final private SimpleStringProperty classe;
    final private SimpleIntegerProperty level;
    final private SimpleIntegerProperty rating;
    final private SimpleIntegerProperty played;
   
    public Player(String name, String faction, String classe, int level, int rating, int played){
        this.name = new SimpleStringProperty(name);
        this.faction = new SimpleStringProperty(faction);
        this.classe = new SimpleStringProperty(classe);
        this.level = new SimpleIntegerProperty(level);
        this.rating = new SimpleIntegerProperty(rating);
        this.played = new SimpleIntegerProperty(played);
    }
    
    public String getName() {return name.get();}
    public String getFaction() {return faction.get();}
    public String getClasse() {return classe.get();}
    public int getLevel() {return level.get();}
    public int getRating() {return rating.get();}
    public int getPlayed() {return played.get();}
    
    public void setName(String name) {this.name.set(name);}
    public void setFaction(String faction) {this.faction.set(faction);}
    public void setClasse(String classe) {this.classe.set(classe);}
    public void setLevel(int level) {this.level.set(level);}
    public void setRating(int rating) {this.rating.set(rating);}
    public void setPlayed(int played) {this.played.set(played);}
    
}

/*
Note:
    [01] Classe bean con la quale si costruisce la lista osservabile nella 
         classe RankTable e PieChart
*/