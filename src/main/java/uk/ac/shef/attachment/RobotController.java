/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.robokind.api.animation.messaging.RemoteAnimationPlayerClient;
import org.robokind.api.common.position.NormalizedDouble;
import org.robokind.api.motion.Robot;
import org.robokind.api.motion.messaging.RemoteRobot;
import org.robokind.api.speech.messaging.RemoteSpeechServiceClient;
import org.robokind.client.basic.Robokind;
import org.robokind.client.basic.UserSettings;
import uk.ac.shef.attachment.threads.HeadTrackThread;
import uk.ac.shef.attachment.threads.MimicThread;
import uk.ac.shef.attachment.threads.SpeechThread;

/**
 *
 * @author samf
 */
public class RobotController {

    public RemoteRobot myRobot;
    Robot.RobotPositionMap myGoalPositions;
    Robot.JointId neck_yaw;
    Robot.JointId neck_pitch;
    RemoteSpeechServiceClient mySpeaker;
    public RemoteAnimationPlayerClient myPlayer;

    public RobotController(String robotIP) {
      
        String robotID = "myRobot";

        // set respective addresses


        UserSettings.setRobotId(robotID);
        UserSettings.setRobotAddress(robotIP);
        UserSettings.setSpeechAddress(robotIP);
        UserSettings.setAnimationAddress(robotIP);


        mySpeaker = Robokind.connectSpeechService();
        myPlayer = Robokind.connectAnimationPlayer();
        myRobot = Robokind.connectRobot();
        myGoalPositions = new org.robokind.api.motion.Robot.RobotPositionHashMap();
        myGoalPositions = myRobot.getDefaultPositions();
        myRobot.move(myGoalPositions, 1000);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(RobotController.class.getName()).log(Level.SEVERE, null, ex);
        }
        myGoalPositions.clear();
    }
    public void addGoalPosition(org.robokind.api.motion.Robot.JointId jointID, NormalizedDouble val) {
        myGoalPositions.put(jointID, val);
    }


    void moveGoalPositions() {
        myRobot.move(myGoalPositions, 200);
        myGoalPositions.clear();
    }
    
    void setDefaultPositions() {
        myGoalPositions = myRobot.getDefaultPositions();
        myRobot.move(myGoalPositions, 1000);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Attachment.class.getName()).log(Level.SEVERE, null, ex);
        }
        myGoalPositions.clear();
    
    }
}
