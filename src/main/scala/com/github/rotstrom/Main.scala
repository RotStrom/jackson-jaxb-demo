package com.github.rotstrom

import javax.xml.bind.{JAXBContext, Marshaller}

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.dataformat.xml.{JacksonXmlModule, XmlMapper}
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector
import generated.{ObjectFactory, Sex, Students}

import scala.collection.JavaConversions._

object Main {
  val factory = new ObjectFactory
  val mapper = new ObjectMapper
  val introspector = new JaxbAnnotationIntrospector(mapper.getTypeFactory)

  mapper.setAnnotationIntrospector(introspector)
  mapper.enable(SerializationFeature.INDENT_OUTPUT)
  mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false)

  val student = factory.createStudent()
  student.setName("Boris")
  student.setAge(25)
  student.setGroup("101")
  student.setSex(Sex.BOTH)

  val students = factory.createStudents()
  students.getStudent.add(student)
  students.getStudent.add(student)
  students.setName("MSU")

  val university = factory.createUniversity(students)

  def main (args: Array[String]): Unit = {
    marshallToJson()
    unmarshallFromJson()
    marshallToXml()
    marshallWithJaxb()
  }

  def marshallToJson(): Unit = {
    println("Marshalled university:")
    mapper.writeValue(System.out, university.getValue)
    println()
  }

  def unmarshallFromJson(): Unit = {
    val json = mapper.writeValueAsString(students)
    println(s"Marshalled:\n$json")
    val parsed = mapper.readValue(json, classOf[Students])
    println(s"Unmarshalled: ${parsed.getName} ${parsed.getStudent.map(_.getName).mkString(" ")}")
  }

  /** *
    * note: code first: to marshall xml attributes @JacksonXmlProperty(isAttribute = true) should be provided
    */
  def marshallToXml(): Unit = {
    val module = new JacksonXmlModule()
    module.setDefaultUseWrapper(false)
    val mapper = new XmlMapper(module)
    mapper.enable(SerializationFeature.INDENT_OUTPUT)
    mapper.writeValue(System.out, university)
  }

  def marshallWithJaxb(): Unit = {
    val marshaller = JAXBContext.newInstance(classOf[Students]).createMarshaller()
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
    marshaller.marshal(university, System.out)
  }
}
