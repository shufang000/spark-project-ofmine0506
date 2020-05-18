package com.shufang.sparkcore

import com.shufang.utils.SparkUtil
import org.apache.spark.SparkContext
import org.apache.spark.util.LongAccumulator

object LongAccumulatorDemo {
  def main(args: Array[String]): Unit = {

    val sc: SparkContext = SparkUtil.getLocalSC()

    //1.在Driver端创建累加器
    val accu: LongAccumulator = sc.longAccumulator("longAccumulator")

    //2.在Executor端的算子执行中使用累加器
    sc.makeRDD(Seq(1,2,3,4)).foreach(_ => accu.add(1))

    //3.最后等job执行完之后，读取累加器的值，这里应该是4
    println(s"最后的累加器的值为：=>>  ${accu.value}")
    sc.stop()
  }
}
