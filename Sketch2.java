import hypermedia.net.UDP;
import java.util.List;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

@SuppressWarnings("serial")
public class Sketch2 extends PApplet
{
  private static final int BOIDS_COUNT =   25;
  private static final int SPEED       = 1000;

  private static final String IP = "192.168.1.123";
  private static final int PORT  = 9100;

  private Flock _flock;

  private UDP _receiver;

  private List<PImage> _wholeNoteImages     = new ArrayList<PImage>();
  private List<PImage> _halfNoteImages      = new ArrayList<PImage>();
  private List<PImage> _quarterNoteImages   = new ArrayList<PImage>();
  private List<PImage> _eighthNoteImages    = new ArrayList<PImage>();
  private List<PImage> _sixteenthNoteImages = new ArrayList<PImage>();

  public void setup()
  {
    size(displayWidth, displayHeight);
    frameRate(30);

    _flock = new Flock(this);

    _receiver = new UDP(this, PORT);
    _receiver.listen(true);

    _wholeNoteImages.add(loadImage("data/whole-note.png"));

    _halfNoteImages.add(loadImage("data/half-note.png"));
    _halfNoteImages.add(loadImage("data/half-note_upsidedown.png"));

    _quarterNoteImages.add(loadImage("data/quarter-note.png"));
    _quarterNoteImages.add(loadImage("data/quarter-note_upsidedown.png"));

    _eighthNoteImages.add(loadImage("data/8th-note.png"));
    _eighthNoteImages.add(loadImage("data/8th-note_upsidedown.png"));

    _sixteenthNoteImages.add(loadImage("data/16th-note.png"));
    _sixteenthNoteImages.add(loadImage("data/16th-note_upsidedown.png"));

    for (int i = 0; i < BOIDS_COUNT; i++) {
      _flock.add(generateBoid(width, height, SPEED));
    }
  }

  public void draw()
  {
    background(0);

    _flock.draw();
  }

  public void keyPressed()
  {
    if (key == 'g') {
      _flock.setGatherPoint(mouseX, mouseY);
      _flock.setShouldGather(true);
    } else if (key == 'G') {
      _flock.setShouldGather(false);
    }
  }

  public void receive(byte[] message)
  {
    Skeleton.parseBinary(message);

    System.out.println("Left Hand: " + Skeleton.getJoint(Joint.HAND_LEFT));
    System.out.println("Right Hand: " + Skeleton.getJoint(Joint.HAND_RIGHT));
  }

  private Boid generateBoid(float w, float h, float s)
  {
    PVector position = new PVector(random(w), random(h));
    PVector velocity = new PVector(random(-s, s), random(-s, s));

    float r = random(1.0f);
    int index = (int) random(0, 2);

    if (r < 0.2f) {
      return new WholeNote(this, position, velocity, _wholeNoteImages.get(0));
    } else if (r < 0.4f) {
      return new HalfNote(this, position, velocity, _halfNoteImages.get(index));
    } else if (r < 0.6f) {
      return new QuarterNote(this, position, velocity, _quarterNoteImages.get(index));
    } else if (r < 0.8f) {
      return new EighthNote(this, position, velocity, _eighthNoteImages.get(index));
    } else {
      return new SixteenthNote(this, position, velocity, _sixteenthNoteImages.get(index));
    }
  }

  public static void main(String[] args)
  {
    PApplet.main(new String[] { "--present", "Sketch2" });
  }
}
