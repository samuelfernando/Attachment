package uk.ac.shef.attachment;

import uk.ac.shef.attachment.utils.VectorCalc;
import uk.ac.shef.attachment.actions.MimicAction;
import uk.ac.shef.attachment.actions.ZenoAction;
import uk.ac.shef.attachment.actions.ByeAction;
import uk.ac.shef.attachment.actions.HelloAction;
import uk.ac.shef.attachment.threads.MasterThread;
import com.primesense.nite.*;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import javax.vecmath.Vector3f;
import org.robokind.api.animation.Animation;
import org.robokind.api.animation.messaging.RemoteAnimationPlayerClient;
import org.robokind.api.common.position.NormalizedDouble;
import org.robokind.api.motion.Robot.JointId;
import org.robokind.api.motion.Robot.RobotPositionMap;
import org.robokind.api.motion.messaging.RemoteRobot;
import org.robokind.api.speech.messaging.RemoteSpeechServiceClient;
import org.robokind.client.basic.Robokind;
import org.robokind.client.basic.UserSettings;
import uk.ac.shef.attachment.actions.BlinkAction;
import uk.ac.shef.attachment.utils.ReadConfig;

public class Attachment {

    public RemoteRobot myRobot;
    public RobotPositionMap myGoalPositions;
    PrintStream out;
    DecimalFormat df;
    public PositionPanel positionPanel;
    JointId neck_yaw;
    JointId neck_pitch;
    VectorCalc vc;
    public EventTracker et;
    public UserTracking userTracking;
    public boolean robotActive;
    Animation ehoh;
    Animation byebye;
    public HashMap<Short, MyUserRecord> currentVisitors;
    public HashMap<Short, Boolean> isDueToMimic;
    RemoteSpeechServiceClient mySpeaker;
    public RemoteAnimationPlayerClient myPlayer;
    public boolean needsToMove;
//    public Attachment(UserTracker tracker, JLabel positionLabel) {

    public Attachment(UserTracker tracker, PositionPanel positionPanel) {

        try {
            //BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\samf\\Documents\\NetBeansProjects\\zeno-ip.txt"));
            HashMap<String, String> configs = ReadConfig.readConfig();
            robotActive = Boolean.parseBoolean(configs.get("robot-active"));

            if (robotActive) {
                String robotIP = configs.get("ip");

                String robotID = "myRobot";

                // set respective addresses


                UserSettings.setRobotId(robotID);
                UserSettings.setRobotAddress(robotIP);
                UserSettings.setSpeechAddress(robotIP);
                UserSettings.setAnimationAddress(robotIP);


                mySpeaker = Robokind.connectSpeechService();
                myPlayer = Robokind.connectAnimationPlayer();
                myRobot = Robokind.connectRobot();
                myGoalPositions = new org.robokind.api.motion.Robot.RobotPositionHashMap();
                myGoalPositions = myRobot.getDefaultPositions();
                myRobot.move(myGoalPositions, 1000);
                Thread.sleep(1000);
                myGoalPositions.clear();
            }

            // this.positionLabel = positionLabel;
            ehoh = Robokind.loadAnimation("animations/eh-oh-2.xml");
            byebye = Robokind.loadAnimation("animations/byebye.xml");
            isDueToMimic = new HashMap<Short, Boolean>();
            currentVisitors = new HashMap<Short, MyUserRecord>();
            this.positionPanel = positionPanel;
            df = new DecimalFormat("#.##");
            userTracking = new UserTracking(tracker, this);
            vc = new VectorCalc();
            et = new EventTracker(this);
            needsToMove = false;
            short fakeId = 9999;
            long duration = 100000000;
            BlinkAction blink;
            blink = new BlinkAction(this, "blink", fakeId, duration);
            commence(blink);
            et.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public PositionPanel getPositionPanel() {
        return positionPanel;
    }

    boolean userStillActive(short id) {
        // Need a better idea of what the 
        //UserData user = userTracking.getLastFrame().getUserById(id);
        //return (user!=null);
        return currentVisitors.containsKey(id);
    }

    void commence(ZenoAction action) {
        action.commence();
    }

    void conclude(ZenoAction action) {
        action.conclude();
    }

    boolean check(short id, String type) {
        MyUserRecord rec = currentVisitors.get(id);
        if (type.equals("farewell")) {
            return rec.farewelled;
        } else if (type.equals("greeted")) {
            return rec.greeted;
        }
        return false;
    }

    void userUpdate(short id, String type) {
        MyUserRecord userRecord = currentVisitors.get(id);
        if (type.equals("greet")) {
            userRecord.greeted = true;
        } else if (type.equals("bye")) {
            userRecord.farewelled = true;
        } else {
            System.out.println("Wrong user update");
            System.exit(1);
        }
        currentVisitors.put(id, userRecord);
    }

    void sensorMotors() {
        //float timeSince = et.timeSinceLastUpdate();
        long timeSince = et.getTimeSinceLastUpdate();
        if (timeSince > 200) {
            for (short id : currentVisitors.keySet()) {
                if (userTracking.getLastFrame().getUserById(id) == null) {

                    MyUserRecord userRec = currentVisitors.get(id);
                    if (userRec.scheduledForDeletion) {
                        if (System.currentTimeMillis() > userRec.timeForDeletion) {
                            currentVisitors.remove(id);
                        }
                    } else {
                        userRec.scheduleForDeletion();
                    }
                }
            }
        }

        for (UserData user : userTracking.getLastFrame().getUsers()) {
            if (user.getSkeleton().getState() == SkeletonState.TRACKED) {

                if (timeSince > 200) {

                    //System.out.println("sensorMotors time");
                    short id = user.getId();
                    if (!currentVisitors.containsKey(id)) {
                        // new visitor
                        //System.out.println("hello length = "+ehoh.getLength() + " id = "+id);

                        HelloAction greet = new HelloAction(this, "greet", id, ehoh.getLength());
                        MyUserRecord rec = new MyUserRecord(user);

                        currentVisitors.put(id, rec);
                        userUpdate(id, "greet");

                        et.push(greet);


                    } else {
                        MyUserRecord rec = currentVisitors.get(id);
                        rec.userData = user;
                        rec.cancelDeletion();
                        currentVisitors.put(id, rec);
                    }
                    Vector3f vel = userTracking.userVel(user);
                    positionPanel.vel = vel;
                    float threshold = 500.0f;
                    if (vel.z > threshold && !check(id, "farewell")) {
                        ByeAction bye = new ByeAction(this, "bye", id, byebye.getLength());
                        userUpdate(id, "bye");
                        et.push(bye);
                    } else {
                        if (!dueToMimic(id)) {
                            MimicAction mimicAndHeadTrack = new MimicAction(this, "mimic+headTrack", id, 10000);
                            et.push(mimicAndHeadTrack);
                            isDueToMimic.put(id, true);
                        }
                    }
                    et.updated();
                    positionPanel.repaint();
                    if (robotActive && needsToMove) {
                        //System.out.println("Moving robot");
                        myRobot.move(myGoalPositions, 200);
                        myGoalPositions.clear();
                    }


                }
            }
        }
    }

    public void addGoalPosition(org.robokind.api.motion.Robot.JointId jointID, NormalizedDouble val) {
        myGoalPositions.put(jointID, val);
    }

    boolean dueToMimic(short id) {
        boolean result = false;
        if (isDueToMimic.containsKey(id)) {
            result = isDueToMimic.get(id);
        }
        return result;
    }
}