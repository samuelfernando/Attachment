/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

import uk.ac.shef.attachment.actions.ZenoAction;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zeno
 */
public class EventTracker {
   Attachment parent;
   Queue<ZenoAction> actions;
   public ZenoAction currentAction;
   boolean busy;
   public long currentTaskEndTime;
   long timeSinceLastUpdate;
   ScheduledExecutorService scheduler;
   Runnable toRun;
   
   public EventTracker(Attachment parent) {
        actions = new ArrayBlockingQueue<ZenoAction>(10);
        toRun = new Runnable() {
            public void run() {
                //System.out.println("running");
                try {
                    maintainStack();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        };
        this.parent = parent;
        busy = false;
       scheduler = Executors.newScheduledThreadPool(1);
   }
  
   long getTimeSinceLastUpdate() { 
       return timeNow()-timeSinceLastUpdate;
   }
   
   void updated() {
       timeSinceLastUpdate = timeNow();
   }
    void start() { 
        scheduler.scheduleAtFixedRate(toRun, 0, 200, TimeUnit.MILLISECONDS);
    }
    
    public long timeNow() {
        return System.currentTimeMillis();
    }
    
    boolean isBusy() {
        return busy;
    }
    
    int noActions() {
        return actions.size();
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
                currentAction = actions.remove();
                boolean userStillActive = parent.userStillActive(currentAction.getId());
                if (userStillActive) {
                    currentTaskEndTime = timeNow() + currentAction.getDuration();

                    parent.commence(currentAction);
                    busy = true;
                }
            }
        }
    }

    void push(ZenoAction action) {
        actions.add(action);
    }

    
}
