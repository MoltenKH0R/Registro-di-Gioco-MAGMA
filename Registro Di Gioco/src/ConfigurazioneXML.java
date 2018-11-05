
//01
public class ConfigurazioneXML {
    public ConfigurazioneStyleXML confStyle;
    public ConfigurazioneDBXML confDB;
    public ConfigurazioneServerXML confServer;
    public ConfigurazioneNumXML confNum;
    public ConfigurazioneDateXML confDate;
    
    public ConfigurazioneXML(ConfigurazioneStyleXML cStyle, ConfigurazioneDBXML cDB, ConfigurazioneServerXML cServer, ConfigurazioneNumXML cNum, ConfigurazioneDateXML cDate){
        confStyle = cStyle;
        confDB = cDB;
        confServer = cServer;
        confNum = cNum;
        confDate = cDate; 
    }
    
    class ConfigurazioneStyleXML{
        int sectionDimFont;
        int labelDimFont;
        String font;
        String fontWeight;
        String background;
        
        public ConfigurazioneStyleXML(int sdf, int ldf, String f, String fw, String bg){
            sectionDimFont = sdf;
            labelDimFont = ldf;
            font = f;
            fontWeight = fw;
            background = bg;
        }
    }

    class ConfigurazioneDBXML{
        public int port;
        public String ip;
        
        public ConfigurazioneDBXML(int p, String i){
            port = p;
            ip = i;
        }
    }
    
    class ConfigurazioneServerXML{
        public int port;
        public String ip;
        
        public ConfigurazioneServerXML( String i, int p){
            port = p;
            ip = i;
        }
    }
    
    class ConfigurazioneNumXML{
        public int num;
        
        ConfigurazioneNumXML(int n){
            num = n;
        }
    }
    
    class ConfigurazioneDateXML{
        public String date;
        
        public ConfigurazioneDateXML(String d){
            date = d;
        }
    }
}

/*
Note: 
    [01] Un'istanza di tale classe viene inizializzata all'avvio, prende le 
         informazioni da un file XML. Contiene le seguenti sottoclassi:
            - ConfigurazioneStyleXML
            - ConfigurazioneDBXML
            - ConfigurazioneServerXML
            - ConfigurazioneNumXML
            - ConfigurazioneDateXML
*/