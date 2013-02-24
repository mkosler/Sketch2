import hypermedia.net.UDP;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class Sketch2 extends PApplet
{
  private static final int BOIDS_COUNT =  25;
  private static final int SPEED       = 150;

  private static final String IP = "localhost";
  private static final int PORT = 9100;

  private Flock _flock;

  private UDP _receiver;

  public void setup()
  {
    size(displayWidth, displayHeight);
    frameRate(30);

    _flock = new Flock(this, 8, 30, 10);

    _receiver = new UDP(this, PORT, IP);
    _receiver.listen(true);

    for (int i = 0; i < BOIDS_COUNT; i++) {
      _flock.add(random(width), random(height), random(-SPEED, SPEED), random(-SPEED, SPEED));
    }
  }

  public void draw()
  {
    background(0);

    _flock.draw();
  }

  public void receive(byte[] message)
  {
    Skeleton.parse(message, true);

    System.out.println("Left Hand: " + Skeleton.getJoint(Joint.HAND_LEFT));
    System.out.println("Right Hand: " + Skeleton.getJoint(Joint.HAND_RIGHT));
  }

  public static void main(String[] args)
  {
    PApplet.main(new String[] { "--present", "Sketch2" });
  }
}
