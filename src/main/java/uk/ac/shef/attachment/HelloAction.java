/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

/**
 *
 * @author samf
 */
class HelloAction extends ZenoAction {
    HelloAction(Attachment parent, String type, short id, long duration) {
        super(parent, type, id, duration);
    }
    void commence() {
        //System.out.println("Hello action commenced");
        playSound("eh-oh");
        userUpdate(id, "greet");
        parent.positionPanel.setText("Greetings visitor "+timeRemaining() + " "+id); 
        parent.positionPanel.repaint();
    }
    void conclude() {
        parent.positionPanel.setText("Finished greeting visitor "+id);
        parent.positionPanel.repaint();
    }
}
