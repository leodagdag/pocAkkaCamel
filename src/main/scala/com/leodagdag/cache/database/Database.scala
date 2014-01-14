package com.leodagdag.cache.database

import akka.actor.{ActorRef, ActorLogging, Actor}
import com.leodagdag.cache.api.ToStringCacheKeyGenerator
import com.leodagdag.cache.api.impl.ScalaCacheImpl
import com.leodagdag.cache.model.Trade
import java.sql.{Statement, ResultSet, DriverManager}
import net.sf.ehcache.CacheManager
import scala.Predef.String

class Database extends Actor with ActorLogging {

  import Database._

  implicit val cacheKeyGenerator = ToStringCacheKeyGenerator
  implicit val cacheManager = CacheManager.getInstance()
  val cache = ScalaCacheImpl[String, Trade]("tradeCache")


  implicit def fall(key: String): Option[Trade] = {
    log.debug("Query Database for {}", key)
    val stmt: Statement = conn.createStatement()
    val rs: ResultSet = stmt.executeQuery(s"SELECT * FROM TRADE WHERE ID = '$key'")
    val isNotEmpty = rs.next()
    val result = if (isNotEmpty) {
      log.debug("Found {} in Database !", key)
      Some(new Trade(rs.getString(1), rs.getString(2), rs.getInt(3)))
    } else {
      log.debug("Not Found {} in Database :(", key)
      None
    }
    stmt.close()
    result
  }


  def receive: Actor.Receive = {
    case Request(requester: ActorRef, id: Int, key: String) =>
      cache.withFallback(key) match {
        case Some(x) =>
          requester ! Found(id, x)
        case None =>
          requester ! NotFound(id)
      }

  }
}

object Database {

  case class Request(requester: ActorRef, id: Int, key: String)

  trait Response{
    def id: Int
  }
  case class Found(override val id: Int, obj: Any) extends Response
  case class NotFound(override val id: Int)extends Response

  val driver = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbName = "cacheSample"
  val connectionURL: String = s"jdbc:derby:$dbName;create=true"
  Class.forName(driver)
  val conn = DriverManager.getConnection(connectionURL)

  conn.createStatement().execute("DROP TABLE TRADE")

  conn.createStatement().execute("CREATE TABLE TRADE (ID VARCHAR(5) NOT NULL, VALUE VARCHAR (255), AMOUNT INT)")
  conn.createStatement().execute("INSERT INTO TRADE (ID, VALUE , AMOUNT ) VALUES ('15', 'MY VALUE', 70)")
}