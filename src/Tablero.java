
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Tablero extends JPanel implements ActionListener {

    Scanner entrada = new Scanner(System.in);

    Image fantasma1Img = new ImageIcon(this.getClass().getResource("/imagenes/fantasmas/fantasma1.gif")).getImage();
    Image fantasma2Img = new ImageIcon(this.getClass().getResource("/imagenes/fantasmas/fantasma2.gif")).getImage();

    Jugador[] jugadores;
    Fantasma fantasma1;
    Fantasma fantasma2;
    Timer timer;
    //definiendo los limites del tablero ( el cual es de tamaño 810, 600 definido en la clase snake)
    int superiorx = 780;
    int superiory = 550;
    int inferiory = 0;
    int inferiorx = 0;

    public Tablero() {

        //preguntando los paramentros iniciales
        int numeroJugadores = 1;
        while (true) {
            System.out.println("Ingrese la cantidad de jugadores(1-2)");
            numeroJugadores = entrada.nextInt();
            if (numeroJugadores == 1 || numeroJugadores == 2) {
                break;
            } else {
                System.out.println("HEYY SOLO PUEDEN SER 1 O 2 JUGADORES");
            }
        }
        //creamos el arreglo de los jugadores
        this.jugadores = new Jugador[numeroJugadores];
        int color1, color2;

        String color1L = "amarillo";
        String color2L = "amarillo";
        while (true) {
            System.out.println("Elije entre estos colores JUGADOR 1:\n1=amarillo\n2=cafe\n3=rosado");// \n es un codigo para dar un enter en el sstring
            color1 = entrada.nextInt();
            if (color1 == 1 || color1 == 2 || color1 == 3) {
                if (color1 == 1) {
                    color1L = "amarillo";
                } else if (color1 == 2) {
                    color1L = "cafe";
                } else if (color1 == 3) {
                    color1L = "rosado";
                }
                break;
            } else {
                System.out.println("SOLO HAY ESOS COLORES PARA EL JUGADOR 1  Y ACEPTE SU DESTINO");
            }
        }
        if (numeroJugadores == 2) {
            while (true) {
                System.out.println("Elije entre estos colores JUGADOR 2:\n1=azul\n2=rojo\n3=verde");// \n es un codigo para dar un enter en el sstring
                color2 = entrada.nextInt();
                if (color2 == 1 || color2 == 2 || color2 == 3) {
                    if (color2 == 1) {
                        color2L = "azul";
                    } else if (color2 == 2) {
                        color2L = "rojo";
                    } else if (color2 == 3) {
                        color2L = "verde";
                    }
                    break;
                } else {
                    System.out.println("SOLO HAY ESOS COLORES PARA EL JUGADOR 2  Y ACEPTE SU DESTINO, MALDITO DEL SEGUNDO CONTROL");
                }
            }
        }

        //creando jugadores
        jugadores[0] = new Jugador(1, 30, 30, color1L, this);
        if (numeroJugadores == 2) {
            jugadores[1] = new Jugador(2, 60, 60, color2L, this);
        }

        setBackground(Color.BLACK);

//        snake1.push(new Punto(30, 30));
//        snake1.push(new Punto(40, 30));
        //aniadiendo listeners de controles para los jugadores
        addKeyListener(new listenerJugador1());
        if (numeroJugadores == 2) {
            addKeyListener(new listenerJugador2());
        }

        setFocusable(true);
        requestFocusInWindow();

        fantasma1 = new Fantasma(50, 250, fantasma1Img);
        fantasma2 = new Fantasma(150, 450, fantasma2Img);

        timer = new Timer(50, this);
        timer.start();
    }

    //aqui se calculan todos los movimientos y se repinta
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //recorriendo todos los pacmans moviendolos y dibujandolos
        for (int i = 0; i < jugadores.length; i++) {
            Pacman pacman = jugadores[i].pacman;
            pacman.mover();
            g.drawImage(pacman.imagen, pacman.x, pacman.y, this);
        }

        g.drawImage(fantasma1.imagen, fantasma1.x, fantasma1.y, this);
        g.drawImage(fantasma2.imagen, fantasma2.x, fantasma2.y, this);

    }

    public void actualizarTablero() {
        //moviendo los pacmans

        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actualizarTablero();

    }

    private class listenerJugador1 extends java.awt.event.KeyAdapter {

        Pacman pacman = jugadores[0].pacman;

        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    pacman.moverArriba = true;
                    pacman.moverAbajo = false;
                    pacman.moverDerecha = false;
                    pacman.moverIzquierda = false;
                    pacman.imagen = new ImageIcon(this.getClass().getResource("/imagenes/pacman/" + pacman.color + "/pacman-arriba.gif")).getImage();
                    break;
                case KeyEvent.VK_DOWN:
                    pacman.moverArriba = false;
                    pacman.moverAbajo = true;
                    pacman.moverDerecha = false;
                    pacman.moverIzquierda = false;
                    pacman.imagen = new ImageIcon(this.getClass().getResource("/imagenes/pacman/" + pacman.color + "/pacman-abajo.gif")).getImage();
                    break;
                case KeyEvent.VK_RIGHT:
                    pacman.moverArriba = false;
                    pacman.moverAbajo = false;
                    pacman.moverDerecha = true;
                    pacman.moverIzquierda = false;
                    pacman.imagen = new ImageIcon(this.getClass().getResource("/imagenes/pacman/" + pacman.color + "/pacman-derecha.gif")).getImage();
                    break;
                case KeyEvent.VK_LEFT:
                    pacman.moverArriba = false;
                    pacman.moverAbajo = false;
                    pacman.moverDerecha = false;
                    pacman.moverIzquierda = true;
                    pacman.imagen = new ImageIcon(this.getClass().getResource("/imagenes/pacman/" + pacman.color + "/pacman-izquierda.gif")).getImage();
                    break;
                default:
                    break;
            }

        }
    }

    private class listenerJugador2 extends java.awt.event.KeyAdapter {

        Pacman pacman = jugadores[1].pacman;

        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    pacman.moverArriba = true;
                    pacman.moverAbajo = false;
                    pacman.moverDerecha = false;
                    pacman.moverIzquierda = false;
                    pacman.imagen = new ImageIcon(this.getClass().getResource("/imagenes/pacman/" + pacman.color + "/pacman-arriba.gif")).getImage();
                    break;
                case KeyEvent.VK_S:
                    pacman.moverArriba = false;
                    pacman.moverAbajo = true;
                    pacman.moverDerecha = false;
                    pacman.moverIzquierda = false;
                    pacman.imagen = new ImageIcon(this.getClass().getResource("/imagenes/pacman/" + pacman.color + "/pacman-abajo.gif")).getImage();
                    break;
                case KeyEvent.VK_D:
                    pacman.moverArriba = false;
                    pacman.moverAbajo = false;
                    pacman.moverDerecha = true;
                    pacman.moverIzquierda = false;
                    pacman.imagen = new ImageIcon(this.getClass().getResource("/imagenes/pacman/" + pacman.color + "/pacman-derecha.gif")).getImage();
                    break;
                case KeyEvent.VK_A:
                    pacman.moverArriba = false;
                    pacman.moverAbajo = false;
                    pacman.moverDerecha = false;
                    pacman.moverIzquierda = true;
                    pacman.imagen = new ImageIcon(this.getClass().getResource("/imagenes/pacman/" + pacman.color + "/pacman-izquierda.gif")).getImage();
                    break;
                default:
                    break;
            }

        }
    }
}
