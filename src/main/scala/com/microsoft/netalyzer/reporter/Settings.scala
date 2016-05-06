package com.microsoft.netalyzer.reporter

import com.typesafe.config.ConfigFactory

class Settings() {
  val config = ConfigFactory.load()
  config.checkValid(ConfigFactory.defaultReference(), "netalyzer-reporter")
  val inputDataSpec = config.getString("netalyzer-loader.inputDataSpec")
  val outputDataSpec = config.getString("netalyzer-loader.outputDataSpec")
}
