import java.io.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Admin {
  public static void main(String[] args) throws Exception {
    if(args.length != 1) {
      System.out.println("Usage: java Admin [args]");
      System.out.println("");
      System.out.println("args:");
      System.out.println("clear.......... Clears the database");
      return;
    }
    if("clear".equals(args[0])) {
      FileWriter out = new FileWriter("../app/db/cart.json");
      out.write("[]");
      out.flush();
      out.close();
    }
  }
}
