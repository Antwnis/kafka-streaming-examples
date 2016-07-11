object Schemas {
  sealed class Entity
  case class Shipments_v1(itemID: Long, storeCode: String, count: Int) extends Entity
  case class Sales_v2(itemID: Long, storeCode: String, count: Int, customerID: String) extends Entity
}
