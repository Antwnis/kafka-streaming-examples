package tools

import kafka.serializer.DefaultDecoder
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils

case class KafkaPayload(value: Array[Byte])

class KafkaDStreamSource(config: Map[String, String]) {

  def createSource(ssc: StreamingContext, topic: String): DStream[KafkaPayload] = {
    val kafkaParams = config
    val kafkaTopics = Set(topic)

    KafkaUtils.
      createDirectStream[Array[Byte], Array[Byte], DefaultDecoder, DefaultDecoder](
      ssc,
      kafkaParams,
      kafkaTopics).
      map(dStream => KafkaPayload(dStream._2))
  }

}

object KafkaDStreamSource {
  def apply(config: Map[String, String]): KafkaDStreamSource = new KafkaDStreamSource(config)
}