/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author Usuario
 */
public class Snake extends JFrame {
    int x = 810-15;
    int y = 600-15-10;
    public Snake() {

        add(new Tablero(x,y));

        setResizable(false);
            setSize(new Dimension(x, y));
        setTitle("PAC-MAN");
        setLocationRelativeTo(null);
        //Dimension o = this.getSize();
        //System.out.println("alto"+o.height);
        //System.out.println("largo"+o.width);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
