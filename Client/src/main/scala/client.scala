import akka.actor._
import com.typesafe.config.ConfigFactory
import java.net.InetAddress
import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks
import scala.util.Random
import akka.routing.RoundRobinRouter
import java.security.MessageDigest


case class MiningWorkers(zeros:Int) 
case class startMining(zeros :Int) 
case class aggregateCoins(coins: ArrayBuffer[String]) 
case class PrintResult(coins: ArrayBuffer[String]) 
case class LocalMessage(coins:ArrayBuffer[String]) 
case class RemoteMessage(coins:ArrayBuffer[String]) 
case class assignWork(zeros:Int) 


object BitcoinClient {

  def main(args: Array[String]) {
  
	val ip = args(0)
    val system = ActorSystem("ClientSystem")
    val ReportResult = system.actorOf(Props(new ReportResult(ip)), name = "ReportResult")
   
    val listener = system.actorOf(Props(new Listener(ReportResult)), name = "listener")
    val master =  system.actorOf(Props(new Master(10, listener)),name = "master")
    
    val GetWorkActor = system.actorOf(Props(new GetWorkActor(ip,master)), name = "GetWorkActor") 
    
     GetWorkActor ! "AskServerForWork" 
    
     
  }
}

class GetWorkActor(ip: String , master:ActorRef) extends Actor {
 
  val server = context.actorSelection("akka.tcp://BitcoinServer@" + ip + ":21000/user/AssignWorkActor")

  def receive = {
    case "AskServerForWork" =>
      print("s")
       server ! "AssignWorkToMe"
    case assignWork(zeros) =>
       println("Received Work from server to generate Bitcoins which start with " + zeros +" Zeros")
       master ! MiningWorkers(zeros)
  }
}


class ReportResult(ip: String) extends Actor {
  println("akka.tcp://BitcoinServer@" + ip + ":21000/user/AssignWorkActor")
  
  val remote = context.actorSelection("akka.tcp://BitcoinServer@" + ip + ":21000/user/AssignWorkActor")

  def receive = {
    case LocalMessage(coins) =>
       remote ! RemoteMessage(coins)
       println("Sent result to the server") 
       
    case _ =>
      println("Unknown message received")
  }
}

class Master(WorkersCount: Int, listener: ActorRef) extends Actor {

  var total_mined: ArrayBuffer[String] = new ArrayBuffer[String]()
  var messages: Int = _
  val start: Long = System.currentTimeMillis

  val workerRouter = context.actorOf(
    Props[Worker].withRouter(RoundRobinRouter(WorkersCount)), name = "workerRouter")

  def receive = {

    case MiningWorkers(numberOfZeros) =>
      for (i <- 0 until WorkersCount) workerRouter ! startMining(numberOfZeros)
    case aggregateCoins(coins) =>
      total_mined ++= coins

        listener ! PrintResult(total_mined)

	  total_mined = new ArrayBuffer[String]()
  }

}

class Worker extends Actor {

  def receive = {

    case startMining(zeros) => {
	mineCoins(zeros)
	}			
				
  def hash( s:String) : String ={
    var m = java.security.MessageDigest.getInstance("SHA-256").digest(s.getBytes("UTF-8"))
    var Bitcoin = m.map("%02x".format(_)).mkString
    Bitcoin
  }
	def mineCoins(zeros:Int) = {
	var mined:ArrayBuffer[String] = ArrayBuffer[String]()
    var count:Int = 0
    var startTime = System.currentTimeMillis
    var rand :String=Random.alphanumeric.take(5).mkString
      
    while(System.currentTimeMillis - startTime<3000000){
        var string:String = "prashanth" +rand+count
        var Bitcoin = hash(string)
        var leading = Bitcoin.substring(0,zeros)
        var zeroString = "0" * zeros
        if(leading.equals(zeroString)){
          mined += string + "    ----   " + Bitcoin
        }
        count+=1
		
		if(count%1000000==0){
			sender ! aggregateCoins(mined)
		}
    }
	
  }
    }

  

}

class Listener(ReportResult: ActorRef) extends Actor {
  def receive = {
    case PrintResult(coins) =>
      {
	  //for(i <- 0 until coins.length){
      // println(coins(i))
	 //}
        println("Assigned Work Completed")
        ReportResult ! LocalMessage(coins)
      }
      
  }
}