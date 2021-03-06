/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.threads;

import com.primesense.nite.JointType;
import com.primesense.nite.Quaternion;
import com.primesense.nite.SkeletonState;
import com.primesense.nite.UserData;
import javax.vecmath.Point3f;
import org.robokind.api.motion.Joint;
import org.robokind.api.motion.Robot.JointId;
import static org.robokind.client.basic.RobotJoints.*;
import uk.ac.shef.attachment.Attachment;
import uk.ac.shef.attachment.MyUserRecord;
import uk.ac.shef.attachment.RobotController;

/**
 *
 * @author samf
 */
public class MimicThread extends ServantThread {

    JointId left_shoulder_pitch;
    JointId left_shoulder_roll;
    JointId left_elbow_pitch;
    JointId right_elbow_yaw;
    JointId left_elbow_yaw;
    JointId right_shoulder_pitch;
    JointId right_shoulder_roll;
    JointId right_elbow_pitch;
   long lastUpdate;

    public MimicThread(Attachment parent) {
        super(parent);
        RobotController controller = parent.robotController;
        if (parent.robotActive) {
            left_shoulder_pitch = new JointId(controller.myRobot.getRobotId(), new Joint.Id(LEFT_SHOULDER_PITCH));
            left_shoulder_roll = new JointId(controller.myRobot.getRobotId(), new Joint.Id(LEFT_SHOULDER_ROLL));
            left_elbow_pitch = new JointId(controller.myRobot.getRobotId(), new Joint.Id(LEFT_ELBOW_PITCH));
            right_elbow_yaw = new JointId(controller.myRobot.getRobotId(), new Joint.Id(RIGHT_ELBOW_YAW));
            left_elbow_yaw = new JointId(controller.myRobot.getRobotId(), new Joint.Id(LEFT_ELBOW_YAW));
            right_shoulder_pitch = new JointId(controller.myRobot.getRobotId(), new Joint.Id(RIGHT_SHOULDER_PITCH));
            right_shoulder_roll = new JointId(controller.myRobot.getRobotId(), new Joint.Id(RIGHT_SHOULDER_ROLL));
            right_elbow_pitch = new JointId(controller.myRobot.getRobotId(), new Joint.Id(RIGHT_ELBOW_PITCH));
        }
        
        // need to init neck yaw...
    }

    @Override
    public void update() {
        //System.out.println("running mimic "+userRec.userData.getId());
        MyUserRecord userRec = parent.currentVisitors.get(id);
        if (userRec==null) {
            System.out.println("null userRec");
            return;
        }
        UserData user = userRec.userData;
        if (user==null) {
            System.out.println("null userData");

            return;
        }
      //  long now = System.currentTimeMillis();
         
       // UserData user = master.userRec.userData;
       if (user.getSkeleton().getState() == SkeletonState.TRACKED) {
            Point3f leftShoulder = vc.convertPoint(user.getSkeleton().getJoint(JointType.LEFT_SHOULDER).getPosition());
            Point3f rightShoulder = vc.convertPoint(user.getSkeleton().getJoint(JointType.RIGHT_SHOULDER).getPosition());
            Point3f torso = vc.convertPoint(user.getSkeleton().getJoint(JointType.TORSO).getPosition());
            Point3f leftElbow = vc.convertPoint(user.getSkeleton().getJoint(JointType.LEFT_ELBOW).getPosition());
            Point3f rightElbow = vc.convertPoint(user.getSkeleton().getJoint(JointType.RIGHT_ELBOW).getPosition());
            Point3f leftHand = vc.convertPoint(user.getSkeleton().getJoint(JointType.LEFT_HAND).getPosition());
            Point3f neck = vc.convertPoint(user.getSkeleton().getJoint(JointType.NECK).getPosition());
            Point3f rightHand = vc.convertPoint(user.getSkeleton().getJoint(JointType.RIGHT_HAND).getPosition());
            Point3f origin = new Point3f();
            
            //leftShoulder.add(zenoPos);
            
            
            float leftShoulderPitch = vc.planeAngle(leftShoulder, rightShoulder, torso, leftShoulder, leftElbow);
            if (leftElbow.y > leftShoulder.y) {
                leftShoulderPitch = (float) Math.PI / 2 - leftShoulderPitch;
            }
            float leftShoulderRoll = vc.planeAngle(neck, torso, origin, leftShoulder, leftElbow);

            float normPitch = (2.3f - leftShoulderPitch) / 2.6f;
            float normRoll = (leftShoulderRoll - 1.7f) / 1.5f;
            setPosition(left_shoulder_pitch, normPitch);
            setPosition(left_shoulder_roll, normRoll);

            float leftElbowPitch = vc.vectorAngle(leftElbow, leftShoulder, leftHand);

            normPitch = (float) (3 - leftElbowPitch) / (float) (Math.PI / 2);
            setPosition(left_elbow_pitch, normPitch);

            float rightShoulderPitch = vc.planeAngle(leftShoulder, rightShoulder, torso, rightShoulder, rightElbow);
            if (rightElbow.y > rightShoulder.y) {
                rightShoulderPitch = (float) Math.PI / 2 - rightShoulderPitch;
            }

            normPitch = (2.3f - rightShoulderPitch) / 2.6f;
            setPosition(right_shoulder_pitch, normPitch);
            float rightShoulderRoll = vc.planeAngle(neck, torso, origin, rightShoulder, rightElbow);
            normRoll = (1.5f - rightShoulderRoll) / 1.5f;
            setPosition(right_shoulder_roll, normRoll);


            float rightElbowPitch = vc.vectorAngle(rightElbow, rightShoulder, rightHand);

            normPitch = (float) (3 - rightElbowPitch) / (float) (Math.PI / 2);
            setPosition(right_elbow_pitch, normPitch);
            Quaternion leftElbowOrientation = user.getSkeleton().getJoint(JointType.LEFT_ELBOW).getOrientation();
            float angle = vc.quatToAngle(leftElbowOrientation);

            if (angle > Math.PI) {
                angle = (float) (2 * Math.PI - angle);
            }
            float normAngle = 1 - angle / 4.0f;

            setPosition(left_elbow_yaw, normAngle);

            Quaternion rightElbowOrientation = user.getSkeleton().getJoint(JointType.RIGHT_ELBOW).getOrientation();
            angle = vc.quatToAngle(rightElbowOrientation);
            if (angle > Math.PI) {
                angle = (float) (2 * Math.PI - angle);
            }
            normAngle = 1 - angle / 4.0f;
            setPosition(right_elbow_yaw, normAngle);
       }
     }
    

    
}
