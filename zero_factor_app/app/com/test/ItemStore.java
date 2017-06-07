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

public class ItemStore {
  public static final String DATA_DIR = "/Users/brent.gardner/workspace/w12/xp_problem_set_zero_factor_app/app/db/";

  public static JSONArray getItems() {
    try {
      JSONParser parser = new JSONParser();
      JSONArray items = (JSONArray)parser.parse(new FileReader(DATA_DIR + "items.json"));
      return items;
    } catch(Exception ex) {
      // oops!
      return null;
    }
  }
}
