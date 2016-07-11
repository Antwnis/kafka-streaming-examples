import Schemas.{Sales_v2, Shipments_v1}
import io.confluent.kafka.serializers.KafkaAvroDecoder
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkConf

object Run extends App {

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

  ssc.start()
  ssc.awaitTermination()

}

//sales.print()
//
////  println("Sales = " + sales)
//
////    .count()
//
////  println("Shipments = " + shipment)


//}


//  val lines = messages.map { msg =>
//    println(msg._1.toString + " -> " + msg._2.toString)
//    // AvroConverter.convert(_)
//  }
//  lines.print()

//  val count = messages.map { msg =>
//    println(msg._1 + " -> " + msg._2.toString)
//    // AvroConverter.convert(_)
//  }
