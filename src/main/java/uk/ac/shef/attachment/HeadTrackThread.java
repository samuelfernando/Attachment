/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

import com.primesense.nite.JointType;
import com.primesense.nite.SkeletonState;
import com.primesense.nite.UserData;
import javax.vecmath.Point3f;
import org.robokind.api.common.position.NormalizedDouble;
import org.robokind.api.motion.Joint;
import org.robokind.api.motion.Robot.JointId;
import org.robokind.api.motion.Robot.RobotPositionHashMap;
import org.robokind.api.motion.messaging.RemoteRobot;
import org.robokind.client.basic.RobotJoints;

/**
 *
 * @author samf
 */
public class HeadTrackThread extends Thread {
    MyUserRecord userRec;
    VectorCalc vc;
    JointId neck_yaw; 
        JointId neck_pitch;
    RemoteRobot myRobot;
        RobotPositionHashMap myGoalPositions;
        boolean shouldRun;
        MasterThread masterThread;
        public HeadTrackThread(MasterThread masterThread) {
            this.masterThread = masterThread;
            
        }
    public HeadTrackThread(MyUserRecord userRec, RemoteRobot myRobot) {
        this.userRec = userRec;
        this.myRobot = myRobot;
        neck_yaw = new JointId(myRobot.getRobotId(), new Joint.Id(RobotJoints.NECK_YAW));
        neck_pitch = new JointId(myRobot.getRobotId(), new Joint.Id(RobotJoints.NECK_PITCH));
        vc = new VectorCalc();
        this.shouldRun = true;
    }
    
    private void setPosition(org.robokind.api.motion.Robot.JointId jointID, float val) {
        if (val < 0) {
            val = 0.0f;
        }
        if (val > 1) {
            val = 1.0f;
        }
        myGoalPositions.put(jointID, new NormalizedDouble(val));
    }
    
    public void end() {
        this.shouldRun = false;
    }
    @Override
    public void run() {
        while (shouldRun) {
              UserData user = userRec.userData;
              if (user.getSkeleton().getState() == SkeletonState.TRACKED) {

                Point3f head = vc.convertPoint(user.getSkeleton().getJoint(JointType.HEAD).getPosition());

                Point3f orig = new Point3f(0, 0, 0);

                Point3f center_head = new Point3f(0, 270, 2100);
                Point3f center_feet = new Point3f(0, -270, 2100);
                float yawA, pitchA;
                float angle;
                yawA = angle = vc.planeAngle(orig, center_head, center_feet, orig, head);
                angle = (float) Math.cos(angle) * 0.7f + 0.6f;
                setPosition(neck_yaw, angle);
                Point3f zeno_head = new Point3f(0, -100, 0);
                Point3f zeno_level = new Point3f(100, -100, 0);
                Point3f zeno_level2 = new Point3f(0, -100, 100);

                pitchA = angle = vc.planeAngle(zeno_head, zeno_level, zeno_level2, zeno_head, head);
                //positionLabel.setText("pitch "+df.format(pitchA) + " yaw "+df.format(yawA));
                angle = 0.75f - (float) Math.cos(angle);

                setPosition(neck_pitch, angle);


            }
            myRobot.move(myGoalPositions, 200);
        }
        
    }
}
