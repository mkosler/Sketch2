import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class Boid
{
  private static final int BOUND_BUFFER  =  50;
  private static final int WIDTH         =  10;
  private static final int HEIGHT        =   5;

  protected PApplet _parent;
  protected PVector _position;
  protected PVector _velocity;

  private float _cohesionScale;
  private float _minimumDistance;
  private float _alignmentScale;
  private float _maximumSpeed;

  public Boid(PApplet parent,
              PVector position,
              PVector velocity,
              float cohesionScale,
              float minimumDistance,
              float alignmentScale,
              float maximumSpeed)
  {
    _parent          = parent;
    _position        = position;
    _velocity        = velocity;
    _cohesionScale   = cohesionScale;
    _minimumDistance = minimumDistance;
    _alignmentScale  = alignmentScale;
    _maximumSpeed    = maximumSpeed;
  }

  public void update(PVector velocity)
  {
    _velocity.add(velocity);

    if (_velocity.mag() > _maximumSpeed) {
      _velocity.normalize();
      _velocity.mult(_maximumSpeed);
    }
    _velocity.add(bound());
    _position.add(PVector.mult(_velocity, 1 / _parent.frameRate));
  }

  public void draw(PGraphics canvas)
  {
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
    } else if (_position.y > _parent.height / 2) {
      v.y = -BOUND_BUFFER;
    }

    return v;
  }

  public float getCohesionScale()
  {
    return _cohesionScale;
  }

  public float getMinimumDistance()
  {
    return _minimumDistance;
  }

  public float getAlignmentScale()
  {
    return _alignmentScale;
  }
}
