/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

/**
 *
 * @author samf
 */
class ZenoAction {
   String type;
   short id;
   long duration;
   long startTime;
   public ZenoAction(String type, short id, long duration) {
       this.type = type;
       this.id = id;
       this.duration = duration;
       //this.startTime = System.currentTimeMillis();
   }
   
}
