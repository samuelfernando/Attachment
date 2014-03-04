/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.actions;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import org.robokind.api.animation.messaging.RemoteAnimationPlayerClient;
import org.robokind.api.motion.messaging.RemoteRobot;
import org.robokind.client.basic.Robokind;
import uk.ac.shef.attachment.Attachment;
import uk.ac.shef.attachment.MyUserRecord;
import uk.ac.shef.attachment.PositionPanel;

/**
 *
 * @author samf
 */
public abstract class ZenoAction {
   
    String type;
   short id;
   long duration;
   long startTime;
   PositionPanel panel;
   Attachment parent;
    DecimalFormat df;
    RemoteRobot myRobot;
    public int priority=0;
    DatagramSocket socket;
    InetAddress address;
       int port;
   public ZenoAction(Attachment parent, String type, short id, long duration) {
        try {
            this.parent = parent;
            this.type = type;
            this.id = id;
            this.duration = duration;
            df = new DecimalFormat("#.##");
            //this.startTime = System.currentTimeMillis();
              socket = new DatagramSocket();
             address = InetAddress.getByName("192.168.0.101");
             port = 9999;
        } catch (Exception ex) {
            Logger.getLogger(ZenoAction.class.getName()).log(Level.SEVERE, null, ex);
        }
  
   }
   public String getType() {
       return type;
   }
   public long getDuration() {
       return duration;
   }
   
   public short getId() {
       return id;
   }
    void playSound(String text) throws Exception {
       byte[] strBytes = text.getBytes("utf-8");
       DatagramPacket packet = new DatagramPacket(strBytes, 0, strBytes.length, address, port);
       socket.send(packet);
    }
   long timeRemaining() {
       return parent.et.currentTaskEndTime - parent.et.timeNow();
   }
   void playLocalSound(String filename) {
        try {
            
            File file = new File("sounds/"+filename+".wav");
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            Clip clip;
            
            stream = AudioSystem.getAudioInputStream(file);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
            //Thread.sleep(4000);
        } catch (Exception ex) {
            Logger.getLogger(Attachment.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
   public abstract void commence();
   public abstract void conclude();
}

 
