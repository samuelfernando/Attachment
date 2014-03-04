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

    public HeadTrackThread() {
        vc = new VectorCalc();
        // need to init neck yaw...
    }
    
    @Override
    public void setMaster(MasterThread master) {
        super.setMaster(master);
        if (master.parent.robotActive) {
            neck_yaw = new JointId(master.parent.myRobot.getRobotId(), new Joint.Id(NECK_YAW));
            neck_pitch = new JointId(master.parent.myRobot.getRobotId(), new Joint.Id(NECK_PITCH));
        }
     
    }

    public void runChecked() {
        MyUserRecord userRec = master.parent.currentVisitors.get(master.userRec.userData.getId());
        UserData user = userRec.userData;
        if (user.getSkeleton().getState() == SkeletonState.TRACKED) {

            Point3f head = vc.convertPoint(user.getSkeleton().getJoint(JointType.HEAD).getPosition());

            Point3f orig = new Point3f(0, 0, 0);

            Point3f center_head = new Point3f(0, 270, 2100);
            Point3f center_feet = new Point3f(0, -270, 2100);
            float yawA, pitchA;
            yawA = vc.planeAngle(orig, center_head, center_feet, orig, head);
            yawA = (float) Math.cos(yawA) * 0.7f + 0.6f;
            setPosition(neck_yaw, yawA);
            Point3f zeno_head = new Point3f(0, -100, 0);
            Point3f zeno_level = new Point3f(100, -100, 0);
            Point3f zeno_level2 = new Point3f(0, -100, 100);

            pitchA = vc.planeAngle(zeno_head, zeno_level, zeno_level2, zeno_head, head);
            //positionLabel.setText("pitch "+df.format(pitchA) + " yaw "+df.format(yawA));
            pitchA = 0.75f - (float) Math.cos(pitchA);

            setPosition(neck_pitch, pitchA);


        }
    }
}
