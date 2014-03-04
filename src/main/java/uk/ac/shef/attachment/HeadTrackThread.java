/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

import com.primesense.nite.JointType;
import com.primesense.nite.SkeletonState;
import com.primesense.nite.UserData;
import javax.vecmath.Point3f;
import org.robokind.api.motion.Robot.JointId;

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
    public void runChecked() {
              UserData user = userRec.userData;
              if (user.getSkeleton().getState() == SkeletonState.TRACKED) {

                Point3f head = vc.convertPoint(user.getSkeleton().getJoint(JointType.HEAD).getPosition());

                Point3f orig = new Point3f(0, 0, 0);

                Point3f center_head = new Point3f(0, 270, 2100);
                Point3f center_feet = new Point3f(0, -270, 2100);
                float yawA, pitchA;
                float angle;
                yawA = angle = vc.planeAngle(orig, center_head, center_feet, orig, head);
                yawA = (float) Math.cos(angle) * 0.7f + 0.6f;
                setPosition(neck_yaw, yawA);
                Point3f zeno_head = new Point3f(0, -100, 0);
                Point3f zeno_level = new Point3f(100, -100, 0);
                Point3f zeno_level2 = new Point3f(0, -100, 100);

                pitchA = angle = vc.planeAngle(zeno_head, zeno_level, zeno_level2, zeno_head, head);
                //positionLabel.setText("pitch "+df.format(pitchA) + " yaw "+df.format(yawA));
                pitchA = 0.75f - (float) Math.cos(angle);

                setPosition(neck_pitch, pitchA);


            }
          }
        
    
}
