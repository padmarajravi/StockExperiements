name := "StockExperiements"

version := "1.0"

scalaVersion := "2.12.1"


libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "1.2.0",
  "org.apache.spark" % "spark-streaming_2.10" % "1.2.0",
  "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.2.0",
  "org.apache.spark" % "spark-sql_2.10" % "1.2.0",
  "org.apache.spark" % "spark-catalyst_2.10" % "1.2.0",
  "com.michaelpollmeier" %% "gremlin-scala" % "3.2.3.3"
)
    