/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package init;

import Database.Dipartimento;
import Network.DownloadDati;
import Network.MultiThreadHTTP;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author creamcodifier
 */
class Corso {
    String id;
    String nome;
    Dipartimento dipartimento;
    
    Corso(String id,String nome)
    {
        this.id=id;
        this.nome=nome;
    }
    
    @Override
    public String toString() {
        return "NOME: " + nome + " ID: " + id + " | " + dipartimento.toString();
    }
}

public class InizializzaDati {
    private final String urlBase="http://webapps.unitn.it/Orari/it/Web/CalendarioCds";
    private final String urlPost = "http://webapps.unitn.it/Orari/it/Web/AjaxCds";
    private Document doc;
    private String annoSelected;
    
    final void checkConfigDir() {
        File config = new File("config");
        if (!config.exists()) {
            config.mkdir();
        }
    }
    
    private void DownloadBaseHTML() {
        try {
            doc = Jsoup.connect(urlBase).get();
        } catch (IOException ex) {
            System.out.println("Unable to download HTML page");
        }
    }
    
    private String CalcolaAnnoAccademico() {
    Elements options = doc.getElementsByAttributeValue("name", "id");

    Elements anni = options.get(0).getElementsByTag("option");

    Elements annoselected = anni.get(0).getElementsByAttributeValue("selected", "selected");

    String anno_id= annoselected.get(0).attributes().get("value");

    return anno_id;
    }
    
    List<Dipartimento> OttieniDipartimenti() {
        List<Dipartimento> indirizzi = new ArrayList<>();
        Elements labels = doc.getElementsByAttributeValue("name", "id2");
        Elements dipartimenti = labels.get(0).getElementsByTag("option");

        annoSelected = CalcolaAnnoAccademico();
        
        for (int i = 1; i<dipartimenti.size(); i++) {
            Element elem = dipartimenti.get(i);
            String value = elem.attributes().get("value");
            String name = elem.html();

            Dipartimento dip = new Dipartimento();
            dip.id = i-1;
            dip.setNome(name);
            dip.setAnno(annoSelected);
            dip.setUrlCorso(value);
            indirizzi.add(dip);
        }

//            DEBUG
//        for (int i = 0; i< indirizzi.size(); i++) {
//            System.out.println(indirizzi.get(i));
//        }
         
        
        return indirizzi;
    }
    
    void SalvaDipartimenti() {
        try {
            FileWriter writer = new FileWriter("config/dipartimenti.json");
            
            List<Dipartimento> list = OttieniDipartimenti();
            JSONArray array = new JSONArray();
            
            BufferedWriter buff = new BufferedWriter(writer);
            
            for (int i=0; i<list.size(); i++) {
                Dipartimento dip = list.get(i);
                Map obj = new LinkedHashMap();
                obj.put("id", dip.id);
                obj.put("name", dip.getNome());
                obj.put("urlCorse", dip.getUrlCorso());
                obj.put("accademicYear", dip.getAnno());

                array.add(JSONValue.toJSONString(obj));
            }
          
            buff.write(array.toString());
            buff.close();
            
        } catch (IOException ex) {
            System.out.println("Unable to write file");
        }
    }
    
    void OttieniCorsi(List<Dipartimento> list_dip) {
        List<String> parameters = new ArrayList<>();
        List<String> courses = new ArrayList<>();
        
        for (Dipartimento dip : list_dip) {
            String parameter = "id="+annoSelected+"&id2="+dip.id;
            parameters.add(parameter);
        }
        
////        DEBUG
//        for (int i=0; i < parameters.size(); i++){
//            System.out.println(parameters.get(i));
//        }
        
        MultiThreadHTTP downloader = new MultiThreadHTTP();
        courses = downloader.PostfFromParameters(urlPost, parameters);
        
        for (int i=0; i<courses.size();i++) {
            System.out.println(courses.get(i));
        }
    }
    
    public InizializzaDati ()
    {      
        checkConfigDir();
        DownloadBaseHTML();
        SalvaDipartimenti();
//        List<Dipartimento> list_dip = OttieniDipartimenti();
//        OttieniCorsi(list_dip);
        //chiama la funzione OttieniIdDipartimenti() che ritorna una lista di stringhe
        //passa la lista di dipartimenti alla funzione OttieniIdCorsi(List<String> dipartimenti) che ritorna una lista di stringhe (metodo post)
        //viene chiamata la funzione OttieniEventi che riceve la lista di corsi e ritorna una hashmap che associa ad ogni corso i relativi url di eventi
        //ad ogni evento di ogni corso viene mandata una richesta per ottenere le aule e l'edificio relativo all'evento per costruire 2 map che associano edificio e aule ed edificio e corsi
    }
}
