package com.shufang.sparkcore

import com.shufang.utils.SparkUtil
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2

/**
 * =>>累加器从Driver端生成，传输到Executor端的过程只需要执行一次，大量较少了网络-IO
 * 必须指定In、Out类型，分别是输入、输出类型
 * OUT应该是那中读取是带原子性的，比如Int、Long，或者是线程安全的
 * `OUT` should be a type that can be read atomically (e.g., Int, Long), or thread-safely
 **/

class MyAccumulator extends AccumulatorV2[Long, (Long, Long, Long)] {

  //1、首先定义几个属性，用来累加的属性，比如sum 、count
  private var count: Long = 0L
  private var sum: Long = 0L

  //2、重写相关的需要被实现的的方法
  //1) 判断累加器是否已经被调用了方法，如果count、sum有一个不等于0，那么add方法就被调用过，返回false，反之true
  override def isZero: Boolean = true

  //2) 拷贝当前累加器的值，生成一个新的累加器进行返回
  override def copy(): AccumulatorV2[Long, (Long, Long, Long)] = {
    //2.1 声明一个累加器实例
    val newAcc = new MyAccumulator()

    //2.2 给新的累加器的属性复制
    newAcc.sum = this.sum
    newAcc.count = this.count

    //2.3 返回新的累加器
    newAcc
  }

  //3) 将累加器的属性进行还原，重置
  override def reset(): Unit = {
    sum = 0L
    count= 0L
  }

  //4) add方法逻辑，这里是简单的求和、计数，用于求出平均值
  override def add(v: Long): Unit = {
    sum += v
    count += 1L
  }

  //4) 对不同Executor上task的累加器进行一个汇总操作
  override def merge(other: AccumulatorV2[Long, (Long, Long, Long)]): Unit = this match {
    case o: MyAccumulator =>
      sum += o.sum
      count += o.count

    case _ =>
      throw new Exception("the acc doesn't match the result !~ please care and retry")
  }

  //5） value的返回格式，这里返回的是一个 三元组[和、次、平均]
  override def value: (Long, Long, Long) = {
    (sum,count,sum/count)
  }

}

