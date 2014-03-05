/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.threads;

import com.primesense.nite.JointType;
import com.primesense.nite.SkeletonState;
import com.primesense.nite.UserData;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.robokind.api.motion.Joint;
import org.robokind.api.motion.Robot.JointId;
import uk.ac.shef.attachment.utils.VectorCalc;
import static org.robokind.client.basic.RobotJoints.*;
import uk.ac.shef.attachment.Attachment;
import uk.ac.shef.attachment.MyUserRecord;

/**
 *
 * @author samf
 */
public class HeadTrackThread extends ServantThread {

    JointId neck_yaw;
    JointId neck_pitch;
    long startTime;
    Point3f prevLeftHand;
    Point3f prevRightHand;
   // GET MEASURING TAPE to measure exactly!
    long lastUpdate;
    long lastTargetChange;
    JointType targetJoint;
           
    public HeadTrackThread(Attachment parent, MyUserRecord userRec) {
        super(parent, userRec);
        startTime = System.currentTimeMillis();
        if (parent.robotActive) {
            neck_yaw = new JointId(parent.myRobot.getRobotId(), new Joint.Id(NECK_YAW));
            neck_pitch = new JointId(parent.myRobot.getRobotId(), new Joint.Id(NECK_PITCH));
        }
        prevLeftHand = new Point3f();
        prevRightHand = new Point3f();
        targetJoint = JointType.HEAD;
        // need to init neck yaw...
    }

    public void runChecked() {
        userRec = parent.currentVisitors.get(userRec.userData.getId());
        
        UserData user = userRec.userData;
         long now = System.currentTimeMillis();
         if (now-lastUpdate<200) {
             return;
         }
         lastUpdate = now;
     
        if (user.getSkeleton().getState() == SkeletonState.TRACKED) {
            Point3f leftHand = vc.convertPoint(user.getSkeleton().getJoint(JointType.LEFT_HAND).getPosition());
            Point3f rightHand = vc.convertPoint(user.getSkeleton().getJoint(JointType.RIGHT_HAND).getPosition());
            Point3f head = vc.convertPoint(user.getSkeleton().getJoint(JointType.HEAD).getPosition());
       
            //Point3f target = vc.convertPoint(user.getSkeleton().getJoint(targetJoint).getPosition());
            Vector3f leftHandVel = new Vector3f();
            Vector3f rightHandVel = new Vector3f();
            
            leftHandVel.sub(leftHand, prevLeftHand);
            rightHandVel.sub(rightHand, prevRightHand);
            float leftHandSpeed = leftHandVel.length();
            float rightHandSpeed = rightHandVel.length();
            
            
            parent.positionPanel.setVar("leftHand", leftHand.x);
            parent.positionPanel.setVar("rightHand", rightHand.x);
           if (now-lastTargetChange>2500) {
                if (rightHandSpeed - leftHandSpeed > 50) {
                    targetJoint = JointType.RIGHT_HAND;
                }
                else if (leftHandSpeed - rightHandSpeed > 50) {
                    targetJoint = JointType.LEFT_HAND ;
                }
                else {
                    targetJoint = JointType.HEAD;
                }
                lastTargetChange = now;
           }
            prevLeftHand = leftHand;
            prevRightHand = rightHand;
            //targetJoint = JointType.HEAD;
            Point3f target = vc.convertPoint(user.getSkeleton().getJoint(targetJoint).getPosition());
            Point3f orig = zenoPos;
            target.add(zenoPos);
            Point3f center_head = new Point3f(0, 270, 2100);
            center_head.add(zenoPos);
            Point3f center_feet = new Point3f(0, -270, 2100);
            center_feet.add(zenoPos);
            float yawA, pitchA;
            yawA = vc.planeAngle(orig, center_head, center_feet, orig, target);
            //parent.positionPanel.setVar("neck-yaw", yawA);
            
            yawA = (float) Math.cos(yawA) * 0.75f + 0.5f;
            setPosition(neck_yaw, yawA);
            
            Point3f zeno_head = zenoPos;
            Point3f zeno_level = new Point3f(100, 0, 0);
            zeno_level.add(zenoPos);
            Point3f zeno_level2 = new Point3f(0, 0, 100);
            zeno_level2.add(zenoPos);
            pitchA = vc.planeAngle(zeno_head, zeno_level, zeno_level2, zeno_head, target);
            //positionLabel.setText("pitch "+df.format(pitchA) + " yaw "+df.format(yawA));
            //parent.positionPanel.setVar("neck-pitch", pitchA);
            pitchA = 0.8f - (float) Math.cos(pitchA);
            
            
            setPosition(neck_pitch, pitchA);


        }
    }
}
