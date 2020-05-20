package com.shufang.spark_streaming

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreamingDemo {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("sparkStreaming").setMaster("local[*]")

    val ssc = new StreamingContext(conf, Seconds.apply(5))

    val properties = new Properties()
    properties.load(this.getClass.getClassLoader.getResourceAsStream("socket.properties"))

    val hostname: String = properties.getProperty("hostname", "localhost")
    val port: Int = properties.getProperty("port", "9999").toInt

    val ds: ReceiverInputDStream[String] = ssc.socketTextStream(hostname, port, StorageLevel.MEMORY_ONLY)

    ds.print()

    //开始
    ssc.start()

    //等待终止，任务结束
    ssc.awaitTermination()

  }
}
