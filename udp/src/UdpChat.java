    /**
 * Simple, blocking UDP server loops until "quit" received
 *
 * Saleem Bhatti
 * Oct 2018
 *
 */

import java.io.*;
import java.net.*;

public class UdpChat {

  private static int sleepTime_ = 1000; // 1 second
  private static int port_ = 51159; // your port number here

  public static void main(String[] args)
  {
    if (args.length != 2) { // user has not provided arguments
      System.out.println("\n UdpChat1 <destination host> <destination port> \n");
      System.exit(0);
    }

    UdpPort udpPort = new UdpPort(port_, args[0], args[1]);

    String quit = new String("quit"); // to stop the program
    boolean flag = true;

    while (flag) {

      String line;

      line = ByteReader.readLine(System.in);
      if (line != null) {
        udpPort.tx(line);
        if (line.equalsIgnoreCase(quit)) { flag = false; }
      }

      line = udpPort.rx();
      if (line != null) {
        System.out.println("rx -> : " + line);
      }

      try {
        Thread.sleep(sleepTime_);
      }
      catch (InterruptedException e) {
        // do nothing ...
      }

    }

  } // main()

} // class
