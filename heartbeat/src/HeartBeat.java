/*
  HeartBeat

  Saleem Bhatti, Oct 2018

  Send out a multicast heartbeat and listen out
  for other heartbeats.

*/

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class HeartBeat
{
  static Config            c_;
  static MulticastEndpoint m_;
  static String username_ = System.getProperty("user.name");

  public static void main(String args[])
  {
    c_ = new Config();
    m_ = new MulticastEndpoint(c_);

    m_.join();

    for (int h = c_.heartbeat_; h > 0; --h) {

      rxHeartBeat();

      txHeartBeat();

      try {
        Thread.sleep(c_.sleepTime_);
      }
      catch (InterruptedException e) {
        // do nothing
      }

    } // for (h)

    m_.leave();
  }


  static void rxHeartBeat()
  {
    byte[] b = new byte[c_.msgSize_];
    if (m_.rx(b) && b.length > 0) {
      System.out.println("-> rx : " + new String(b));
    }
  }

  static void txHeartBeat()
  {
    byte[] b = new byte[0];
    String h = heartBeat();

    try {
      b = h.getBytes("US-ASCII");
    }
    catch (UnsupportedEncodingException e) {
      System.out.println("Problem: " + e.getMessage());
    }

    if (m_.tx(b)) {
      System.out.println("<- tx : " + new String(b));
    }
  }


  static String heartBeat()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss.SSS");
    String now = sdf.format(new Date());
    String s = now + "|" + username_ + "|" + c_.hostInfo_;
    return s;
  }

}
