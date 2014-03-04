/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 *
 * @author samf
 */
abstract class ZenoAction {
   
    String type;
   short id;
   long duration;
   long startTime;
   PositionPanel panel;
   Attachment parent;
    DecimalFormat df;
   public ZenoAction(Attachment parent, String type, short id, long duration) {
       this.parent = parent;
       this.type = type;
       this.id = id;
       this.duration = duration;
       df = new DecimalFormat("#.##");
       //this.startTime = System.currentTimeMillis();
   }
   
   void userUpdate(short id, String type) {
        MyUserRecord userRecord = parent.currentVisitors.get(id);
        if (type.equals("greet")) {
            userRecord.greeted = true;
        }
        else if (type.equals("bye")) {
            userRecord.farewelled = true;
        }
        parent.currentVisitors.put(id, userRecord);
    }
   long timeRemaining() {
       return parent.et.currentTaskEndTime - parent.et.timeNow();
   }
   void playSound(String filename) {
        try {
            
            File file = new File(filename+".wav");
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
   abstract void commence();
   abstract void conclude();
}

 
