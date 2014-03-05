/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.threads;

import com.primesense.nite.JointType;
import com.primesense.nite.SkeletonState;
import com.primesense.nite.UserData;
import java.util.Stack;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.robokind.api.motion.Joint;
import org.robokind.api.motion.Robot.JointId;
import uk.ac.shef.attachment.utils.VectorCalc;
import static org.robokind.client.basic.RobotJoints.*;
import uk.ac.shef.attachment.Attachment;
import uk.ac.shef.attachment.MyUserRecord;
import uk.ac.shef.attachment.RobotController;

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
    Stack<Vector3f> leftHandSpeeds;
    Stack<Vector3f> rightHandSpeeds;
    // GET MEASURING TAPE to measure exactly!
    long lastUpdate;
    long lastTargetChange;
    JointType targetJoint;
           
    public HeadTrackThread(Attachment parent) {
        super(parent);
        RobotController controller = parent.robotController;
        startTime = System.currentTimeMillis();
        if (parent.robotActive) {
            neck_yaw = new JointId(controller.myRobot.getRobotId(), new Joint.Id(NECK_YAW));
            neck_pitch = new JointId(controller.myRobot.getRobotId(), new Joint.Id(NECK_PITCH));
        }
        prevLeftHand = new Point3f();
        prevRightHand = new Point3f();
        targetJoint = JointType.HEAD;
        lastTargetChange = startTime;
        // need to init neck yaw...
        leftHandSpeeds = new Stack<Vector3f>();
        rightHandSpeeds = new Stack<Vector3f>();
    }

    public void update() {
        //System.out.println("running head track "+userRec.userData.getId());
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
         long now = System.currentTimeMillis();
     
        if (user.getSkeleton().getState() == SkeletonState.TRACKED) {
            Point3f leftHand = vc.convertPoint(user.getSkeleton().getJoint(JointType.LEFT_HAND).getPosition());
            Point3f rightHand = vc.convertPoint(user.getSkeleton().getJoint(JointType.RIGHT_HAND).getPosition());
       
            //Point3f target = vc.convertPoint(user.getSkeleton().getJoint(targetJoint).getPosition());
            Vector3f leftHandVel = new Vector3f();
            Vector3f rightHandVel = new Vector3f();
            
            leftHandVel.sub(leftHand, prevLeftHand);
            rightHandVel.sub(rightHand, prevRightHand);
            leftHandSpeeds.push(leftHandVel);
            rightHandSpeeds.push(rightHandVel);
            
            
            parent.positionPanel.setVar("leftHand", leftHand.x);
            parent.positionPanel.setVar("rightHand", rightHand.x);
            if (now-lastTargetChange>1500) {
                float leftHandTotal = 0.0f;
                float rightHandTotal = 0.0f;
                while (!leftHandSpeeds.isEmpty()) {
                    Vector3f leftHandV = leftHandSpeeds.pop();
                    Vector3f rightHandV = rightHandSpeeds.pop();
                    leftHandTotal+=leftHandV.length();
                    rightHandTotal+=rightHandV.length();
                }
                if (leftHandTotal>rightHandTotal) {
                    targetJoint = JointType.LEFT_HAND;
                }
                else {
                    targetJoint = JointType.RIGHT_HAND;
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
