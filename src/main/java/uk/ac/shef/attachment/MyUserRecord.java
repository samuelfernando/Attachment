/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

import com.primesense.nite.UserData;

/**
 *
 * @author samf
 */
public class MyUserRecord {
    public UserData userData;
    public boolean greeted=false;
    public boolean farewelled=false;
    public MyUserRecord(UserData userData) {
        this.userData = userData;
    }
}
