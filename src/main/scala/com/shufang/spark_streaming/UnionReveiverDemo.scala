package com.shufang.spark_streaming

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.receiver.Receiver

import scala.collection.immutable


/**
 * 本类主要是实现多个ReceiverStream的union操作
 */
object UnionReveiverDemo {
  def main(args: Array[String]): Unit = {
    val ssc = new StreamingContext(new SparkConf().setAppName("union receiver").setMaster("local[*]"), Seconds(10))


/*    val seq: immutable.IndexedSeq[ReceiverInputDStream[String]] = (1 to 10).map {
      case a =>
        ssc.receiverStream[String](new Receiver[String](StorageLevel.MEMORY_ONLY) {
          override def onStart(): Unit = ???

          override def onStop(): Unit = ???
        })
    }

    val unionStream: DStream[String] = ssc.union(seq)*/

    ssc.start()
    ssc.awaitTermination()
  }
}
