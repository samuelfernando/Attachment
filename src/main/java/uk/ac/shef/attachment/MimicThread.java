/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

import com.primesense.nite.JointType;
import com.primesense.nite.UserData;
import javax.vecmath.Point3f;
import org.robokind.api.motion.Robot.JointId;
import org.robokind.api.motion.messaging.RemoteRobot;

/**
 *
 * @author samf
 */
public class MimicThread extends Thread {
    MyUserRecord userRec;
    boolean shouldRun;
    RemoteRobot myRobot;
    JointId left_shoulder_pitch;
    JointId left_shoulder_roll;
    JointId left_elbow_pitch;
    JointId right_elbow_yaw;
    JointId left_elbow_yaw;
    JointId right_shoulder_pitch;
    JointId right_shoulder_roll;
    JointId right_elbow_pitch;
    VectorCalc vc;
    MasterThread masterThread;
    
    public MimicThread(MasterThread masterThread) {
        this.masterThread = masterThread;
    }
    
    
    public MimicThread(MyUserRecord userRec, RemoteRobot myRobot) {
        this.userRec= userRec;
        this.myRobot = myRobot;
        vc = new VectorCalc();
    }
    @Override
    public void run() {
        UserData user = userRec.userData;
        while (shouldRun) {
            Point3f leftShoulder = vc.convertPoint(user.getSkeleton().getJoint(JointType.LEFT_SHOULDER).getPosition());
            Point3f rightShoulder = vc.convertPoint(user.getSkeleton().getJoint(JointType.RIGHT_SHOULDER).getPosition());
            Point3f torso = vc.convertPoint(user.getSkeleton().getJoint(JointType.TORSO).getPosition());
            Point3f leftElbow = vc.convertPoint(user.getSkeleton().getJoint(JointType.LEFT_ELBOW).getPosition());
            Point3f rightElbow = vc.convertPoint(user.getSkeleton().getJoint(JointType.RIGHT_ELBOW).getPosition());
             Point3f leftHand = vc.convertPoint(user.getSkeleton().getJoint(JointType.LEFT_HAND).getPosition());
            Point3f neck = vc.convertPoint(user.getSkeleton().getJoint(JointType.NECK).getPosition());
            Point3f rightHand = vc.convertPoint(user.getSkeleton().getJoint(JointType.RIGHT_HAND).getPosition());
            Point3f origin = new Point3f();
            float leftShoulderPitch = vc.planeAngle(leftShoulder, rightShoulder, torso, leftShoulder, leftElbow);
            if (leftElbow.y > leftShoulder.y) {
                leftShoulderPitch = (float)Math.PI/2 - leftShoulderPitch;
            }
            float leftShoulderRoll = vc.planeAngle(neck, torso, origin, leftShoulder, leftElbow);
                    
            float normPitch = (2.3f-leftShoulderPitch)/2.6f;
            float normRoll = (leftShoulderRoll-1.7f)/1.5f;
         /*
            parent.setPosition(left_shoulder_pitch, normPitch);
            parent.setPosition(left_shoulder_roll, normRoll);
            
            float leftElbowPitch = vc.vectorAngle(leftElbow, leftShoulder, leftHand);
            
            normPitch = (float)(3 - leftElbowPitch) / (float)(Math.PI/2);
            parent.setPosition(left_elbow_pitch, normPitch);
            
            float rightShoulderPitch = planeAngle(leftShoulder, rightShoulder, torso, rightShoulder, rightElbow);
            if (rightElbow.y > rightShoulder.y) {
                rightShoulderPitch = (float)Math.PI/2 - rightShoulderPitch;
            }
            
            normPitch = (2.3f-rightShoulderPitch)/2.6f;
            parent.setPosition(right_shoulder_pitch, normPitch);
            float rightShoulderRoll = planeAngle(neck, torso, origin, rightShoulder, rightElbow);
            normRoll = (1.5f - rightShoulderRoll)/1.5f;
            parent.setPosition(right_shoulder_roll, normRoll);
            
            
            float rightElbowPitch = vectorAngle(rightElbow, rightShoulder, rightHand);
            
            normPitch = (float)(3 - rightElbowPitch) / (float)(Math.PI/2);
            setPosition(right_elbow_pitch, normPitch);
            Quaternion leftElbowOrientation = user.getSkeleton().getJoint(JointType.LEFT_ELBOW).getOrientation();
            float angle = quatToAngle(leftElbowOrientation);
            
            if (angle>Math.PI) {
                 angle = (float)(2 * Math.PI - angle);
             }
            float normAngle = 1-angle/4.0f;
            
            setPosition(left_elbow_yaw, normAngle);
            
             Quaternion rightElbowOrientation = user.getSkeleton().getJoint(JointType.RIGHT_ELBOW).getOrientation();
            angle = quatToAngle(rightElbowOrientation);
               if (angle>Math.PI) {
                 angle = (float)(2 * Math.PI - angle);
             }
            normAngle = 1-angle/4.0f;
            setPosition(right_elbow_yaw, normAngle);
         */   
        }
    }
    
    void end() {
        this.shouldRun = false;
    }
}
