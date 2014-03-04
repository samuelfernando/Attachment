/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.threads;

import org.robokind.api.common.position.NormalizedDouble;
import org.robokind.api.motion.Robot.RobotPositionHashMap;
import uk.ac.shef.attachment.MyUserRecord;

/**
 *
 * @author samf
 */
abstract class ServantThread extends Thread {
    MasterThread master;
    boolean shouldRun = false;
    RobotPositionHashMap myGoalPositions;
    void setMaster(MasterThread master) {
        this.master = master;
        this.myGoalPositions = new RobotPositionHashMap();
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
        if (master.parent.robotActive) {
            System.out.println("set new goal position" + jointID + " "+ val);
            myGoalPositions.put(jointID, new NormalizedDouble(val));
            master.parent.addGoalPositions(myGoalPositions);
        }
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
