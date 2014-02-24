/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.robokind.api.animation.Animation;
import org.robokind.api.animation.messaging.RemoteAnimationPlayerClient;
import org.robokind.api.motion.Robot;
import org.robokind.api.motion.messaging.RemoteRobot;
import org.robokind.client.basic.Robokind;
import org.robokind.client.basic.UserSettings;



/**
 *
 * @author samf
 */
public class SoundAnim {
    RemoteRobot myRobot;
    RemoteAnimationPlayerClient myPlayer;
    DatagramSocket socket;
    InetAddress address;
    int port = 9999;
    public SoundAnim() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\samf\\Documents\\NetBeansProjects\\zeno-ip.txt"));
        String robotIP = br.readLine();
        String robotID = "myRobot";
        
        UserSettings.setRobotId(robotID);
        UserSettings.setRobotAddress(robotIP);
        UserSettings.setAnimationAddress(robotIP);
        myRobot = Robokind.connectRobot();
        myPlayer = Robokind.connectAnimationPlayer();
        socket = new DatagramSocket();
        address = InetAddress.getByName("192.168.0.101");
    }
    
    void send(String text) throws Exception {
       byte[] strBytes = text.getBytes("utf-8");
       DatagramPacket packet = new DatagramPacket(strBytes, 0, strBytes.length, address, port);
       socket.send(packet);
    }
    
    public static void main(String args[]) {
        try {
            SoundAnim soundAnim = new SoundAnim();
            soundAnim.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    void run() throws Exception {
        
        Robot.RobotPositionMap myGoalPositions = myRobot.getDefaultPositions();
        myRobot.move(myGoalPositions, 1000);
        Thread.sleep(1000);
        Animation anim = Robokind.loadAnimation("animations/eh-oh-2.xml");
        myPlayer.playAnimation(anim);
        send("eh-oh");
        Thread.sleep(1600);
        send("eh-oh");
    }
}
