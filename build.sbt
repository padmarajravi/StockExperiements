import sbtassembly.AssemblyPlugin

name := "StockExperiements"

version := "1.0"

scalaVersion := "2.11.8"

val sparkVersion = "2.0.0"

libraryDependencies ++= Seq(
  "com.michaelpollmeier" %% "gremlin-scala" % "3.2.4.0",
  "org.apache.tinkerpop" % "neo4j-gremlin" % "3.2.3" exclude("com.github.jeremyh", "jBCrypt"), // travis can't find jBCrypt...
  "org.neo4j" % "neo4j-tinkerpop-api-impl" % "0.4-3.0.3",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "joda-time" % "joda-time" % "2.9.7",
  "commons-io" % "commons-io" % "2.4"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)

disablePlugins(PlayLayoutPlugin)

routesGenerator := InjectedRoutesGenerator

//skip in update := true

AssemblyPlugin.assemblySettings

mainClass in assembly := Some("play.core.server.ProdServerStart")

fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value)

unmanagedJars in Compile += ( baseDirectory.value / "target/scala-2.11/classes" )

PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value

/*
assemblyMergeStrategy in assembly := {
  case PathList("org","aopalliance", xs @ _*) => MergeStrategy.last
  case PathList("javax", "inject", xs @ _*) => MergeStrategy.last
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
  case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
  case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
  case PathList("org", "slf4j", xs @ _*) => MergeStrategy.first
  case PathList("org", "objectweb", xs @ _*) => MergeStrategy.last
  case PathList("javax", "transaction", xs @ _*) => MergeStrategy.last
  case PathList("javax", "xml", xs @ _*) => MergeStrategy.last
  case PathList("io", "netty", xs @ _*) => MergeStrategy.last
  case PathList("org", "objenesis", xs @ _*) => MergeStrategy.last
  case PathList("META-INF","native", xs @ _*) => MergeStrategy.last
  case "pom.xml" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "plugin.xml" => MergeStrategy.last
  case "parquet.thrift" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case "META-INF/modules.properties" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/io.netty.versions.properties" => MergeStrategy.last
  case "META-INF/LICENSES.txt" => MergeStrategy.last
  case "META-INF/groovy-release-info.properties" => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

*/
