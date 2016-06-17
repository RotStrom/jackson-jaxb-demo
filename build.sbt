organization := "com.github.rotstrom"

name := "jackson-jaxb-demo"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-jaxb-annotations" % "2+"

libraryDependencies += "com.fasterxml.jackson.dataformat" % "jackson-dataformat-xml" % "2+"

sources in (Compile, xjc) <<= sourceDirectory map (_ / "main" / "resources" ** "*.xsd" get)

sourceManaged in (Compile, xjc) := sourceManaged.value / "main"
