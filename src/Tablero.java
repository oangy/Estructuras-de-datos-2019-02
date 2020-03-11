
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Tablero extends JPanel implements ActionListener {

    Image pacmanDerechaImg = new ImageIcon(this.getClass().getResource("pacmanDerecha.gif")).getImage();
    Image pacmanIzquierdaImg = new ImageIcon(this.getClass().getResource("pacmanIzquierda.gif")).getImage();
    Image pacmanArribaImg = new ImageIcon(this.getClass().getResource("pacmanArriba.gif")).getImage();
    Image pacmanAbajoImg = new ImageIcon(this.getClass().getResource("pacmanAbajo.gif")).getImage();
    Image fantasma1Img = new ImageIcon(this.getClass().getResource("fantasma1.gif")).getImage();
    Image fantasma2Img = new ImageIcon(this.getClass().getResource("fantasma2.gif")).getImage();

    Pacman pacman;
    Fantasma fantasma1;
    Fantasma fantasma2;
    Timer timer;
    //definiendo los limites del tablero ( el cual es de tamaño 810, 600 definido en la clase snake)
    int superiorx = 780;
    int superiory = 550;
    int inferiory = 0;
    int inferiorx = 0;
    
    //variable que dice la velocidad, por defecto el profesor la dejo en 4
    int velocidad = 6;

    public Tablero() {
        setBackground(Color.BLACK);
        
//        snake1.push(new Punto(30, 30));
//        snake1.push(new Punto(40, 30));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter());

        pacman = new Pacman(30, 30, pacmanDerechaImg);
        fantasma1 = new Fantasma(50, 250, fantasma1Img);
        fantasma2 = new Fantasma(150, 450, fantasma2Img);

        timer = new Timer(50, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(pacman.imagen, pacman.x, pacman.y, this);

        g.drawImage(fantasma1.imagen, fantasma1.x, fantasma1.y, this);
        g.drawImage(fantasma2.imagen, fantasma2.x, fantasma2.y, this);

    }

    public void mover() {
        if (pacman.moverArriba) {
            if (pacman.y-velocidad>=0) {
                pacman.y -= velocidad;
            }
            else{
                pacman.y=inferiory;
                pacman.moverArriba=false;
            }
            
        }
        if (pacman.moverAbajo) {
            if (pacman.y+velocidad<=superiory) {
                pacman.y += velocidad;
            }
            else{
                pacman.y=superiory;
                pacman.moverAbajo=false;
            }
        }
        if (pacman.moverDerecha) {
            if (pacman.x+velocidad<=superiorx) {
                pacman.x += velocidad;
            }
            else{
                pacman.x=superiorx;
                pacman.moverDerecha=false;
            }
        }
        if (pacman.moverIzquierda) {
            if (pacman.x-velocidad>=0) {
                pacman.x -= velocidad;
            }
            else{
                pacman.x=inferiorx;
                pacman.moverIzquierda=false;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mover();
        repaint();
    }

    private class KeyAdapter extends java.awt.event.KeyAdapter {

        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    pacman.moverArriba = true;
                    pacman.moverAbajo = false;
                    pacman.moverDerecha = false;
                    pacman.moverIzquierda = false;
                    pacman.imagen = pacmanArribaImg;
                    break;
                case KeyEvent.VK_DOWN:
                    pacman.moverArriba = false;
                    pacman.moverAbajo = true;
                    pacman.moverDerecha = false;
                    pacman.moverIzquierda = false;
                    pacman.imagen = pacmanAbajoImg;
                    break;
                case KeyEvent.VK_RIGHT:
                    pacman.moverArriba = false;
                    pacman.moverAbajo = false;
                    pacman.moverDerecha = true;
                    pacman.moverIzquierda = false;
                    pacman.imagen = pacmanDerechaImg;
                    break;
                case KeyEvent.VK_LEFT:
                    pacman.moverArriba = false;
                    pacman.moverAbajo = false;
                    pacman.moverDerecha = false;
                    pacman.moverIzquierda = true;
                    pacman.imagen = pacmanIzquierdaImg;
                    break;
                default:
                    break;
            }

        }
    }
}

class Fantasma {

    int x;
    int y;
    Image imagen;

    public Fantasma(int x, int y, Image imagen) {
        this.x = x;
        this.y = y;
        this.imagen = imagen;
    }

}

class Pacman {

    int x;
    int y;
    Image imagen;
    boolean moverArriba = false;
    boolean moverAbajo = false;
    boolean moverDerecha = false;
    boolean moverIzquierda = false;

    public Pacman(int x, int y, Image imagen) {
        this.x = x;
        this.y = y;
        this.imagen = imagen;
    }

}
