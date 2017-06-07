package com.test;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import com.test.ItemStore;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.TimerTask;
import java.nio.file.Files;

public class App extends HttpServlet {

  final static Logger LOGGER = Logger.getLogger(App.class);

  public static final String DEV_DIR = "/Users/brent.gardner/workspace/w12/xp_problem_set_zero_factor_app/app/db/";
  public static final String PROD_DIR = "/Users/tomcat/shopcart/db/";

  public static List<JSONObject> itemsSold = new ArrayList<>();

  private ScheduledExecutorService scheduler;

  public void init() throws ServletException
  {
    scheduler = Executors.newSingleThreadScheduledExecutor();
    scheduler.scheduleAtFixedRate(new PrintReportTask(), 0, 1, TimeUnit.SECONDS);
  }

  public class PrintReportTask extends TimerTask {
      public void run() {
        try {
          LOGGER.info("Tick!");
          File f = new File(DEV_DIR + "generate");
          if(f.exists()) {
              f.delete();
              FileWriter out = new FileWriter(DEV_DIR + "report.txt");
              double total = 0.0;
              for(JSONObject item : itemsSold) {
                total += (double)item.get("price");
              }
              out.write("Total amount ordered: " + total + "\n");
              out.flush();
              out.close();
          }
        } catch(Exception ex) {
          LOGGER.error(ex);
        }
      }
  }

  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
    String dataDir = "/Users/tomcat/shopcart/db/";
    if("0:0:0:0:0:0:0:1".equals(request.getLocalAddr())
      || "127.0.0.1".equals(request.getLocalAddr())) {
      dataDir = DEV_DIR;
      LOGGER.info("using dev database!");
    }
    LOGGER.info("looping " + request.getLocalAddr());
    List<Long> ids = new ArrayList<>();
    for(Object obj : ItemStore.getItems()) {
      JSONObject item = (JSONObject)obj;
      itemsSold.add(item);
      Long id = (Long)item.get("id");
      LOGGER.info("id=" + id);
      String fieldName = "cb" + id;
      String value = (String)request.getParameter(fieldName);
      LOGGER.info("value=" + value);
      if("on".equals(value)) {
        ids.add(id);
      }
    }
    CartStore.saveItems(dataDir, ids);
    response.sendRedirect("cart.jsp");
  }

  public void destroy()
  {
      scheduler.shutdownNow();
  }
}
