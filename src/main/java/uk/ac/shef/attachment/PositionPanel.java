/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashMap;
import javax.vecmath.Vector3f;

/**
 *
 * @author samf
 */
public class PositionPanel extends Component {
    
    Vector3f vel;
    String text;
    Font font;
    long endTime;
    HashMap<String, Float> vars;
    public PositionPanel() {
        vel = new Vector3f();
        font = new Font("Serif", Font.PLAIN, 36);
        text = "waiting";
        vars = new HashMap<String, Float>();
    }
    
    public void setTimer(long time) {
        endTime = time;
    }
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0,this.getWidth(), this.getHeight());
       g.setColor(Color.white);
       g.setFont(font);
       g.drawString("vel " +vel.z, 0, 200);
       g.drawString(text, 0,250);
       
       String out = "timeRemaining = "+(endTime-System.currentTimeMillis());
       g.drawString(out, 0, 300);
       int count = 0;
       for (String key : vars.keySet()) {
           float val = vars.get(key);
           out = key+" "+val;
           g.drawString(out, 0, 350+count*50);
           ++count;
       }
        /* g.setColor(Color.white);
        
       
        if (vel.z<0) {
            g.fillRect(400+(int)vel.z, 100, (int)(-vel.z), 50);
        }
        else {
            g.fillRect(400, 100, (int)(vel.z), 50);   
        }*/
    }
    public void setText(String text) {
        this.text = text;
    }
    
    public void setVar(String key, float val) {
        vars.put(key, val);
    }

   
}
