package com.shufang.beans

import org.apache.spark.util.SizeEstimator

/**
 * 本代码用于估算特定对象的内存占用大小
 */
object BeanObjectSizeEvaluateDemo {

  def main(args: Array[String]): Unit = {
    val bean = new KryoEntity2()
    val size: Long = SizeEstimator.estimate(bean)
    //该对象占用24个bytes
    println(size)

    //估算一个String类型的对象的占用内存,应该比原始内存要大,
    //目测是10个Char，应该是20个Byte，可是实际内存肯定不止,最后查看是64Bytes
    //为什么呢？因为String类型底层是以Array[Char]进行存储的，除了初始内存，还包括对象头、索引、数组长度等描述
    val stringVar = "helloworld"
    println(SizeEstimator.estimate(stringVar))

  }
}
