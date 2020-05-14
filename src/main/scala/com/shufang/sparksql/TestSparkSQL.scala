package com.shufang.sparksql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object TestSparkSQL {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder().master("local[4]").appName("ss").getOrCreate()


    import spark.implicits._

    val df: DataFrame = spark.read.text("src/main/data/people.txt")

    val rdd: RDD[(String, Int)] = df.rdd.map { case Row(a: String) => a.split(" ") }.map(a => (a(0), a(1).toInt))

    val df1: DataFrame = rdd.toDF("name", "age")

    df1.printSchema()

    df1.select($"name", $"age" + 1).show()
    rdd.foreach(print(_))

    df1.filter($"age">19).show()



    //创建物化视图
    df1.createOrReplaceTempView("nice")
    import spark.sql
    //df1.createGlobalTempView("nice1")
    spark.newSession().sql("select * from nice1").show()
    sql("select name,age from nice").show()
    spark.stop()
  }
}
