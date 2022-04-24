val projectVersion = "0.0.2"

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := projectVersion
ThisBuild / organization     := "work.chiro.game"

lazy val AircraftWar = (project in file("."))
  .settings(
    name := "AircraftWar",
    assembly / mainClass := Some("work.chiro.game.application.Main"),
    assembly / assemblyJarName := f"aircraft-war-$projectVersion.jar",
    assembly / assemblyOption ~= {
      _.withIncludeBin(true)
        .withIncludeScala(true)
        .withIncludeDependency(true)
    },
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.2.11",
      "org.scalatest" %% "scalatest" % "3.2.11" % "test",
      // for logger
      "com.lihaoyi" %% "sourcecode" % "0.2.8",
      // for game scripts
      "org.luaj" % "luaj-jse" % "3.0.1"
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
