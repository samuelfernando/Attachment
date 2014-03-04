/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

import java.util.Comparator;
import java.util.PriorityQueue;
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
   PriorityQueue<ZenoAction> actions;
   public ZenoAction currentAction;
   boolean busy;
   public long currentTaskEndTime;
   long timeSinceLastUpdate;
   ScheduledExecutorService scheduler;
   Runnable toRun;
   
   public EventTracker(Attachment parent) {
        actions = new PriorityQueue<ZenoAction>(11, new ZenoActionComparator());
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
                else {
                    System.out.println("Lost user "+currentAction.getId());
                } 
            }
        }
    }

    void push(ZenoAction action) {
        System.out.println("adding "+action.getType());
        actions.add(action);
        System.out.println(actions);
    }

    
}

class ZenoActionComparator implements Comparator<ZenoAction>
{
    public int compare(ZenoAction o1, ZenoAction o2) {
        return o2.priority-o1.priority;      
    }
}