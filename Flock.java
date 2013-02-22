import java.util.List;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PVector;

public class Flock
{
  private PApplet _parent;

  private int _cohesionScale;
  private int _minimumDistance;
  private int _alignmentScale;

  private List<Boid> _boids;

  private PVector _gatherPoint;

  private boolean _shouldGather;

  public Flock(PApplet parent, int cohesionScale, int minimumDistance, int alignmentScale)
  {
    _parent          = parent;

    _cohesionScale   = cohesionScale;
    _minimumDistance = minimumDistance;
    _alignmentScale  = alignmentScale;

    _boids           = new LinkedList<Boid>();

    _gatherPoint     = new PVector(0, 0);

    _shouldGather    = false;
  }

  public void draw()
  {
    for (Boid b : _boids) {
      PVector v = getCohesion(b);
      v.add(getAlignment(b));
      v.add(getSeparation(b));

      if (_shouldGather) {
        v.add(getGatherVector(b));
      }

      b.updateVelocity(v);
      b.draw();
    }
  }

  public void add(float x, float y, float vx, float vy)
  {
    _boids.add(new Boid(_parent, new PVector(x, y), new PVector(vx, vy)));
  }

  public PVector getGatherPoint()
  {
    return _gatherPoint;
  }

  public void setGatherPoint(float x, float y)
  {
    _gatherPoint.x = PApplet.max(0, x);
    _gatherPoint.y = PApplet.min(_parent.width, y);

    System.out.println("Gather Point: " + _gatherPoint);
  }

  public void setShouldGather(boolean b)
  {
    _shouldGather = b;
  }

  private PVector getGatherVector(Boid boid)
  {
    PVector v = PVector.sub(_gatherPoint, boid.getPosition());
    v.div(_cohesionScale);

    if (v.mag() > 50) {
      v.normalize();
      v.mult(50);
    }

    return v;
  }

  private PVector getCohesion(Boid boid)
  {
    PVector centerMass = new PVector(0, 0);

    for (Boid b : _boids) {
      if (b != boid) {
        centerMass.add(b.getPosition());
      }
    }

    centerMass.div(_boids.size() - 1);
    centerMass.sub(boid.getPosition());
    centerMass.div(_cohesionScale);
    
    return centerMass;
  }

  private PVector getSeparation(Boid boid)
  {
    PVector offset = new PVector(0, 0);

    for (Boid b : _boids) {
      if (b != boid && PVector.dist(b.getPosition(), boid.getPosition()) < _minimumDistance) {
        offset.sub(PVector.mult(PVector.sub(b.getPosition(), boid.getPosition()), 0.5f));
      }
    }

    return offset;
  }

  private PVector getAlignment(Boid boid)
  {
    PVector align = new PVector(0, 0);

    for (Boid b : _boids) {
      if (b != boid) {
        align.add(b.getVelocity());
      }
    }

    align.div(_boids.size());
    align.sub(boid.getVelocity());
    align.div(_alignmentScale);

    return align;
  }
}
