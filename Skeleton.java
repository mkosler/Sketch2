import processing.core.PVector;

public class Skeleton
{
  private static final int JOINT_COUNT = 60;

  private static int _frameNumber;
  private static int _skeletonNumber;
  private static float[] _values = new float[JOINT_COUNT];

  public static void parse(byte[] stream, boolean isBinary)
  {
    if (isBinary) {
      parseBinary(stream);
    } else {
      parseASCII(stream);
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
    return new PVector(_values[i * 3], _values[(i * 3) + 1], _values[(i * 3) + 2]);
  }

  private static void parseBinary(byte[] stream)
  {
    for (int i = 0; i < JOINT_COUNT + 2; i++) {
      int v = ((stream[i * 4 + 3] & 0xFF) << 24) |
              ((stream[i * 4 + 2] & 0xFF) << 16) |
              ((stream[i * 4 + 1] & 0xFF) <<  8) |
              ((stream[i * 4 + 0] & 0xFF) <<  0);

      if (i == 0) {
        _frameNumber = v;
      } else if (i == 1) {
        _skeletonNumber = v;
      } else {
        _values[i - 2] = v * 0.001f;
      }
    }

    System.out.println("_frameNumber: " + _frameNumber);
    System.out.println("_skeletonNumber: " + _skeletonNumber);

    for (int i = 0; i < JOINT_COUNT; i += 3) {
      System.out.printf("Joint (%f, %f, %f)\n", _values[i], _values[i + 1], _values[i + 2]);
    }
  }

  private static void parseASCII(byte[] stream)
  {
  }
}
