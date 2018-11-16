import java.sql.*;
import java.util.List;
import javafx.collections.*;
import javafx.scene.chart.*;

//01
public class DbManager {
    private static Connection connessioneDB;

    public DbManager(String ip, int port){
        try{
            connessioneDB = DriverManager.getConnection("jdbc:mysql://"+ip+":"+port+"/magmadb", "root", "");
        }
        catch(SQLException e){System.err.println(e.getMessage());}
    }
    
    //02
    public List loadPlayerList(String query){
       List playerList = FXCollections.observableArrayList();
        try(
            PreparedStatement ps = connessioneDB.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            )
        {
            while (rs.next()){
              playerList.add(new Player(rs.getString("usernameID"), rs.getString("faction"), rs.getString("class"), rs.getInt("level"), rs.getInt("rating"), rs.getInt("matches")));
            }
        }catch(SQLException e) {System.err.println("Errore SQL playerList: "+e.getMessage());}
        
        return playerList;
    }
    
    //03
    public List loadPlayerStats(String query){
        List topPlayedData = FXCollections.observableArrayList();
        try(
            PreparedStatement ps = connessioneDB.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            )
        {
            while(rs.next()){
                topPlayedData.add(new PieChart.Data(rs.getString("usernameID"), rs.getInt("matches")));
            }
        }catch(SQLException e) {System.err.println("Errore SQL playerStat: "+e.getMessage());}
        
        return topPlayedData;
    } 
    
    //04
    public int sendItem(String query){
        try(
            PreparedStatement ps = connessioneDB.prepareStatement(query);
            ){
             return ps.executeUpdate();
            }catch(SQLException e) {System.err.println(e.getMessage());}
        return 0;
    }
}

/*
Note:
    [01] Classe per la gestione della connessione e ricezione/invio dei dati
         dal/al database
    [02] loadPlayerList è un metodo che riceve una String con una query e la inoltra
         al database, ritorna una List a seconda della query ricevuta
    [03] loadPlayerStats è un metodo che riceve una query e ritorna una lista contenente
         oggetti <PieChart.Data>
    [04] Funzione per l'invio delle informazioni di un Item nel database. Ritorna
         il risultato della query.

*/