val Http4sVersion = "$http4s_version$"
val CirceVersion = "$circe_version$"
val Specs2Version = "$specs2_version$"
val LogbackVersion = "$logback_version$"

lazy val root = (project in file("."))
  .settings(
    organization := "$organization$",
    name := "$name;format="norm"$",
    version := "0.1.0",
    scalaVersion := "$scala_version$",
    // Generate output in the classDirectory so that it can be serve from the staticRoute
    apidocOutputDir := (classDirectory in Compile).value / "apidoc",
    apidocTitle := Some("""http4s with sbt-apidoc Demo"""),
    apidocSampleURL := Some("http://localhost:8080"),
    // Make the apidoc run before the run command
    run := (run in Compile).dependsOn(apidoc).evaluated,
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "org.specs2"      %% "specs2-core"         % Specs2Version % "test",
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings"
)
