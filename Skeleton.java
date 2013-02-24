import processing.core.PVector;

public class Skeleton
{
  private static final int JOINT_COUNT = 60;

  private static int _frameNumber;
  private static int _skeletonNumber;
  private static int[] _values = new int[JOINT_COUNT];

  public static void parse(byte[] stream)
  {
    _frameNumber    = ((stream[3] & 0xFF) << 24) | ((stream[2] & 0xFF) << 16) | ((stream[1] & 0xFF) << 8) | ((stream[0] & 0xFF) << 0);
    _skeletonNumber = ((stream[7] & 0xFF) << 24) | ((stream[6] & 0xFF) << 16) | ((stream[5] & 0xFF) << 8) | ((stream[4] & 0xFF) << 0);

    System.out.println("_frameNumber = " + _frameNumber);
    System.out.println("_skeletonNumber = " + _skeletonNumber);

    int j = 0;
    for (int i = 8; i < JOINT_COUNT; i += 4) {
      _values[j++] =((stream[i + 3] & 0xFF) << 24) | ((stream[i + 2] & 0xFF) << 16) | ((stream[i + 1] & 0xFF) << 8) | (stream[i] & 0xFF);

      if (j % 3 == 0) {
        System.out.printf("Joint ( %d, %d, %d)\n", _values[j - 2], _values[j - 1], _values[j]);
      }
    }
  }

  public static int getFrameNumber()
  {
    return _frameNumber;
  }

  public static int getSkeletonNumber()
  {
    return _skeletonNumber;
  }

  public static PVector getJoint(Joint j)
  {
    int i = j.id;
    return new PVector(_values[i * 4], _values[(i * 4) + 1], _values[(i * 4) + 2]);
  }
}
