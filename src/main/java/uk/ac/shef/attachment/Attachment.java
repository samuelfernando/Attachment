package uk.ac.shef.attachment;

import javax.vecmath.*;

import com.primesense.nite.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import javax.swing.JLabel;
import org.robokind.api.common.position.NormalizedDouble;
import org.robokind.api.motion.Joint;
import org.robokind.api.motion.Robot.JointId;
import org.robokind.api.motion.Robot.RobotPositionMap;
import org.robokind.api.motion.messaging.RemoteRobot;
import org.robokind.api.speech.messaging.RemoteSpeechServiceClient;
import org.robokind.client.basic.Robokind;
import static org.robokind.client.basic.RobotJoints.NECK_PITCH;
import static org.robokind.client.basic.RobotJoints.NECK_YAW;
import org.robokind.client.basic.UserSettings;


public class Attachment  {
    RemoteRobot myRobot;
    RobotPositionMap myGoalPositions;
    PrintStream out;
    JLabel positionLabel;
    DecimalFormat df;
   

    private static RemoteSpeechServiceClient mySpeaker;
    
     JointId neck_yaw;
    JointId neck_pitch;
    VectorCalc vc;
    EventTracker et;
    Short prevUserTrack=-1;
    UserTracking userTracking;
    public Attachment(UserTracker tracker, JLabel positionLabel) {
        String robotID = "myRobot";
        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\samf\\Documents\\NetBeansProjects\\zeno-ip.txt"));
            String robotIP = br.readLine();
           
            // set respective addresses
            UserSettings.setRobotId(robotID);
            UserSettings.setRobotAddress(robotIP);
            UserSettings.setSpeechAddress(robotIP);
            userTracking = new UserTracking(tracker, this);            
            this.positionLabel = positionLabel;
            df = new DecimalFormat("#.##");
            mySpeaker = Robokind.connectSpeechService();
            
            myRobot = Robokind.connectRobot();
            myGoalPositions = new org.robokind.api.motion.Robot.RobotPositionHashMap();
            myGoalPositions = myRobot.getDefaultPositions();
            myRobot.move(myGoalPositions, 1000);
              neck_yaw = new org.robokind.api.motion.Robot.JointId(myRobot.getRobotId(), new Joint.Id(NECK_YAW));
            neck_pitch = new org.robokind.api.motion.Robot.JointId(myRobot.getRobotId(), new Joint.Id(NECK_PITCH));
         
            
            
            vc = new VectorCalc();
            et = new EventTracker();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void sensorMotors() {
        HashMap<Short, Float> speeds = new HashMap<Short, Float>();
        float timeSince = et.timeSinceLastUpdate();
         for (UserData user : userTracking.getLastFrame().getUsers()) {
            if (user.getSkeleton().getState() == SkeletonState.TRACKED) {
                if (timeSince>200) {
                    float speed = userTracking.findDist(user)/timeSince;
                    speeds.put(user.getId(), speed);
                }
            }
         }
        
        if (timeSince>200) {
            if (et.timeSinceChangeUser()>3000) {
                UserSpeed userSpeed = userTracking.findFastestUser(speeds);
                UserData fastestUser = userSpeed.user;
                float maxSpeed = userSpeed.speed;
                if (fastestUser!=null && timeSince>200) {
                    headTrack(fastestUser, maxSpeed);
                    //positionLabel.setText("id "+fastestUser.getId()+" speed "+ df.format(maxSpeed));             
                    prevUserTrack = fastestUser.getId();
                }
                et.lastChangeUser = System.currentTimeMillis();
               
            }
            else {
                if (speeds.keySet().contains(prevUserTrack)) {
                    float speed = speeds.get(prevUserTrack);
                    headTrack(userTracking.getLastFrame().getUserById(prevUserTrack), speed);
                   //positionLabel.setText("id "+prevUserTrack+" speed "+ df.format(speed));  
                } else {
                    UserSpeed userSpeed = userTracking.findFastestUser(speeds);
                    UserData fastestUser = userSpeed.user;
                    float maxSpeed = userSpeed.speed;
                    if (fastestUser!=null && timeSince>200) {
                        headTrack(fastestUser, maxSpeed);
                       // positionLabel.setText("id "+fastestUser.getId()+" speed "+ df.format(maxSpeed));             
                        prevUserTrack = fastestUser.getId();
                    }
                    et.lastChangeUser = System.currentTimeMillis();
                }
            }
            
        }
    }
    private void headTrack(UserData user, float speed) {

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
            positionLabel.setText("pitch "+df.format(pitchA) + " yaw "+df.format(yawA));
            angle = 0.75f - (float) Math.cos(angle);

            setPosition(neck_pitch, angle);
            
           
        }
        myRobot.move(myGoalPositions, 200);
        et.lastUpdateTime = System.currentTimeMillis();
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
    
    
                
    

   

   

    
}
