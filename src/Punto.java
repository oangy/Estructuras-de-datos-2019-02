
import java.awt.Image;
import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Angy 
 */
public class Punto {
    //atributos del punto
    boolean caminable=true,galleta=false,bolaDePoder=false,obstaculo=false;
    Image imagen;
    int puntaje;
    //coordenadas en pixeles
    int x, y;
    
    public Punto(int x, int y){
        puntaje = 10;
        imagen=new ImageIcon(this.getClass().getResource("/imagenes/Items/galleta.png")).getImage();
        this.x=x;
        this.y=y;
    }
}
