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

    val cookedDf = sqlContext
      .read
      .format("orc")
      .load(settings.inputDataSpec)

    cookedDf.registerTempTable("data")

    sqlContext.sql("select count(*) as count, timestamp from data group by timestamp order by count")
      .coalesce(1)
      .write
      .format("com.databricks.spark.csv")
      .option("header", "true")
      .mode("overwrite")
      .save(settings.outputDataSpec)

    val t1 = System.currentTimeMillis
    println("Elapsed Time: " + (t1 - t0) / 1000)
  }

}
