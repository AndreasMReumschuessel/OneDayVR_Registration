package model

import scala.collection.mutable.ListBuffer

case class State(){

  val OK: Int = 200
  val FAIL: Int = 400//bad request
  var state = 0
  var statuscode:Int = OK
  var data: ListBuffer[Tuple2[String, String]] = ListBuffer[Tuple2[String, String]]()

  def addDataEntry(d: String, regex: String): Unit ={
    data.append((d, regex))
  }
  def validate(): Unit ={
    if(data.length <= 0){
      println("[error] You have no data to validate.")
      return
    }
    if(!data(state)._1.matches(data(state)._2)){
      statuscode = FAIL
      println("DEBUG: data(state)._1.matches(data(state)._2) " + data(state)._1 + "\nRegex: " + data(state)._2)
    }
    state += 1
  }

  def setNull(): Unit ={
    statuscode = FAIL
  }

  def hasNext(): Boolean={
    if(state < data.length){
      true
    }else
      false
  }
}
