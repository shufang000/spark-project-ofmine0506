package com.shufang.beans

/**
 * 实体类一
 * @param name
 * @param id
 */
class KryoEntity1(var name: String, var id: Int) {
}


/**
 * 实体类二
 */
class KryoEntity2() {
  //属性
  var id: Int = _
  var address: String = _

  //辅助构造器，
  def this(id: Int, address: String) {
    //必须调用主构造器，不然编译不通过
    this()
    this.id = id
    this.address = address
  }
}

case class Num(id:Int)
