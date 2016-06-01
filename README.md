# Arakhnê Foundation Classes

The Arakhnê Foundation Classes (AFC) is a collection of Java libraries that may be used to build applications. The Arakhnê Foundatation Classes provides tools that are not directly available in the standard Java API. The AFC libraries are distributed under the terms of the Apache License.

[![travis build](https://img.shields.io/travis/gallandarakhneorg/afc.svg?style=flat-square)](https://travis-ci.org/gallandarakhneorg/afc)
[![Apache 2.0 License](https://img.shields.io/github/license/gallandarakhneorg/afc.svg?style=flat-square)](https://opensource.org/licenses/Apache-2.0)

# Content of the AFC

AFC contains (modules that are not deprecated):


Name | Group Id | Artifact Id | Explanation
-----|----------|-------------|------------
VM Utilities | org.arakhne.afc.core | vmutils | Utilities related to the virtual machine, file systems, etc.
Weak Reference Utilities | org.arakhne.afc.core | references | Set of classes for creating advanced weak references.
Text Utilities | org.arakhne.afc.core | text | Utilities for string of characters
Input-Output Utilities | org.arakhne.afc.core | inputoutput | General utilities related to IO
General Utilities | org.arakhne.afc.core | util | General utility classes that do not fit in the other AFC modules
Math Tools | org.arakhne.afc.core | math | Mathematic and Geometry tools and primitives
JavaFX Math Tools | org.arakhne.afc.advanced | mathfx | JavaFX implementation of the mathematic and geometry tools
Generic Attribute API | org.arakhne.afc.advanced | attributes | Library for creating generic attributes, aka. Variant attributes.


# Requirements

* Java Development Toolkit (JDK) 1.8 or higher.

# Using AFC in a Maven project

## Recommendations

For making your experience with AFC the best, we recommend you:
* **to enable the assertions at development time (with the `-ea` command line option).**

## Use the stable version

The lastest stable version of AFC is available on [Maven Central.](http://search.maven.org/)
Consequently, you could directly include the AFC module that you want to use into the Maven dependencies of your project.
For example, if you want to use the "vmutils" module:

```xml
	...
	<dependency>
	  <groupId>org.arakhne.core</groupId>
	  <artifactId>vmutils</artifactId>
	  <version>12.0</version>
	</dependency>
	...
```

Please, replace Version `12.0` in the previous snipset by the number of the version you want to use (`12.0` is the first version that is available on Maven Central).

## Use of the development version

New features, enhancements and bug fixes are available in the SNAPSHOT (development) version of AFC.
For using this version, you must add the Maven Repository Server of AFC in your pom file:

```xml
	...
	<dependencies>
	  <dependency>
	    <groupId>org.arakhne.core</groupId>
	    <artifactId>vmutils</artifactId>
	    <version>13.0-SNAPSHOT</version>
	  </dependency>
	<dependencies>
	...
	<repositories>
	  <repository>
	    <id>org.arakhne-maven</id>
	    <name>Arakhnê.org Snapshots</name>
	    <url>http://download.tuxfamily.org/arakhne/maven/</url>
	  </repository>
	</repositories>
	<pluginRepositories>
    	  <pluginRepository>
	    <id>org.arakhne-maven</id>
	    <name>Arakhnê.org Snapshots</name>
	    <url>http://download.tuxfamily.org/arakhne/maven/</url>
	    <snapshots>
	      <enabled>true</enabled>
	    </snapshots>
	  </pluginRepository>
	</pluginRepositories>
	...
```

# Issues

Issues related to the AFC are tracked on [GitHub](https://github.com/gallandarakhneorg/afc/issues)
You must use this issue tracker to report and follow your issues.

# Contributions

Any contribution to the AFC library is welcome.

## Installing the Development Environment

For setting up the development environment, you should follow the steps:
* Download and install "Eclipse for Java Developers".
* Download the [contributors.p2f](./etc/contributors.p2f) file that specifies the Eclipse plugins for the development environment.
* Install the Eclipse plugins by selecting in Eclipse: `File > Import > Install > Install software items from file.`
* Download the [contributors.epf](./etc/contributors.epf) file that contains the Eclipse general preferences related to the AFC project.
* Import the general preferences: `File > Import > General > Preferences`

## Obtaining the AFC Source Code

For obtaining the code of the AFC library, you must clone it from the Git:
```bash
git clone https://github.com/gallandarakhneorg/afc
```

## Compiling the AFC Source Code

Maven is the standard tool for compiling the AFC library:
```bash
mvn clean install
```

## Sending the Contribution

For sending your contribution to the AFC master repositoty, you must request a pull (PR) to the [GitHub repository](https://github.com/gallandarakhneorg/afc/).

For being merged, your PR must:
* be compilable with Maven.
* pass the compilation process successfully, including the code compilation, unit testing, and code style checking. This process is supported by Travis-CI;
* sign the [Contributor License Agreement](./CLA.md) on GitHub. It is supported by [cla-assistant](https://cla-assistant.io/gallandarakhneorg/afc);
* be reviewed by one or more of the main contributors for ensure your PR is following the development rules and philosophy related to the AFC.

If the CI process is failing on your PR, please follows the steps:
* Go on the Travis-CI output for obtaining the cause of the failure.
* Fix the code of your PR on your local copy.
* Commit on your local repository, and push the changes on the same PR. **Do not create a new PR for the fix.**
* The GitHub platform will relaunch the CI process automatically.

# License of AFC

The Arakhnê Foundation Classes are distributed under the [Apache v2 license](./LICENSE), and is copyrigthed to the original authors and the other authors, as expressed in the [NOTICE](./NOTICE).
