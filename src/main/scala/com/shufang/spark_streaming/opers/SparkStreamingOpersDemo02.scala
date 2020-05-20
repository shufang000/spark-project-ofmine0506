package com.shufang.spark_streaming.opers

import com.shufang.utils.SparkUtil
import org.apache.spark.SparkContext
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Durations, StreamingContext}

/**
 * join
 * union
 * coGroup
 */
object SparkStreamingOpersDemo02 {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtil.getLocalSC()
    val ssc = new StreamingContext(sc, Durations.seconds(10))


    /**
     * JOIN操作，只能针对RDD中KV类型的元素
     * 筛选出同一批次2个不同流的相同key的元素
     */
    val dStream: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)
    val stream1: DStream[(String, Int)] = dStream.flatMap(_.split("\\s"))
      .filter(!_.isEmpty)
      .map((_, 1))
      .reduceByKey(_+_)

    val stream2: DStream[(String, Int)] = ssc.socketTextStream("localhost", 8888)
      .filter(!_.isEmpty).map((_, 2))

    val joinStream: DStream[(String, (Int, Int))] = stream1.join(stream2)

    //joinStream.print()

    /**
     * UNION操作,可以针对RDD的中任何类型的元素
     * 将两个RDD[类型]相同的DStream union起来形成一个新的离散流
     */

    val unionStream: DStream[(String, Int)] = stream1.union(stream2)
    unionStream.print()


    /**
     * coGroup操作，只能针对RDD中KV类型的元素,
     * 相当于把2个DStream中相同Key的RDD进行类似于组合，形成新的DStream[RDD<KeyType,(Iterator(v1...),Iterator(v2.....))>]
     */
    val value: DStream[(String, (Iterable[Int], Iterable[Int]))] = stream1.cogroup(stream2)

    ssc.start()
    ssc.awaitTermination()
  }
}
