package com.shufang.test

import scala.collection.mutable
import scala.io.{BufferedSource, Source}

object TestScalaSource {

  def main(args: Array[String]): Unit = {

    val source: BufferedSource = Source.fromFile("src/main/resources/jdbc.properties", "utf-8")

    val lines: Array[String] = source.getLines().toArray

    val kvs: mutable.Map[String, String] = mutable.HashMap[String, String]()
    for (line <- lines) {
      val kv: Array[String] = line.split("=")
      val k: String = kv(0)
      val v: String = kv(1)
      kvs.put(k, v)
    }

    /** 演示结果
     * hr_emp
     * root
     * 888888
     * com.mysql.jdbc.Driver
     * jdbc:mysql://localhost:3306/hello
     */
    println(s"${kvs("dbtable")}")
    println(kvs("user"))
    println(kvs("password"))
    println(kvs("driver"))
    println(kvs("url"))

    source.close()
    println("--------------------------")

  }
}

class TestScalaSource()
