package com.shufang.spark_streaming

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * 初始化程序入口StreamingContext
 */
object StreamingContextInitDemo {
  def main(args: Array[String]): Unit = {

    //1、从配置进行初始化
    val conf: SparkConf = new SparkConf().setAppName("Streaming Demo").setMaster("local[2]")
    var ssc = new StreamingContext(conf, Seconds(10))

    //2、从SparkContext进行初始化
    val sc = new SparkContext(conf)
    ssc = new StreamingContext(sc,Seconds(10))

    ssc.start()
    //TODO SOMETHING
    ssc.awaitTermination()

    ssc.stop(false)//手动停止进程，配合开启相关的gentle.stop配置使用

  }
}
