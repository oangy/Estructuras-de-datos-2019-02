
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
public class Jugador {
    Pacman pacman;
    int numeroJugador;
    int puntaje;
    int numeroVidas;
    Tablero tablero;
    public Jugador(int numero,String color,Tablero tablero){
        this.numeroJugador=numero;
        this.pacman=new Pacman(color,tablero);
        this.numeroVidas=3;
        this.puntaje=0;
        this.tablero = tablero;
    }
    public void morir(){
        this.numeroVidas--;
        if(numeroVidas>0){
            this.pacman.spawn();
        }
        else{
            tablero.finalizarPartida();
        }
    }
    
}
