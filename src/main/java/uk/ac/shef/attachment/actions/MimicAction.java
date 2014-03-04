/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.actions;

import uk.ac.shef.attachment.Attachment;
import uk.ac.shef.attachment.MyUserRecord;
import uk.ac.shef.attachment.threads.HeadTrackThread;
import uk.ac.shef.attachment.threads.MasterThread;
import uk.ac.shef.attachment.threads.MimicThread;

/**
 *
 * @author samf
 */
public class MimicAction extends ZenoAction {
   
     public MimicAction(Attachment parent, String type, short id, long duration) {
        super(parent, type, id, duration);
     }
    public void commence() {
        MyUserRecord userRec;
        userRec = parent.currentVisitors.get(id);
        parent.masterThread = new MasterThread(userRec, parent.myRobot);
        MimicThread mimicThread = new MimicThread();
        HeadTrackThread headTrackThread = new HeadTrackThread();
        parent.masterThread.add(mimicThread);
        parent.masterThread.add(headTrackThread);
        parent.masterThread.start(); 
        parent.positionPanel.setText("Mimicking user "+df.format(timeRemaining())+ " "+id);
        parent.positionPanel.repaint();
    }
    public void conclude() {
       parent.positionPanel.setText("Stop mimicking user "+id);
        parent.positionPanel.repaint();
        parent.masterThread.end();
        
    }
}
