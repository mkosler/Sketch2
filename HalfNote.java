import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class HalfNote extends Boid
{
  private PImage _image;

  public HalfNote(PApplet parent, PVector position, PVector velocity, PImage image)
  {
    super(parent, position, velocity, 4.0f, 25.0f, 15.0f, 250.0f);

    _image = image;
  }

  public void draw(PGraphics canvas)
  {
    canvas.image(_image, _position.x, _position.y);
  }
}
