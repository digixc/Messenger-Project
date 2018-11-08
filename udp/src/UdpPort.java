/**
 * Simple, UDP port abstraction
 *
 * Saleem Bhatti
 * Oct 2018
 *
 */

import java.io.*;
import java.net.*;

class UdpPort {

  DatagramSocket server_ = null;
  InetAddress dstAddr_;
  int dstPort_;

  final int soTimeout_ = 10; // ms

  UdpPort(int port, String dstHost, String dstPort)
  {
    try {
      dstAddr_ = InetAddress.getByName(dstHost);
      dstPort_ = Integer.parseInt(dstPort);

      if (dstPort_ < 0) {
        System.err.println("Bad port number: " + dstPort);
        System.exit(0);
      }
    }
    catch (UnknownHostException e) {
      System.err.println("Unknown host: " + e.getMessage());
      System.exit(0);
    }


    try {
      // create socket
      server_ = new DatagramSocket(port, InetAddress.getLocalHost());

      System.out.println("Starting server");
      System.out.println("host: "
                         + server_.getLocalAddress().getHostName() + "/"
                         + server_.getLocalAddress().getHostAddress()
                         + ":" + server_.getLocalPort());

      System.out.println("destination port: "
                         + dstAddr_.getHostName() + " "
                         + dstAddr_.getHostAddress()
                         + ":" + port);

      server_.setSoTimeout(soTimeout_); // non-blocking
    }
    catch (UnknownHostException e) {
      System.err.println("Unknown host: " + e.getMessage());
    }
    catch (SocketException e) {
      System.err.println("Socket problem: " + e.getMessage());
    }
  } // UdpPort


  public String rx() {

    String line = null;

    try {
      byte[] buffer = new byte[256];
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      server_.receive(packet);
      String text = new String(packet.getData(), 0, packet.getLength());

      line = new String(packet.getAddress().getHostName() + ":"
            + packet.getAddress().getHostAddress() + ":"
            + packet.getPort() + " -> "
            + text);
    }
    catch (SocketTimeoutException e) {
      // do nothing - no incoming requests
    }
    catch (IOException e) {
      System.err.println("IO problem: " + e.getMessage());
    }

    return line;
  } // rx()


  public void tx(String line) {

    try {
      DatagramPacket packet =
      new DatagramPacket(line.getBytes(), line.length(),
                         dstAddr_, dstPort_);
      server_.send(packet);
    }
    catch (IOException e) {
      System.err.println("IO problem: " + e.getMessage());
    }

  } // tx()

  public void finalize() {
    if (server_ != null) { server_.close(); }
  } // finalize()

} // class
