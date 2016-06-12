
RELEASE AFC
===========

The steps for releasing AFC are:

A) PHASE 1: RELEASE VERSION

A.1) Remove "-SNAPSHOT" in all the poms, and AndroidManifest.xml.

A.2) Updgrade Maven dependencies and plugins.

A.3) Compiling locally without error.

     $> rm -rf $HOME/.m2/repository
     $> mvn clean install -P generate-android-libraries

A.4) Create the aggregated documentation, and copy the generated archive file into a safe folder:

     $> mvn-javadoc-aggregate -P generate-android-libraries

   or

    $> mvn -P generate-android-libraries -Dmaven.test.skip=true clean org.arakhne.afc.maven:tag-replacer:generatereplacesrc javadoc:aggregate

A.5) Prepare the bundles for Maven Central, and copy the generated archive files into a safe folder:

     $> ./build-tools/src/main/resources/bash/prepare-bundles-for-central

    or

     $> mvn-create-bundle --create -Dmaven.test.skip=true -DperformRelease=true

A.6) Commit, Tag and push to Github:

     $> git commit
     $> git tag "vX.Y.Z"
     $> git push --all --tags

B) PHASE 2: DISSEMINATION OF THE RELEASE VERSION

B.1) Updload the Maven Bundle on Maven Central with
     [http://oss.sonatype.org](http://oss.sonatype.org)

B.2) Update the  Arakhne.org website (including the Javadoc).

B.3) Move all the remaining issues on Github to the following version.

B.4) Close the released milestone on Github.

B.5) Generate the changelog from Git and put in the milestone's description.

B.6) Announce the new version of AFC on the mailing lists.

C) PHASE 3: DEVELOPMENT VERSION

C.1) Revert step A.1.

C.2) Compiling locally without error.

     $> rm -rf $HOME/.m2/repository
     $> mvn clean install deploy

C.3) Commit and push to Github:

     $> git commit
     $> git push --all



