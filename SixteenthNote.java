import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class SixteenthNote extends Boid
{
  private PImage _image;

  public SixteenthNote(PApplet parent, PVector position, PVector velocity, PImage image)
  {
    super(parent, position, velocity, 15.0f, 25.0f, 75.0f, 1000.0f);

    _image = image;
  }

  public void draw()
  {
    _parent.image(_image, _position.x, _position.y);
  }
}
