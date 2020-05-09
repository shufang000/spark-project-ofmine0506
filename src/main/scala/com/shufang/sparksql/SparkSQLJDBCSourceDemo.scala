package com.shufang.sparksql

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * 想要看SparkSQL的版本迭代过程及每次迭代更新的内容，please refer to the below  official link ==>>
 * @http://spark.apache.org/docs/2.4.0/sql-migration-guide.html
 */
object SparkSQLJDBCSourceDemo {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder().appName("jdbcdemo").master("local[1]").getOrCreate()


    //1、读取指定的JDBC-MySQL数据库的数据
    var df: DataFrame = spark.read.format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/hello")
      .option("dbtable", "hr_emp")
      .option("user", "root")
      .option("password", "888888")
      .load()

    /** Reault ==>>
     * |empid|     name|gender|age|deptid|
     * +-----+---------+------+---+------+
     * | 1004|   迈克尔|     2| 18|  1089|
     * | 1006|  Michale|     2| 33|  1111|
     * | 1009|     lily|     2| 16|  1111|
     * | 1010| SuperMan|     2| 26| 10086|
     * | 1011|   哇哈哈|     1| 17| 10086|
     * | 1016| SUPERMAN|    27|  1|  1111|
     * | 1017|  BITEMAN|    38|  1|  1089|
     * | 1018|SPIDERMAN|    19|  1|  1089|
     * | 1019| SUPERMAN|     1| 27|  1089|
     * | 1020|  BITEMAN|     1| 38|  1089|
     * | 1021|SPIDERMAN|     1| 91|  1089|
     */

    df.show()

    //2、写入到Mysql
    df.write
      .format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/hello")
      .option("dbtable", "hr_emp1")
      .option("user", "root")
      .option("password", "888888")
      .save()

    spark.stop()


  }
}
