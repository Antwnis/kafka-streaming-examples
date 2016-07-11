import com.github.retronym.SbtOneJar._

oneJarSettings

name := "spark-streaming"

libraryDependencies += "commons-lang" % "commons-lang" % "2.6"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "confluent" at "http://packages.confluent.io/maven/"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-streaming_2.11" % "1.6.2",
  "org.apache.spark" % "spark-streaming-kafka_2.11" % "1.6.2", // kafka
  "io.confluent" % "kafka-avro-serializer" % "2.0.1"
)
