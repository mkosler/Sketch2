import hypermedia.net.UDP;
import java.util.*;
import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.*;

@SuppressWarnings("serial")
public class SingleKinect extends PApplet
{
  private static final String IP = "";
  private static final int PORT = 9000;

  private static final String DESTINATION_IP = "";
  private static final int DESTINATION_PORT = 9100;

  private SimpleOpenNI _kinect;
  private UDP _server;

  public void setup()
  {
  }

  private void setupKinect()
  {
    _kinect = new SimpleOpenNI(this);
    _kinect.setMirror(false);
    _kinect.enableDepth();

    _kinect.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
  }

  private void setupUDPServer()
  {
    _server = new UDP(this, PORT, IP);
  }

  public void draw()
  {
    _kinect.update();

    IntVector userList = new IntVector();

    _kinect.getUsers();

    if (userList.size() > 0) {
      int userID = userList.get(0);

      if (_kinect.isTrackingSkeleton(userID)) {
        PVector leftFoot = new PVector();
        PVector rightFoot = new PVector();
        PVector head = new PVector();

        float lfConfidence = _kinect.getJointPositionSkeleton(userID, SimpleOpenNI.SKEL_RIGHT_FOOT, leftFoot);
        float rfConfidence = _kinect.getJointPositionSkeleton(userID, SimpleOpenNI.SKEL_LEFT_FOOT, rightFoot);
        float hConfidence  = _kinect.getJointPositionSkeleton(userID, SimpleOpenNI.SKEL_HEAD, head);

        PVector convertedRightFoot = new PVector();
        _kinect.convertRealWorldToProjective(rightFoot, convertedRightFoot);

        PVector convertedLeftFoot = new PVector();
        _kinect.convertRealWorldToProjective(leftFoot, convertedLeftFoot);

        PVector convertedHead = new PVector();
        _kinect.convertRealWorldToProjective(head, convertedHead);

        _server.send(getMessageString(convertedRightFoot, convertedLeftFoot, convertedHead),
            DESTINATION_IP, DESTINATION_PORT);
      }
    }
  }

  public void onNewUser(int uID)
  {
    System.out.println("Starting pose detection");
    _kinect.startPoseDetection("Psi", uID);
  }

  public void onEndCalibration(int uID, boolean successful)
  {
    if (successful) {
      System.out.println("USER CALIBRATED!!!");
      _kinect.startTrackingSkeleton(uID);
    } else {
      System.out.println("FAILED CALIBRATION!!!");
      _kinect.startPoseDetection("Psi", uID);
    }
  }

  public void onStartPose(String pose, int uID)
  {
    System.out.println("Started pose for user");
    _kinect.stopPoseDetection(uID);
    _kinect.requestCalibrationSkeleton(uID, true);
  }

  private String getMessageString(PVector rf, PVector lf, PVector h)
  {
    StringBuilder sb = new StringBuilder();

    sb.append(rf.x); sb.append(' ');
    sb.append(rf.y); sb.append(' ');
    sb.append(rf.z); sb.append(' ');
    sb.append(lf.x); sb.append(' ');
    sb.append(lf.x); sb.append(' ');
    sb.append(lf.y); sb.append(' ');
    sb.append(h.z);  sb.append(' ');
    sb.append(h.y);  sb.append(' ');
    sb.append(h.z);

    return sb.toString();
  }

  public static void main(String[] args)
  {
    PApplet.main(new String[] { "--present", "SingleKinect" });
  }
}
