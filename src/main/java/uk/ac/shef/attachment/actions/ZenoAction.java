/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.actions;

import java.io.File;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import uk.ac.shef.attachment.Attachment;
import uk.ac.shef.attachment.EventTracker;
import uk.ac.shef.attachment.threads.MasterThread;

/**
 *
 * @author samf
 */
public abstract class ZenoAction {
   
    String type;
   short id;
   long duration;
   long startTime;
   EventTracker et;
   Attachment parent;
    DecimalFormat df;
    public int priority=0;
   public ZenoAction(Attachment parent, String type, short id, long duration) {
        try {
            this.parent = parent;
            this.et = parent.et;
            this.type = type;
            this.id = id;
            this.duration = duration;
            df = new DecimalFormat("#.##");
            //this.startTime = System.currentTimeMillis();
             
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
   long timeRemaining() {
       return et.currentTaskEndTime - et.timeNow();
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

 
