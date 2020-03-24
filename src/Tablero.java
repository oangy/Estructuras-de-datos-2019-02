
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Deque;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Tablero extends JPanel implements ActionListener {

    //mapa 27x20 = 540 puntos con atributos de obstaculo,caminable,moneda
    Punto[][] coordenadas; 
    
    //arreglo de obstaculos
    Obstaculo[] obstaculos;


    Scanner entrada = new Scanner(System.in);

    Jugador[] jugadores;
    
    Fantasma[] fantasmas;
    
    Punto[] spawnsFantasmas;
    
    Timer timer;
    //definiendo los limites del tablero ( el cual es de tamaño 810, 600 definido en la clase snake)
//    int superiorx = 780;
//    int superiory = 550;
    
    //contador global de actualizaciones de dibujo que inicia en 0 y se reinicia en 200 (utilizado para el movimiento aleatorio de los fantasmas)
    int frames;
    
    int limiteInferiorFantasmas;
    int limiteSuperiorFantasmas;
    int numeroFantasmas;
    
    int superiorx ;
    int superiory ;
    int inferiory = 0;
    int inferiorx = 0;
    
    int numCoordenadasX;
    int numCoordenadasY;

    public Tablero(int x, int y) {
        
        superiorx= x-30-10;
        superiory= y-30-15-15 ;
        numCoordenadasX = x/30;
        numCoordenadasY = y/30;
        coordenadas= new Punto[numCoordenadasX][numCoordenadasY];
        //---------------------------------------------------preguntando los paramentros iniciales------------------------------------------------------------
        
        initialSpawn();
        setFocusable(true);
        requestFocusInWindow();


        timer = new Timer(50, this);
        timer.start();
    }
    public void initialSpawn(){
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
                System.out.println("SOLO HAY DISPONIBLES ESOS COLORES PARA EL JUGADOR 1  Y ACEPTE SU DESTINO");
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
                    System.out.println("SOLO HAY DISPONIBLES ESOS COLORES PARA EL JUGADOR 2  ");
                }
            }
        }
