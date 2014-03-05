/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samf
 */
public class SoundPlayer {
     DatagramSocket socket;
    InetAddress address;
       int port;
    public SoundPlayer() {
         try {
             socket = new DatagramSocket();
                         address = InetAddress.getByName("192.168.0.101");
                         port = 9999;
         } catch (Exception ex) {
             Logger.getLogger(SoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    public void playSound(String text) throws Exception {
       byte[] strBytes = text.getBytes("utf-8");
       DatagramPacket packet = new DatagramPacket(strBytes, 0, strBytes.length, address, port);
       socket.send(packet);
    }

    public long length(String neutralwonder) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   
}
