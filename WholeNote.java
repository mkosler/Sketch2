import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class WholeNote extends Boid
{
  private PImage _image;

  public WholeNote(PApplet parent, PVector position, PVector velocity, PImage image)
  {
    super(parent, position, velocity, 12.0f, 45.0f, 15.0f, 150.0f);

    _image = image;
  }

  public void draw(PGraphics canvas)
  {
    canvas.image(_image, _position.x, _position.y);
  }
}
