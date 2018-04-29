# goldengine for Java

A Java implementation of Devin Cook's GOLD Parser engine. The goldengine for Java is compatible with version 5.0 of the [GOLD parsing engine][1] and a code generation template for the engine is included in GOLD Builder.

The GOLD parsing system is a tool that can be used to develop programming and scripting languages, compilers, and interpreters. GOLD can be used with numerous programming languages and on multiple platforms.

## Features

- Close to a one-to-one port of Devin's reference engine making it easier to
keep the Java engine on par with the latest engine design.
- Support for CGT and EGT format grammar tables.
- Unit tests for all classes with nearly 100% line coverage. 
- The GOLDParser wrapper class can execute programs or output the parse tree for
debugging or educational purposes.
- Rule handler classes are bound to rules using Java reflection and can be reused.
- Includes implementations of a Simple 2 interpreter and a Simple 3 interpreter
(extension of Simple 2 with functions and additional control loops) with sample
programs.

## Requirements

This project uses Java 1.5+ and requires no additional libraries other than the ones provided by the JDK/JRE.

## Installation

### From source

Clone the repository from Github:

    git://github.com/ridencww/goldengine.git

Build the project and create the JAR:

##### Using Maven
    mvn clean install

The is a Maven multi-module project. The goldengine.jar can be found in the engine/target folder and simple2.jar and simple3.jar in their respective target folders.

### Maven

If you're using Maven, add the following to your project's pom.xml:

    <dependency>
      <groupId>com.creativewidgetworks</groupId>
      <artifactId>goldengine</artifactId>
      <version>5.0.5</version>
    </dependency>

The goldengine engine jar is available for download at the Maven Central repository.

## Using the samples

The engine jar contains the parser and supporting classes only. The simple2.jar and simple3.jar files are "fat" jars that contain the goldengine, rule handlers for the language, a shell to execute programs written in that language, and a few example programs.

The shell will attempt to execute the source file specified on the command line. If the file cannot be located, the shell also looks in the /examples folder in the jar so the supplied demo programs can be executed.

Examples (assumes the current directory is where simple2.jar or simple3.jar is located):

>java -jar simple2.jar myprogram.txt (execute program outside of jar)

>java -jar simple2.jar examples/Hello.txt (execute program included in jar)

>java -jar simple3.jar examples/Functions.txt -tree
    
## Version History

 - 5.0.6-SNAPSHOT -- Refactor of Maven build. Removed ant building. Other work in progress, hench the SNAPSHOT.
 
 - 5.0.5 -- Refactor to allow creation of engine only and engine with examples jars (thanks to **vincent-vandemeulebrouck-ullink**).
 - 5.0.4 
 -- Fixed issue with Symbol.literalFormat() that didn't handle non-identifiers correctly resulting in unquoted minus symbol. This also fixed potential issue with "." and "_" as well.
 - 5.0.3 
 -- Added support for virtual terminals including IndentIncrease / IndentDecrease to support grammars like Python where indentation marks blocks. Fixed issue with incorrect column number reporting. Fixed issue with spaces and other special characters in ReflectionHelper (a big thank you to **nimatrueway** for not only identifying the problem, but also providing a clean solution to the issue).
 - 5.0.2 Fixed Token.appendData() performance issue
 - 5.0.1 Fixed error loading larger grammar files
 - 5.0.0 Initial release for GOLD Builder 5.0

    
## License

goldengine for Java is licensed under the [Modified BSD][3] license. Permission is granted to anyone to use this software for any purpose, including commercial applications.

Enjoy.


  [1]: http://goldparser.org
  [2]: https://creativewidgetworks.com/public/files/goldengine/goldengine-5.0.5.jar
  [3]: http://www.opensource.org/licenses/BSD-3-Clause
  [4]: https://creativewidgetworks.com/public/files/goldengine/goldengine-examples-5.0.5.jar
  