package com.shufang.sparksql

import java.util

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SaveMode, SparkSession}

import scala.collection.mutable


/**
 * 在SparkSQL中默认的DataSource为 ： parquet文件
 */
object SparkSQLParquetSourceDemo {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder().appName("parquetdemo").master("local[*]").getOrCreate()

    //must import ,or will throw exception!~
    //创建一个schema，可以将RDD转换成DF
    import org.apache.spark.sql.types._
    val cols = "firstname lastname"
    val names: Array[String] = cols.split(" ")
    val fields: Array[StructField] = names.map(colname => StructField(colname, StringType, nullable = true))
    val schema: StructType = StructType(fields) //获取到schema


    //ROW[line] => String => ROW[firstname,lastname] => ROW + schema
    val df: DataFrame = spark.read.text("src/main/data/hello.txt")
    val rdd: RDD[Row] = df.rdd.map(_.getAs[String](0)).map(_.split(" ")).map(a => Row(a(0), a(1)))
    val df1: DataFrame = spark.createDataFrame(rdd, schema)

    /**
     * Schema =>>
     * root
     * |-- firstname: string (nullable = true)
     * |-- lastname: string (nullable = true)
     */

    df1.printSchema()


    import spark.implicits._

    /**
     * Schema =>>
     * root
     * |-- _1: string (nullable = true)
     * |-- _2: string (nullable = true)
     */
    val ds: Dataset[(String, String)] = df.map(row => row.getString(0)).map(_.split(" ")).map(a => (a(0), a(1)))
    ds.printSchema()

    df1.select("firstname", "lastname").show()


    //保存成parquet格式
    //parquet文件实际上是一个目录，实际结构如下
    /**
     * --word.parquet
     *    -- ._SUCCESS.crc
     *    -- part-000-xxxxx.parquet.crc
     *    -- ._SUCCESS
     *    -- part-000-xxxxx.parquet
     */
    for (i <- 1 to 10){
      df1.write.mode(SaveMode.Append).parquet("src/main/data/word.parquet")
    }


    spark.stop()
  }
}
