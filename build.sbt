val `scalaVersion_3`    = "3.3.6"
val `scalaVersion_2.13` = "2.13.16"

ThisBuild / scalaVersion := `scalaVersion_2.13`

val errors = 400

val targetJdk = "11"
val inline    = 64.toString

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-feature",
  s"-release:$targetJdk"
) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((2, _)) =>
    Seq(
      "-language:_",
      "-Ycache-plugin-class-loader:last-modified",
      "-Ycache-macro-class-loader:last-modified",
      "-Ywarn-dead-code",
      "-Xlint",
      "-Yrangepos",
      "-Xsource:3",
      "-Xmaxwarns",
      s"$errors",
      "-Xlint:-byname-implicit",
      "-Ymacro-annotations"
    )
  case _ =>
    Seq(
      "-Wunused:imports",
      "-Wunused:linted",
      "-Wunused:locals",
      "-Wunused:params",
      "-Wunused:privates",
      "-language:implicitConversions",
      "-Xmax-inlines",
      inline
    )
})

ThisBuild / crossScalaVersions := Seq(`scalaVersion_2.13`, `scalaVersion_3`)

ThisBuild / wartremoverWarnings ++= Warts.allBut(
  Wart.Any,
  Wart.DefaultArguments,
  Wart.Equals,
  Wart.ImplicitConversion,
  Wart.ImplicitParameter,
  Wart.JavaSerializable,
  Wart.NonUnitStatements,
  Wart.Nothing,
  Wart.Overloading,
  Wart.StringPlusAny,
  Wart.Throw,
  Wart.ToString
)

val scalaTestVersion = "3.2.19"
val sttpVersion      = "4.0.8"

val `ch.qos.logback_logback-classic`           = "ch.qos.logback"                 % "logback-classic" % "1.5.18"
val `com.github.tototoshi_scala-csv`           = "com.github.tototoshi"          %% "scala-csv"       % "2.0.0"
val `com.softwaremill.sttp.client_core`        = "com.softwaremill.sttp.client4" %% "core"            % sttpVersion
val `com.typesafe.scala-logging_scala-logging` = "com.typesafe.scala-logging"    %% "scala-logging"   % "3.9.5"
val `org.scalacheck_scalacheck`                = "org.scalacheck"                %% "scalacheck"      % "1.18.1"               % Test
val `org.scalatest_scalatest`                  = "org.scalatest"                 %% "scalatest"       % scalaTestVersion       % Test
val `org.scalatestplus_scalacheck`             = "org.scalatestplus"             %% "scalacheck-1-18" % s"$scalaTestVersion.0" % Test

def projectWithName(name: String, file: File): Project =
  Project(name, file).settings(
    libraryDependencies ++= Seq(
      `ch.qos.logback_logback-classic`,
      `com.typesafe.scala-logging_scala-logging`,
      `org.scalatest_scalatest`,
      `org.scalacheck_scalacheck`,
      `org.scalatestplus_scalacheck`
    ),
    licenseReportTitle      := s"Copyright (c) ${java.time.LocalDate.now.getYear} Andrzej Jozwik",
    licenseSelection        := Seq(LicenseCategory.MIT),
    Compile / doc / sources := Seq.empty
  )

lazy val `data` = projectWithName("data", file("data"))
  .settings(libraryDependencies ++= Seq(`com.softwaremill.sttp.client_core`))
  .dependsOn(`mean`)
  .dependsOn(`mean` % "test->test")

lazy val `file-data` = projectWithName("file-data", file("filedata"))
  .settings(libraryDependencies ++= Seq(`com.softwaremill.sttp.client_core`, `com.github.tototoshi_scala-csv`))
  .dependsOn(`data`, `mean`)
  .dependsOn(`mean` % "test->test")

lazy val `mean` = projectWithName("mean", file("mean"))

lazy val `openmeteo` = projectWithName("openmeteo", file("openmeteo"))
  .dependsOn(`data`)
  .dependsOn(`mean` % "test->test")
