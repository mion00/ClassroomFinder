/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dati;

/**
 *
 * @author mion00
 */
public class Dipartimento {
    protected String nome;
    protected String urlCorso;
    String annoAccademico;
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setAnno(String anno) {
        this.annoAccademico = anno;
    }
    
    public String getAnno() {
        return annoAccademico;
    }
    
    public void setUrlCorso(String value){
        this.urlCorso = "id="+annoAccademico+"&id2="+value;
    }
    
    public String getUrlCorso() {
        return urlCorso;
    }
    
    public String toString() {
        return "NOME: " + nome + " \t URLCorso: " + urlCorso + " \t anno: " + annoAccademico;
    }
}
