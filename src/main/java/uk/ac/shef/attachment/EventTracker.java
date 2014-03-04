/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

import java.util.Queue;
import java.util.Stack;
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
   ZenoAction currentAction;
   boolean busy;
   long currentTaskEndTime;
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
    
    long timeNow() {
        return System.currentTimeMillis();
    }
    
    boolean isBusy() {
        return busy;
    }
    
    int noActions() {
        return actions.size();
    }
    
    void maintainStack() {
        //System.out.println("maintain Stack");
        if (busy) {
            //System.out.println("busy");
            if (timeNow()>currentTaskEndTime) {
               // System.out.println("concluding action now "+ timeNow() + " end "+currentTaskEndTime);
            
                parent.conclude(currentAction);
                busy = false;
            }
        }
        if (!busy) {
           //System.out.println("not busy");

            if (!actions.isEmpty()) {
                //System.out.println("running action");
                currentAction = actions.remove();
                boolean userStillActive = parent.userStillActive(currentAction.id);
                //System.out.println("id = "+currentAction.id);
                if (userStillActive) {
                    //System.out.println("User active");
                    currentTaskEndTime = timeNow() + currentAction.duration;
                    //System.out.println("commencing action now "+ timeNow() + " end "+currentTaskEndTime);

                    parent.commence(currentAction);
                    busy = true;
                }
            }
        }
    }

    void push(ZenoAction action) {
        //System.out.println("action "+action.type+ " added");
        actions.add(action);
    }

    
}
