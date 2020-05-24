package com.shufang.spark_streaming

import com.shufang.utils.SparkUtil
import org.apache.spark.SparkContext
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Durations, StreamingContext}

object CreateStreamingContextByCheckpointDir {


  def functionToCreateAStreamingContext(): StreamingContext = {

    println("这是第一次创建一个StreamingContext")
    val ssc = new StreamingContext(SparkUtil.getLocalSC(), Durations.seconds(5))

    // 创建一个DStream
    val dStream: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)

    // 进行逻辑操作
    dStream.flatMap(_.split("\\s"))
        .map((_,1))
        .reduceByKey(_+_)
        .print()

    // 进行checkpoint操作
    ssc.checkpoint("src/main/data/checkpoint_dir")

    // 方法返回值
    ssc
  }


  def main(args: Array[String]): Unit = {

    //创建一个StreamingContext对象
    //假如是第一次启动，那么就通过调用functionToCreateAStreamingContext的形式创建一个实例
    //假如不是，就会从checkpoint的目录进行恢复创建一个StreamingContext的对象
    val ssc: StreamingContext = StreamingContext.getOrCreate("src/main/data/checkpoint_dir", functionToCreateAStreamingContext)

    ssc.start()
    ssc.awaitTermination()
  }


}
