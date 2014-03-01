/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import javax.vecmath.Vector3f;

/**
 *
 * @author samf
 */
public class PositionPanel extends Component {
    
    Vector3f vel;
    String text;
    Font font;
    public PositionPanel() {
        vel = new Vector3f();
        font = new Font("Serif", Font.PLAIN, 36);
        text = "waiting";
    }
    @Override
    public synchronized void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0,this.getWidth(), this.getHeight());
       g.setColor(Color.white);
       g.setFont(font);
       g.drawString(text, 400, 400);
        /* g.setColor(Color.white);
        
       
        if (vel.z<0) {
            g.fillRect(400+(int)vel.z, 100, (int)(-vel.z), 50);
        }
        else {
            g.fillRect(400, 100, (int)(vel.z), 50);   
        }*/
    }
    void setText(String text) {
        this.text = text;
    }
}
