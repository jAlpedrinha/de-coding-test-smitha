package ai.humn.telematics

import scala.collection.mutable.ListBuffer
import scala.io.Source

object ProcessDataFile {

  def main(args: Array[String]) = {

    // read file path from args
    var x = args(0)

    // This is the file
    val y = Source.fromFile(x)

    // All of the lines in the file
    var l: Seq[String] = y.getLines().toList

    // Make a variable to hold the parsed lines from the file.
    var results = ListBuffer[ Array[String] ]()

    // parse each line as csv to a collection
    for (a <- 0 until l.length) {
          results += l(a).split(",")
    }

    // This is a collection of the journey lines
    val j = results.toList

    // 1. Find journeys that are 90 minutes or more.
    println("Journeys of 90 minutes or more.")
    var i = 1
    while (i < j.length) {
      if (j(i)(3).toLong - j(i)(2).toLong >= 90 * 1000 * 60) {
        if (j(i)(9).toInt - j(i)(8).toInt > 0) {
          var dist = j(i)(9).toFloat - j(i)(8).toFloat
          var timetaken = j(i)(3).toLong - j(i)(2).toLong
          var timeinHr = timetaken / (1000 * 60 * 60)
          var avgspeed = dist / timeinHr
          printf("\n journeyId: %s %s  distance: %.1f durationMS %d avgSpeed in kph was %.2f", j(i)(0), j(i)(1), dist, timetaken, avgspeed);
        }
      }
      i = i + 1
    }

    // 2. Find the average speed per journey in kph.
    var k = j.size
    var x = 1
    println("\n\n Average speeds in Kph ")

    while (k > 1) {
      var dist: Float = j(x)(9).toInt - j(x)(8).toInt
      var timetaken = j(x)(3).toLong - j(x)(2).toLong
      var driver = j(x)(1)

      if (dist > 0) {
        if (timetaken > 0) {
          if (j(x)(1) != j(x - 1)(1)) {
            var avgspeed = (dist * (1000 * 60 * 60)) / timetaken
            printf("\n journeyId: %s %s  distance: %.1f durationMS %d avgSpeed in kph was %.2f", j(x)(0), j(x)(1), dist, timetaken, avgspeed);
            k = k - 1;
            x = x + 1
          }
          else if (j(x)(2).toLong != j(x - 1)(2).toLong) {
            var avgspeed = (dist * (1000 * 60 * 60)) / timetaken
            printf("\n journeyId: %s %s  distance: %.1f durationMS %d avgSpeed in kph was %.2f", j(x)(0), j(x)(1), dist, timetaken, avgspeed)
            k = k - 1;
            x = x + 1
          }
          else {
            k = k - 1;
            x = x + 1
          }
        }
        else {
          k = k - 1; x = x + 1
        }
      }
      else {
        k = k - 1; x = x + 1
      }

    }


    //3. Find Mileage By Driver

    println("\n\n Mileage By Driver")

    var a = j.size
    var b = 1
    var dist = 0
    var distList = new ListBuffer[Float]()
    while (a > 1) {
      //dist = dist + (j(b)(9).toInt - j(b)(8).toInt)
      var timetaken = j(b)(3).toLong - j(b)(2).toLong
      var driver = j(b)(1)
      if (j(b)(9).toInt - j(b)(8).toInt >= 0) {
        if (timetaken > 0) {
          if (j(b)(1) != j(b - 1)(1)) {
            if (b >= 1) {
              dist =  j(b)(9).toInt - j(b)(8).toInt
              //distList = distList.addOne(dist)
              a = a - 1;
              b = b + 1
            }
            else {
              a = a - 1;
              b = b + 1
            }
          }
          else if (j(b)(2).toLong != j(b - 1)(2).toLong) {
            dist = dist + j(b)(9).toInt - j(b)(8).toInt
            a = a - 1;
            b = b + 1
            //distList = distList.addOne(dist)
          }
          else {
            distList = distList.addOne(dist)

            a = a - 1;
            b = b + 1
          }
        }
        else {
          a = a - 1;
          b = b + 1
          distList = distList.addOne(dist)
          //printf("%d drove %d kilometers",j(b)(1),distList(b))
        }
      }
          else
          {a = a - 1;
            b = b + 1

          }



        }
    var q=0
    while (q < distList.length) {
      printf("\ndrove %s kilometers",  distList(q))
      q = q + 1
    }
    //println(distList)
      }
}
