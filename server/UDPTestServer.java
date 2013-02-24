import hypermedia.net.UDP;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Scanner;

public class UDPTestServer
{
  private static final String IP = "localhost";
  private static final int PORT = 9000;

  private UDP _server;
  private InputStream _stream;

  private String _destinationIP;
  private int _destinationPort;

  public UDPTestServer(String destinationIP, int destinationPort)
  {
    _server = new UDP(this, PORT, IP);
    File f = new File("data/kinect_indirect_slower.bin");
    try {
      _stream = new FileInputStream(f);
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
    }

    try {
      System.out.println("Available bytes: " + _stream.available());
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }

    _destinationIP = destinationIP;
    _destinationPort = destinationPort;
  }

  public void push() throws IOException
  {
    System.out.println("Pushing across UDP");
    _server.send(readFromStream(_stream), _destinationIP, _destinationPort);
  }

  public void close()
  {
    _server.close();
  }
  
  private byte[] readFromStream(InputStream is) throws IOException
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] b = new byte[4 * 62]; // 4 * 62 = 248

    int count = is.read(b);
    baos.write(b, 0, count);

    System.out.println("BAOS Size: " + baos.size());

    return baos.toByteArray();
  }

  public static void main(String[] args)
  {
    UDPTestServer test = new UDPTestServer("localhost", 9100);
    Scanner s = new Scanner(System.in);

    System.out.println("Enter a number to push data. Exit by pressing 0");
    while (s.nextInt() > 0) {
      try {
        test.push();
      } catch (IOException e) {
        System.err.println(e.getMessage());
        break;
      }
    }

    test.close();
  }
}
