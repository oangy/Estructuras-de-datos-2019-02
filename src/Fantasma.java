
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

    boolean moverArriba = false;
    boolean moverAbajo = false;
    boolean moverDerecha = false;
    boolean moverIzquierda = false;

    //limites de movimiento
    int superiorx;
    int superiory;
    int inferiory;
    int inferiorx;
    //variable que dice la velocidad, por defecto el profesor la dejo en 4
    int velocidad;

    int x;
    int y;
    int ancho;
    int alto;
    
    int moviFantasma;
    Image imagen;
    Tablero tablero;
    Boolean vivo;

    public Fantasma(Tablero tablero) {
        this.tablero = tablero;
        this.superiorx = tablero.superiorx;
        this.superiory = tablero.superiory;
        this.inferiorx = tablero.inferiorx;
        this.inferiory = tablero.inferiory;
        this.velocidad = 4;
        this.imagen = new ImageIcon(this.getClass().getResource("/imagenes/fantasmas/fantasma1.gif")).getImage();
        this.moviFantasma=((int) (Math.random() * (200 - 0 + 1) + 0));
        this.alto=22;
        this.ancho=22;
        spawn();
    }

    public void spawn() {
        vivo = true;
        int inicio = 0;
        int fin = 1;
        int spawnAux = ((int) (Math.random() * (fin - inicio + 1) + inicio));
        this.x = (tablero.spawnsFantasmas[spawnAux]).x;
        this.y = (tablero.spawnsFantasmas[spawnAux]).y;
    }

    public void mover() {
        //verificar 
        if (moviFantasma-tablero.frames==0) {
            moverArriba = false;
            moverAbajo = false;
            moverDerecha = false;
            moverIzquierda = false;
            
            int aleat = ((int) (Math.random() * (3 - 0 + 1) + 0));
            switch (aleat) {
                case 0:
                    moverArriba = true;
                    break;
                case 1:
                    moverAbajo = true;
                    break;
                case 2:
                    moverIzquierda = true;
                    break;
                case 3:
                    moverDerecha = true;
                    break;
            }
             this.moviFantasma=((int) (Math.random() * (150 - 0 + 1) + 0));
        }
        //eligiendo direccion aleatoria si no se esta moviendo(si choco contra una pared)
        if (!moverIzquierda && !moverDerecha && !moverAbajo && !moverArriba) {
            int aleat = ((int) (Math.random() * (3 - 0 + 1) + 0));
            switch (aleat) {
                case 0:
                    moverArriba = true;
                    break;
                case 1:
                    moverAbajo = true;
                    break;
                case 2:
                    moverIzquierda = true;
                    break;
                case 3:
                    moverDerecha = true;
                    break;
            }
        }
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

            if ((x >= xMenor - 18) && (y >= yMenor - 18)) {
                if ((x <= xMayor + 30) && (y <= yMayor + 30)) {
                    if (moverArriba) {
                        y = y + velocidad;
                        moverArriba = false;
                    }
                    if (moverAbajo) {
                        y = y - velocidad;
                        moverAbajo = false;
                    }
                    if (moverDerecha) {
                        x = x - velocidad;
                        moverDerecha = false;
                    }
                    if (moverIzquierda) {
                        x = x + velocidad;
                        moverDerecha = false;
                    }
                }
            }
        }
    }
}
