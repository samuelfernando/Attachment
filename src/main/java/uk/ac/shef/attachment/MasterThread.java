/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

import java.util.HashSet;
import java.util.Set;
import org.robokind.api.motion.messaging.RemoteRobot;

/**
 *
 * @author samf
 */
class MasterThread {
    Set<ServantThread> tasks;
    public MasterThread(MyUserRecord userRec, RemoteRobot myRobot) {
        // run tasks
        tasks = new HashSet<ServantThread>();
    }

    void add(ServantThread servant, MyUserRecord rec) {
        tasks.add(servant);
        servant.setUser(rec);
    }

    void start() {
        for (ServantThread task : tasks) {
            task.start();
        }
    }

    void end() {
        for (ServantThread task : tasks) {
            task.end();
        }
    }
    
    
    
}
