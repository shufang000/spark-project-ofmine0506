package com.shufang.spark_streaming.core_opers

import com.shufang.utils.SparkUtil
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object RDDCoGroupOperDemo {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtil.getLocalSC()

    val rdd: RDD[String] = sc.textFile("src/main/data/hello.txt")

    val rdd1: RDD[(String, Int)] = rdd.flatMap(_.split("\\s")).map((_, 1)).reduceByKey(_ + _)

    val rdd2: RDD[(String, Int)] = sc.makeRDD(Seq(("spark", 1),  ("core", 88),("core", 89), ("streaming", 100)))


    val cogroupRDD: RDD[(String, (Iterable[Int], Iterable[Int]))] = rdd1.cogroup(rdd2)

    cogroupRDD.foreach(println(_))


    sc.stop()
  }
}
