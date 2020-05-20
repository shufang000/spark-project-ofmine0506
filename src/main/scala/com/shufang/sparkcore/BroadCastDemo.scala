package com.shufang.sparkcore

import com.shufang.utils.SparkUtil
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD

object BroadCastDemo {
  def main(args: Array[String]): Unit = {

    val sc: SparkContext = SparkUtil.getLocalSC()

    //声明一个需要被广播的变量
    val value = Array(1,2,3,4,5)

    //进行广播
    val broadcastVar: Broadcast[Array[Int]] = sc.broadcast[Array[Int]](value)


    //使用广播变量
    val rdd: RDD[(String, Int)] = sc.sequenceFile[String, Int]("src/main/core-output/sequence")
    val rdd2: RDD[Array[Int]] = rdd.map {
       tuple =>
        //调用广播变量的值
        broadcastVar.value
    }

    println(rdd2.collect().mkString("-|-"))
    sc.stop()
  }
}
