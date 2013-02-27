//import codeanticode.syphon.SyphonServer;
import ddf.minim.AudioInput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import hypermedia.net.UDP;
import java.util.List;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

@SuppressWarnings("serial")
public class Sketch2 extends PApplet
{
  private static final int BOIDS_COUNT =   25;
  private static final int SPEED       = 1000;
  private static final int PORT        = 9100;

  private Flock _flock;
  private UDP _receiver;
  private List<PImage> _wholeNoteImages     = new ArrayList<PImage>();
  private List<PImage> _halfNoteImages      = new ArrayList<PImage>();
  private List<PImage> _quarterNoteImages   = new ArrayList<PImage>();
  private List<PImage> _eighthNoteImages    = new ArrayList<PImage>();
  private List<PImage> _sixteenthNoteImages = new ArrayList<PImage>();

  private AudioInput input;
  private Minim minim;
  private float pathWidth;
  private float offset;
  private boolean newStepA;
  private boolean newStepB;
  private AudioPlayer lowC;
  private AudioPlayer lowE;
  private AudioPlayer lowG;
  private AudioPlayer middleC;
  private AudioPlayer middleE;
  private AudioPlayer middleG;
  private AudioPlayer highC;
  private AudioPlayer highE;

  private PImage _keys;
  private PGraphics canvas;
  //private SyphonServer server;

  public void setup()
  {
    size(displayWidth, displayHeight, P3D);
    frameRate(30);

    canvas = createGraphics(displayWidth, displayHeight, P3D);
    //server = new SyphonServer(this, "Processing Server");

    _keys = loadImage("data/path-complete.png");

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

    pathWidth = displayWidth / 1.11f;
    offset = (displayWidth - pathWidth) / 2.0f;
    newStepA = newStepB = true;

    minim = new Minim(this);
    loadMusic();
  }

  public void draw()
  {
    canvas.beginDraw();
    canvas.background(0);
    canvas.image(_keys, 0, displayHeight - displayHeight / 3, displayWidth, displayHeight - displayHeight / 3);
    _flock.draw(canvas);
    canvas.endDraw();

    image(canvas, 0, 0);
    //server.sendImage(canvas);
    checkStep(mouseX, mouseY, mouseX, mouseY);
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

  public void stop()
  {
    lowC.close();
    lowE.close();
    lowG.close();
    middleC.close();
    middleE.close();
    middleG.close();
    highC.close();
    highE.close();

    minim.stop();

    super.stop();
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

  private void loadMusic()
  {
    lowC = minim.loadFile("data/lowC.mp3");
    lowE = minim.loadFile("data/lowE.mp3");
    lowG = minim.loadFile("data/lowG.mp3");
    middleC = minim.loadFile("data/middleC.mp3");
    middleE = minim.loadFile("data/middleE.mp3");
    middleG = minim.loadFile("data/middleG.mp3");
    highC = minim.loadFile("data/highC.mp3");
    highE = minim.loadFile("data/highE.mp3");
  }

  private void checkStep(int ax, int ay, int bx, int by)
  {
    if (displayHeight - ay < displayHeight/3 && newStepA == true && ax > offset){
        if (ax < pathWidth/8 + offset){
          print("Low C\n");
          lowC.cue(0);
          lowC.play();
        }
        else if (ax < 2*pathWidth/8 + offset){
          print("Low E\n");
          lowE.cue(0);
          lowE.play();
        }
        else if (ax < 3*pathWidth/8 + offset){
          print("Low G\n");
          lowG.cue(0);
          lowG.play();
        }
        else if (ax < 4*pathWidth/8 + offset){
          print("Middle C\n");
          middleC.cue(0);
          middleC.play();
        }
        else if (ax < 5*pathWidth/8 + offset){
          print("Middle E\n");
          middleE.cue(0);
          middleE.play();
          
        }
        else if (ax < 6*pathWidth/8 + offset){
          print("Middle G\n");
          middleG.cue(0);
          middleG.play();
        }
        else if (ax < 7*pathWidth/8 + offset){
          print("High C\n");
          highC.cue(0);
          highC.play();
        }
        else if(ax < pathWidth + offset){
          print("High E\n");
          highE.cue(0);
          highE.play();
        }
        newStepA = false;
      }
      else if (displayHeight - ay > displayHeight/3){
        newStepA = true;
      }
      
      if (displayHeight - by < displayHeight/3 && newStepB == true && bx > offset){
        if (bx < pathWidth/8 + offset){
          print("Low C\n");
          lowC.cue(0);
          lowC.play();
        }
        else if (bx < 2*pathWidth/8 + offset){
          print("Low E\n");
          lowE.cue(0);
          lowE.play();
        }
        else if (bx < 3*pathWidth/8 + offset){
          print("Low G\n");
          lowG.cue(0);
          lowG.play();
        }
        else if (bx < 4*pathWidth/8 + offset){
          print("Middle C\n");
          middleC.cue(0);
          middleC.play();
        }
        else if (bx < 5*pathWidth/8 + offset){
          print("Middle E\n");
          middleE.cue(0);
          middleE.play();
          
        }
        else if (bx < 6*pathWidth/8 + offset){
          print("Middle G\n");
          middleG.cue(0);
          middleG.play();
        }
        else if (bx < 7*pathWidth/8 + offset){
          print("High C\n");
          highC.cue(0);
          highC.play();
        }
        else if (bx < pathWidth + offset){
          print("High E\n");
          highE.cue(0);
          highE.play();
        }
        newStepB = false;
      }
      else if (displayHeight - by > displayHeight/3){
        newStepB = true;
      }
  }

  public static void main(String[] args)
  {
    PApplet.main(new String[] { "--present", "Sketch2" });
  }
}
