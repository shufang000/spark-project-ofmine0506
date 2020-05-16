package com.shufang.sparkcore

import com.shufang.utils.SparkUtil
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object RDDTransformDemo {
  def main(args: Array[String]): Unit = {

    val sc: SparkContext = SparkUtil.getLocalSC()

    val source: RDD[String] = sc.textFile("src/main/data/*.txt")

    //缓存RDD，避免重溯血缘关系，减少重复计算，提高复用性
    source.cache()

    //1.transforms operators
    val rdd: RDD[(String, Int)] = source.flatMap(_.split("\\s"))
      .filter(!_.isEmpty)
      .map((_, 1))
      .reduceByKey(_ + _, 2)
      .sortBy(_._2)

    //获取rdd的分区数
    println(rdd.getNumPartitions)


    /**
     * 常用的action算子
     */

    //foreach
    rdd.foreachPartition(iter => iter.foreach(println(_)))

    //take(n) 取出RDD中的前n个元素
    val tuples: Array[(String, Int)] = rdd.take(4)
    println(tuples.mkString("-"))

    //first() 取出rdd中的第一个元素
    val tuple: (String, Int) = rdd.first()
    println(tuple.toString())

    //saveASXXXFile() 保存到文件
    //rdd.saveAsHadoopFile()
    //rdd.saveAsTextFile()
    //rdd.repartition(1).saveAsObjectFile("src/main/core-output/object")
    //rdd.saveAsSequenceFile("src/main/core-output/sequence")

    //读取sequenceFile
    val rdd1: RDD[(String, Int)] = sc.sequenceFile[String, Int]("src/main/core-output/sequence")
    println(rdd1.collect().mkString("--"))






    sc.stop()


  }
}
