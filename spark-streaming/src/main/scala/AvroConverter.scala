import org.apache.avro.generic.{GenericRecord, IndexedRecord}

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
      record.get("customerID").toString().asInstanceOf[String])
  }

  def getShipment(message: (Object, Object)) = {
    val (k, v) = message
    //val name = k.asInstanceOf[IndexedRecord].getSchema.getName
    //if (name == "Shipments_v1")
    val value = v.asInstanceOf[GenericRecord]
    shipment(value)
  }

  def getSale(message: (Object, Object)) = {
    val (k, v) = message
    val value = v.asInstanceOf[GenericRecord]
    sale(value)
  }

}