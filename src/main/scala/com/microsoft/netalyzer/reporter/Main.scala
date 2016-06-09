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

    val t0 = System.currentTimeMillis

    val blah = sqlContext.sql("select count(*) as count, datetime from netalyzer.samples group by datetime order by count").collect()
    blah.foreach(println)

    val t1 = System.currentTimeMillis
    println("Elapsed Time: " + (t1 - t0) / 1000)
  }

}
