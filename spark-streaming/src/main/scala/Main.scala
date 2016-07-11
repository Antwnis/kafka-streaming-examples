import EcommerceSchema.{Sales_v2, Shipments_v1}
import kafka.serializer._
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import io.confluent.kafka.serializers.KafkaAvroDecoder

object Main extends App {

  val sparkConf = new SparkConf()
    .setAppName("ecommerce inventory")
    .setMaster("local[2]")
  val ssc = new StreamingContext(sparkConf, Seconds(4))

  val kafkaParams = Map[String, String](
    "auto.offset.reset" -> "smallest",
    // "zookeeper.connect" -> "localhost:2181",
    "group.id" -> "group1",
    "schema.registry.url" -> "cloudera.landoop.com:28081",
    "metadata.broker.list" -> "cloudera.landoop.com:29092")

  val salesTopic = Set("generator-sales")
  val salesStream = KafkaUtils.createDirectStream[Array[Byte], Object, DefaultDecoder, KafkaAvroDecoder](ssc, kafkaParams, salesTopic)

  val shipmentsTopic = Set("generator-sales")
  val shipmentsStream = KafkaUtils.createDirectStream[Array[Byte], Object, DefaultDecoder, KafkaAvroDecoder](ssc, kafkaParams, shipmentsTopic)

  val sales = salesStream
    .map[Sales_v2](AvroConverter.getSale(_))
    .count()

  println("Sales = " + sales)

  val shipment = salesStream
    .map[Shipments_v1](AvroConverter.getShipment(_))
    .count()

  println("Shipments = " + shipment)

  ssc.start()
  ssc.awaitTermination()

}


//  val lines = messages.map { msg =>
//    println(msg._1.toString + " -> " + msg._2.toString)
//    // AvroConverter.convert(_)
//  }
//  lines.print()

//  val count = messages.map { msg =>
//    println(msg._1 + " -> " + msg._2.toString)
//    // AvroConverter.convert(_)
//  }
