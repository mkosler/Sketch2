import processing.core.PApplet;
import processing.core.PVector;

public class Boid
{
  private static final int MAXIMUM_SPEED = 300;
  private static final int BOUND_BUFFER  =  50;
  private static final int WIDTH         =  10;
  private static final int HEIGHT        =   5;
  private PApplet _parent;
  private PVector _position;
  private PVector _velocity;

  public Boid(PApplet parent, PVector position, PVector velocity)
  {
    _parent   = parent;
    _position = position;
    _velocity = velocity;
  }

  public void draw()
  {
    if (_velocity.mag() > MAXIMUM_SPEED) {
      _velocity.normalize();
      _velocity.mult(MAXIMUM_SPEED);
    }
    _velocity.add(bound());
    _position.add(PVector.mult(_velocity, 1 / _parent.frameRate));

    _parent.fill(0, 255, 0);
    _parent.stroke(255, 255, 0);
    _parent.ellipse(_position.x, _position.y, WIDTH, HEIGHT);
  }

  public void updateVelocity(PVector velocity)
  {
    _velocity.add(velocity);
  }

  public PVector getPosition()
  {
    return _position;
  }

  public PVector getVelocity()
  {
    return _velocity;
  }

  private PVector bound()
  {
    PVector v = new PVector(0, 0);

    if (_position.x < 0) {
      v.x = BOUND_BUFFER;
    } else if (_position.x > _parent.width) {
      v.x = -BOUND_BUFFER;
    }

    if (_position.y < 0) {
      v.y = BOUND_BUFFER;
    } else if (_position.y > _parent.height) {
      v.y = -BOUND_BUFFER;
    }

    return v;
  }
}
