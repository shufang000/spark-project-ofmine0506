package com.shufang.sparkcore

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SparkCoreFirstDemo {
  def main(args: Array[String]): Unit = {

    //获取执行配置
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("first-spark-demo")

    //获取程序接口sc
    val sc = new SparkContext(conf)

    sc.setCheckpointDir("src/main/data")

    //创建RDD
    val rdd: RDD[Int] = sc.makeRDD(1 until 10).repartition(1)

    rdd.foreach(println(_))

    rdd.map(_+"~hello").filter(!_.isEmpty).foreach(printf("%s",_))

    val rdd1: RDD[Any] = sc.parallelize(List(1, 2, "hello"))

    //将RDD的elements手机到driver端，进行打印
    println(rdd1.collect().mkString("-"))

    sc.stop()

  }
}
