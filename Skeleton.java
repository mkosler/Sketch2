import processing.core.PVector;

public class Skeleton
{
  private static final int JOINT_COUNT = 60;

  private int _frameNumber;
  private int _skeletonNumber;
  private int[] _values;

  public enum Joint
  {
    HIP_CENTER(0), SPINE(1), SHOULDER_CENTER(2), HEAD(3), SHOULDER_LEFT(4), ELBOW_LEFT(5), WRIST_LEFT(6), HAND_LEFT(7), SHOULDER_RIGHT(8), ELBOW_RIGHT(9), WRIST_RIGHT(10), HAND_RIGHT(11), HIP_LEFT(12), KNEE_LEFT(13), ANKLE_LEFT(14), FOOT_LEFT(15), HIP_RIGHT(16), KNEE_RIGHT(17), ANKLE_RIGHT(18), FOOT_RIGHT(19);

    public final int id;

    Joint(int id)
    {
      this.id = id;
    }
  }

  public Skeleton()
  {
    _values = new int[JOINT_COUNT];
  }

  public void parse(byte[] stream)
  {
    _frameNumber    = ((stream[3] & 0xFF) << 24) | ((stream[2] & 0xFF) << 16) | ((stream[1] & 0xFF) << 8) | ((stream[0] & 0xFF) << 0);
    _skeletonNumber = ((stream[7] & 0xFF) << 24) | ((stream[6] & 0xFF) << 16) | ((stream[5] & 0xFF) << 8) | ((stream[4] & 0xFF) << 0);

    int j = 0;
    for (int i = 8; i < JOINT_COUNT; i += 4) {
      _values[j++] =((stream[i + 3] & 0xFF) << 24) | ((stream[i + 2] & 0xFF) << 16) | ((stream[i + 1] & 0xFF) << 8) | (stream[i] & 0xFF);
    }
  }

  public int getFrameNumber()
  {
    return _frameNumber;
  }

  public int getSkeletonNumber()
  {
    return _skeletonNumber;
  }

  public PVector getJoint(Joint j)
  {
    int i = j.id;
    return new PVector(_values[i * 4], _values[(i * 4) + 1], _values[(i * 4) + 2]);
  }
}
