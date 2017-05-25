/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Master;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import Document.document;

/**
 *
 * @author ahmed
 */
public class master {
  // arrayList contain all file paths

  static ArrayList<String> filesPath = new ArrayList<String>();
  // arrayList contain all file Names

  static ArrayList<String> filesName = new ArrayList<String>();
  // list of files
  static File[] listOfFiles;
  public HashMap FinalMap;
  // to build index
  static HashMap index;
  static ArrayList<String> all_terms = new ArrayList<String>();

  // *********************** constructor ********************************
  // -------using constactor to store all terms in and to store your hashmap-----
  public static void BuildIndex(HashMap index) {

    master.index = index;
    Collection<document> Collection = index.values();
    ArrayList<document> all = new ArrayList();
    for (int i = 0; i < all.size(); i++) {
      document d = (document) all.get(i);
      Set s = d.doc.keySet();
      all_terms.addAll(s);
    }

  }

  // ********************** search method ****************************
  public String search(String Quary) {

    String Result = "";
    ArrayList<String> q_term = new ArrayList<String>();
    ArrayList<Double> TF = new ArrayList<Double>();
    ArrayList<Double> DF = new ArrayList<Double>();
    ArrayList<Double> weight = new ArrayList<Double>();

    // ------------------------(Quary)---------------------//
    StringTokenizer toke = new StringTokenizer(Quary, " ");
    while (toke.hasMoreTokens()) {
      String s = toke.nextToken();
      double tf = 1;
      int i = 0;
      for (i = 0; i < q_term.size(); i++) {
        if (s.equalsIgnoreCase(q_term.get(i))) {
          tf = TF.get(i) + 1;
          break;
        }
      }
      if (tf == 1) {
        q_term.add(s);
        TF.add(tf);
      } else {
        TF.set(i, tf);
      }
    }

    for (int i = 0; i < q_term.size(); i++) {
      double Df = 0;
      for (int j = 0; j < all_terms.size(); j++) {
        if (q_term.get(i).equalsIgnoreCase(all_terms.get(j))) {
          Df++;
        }
      }
      DF.add(listOfFiles.length / Df);
    }
    double lengh = 0;
    for (int i = 0; i < TF.size(); i++) {
      lengh += Math.pow(((1 + Math.log(TF.get(i))) * (Math.log(DF.get(i)))), 2);
      weight.add((1 + Math.log(TF.get(i))) * (Math.log(DF.get(i))));
    }
    lengh = Math.sqrt(lengh);
    for (int i = 0; i < q_term.size(); i++) {
      weight.set(i, weight.get(i) / lengh);
      System.out.println(weight.get(i) + "   " + q_term.get(i));

    }
    // ------------------(document)--------------------//
    Iterator it = (Iterator) index.keySet().iterator();
    ArrayList<Double> Score = new ArrayList<Double>();
    ArrayList<Double> sorted = new ArrayList();

    ArrayList<String> Match_document = new ArrayList<String>();
    while (it.hasNext()) {
      Object temp_doc = it.next();
      System.out.println(temp_doc.toString());
      document docs = (document) index.get(temp_doc);
      Collection co = docs.doc.values();
      ArrayList<Double> Wt = new ArrayList();
      Wt.addAll(co);
      Collection set = docs.doc.keySet();
      ArrayList<String> term = new ArrayList<String>();
      term.addAll(set);

      double lenght2 = 0;
      for (int i = 0; i < Wt.size(); i++) {
        lenght2 += Math.pow(Wt.get(i), 2);
      }
      lenght2 = Math.sqrt(lenght2);
      for (int i = 0; i < Wt.size(); i++) {
        Wt.set(i, Wt.get(i) / lenght2);
      }
      double sco = 0;

      for (int i = 0; i < q_term.size(); i++) {
        for (int j = 0; j < term.size(); j++) {
          if (q_term.get(i).equalsIgnoreCase(term.get(j).toString())) {
            sco += Wt.get(j) * weight.get(i);
            break;
          }
        }

      }
      Score.add(sco);
      Match_document.add(temp_doc.toString());

    }
    sorted.addAll(Score);
    sorted.sort(null);
    for (int i = 1; i < 4; i++) {
      Result += "\n" + Match_document.get(Score.indexOf(sorted.get(sorted.size() - i)));
      System.out.println(sorted.get(sorted.size() - i) + "   "
          + Match_document.get(Score.indexOf(sorted.get(sorted.size() - i))));
    }

    return Result;
  }

  public static void main(String[] args) {

    // to get all Files from specific folder
    File folder = new File("C:\\Users\\ahmed\\Desktop\\files");
    listOfFiles = folder.listFiles();
    for (File f : listOfFiles) {
      // add all files name and paths to arraylist
      filesPath.add(f.getAbsolutePath());
      filesName.add(f.getName());

    }
    new master().send();

  }

