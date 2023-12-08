lazy val global = project
	.in(file("."))
	.settings(commonSettings)
	.disablePlugins(AssemblyPlugin)
	.aggregate(
		Formatting,
		GameEngine,
		DoodleBowl
	)

lazy val Formatting = project
	.in(file("Formatting"))
	.settings(commonSettings)
	.disablePlugins(AssemblyPlugin)

lazy val GameEngine = project
	.settings(
		commonSettings,
		resourceDirectory in Compile := file("GameEngine") / "src/main/resources",
	)
	.disablePlugins(AssemblyPlugin)
	.dependsOn( Formatting )

lazy val DoodleBowl = project
	.settings(
		commonSettings,
		assemblySettings
	)
	.dependsOn( GameEngine )

lazy val commonSettings = Seq(
	organization := "bowl.lee.joseph.cole",
	version := "1.0",
	scalaVersion := "2.13.3",
)

lazy val assemblySettings = Seq(
	assemblyJarName in assembly := name.value + ".jar",
	assemblyOutputPath in assembly := file(s"""bin/${name.value}.jar"""),
	assemblyMergeStrategy in assembly := {
		case PathList("META-INF", xs @ _*)	=> MergeStrategy.discard
		case "application.conf"				=> MergeStrategy.concat
		case x =>
			val oldStrategy = (assemblyMergeStrategy in assembly).value
			oldStrategy(x)
	}
)
