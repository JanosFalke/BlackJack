/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

/**
 *
 * @author janosfalke
 */
public class Carte {
    
    private String cardName;
    private String link;
    private int value;
    
    Carte (String cardName, String link, int value){
        this.cardName = cardName;
        this.link = link;
        this.value = value;
    }
    
    
    
    
    public String toString(){
        return "Card: "+this.getCardName()+" - "+this.getLink()+" with value: "+this.getValue()+"\n";
        
    }

    /**
     * @return the cardName
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }
    
    
}
