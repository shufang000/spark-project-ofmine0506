package com.shufang.spark_streaming

import org.apache.hadoop.io.Text
import org.apache.hadoop.mapred.SequenceFileAsTextInputFormat
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Duration, Durations, Seconds, StreamingContext}

import scala.collection.mutable
import scala.collection.parallel.immutable

object SparkStreamingBasicSourceDemo {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("streaming source").setMaster("local[2]")

    val ssc = new StreamingContext(conf, Durations.milliseconds(10))

    //1.FileSystem Source 用来读取HDFS上的文件，同时指定 K，V，InputFormat类型
    //ssc.fileStream[Text,Text,SequenceFileAsTextInputFormat]("hdfs:///src/main/core-output/dir")

    //2.从socket读取
    //ssc.socketTextStream("localhost",9999)

    //3.从文件目录读取
    val dstream: DStream[String] = ssc.textFileStream("src/main/data")

    //dstream.print()

    //4.从RDD队列创建DStream
    var queue = new mutable.Queue[RDD[Int]]()
    val dstream1: InputDStream[Int] = ssc.queueStream(queue,true)//一次处理一个RDD


    /**
     * 结果示例数据
     * Time: 1589857310910 ms
     * -------------------------------------------
     * 2199
     * 2200
     * 2201
     *
     * -------------------------------------------
     * Time: 1589857310920 ms
     * -------------------------------------------
     * 2200
     * 2201
     * 2202
     */
    dstream1.print(3)  //打印每个RDD前3个元素

    ssc.start()

    //往RDD的队列中添加rdd元素
    for (i <- 1 to 10000) {
      val rdd: RDD[Int] = ssc.sparkContext.makeRDD(Array(i, i + 1, i + 2))
      queue.+=(rdd)
    }
    ssc.awaitTermination()
  }
}
