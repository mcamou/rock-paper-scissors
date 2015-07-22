import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._
import AssemblyKeys._

val akkaVersion = "2.3.12"
val sprayVersion = "1.3.3"
val json4sVersion = "3.2.11"
val slf4jVersion = "1.7.12"
val scalatestVersion = "2.2.4"
val projectMainClass = Some("com.tecnoguru.rock_paper_scissors.Main")

scalaVersion := "2.11.7"

name := "rock-paper-scissors"

organization := "com.tecnoguru"

version := "1.0"

resolvers ++= Seq(Resolver.mavenLocal,
                  Resolver.typesafeRepo("releases"),
                  Resolver.sonatypeRepo("snapshots"),
                  "spray repo" at "http://repo.spray.io")

libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-actor" % akkaVersion,
                            "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
                            "com.github.nscala-time" %% "nscala-time" % "2.0.0",
                            "io.spray" %% "spray-routing-shapeless2" % sprayVersion,
                            "io.spray" %% "spray-can" % sprayVersion,
                            "org.json4s" %% "json4s-jackson" % json4sVersion,
                            "org.json4s" %% "json4s-ext" % json4sVersion,
                            // Ensure we pick up the correct version of scalap and scala-library (json4s brings in 2.11.0)
                            "org.scala-lang" % "scalap" % scalaVersion.value,
                            "org.slf4j" % "slf4j-api" % slf4jVersion,
                            "ch.qos.logback" % "logback-classic" % "1.1.3",
                            "com.typesafe" % "config" % "1.3.0")

// Test dependencies
libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
                            "org.scalatest" %% "scalatest" % scalatestVersion % "test",
                            "io.spray" %% "spray-testkit" % sprayVersion % "test")

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Yinline-warnings", "-Xlint")

javaOptions in Test ++= Seq("-Dconfig.file=src/test/resources/application.conf")

mainClass in run in Compile := projectMainClass

scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(PreserveDanglingCloseParenthesis, true)
    .setPreference(PreserveSpaceBeforeArguments, true)
    .setPreference(RewriteArrowSymbols, true)

org.scalastyle.sbt.ScalastylePlugin.Settings

jarName in assembly := "rock_paper_scissors.jar"

assemblySettings

mainClass in assembly := projectMainClass

addCommandAlias("coverage", "scoverage:test")
