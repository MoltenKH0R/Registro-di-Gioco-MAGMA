
import com.thoughtworks.xstream.XStream;
import java.io.*;
import java.nio.file.*;
import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*; 
import org.w3c.dom.Document;

//01
public class XmlHandler {
    ConfigurazioneXML configurazione;
      
    public XmlHandler(){}
    
    //02
    public ConfigurazioneXML executeXmlDeserialize(){
        deserializeXML();
        
        return configurazione;
    }
    
    //03
    private void deserializeXML(){
        validateXML();
        XStream xs = new XStream();
        try{
            configurazione = (ConfigurazioneXML) xs.fromXML(new String(Files.readAllBytes(Paths.get("./ConfigurazioneXML.xml"))));
        }catch(IOException e){System.err.println("ERRORE deserializzazione: "+ e.getMessage());}
    }
    
    //04
    private void validateXML(){
        try{
            DocumentBuilder docb = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Document d = (Document) docb.parse(new File("./ConfigurazioneXML.xml"));
            Schema s = sf.newSchema(new StreamSource(new File("./ConfigurazioneXML.xsd")));
            s.newValidator().validate(new DOMSource(d));
        }catch(Exception e){System.out.println("Errore validazione: "+ e.getMessage());}
    }
}

/*
Note: 
    [01]: Clase per la gestione delle operazioni di file in estensione .xml
    [02]: unica funzione pubblica accessibile dalle altre classi che invoca le
          funzioni di elaborazione xml
    [03]: funzione di deserializzazione, prende il file xml e crea un oggetto di 
          tipo ConfigurazioneXML che conterrà tutte le proprietà
    [04]: funzione di validazione per il controllo della corretta struttura tra
          lo schema xsd e i dati xml
*/
