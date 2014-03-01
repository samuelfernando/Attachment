/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

/**
 *
 * @author zeno
 */
public class EventTracker {
    long lastBlink = 0;
    long lastUpdateTime = 0;
    long lastSpeak = 0;
    long lastChangeUser =0;
     long timeSinceLastUpdate() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastUpdateTime;
    }

    long timeSinceLastSpeak() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastSpeak;
    }
    
    long timeSinceLastBlink() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastBlink;
    }
    
    long timeSinceChangeUser() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastChangeUser;
    }
    
    void updateTime() {
        lastUpdateTime = System.currentTimeMillis();
    }
}
