/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dati;

import java.util.Date;
import java.util.Hashtable;

/**
 *
 * @author creamcodifier
 */
public class Universita {
    Hashtable<String,Dati> polo;
    
    public Universita()
    {
        polo=new Hashtable<>();
    }
    
    public void OttieniDati(String nomePolo,java.util.Date data)
    {
        if(polo.containsKey(nomePolo)) // se il nome è gia presente controlla che i dati siano aggiornati
        {
            polo.get(nomePolo).OttieniDati(nomePolo,data);
        }
        else // se il nome non è presente lo aggiunge alla tabella e poi controlla che i dati siano aggiornati
        { 
            Dati dati=new Dati();
            polo.put(nomePolo,dati);
            dati.OttieniDati(nomePolo,data);
        }
    }
    
}
