/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

/**
 *
 * @author samf
 */
public class MimicAction extends ZenoAction {
   
     MimicAction(Attachment parent, String type, short id, long duration) {
        super(parent, type, id, duration);
     }
    void commence() {
        MyUserRecord userRec = parent.currentVisitors.get(id);
        parent.masterThread = new MasterThread(userRec, parent.myRobot);
        MimicThread mimicThread = new MimicThread();
        HeadTrackThread headTrackThread = new HeadTrackThread();
        parent.masterThread.add(mimicThread);
        parent.masterThread.add(headTrackThread);
        parent.masterThread.start(); 
        parent.positionPanel.setText("Mimicking user "+df.format(timeRemaining())+ " "+id);
        parent.positionPanel.repaint();
    }
    void conclude() {
       parent.positionPanel.setText("Stop mimicking user "+id);
        parent.positionPanel.repaint();
   
        
    }
}
