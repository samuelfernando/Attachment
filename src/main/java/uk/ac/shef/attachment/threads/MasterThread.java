/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.threads;

import java.util.HashSet;
import java.util.Set;
import org.robokind.api.motion.messaging.RemoteRobot;
import uk.ac.shef.attachment.Attachment;
import uk.ac.shef.attachment.MyUserRecord;

/**
 *
 * @author samf
 */
public class MasterThread {
    Set<ServantThread> tasks;
    MyUserRecord userRec;
    Attachment parent;
    public MasterThread(Attachment parent, MyUserRecord userRec) {
        // run tasks
        tasks = new HashSet<ServantThread>();
        this.userRec = userRec;
        this.parent = parent;
    }

    public void add(ServantThread servant) {
        servant.setMaster(this);
        tasks.add(servant);
    }

    public void start() {
        for (ServantThread task : tasks) {
            task.start();
        }
    }

    public void end() {
        for (ServantThread task : tasks) {
            task.end();
        }
    }
    
    
    
}
