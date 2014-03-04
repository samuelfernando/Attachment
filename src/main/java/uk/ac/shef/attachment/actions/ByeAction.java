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
public class ByeAction extends ZenoAction {
    public ByeAction(Attachment parent, String type, short id, long duration) {
        super(parent, type, id, duration);
        priority = 1000;
    }
    public void commence() {
        System.out.println("bye bye");
        
        playSound("bye-bye2");
        parent.getPositionPanel().setText("Bye visitor "+id);
        parent.getPositionPanel().setTimer(parent.et.currentTaskEndTime);
        parent.getPositionPanel().repaint();
    }
    public void conclude() {
        parent.getPositionPanel().setText("Finished bye visitor "+id);
        parent.getPositionPanel().repaint();
    }
}
