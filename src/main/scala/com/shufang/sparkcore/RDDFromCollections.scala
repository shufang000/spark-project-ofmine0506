package com.shufang.sparkcore

import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RDDFromCollections {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local[4]").setAppName("rdd")

    val sc = new SparkContext(conf)

    //1.从scala集合创建一个RDD
    val ints = Seq(1, 2, 3, 4, 5)
    var rdd: RDD[Int] = sc.makeRDD(ints)
    rdd = sc.parallelize(ints)
    val i: Int = rdd.reduce(_ + _)
    println(i)

    //2.从文件读取创建RDD,spark的所有文件读取方法都支持：可以读取目录、文件、以相同后缀结尾的文件集合

    //2.1 textFile()
    sc.textFile("/*.txt")
    sc.textFile("/hello.txt")
    sc.textFile("/data")
    //2.2 wholeTextFiles()
    sc.wholeTextFiles("/")
    //2.3 sequenceFile[K,V](),其中KV必须是Hadoop中的序列化类型，IntWriteAble or Text......
    sc.sequenceFile[Text,IntWritable]("/")
    //2.4 objectFile()
    sc.objectFile("")


    sc.stop()
  }
}
