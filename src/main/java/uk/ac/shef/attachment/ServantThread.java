/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

import org.robokind.api.common.position.NormalizedDouble;
import org.robokind.api.motion.Robot;
import org.robokind.api.motion.messaging.RemoteRobot;

/**
 *
 * @author samf
 */
abstract class ServantThread extends Thread {
    MasterThread master;
    boolean shouldRun = false;
    Robot.RobotPositionHashMap myGoalPositions;
    MyUserRecord userRec;
   
    public void add(MasterThread master) {
        this.master = master;
    }
    
    @Override
    public void run() {
        while (shouldRun) {
            runChecked();
        }
    }
    
     void setPosition(org.robokind.api.motion.Robot.JointId jointID, float val) {
        if (val < 0) {
            val = 0.0f;
        }
        if (val > 1) {
            val = 1.0f;
        }
        myGoalPositions.put(jointID, new NormalizedDouble(val));
    }
    abstract void runChecked();

    @Override
    public void start() {
        super.start();
        shouldRun = true;
    }
    void end() {
        shouldRun = false;
    }
    
    
}
