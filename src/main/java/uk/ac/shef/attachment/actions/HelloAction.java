/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.actions;

import org.robokind.api.animation.Animation;
import org.robokind.client.basic.Robokind;
import uk.ac.shef.attachment.Attachment;
import uk.ac.shef.attachment.MyUserRecord;
import uk.ac.shef.attachment.threads.HeadTrackThread;

/**
 *
 * @author samf
 */
public class HelloAction extends ZenoAction {
    public HelloAction(Attachment parent, String type, short id, long duration) {
        super(parent, type, id, duration);
        priority = 1000;
    }
    public void commence() {
        try {
            Animation anim = Robokind.loadAnimation("animations/eh-oh-2.xml");
            parent.headTrackThread.start(id);
            parent.robotController.myPlayer.playAnimation(anim);
            parent.soundPlayer.playSound("eh-oh");
            Thread.sleep(1600);
            parent.soundPlayer.playSound("eh-oh");
           
            
            
            parent.getPositionPanel().setText("Greetings visitor "+id);
            parent.getPositionPanel().setTimer(parent.et.currentTaskEndTime);
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void conclude() {
        
        parent.getPositionPanel().setText("Finished greeting visitor "+id);
        parent.getPositionPanel().repaint();
    }
}
