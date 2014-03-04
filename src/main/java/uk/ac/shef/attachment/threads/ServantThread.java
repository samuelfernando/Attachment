/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.threads;

import javax.vecmath.Point3f;
import org.robokind.api.common.position.NormalizedDouble;
import org.robokind.api.motion.Robot.RobotPositionHashMap;
import uk.ac.shef.attachment.Attachment;
import uk.ac.shef.attachment.MyUserRecord;
import uk.ac.shef.attachment.utils.ReadConfig;
import uk.ac.shef.attachment.utils.VectorCalc;

/**
 *
 * @author samf
 */
abstract class ServantThread extends Thread {

    boolean shouldRun;
    RobotPositionHashMap myGoalPositions;
    Point3f zenoPos;
    VectorCalc vc;
    Attachment parent;
    MyUserRecord userRec;

    public ServantThread(Attachment parent, MyUserRecord userRec) {
        vc = new VectorCalc();
        this.parent = parent;
        this.userRec = userRec;
        this.myGoalPositions = new RobotPositionHashMap();
        String zenoPosStr = ReadConfig.readConfig().get("zeno-pos");
        String splits[] = zenoPosStr.split(" ");
        float x = Float.parseFloat(splits[0]);
        float y = Float.parseFloat(splits[1]);
        float z = Float.parseFloat(splits[2]);
        this.zenoPos = new Point3f(x, y, z);
        shouldRun = false;
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
        if (parent.robotActive) {
            //System.out.println("set new goal position" + jointID + " "+ val);

            parent.addGoalPosition(jointID, new NormalizedDouble(val));
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
