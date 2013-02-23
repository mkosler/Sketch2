import hypermedia.net.UDP;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class UDPTestServer
{
  private static final String IP = "localhost";
  private static final int PORT = 9000;

  private UDP _server;
  private ByteArrayInputStream _stream;

  private String _destinationIP;
  private int _destinationPort;

  public UDPTestServer(String destinationIP, int destinationPort)
  {
    _server = new UDP(this, PORT, IP);
    _stream = (ByteArrayInputStream) getClass().getResourceAsStream("/data/kinect_indirect_slower.bin");

    _destinationIP = destinationIP;
    _destinationPort = destinationPort;
  }

  public void push()
  {
    _stream.reset();
    byte[] b = new byte[_stream.available()];
    if (_stream.read(b, 0, _stream.available()) < 0) {
      System.out.println("Something is wrong!");
    }
    _server.send(b, _destinationIP, _destinationPort);
  }

  public void close()
  {
    _server.close();
  }

  public static void main(String[] args)
  {
    UDPTestServer test = new UDPTestServer("localhost", 9100);
    Scanner s = new Scanner(System.in);

    while (s.nextInt() > 0) {
      test.push();
    }

    test.close();
  }
}
