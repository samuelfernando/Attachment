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
        priority = 100;
     }
    public void commence() {
        MyUserRecord userRec;
        userRec = parent.currentVisitors.get(id);
        parent.masterThread = new MasterThread(parent, userRec);
        MimicThread mimicThread = new MimicThread();
        HeadTrackThread headTrackThread = new HeadTrackThread();
        parent.masterThread.add(mimicThread);
        parent.masterThread.add(headTrackThread);
        parent.masterThread.start(); 
        parent.positionPanel.setText("Mimicking user  "+id);
        parent.positionPanel.setTimer(parent.et.currentTaskEndTime);
        parent.positionPanel.repaint();
    }
    public void conclude() {
       parent.positionPanel.setText("Stop mimicking user "+id);
        parent.positionPanel.repaint();
        parent.isDueToMimic.put(id, false);
        parent.masterThread.end();
        
    }
}
