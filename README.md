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

### From pre-built binaries
Download the pre-built jar file and include in your project's classpath.

[GOLDEngine for Java jar (engine only)][2]

[GOLDEngine for Java jar with examples][4]


### From source

Clone the repository from Github:

    git://github.com/ridencww/goldengine.git

Build the project and create the JAR:

##### Using ant
    ant clean build

    ant targets:
       engine-only          : Java engine without exaples
       engine-with-examples : Java engine with Simple2/Simple3 example languages
       test                 : run JUnit unit tests
       junitreport          : generate HTML report of unit test run 

##### Using Maven
    mvn clean install


### Maven

If you're using Maven, add the following to your project's pom.xml:

    <dependency>
      <groupId>com.creativewidgetworks</groupId>
      <artifactId>goldengine</artifactId>
      <version>5.0.5</version>
    </dependency>

The goldengine artifacts are available for download at the Maven Central repository.

## Using the samples

The engine jar includes two sample interpreted languages with source files that be executed from the command line. The easiest way to access these samples is to extract the files in the jar to a folder of your choice and run the examples from the command line. Windows batch files are included to get your started.

Assuming that the jar was extracted to C:\gold, Windows users, use the following steps to run the sample programs:

    rem Simple2 examples (for example, Hello.txt)
    c:
    cd \gold\simple2\examples
    run Hello.txt
    run Hello.txt -tree
    
    rem Simple3 examples (for example, Functions.txt)
    c:
    cd \gold\simple3\examples
    run Functions.txt
    run Functions.txt -tree

The Simple2/Simple3 interpreter can also be accessed without extracting the files from the jar to execute your own source files.  Assuming the jar file and the files to be interpreted are located in the C:\gold folder, the source files can be executed using the following steps:

    rem Simple2 examples (for example, Hello.txt)
    c:
    cd \gold
    java -classpath goldengine.jar com.creativewidgetworks.goldparser.simple2.Simple2 Hello.txt
    java -classpath goldengine.jar com.creativewidgetworks.goldparser.simple2.Simple2 Hello.txt -tree

    rem Simple3 examples (for example, Functions.txt)
    c:
    cd \gold
    java -classpath goldengine.jar com.creativewidgetworks.goldparser.simple3.Simple3 Functions.txt
    java -classpath goldengine.jar com.creativewidgetworks.goldparser.simple3.Simple3 Functions.txt -tree 
    
## Version History

 
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
  [2]: https://creativewidgetworks.com/public/files/goldengine/goldengine-5_0_5.jar
  [3]: http://www.opensource.org/licenses/BSD-3-Clause
  [4]: https://creativewidgetworks.com/public/files/goldengine/goldengine-examples-5_0_5.jar
  
  