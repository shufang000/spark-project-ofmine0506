package com.shufang.sparksql

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object SparkSQLJSONSourceDemo {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName("jsondemo").master("local[1]").getOrCreate()

    //1、读取json格式
    //var df: DataFrame = spark.read.json("src/main/data/word.json/part-00000-292eaa07-cd5b-4e79-8fc5-8a9883122b81-c000.json")
    var df = spark.read.format("json").load("src/main/data/word.json")
    df = spark.sql("select * from json.`src/main/data/word.json`")
    //1.1、通过SQL来读取json文件的数据
    df = spark.sql("select firstname,lastname , 1 from json.`src/main/data/word.json`")
    /**
     * +---------+---------+
     * |firstname| lastname|
     * +---------+---------+
     * |    spark|      sql|
     * |    spark|     core|
     * .....................
     */

    //2、输出成json格式
    //df.write.mode(SaveMode.Overwrite).parquet("src/main/data/word.parquet")
    df.write.mode(SaveMode.Overwrite).json("src/main/data/word1.json")
    df.show()






    spark.stop()



  }
}
