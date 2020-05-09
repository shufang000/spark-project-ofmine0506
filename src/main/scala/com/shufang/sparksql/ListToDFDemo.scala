package com.shufang.sparksql

import org.apache.spark.sql.{DataFrame, SparkSession}

object ListToDFDemo {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local[*]").appName("spark-sql-source01").getOrCreate()

    import spark.implicits._
    //完美将一个List集合转化成一个Spark的带schema的DataFrame，perfect！～～
    val df: DataFrame = List("food","good").toDF()
    df.show()

    /** df.show()  =>>
     * +-----+
     * |value|
     * +-----+
     * | food|
     * | good|
     * +-----+
     */
    spark.stop()
  }
}
