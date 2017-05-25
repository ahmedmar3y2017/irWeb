/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inverter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import Document.document;

/**
 *
 * @author ahmed
 */
public class inverter {
  // arrayList Contain all map that send from master to inverter to make inverter algorhitm

  ArrayList<Map> arrayList;
  Map<String, Document.document> finalMap = new HashMap<String, document>();
  ArrayList<String> allTerms = new ArrayList<String>();
  ArrayList<String> allDocs = new ArrayList<String>();

  public inverter() throws IOException, ClassNotFoundException {
    // init server that will recieve all data from master

    ServerSocket ss = new ServerSocket(8888);

    System.out.println("Waiting ....");
    Socket s = ss.accept();// establishes connection
    ObjectInputStream dis = new ObjectInputStream(s.getInputStream());
    arrayList = (ArrayList<Map>) dis.readObject();
    System.out.println("message= " + arrayList);
    ss.close();
    Map mmMap = reduce(arrayList);
    makeClient(mmMap);

  }

  public void EnterQuery(String query) {
    System.out.println("Query is : " + query);
  }

  public static void main(String[] args) {

    try {
      inverter ii = new inverter();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // Scanner reader = new Scanner(System.in); // Reading from System.in
    // System.out.println("Enter a number: ");
    // String qq = reader.next(); // Scans the next token of the input as an int.
    // ii.EnterQuery(qq);
  }

  private void makeClient(Map<String, document> finalMap) {

    try {
      Socket s = new Socket("localhost", 9999);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());
      objectOutputStream.writeObject(finalMap);
      objectOutputStream.flush();
      objectOutputStream.close();
      s.close();
    } catch (Exception e) {
      System.out.println(e);
    }

  }

  // HashMap map =new HashMap<String, document>();
  public Map reduce(ArrayList<Map> array) {
    for (int i = 0; i < array.size(); i++) {
      Map hash = array.get(i);
      Collection con = hash.values();
      ArrayList<ArrayList> list = new ArrayList<ArrayList>();
      list.addAll(con);
      allTerms.addAll(list.get(0));
      allTerms.addAll(list.get(2));
      allDocs.addAll(list.get(1));
      allDocs.addAll(list.get(3));
      ArrayList<Double> weight = new ArrayList<Double>();
      for (int j = 0; j < allTerms.size(); j++) {
        int Tf = 1;
        int Df = 1;
        for (int k = j + 1; k < allTerms.size(); k++) {
          if (allTerms.get(j).equalsIgnoreCase(allTerms.get(k))) {
            if (allDocs.get(j).equalsIgnoreCase(allDocs.get(k))) {
              Tf++;
              allDocs.remove(k);
              allTerms.remove(k);
              k--;
            } else {
              Df++;
            }
          }

        }
        weight.add(Math.log(9 / Df) * (1 + Math.log(Tf)));
      }
      for (int j = 0; j < allDocs.size(); j++) {
        document d = new document();
        d.doc.put(allTerms.get(j), weight.get(j));
        int k = 0;
        for (k = j + 1; k < allDocs.size(); k++) {
          if (allDocs.get(j).equals(allDocs.get(k))) {
            d.doc.put(allTerms.get(k), weight.get(k));
            allTerms.remove(k);
            allDocs.remove(k);
            weight.remove(k);
            k--;
          }
        }
        // System.out.println(d);
        // System.out.println(allDocs.get(j));

        finalMap.put(allDocs.get(j), d);
      }

    }
    return finalMap;
  }
}
