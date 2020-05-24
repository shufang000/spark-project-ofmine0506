package com.shufang.spark_streaming.opers

import com.shufang.utils.SparkUtil
import org.apache.spark.SparkContext
import org.apache.spark.api.java.StorageLevels
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Durations, StreamingContext}

/**
 * foreachRDD，这是一个最终output的算子，这个算子可以将不同的RDD的数据
 * 输出到外部存储系统如：HDFS\DATBASE\FILE
 */
object ForeachRDDOperDemo {

  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtil.getLocalSC()
    val ssc = new StreamingContext(sc, Durations.seconds(10))

    val source: DStream[(String, Int)] = ssc.socketTextStream("localhost", 9999)
      .flatMap(_.split("\\s")).map((_, 1))
      .reduceByKey(_ + _)

    source.persist(StorageLevels.MEMORY_ONLY_2)
    /**
     * DStream的output操作
     * RDD的transform操作是lazy的，只有碰见action算子，那么才开始计算
     * DStream的output算子例如foreachRDD也是执行计算的关键：
     * 1、foreachRDD算子会按照血缘关系计算之前的transform算子
     * 2、假如foreachRDD中没对RDD做action操作，那么就会舍弃这些数据，什么也不做，
     * 只有对RDD做了对应的action操作，才会触发job，执行当前DStream的批次计算
     */
    /*    source.foreachRDD(
          rdd => {

            //spark的创建和相关导入都需要放在foreachRDD里面
            val spark: SparkSession = SparkSession.builder().config(sc.getConf).getOrCreate()
            import spark.implicits._

            val ds: Dataset[CountCase] = rdd.toDF("name", "count").as[CountCase]
            //output
            ds.write
              .format("jdbc")
              .mode("overwrite")
              .option("url", "jdbc:mysql://localhost:3306/hello")
              .option("dbtable", "foreach_rdd")
              .option("user", "root")
              .option("password", "888888")
              .save()
          }
        )*/

    source.foreachRDD {
      rdd1 =>
        val s: String = "你好树先生！～"
        rdd1.map(
          a => (a._1 + s, a._2)
        ).foreach(println(_))
    }

    ssc.start()
    ssc.awaitTermination()
  }
}

case class CountCase(name: String, count: Long)
