import sbt._
import Keys._
import spray.revolver.RevolverPlugin._

object build extends Build {

  val gcsettings = Defaults.defaultSettings

  val gc = TaskKey[Unit]("gc", "runs garbage collector")
  val gcTask = gc := {
    println("requesting garbage collection")
    System gc()
  }

  lazy val project = Project (
    "project",
    file("."),
    settings = gcsettings ++ Seq(gcTask) ++ Revolver.settings
  )
}
