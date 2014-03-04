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
        //System.out.println("Hello action commenced");
        //playSound("eh-oh");
        
        
        
        try {
            Animation anim = Robokind.loadAnimation("animations/eh-oh-2.xml");
            MyUserRecord userRec;
            userRec = parent.currentVisitors.get(id);
            HeadTrackThread headTrackThread = new HeadTrackThread(parent, userRec);
            masterThread.add(headTrackThread);
            masterThread.start(); 
            if (parent.robotActive) {
                parent.myPlayer.playAnimation(anim);
                playSound("eh-oh");
                Thread.sleep(1600);
                playSound("eh-oh");
            }
            parent.getPositionPanel().setText("Greetings visitor "+id);
            parent.getPositionPanel().setTimer(parent.et.currentTaskEndTime);
            parent.needsToMove = true;
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void conclude() {
        if (parent.robotActive) {
            masterThread.end();
            parent.needsToMove = false;
        }
        parent.getPositionPanel().setText("Finished greeting visitor "+id);
        parent.getPositionPanel().repaint();
    }
}