  public void send() {

    int numberOfFiles = listOfFiles.length;
    // parser1 files
    int parser1 = numberOfFiles / 2;
    // parser2 files
    int counter = 0;
    int parser2 = numberOfFiles - parser1;
    // sendnumber of files to parser1
    for (int i = 0; i < parser1; i++) {
      counter++;
      try {
        // method FileSender to send file from master to parser by file path , file name , specific
        // ip , specific port , NUmber of files
        FileSender(filesPath.get(i), filesName.get(i), "127.0.0.1", 4444, parser1);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }

    // make server to recieve data from parser1
    Map<Integer, ArrayList<String>> map1 = makeServer1();
    // send Another files to parser2
    for (int i = counter; i < numberOfFiles; i++) {
      try {
        System.out.println(i);
        // send Some files To Parser 1 .......
        FileSender(filesPath.get(i), filesName.get(i), "127.0.0.1", 55555, parser2);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    // make server to recieve data from parser 2
    Map<Integer, ArrayList<String>> map2 = makeServer2();

    // amake ArrayList That contain map 1 from parser 1 and map 2 from parser2
    ArrayList<Map<Integer, ArrayList<String>>> arrayList = new ArrayList<Map<Integer, ArrayList<String>>>();
    // add map 1
    arrayList.add(map1);
    // add map 2
    arrayList.add(map2);
    // send this array List to inverter
    makeClient1(arrayList);
    HashMap map = makeServer3();
    // build index
    BuildIndex(map);
    // search Method

    System.out.println(search("ahmed mar3y") + "     ****************************-  ");

    // search algorithms ****************************************************************
    // System.out.println(map + "    **********************************************");
  }

  // senf file to parser to make Some Operations and algorithms

  public void FileSender(String FilePath, String fileName, String ip, int port, int number)
      throws IOException {
    Socket socket = null;

    socket = new Socket(ip, port);

    File file = new File(FilePath);
    // Get the size of the file
    long length = file.length();
    byte[] bytes = new byte[16 * 1024];
    InputStream in = new FileInputStream(file);
    OutputStream out = socket.getOutputStream();
    DataInputStream din = new DataInputStream(socket.getInputStream());
    DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
    // din.
    dout.writeUTF(fileName);
    dout.flush();
    dout.writeInt(number);
    dout.flush();

    int count;
    while ((count = in.read(bytes)) > 0) {
      out.write(bytes, 0, count);
    }

    dout.close();
    din.close();
    out.close();
    in.close();
    socket.close();

  }

  // server 1 will get object (recieve) from parser 1

  private Map<Integer, ArrayList<String>> makeServer1() {
    Map<Integer, ArrayList<String>> map = null;
    try {
      ServerSocket ss = new ServerSocket(6666);
      Socket s = ss.accept();// establishes connection
      ObjectInputStream dis = new ObjectInputStream(s.getInputStream());
      map = (Map<Integer, ArrayList<String>>) dis.readObject();
      System.err.println("message= " + map);
      ss.close();
    } catch (Exception e) {
      System.out.println(e);
    }
    return map;

  }

  // server 2 will get object (recieve) from parser 2

  private Map<Integer, ArrayList<String>> makeServer2() {
    Map<Integer, ArrayList<String>> map = null;
    try {
      ServerSocket ss = new ServerSocket(7777);
      Socket s = ss.accept();// establishes connection
      ObjectInputStream dis = new ObjectInputStream(s.getInputStream());
      map = (Map<Integer, ArrayList<String>>) dis.readObject();
      System.err.println("message= " + map);
      ss.close();
    } catch (Exception e) {
      System.out.println(e);
    }
    return map;

  }

  // client that will send arrayList to Inverter

  public void makeClient1(ArrayList list) {

    try {
      Socket s = new Socket("localhost", 8888);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());
      objectOutputStream.writeObject(list);
      objectOutputStream.flush();
      objectOutputStream.close();
      s.close();
    } catch (Exception e) {
      System.out.println(e);
    }

  }

  private HashMap<String, document> makeServer3() {
    try {
      ServerSocket ss = new ServerSocket(9999);
      System.out.println("Waiting Final result from inverter");
      Socket s = ss.accept();// establishes connection
      ObjectInputStream dis = new ObjectInputStream(s.getInputStream());
      FinalMap = (HashMap) dis.readObject();
      System.err.println("message= ");
      ss.close();
    } catch (Exception e) {
      System.out.println(e);
    }
    return FinalMap;

  }

  public static int GetFilesNUmber() {

    return listOfFiles.length;

  }

}
