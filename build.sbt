name := "StockExperiements"

version := "1.0"

scalaVersion := "2.11.8"


libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "1.2.0",
  "org.apache.spark" % "spark-streaming_2.10" % "1.2.0",
  "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.2.0",
  "org.apache.spark" % "spark-sql_2.10" % "1.2.0",
  "org.apache.spark" % "spark-catalyst_2.10" % "1.2.0",
  "com.michaelpollmeier" %% "gremlin-scala" % "3.2.3.1",
  "org.apache.tinkerpop" % "neo4j-gremlin" % "3.2.3" exclude("com.github.jeremyh", "jBCrypt"),
  "org.neo4j" % "neo4j-tinkerpop-api-impl" % "0.4-3.0.3",
  "org.slf4j" % "slf4j-simple" % "1.7.21",
  "joda-time" % "joda-time" % "2.9.7"
)



    