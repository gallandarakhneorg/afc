# Arakhnê Foundation Classes

The Arakhnê Foundation Classes (AFC) is a collection of Java libraries that may be used to build applications. The Arakhnê Foundation Classes provide tools that are not directly available in the standard Java API.

[![Require Java](https://img.shields.io/badge/requires-Java%208-red.svg?style=flat-square)](https://www.java.com)
[![travis build](https://travis-ci.org/gallandarakhneorg/afc.svg?branch=master)](https://travis-ci.org/gallandarakhneorg/afc)
[![appveyor build](https://ci.appveyor.com/api/projects/status/github/gallandarakhneorg/afc?branch=master&svg=true)](https://ci.appveyor.com/project/gallandarakhneorg/afc)

[![Maven Compliant](https://img.shields.io/badge/compliant-Maven-yellowgreen.svg?style=flat-square)](http://maven.apache.com)
[![Java Compliant](https://img.shields.io/badge/compliant-Java-yellowgreen.svg?style=flat-square)](https://www.java.com)
[![SARL Compliant](https://img.shields.io/badge/compliant-SARL-yellowgreen.svg?style=flat-square)](http://sarl.io)
[![Scala Compliant](https://img.shields.io/badge/compliant-Scala-yellowgreen.svg?style=flat-square)](http://scala-lang.org)
[![Xtext Compliant](https://img.shields.io/badge/compliant-Xtext-yellowgreen.svg?style=flat-square)](https://eclipse.org/Xtext)
[![Xtend Compliant](https://img.shields.io/badge/compliant-Xtend-yellowgreen.svg?style=flat-square)](https://eclipse.org/Xtext)
[![OSGI Compliant](https://img.shields.io/badge/compliant-OSGI-yellowgreen.svg?style=flat-square)](https://www.osgi.org)

[![Apache 2.0 License](https://img.shields.io/github/license/gallandarakhneorg/afc.svg?style=flat-square)](https://opensource.org/licenses/Apache-2.0)
[![CLAs signed](https://cla-assistant.io/readme/badge/gallandarakhneorg/afc)](https://cla-assistant.io/gallandarakhneorg/afc)

# 1. Content of the AFC

## 1.1 Compatibility with other Languages than Java

AFC is a Java library that provides extensions for being used with other programming languages.

For example, [Vector2D](http://arakhne.org/afc/apidocs/index.html?org/arakhne/afc/math/geometry/d2/Vector2D.html) provides overloading function for operator `+`. In this way, this operator may be used by typing `v1 + 1` instead of `v1.add(1)`.

### 1.1.1 SARL agent-oriented programming language

AFC library is compatible with the [SARL](http://www.sarl.io) agent-oriented programming language, which is a Xtext-based language (see below).
Indeed, the AFC classes use the specific annotations: `@Pure` for making [pure functions](https://en.wikipedia.org/wiki/Pure_function), and `@Inline` for [inline functions](https://en.wikipedia.org/wiki/Inline_function)). AFC also provides the overridings of the operators (`operator_plus`, `operator_minus`, etc.) for vectors, matrices, etc.

### 1.1.2 Scala object-oriented programming language

AFC library is compatible with the [Scala](http://scala-lang.org) object-oriented programming language.
Indeed, the AFC classes provide the overridings of the operators (`$plus`, `$minus`, etc.) for vectors, matrices, etc.

### 1.1.3 Xtend object-oriented programming language

AFC library is compatible with the [Xtend](https://www.eclipse.org/xtend/) object-oriented programming language, which is a Xtext-based language (see below).
Indeed, the AFC classes use the specific annotations: `@Pure` for making [pure functions](https://en.wikipedia.org/wiki/Pure_function), and `@Inline` for [inline functions](https://en.wikipedia.org/wiki/Inline_function)). AFC also provides the overridings of the operators (`operator_plus`, `operator_minus`, etc.) for vectors, matrices, etc.

### 1.1.4 Other Xtext-base languages

AFC library is compatible with all the languages that are defined upon the [Xtext](https://www.eclipse.org/Xtext/) framework for development of programming languages and domain-specific languages.
Indeed, the AFC classes use the specific annotations: `@Pure` for making [pure functions](https://en.wikipedia.org/wiki/Pure_function), and `@Inline` for [inline functions](https://en.wikipedia.org/wiki/Inline_function)). AFC also provides the overridings of the operators (`operator_plus`, `operator_minus`, etc.) for vectors, matrices, etc.

## 1.1. Alive Modules

AFC library contains the following alive modules:


Name | Group Id | Artifact Id | Explanation
-----|----------|-------------|------------
[VM Utilities](http://arakhne.org/afc/apidocs/index.html?org/arakhne/afc/vmutil/package-summary.html) | org.arakhne.afc.core | vmutils | Utilities related to the virtual machine, file systems, etc.
[Weak Reference Utilities](http://arakhne.org/afc/apidocs/index.html?org/arakhne/afc/references/package-summary.html) | org.arakhne.afc.core | references | Set of classes for creating advanced weak references.
[Text Utilities](http://arakhne.org/afc/apidocs/index.html?org/arakhne/afc/text/package-summary.html) | org.arakhne.afc.core | text | Utilities for string of characters
[Input-Output Utilities](http://arakhne.org/afc/apidocs/index.html?org/arakhne/afc/io/filefilter/package-summary.html) | org.arakhne.afc.core | inputoutput | General utilities related to IO
[General Utilities](http://arakhne.org/afc/apidocs/index.html?org/arakhne/afc/util/package-summary.html) | org.arakhne.afc.core | util | General utility classes that do not fit in the other AFC modules
[Math Tools](http://arakhne.org/afc/apidocs/index.html?org/arakhne/afc/math/package-summary.html) | org.arakhne.afc.core | math | Mathematic and Geometry tools and primitives
[JavaFX Math Tools](http://arakhne.org/afc/apidocs/index.html?org/arakhne/afc/math/geometry/d2/dfx/package-summary.html) | org.arakhne.afc.advanced | mathfx | JavaFX implementation of the mathematic and geometry tools
[Generic Attribute API](http://arakhne.org/afc/apidocs/index.html?org/arakhne/afc/attrs/collection/package-summary.html) | org.arakhne.afc.advanced | attributes | Library for creating generic attributes, aka. Variant attributes.
[Agent Motion Algos](http://arakhne.org/afc/apidocs/index.html?org/arakhne/afc/agentmotion/package-summary.html) | org.arakhne.afc.advanced | agentmotion | Collection of algorithms for calculating the motion of mobile agents.


## 1.2. Deprecated and Dead Modules

AFC library contains the following deprecated modules (deprecated modules are subject to removal in next version).
The table presents the modules, the version from which they are deprecated, and the version at which they will be totaly removed from.


Name | Group Id | Artifact Id | Explanation | Deprecation Start | Total Removal
-----|----------|-------------|-------------|-------------------|--------------
Base UI Tools | org.arakhne.afc.ui | base | Base tools for user interfaces. | 13.0 | 15.0
AWT Tools | org.arakhne.afc.ui | awt | Extra AWT widgets. | 13.0 | 15.0
Swing Widgets | org.arakhne.afc.ui | swing |  Extra Swing widgets. | 13.0 | 15.0
Android Tools | org.arakhne.afc.ui | android | Extra Android widgets and activities. | 13.0 | 15.0
Vector Window Toolkit | org.arakhne.afc.ui | vector | Vectorial primitives for building vectorial graphical editors. | 13.0 | 15.0
AWT implementation of vector | org.arakhne.afc.ui | awt-vector | AWT implementation of the vectorial primitives. | 13.0 | 15.0
Android implementation of vector | org.arakhne.afc.ui | android-vector | Android implementation of the vectorial  primitives.| 13.0 | 15.0
Atomic deployment of files | org.arakhne.afc.maven | atomicdeploy | Maven plugin for deploying aa single file. | 13.0 | 15.0


# 2. Requirements

* Java Development Toolkit (JDK) 1.8 or higher.

# 3. Using AFC in a Maven project

## 3.1. Recommendations

For making your experience with AFC the best, we recommend you:
* **to enable the assertions at development time (with the `-ea` command line option).**

## 3.2. Use the stable version

The lastest stable version of AFC is available on [Maven Central.](http://search.maven.org/)
Consequently, you could directly include the AFC module that you want to use into the Maven dependencies of your project.
For example, if you want to use the "vmutils" module:

```xml
	...
	<dependency>
	  <groupId>org.arakhne.core</groupId>
	  <artifactId>vmutils</artifactId>
	  <version>13.0</version>
	</dependency>
	...
```

Please, replace Version `12.0` in the previous snipset by the number of the version you want to use (`12.0` is the first version that is available on Maven Central).

## 3.3. Use of the development version

New features, enhancements and bug fixes are available in the SNAPSHOT (development) version of AFC.
For using this version, you must add the Maven Repository Server of AFC in your pom file:

```xml
	...
	<dependencies>
	  <dependency>
	    <groupId>org.arakhne.core</groupId>
	    <artifactId>vmutils</artifactId>
	    <version>14.0-SNAPSHOT</version>
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

# 4. Issues

Issues related to the AFC are tracked on [GitHub](https://github.com/gallandarakhneorg/afc/issues)
You must use this issue tracker to report and follow your issues.

# 5. Contributions

Any contribution to the AFC library is welcome.

## 5.1. Installing the Development Environment

For setting up the development environment, you should follow the steps:
* Download and install "Eclipse for Java Developers".
* Download the [contributors.p2f](./etc/contributors.p2f) file that specifies the Eclipse plugins for the development environment.
* Install the Eclipse plugins by selecting in Eclipse: `File > Import > Install > Install software items from file.`
* Download the [contributors.epf](./etc/contributors.epf) file that contains the Eclipse general preferences related to the AFC project.
* Import the general preferences: `File > Import > General > Preferences`

## 5.2. Obtaining the AFC Source Code

For obtaining the code of the AFC library, you must clone it from the Git:
```bash
git clone https://github.com/gallandarakhneorg/afc
```

***Caution:*** due to an issue in the Eclipse Checkstyle plugin, it is mandatory to install the `build-tools` module in the your `.m2` repository prior to the first launch of the Eclipse IDE:
```bash
mvn clean install -Dcheckstyle.skip=true
```

## 5.3. Compiling the AFC Source Code

Maven is the standard tool for compiling the AFC library. It is recommended to launch the Maven compilation process on the command at least before submitting a pull request. The command line is:
```bash
mvn clean install
```

## 5.4. Sending the Contribution

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

# 6. License of AFC

The Arakhnê Foundation Classes are distributed under the [Apache v2 license](./LICENSE), and is copyrigthed to the original authors and the other authors, as expressed in the [NOTICE](./NOTICE).

# 7. Success Stories

The following projects have sucessfully used a module of the AFC:
* [FLO](http://www.multiagent.fr/MultiAgentWiki:FLO)
* [Jaak Simulation Library](https://github.com/gallandarakhne.org/jaak)
* [Janus agent platform](http://www.janusproject.io)
* [Metro-B](http://www.multiagent.fr/MultiAgentWiki:MetroB)
* [NetEditor](http://www.arakhne.org/neteditor)
* [SARL agent-programming language](http://www.sarl.io)
