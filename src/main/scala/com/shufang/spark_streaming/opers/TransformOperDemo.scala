package com.shufang.spark_streaming.opers

import com.shufang.utils.SparkUtil
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Durations, StreamingContext}

/**
 * transform算子
 */
object TransformOperDemo {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtil.getLocalSC()
    val ssc = new StreamingContext(sc, Durations.seconds(10))


    val seq = Seq(("spark", 1), ("core", 88), ("streaming", 100))
    //val rdd: RDD[(String, Int)] = sc.makeRDD(seq)
    //通过广播变量将RDD的数据以集合的形式传送到DStream中进行运算
    val bc: Broadcast[Seq[(String, Int)]] = sc.broadcast(seq)

    val spark: SparkSession = SparkSession.builder().config(sc.getConf).getOrCreate()
    import spark.implicits._


    //获取DStream
    val source: DStream[(String, Int)] = ssc.socketTextStream("localhost", 9999)
      .flatMap(_.split("\\s"))
      .map((_, 1))
      .reduceByKey(_ + _)

    //调用transform算子,还可以使用广播变量
    val dStream: DStream[(String, Int)] = source.transform {
      rdd1 =>
        val rdd: RDD[(String, Int)] = bc.value.toDF().rdd.map(row => (row.getString(0), row.getInt(1)))
        val result: RDD[(String, Int)] = rdd1.union(rdd)
        result
    }

    //输出transform算子的转换结果
    dStream.print(100)

    ssc.start()
    ssc.awaitTermination()


  }
}
