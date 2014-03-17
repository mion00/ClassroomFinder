/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Network;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author creamcodifier
 */
public class DownloadDati {
    void ScriviIndirizziSuFile(String nomePolo,JSONArray contenuto)
    {
        FileWriter file;		
		try
		{
		    file = new FileWriter (nomePolo+"_indirizzi.json");
                    if(contenuto!=null)file.write(contenuto.toString());
		    file.close();		
		}
		// Catches any error conditions
		catch (IOException e)
		{
			System.err.println ("Unable to write to file");
			System.exit(-1);
		}
    }
    
    List<String> LeggiIndirizziDaFile(String nomePolo)
    {
        JSONArray contenuto=null;
        List<String> dati=new ArrayList<>();
        FileReader file;
        try
        {
            file = new FileReader (nomePolo+"_indirizzi.json");

            BufferedReader in = new BufferedReader(file);
            String buffer;
            String contenutoFile = "";
            
            try 
            {
                while ((buffer = in.readLine()) != null) {
                    contenutoFile+=buffer;
                }
            } 
            catch (IOException e) 
            {
                System.out.println("Error reading file");
                System.exit(-1);
            }
            try 
            {
                in.close();
            } 
            catch (IOException e) {
                System.out.println("Error closing stream");
                System.exit(-1);
            }
            try 
            {
                file.close();
            } 
            catch (IOException e) 
            {
                System.out.println("Error closing file");
                System.exit(-1);
            }
            contenuto=(JSONArray)JSONValue.parse(contenutoFile);
            if(contenuto!=null)
            {
                for(int i=0;i<contenuto.size();i++)
                    dati.add(contenuto.get(i).toString());
            }
            
        } 
        catch (FileNotFoundException e)
        {
            //Da chiamare la funzione che ottiene gli indirizzi dal web
            ScriviIndirizziSuFile(nomePolo,contenuto);
        }
        
        return dati;
    }
    
    public JSONObject OttieniJSON(String nomePolo,java.util.Date data)
    {
        //Controlla se esiste il file che associa il nome del polo (nomePolo_indirizzi) con gli indirizzi da cui poter ottenere i dati
        LeggiIndirizziDaFile(nomePolo);
            //se il file esiste, gli indirizzi vengono messi in una list di String tramite la funzione OttieniIndirizzi(JSONObject dati)
            //se il file non esiste viene chiamata la funzione OttieniIndirizzi(nomePolo) che ritorna un JSONObject contenente i dati scaricati
                //viene chiamata la funzione ScriviFile(String nomePolo,JSONObject dati) che scrive i dati nel file
                //viene creata una lista di stringhe tramite la funzione OttieniIndirizzi(JSONObject dati)
        //Viene istanziata una classe di MULTITreadHTTP che, attraverso il metodo DownloadFromUrl(List<String> urls) ottiene una List<String>
        //la lista di stringhe contenente i dati viene riorganizzata in un JSONObject conforme alla struttura di Dati tramite la funzione OrganizzaJSON(List<String> dati)
            
        return null;
    }
}
