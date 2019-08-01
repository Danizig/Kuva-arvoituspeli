/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package harjoitustyo.janiheiskanen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Kuva-arvoitus peli, jossa pelaaja yrittää löytää oikeat
 * parit korttien seasta. 
 * Alustetaan parien ja rivien määrä.
 * Määritellään valittu kortti nulliksi.
 * Asetetaan klikkausrajaksi 2
 * Alustetaan klikkilaskuri 0
 * @author JANI
 */
public class Harjoitustyo extends Application {
    
    private static final int PARIT = 8;
    private static final int RIVIT = 4;

    private Ruutu valittu = null;
    private int Klikkaukset = 2;
    private int KlikkausLaskuri = 0;
    
    
    

  /**
   *  Start metodi, jossa alustetaan pohjapaneeli, painikkeet ja tulosteksti.
    Metodi luo listan ja lisää kortit siihen, sekä antaa niille arvot.
    Metodi sekoittaa kortit ja ruutu-olion avulla luo ne graafisesti.
    Metodi myöskin laskee klikkausten määrän, sekä asettaa toiminnot
    "Tallenna" ja "Tulosta" painikkeille. Metodi luo ikkunan, johon pohjapaneeli
    asetetaan.
   * @param primaryStage 
   */
    @Override
    public void start(Stage primaryStage){
     
        Pane root = new Pane();
        TextField tf = new TextField();
        Label lbl = new Label("Nimesi", tf );
        lbl.setTranslateX(30);
        lbl.setTranslateY(250);
        HBox napit = new HBox(10);
        Button btn1 = new Button("Tallenna");
        Button btn2 = new Button("Tulosta");
        napit.getChildren().addAll(btn1, btn2);
        napit.setTranslateX(30);
        napit.setTranslateY(300);
        Text tulos = new Text(250, 100, "");
        tulos.setFont(Font.font(15));
        tulos.setFill(Color.DARKRED);
        tulos.setUnderline(true);
        root.getChildren().addAll(lbl, napit, tulos);
        
        char c = 'A';
        List<Ruutu> ruudut = new ArrayList<>();
        
        for (int i = 0; i < PARIT; i++) {
            ruudut.add(new Ruutu(String.valueOf(c)));
            ruudut.add(new Ruutu(String.valueOf(c)));
            c++;
        }
        

        Collections.shuffle(ruudut);

        for (int i = 0; i < ruudut.size(); i++) {
            Ruutu ruutu = ruudut.get(i);
            ruutu.setTranslateX(50 * (i % RIVIT));
            ruutu.setTranslateY(50 * (i / RIVIT));
            root.getChildren().add(ruutu);
        }
       
        root.setOnMouseClicked(e -> {
            KlikkausLaskuri++;
        });
        
       btn1.setOnAction(e ->{
           try{
           kirjoita(tf.getText(), KlikkausLaskuri);
           }catch(Exception e1){
               System.out.println("Virhe");
           }
       });
       btn2.setOnAction(e ->{
         
            try {
                tulos.setText(lue());
            } catch (FileNotFoundException ex) {
                System.out.println("Virhe");
            }
       
       });
        

        Scene scene = new Scene(root, 470, 350); 
        primaryStage.setScene(scene);
        primaryStage.setTitle("Muistipeli");
        primaryStage.show();
    }
    /**
     * Metodi kirjoittaa pelin tuloksen ja pelaajan syöttämän nimen
     * tiedostoon.
     * @param nimi
     * @param tulos
     * @throws Exception 
     */
    public void kirjoita(String nimi, int tulos) throws Exception{
        String tiedosto = "tulokset.txt";
        PrintWriter kirjoittaja = null;
        FileWriter fw = new FileWriter(tiedosto, true);
        kirjoittaja = new PrintWriter(fw);
        kirjoittaja.println("Nimi: "+nimi+", Tulos: "+tulos);
        kirjoittaja.close();
        
    }
    /**
     * Metodi palauttaa String alkion, joka sisältää
     * viimeisimpänä tiedostoon tallennetut tiedot
     * @return
     * @throws FileNotFoundException 
     */
    public String lue() throws FileNotFoundException{
        String tiedosto = "tulokset.txt";
        String rivi = "";
        File luettava = new File(tiedosto);
        Scanner lukija = new Scanner(luettava);
        while (lukija.hasNext()){
            rivi = lukija.nextLine();
        }
        lukija.close();
        return rivi;
    }

    /**
     * Ruutu-luokka, joka sisältää kortin sisällön, sekä koon.
     * 
     */
    private class Ruutu extends StackPane {
        private Text text = new Text();
        String arvo;
        Rectangle alue = new Rectangle(50, 50);
        

        /**
         * Ruutu-luokan alustaja, joka määrittää kortin tyylin,
         * sekä sisältää tapahtumakäsittelijän korttien valitsemiselle.
         * @param arvo 
         */
        public Ruutu(String arvo) {
            
            this.arvo = arvo;
            

            alue.setFill(Color.WHITE);
            alue.setStroke(Color.BLACK);

            text.setText(arvo);
            text.setFont(Font.font(30));

            setAlignment(Pos.CENTER);
            getChildren().addAll(alue, text);

            setOnMouseClicked(event ->{
                
                if (Avattu() || Klikkaukset == 0)
                    
                
                    
                return;

            Klikkaukset--;

            if (valittu == null) {
                valittu = this;
                avaa(() -> {});
            }
            else {
                avaa(() -> {
                  
                    if(!OnSama(valittu)){
                        valittu.sulje();
                        this.sulje();
                    }
                   

                    valittu = null;
                    Klikkaukset = 2;
                    
                    
                
            });
            
        }
            
       });
          
            sulje();
          
            
                    }

        /**
         * Metodi palauttaa kortin sisällön
         * läpikuultamattomuuden.
         * @return 
         */
        public boolean Avattu() {
            return text.getOpacity() == 1;
        }

        /**
         * Metodi suorittaa kortin avaamisanimaation.
         * @param action 
         */
        public void avaa(Runnable action) {
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), text);
            ft.setToValue(1);
            ft.setOnFinished(e -> action.run());
            ft.play();
        }

        /**
         * Metodi suorittaa kortin sulkemisanimaation
         */
        public void sulje() {
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), text);
            ft.setToValue(0);
            ft.play();
        }

        /**
         * Metodi vertailee avattujen korttien sisältöä.
         * @param other
         * @return 
         */
        public boolean OnSama(Ruutu other) {
            return text.getText().equals(other.text.getText());
        }
        
    }

    /**
     * Pääohjelma
     * @param args 
     */
    public static void main(String[] args) {
        launch(args);
        
}
}
