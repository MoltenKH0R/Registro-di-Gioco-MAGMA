import java.sql.*;
import javafx.collections.*;
import javafx.scene.chart.*;

//01
public class DbManager {
    private static Connection connessioneDB;
    private static ObservableList<Player> playerList;
    private static ObservableList<Item> itemList;

    public DbManager(String ip, int port){
        try{
            connessioneDB = DriverManager.getConnection("jdbc:mysql://"+ip+":"+port+"/magmadb", "root", "");
        }
        catch(SQLException e){System.err.println(e.getMessage());}
    }
    
    //02
    public ObservableList<Player> caricaListaPlayer(String date, int num){
        playerList = FXCollections.observableArrayList();
        try(
            PreparedStatement ps = connessioneDB.prepareStatement("SELECT usernameID, faction, class, level, rating, matches from playertab WHERE lastPlayed >= '"+date+"' ORDER BY rating DESC LIMIT "+num+";");
            )
        {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                playerList.add(new Player(rs.getString("usernameID"), rs.getString("faction"), rs.getString("class"), rs.getInt("level"), rs.getInt("rating"), rs.getInt("matches")));
            }
        }catch(SQLException e) {System.err.println("Errore SQL playerList: "+e.getMessage());}
        
        return playerList;
    }
    
    //03
    public ObservableList<PieChart.Data> caricaPlayerStats(String date, int num){
        ObservableList<PieChart.Data> topPlayedData = FXCollections.observableArrayList();
        try(
            PreparedStatement ps = connessioneDB.prepareStatement("SELECT usernameID, matches FROM playertab WHERE lastPlayed >= '"+date+"' ORDER BY rating DESC LIMIT "+num+";");
            )
        {
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                topPlayedData.add(new PieChart.Data(rs.getString("usernameID"), rs.getInt("matches")));
            }
        }catch(SQLException e) {System.err.println("Errore SQL playerStat: "+e.getMessage());}
        
        return topPlayedData;
    } 
    
    //04
    public int inviaItem(Item itemToSend){
        try(
            PreparedStatement ps = connessioneDB.prepareStatement("INSERT INTO itemtab (name, category, itemLevel, levelRequired) VALUES (?,?,?,?);");
            ){
                ps.setString(1, itemToSend.getName());
                ps.setString(2, itemToSend.getCategory());
                ps.setInt(3, itemToSend.getItemLevel());
                ps.setInt(4, itemToSend.getLevelRequired());
                return  ps.executeUpdate();
            }catch(SQLException e) {System.err.println(e.getMessage());}
        return 0;
    }
}

/*
Note:
    [01] Classe per la gestione della connessione e ricezione/invio dei dati
         dal/al database
    [02] Funzione per il caricamento dei migliori X(num) Players in ordine di punteggio,
         che hanno giocato l'ultima partita recentemente rispetto a (date), la lista verrà inserita
         nella tabella di classe RankTable.
    [03] Funzione per il caricamento dei migliori X(num) Players come [02], prelevando 
         solo username e numero di partite giocate. I dati sono inseriti in una lista osservabile
         per essere visualizzati in un grafico.
    [04] Funzione per l'invio delle informazioni su un Item nel database.

*/