/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.threads;

import org.robokind.api.motion.Joint;
import org.robokind.api.motion.Robot.JointId;
import static org.robokind.client.basic.RobotJoints.*;
import uk.ac.shef.attachment.Attachment;
import uk.ac.shef.attachment.MyUserRecord;

/**
 *
 * @author samf
 */
public class BlinkThread extends ServantThread {
   JointId eyelids;
   long lastBlink;
   long lastStart;
   int state;
   static final int START = 0;
   static final int CLOSE_EYES = 1;
   
   public BlinkThread(Attachment parent, MyUserRecord userRec) {
        super(parent, userRec);
        if (parent.robotActive) {
          eyelids = new JointId(parent.myRobot.getRobotId(), new Joint.Id(EYELIDS));
                  
         }
            state = START;     // need to init neck yaw...
    }
   
   public void runChecked() {
       long now = System.currentTimeMillis();
       if (now-lastBlink>3000) {
           if (state==START) {
               state = CLOSE_EYES;
               setPosition(eyelids, 0.0f);
               lastStart = now;
           }
           else if (state==CLOSE_EYES && now-lastStart>200) {
               state = START;
               setPosition(eyelids, 1.0f);
               lastBlink = now;
           }
       }
   }

   
}
