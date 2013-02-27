import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class QuarterNote extends Boid
{
  private PImage _image;

  public QuarterNote(PApplet parent, PVector position, PVector velocity, PImage image)
  {
    super(parent, position, velocity, 8.0f, 25.0f, 10.0f, 500.0f);

    _image = image;
  }

  public void draw(PGraphics canvas)
  {
    canvas.image(_image, _position.x, _position.y);
  }
}
