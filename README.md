# Arakhnê Foundation Classes

The Arakhnê Foundation Classes (AFC) is a collection of Java libraries that may be used to build applications. The Arakhnê Foundation Classes provide tools that are not directly available in the standard Java API.

AFC library is compatible with the languages that are defined with [Xtext](https://eclipse.org/Xtext): SARL, Xtend, etc.
Indeed, the AFC classes use the annotations (`@Pure`, `@Inline`), and provide the operators' overriding functions (`operator_plus`, `operator_minus`, etc.) that are supported by the compilers of the Xtext-based languages.

[![Compatible with SARL](https://img.shields.io/badge/compatible%20with-SARL-yellowgreen.svg?style=flat-square)](http://sarl.io)
[![Compatible with Xtext](https://img.shields.io/badge/compatible%20with-Xtext-yellowgreen.svg?style=flat-square)](https://eclipse.org/Xtext)
[![Apache 2.0 License](https://img.shields.io/github/license/gallandarakhneorg/afc.svg?style=flat-square)](https://opensource.org/licenses/Apache-2.0)
[![travis build](https://img.shields.io/travis/gallandarakhneorg/afc.svg?style=flat-square)](https://travis-ci.org/gallandarakhneorg/afc)

# 1. Content of the AFC

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
	  <version>12.0</version>
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

## 5.3. Compiling the AFC Source Code

Maven is the standard tool for compiling the AFC library:
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
