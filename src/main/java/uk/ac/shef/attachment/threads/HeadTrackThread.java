/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.threads;

import com.primesense.nite.JointType;
import com.primesense.nite.SkeletonState;
import com.primesense.nite.UserData;
import javax.vecmath.Point3f;
import org.robokind.api.motion.Joint;
import org.robokind.api.motion.Robot.JointId;
import uk.ac.shef.attachment.utils.VectorCalc;
import static org.robokind.client.basic.RobotJoints.*;
import uk.ac.shef.attachment.MyUserRecord;

/**
 *
 * @author samf
 */
public class HeadTrackThread extends ServantThread {

    VectorCalc vc;
    JointId neck_yaw;
    JointId neck_pitch;
    long startTime;
   // GET MEASURING TAPE to measure exactly!
    public HeadTrackThread() {
        vc = new VectorCalc();
        
        
        // need to init neck yaw...
    }
    
    @Override
    public void setMaster(MasterThread master) {
        startTime = System.currentTimeMillis();
        super.setMaster(master);
        if (master.parent.robotActive) {
            neck_yaw = new JointId(master.parent.myRobot.getRobotId(), new Joint.Id(NECK_YAW));
            neck_pitch = new JointId(master.parent.myRobot.getRobotId(), new Joint.Id(NECK_PITCH));
        }
     
    }

    public void runChecked() {
        MyUserRecord userRec = master.parent.currentVisitors.get(master.userRec.userData.getId());
        UserData user = userRec.userData;
        long elapsed = System.currentTimeMillis() - startTime;
        JointType targetJoint;
        if (elapsed < 3000) {
            targetJoint = JointType.HEAD;
        }
        else if (elapsed < 6000) {
            targetJoint = JointType.LEFT_HAND;
        }
        else {
            targetJoint = JointType.RIGHT_HAND;
        }
        if (user.getSkeleton().getState() == SkeletonState.TRACKED) {
            Point3f target = vc.convertPoint(user.getSkeleton().getJoint(targetJoint).getPosition());
            Point3f orig = zenoPos;
            target.add(zenoPos);
            Point3f center_head = new Point3f(0, 270, 2100);
            center_head.add(zenoPos);
            Point3f center_feet = new Point3f(0, -270, 2100);
            center_feet.add(zenoPos);
            float yawA, pitchA;
            yawA = vc.planeAngle(orig, center_head, center_feet, orig, target);
            master.parent.positionPanel.setVar("neck-yaw", yawA);
            
            yawA = (float) Math.cos(yawA) * 0.75f + 0.5f;
            setPosition(neck_yaw, yawA);
            
            Point3f zeno_head = zenoPos;
            Point3f zeno_level = new Point3f(100, 0, 0);
            zeno_level.add(zenoPos);
            Point3f zeno_level2 = new Point3f(0, 0, 100);
            zeno_level2.add(zenoPos);
            pitchA = vc.planeAngle(zeno_head, zeno_level, zeno_level2, zeno_head, target);
            //positionLabel.setText("pitch "+df.format(pitchA) + " yaw "+df.format(yawA));
            master.parent.positionPanel.setVar("neck-pitch", pitchA);
            pitchA = 0.8f - (float) Math.cos(pitchA);
            
            
            setPosition(neck_pitch, pitchA);


        }
    }
}
