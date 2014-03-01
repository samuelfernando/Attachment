package uk.ac.shef.attachment;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JFrame;

import org.openni.Device;
import org.openni.OpenNI;

import org.openni.*;
import com.primesense.nite.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AttachmentApplication {

    private JFrame mFrame;
    private boolean mShouldRun = true;
    private Attachment mViewer;
    
    public AttachmentApplication(UserTracker tracker) {
        mFrame = new JFrame("NiTE User Tracker Viewer");
        PositionPanel positionPanel = new PositionPanel();
        JPanel panel = new JPanel(new GridLayout(1,2));
        mViewer = new Attachment(tracker, positionPanel);
        UserTracking userTracking = mViewer.userTracking;
        panel.add(userTracking);
        panel.add(positionPanel);
        // register to key events
        mFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent arg0) {}
            
            @Override
            public void keyReleased(KeyEvent arg0) {}
            
            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    mShouldRun = false;
                }
            }
        });
        
        // register to closing event
        mFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                mShouldRun = false;
               
            }
        });

       // mViewer.setSize(800, 600);
        userTracking.setSize(800,800);
        positionPanel.setSize(800,800);
        mFrame.add(panel);

 //       mFrame.setSize(mViewer.getWidth(), mViewer.getHeight());
          mFrame.setSize(1600, 800);
   
        mFrame.setVisible(true);
    }

    void run() {
        while (mShouldRun) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mFrame.dispose();
        System.exit(0);
    }

    public static void main(String s[]) {
        // initialize OpenNI and NiTE
    	OpenNI.initialize();
        NiTE.initialize();
        
        List<DeviceInfo> devicesInfo = OpenNI.enumerateDevices();
        if (devicesInfo.size() == 0) {
            JOptionPane.showMessageDialog(null, "No device is connected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Device device = Device.open(devicesInfo.get(0).getUri());
        UserTracker tracker = UserTracker.create();

        final AttachmentApplication app = new AttachmentApplication(tracker);
        app.run();
    }
}
