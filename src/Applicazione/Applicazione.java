/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Applicazione;

import Dati.Universita;
import init.InizializzaDati;
import org.json.simple.JSONObject;



/**
 *
 * @author creamcodifier
 */
public class Applicazione {
    public static void main(String args[])
    {
        Universita trento = new Universita();
        trento.OttieniDati("Povo1",new java.util.Date());
        new InizializzaDati();
    }
}
