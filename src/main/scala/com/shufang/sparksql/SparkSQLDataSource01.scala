package com.shufang.sparksql

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object SparkSQLDataSource01 {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local[*]").appName("spark-sql-source01").getOrCreate()

    //一、SparkSQL默认数据源是Parquet数据源，我们可以通过 spark.sql.sources.default = orc or other来指定默认数据源
    val df1: DataFrame = spark.read.load("examples/src/main/resources/users.parquet")
    df1.select("name", "favorite_color").write.save("namesAndFavColors.parquet")


    //二、手动指定数据源格式
    val df2: DataFrame = spark.read.format("json").load("examples/src/main/resources/people.json")
    df2.select("name").write.format("parquet").save("example.parquet") //输出成parquet格式


    //三、指定数据源、并且指定相关的配置选项,保存并指定相关的orc配置选项
    val peopleDFCsv = spark.read.format("csv")
      .option("sep", ";")
      .option("inferSchema", "true")
      .option("header", "true")
      .load("examples/src/main/resources/people.csv")


    peopleDFCsv.write.format("csv")
      .option("orc.bloom.filter.columns", "favorite_color")
      .option("orc.dictionary.key.threshold", "1.0")
      .save("users_with_options.orc")


    //四、直接通过SQL查询结构化文件Parquet
    val sqlDF = spark.sql("SELECT * FROM parquet.`examples/src/main/resources/users.parquet`")
    //直接将查询结果存进Hive的表中
    /**
     * 从Spark 2.1开始，持久数据源表在Hive元存储中存储了按分区的元数据。这带来了几个好处：
     * 由于元存储只能返回查询的必要分区，因此不再需要在第一个查询中将所有分区发现到表中。
     * Hive DDL，例如ALTER TABLE PARTITION ... SET LOCATION现在可用于使用Datasource API创建的表。
     */
    sqlDF.write.saveAsTable("dwi.table_name")
    //可以选择不同的SaveMode
    sqlDF.write
//      .mode("error ｜ errorfexists")  //当将一个df保存到一个数据源，假如数据已经存在，那么抛出错误
//      .mode(SaveMode.ErrorIfExists) //与上面的mode作用是一样，这是默认的mode
//      .mode(SaveMode.Append)  //将一个df数据追加到指定的数据源
//      .mode(SaveMode.Ignore)  //将一个df的数据save到指定的数据源，如果数据存在，那么忽略，什么也不做，也不抛出异常
//      .mode(SaveMode.Overwrite)//将一个df的数据覆盖到原先的table、或者指定的数据源
      .saveAsTable("table_name")

    spark.stop()
  }
}
