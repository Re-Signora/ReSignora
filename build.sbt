// See README.md for license details.

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.3.0"
ThisBuild / organization     := "edu.hitsz"

lazy val AirCraft = (project in file("AircraftWar"))
  .settings(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.11",
      "org.scalactic" %% "scalactic" % "3.2.11"
    ),
    scalacOptions ++= Seq(
      "-language:reflectiveCalls",
      "-deprecation",
      "-feature",
      "-Xcheckinit",
      // for scala2plantuml
      "-Yrangepos"
    ),
    addCompilerPlugin("org.scalameta" % "semanticdb-scalac" % "4.5.0" cross CrossVersion.full)
  )
