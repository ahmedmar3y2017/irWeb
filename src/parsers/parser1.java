/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

//import file3.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class parser1 {

  static ServerSocket serverSocket = null;
  static int counter = 0;
  static Map<Integer, ArrayList<String>> map = new HashMap<Integer, ArrayList<String>>();
  ArrayList<String> firstRange = new ArrayList<String>();
  ArrayList<String> lastRange = new ArrayList<String>();
  ArrayList<String> firstDoc = new ArrayList<String>();
  ArrayList<String> lastDoc = new ArrayList<String>();
  ArrayList<String> Stop_words = new ArrayList<String>();

  public static void main(String[] args) throws IOException {

    try {
      serverSocket = new ServerSocket(4444);

      // System.out.println("Waiting...");
      Socket socket = null;
      InputStream in = null;
      OutputStream out = null;
      while (true) {
        System.out.println("Waiting...");
        try {
          socket = serverSocket.accept();

          DataInputStream din = new DataInputStream(socket.getInputStream());
          DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
          // recieve Data From
          String fileName = din.readUTF();
          int number = din.readInt();

          try {
            in = socket.getInputStream();
          } catch (IOException ex) {
            System.out.println("Can't get socket input stream. ");
          }
          // create AFile
          File ff = new File("D:\\parser1\\" + fileName);
          // If Not Exist Create One ...
          if (!ff.exists()) {
            ff.createNewFile();
          }
          try {
            out = new FileOutputStream(ff);
          } catch (FileNotFoundException ex) {
            System.out.println("File not found. ");
          }

          byte[] bytes = new byte[16 * 1024];

          int count;
          while ((count = in.read(bytes)) > 0) {
            out.write(bytes, 0, count);
          }
          counter++;
          // server will stop if all client send to this server ..... no service
          if (number == counter) {

            break;
          }

        } finally {
          // close all buffers
          if (out != null) {
            out.close();
          }
          if (in != null) {
            in.close();
          }
          if (socket != null) {
            socket.close();
          }

        }

      }
    } catch (IOException ex) {
      System.out.println("Can't setup server on this port number. ");
    } finally {

      serverSocket.close();
    }

    Map ppp = makeAlgorithm();
    makeCLient(ppp);

  }

  private static void makeCLient(Map map) {

    try {
      Socket s = new Socket("localhost", 6666);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());
      objectOutputStream.writeObject(map);
      objectOutputStream.flush();
      objectOutputStream.close();
      s.close();
    } catch (Exception e) {
      System.out.println(e);
    }

  }

  // write parser algorhihms

  private static Map makeAlgorithm() throws FileNotFoundException {
    // to get all Files from specific folder
    File folder = new File("D:\\parser1");
    File[] listOfFiles = folder.listFiles();
    ArrayList<File> arrayList = new ArrayList<File>(Arrays.asList(listOfFiles));
    // try {
    Map mm = new parser1().mapping(arrayList);
    // ArrayList<String> arrayList1 = new ArrayList<>();
    // arrayList1.add("ahmed");
    // ArrayList<String> arrayList2 = new ArrayList<>();
    // arrayList2.add("mohamed");
    // ArrayList<String> arrayList3 = new ArrayList<>();
    // arrayList3.add("selam");
    // ArrayList<String> arrayList4 = new ArrayList<>();
    // arrayList4.add("ppppp");
    //
    // map.put(1, arrayList1);
    // map.put(2, arrayList2);
    // map.put(3, arrayList3);
    // map.put(4, arrayList4);

    return mm;
  }

  public Map mapping(ArrayList<File> file) throws FileNotFoundException {
    Stop_words.add("the");
    Stop_words.add("an");
    Stop_words.add("is");
    Stop_words.add("are");
    Stop_words.add("am");
    Stop_words.add("was");
    Stop_words.add("where");
    Stop_words.add("can");
    Stop_words.add("@");
    Stop_words.add("#");
    Stop_words.add("$");
    Stop_words.add("%");
    Stop_words.add("*");
    Stop_words.add("in");
    Stop_words.add("what");
    Stop_words.add("why");
    Stop_words.add("we");
    Stop_words.add("IN");
    for (File f : file) {
      Scanner scan = new Scanner(f);
      // ------------> split each doc into lines....

      while (scan.hasNext()) {
        String line = scan.nextLine();
        StringTokenizer toke = new StringTokenizer(line, " ");
        // ------------> split each line into tokens....
        while (toke.hasMoreTokens()) {
          String s = toke.nextToken();
          // ------------> store each token in array List.......
          boolean stop = false;
          for (int i = 0; i < Stop_words.size(); i++) {
            if (Stop_words.get(i).equalsIgnoreCase(s)) {
              stop = true;
            }
          }
          if (!stop) {
            if (s.charAt(0) < 'm') {
              firstRange.add(s);
              firstDoc.add(f.getAbsolutePath());
            } else {
              lastRange.add(s);
              lastDoc.add(f.getAbsolutePath());
            }
          }

        }
      }

    }

    map.put(1, firstRange);
    map.put(2, firstDoc);
    map.put(3, lastRange);
    map.put(4, lastDoc);

    return map;

  }

}
