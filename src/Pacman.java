
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
public class Pacman {

    int x;
    int y;
    int ancho;
    int alto;
    Image imagen;

    boolean moverArriba = false;
    boolean moverAbajo = false;
    boolean moverDerecha = false;
    boolean moverIzquierda = false;
    Tablero tablero;
    String color;
    //limites de movimiento
    int superiorx;
    int superiory;
    int inferiory;
    int inferiorx;
    //variable que dice la velocidad, por defecto el profesor la dejo en 4
    int velocidad;

    public Pacman(String color, Tablero tablero) {
        this.imagen = new ImageIcon(this.getClass().getResource("/imagenes/pacman/" + color + "/pacman-derecha.gif")).getImage();
        this.tablero = tablero;
        this.superiorx = tablero.superiorx;
        this.superiory = tablero.superiory;
        this.inferiorx = tablero.inferiorx;
        this.inferiory = tablero.inferiory;
        this.velocidad = 4;
        this.color = color;
        this.alto=22;
        this.ancho=22;
        spawn();
    }
    
    public void spawn(){
        int inicioX=0;
        int finX =tablero.numCoordenadasX;
        this.x = ((int)(Math.random()*(finX-inicioX+1)+inicioX))*30;
        int inicioY=tablero.numCoordenadasY-3;
        int finY =tablero.numCoordenadasY;
        this.y = ((int)(Math.random()*(finY-inicioY+1)+inicioY))*30;
        System.out.println(this.x);
        System.out.println(this.y);
    }
    
    public void mover() {
        //verificando si esta dentro de los limites del tablero
        if (moverArriba) {
            if (y - velocidad >= 0) {
                y -= velocidad;
            } else {
                y = inferiory;
                moverArriba = false;
            }

        }
        if (moverAbajo) {
            if (y + velocidad <= superiory) {
                y += velocidad;
            } else {
                y = superiory;
                moverAbajo = false;
            }
        }
        if (moverDerecha) {
            if (x + velocidad <= superiorx) {
                x += velocidad;
            } else {
                x = superiorx;
                moverDerecha = false;
            }
        }
        if (moverIzquierda) {
            if (x - velocidad >= 0) {
                x -= velocidad;
            } else {
                x = inferiorx;
                moverIzquierda = false;
            }
        }

        //verificando si donde se va a mover no es un obstaculo
        for (int i = 0; i < tablero.obstaculos.length; i++) {
            int xMenor = (int) (tablero.obstaculos[i]).inicio.x;
            int yMenor = (int) (tablero.obstaculos[i]).inicio.y;
            int xMayor = (int) (tablero.obstaculos[i]).fin.x;
            int yMayor = (int) (tablero.obstaculos[i]).fin.y;

            if ((x >= xMenor-18) && (y >= yMenor-18)) {
                if ((x <= xMayor+30) && (y <= yMayor+30)) {
                    if (moverArriba) {
                        y=y+velocidad;
                        moverArriba=false;
                    }
                    if (moverAbajo) {
                        y=y-velocidad;
                        moverAbajo=false;
                    }
                    if (moverDerecha) {
                        x=x-velocidad;
                        moverDerecha=false;
                    }
                    if (moverIzquierda) {
                        x=x+velocidad;
                        moverDerecha=false;
                    }
                }
            }
        }

    }

}
