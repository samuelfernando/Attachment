/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.threads;

import java.util.HashSet;
import java.util.Set;
import uk.ac.shef.attachment.Attachment;

/**
 *
 * @author samf
 */
public class MasterThread {
    Set<ServantThread> tasks;
    Attachment parent;
    public MasterThread(Attachment parent) {
        // run tasks
        tasks = new HashSet<ServantThread>();
        this.parent = parent;
    }

    public void add(ServantThread servant) {
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
