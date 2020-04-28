/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;

/**
 *
 * @author janosfalke
 */
public class JeuDeCartes {
    
    
    private ArrayList <Carte> JeuCartes; 

    public JeuDeCartes() {
        this.JeuCartes = new ArrayList();
    }
    
    public int size(){
        return this.JeuCartes.size();
    }
    
    public Carte get(int i){
        return this.JeuCartes.get(i);
    }
    
    
    
        public void chargement( String NomFichier )throws IOException
{
  	String LeTitre="",NomPhoto=""; int NbEx=-1;  // recuperation temporaire de ces infos
  	Carte uneCarte;
  
 	FileReader fic = new FileReader(NomFichier);  		//ouverture du fichier
	StreamTokenizer entree = new StreamTokenizer(fic);	// intermediaire avec FileReader
	entree.quoteChar('"');
		
// lecture des donn�es dans le fichier connaissant le format-----------------------------------------------------
			int i =0;
			entree.nextToken() ;							// on passe � l'element suivant
			while ( entree.ttype != entree.TT_EOF ) // c'est la fin du fichier ou pb ?
			{  	  
					LeTitre = entree.sval; 
					entree.nextToken() ;
					NomPhoto = entree.sval;
					entree.nextToken() ;
					NbEx = (int) entree.nval;
               
					uneCarte = new Carte( LeTitre, NomPhoto, NbEx); // nouvelle carte
					JeuCartes.add(uneCarte);												// on ajoute � la videotheque
								
		
				i++;
				
				entree.nextToken() ;	
			}  // fin while
			
	fic.close();
}
        
        public String toString(){
            String s = "";
            
            for (int i = 0; i < JeuCartes.size(); i++) {
                s += this.JeuCartes.get(i);
            }
            
            return s;
        }

    void clear() {
        this.JeuCartes.clear();
    }
}
