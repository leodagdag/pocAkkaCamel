package com.leodagdag.cassandra

import com.datastax.driver.core.{BoundStatement, PreparedStatement, ResultSet, Cluster}
import scala.collection._
import java.util.Date

/**
 * @author patinfr
 */
object LoadCassandra {
  def main(args: Array[String]) {

    val cluster: Cluster = Cluster.builder().addContactPoint("127.0.0.1").build()
    val session = cluster.connect("ipm")

    val stmt: PreparedStatement = session.prepare("INSERT INTO evt(nego_id,evt,event_time) VALUES(?,?,?,?)")
    (1 until 1000).foreach {
      i =>
        val boundStatement = new BoundStatement(stmt)
        val l: java.lang.Long = new Date().getTime
        val b: BoundStatement = boundStatement.bind(s"$i", "20140103", "0-START", new Date())
        session.execute(b)
        val l2: java.lang.Long = new Date().getTime
        session.execute(boundStatement.bind(s"$i", "20140103", "2-END", new Date()))
    }

    val results: ResultSet = session.execute("SELECT * FROM evt")
    val iter = results.iterator()
    val map = mutable.Map.empty[String, (Date, Date)]
    while (iter.hasNext) {
      val row = iter.next()

      println(s"${row.getString("nego_id")}\t${row.getString("date")}\t${row.getString("evt")}\t\t${row.getDate("event_time")}")
      val nego_id = row.getString("nego_id")
      val evt = row.getString("evt")
      val event_time = row.getDate("event_time")
      if (evt == "2-END" || evt == "0-START") {
        if (!map.contains(nego_id)) {
          map.put(nego_id, event_time -> null)
        } else {
          map += ((nego_id, map(nego_id)._1 -> event_time))

        }

      }
    }

    println(s"result: $map")
    map.values.map(t => t._1.getTime - t._2.getTime).foreach(println(_))
    cluster.shutdown()


  }
}
