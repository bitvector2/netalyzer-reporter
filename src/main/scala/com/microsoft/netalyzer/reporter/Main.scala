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

    val blah = sqlContext.sql("select datetime, count(*) as count from netalyzer.samples group by datetime order by datetime").collect()
    blah.foreach(println)

    val t1 = System.currentTimeMillis
    println("Elapsed Time: " + (t1 - t0) / 1000)
  }


  /*
  def materializeDeltas(sc: SQLContext): Unit = {

      sc.sql(
      """
        CREATE TABLE IF NOT EXISTS netalyzer.deltas (
          deltaseconds INT,
          deltarxbytes INT,
          deltatxbytes INT,
          id VARCHAR(127)
        )
        CLUSTERED BY(id) INTO 16 BUCKETS
        STORED AS ORC
        TBLPROPERTIES("transactional"="true")
      """.stripMargin
    )

    val deltasDf = sc.sql(
      """
        SELECT unix_timestamp(datetime) - lag(unix_timestamp(datetime)) OVER (PARTITION BY hostname, portname ORDER BY datetime) AS deltaseconds,
          CASE WHEN (lag(totalrxbytes) OVER (PARTITION BY hostname, portname ORDER BY datetime) > totalrxbytes)
            THEN round(18446744073709551615 - lag(totalrxbytes) OVER (PARTITION BY hostname, portname ORDER BY datetime) + totalrxbytes)
            ELSE round(totalrxbytes - lag(totalrxbytes) OVER (PARTITION BY hostname, portname ORDER BY datetime))
          END AS deltarxbytes,
          CASE WHEN (lag(totaltxbytes) OVER (PARTITION BY hostname, portname ORDER BY datetime) > totaltxbytes)
            THEN round(18446744073709551615 - lag(totaltxbytes) OVER (PARTITION BY hostname, portname ORDER BY datetime) + totaltxbytes)
            ELSE round(totaltxbytes - lag(totaltxbytes) OVER (PARTITION BY hostname, portname ORDER BY datetime))
          END AS deltatxbytes,
          id
        FROM netalyzer.samples
      """.stripMargin
    ).repartition(16)

    deltasDf.printSchema()
    deltasDf.show(100)

    sc.sql(
      """
        TRUNCATE TABLE netalyzer.deltas
      """.stripMargin
    )

    deltasDf.write.mode("append").saveAsTable("netalyzer.deltas")
  }
  */

}
