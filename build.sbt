name := "doctors-rota-generator"

version := "0.1"

scalaVersion := "2.13.2"

scalastyleFailOnError := true
scalastyleFailOnWarning := true

// Add Scalastyle task to Compile
lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")
compileScalastyle := scalastyle.in(Compile).toTask("").value
(compile in Compile) := ((compile in Compile) dependsOn compileScalastyle).value

// add Scalastyle task to test
(scalastyleConfig in Test) := baseDirectory.value / "scalastyle-test-config.xml"
lazy val testScalastyle = taskKey[Unit]("testScalastyle")
testScalastyle := scalastyle.in(Test).toTask("").value
(test in Test) := ((test in Test) dependsOn testScalastyle).value

assemblyJarName in assembly := "rota.jar"

scalafmtConfig in ThisBuild := file("scalafmt.conf")
scalafmtConfig := file("scalafmt.conf")

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.scalatest" % "scalatest_2.13" % "3.1.2" % "test",
)
