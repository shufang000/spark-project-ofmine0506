package com.shufang.sparksql

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * 本段代码主要是用来实现SparkSQL的缓存功能，及配置实现性能的简单优化：
 * 使用对应的缓存时==>
 * SparkSQL默认将自动将【列式缓存】数据进行压缩，最大程度减轻内存消耗与GC压力!
 * 我们可以通过相关参数来调整压缩相关配置:
 *          spark.sql.inMemoryColumnarStorage.compressed = true(default) ：设置为true时，Spark SQL将根据数据统计信息自动为每一列选择一个压缩编解码器
 *          spark.sql.inMemoryColumnarStorage.batchSize = 10000(default) ：控制用于列式缓存的批处理的大小。较大的批处理大小可以提高内存利用率和压缩率，但是在缓存数据时会出现OOM
 * 常用的缓存方式有2种"
 * 1、spark.catalog.cache("tableName")
 * 2、dataframe.cache()
 */
object SparkSQLTuningDemoByCache {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName("cache").master("local[*]").getOrCreate()

    /**
     * 一、配置列式存储缓存相关的压缩配置
     */
    spark.conf.set("spark.sql.inMemoryColumnarStorage.compressed", true)
    spark.conf.set("spark.sql.inMemoryColumnarStorage.batchSize", 10000)


    /**
     * 二、其他配置
     */
    val df: DataFrame = spark.read.load("src/main/data/word.parquet")

    //缓存方式一
    df.cache()
    //df.unpersist()

    //将hive中查询的数据创建视图
    df.createTempView("word")

    //缓存方式二
    //spark.catalog.cacheTable("word")
    //spark.catalog.uncacheTable("word")

    /*val result: Boolean = spark.catalog.dropGlobalTempView("word")
    println(result)*/

    import spark.implicits._
    import spark.sql
    //val df: DataFrame = sql("select * from xxxxx")
    //df.show()
    //println(spark.sql("select * from word"))
    spark.stop()
  }
}
