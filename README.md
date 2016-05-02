# Arakhnê Foundation Classes

The Arakhnê Foundation Classes (AFC) is a collection of Java libraries that may be used to build applications. The Arakhnê Foundatation Classes provides tools that are not directly available in the standard Java API. The AFC libraries are distributed under the terms of the Apache License.

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
Generic Attribute API | org.arakhne.afc.advanced | attributes | Library for creating generic attributes, aka. Variant attributes.


# Requirements

* Java Development Toolkit (JDK) 1.8 or higher.

# Using AFC in a Maven project

## Recommendations

For making your experience with AFC the best, we recommend you:
* **to enable the assertions at development time (with the `-ea` command line option).**

## Use the stable version

The lastest stable version of AFC is available on "Maven Central.":http://search.maven.org/
Consequently, you could directly include the AFC module that you want to use into the Maven dependencies of your project.
For example, if you want to use the "vmutils" module:

```xml
	<dependency>
	  <groupId>org.arakhne.core</groupId>
	  <artifactId>vmutils</artifactId>
	  <version>12.0</version>
	</dependency>
```

Please replace Version `12.0` in the previous snipset by the number of the version you want to use (`12.0` is the first available on Maven Central).

## Use of the development version

New features, enhancements and bug fixes are available in the SNAPSHOT (development) version of AFC.
For using this version, you must add the Maven Repository Server of AFC in your pom file:

```xml
	<dependencies>
	  <dependency>
	    <groupId>org.arakhne.core</groupId>
	    <artifactId>vmutils</artifactId>
	    <version>13.0-SNAPSHOT</version>
	  </dependency>
	<dependencies>
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
```

# Issues

Issues related to the AFC are tracked on "GitHub":https://github.com/gallandarakhneorg/afc/issues
You must use this issue tracker to report and follow your issues.
