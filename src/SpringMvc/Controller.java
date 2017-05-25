package SpringMvc;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.stereotype.Controller
public class Controller {

  @RequestMapping("/")
  public ModelAndView Hello() {

    ModelAndView modelAndView = new ModelAndView("index");

    return modelAndView;

  }

  // done.html
  @RequestMapping("/done.html")
  public ModelAndView Done(@RequestParam("Query") String query) {
    // System.out.println(query + "  ************************************************");
    // send query to master
    sendQueryToMaster(query);

    // String result =;
    ArrayList<String> arrayList = recieveResultFromMaster();

    ModelAndView modelAndView = new ModelAndView("result");
    modelAndView.addObject("result", arrayList);

    return modelAndView;

  }

  private ArrayList<String> recieveResultFromMaster() {
    ArrayList<String> arrayList = null;
    try {
      ServerSocket ss = new ServerSocket(2211);
      Socket s = ss.accept();// establishes connection
      ObjectInputStream dis = new ObjectInputStream(s.getInputStream());
      arrayList = (ArrayList<String>) dis.readObject();
      System.out.println("message= " + arrayList);
      ss.close();
    } catch (Exception e) {
      System.out.println(e);
    }
    return arrayList;
  }

  private void sendQueryToMaster(String query) {
    try {
      Socket s = new Socket("localhost", 1122);
      DataOutputStream objectOutputStream = new DataOutputStream(s.getOutputStream());
      objectOutputStream.writeUTF(query);
      objectOutputStream.flush();
      objectOutputStream.close();
      s.close();
    } catch (Exception e) {
      System.out.println(e);
    }

  }

}
