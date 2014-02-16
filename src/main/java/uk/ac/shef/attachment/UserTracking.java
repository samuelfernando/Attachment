/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment;

import com.primesense.nite.JointType;
import com.primesense.nite.Skeleton;
import com.primesense.nite.SkeletonJoint;
import com.primesense.nite.SkeletonState;
import com.primesense.nite.UserData;
import com.primesense.nite.UserTracker;
import com.primesense.nite.UserTrackerFrameRef;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.HashMap;
import javax.vecmath.Point3f;
import org.openni.VideoFrameRef;

/**
 *
 * @author zeno
 */
public class UserTracking extends Component implements UserTracker.NewFrameListener {
    
    float mHistogram[];
    int[] mDepthPixels;
    UserTracker mTracker;
    UserTrackerFrameRef mLastFrame;
    BufferedImage mBufferedImage;
    int[] mColors;
    HashMap<Short, Skeleton> prevSkeletons;
    Attachment parent;
    VectorCalc vc;
    public UserTracking(UserTracker tracker, Attachment parent) {
        mTracker = tracker;
            this.parent = parent;
            mTracker.addNewFrameListener(this);
            
            mColors = new int[]{0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00, 0xFFFF00FF, 0xFF00FFFF};
            prevSkeletons = new HashMap<Short, Skeleton>();
            vc = new VectorCalc();
    }
    UserTrackerFrameRef getLastFrame() {
        return mLastFrame;
    }
    
