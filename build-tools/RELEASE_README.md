
RELEASE AFC
===========

The steps for releasing AFC are:

A) PHASE 1: RELEASE VERSION

A.1) Remove "-SNAPSHOT" in all the poms.

A.2) Compiling locally without error.

     $> rm -rf $HOME/.m2/repository
     $> mvn clean install -P generate-android-libraries

A.3) Prepare the bundles for Maven Central:

     $> ./scripts/prepare-bundles-for-central

    or

     $> mvn-create-bundle --create -Dmaven.test.skip=true -DperformRelease=true

A.4) Commit and push to Github:

     $> git commit
     $> git push --all

A.5) Tag the Git with the version number.

     $> git tag "vX.Y.Z"
     $> git push --tags

A.6) Create the aggregated documentation:

     $> mvn-javadoc-aggregate

   or

    $> mvn -Dmaven.test.skip=true clean org.arakhne.afc.maven:tag-replacer:generatereplacesrc javadoc:aggregate

A.6) Create the release version:

     $> mvn-release deploy

A.6) Upload on the Arakhne Maven repository.

B) PHASE 2: DISSEMINATION OF THE RELEASE VERSION

B.1) Updload the Maven Bundle on Maven Central with
     [http://oss.sonatype.org](http://oss.sonatype.org)

B.2) Update the  Arakhne.orgg website.

B.3) Move all the remaining issues on Github to the following version.

B.4) Close the released milestone on Github.

B.5) Announce the new version of SARL on the mailing lists.

C) PHASE 3: DEVELOPMENT VERSION

C.1) Revert step A.1.

C.2) Compiling locally without error.

     $> rm -rf $HOME/.m2/repository
     $> mvn clean install deploy

C.3) Commit and push to Github:

     $> git commit
     $> git push --all

C.4) Upload on the Arakhne Maven repository.


