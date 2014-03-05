/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.actions;

import uk.ac.shef.attachment.Attachment;
import uk.ac.shef.attachment.MyUserRecord;
import uk.ac.shef.attachment.threads.BlinkThread;

/**
 *
 * @author samf
 */
public class BlinkAction extends ZenoAction {
 public BlinkAction(Attachment parent, String type, short id, long duration) {
     super(parent, type, id, duration);
 }
    @Override
    public void commence() {
        MyUserRecord userRec;
        userRec = parent.currentVisitors.get(id);
        BlinkThread blinkThread = new BlinkThread(parent, userRec);
          masterThread.add(blinkThread);
        System.out.println("Blink starting");
        masterThread.start(); 
    }
    public void conclude() {
        masterThread.end();
    }
    
}
