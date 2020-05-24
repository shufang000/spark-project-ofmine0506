package com.shufang.spark_streaming.core_opers

import com.shufang.utils.SparkUtil
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object RDDOperDemo {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtil.getLocalSC()
    sc.setCheckpointDir("src/main/data/checkpoint")
    val rdd: RDD[String] = sc.textFile("src/main/data/hello.txt")

    val rdd1: RDD[(String, Int)] = rdd.flatMap(_.split("\\s"))
      .map((_, 1))

    rdd1.cache()
    rdd1.checkpoint()


    val long: Long = rdd.count()

    val str: String = rdd.reduce(_ + _)

    /**
     * 将rdd[T]的范型，转换成rdd[Long]的类型，Long代表RDD中元素的个数
     */
    val rdd2: RDD[Long] = rdd1.map(_ => (null, 1L))
      .union(sc.makeRDD(Seq((null, 0L)), 1))
      .reduceByKey(_ + _)
      .map(_._2)

    rdd2.foreach(print(_))
    sc.stop()
  }
}
