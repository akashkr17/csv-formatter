package edu.knoldus

import java.io.{BufferedWriter, File, FileWriter, PrintWriter}

import scala.io.Source
import scala.util.Using

trait AddressReader {
  def readAddress(): Seq[Address]
}

case class Address(firstname:String,lastname:String,street:String,countryCode:String,code:String)
case class Country(name: String, twoDigitCode: String, threeDigitCode: String)

object CsvMain extends App{

//  {
//    writer => a
//  }
  val address = new AddressCSVReader("src/main/resources/addresses (1).csv").readAddress()
  def fileCreater(data:String,index:Int): Unit = {

  }
  address.map(println)
  val file = new File("file.sql")
  val bw = new BufferedWriter(new FileWriter(file,true))
    address.zipWithIndex.foreach( ff => {
      val f = ff._1
      val index = ff._2
      val data = s"insert into table_name (firstname,lastname,street,countryCode,code) values(" +
        s"${f.firstname},${f.lastname},${f.street},${f.countryCode},${f.code},);"
      if( index < 3) {
        bw.write(data)
        bw.newLine()
      }

    })
    bw.close()

  for {
    line <- Source.fromFile(file).getLines().toVector
  } yield println(line)
}

class AddressCSVReader(val fileName: String) extends AddressReader {
  val countries = Seq(Country("India", "IN", "IND"),
    Country("Australia", "AU", "AUS"),
    Country("England", "EN", "ENG"),
    Country("United States of America", "US", "USA"))
  override def readAddress(): Seq[Address] = {
    for {
      line <- Source.fromFile(fileName).getLines().toVector
      values = line.split(",").map(_.trim)
    } yield {
      val countryCode = countries.filter(_ .twoDigitCode == values(3)).map(_.threeDigitCode).headOption.getOrElse("")
      Address(values(0), values(1), values(2),countryCode,values(4))
    }
  }

}
