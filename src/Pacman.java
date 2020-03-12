
import java.awt.Image;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author angy
 */
public class Pacman {
    
    int x;
    int y;
    Image imagen;
    
    boolean moverArriba = false;
    boolean moverAbajo = false;
    boolean moverDerecha = false;
    boolean moverIzquierda = false;
    Tablero tablero;
    //limites de movimiento
    int superiorx;
    int superiory;
    int inferiory;
    int inferiorx;
    //variable que dice la velocidad, por defecto el profesor la dejo en 4
    int velocidad;
    
    public Pacman(int x, int y, Image imagen, Tablero tablero) {
        this.x = x;
        this.y = y;
        this.imagen = imagen;
        this.tablero = tablero;
        this.superiorx=tablero.superiorx;
        this.superiory=tablero.superiory;
        this.inferiorx=tablero.inferiorx;
        this.inferiory=tablero.inferiory;
        this.velocidad=4;
    }
    
    public void mover(){
        if (moverArriba) {
            if (y-velocidad>=0) {
                y -= velocidad;
            }
            else{
                y=inferiory;
                moverArriba=false;
            }
            
        }
        if (moverAbajo) {
            if (y+velocidad<=superiory) {
                y += velocidad;
            }
            else{
                y=superiory;
                moverAbajo=false;
            }
        }
        if (moverDerecha) {
            if (x+velocidad<=superiorx) {
                x += velocidad;
            }
            else{
                x=superiorx;
                moverDerecha=false;
            }
        }
        if (moverIzquierda) {
            if (x-velocidad>=0) {
                x -= velocidad;
            }
            else{
                x=inferiorx;
                moverIzquierda=false;
            }
        }
    }

}
