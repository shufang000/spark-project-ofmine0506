package com.shufang.tuning

import com.shufang.beans.{KryoEntity1, KryoEntity2}
import com.shufang.utils.SparkUtil
import org.apache.spark.SparkContext

/**
 * 官方建议使用Kryo进行序列化，在Spark2.0.0之后，
 * 默认在shuffle阶段针对某些类型使用Kryo序列化方式:
 * ｜-- 简单类型例如: Int、Long...
 * | -- 简单的数组类型如: Array
 * ｜-- String类型
 */
object KryoSerializerDemo {

  def main(args: Array[String]): Unit = {

    val sc: SparkContext = SparkUtil.getLocalSC()

    sc.getConf
      //1、指定默认的序列化方式为KryoSerializer，这样不仅仅指定了Woker之间进行shuffle的序列化方式，
      //   同时也指定了RDD序列化存储到disk磁盘上的序列化方式，提高磁盘IO序列化效率
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      //2、注册想要以Kryo序列化的自定义类，用逗号分隔，也可以通过：spark.kryo.classesToRegister参数指定
      //   即使不手动指定需要被序列化的类，也可食使用kryo进行序列化，但是那样的话序列化描述就会使用全类名作为object header，占用较大的内存空间
      .registerKryoClasses(Array(classOf[KryoEntity1], classOf[KryoEntity2]))

      .set("spark.kryoserializer.buffer","128k")  //default 64k,不够的话会扩容
      .set("spark.kryoserializer.buffer.max","128M") //default 64M
        //3、同时除了默认的2个序列化库，我们还可以引入第三方的序列化库
        //  Spark automatically includes Kryo serializers for the many commonly-used core Scala classes covered in the AllScalaRegistrar from the Twitter chill library.

        sc.stop()
  }
}