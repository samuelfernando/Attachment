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
    }
    public void commence() {
        userUpdate(id, "bye");
        playSound("bye-bye");
        parent.getPositionPanel().setText("Bye visitor "+timeRemaining() + " "+id);
        parent.getPositionPanel().repaint();
    }
    public void conclude() {
        parent.getPositionPanel().setText("Finished bye visitor "+id);
        parent.getPositionPanel().repaint();
    }
}
