package com.louishong.ibg.weixin;

import java.util.List;

import com.louishong.ibg.weixin.model.StaxTestItem;

public class TestRead {
  public static void main(String args[]) {
    StaXParser read = new StaXParser();
    List<StaxTestItem> readConfig = read.readConfig("config.xml");
    for (StaxTestItem item : readConfig) {
      System.out.println(item);
    }
  }
} 