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
import uk.ac.shef.attachment.threads.MasterThread;

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
        try {
             MyUserRecord userRec;
            userRec = parent.currentVisitors.get(id);
            parent.masterThread = new MasterThread(parent, userRec);
            HeadTrackThread headTrackThread = new HeadTrackThread();
            parent.masterThread.add(headTrackThread);
            parent.masterThread.start(); 
            if (parent.robotActive) {
               Animation anim = Robokind.loadAnimation("animations/byebye.xml");
               parent.myPlayer.playAnimation(anim);
               playSound("bye-bye");
               Thread.sleep(1600);
               playSound("bye-bye");
            }
            parent.needsToMove = true;
            parent.getPositionPanel().setText("Bye visitor "+id);
            parent.getPositionPanel().setTimer(parent.et.currentTaskEndTime);
            parent.getPositionPanel().repaint();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void conclude() {
         if (parent.robotActive) {
            parent.masterThread.end();
            parent.needsToMove = false;
        }
        parent.getPositionPanel().setText("Finished bye visitor "+id);
        parent.getPositionPanel().repaint();
    }
}
