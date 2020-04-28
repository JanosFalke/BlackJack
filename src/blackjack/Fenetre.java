/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

/**
 *
 * @author janosfalke
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*; // Pour pouvoir utiliser les fichiers
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Fenetre extends JFrame implements ActionListener {

    private JPanel zoneDessin;            // zone de dessin central ou on va dessiner
    private JPanel p1;                    // zone boutons hauts
    private JPanel p2;                    // zone boutons bas  

    private Image im;
    private Image imNext;
    private ArrayList<Image> images;
    private ArrayList<Image> imagesPC;

    JButton prendCarte = new JButton("Piocher carte");
    JButton arreter = new JButton("Arreter");
    JButton rejouer = new JButton("Rejouer");

    private JeuDeCartes jeuCartes;
    private int mise = 1;
    private int partiesJouees = 0;
    private int solde = 20;
    private int valuePlayer = 0;
    private int valuePC = 0;
    private int compteurCartes = 0;
    private int compteur = 0;
    private int distancePlayer = 21 - valuePlayer;
    private int distancePc = 21 - valuePC;
    private int compteurRejouer = 1;

    private boolean win = true;
    private boolean winGame = false;
    private boolean lostGame = false;
    private boolean egalite = false;
    private boolean finit = false;
    private boolean noMoney = false;

// CONSTRUCTEUR 
    public Fenetre(String titre, int largeur, int hauteur) throws IOException {

        super(titre);

        init();
        prendCarte.addActionListener(this);
        arreter.addActionListener(this);
        rejouer.addActionListener(this);
        getContentPane().setLayout(new BorderLayout());
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mise_en_page(largeur, hauteur);    // on place les boutons et la zone de dessin ...

        final JPanel panel = new JPanel();

        setVisible(true);
        setLocationRelativeTo(null);

        JOptionPane.showMessageDialog(panel, "La mise: 1€ (minimum) \nSi gagné: + mise*2 \nSi perdu: - mise  \nSi match nul: + mise\nSi 21 (BlackJack): + mise*4", "Explications BlackJack",
                JOptionPane.INFORMATION_MESSAGE);

        repaint();

    }

// la fenetre est constitu�e de trois parties Panel Nord : boutons ; Sud : boutons; Centre: zone de zoneDessin
    public void mise_en_page(int maxX, int maxY) {
        //--------------------------------------------------------------------
        // insertion boutons du haut
        this.p1 = new JPanel(new GridLayout());

        p1.add(prendCarte);
        p1.add(arreter);
        p1.add(rejouer);

        getContentPane().add(p1, "North");  // on ajoute le panel en haut de la fenetre frame

        //--------------------------------------------------------------------
        // insertion boutons du bas
        this.p2 = new JPanel(new GridLayout());
        ajouteBouton("Miser +1€", p2);
        ajouteBouton("Miser -1€", p2);
        ajouteBouton("Quitter", p2);

        getContentPane().add(p2, "South");  // on ajoute le panel en bas

        //--------------------------------------------------------------------
        // zone de dessin
        this.zoneDessin = new JPanel();
        this.zoneDessin.setSize(maxX, maxY);
        this.zoneDessin.setPreferredSize(new Dimension(maxX, maxY));
        getContentPane().add(this.zoneDessin, "Center");  // panel pour zoneDessiner au milieu
        //--------------------------------------------------------------------

        pack();
        setVisible(true);
    }

    // Accesseur au zoneDessin de la fen�tre
    public Graphics getzoneDessin() {
        return this.zoneDessin.getGraphics();
    }

    void init() throws IOException {
        mise = 1;
        valuePlayer = 0;
        valuePC = 0;
        compteurCartes = 0;
        compteur = 0;

        win = true;
        finit = false;
        winGame = false;
        lostGame = false;
        egalite = false;
        partiesJouees++;
        compteurRejouer++;

        if (partiesJouees > 1) {
            images.clear();
            imagesPC.clear();
        }

        jeuCartes = new JeuDeCartes();
        images = new ArrayList();
        imagesPC = new ArrayList();

        jeuCartes.chargement("cartes.txt");

        int randomFirstCard = randomInt();
        valuePlayer += jeuCartes.get(randomFirstCard).getValue();

        prendCarte.setEnabled(true);
        arreter.setEnabled(true);
        rejouer.setEnabled(false);

        if (this.solde <= 0 || this.mise > this.solde) {
            finit();
        }

        try {
            im = ImageIO.read(new File("cards/" + jeuCartes.get(randomFirstCard).getLink()));
        } catch (IOException ex) {
        }

    }

    int randomInt() {

        return 0 + (int) (Math.random() * ((51 - 0) + 1));
    }

    void perdu() throws IOException {
        lostGame = true;
        arreter.setEnabled(false);
        rejouer.setEnabled(true);
    }

    void finit() {
        prendCarte.setEnabled(false);
        arreter.setEnabled(false);
        noMoney = true;
        finit = true;
    }

    void arreter() {

        arreter.setEnabled(false);
        prendCarte.setEnabled(false);
        distancePlayer = 21 - valuePlayer;
        distancePc = 21 - valuePC;
        if (valuePC > 21) {
            lostGame = false;
            winGame = true;
        } else if (lostGame == false && winGame == false) {
            while (Math.abs(distancePc) >= Math.abs(distancePlayer) && distancePc >= 4) {
                pcPlays();
                distancePc = 21 - valuePC;
            }

            if (distancePlayer < 2 && distancePc < 4 && distancePc > distancePlayer) {
                pcPlays();
                distancePc = 21 - valuePC;
            }

        }
        won();

    }

    void won() {
        rejouer.setEnabled(true);

        if (valuePlayer < 21 && valuePC <= 21) {

            if (Math.abs(distancePlayer) > Math.abs(distancePc)) {
                lostGame = true;
                winGame = false;
                egalite = false;

            } else if (Math.abs(distancePlayer) < Math.abs(distancePc)) {
                winGame = true;
                lostGame = false;
                egalite = false;
                this.solde += (this.mise * 2);

            } else if (Math.abs(distancePc) == Math.abs(distancePlayer)) {
                egalite = true;
                lostGame = false;
                winGame = false;
                this.solde += (this.mise);
            }
        } else if (valuePlayer == 21) {
            if (valuePC != 21) {
                winGame = true;
                lostGame = false;
                egalite = false;
                this.solde += (this.mise * 4);

            } else {
                egalite = true;
                lostGame = false;
                winGame = false;
                this.solde += (this.mise);
            }
        } else if (valuePC >= 21 || valuePlayer > 21) {

            if (valuePC > 21 && valuePlayer > 21) {
                egalite = true;
                lostGame = false;
                winGame = false;
                this.solde += (this.mise);

            } else if (valuePC > 21 && valuePlayer < 21) {
                winGame = true;
                lostGame = false;
                egalite = false;
                this.solde += (this.mise * 2);

            } else if (valuePlayer > 21 && valuePC <= 21) {
                lostGame = true;
                winGame = false;
                egalite = false;
            }
        }

        if (this.solde == 0) {
            prendCarte.setEnabled(false);
            arreter.setEnabled(false);
        }

    }

    void playerPlays() {
        int randomCard = randomInt();

        if (21 - valuePlayer < 11 && jeuCartes.get(randomCard).getValue() == 11) {
            valuePlayer += 1;

        } else {
            valuePlayer += jeuCartes.get(randomCard).getValue();
        }

        try {
            images.add(ImageIO.read(new File("cards/" + jeuCartes.get(randomCard).getLink())));      // on charge une image
        } catch (IOException ex) {
        }
    }

    void pcPlays() {
        int randomCardPC = randomInt();

        if (21 - valuePC < 11 && jeuCartes.get(randomCardPC).getValue() == 11) {
            valuePC += 1;

        } else {
            valuePC += jeuCartes.get(randomCardPC).getValue();
        }

        try {
            imagesPC.add(ImageIO.read(new File("cards/" + jeuCartes.get(randomCardPC).getLink())));      // on charge une image
        } catch (IOException ex) {
        }
    }

    // Proc�dure d'arr�t
    void quitter() {
        System.exit(0);
    }

    void effacer() {
        Graphics g = this.zoneDessin.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    private void ajouteBouton(String label, JPanel p) {
        // Ajoute un bouton, avec le texte label, au panneau p
        JButton b = new JButton(label);
        p.add(b);
        b.addActionListener(this);
    }

    public void paint(Graphics g) // dessin de la fen�tre g�n�rale
    {

        int xPhoto = 140;
        int pcPhoto = 120;

        this.p1.repaint();  // on redessine les boutons hauts
        this.p2.repaint();  // on redessine les boutons bas

        g = this.zoneDessin.getGraphics(); // on redessine dans le panel de dessin

        effacer();
        // c'est ici qu'il faut mettre les elements � afficher
        g.drawImage(im, 140, 10, 100, 170, zoneDessin);

        for (int i = 0; i < this.images.size(); i++) {
            xPhoto += 30;
            g.drawImage(images.get(i), xPhoto, 10, 100, 170, zoneDessin);
        }

        for (int i = 0; i < this.imagesPC.size(); i++) {
            pcPhoto += 30;
            g.drawImage(imagesPC.get(i), pcPhoto, 200, 100, 170, zoneDessin);
        }

        Font StyleLesTitres = new Font("TimesRoman", Font.BOLD, 18); // trois styles d'ecriture
        Font StyleMoyen = new Font("TimesRoman", Font.ITALIC, 12);
        Font StyleBig = new Font("TimesRoman", Font.PLAIN, 18);

        // Afficher l'affiche
        g.setFont(StyleLesTitres);
        g.drawString("Mise: " + this.mise + "€", 10, 20);
        // Afficher le titre
        g.setFont(StyleMoyen);
        g.drawString("Solde: " + this.solde + "€", 10, 40);
        // Afficher le texte
        g.setFont(StyleBig);
        g.drawString("Valeur: " + valuePlayer, 10, 120);

        g.setFont(StyleBig);
        g.drawString("Valeur-Ordi: " + valuePC, 10, 250);

        if (lostGame == true) {
            g.setFont(StyleBig);
            g.setColor(Color.red);
            g.drawString("Vous avez perdu!", 180, 410);
            g.drawString("Perte : -" + this.mise + "€", 360, 310);
            prendCarte.setEnabled(false);
        } else if (winGame == true) {
            g.setFont(StyleBig);
            g.setColor(Color.green);
            g.drawString("Vous avez gagné!", 180, 410);
            prendCarte.setEnabled(false);
            if (valuePlayer == 21) {
                g.setColor(Color.ORANGE);
                g.drawString("BLACKJACK(x4)", 30, 410);
                g.drawString("Gain : " + this.mise * 4 + "€", 360, 310);
            } else {
                g.drawString("Gain : " + this.mise * 2 + "€", 360, 310);
            }
        } else if (egalite == true) {
            g.setFont(StyleBig);
            g.setColor(Color.DARK_GRAY);
            g.drawString("Match nul!", 180, 410);
            prendCarte.setEnabled(false);
        }

        if (noMoney == true) {
            g.setFont(StyleBig);
            g.setColor(Color.RED);
            g.drawString("Vous n'avez plus d'argent", 140, 300);
        }

    }

    // GESTION DES ACTIONS SUITE A UN APPUIS SUR BOUTON : cette methode est declench�e si Un bouton quelconque est appuy�
    public void actionPerformed(ActionEvent e) // on associe l'evenement souris sur bouton avec l'execution d'un sous prg
    {
        String c = e.getActionCommand();     // on capte l'evenement : nom du bouton !

        if (finit == false) {

            if ("Piocher carte".equals(e.getActionCommand())) {
                distancePlayer = 21 - valuePlayer;
                distancePc = 21 - valuePC;
                compteurCartes++;

                if (compteurCartes > 0) {
                    rejouer.setEnabled(false);
                }

                if (compteur == 0) {
                    this.solde -= this.mise;
                    compteur++;
                }
                if (valuePlayer < 21 && win == true) {
                    playerPlays();
                    if (valuePlayer > 21) {
                        try {
                            perdu();
                        } catch (IOException ex) {
                            Logger.getLogger(Fenetre.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } else {
                        if (valuePC < 21) {
                            pcPlays();
                            if (valuePC >= 21) {
                                arreter();
                            }

                        } else {
                            arreter();
                        }
                    }

                } else if (valuePlayer == 21) {
                    arreter();

                } else {
                    try {
                        perdu();
                    } catch (IOException ex) {
                        Logger.getLogger(Fenetre.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                if (valuePlayer == 21) {
                    arreter();
                }
                repaint();
            }

            if ("Arreter".equals(e.getActionCommand())) {
                if (compteur == 0) {
                    this.solde -= this.mise;
                }
                arreter();

                repaint();
            }

            if (c.equals("Miser +1€")) {

                if (this.mise < 20 && this.images.size() < 1) {

                    if (this.mise < this.solde) {
                        this.mise += 1;
                    }

                    if (this.mise > this.solde) {
                        prendCarte.setEnabled(false);
                        arreter.setEnabled(false);
                        noMoney = true;
                    }
                }

                repaint();
            }

            if (c.equals("Miser -1€")) {

                if (this.mise <= 10 && this.mise > 1 && this.images.size() < 1) {
                    this.mise -= 1;

                    if (this.mise > this.solde) {
                        prendCarte.setEnabled(true);
                        arreter.setEnabled(true);
                        noMoney = true;
                    }
                }

                repaint();
            }

            if ("Rejouer".equals(e.getActionCommand())) {
                try {
                    init();
                } catch (IOException ex) {
                    Logger.getLogger(Fenetre.class.getName()).log(Level.SEVERE, null, ex);
                }

                repaint();

            }
        }
        if (c.equals("Quitter")) {
            quitter();
        }

    }

}