//----------------------------------------------------------------generando mapa-------------------------------------------------------------------------------------------------
        
        setBackground(Color.BLACK);
       //generando todos los puntos como galletas
        for (int i = 0; i < numCoordenadasX; i++) {
            for (int j = 0; j < numCoordenadasY; j++) {
                Punto coordenada = new Punto(i*30, j*30);
                coordenada.galleta = true;
                coordenadas[i][j]=coordenada;
            }
        }
        // creando obstaculos
        System.out.println("Ingrese la cantidad de obstaculos que desea: ");
        int numeroObstaculos = 0;
        obstaculos=new Obstaculo[numeroObstaculos];
        numeroObstaculos = entrada.nextInt();
        if (numeroObstaculos > 0) {
            obstaculos=new Obstaculo[numeroObstaculos];
            System.out.println("Las celdas inician en 1 en la esquina superior izquierda y\n"
                    + "aumentan positivamente hacia la derecha y hacia abajo");
            for (int i = 0; i < numeroObstaculos; i++) {
                System.out.println("Obstaculo " + i + 1 + ": ");
                System.out.println("Ingrese la x de la coordenada inicial: ");
                int xInicial = entrada.nextInt();
                System.out.println("Ingrese la y de la coordenada inicial: ");
                int yInicial = entrada.nextInt();
                System.out.println("Ingrese la x de la coordenada final: ");
                int xFinal =entrada.nextInt();
                System.out.println("Ingrese la y de la coordenada final: ");
                int yFinal =entrada.nextInt();

                //generando los rangos de pixeles en los que no se puede mover
                int xMenor = 0;
                int xMayor = 0;
                int yMenor = 0;
                int yMayor = 0;
                if (xInicial >= xFinal) {
                    xMenor = xFinal;
                    xMayor = xInicial;
                } else {
                    xMenor = xInicial;
                    xMayor = xFinal;
                }
                if (yInicial >= yFinal) {
                    yMenor = yFinal;
                    yMayor = yInicial;
                } else {
                    yMenor = yInicial;
                    yMayor = yFinal;
                }
                Punto ini = new Punto(xMenor*30,yMenor*30);
                Punto fi = new Punto(xMayor*30,yMayor*30);
                Obstaculo obs =new Obstaculo(ini,fi);
                obstaculos[i]= obs;
                                
                //cambiando las galletas a zonas no caminables
                for (int j = xMenor; j <= xMayor; j++) {
                    for (int k = yMenor; k <= yMayor; k++) {
                        Punto coordenada=coordenadas[j][k];
                        coordenada.caminable=false;
                        coordenada.galleta=false;
                        coordenada.obstaculo=true;
                    }
                }

            }
        }
        //creando jugadores
        jugadores[0] = new Jugador(1,color1L, this);
        if (numeroJugadores == 2) {
            jugadores[1] = new Jugador(2,color2L, this);
        }
        //aniadiendo listeners de controles para los jugadores
        addKeyListener(new listenerJugador1());
        if (numeroJugadores == 2) {
            addKeyListener(new listenerJugador2());
        }
        //generando spawns de fantasmas
        spawnsFantasmas = new Punto[2];
        int xAux = ((int)(Math.random()*(this.numCoordenadasX-0+1)+0))*30;
        int yAux = ((int)(Math.random()*(this.numCoordenadasY-0+1)+0))*30;
        this.spawnsFantasmas[0]=new Punto(xAux,yAux);
         xAux = ((int)(Math.random()*(this.numCoordenadasX-0+1)+0))*30;
         yAux = ((int)(Math.random()*(this.numCoordenadasY-0+1)+0))*30;
        this.spawnsFantasmas[1]=new Punto(xAux,yAux);
        
        System.out.println("Que rango de fantasmas deben de aparecer en el mapa?");
        System.out.println("Escriba limite inferior del rango:");
        this.limiteInferiorFantasmas=entrada.nextInt();
        System.out.println("Escriba limite superior del rango:");
        this.limiteSuperiorFantasmas=entrada.nextInt();
        
        this.numeroFantasmas=((int)(Math.random()*(limiteSuperiorFantasmas-limiteInferiorFantasmas+1)+limiteInferiorFantasmas));
        fantasmas = new Fantasma[numeroFantasmas];
        for (int i = 0; i < numeroFantasmas; i++) {
            fantasmas[i]=new Fantasma(this);
        }
        
        
    }

    //-----------------------------calculando todos los movimientos y dibujandolos--------------------------------------
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //dibujando coordenadas
        for (int i = 0; i < numCoordenadasX; i++) {
            for (int j = 0; j < numCoordenadasY; j++) {
                Punto coordenada = coordenadas[i][j];
                //viendo que tipo de punto es
                if (coordenada.galleta) {
                     g.drawImage(coordenada.imagen, coordenada.x, coordenada.y,15,15, this);
                }
            }
        }
        //dibujando bordes de los obstaculos
        for (int i = 0; i < obstaculos.length; i++) {
            int xMenor = (obstaculos[i]).inicio.x;
            int yMenor = (obstaculos[i]).inicio.y;
            int xMayor = (obstaculos[i]).fin.x+25;
            int yMayor = (obstaculos[i]).fin.y+25;
            g.setColor(Color.GREEN);
            //izquierda
            g.drawLine(xMenor, yMenor, xMenor, yMayor);
            //derecha
            g.drawLine(xMayor, yMenor, xMayor, yMayor);
            //arriba
            g.drawLine(xMenor, yMenor, xMayor, yMenor);
            //abajo
            g.drawLine(xMenor, yMayor, xMayor, yMayor);
        }
        //recorriendo todos los pacmans moviendolos y dibujandolos
        for (int i = 0; i < jugadores.length; i++) {
            Pacman pacman = jugadores[i].pacman;
            pacman.mover();
            g.drawImage(pacman.imagen, pacman.x, pacman.y, this);
        }
        //recorriendo todos los fantasmas moviendolos y dibujandolos
        for (int i = 0; i < numeroFantasmas; i++) {
            Fantasma fantasma = fantasmas[i];
            fantasma.mover();
            g.drawImage(fantasma.imagen, fantasma.x, fantasma.y, this);
        }
        //contabilizando los frames de actualizacion
        if (this.frames ==200) {
            this.frames = 0;
        }
        else{
            this.frames ++;
        }
        
    }

    public void actualizarTablero() {
        //actualizando todos los datos en pantalla

        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actualizarTablero();

    }
    
    

    //-------------------------------------------------------------------------controles--------------------------------------------------------
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
