package com.microsoft.netalyzer.reporter

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

object Main {

  val settings = new Settings()
  val conf = new SparkConf()
  val sc = new SparkContext(conf)
  val sqlContext = new HiveContext(sc)

  def main(args: Array[String]) = {
    sqlContext.setConf("spark.sql.orc.filterPushdown", "true")

    val cookedDf = sqlContext
      .read
      .format("orc")
      .load(settings.inputDataSpec)

    cookedDf.registerTempTable("cookedDf")

    println("Cooked Data:  " + cookedDf.count() + " (rows) ")
    cookedDf.printSchema()
    cookedDf.show(1000)
  }

}
