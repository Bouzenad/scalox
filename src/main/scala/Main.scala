import scala.util.CommandLineParser
import scala.util.boundary.break
import scala.annotation.tailrec

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.List
import java.util.Scanner

object Lox:
  def main(args: Array[String]): Unit =
    if args.length > 1 then
      println("Usage: jlox [script]")
      System.exit(64)
    else if args.length == 1 then
      runFile(args(0))
    else
      runPrompt()
      
  def runFile(path: String): Unit =
    val bytes = Files.readAllBytes(Paths.get(path))
    if !run(String(bytes, Charset.defaultCharset)) then
      ErrorReporting.error(0, "Placeholder error message")
      System.exit(65)

  def runPrompt(): Unit =
    val input = new InputStreamReader(System.in)
    val reader = BufferedReader(input)

    @tailrec
    def loop: Int =
      println("> ")
      val line = reader.readLine
      if line == null || !run(line) then 0
      else
        loop
    loop

  def run(source: String): Boolean =
    val scanner = new Scanner(source)
    val tokens = scanner.tokens
    tokens.forEach(println)
    true

object ErrorReporting:
  // Instead of having a mutable hadError field, error state is instead
  // communicated through the return value of run, for the safe of
  // following more idiomatic "functional style"
  def error(line: Int, message: String): Unit =
    report(line, "", message)

  def report(line: Int, where: String, message: String): Unit =
    println("[line" + line + "] Error" + where + ": " + message)

