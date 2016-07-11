import com.github.retronym.SbtOneJar._

oneJarSettings

name := "spark-streaming"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "confluent" at "http://packages.confluent.io/maven/"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.11" % "1.6.2",
  "org.apache.spark" % "spark-streaming_2.11" % "1.6.2",
  "org.apache.spark" % "spark-streaming-kafka_2.11" % "1.6.2",
  // For Confluent's KafkaAvroDecoder
  "io.confluent" % "kafka-avro-serializer" % "3.0.0",
  // Redis client and embedded Redis
  "redis.clients" % "jedis" % "2.8.0",
  "com.github.kstyrc" % "embedded-redis" % "0.6"
)


// Fixes: com.fasterxml.jackson.databind.JsonMappingException: Could not find creator property with name 'id' (in class org.apache.spark.rdd.RDDOperationScope)
dependencyOverrides ++= Set(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.4"
)