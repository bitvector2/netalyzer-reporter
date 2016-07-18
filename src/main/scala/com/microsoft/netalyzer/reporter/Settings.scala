package com.microsoft.netalyzer.reporter

import com.typesafe.config.ConfigFactory

class Settings() {
  val config = ConfigFactory.load()
  config.checkValid(ConfigFactory.defaultReference(), "netalyzer-reporter")

  val inputDataSpec = config.getString("netalyzer-reporter.inputDataSpec")
  val outputDataSpec = config.getString("netalyzer-reporter.outputDataSpec")
  val jdbcUrl = config.getString("netalyzer-reporter.jdbcUrl")
  val jdbcUser = config.getString("netalyzer-reporter.jdbcUser")
  val jdbcPassword = config.getString("netalyzer-reporter.jdbcPassword")
}