     public void drawLimbs(Graphics g) {
        
        int framePosX = 0;
        int framePosY = 0;
       
       
        VideoFrameRef depthFrame = mLastFrame.getDepthFrame();
        if (depthFrame != null) {
            int width = depthFrame.getWidth();
            int height = depthFrame.getHeight();

            // make sure we have enough room
            if (mBufferedImage == null || mBufferedImage.getWidth() != width || mBufferedImage.getHeight() != height) {
                mBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            }

            mBufferedImage.setRGB(0, 0, width, height, mDepthPixels, 0, width);

            framePosX = (getWidth() - width) / 2;
            framePosY = (getHeight() - height) / 2;

            g.drawImage(mBufferedImage, framePosX, framePosY, null);
        }


         
        
        for (UserData user : mLastFrame.getUsers()) {
            if (user.getSkeleton().getState() == SkeletonState.TRACKED) {
                
                drawLimb(g, framePosX, framePosY, user, JointType.HEAD, JointType.NECK);

                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_SHOULDER, JointType.LEFT_ELBOW);


                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_ELBOW, JointType.LEFT_HAND);

                drawLimb(g, framePosX, framePosY, user, JointType.RIGHT_SHOULDER, JointType.RIGHT_ELBOW);
                drawLimb(g, framePosX, framePosY, user, JointType.RIGHT_ELBOW, JointType.RIGHT_HAND);

                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_SHOULDER, JointType.RIGHT_SHOULDER);

                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_SHOULDER, JointType.TORSO);
                drawLimb(g, framePosX, framePosY, user, JointType.RIGHT_SHOULDER, JointType.TORSO);

                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_HIP, JointType.TORSO);
                drawLimb(g, framePosX, framePosY, user, JointType.RIGHT_HIP, JointType.TORSO);
                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_HIP, JointType.RIGHT_HIP);

                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_HIP, JointType.LEFT_KNEE);
                drawLimb(g, framePosX, framePosY, user, JointType.LEFT_KNEE, JointType.LEFT_FOOT);

                drawLimb(g, framePosX, framePosY, user, JointType.RIGHT_HIP, JointType.RIGHT_KNEE);
                drawLimb(g, framePosX, framePosY, user, JointType.RIGHT_KNEE, JointType.RIGHT_FOOT);

               
                
                
            }
        }
    }

     UserSpeed findFastestUser(HashMap<Short, Float> speeds) {
        UserSpeed userSpeed = new UserSpeed();
        UserData fastestUser=null;
        float maxSpeed=0.0f;
        for (Short userId:speeds.keySet()) {
            float speed = speeds.get(userId);
            if (speed>maxSpeed) {
                fastestUser = mLastFrame.getUserById(userId);
                maxSpeed = speed;
            }
        }
        userSpeed.user = fastestUser;
        userSpeed.speed = maxSpeed;
        return userSpeed;
    }
     
    

    float findDist(UserData user) {
        Skeleton skeleton = user.getSkeleton();
        Skeleton lastSkeleton = prevSkeletons.get(user.getId());
        
        if (lastSkeleton==null) {
            prevSkeletons.put(user.getId(), skeleton);
            return 1000.0f;
        }
        
        SkeletonJoint[] joints = skeleton.getJoints();
        float total=0.0f;
        for (SkeletonJoint joint : joints) {
            JointType type = joint.getJointType();
            SkeletonJoint lastJoint = lastSkeleton.getJoint(type);
            Point3f lastPos = vc.convertPoint(lastJoint.getPosition());
            Point3f curPos = vc.convertPoint(joint.getPosition());
            float dist = lastPos.distance(curPos);
            total+=dist;
            
        }
        
        prevSkeletons.put(user.getId(), skeleton);
        return total;
    }
    
    private void drawLimb(Graphics g, int x, int y, UserData user, JointType from, JointType to) {
        com.primesense.nite.SkeletonJoint fromJoint = user.getSkeleton().getJoint(from);
        com.primesense.nite.SkeletonJoint toJoint = user.getSkeleton().getJoint(to);

     
        com.primesense.nite.Point2D<Float> fromPos = mTracker.convertJointCoordinatesToDepth(fromJoint.getPosition());
        com.primesense.nite.Point2D<Float> toPos = mTracker.convertJointCoordinatesToDepth(toJoint.getPosition());




        // draw it in another color than the use color
        g.setColor(new Color(mColors[(user.getId() + 1) % mColors.length]));
        g.drawLine(x + fromPos.getX().intValue(), y + fromPos.getY().intValue(), x + toPos.getX().intValue(), y + toPos.getY().intValue());
    }

    public synchronized void onNewFrame(UserTracker tracker) {
        if (mLastFrame != null) {
            mLastFrame.release();
            mLastFrame = null;
        }

        mLastFrame = mTracker.readFrame();

        // check if any new user detected
        for (UserData user : mLastFrame.getUsers()) {
            if (user.isNew()) {
                // start skeleton tracking
                mTracker.startSkeletonTracking(user.getId());
            }
        }

        VideoFrameRef depthFrame = mLastFrame.getDepthFrame();

        if (depthFrame != null) {
            ByteBuffer frameData = depthFrame.getData().order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer usersFrame = mLastFrame.getUserMap().getPixels().order(ByteOrder.LITTLE_ENDIAN);

            // make sure we have enough room
            if (mDepthPixels == null || mDepthPixels.length < depthFrame.getWidth() * depthFrame.getHeight()) {
                mDepthPixels = new int[depthFrame.getWidth() * depthFrame.getHeight()];
            }

            calcHist(frameData);
            frameData.rewind();

            int pos = 0;
            while (frameData.remaining() > 0) {
                short depth = frameData.getShort();
                short userId = usersFrame.getShort();
                short pixel = (short) mHistogram[depth];
                int color = 0xFFFFFFFF;
                if (userId > 0) {
                    color = mColors[userId % mColors.length];
                }

                mDepthPixels[pos] = color & (0xFF000000 | (pixel << 16) | (pixel << 8) | pixel);
                pos++;
            }
        }

        repaint();
    }

    private void calcHist(ByteBuffer depthBuffer) {
        // make sure we have enough room
        if (mHistogram == null) {
            mHistogram = new float[10000];
        }

        // reset
        for (int i = 0; i < mHistogram.length; ++i) {
            mHistogram[i] = 0;
        }

        int points = 0;
        while (depthBuffer.remaining() > 0) {
            int depth = depthBuffer.getShort() & 0xFFFF;
            if (depth != 0) {
                mHistogram[depth]++;
                points++;
            }
        }

        for (int i = 1; i < mHistogram.length; i++) {
            mHistogram[i] += mHistogram[i - 1];
        }

        if (points > 0) {
            for (int i = 1; i < mHistogram.length; i++) {
                mHistogram[i] = (int) (256 * (1.0f - (mHistogram[i] / (float) points)));
            }
        }
    }
    
    @Override
    public synchronized void paint(Graphics g) {
        if (mLastFrame==null) {
            return;
        }
       drawLimbs(g);
       parent.sensorMotors();
        
    }
    
  
}
