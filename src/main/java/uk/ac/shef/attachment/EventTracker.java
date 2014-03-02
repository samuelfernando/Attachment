/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zeno
 */
public class EventTracker {
   Attachment parent;
   Stack<ZenoAction> actions;
   ZenoAction currentAction;
   boolean busy;
   long currentTaskEndTime;
   long timeSinceLastUpdate;
   
   
   public EventTracker(Attachment parent) {
        actions = new Stack<ZenoAction>();
        this.parent = parent;
        busy = false;
   }
  
   long getTimeSinceLastUpdate() { 
       return timeNow()-timeSinceLastUpdate;
   }
   
   void updated() {
       timeSinceLastUpdate = timeNow();
   }
    void start() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable toRun = new Runnable() {
            public void run() {
                maintainStack();
            }
        };
        scheduler.scheduleAtFixedRate(toRun, 0, 200, TimeUnit.MILLISECONDS);
    }
    
    long timeNow() {
        return System.currentTimeMillis();
    }
    
    void maintainStack() {
        
        if (busy) {
            if (timeNow()>currentTaskEndTime) {
                parent.conclude(currentAction);
                busy = false;
            }
        }
        if (!busy) {
            if (!actions.isEmpty()) {
                boolean userStillActive = false;
                while (!userStillActive && !actions.isEmpty()) {
                    currentAction = actions.pop();
                    userStillActive = parent.userStillActive(currentAction.id);
                    currentTaskEndTime = timeNow() + currentAction.duration;
                    parent.commence(currentAction);
                    busy = true;
                }
            }
        }
    }

    void push(ZenoAction greet) {
        actions.push(greet);
    }

    long timeSinceLastUpdate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
