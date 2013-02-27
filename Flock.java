import java.util.List;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class Flock
{
  private PApplet _parent;
  private List<Boid> _boids;
  private PVector _gatherPoint;
  private boolean _shouldGather;

  public Flock(PApplet parent)
  {
    _parent          = parent;
    _boids           = new LinkedList<Boid>();
    _gatherPoint     = new PVector(0, 0);
    _shouldGather    = false;
  }

  public void draw(PGraphics canvas)
  {
    for (Boid b : _boids) {
      PVector v = getCohesion(b);
      v.add(getAlignment(b));
      v.add(getSeparation(b));

      if (_shouldGather) {
        v.add(getGatherVector(b));
      }

      b.update(v);
      b.draw(canvas);
    }
  }

  public void add(Boid boid)
  {
    _boids.add(boid);
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

    if (v.mag() < 125) {
      return PVector.div(v, 2);
    } else {
      return PVector.div(v, 50);
    }
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
    centerMass.div(boid.getCohesionScale());
    
    return centerMass;
  }

  private PVector getSeparation(Boid boid)
  {
    PVector offset = new PVector(0, 0);

    for (Boid b : _boids) {
      if (b != boid && PVector.dist(b.getPosition(), boid.getPosition()) < boid.getMinimumDistance()) {
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
    align.div(boid.getAlignmentScale());

    return align;
  }
}
