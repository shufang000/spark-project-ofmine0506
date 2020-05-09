package com.shufang.sparksql

import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSQLParquetSourceDemo02 {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder().appName("parquetdemo").master("local[1]").getOrCreate()


    //1.读取parquet格式文件
    var df: DataFrame  = spark.read.load("src/main/data/word.parquet")
    df = spark.read.format("parquet").load("src/main/data/word.parquet")
    df = spark.read.parquet("src/main/data/word.parquet")
    df = spark.sql("select firstname,lastname from parquet.`src/main/data/word.parquet`")

    df.show()


    //2.输出成Parquet格式文件
    df.write.mode("overwrite").json("src/main/data/word.json")
    df.write.mode("overwrite").json("src/main/data/word.csv")
    df.write.mode("overwrite").json("src/main/data/word.orc")

//    df.write.mode("append").parquet("src/main/data/word.parquet")


    spark.stop()
  }
}
