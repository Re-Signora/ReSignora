val projectVersion = "0.0.5"

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
      "junit" % "junit" % "4.13.2" % Test,
      "org.junit.jupiter" % "junit-jupiter" % "5.8.2" % Test,
      "com.novocode" % "junit-interface" % "0.11" % Test
    )
  )
