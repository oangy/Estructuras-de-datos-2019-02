
import java.awt.Image;
import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author angy
 */
public class Fantasma {
    int x;
    int y;
    Image imagen;
    Tablero tablero;
    Boolean vivo;
    public Fantasma(Tablero tablero) {
        this.tablero=tablero;
        this.imagen =  new ImageIcon(this.getClass().getResource("/imagenes/fantasmas/fantasma1.gif")).getImage();
        spawn();
    }
    public void spawn(){
        vivo=true;
        int inicio=0;
        int fin =1;
        int spawnAux = ((int)(Math.random()*(fin-inicio+1)+inicio));
        this.x=(tablero.spawnsFantasmas[spawnAux]).x;
        this.y=(tablero.spawnsFantasmas[spawnAux]).y;
    }
}
