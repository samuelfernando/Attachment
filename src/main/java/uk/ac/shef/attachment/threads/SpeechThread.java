/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.threads;

import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.shef.attachment.Attachment;
import org.robokind.api.motion.Joint;
import org.robokind.api.motion.Robot.JointId;
import static org.robokind.client.basic.RobotJoints.*;
/**
 *
 * @author samf
 */
public class SpeechThread extends ServantThread {
    JointId jaw;
    boolean soundPlaying;
    boolean jawToClose;
    long soundPeak;
    float jawPosition;
    float jawVel;
     public SpeechThread(Attachment parent) {
         super(parent);
         if (parent.robotActive) {
            jaw = new JointId(parent.robotController.myRobot.getRobotId(), new Joint.Id(JAW));
         }
         soundPlaying = false;
     }
    public void update() {
        long now = System.currentTimeMillis();
        if (!soundPlaying) {
            try {
                parent.soundPlayer.playSound("neutral-wonder");
                soundPlaying = true;
               //    soundEnd = now+parent.soundPlayer.length("neutral-wonder");
                soundPeak = now+3000;
                jawVel = 0.05f;
            } catch (Exception ex) {
                Logger.getLogger(SpeechThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (soundPlaying) { 
            if (now>soundPeak) {          
                jawToClose = true;
            }
            else {
                jawPosition+=0.02f;
            }
        }
        if (jawToClose) {
            jawPosition-=0.1f;
        }
        setPosition(jaw, jawPosition);
    }
    
}
