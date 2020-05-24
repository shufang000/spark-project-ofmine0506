package com.shufang.spark_streaming.opers

import com.shufang.utils.SparkUtil
import org.apache.spark.SparkContext
import org.apache.spark.streaming.{Durations, StreamingContext}
import org.apache.spark.streaming.dstream.DStream

/**
 * 在spark中虽然对状态支持没有Flink做的那么好，但是也是支持保存状态的，
 * 但是需要指定checkpoint的存储目录的
 * updateStateByKey只适用于RDD[(K,V)]类型，可以全局通过checkpoint保证不同key的value状态
 */
object UpdateStateByKeyDemo {

  def main(args: Array[String]): Unit = {

    val sc: SparkContext = SparkUtil.getLocalSC()
    val ssc = new StreamingContext(sc, Durations.seconds(10))
    ssc.checkpoint("src/main/data/checkpoint_dir")
    val source: DStream[(String, Int)] = ssc.socketTextStream("localhost", 9999)
      .flatMap(_.split("\\s")).map((_, 1))
      .reduceByKey(_ + _)

    //自定义更新的方法，也可以用匿名函数代替，如下
     val updateFun = (values:Seq[Int],state:Option[Int]) => {
          val newstate: Int = values.sum
          val oldstate = state.getOrElse(0)
          Some(newstate+oldstate)
        }

    val updateStream: DStream[(String, Int)] = source.updateStateByKey(
      (value: Seq[Int], state: Option[Int]) => {
        val newstate: Int = value.sum
        val oldstate = state.getOrElse(0)
        Some(newstate + oldstate)
      })

    updateStream.print(20)

    ssc.start()
    ssc.awaitTermination()
  }
}
