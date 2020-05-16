package com.shufang.utils

import org.apache.spark.{SparkConf, SparkContext}

object SparkUtil {

  def getLocalSC():SparkContext = {
    val sc = new SparkContext(new SparkConf().setAppName("local").setMaster("local[*]"))
    sc
  }
}
