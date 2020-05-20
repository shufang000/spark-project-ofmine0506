package com.shufang.spark_streaming

import java.util

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, TaskContext}
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010._

/**
 * 本段代码是Spark-2.4.0与kafka2.0.0 通过spark-streaming-kafka-0-10
 * 通过common API 即 Direct-API的方式进行连接
 */
object DirectKafkaStreamDemo {

  def main(args: Array[String]): Unit = {


    //初始化StreamingContext
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("kafka stream")
    val ssc = new StreamingContext(conf, Seconds(5))


    // 配置相关的kafka消费者配置
    val kafkaParams = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "earliest",
      ConsumerConfig.GROUP_ID_CONFIG -> "console-group",
      //这个一定要写成这样,而不能直接写true
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> (false: java.lang.Boolean)
      //      ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG-> 60000
      //      ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG -> 60000
    )



    //如果SparkStreaming设置的批处理时长超过kafka的心跳会话时间（默认30s），
    //那么适当的调大下面的KafkaConsumer以下的参数值，防止超时time-out,可以使用ConsumerConfig.XXX来配置也是极好的
    //    kafkaParams = kafkaParams.+("heartbeat.interval.ms" -> 60000)
    //    kafkaParams = kafkaParams.+("session.timeout.ms" -> 60000)
    //TODO 对于5分钟的批处理，我们需要更改Broker上的group.max.session.timeout.ms配置


    // 获取到kafka相关的数据流
    val kafkaStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](List("flink_topic"), kafkaParams)
    )

    //在源流的基础上作相应的算子操作
    /*    val source: DStream[String] = kafkaStream.map(record => record.value())
        source.cache()

        val aggrStream: DStream[(String, Int)] = source.flatMap(_.split("\\s"))
          .map((_, 1))
          .reduceByKey(_ + _)

        aggrStream.print()*/


    //通过offsetRange来创建RDD
    val offsetRanges: Array[OffsetRange] = Array(
      //topic, partition_index, include startOffset, exclude endOffset
      OffsetRange("flink_topic", 0, 0, 100)
    )


    //获取每个RDD中对应的kafka分区信息，和offset
    kafkaStream.foreachRDD {
      rdd =>
        val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd.foreachPartition { iter =>
          val o: OffsetRange = offsetRanges(TaskContext.get.partitionId)
          println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
        }
    }




    //如何获取偏移量


    //开始计算
    ssc.start()
    ssc.awaitTermination()

  }
}
