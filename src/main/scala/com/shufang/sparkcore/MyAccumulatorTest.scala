package com.shufang.sparkcore

import com.shufang.utils.SparkUtil
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
 * 用来注册，并且测试自定义的累加器
 */
object MyAccumulatorTest {
  def main(args: Array[String]): Unit = {

    //1、获取上下文SparkContext
    val sc: SparkContext = SparkUtil.getLocalSC()

    //2、注册累加器
    val myAcc = new MyAccumulator
    sc.register(myAcc, "myUdfAccumulator")

    //3、验证累加器
    val rdd: RDD[Int] = sc.parallelize(Seq(1, 2, 3, 4, 5))

    /*
        rdd.map {
          a =>
            myAcc.add(a)
            a
        }.foreach(println(_))
    */

    rdd.foreach {
      case a =>
        myAcc.add(a)
        print(a)
    }


    val registered: Boolean = myAcc.isRegistered
    println(registered)

    println(myAcc.value.toString())

    sc.stop()
  }
}


