/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package init;

/**
 *
 * @author creamcodifier
 */
public class InizializzaDati {
    private final String urlBase="http://webapps.unitn.it/Orari/it/Web/CalendarioCds";
    
    public InizializzaDati ()
    {      
        //chiama la funzione OttieniIdDipartimenti() che ritorna una lista di stringhe
        //passa la lista di dipartimenti alla funzione OttieniIdCorsi(List<String> dipartimenti) che ritorna una lista di stringhe (metodo post)
        //viene chiamata la funzione OttieniEventi che riceve la lista di corsi e ritorna una hashmap che associa ad ogni corso i relativi url di eventi
        //ad ogni evento di ogni corso viene mandata una richesta per ottenere le aule e l'edificio relativo all'evento per costruire 2 map che associano edificio e aule ed edificio e corsi
    }
}
