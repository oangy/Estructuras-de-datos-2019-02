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
    public Snake() {

        add(new Tablero());

        setResizable(false);
            setSize(new Dimension(810, 600));
        setTitle("PAC-MAN");
        setLocationRelativeTo(null);
        Dimension o = this.getSize();
        System.out.println("alto"+o.height);
        System.out.println("largo"+o.width);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
