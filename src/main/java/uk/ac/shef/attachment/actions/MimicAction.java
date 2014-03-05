/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.actions;

import uk.ac.shef.attachment.Attachment;
import uk.ac.shef.attachment.MyUserRecord;
import uk.ac.shef.attachment.threads.HeadTrackThread;
import uk.ac.shef.attachment.threads.MimicThread;
import uk.ac.shef.attachment.threads.SpeechThread;

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
       
        parent.mimicThread.start(id);
        parent.headTrackThread.start(id);
        //masterThread.add(speechThread);
        parent.positionPanel.setText("Head Track user  "+id);
        parent.positionPanel.setTimer(parent.et.currentTaskEndTime);
        parent.positionPanel.repaint();
    }
    public void conclude() {
       parent.positionPanel.setText("Stop head tracking user "+id);
        parent.positionPanel.repaint();
        parent.isDueToMimic.put(id, false);
        parent.mimicThread.end();
        parent.headTrackThread.end();
    }
}
