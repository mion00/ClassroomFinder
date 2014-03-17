/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dati;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author creamcodifier
 */
public class Dati {
    Hashtable<Aula,List<Lezione>> dati;
    
    void ScriviFile(String nomeFile, JSONObject contenuto)
    {
        FileWriter file;		
		try
		{
		    file = new FileWriter (nomeFile+".json");
		    file.close();		
		}
		// Catches any error conditions
		catch (IOException e)
		{
			System.err.println ("Unable to write to file");
			System.exit(-1);
		}
    }
    
    JSONObject LeggiFile(String nomeFile)
    {
        JSONObject contenuto=null;
        FileReader file;
        try
        {
            file = new FileReader (nomeFile+".json");

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
            contenuto=(JSONObject)JSONValue.parse(contenutoFile);
        } 
        catch (FileNotFoundException e)
        {
            ScriviFile(nomeFile,contenuto);
        }
        
        return contenuto;
    }
    
    boolean ControllaValidita(JSONObject datiLetti, java.util.Date data)
    {
        String dataExpire=datiLetti.get("expire").toString();
        java.util.Date expire=new java.util.Date((long)Integer.parseInt(dataExpire)*1000);
        return data.before(expire);
    }
    
    Hashtable<Aula,List<Lezione>> OrganizzaDati(JSONObject datiLetti)
    {
        Hashtable<Aula,List<Lezione>> dati=new Hashtable<Aula,List<Lezione>>();
        return dati;
    }
    
    JSONObject DownloadDati(String nomePolo,java.util.Date data)
    {
        JSONObject datiScaricati=null;
        return datiScaricati;
    }
    
    void OttieniDati(String nomePolo,java.util.Date data)
    {
        //chiama la funzione LeggiFile(String nomeFile) che ritorna un JSONObject realtivo al file
        JSONObject datiLetti=LeggiFile(nomePolo); 
        
        //chiama la funzione ControllaValidita(JSONObject datiLetti, java.util.Date data) e ritorna true se i dati sono validi, false altrimenti
        if(ControllaValidita(datiLetti,data))
        {
            //se ControllaValidita(...) ritorna true, viene chiamata la funzione OrganizzaDati(JSONObject datiLetti) e ritorna Hashtable<Aula,List<Lezione>>
            dati=OrganizzaDati(datiLetti);
        }
        else
        {
            //se ControllaValidita(...) ritorna false, viene invocata la funzione DonwloadDati(String nomePolo, java.util.Date data) e ritorna JSONObject datiOttuneuti
            JSONObject datiOttenuti=DownloadDati(nomePolo, data);
 
            //chiama una funzione ScriviFile(String nomePolo,JSONObject contenuto) che sovrascrive il file vecchio con i nuovi dati
            ScriviFile(nomePolo,datiOttenuti);
            //viene chiamata la funzione  OrganizzaDati(JSONObject datiLetti) e ritorna Hashtable<Aula,List<Lezione>>
            OrganizzaDati(datiOttenuti);
        }
       
        
        
    
    }
}
