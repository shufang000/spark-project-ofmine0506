package com.shufang.sparksql

import com.shufang.beans.Num
import com.shufang.utils.SparkUtil
import org.apache.spark.rdd.RDD
import org.apache.spark._
import org.apache.spark.sql._

/**
 * 本垒主要讲解RDD、DataFrame、DataSet之间的相互转化
 */
object RDDToDF_DSTransferDemo {
  def main(args: Array[String]): Unit = {


    val sc: SparkContext = SparkUtil.getLocalSC()
    val spark: SparkSession = SparkSession.builder().config(sc.getConf).getOrCreate()


    val rdd: RDD[Int] = sc.parallelize(Seq(1, 2, 3, 4, 5))
    //println(rdd.collect().mkString("-"))
    val rdd1: RDD[Num] = rdd.map(Num)

    /**
     * RDD to DF
     */
    import spark.implicits._
    val df: DataFrame = rdd1.toDF("num")
    df.show()

    /**
     * RDD to DS
     */
    val ds: Dataset[Num] = rdd1.toDS()
    ds.show()

    /******************************************************/

    /**
     * DF to DS
     */

    val df1: DataFrame= spark.read.text("src/main/data/hello.txt")
    val ds1: Dataset[String] = df1.as[String]
    ds1.show()

    /**
     * DF to RDD
     */
    val rdd2: RDD[Row] = df1.rdd
    val rdd3: RDD[(String, Int)] = rdd2.flatMap(row => row.getString(0).split("\\s")).map((_, 1))
    rdd3.foreach(print)

    /******************************************************/

    /**
     * DS to RDD
     */
    val rdd4: RDD[String] = ds1.rdd

    /**
     * DS to DF
     */
    println("")

    val ds2: Dataset[(String, String)] = ds1.map {
      case line =>
        (line.split("\\s")(0), line.split("\\s")(1))
    }
    val df2: DataFrame = ds2.toDF("project", "class_type")
    df2.show()


    /**
     * 创建临时视图,并通过SQL进行查询DF
     */
    import spark.sql
    df2.createOrReplaceTempView("projects")
    sql("select project,concat(class_type,'|',project) from projects").show()
  }
}
