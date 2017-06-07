package com.test;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.List;
import org.apache.log4j.Logger;
import java.util.Set;
import java.util.HashSet;

public class CartStore {
  public static final String DATA_DIR = "/Users/brent.gardner/workspace/w12/xp_problem_set_zero_factor_app/app/db/";

  final static Logger LOGGER = Logger.getLogger(CartStore.class);

  public static void saveItems(String DATA_DIR, List<Long> ids) {
    LOGGER.info("saving");
    try {
      FileWriter out = new FileWriter(DATA_DIR + "cart.json");
      out.write("[");
      int count = 0;
      for(Long id : ids) {
        if(count != 0) out.write(",");
        out.write(""+id);
        out.flush();
        Thread.sleep(1000);
        count++;
      }
      out.write("]");
      out.close();
      LOGGER.info("saved");
    } catch (Exception e) {
      LOGGER.error(e);
    }
  }

  public static Set<Long> getItems() {
    try {
      JSONParser parser = new JSONParser();
      JSONArray items = (JSONArray)parser.parse(new FileReader(DATA_DIR + "cart.json"));
      Set<Long> ids = new HashSet<>();
      for(Object obj : items) {
        ids.add((Long)obj);
      }
      return ids;
    } catch(Exception ex) {
      LOGGER.error(ex);
      return null;
    }
  }

}
