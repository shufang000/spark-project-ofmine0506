package com.shufang.spark_streaming.opers

import com.shufang.utils.SparkUtil
import org.apache.spark.SparkContext
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming._

/** Transformation Operas=>>
 * map
 * flatMap
 * filter
 * reduceByKey
 * count
 * countByValue
 * reduce
 */
object SparkStreamingOpersDemo01 {
  def main(args: Array[String]): Unit = {

    val sc: SparkContext = SparkUtil.getLocalSC()
    val ssc = new StreamingContext(sc, Durations.seconds(5))

    val dstream: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)

    val priStream: DStream[(String, Int)] = dstream.flatMap(_.split("\\s"))
      .filter(!_.isEmpty) //用来过滤DStream中的元素
      .map((_, 1)) //将Stream中的每个元素进行转换
    //.reduceByKey(_ + _) //将(k,v)类型的数据按照相同的key进行聚合


    //priStream.print()

    /**=>>>>>>>   count()
     * count()：这个算子实际上是=>
     * 将DStream中的每个RDD的数据结构相当于是做了一个union操作具体操作大致如下
     * DStream.map(_ => (null,1L))
           * .transform(
           *        _.union(context.sparkContext.makeRDD(Seq((null, 0L)), 1)))
           * .reduceByKey(_ + _)
           * .map(_._2)
     * 最终得到的是DStream中的每个RDD中只有一个元素，这个元素就是原先RDD中元素的个数
     */
    val secStream: DStream[Long] = priStream.count()
    //secStream.print()

    /** =>>>>>>>>>>>>>>>countByValue()
     * (Long,Long)代表返回值的类型，是一个KV类型
     * K代表原先DStream中RDD中元素的Value值（k1,v1），那么这个K就是v1的值
     * countByValue()就是用来统计有多少不同的value的值
     * 实际上与count类似=>
     *  DStream.reduceByKey()
     *        .map{(k,v) => (v,1L)}
     *        .reduceByKey(_+_)
     */

    val value1: DStream[(Long, Long)] = priStream.reduceByKey(_ + _)
      .map { kv => (kv._2.toLong, 1L) }
      .reduceByKey(_ + _)

    val value2: DStream[((String, Int), Long)] = priStream.countByValue()

    value1.print()
    println("----######----######-----")
    //value2.print()


    /** >>>>>>>>>reduce()
     * RDD的reduce是action算子，但是DStream的reduce算子是一个transform算子
     * ssc.withScope{
     *      //this 代表当前的DStream
           * this.map((null, _))
           * .reduceByKey(reduceFunc, 1)
           * .map(_._2)
     * }
     * 实际上是将所有的DStream中的元素都奖赏相同的key【null】，然后根据这个key。进行reduceBykey的操作
     * 最后取操作完之后的Value值，所以最终该DStream中只有一个唯一的元素
     */

    val thirdStream: DStream[(String, Int)] = priStream.reduce((a, b) => (a._1, a._2 + b._2))

    ssc.start()
    ssc.awaitTermination()




  }
}
