import Schemas.{Sales_v2, Shipments_v1}
import io.confluent.kafka.serializers.KafkaAvroDecoder
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkConf
import redis.embedded.RedisServer

object Run extends App {

  // Start embedded Redis Server
  val redisServer = new RedisServer(6379)
  redisServer.start()

  val sparkConf = new SparkConf()
    .setAppName("e-commerce-demo-inventory")
    .setMaster("local[2]")
  val ssc = new StreamingContext(sparkConf, Seconds(4))

  val kafkaParams = Map[String, String](
    "auto.offset.reset" -> "smallest",
    "zookeeper.connect" -> "cloudera.landoop.com:22181",
    "group.id" -> "group111112",
    "metadata.broker.list" -> "cloudera.landoop.com:29092",
    "schema.registry.url" -> "http://cloudera.landoop.com:28081")

  val salesTopic = Set("generator-sales")
  val salesStream = KafkaUtils.createDirectStream[Object, Object, KafkaAvroDecoder, KafkaAvroDecoder](ssc, kafkaParams, salesTopic)

  val shipmentsTopic = Set("generator-shipments")
  val shipmentsStream = KafkaUtils.createDirectStream[Object, Object, KafkaAvroDecoder, KafkaAvroDecoder](ssc, kafkaParams, shipmentsTopic)

  val sales = salesStream.map[Sales_v2](AvroConverter.getSale(_))
  val shipment = salesStream.map[Shipments_v1](AvroConverter.getShipment(_))

  sales.map(x => 1).reduce(_ + _)
  sales.print()

  shipment.map(x => 1).reduce(_ + _)
    .print()

  sys.ShutdownHookThread {
    println("Gracefully stopping Spark Streaming Application")
    ssc.stop(stopSparkContext = true, stopGracefully = true)
    redisServer.stop()
    println("Application stopped")
  }

  ssc.start()
  ssc.awaitTermination()

}
