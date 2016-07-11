import EcommerceSchema.{Sales_v2, Shipments_v1}
import io.confluent.kafka.serializers.KafkaAvroDecoder
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkConf

object Main extends App {

  val sparkConf = new SparkConf()
    .setAppName("ecommerce inventory")
    .setMaster("local[2]")
  val ssc = new StreamingContext(sparkConf, Seconds(4))

  val kafkaParams = Map[String, String](
    //"auto.offset.reset" -> "smallest",
    "zookeeper.connect" -> "cloudera.landoop.com:22181",
    "group.id" -> "group111112",
    "metadata.broker.list" -> "cloudera.landoop.com:29092",
    "schema.registry.url" -> "http://cloudera.landoop.com:28081")

  val salesTopic = Set("generator-sales")
  val salesStream = KafkaUtils.createDirectStream[Object, Object, KafkaAvroDecoder, KafkaAvroDecoder](ssc, kafkaParams, salesTopic)

  //  val shipmentsTopic = Set("generator-shipments")
  //  val shipmentsStream = KafkaUtils.createDirectStream[Array[Byte],  Array[Byte], DefaultDecoder, DefaultDecoder](ssc, kafkaParams, shipmentsTopic)

  val sales = salesStream.map[Sales_v2] { x =>
    val a = AvroConverter.getSale(x)
    println("A --> " + a.toString)
    a
  }
  sales.print()

  //  println("Sales = " + sales)

  //  val shipment = salesStream
  //    .map[Shipments_v1](AvroConverter.getShipment(_))
  //    .count()

  //  println("Shipments = " + shipment)

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
