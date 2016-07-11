import org.apache.avro.generic.{IndexedRecord, GenericRecord}

object EcommerceSchema {
  sealed class Entity
  case class Shipments_v1(itemID: Long, storeCode: String, count: Int) extends Entity
  case class Sales_v2(itemID: Long, storeCode: String, count: Int, customerID: String) extends Entity
}

object AvroConverter {
  def shipment(record: GenericRecord) = {
    EcommerceSchema.Shipments_v1(
      record.get("itemID").asInstanceOf[Long],
      record.get("storeCode").toString,
      record.get("count").asInstanceOf[Int])
  }

  def sale(record: GenericRecord) = {
    EcommerceSchema.Sales_v2(
      record.get("itemID").asInstanceOf[Long],
      record.get("storeCode").toString,
      record.get("count").asInstanceOf[Int],
      record.get("customerID").asInstanceOf[String])
  }

  def getShipment(message: (Object, Object)) = {
    val (k, v) = message
    val name = k.asInstanceOf[IndexedRecord].getSchema.getName
    val value = v.asInstanceOf[GenericRecord]
    if (name == "Shipments_v1")
      shipment(value)
    else
      throw new Exception(s"unknown name '$name'")
  }

  def getSale(message: (Object, Object)) = {
    val (k, v) = message
    val name = k.asInstanceOf[IndexedRecord].getSchema.getName
    val value = v.asInstanceOf[GenericRecord]
    if (name == "Sales_v2")
      sale(value)
    else
      throw new Exception(s"unknown name '$name'")
  }

  def convert(message: (Object, Object)) = {
    val (k, v) = message
    val name = k.asInstanceOf[IndexedRecord].getSchema.getName
    val value = v.asInstanceOf[GenericRecord]
    name match {
      case "Shipments_v1" => shipment(value)
      case "Sales_v2" => sale(value)
      case n => throw new Exception(s"unknown key '$n'")
    }
  }
}