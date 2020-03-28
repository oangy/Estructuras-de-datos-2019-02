
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
import java.util.Timer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tablero extends JPanel implements ActionListener {

    private Boolean partidaFinalizada = false;

    //mapa 27x20 = 540 puntos con atributos de obstaculo,caminable,moneda
    Punto[][] coordenadas;

    //arreglo de obstaculos
    Obstaculo[] obstaculos;

    Scanner entrada = new Scanner(System.in);

    Jugador[] jugadores;

    Fantasma[] fantasmas;

    Punto[] spawnsFantasmas;

    Timer timer;
    //tiempos en ms
    int timepoPartida = 0;
    int tiempoConPoder = 0;
    Boolean conPoder = false;

    //contador global de actualizaciones de dibujo que inicia en 0 y se reinicia en 150 (utilizado para el movimiento aleatorio de los fantasmas)
    int frames;

    int velocidadFantasmas;
    int velocidadPacmans;

    int limiteInferiorFantasmas;
    int limiteSuperiorFantasmas;
    int numeroFantasmas;
    int numeroFantasmasMuertos = 0;
    int limiteInferiorBolasPoder;
    int limiteSuperiorBolasPoder;

    int superiorx;
    int superiory;
    int inferiory = 0;
    int inferiorx = 0;

    int numCoordenadasX;
    int numCoordenadasY;

    int comida;
    int numeroBolasPoder;

    int numeroPoderesActivados = 0;

    Image imagenFantasma1;
    Image imagenFantasma2;

    public Tablero(int x, int y) {
        timer = new Timer();
        superiorx = x - 30 - 10;
        superiory = y - 30 - 15 - 15;
        numCoordenadasX = x / 30;
        numCoordenadasY = y / 30;
        coordenadas = new Punto[numCoordenadasX][numCoordenadasY];
        velocidadPacmans = 4;
        velocidadFantasmas = 4;

        imagenFantasma1 = new ImageIcon(this.getClass().getResource("/imagenes/fantasmas/fantasma1.gif")).getImage();
        imagenFantasma2 = new ImageIcon(this.getClass().getResource("/imagenes/fantasmas/fantasma2.gif")).getImage();

        initialSpawn();
        controlador();
        setFocusable(true);
        requestFocusInWindow();

    }
//------------------------------------------------------------------------------preguntando parametros iniciales y generando todo el mapa-----------------------------------------------------------------

    public void initialSpawn() {
        //preguntando numero de jugadores
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
        
        //preguntando colores
        String color1L = "amarillo";
        String color2L = "amarillo";
        while (true) {
            System.out.println("Elije entre estos colores JUGADOR 1:\n1=amarillo\n2=cafe\n3=rosado");// \n es un codigo para dar un enter en el string
            color1 = entrada.nextInt();
            if (color1 == 1 || color1 == 2 || color1 == 3) {
                switch (color1) {
                    case 1:
                        color1L = "amarillo";
                        break;
                    case 2:
                        color1L = "cafe";
                        break;
                    case 3:
                        color1L = "rosado";
                        break;
                    default:
                        break;
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
                    switch (color2) {
                        case 1:
                            color2L = "azul";
                            break;
                        case 2:
                            color2L = "rojo";
                            break;
                        case 3:
                            color2L = "verde";
                            break;
                        default:
                            break;
                    }
                    break;
                } else {
                    System.out.println("SOLO HAY DISPONIBLES ESOS COLORES PARA EL JUGADOR 2  ");
                }
            }
        }
//----------------------------------------------------------------generando mapa-------------------------------------------------------------------------------------------------

        setBackground(Color.BLACK);
        comida = 0;
        //-----------------------------------generando todos los puntos como galletas
        for (int i = 0; i < numCoordenadasX; i++) {
            for (int j = 0; j < numCoordenadasY; j++) {
                Punto coordenada = new Punto(i * 30, j * 30);
                coordenada.galleta = true;
                coordenadas[i][j] = coordenada;
                comida++;
            }
        }
        //---------------------------------------------------- creando obstaculos
        System.out.println("Ingrese la cantidad de obstaculos que desea: ");
        int numeroObstaculos = 0;
        obstaculos = new Obstaculo[numeroObstaculos];
        numeroObstaculos = entrada.nextInt();
        if (numeroObstaculos > 0) {
            obstaculos = new Obstaculo[numeroObstaculos];
            System.out.println("Las celdas inician en 1 en la esquina superior izquierda y\n"
                    + "aumentan positivamente hacia la derecha y hacia abajo");
            for (int i = 0; i < numeroObstaculos; i++) {
                System.out.println("Obstaculo " + i + 1 + ": ");
                System.out.println("Ingrese la x de la coordenada inicial: ");
                int xInicial = entrada.nextInt();
                System.out.println("Ingrese la y de la coordenada inicial: ");
                int yInicial = entrada.nextInt();
                System.out.println("Ingrese la x de la coordenada final: ");
                int xFinal = entrada.nextInt();
                System.out.println("Ingrese la y de la coordenada final: ");
                int yFinal = entrada.nextInt();

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
                Punto ini = new Punto(xMenor * 30, yMenor * 30);
                Punto fi = new Punto(xMayor * 30, yMayor * 30);
                Obstaculo obs = new Obstaculo(ini, fi);
                obstaculos[i] = obs;

                //cambiando las galletas a puntos vacios
                for (int j = xMenor; j <= xMayor; j++) {
                    for (int k = yMenor; k <= yMayor; k++) {
                        Punto coordenada = coordenadas[j][k];
                        coordenada.caminable = false;
                        coordenada.galleta = false;
                        coordenada.obstaculo = true;
                        comida--;
                    }
                }

            }
        }
        //creando jugadores
        jugadores[0] = new Jugador(1, color1L, this);
        if (numeroJugadores == 2) {
            jugadores[1] = new Jugador(2, color2L, this);
        }
        //aniadiendo listeners de controles para los jugadores
        addKeyListener(new listenerJugador1());
        if (numeroJugadores == 2) {
            addKeyListener(new listenerJugador2());
        }
        //generando spawns de fantasmas
        spawnsFantasmas = new Punto[2];
        int xAux = ((int) (Math.random() * (this.numCoordenadasX - 0 + 1) + 0)) * 30;
        int yAux = ((int) (Math.random() * (this.numCoordenadasY - 0 + 1) + 0)) * 30;
        this.spawnsFantasmas[0] = new Punto(xAux, yAux);
        xAux = ((int) (Math.random() * (this.numCoordenadasX - 0 + 1) + 0)) * 30;
        yAux = ((int) (Math.random() * (this.numCoordenadasY - 0 + 1) + 0)) * 30;
        this.spawnsFantasmas[1] = new Punto(xAux, yAux);

        System.out.println("Que rango de FANTASMAS deben de aparecer en el mapa?");
        System.out.println("Escriba limite inferior del rango:");
        this.limiteInferiorFantasmas = entrada.nextInt();
        System.out.println("Escriba limite superior del rango:");
        this.limiteSuperiorFantasmas = entrada.nextInt();
        //generando fantasmas
        this.numeroFantasmas = ((int) (Math.random() * (limiteSuperiorFantasmas - limiteInferiorFantasmas + 1) + limiteInferiorFantasmas));
        fantasmas = new Fantasma[numeroFantasmas];
        for (int i = 0; i < numeroFantasmas; i++) {
            fantasmas[i] = new Fantasma(this);
        }

        System.out.println("Que rango de BOLAS DE PODER deben de aparecer en el mapa?");
        System.out.println("Escriba limite inferior del rango:");
        this.limiteInferiorBolasPoder = entrada.nextInt();
        System.out.println("Escriba limite superior del rango:");
        this.limiteSuperiorBolasPoder = entrada.nextInt();
        //generando bolas de poder
        this.numeroBolasPoder = ((int) (Math.random() * (limiteSuperiorBolasPoder - limiteInferiorBolasPoder + 1) + limiteInferiorBolasPoder)) - 1;
        int generadas = 0;
        while (generadas <= this.numeroBolasPoder) {
            int limiteSuperiorRangoX = coordenadas.length - 1;
            int limiteSuperiorRangoY = (coordenadas[0]).length - 1;
            int coordenadaRandomX = ((int) (Math.random() * (limiteSuperiorRangoX - 0 + 1) + 0));
            int coordenadaRandomY = ((int) (Math.random() * (limiteSuperiorRangoY - 0 + 1) + 0));
            Punto punto = coordenadas[coordenadaRandomX][coordenadaRandomY];
            if (punto.galleta) {
                punto.galleta = false;
                punto.bolaDePoder = true;
                generadas++;
            }
        }

    }

    //-----------------------------calculando todos los movimientos y dibujandolos--------------------------------------
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!partidaFinalizada) {
            //verificando colisiones
            for (int i = 0; i < jugadores.length; i++) {
                Jugador jugador = jugadores[i];
                Pacman pacman = jugador.pacman;

                for (int j = pacman.x; j < pacman.x + pacman.ancho; j++) {
                    for (int k = pacman.y; k < pacman.y + pacman.alto; k++) {
                        //buscando colisiones con galletas  y bolas de poder
                        if ((j % 30 == 0) && (k % 30 == 0)) {
                            if (k / 30 < 18) {
                                Punto punto = coordenadas[j / 30][k / 30];
                                if (punto.galleta) {
                                    jugador.puntaje = jugador.puntaje + punto.puntaje;
                                    punto.galleta = false;
                                    comida--;
                                } else if (punto.bolaDePoder) {
                                    jugador.puntaje = jugador.puntaje + punto.puntaje;
                                    punto.bolaDePoder = false;
                                    comida--;

                                    if (!conPoder) {
                                        this.velocidadFantasmas = this.velocidadFantasmas / 2;
                                        this.velocidadPacmans = this.velocidadPacmans * 2;
                                    }
                                    conPoder = true;

                                    //vomitando las galletas de los rivales
                                    int puntajePorGalleta = (coordenadas[0][0]).puntaje;
                                    for (int l = 0; l < jugadores.length; l++) {
                                        Jugador jugadorAux = jugadores[l];
                                        if (l != i) {
                                            int numeroGalletasJugador = (int) Math.floor((double) (jugadorAux.puntaje / puntajePorGalleta));
                                            if (numeroGalletasJugador > 5) {
                                                numeroGalletasJugador = 5;
                                            }
                                            for (int m = 0; m < numeroGalletasJugador; m++) {
                                                int limiteSuperiorRangoX = coordenadas.length - 1;
                                                int limiteSuperiorRangoY = (coordenadas[0]).length - 1;
                                                int coordenadaRandomX = ((int) (Math.random() * (limiteSuperiorRangoX - 0 + 1) + 0));
                                                int coordenadaRandomY = ((int) (Math.random() * (limiteSuperiorRangoY - 0 + 1) + 0));
                                                Punto puntoAux = coordenadas[coordenadaRandomX][coordenadaRandomY];
                                                if (!puntoAux.galleta && !puntoAux.bolaDePoder && !puntoAux.obstaculo) {
                                                    puntoAux.galleta = true;
                                                    puntoAux.bolaDePoder = false;
                                                    comida++;
                                                }
                                            }
                                            jugadorAux.puntaje = jugadorAux.puntaje - puntajePorGalleta;

                                        }
                                    }
                                }
                            }

                        }

                    }
                }
                //buscando colisiones con fantasmas
                Boolean muerto = false;
                for (int l = 0; l < fantasmas.length; l++) {
                    if (muerto) {
                        break;
                    }
                    Fantasma fantasma = fantasmas[l];

                    int xMenor = fantasma.x;

                    int yMenor = fantasma.y;

                    int xMayor = xMenor + fantasma.ancho;

                    int yMayor = yMenor + fantasma.alto;

                    for (int m = pacman.x; m <= pacman.x + pacman.ancho; m++) {
                        if (muerto) {
                            break;
                        }
                        for (int n = pacman.y; n <= pacman.y + pacman.alto; n++) {
                            if (muerto) {
                                break;
                            }
                            if ((m >= xMenor - 1) && (n >= yMenor - 1)) {
                                if ((m <= xMayor) && (n <= yMayor)) {
                                    if (conPoder) {
                                        //matando al fantasma (teleportandolo a un lugar fuera del mapa)
                                        fantasma.x = this.superiorx + 100;
                                        fantasma.y = this.superiory + numeroFantasmasMuertos + 30;
                                        fantasma.vivo = false;
                                        jugador.numeroFantasmasAsesinadosFriamente++;
                                        if (jugador.numeroFantasmasAsesinadosFriamente%3==0) {
                                            System.out.println("Jugador "+jugador.numeroJugador+" ganaste un nuevo pacman.");
                                            jugador.numeroVidas++;
                                        }
                                    } else {
                                        jugador.morir();
                                    }

                                    muerto = true;
                                    break;
                                }
                            }
                        }
                    }

                }
            }
            //dibujando Galletas
            g.setColor(Color.ORANGE);
            for (int i = 0; i < numCoordenadasX; i++) {
                for (int j = 0; j < numCoordenadasY; j++) {
                    Punto coordenada = coordenadas[i][j];
                    //viendo que tipo de punto es
                    if (coordenada.galleta) {
                        //descomentar si se quiere la imagen de galleta
                        //g.drawImage(coordenada.imagen, coordenada.x, coordenada.y,15,15, this);
                        g.fillOval(coordenada.x, coordenada.y, 10, 10);
                    } else if (coordenada.bolaDePoder) {
                        //descomentar si se quiere la imagen de galleta
                        //g.drawImage(coordenada.imagen, coordenada.x, coordenada.y,15,15, this);
                        g.fillOval(coordenada.x, coordenada.y, 20, 20);
                    }
                }
            }
            //dibujando bordes de los obstaculos
            for (int i = 0; i < obstaculos.length; i++) {
                int xMenor = (obstaculos[i]).inicio.x;
                int yMenor = (obstaculos[i]).inicio.y;
                int xMayor = (obstaculos[i]).fin.x + 25;
                int yMayor = (obstaculos[i]).fin.y + 25;
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
                if (conPoder) {
                    g.drawImage(imagenFantasma2, fantasma.x, fantasma.y, this);
                } else {
                    g.drawImage(imagenFantasma1, fantasma.x, fantasma.y, this);
                }

            }
            //contabilizando la cantidad de galletas
            if (comida == 0) {
                finalizarPartida();
            }
            //contabilizando los frames de actualizacion
            if (this.frames == 150) {
                this.frames = 0;
            } else {
                this.frames++;
            }

        } else {
            String mensaje = "Partida finalizada.";
            //eligiendo al ganador
            int ganador = 0;
            if (jugadores.length == 2) {
                if (comida == 0) {
                    if (jugadores[0].puntaje < jugadores[1].puntaje) {
                        ganador = 1;
                    }
                } else {
                    if (jugadores[0].numeroVidas == 0) {
                        ganador = 1;
                    }
                }
                mensaje = "Partida finalizada:\n El ganador fue: " + jugadores[ganador].numeroJugador + "\nCon un puntaje de: " + jugadores[ganador].puntaje;

            } else {
                mensaje = "Partida finalizada:\n El puntaje objtenido fue: " + jugadores[ganador].puntaje;
            }

            g.drawString(mensaje, 810 / 2, 300);
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
    //----------------------------------------------finalizar partida---------------------------------------------------------------------------

    public void finalizarPartida() {
        partidaFinalizada = true;
    }

    //-----------------------------------------------controlador de tiempo----------------------------------------------------------------------
    public void controlador() {
        TimerTask task = new TimerTask() {

            public void run() {
                timepoPartida += 1000;
                if (conPoder) {
                    if (tiempoConPoder == 8000) {

                        velocidadFantasmas = velocidadFantasmas * 2;
                        velocidadPacmans = velocidadPacmans / 2;

                        //colocando el juego en estado sin poder
                        conPoder = false;
                        tiempoConPoder = 0;

                    } else {
                        tiempoConPoder += 10;
                    }
                }
            }
        };

        //ejecutando tarea de poder
        timer.scheduleAtFixedRate(task, 0, 10);
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
