/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.threads;

import org.robokind.api.common.position.NormalizedDouble;
import uk.ac.shef.attachment.MyUserRecord;

/**
 *
 * @author samf
 */
abstract class ServantThread extends Thread {
    MasterThread master;
    boolean shouldRun = false;
    void setMaster(MasterThread master) {
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
        if (master.parent.robotActive) {
            master.parent.myGoalPositions.put(jointID, new NormalizedDouble(val));
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
