package com.shufang.sparksql

import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

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

//    val value: Dataset[String] = df.map {
//      case Row(a: String) => a
//    }

//    value.show()
    spark.stop()
  }
}
