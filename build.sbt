name := """play-scala"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala).aggregate(js)

lazy val js = (project in file("js")).enablePlugins(ScalaJSPlugin).settings(
  libraryDependencies += "be.doeraene" %%% "scalajs-jquery" % "0.9.1"
)
scalaVersion in js := "2.11.8"
addCommandAlias("runWithJS", ";fastOptJS;copyjs;run")

lazy val copyjs = TaskKey[Unit]("copyjs", "Copy javascript files to target directory")
copyjs := {
  val outDir = baseDirectory.value / "public/javascripts"
  val inDir = baseDirectory.value / "js/target/scala-2.11"
  val files = Seq("js-fastopt.js", "js-fastopt.js.map", "js-jsdeps.js") map { p =>   (inDir / p, outDir / p) }
  IO.copy(files, overwrite = true)
}

libraryDependencies += cache
libraryDependencies += ws
libraryDependencies += evolutions
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.38"
libraryDependencies += "com.typesafe.play" %% "play-slick" % "2.0.0"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0"


