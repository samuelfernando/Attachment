/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

/**
 *
 * @author samf
 */
public class ByeAction extends ZenoAction {
    ByeAction(Attachment parent, String type, short id, long duration) {
        super(parent, type, id, duration);
    }
    void commence() {
        userUpdate(id, "bye");
        playSound("bye-bye");
        parent.positionPanel.setText("Bye visitor "+timeRemaining() + " "+id);
        parent.positionPanel.repaint();
    }
    void conclude() {
        parent.positionPanel.setText("Finished bye visitor "+id);
        parent.positionPanel.repaint();
    }
}
