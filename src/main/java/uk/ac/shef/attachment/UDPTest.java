/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 *
 * @author zeno
 */
public class UDPTest {
    DatagramSocket socket;
    InetAddress address;
    int port = 9999;
    public UDPTest() throws Exception {
        
            socket = new DatagramSocket();
            address = InetAddress.getByName("192.168.0.101");
         
            // send the response to the client at "address" and "port"
            
           
         
    }
    
    void send(String text) throws Exception {
       byte[] strBytes = text.getBytes("utf-8");
       DatagramPacket packet = new DatagramPacket(strBytes, 0, strBytes.length, address, port);
       socket.send(packet);
       Thread.sleep(5000);
    }
    void close() {
           socket.close();
    }
    void playSound() {
        try {
            File file = new File("eh-oh.wav");
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
            Thread.sleep(5000);
        } catch (Exception ex) {
            Logger.getLogger(Attachment.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
   
    public static void main(String args[]) {
       try{
            //DatagramSocket socket = new DatagramSocket(9999, InetAddress.getByName("192.168.0.101"));
           UDPTest test = new UDPTest();
           //test.send("eh-oh");
           test.playSound();
           //test.send("neutral-wonder");
           //test.send("neutral-laugh");
           //test.send("bye-bye");
           //test.send("distress-no");
           test.close(); 
             } catch (Exception ex) {
            Logger.getLogger(UDPTest.class.getName()).log(Level.SEVERE, null, ex);
        }   
            
       
    }
}
