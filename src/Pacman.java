
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


    public Pacman(String color, Tablero tablero) {
        this.imagen = new ImageIcon(this.getClass().getResource("/imagenes/pacman/" + color + "/pacman-derecha.gif")).getImage();
        this.tablero = tablero;
        this.superiorx = tablero.superiorx;
        this.superiory = tablero.superiory;
        this.inferiorx = tablero.inferiorx;
        this.inferiory = tablero.inferiory;
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
            if (y - tablero.velocidadPacmans >= 0) {
                y -= tablero.velocidadPacmans;
            } else {
                y = inferiory;
                moverArriba = false;
            }

        }
        if (moverAbajo) {
            if (y + tablero.velocidadPacmans <= superiory) {
                y += tablero.velocidadPacmans;
            } else {
                y = superiory;
                moverAbajo = false;
            }
        }
        if (moverDerecha) {
            if (x + tablero.velocidadPacmans <= superiorx) {
                x += tablero.velocidadPacmans;
            } else {
                x = superiorx;
                moverDerecha = false;
            }
        }
        if (moverIzquierda) {
            if (x - tablero.velocidadPacmans >= 0) {
                x -= tablero.velocidadPacmans;
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

            if ((x >= xMenor-alto) && (y >= yMenor-alto)) {
                if ((x <= xMayor+alto) && (y <= yMayor+alto)) {
                    if (moverArriba) {
                        y=y+tablero.velocidadPacmans;
                        moverArriba=false;
                    }
                    if (moverAbajo) {
                        y=y-tablero.velocidadPacmans;
                        moverAbajo=false;
                    }
                    if (moverDerecha) {
                        x=x-tablero.velocidadPacmans;
                        moverDerecha=false;
                    }
                    if (moverIzquierda) {
                        x=x+tablero.velocidadPacmans;
                        moverDerecha=false;
                    }
                }
            }
        }

    }

}
