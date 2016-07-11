import com.github.retronym.SbtOneJar._

oneJarSettings

name := "spark-streaming"

libraryDependencies += "commons-lang" % "commons-lang" % "2.6"

version := "1.0"

scalaVersion := "2.11.8"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "1.8")
    sys.error("Java 8 is required for this project.")
}

val sparkVersion = "1.6.2"
val confluentVersion = "3.0.0"
val kafkaVersion = "0.10.0.0-cp1"
val avroVersion = "1.7.7"

resolvers += "confluent" at "http://packages.confluent.io/maven/"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-streaming_2.11" % sparkVersion,
  "org.apache.spark" % "spark-streaming-kafka_2.11" % sparkVersion,

  "org.apache.kafka" % "kafka-clients" % kafkaVersion,
  "org.apache.kafka" % "kafka-streams" % kafkaVersion,
  "org.apache.avro" % "avro" % avroVersion,
  "io.confluent" % "kafka-avro-serializer" % confluentVersion,
  "io.confluent" % "kafka-schema-registry-client" % confluentVersion,

  /* This could be a test-only dependency, but we keep it a compile dependency so that
     users are able to package the examples via `mvn package` and actually run the examples
                   against a Kafka cluster. */
  "com.101tec" % "zkclient" % "0.8",
  "junit" % "junit" % "4.12" % Test,
  "org.apache.kafka" % "kafka_2.11" % kafkaVersion,
  "io.confluent" % "kafka-schema-registry" % confluentVersion % Test
  //  "org.assertj" % "assertj-core" % "3.3.0" % Test,
  //  "org.apache.curator" % "curator-test" % "2.9.0" % Test,
  // Required for e.g. schema registry's RestApp
)

//<!-- The following dependencies on ScalaTest are only required for the Scala tests
//             under src/test/scala/.  They are not required for Java code/tests.
//        -->
//  <dependency>
//    <!-- This is a compile-time dependency but included only for scalatest below,
//                 so still list it under test dependencies. -->
//    <groupId>org.scalactic</groupId>
//    <artifactId>scalactic_$
//      {kafka.scala.version}
//    </artifactId>
//    <version>$
//      {scalatest.version}
//    </version>
//  </dependency>
//  <dependency>
//    <groupId>org.scalatest</groupId>
//    <artifactId>scalatest_$
//      {kafka.scala.version}
//    </artifactId>
//    <version>$
//      {scalatest.version}
//    </version>
//    <scope>test</scope>
//  </dependency>