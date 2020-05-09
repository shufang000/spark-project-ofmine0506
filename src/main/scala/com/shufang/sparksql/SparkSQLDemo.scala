package com.shufang.sparksql

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

import scala.collection.mutable.ArrayBuffer

object SparkSQLDemo {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder().appName("spark-sql").master("local[*]").getOrCreate()

    val sc: SparkContext = spark.sparkContext

    //创建一个集合
    val peoples: ArrayBuffer[People] =
    ArrayBuffer(
    People(1001, "Mark", "United State", 1888),
    People(1003, "Lily", "German", 8888),
    People(1004, "Raflang", "Italy", 1334.6),
    People(1002, "Mechaler", "United Kingdom", 1999)
    )

    //RDD之间的转化尽量添加这个隐式转化
    import spark.implicits._
    //创建一个RDD
    val peopleRDD: RDD[People] = sc.makeRDD(peoples, 2).sortBy(_.id)

    //转化成DS
    val peopleSet: Dataset[People] = peopleRDD.toDS()
//    peopleSet.show()

    //转化成DF
//    peopleSet.toDF().show()

    peopleSet.createGlobalTempView("people")

    val frame: DataFrame = spark.sql("select concat(id,name) as nameAndId ,name,current_time as nowtime ,address,price+1000 from people")

    frame.show()

    /**
     * show的结果==>
     * +----+--------+--------------+------+
     * |  id|    name|       address| price|
     * +----+--------+--------------+------+
     * |1001|    Mark|  United State|1888.0|
     * |1002|Mechaler|United Kingdom|1999.0|
     * |1003|    Lily|        German|8888.0|
     * |1004| Raflang|         Italy|1334.6|
     * +----+--------+--------------+------+
     **/
    spark.close()

  }
}


//创建一个样例类，因为RDD是强类型的、DF不是，但是DF有自己的schema，每条记录都是Row实例
/**
 * 类似于Flink中的TypeInformation的Schema，可以通过df（"field_name"）或者df.col("field_name")来获取对应的属性数据，
 * @param id
 * @param name
 * @param address
 * @param price
 */
case class People(id: Int, name: String, address: String, price: Double)