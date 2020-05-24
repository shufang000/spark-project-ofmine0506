package com.shufang.utils

import org.apache.spark.streaming.{Durations, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object SparkUtil {

  /**
   * 创建一个SparkContext对象实例
   * @return
   */
  def getLocalSC(): SparkContext = {
    val sc = new SparkContext(new SparkConf().setAppName("local").setMaster("local[*]"))
    sc
  }


  /**
   * 创建一个StreamingContext的对象
   * @param sc 传入一个SparkContext的实例
   * @param time 穿入一个时间 s
   * @return
   */
  def createSSC(sc: SparkContext, time: Int): StreamingContext = {
    if (time <= 0) {
      throw new Exception("time must be setted morr than 0,e.g 1")
    }
    val ssc = new StreamingContext(sc, Durations.seconds(time))
    ssc
  }


}
