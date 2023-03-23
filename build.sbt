ThisBuild / version := "0.1.0-SNAPSHOT"
//ThisBuild / scalaVersion := "2.11.12"
ThisBuild / scalaVersion := "2.12.15"
ThisBuild / libraryDependencies += "org.apache.spark" %% "spark-core" % "3.3.1"
ThisBuild / libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.3.1" % "provided"

// https://mvnrepository.com/artifact/com.crealytics/spark-excel
libraryDependencies += "com.crealytics" %% "spark-excel" % "3.3.1_0.18.5"

lazy val root = (project in file("."))
  .settings(
    name := "sparkDemo"
  )
