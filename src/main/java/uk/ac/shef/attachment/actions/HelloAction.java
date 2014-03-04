/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.actions;

import uk.ac.shef.attachment.Attachment;

/**
 *
 * @author samf
 */
public class HelloAction extends ZenoAction {
    public HelloAction(Attachment parent, String type, short id, long duration) {
        super(parent, type, id, duration);
    }
    public void commence() {
        //System.out.println("Hello action commenced");
        playSound("eh-oh");
        userUpdate(id, "greet");
        parent.getPositionPanel().setText("Greetings visitor "+timeRemaining() + " "+id); 
        parent.getPositionPanel().repaint();
    }
    public void conclude() {
        parent.getPositionPanel().setText("Finished greeting visitor "+id);
        parent.getPositionPanel().repaint();
    }
}
