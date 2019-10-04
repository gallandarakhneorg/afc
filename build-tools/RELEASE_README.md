# RELEASE AFC

The steps for releasing AFC are:

## A) PHASE 1: PREPARATION

1. Change the P2 configuration. 
2. Updgrade Maven dependencies and plugins.
3. Compiling locally without error.

     $> rm -rf $HOME/.m2/repository
     $> mvn clean install


## B) PHASE 2: RELEASE

1. Remove `-SNAPSHOT` in all the poms.
2. Change the flags for `bytecode.optimize` and `bytecode.debug` in `pom.xm`l 
3. Create the aggregated documentation, and copy the generated archive file into a safe folder:

```sh
$> ./build-tools/src/main/resources/bash/generate_aggregate_javadoc.sh
```

4. Prepare the bundles for Maven Central, and copy the generated archive files into a safe folder:

```sh
$> ./build-tools/src/main/resources/bash/prepare-bundles-for-central.sh
```

5. Commit, Tag and push to Github:

```sh
$> git commit
$> git tag "vX.Y.Z"
$> git push --tags
$> git push --all
```

## C) PHASE 3: DISSEMINATION OF THE RELEASE VERSION

1. Updload the Maven Bundle on Maven Central with [http://oss.sonatype.org](http://oss.sonatype.org).
2. Update the [Arakhne.org website](http://www.arakhne.org) (including the Javadoc).
3. Move all the remaining issues on Github to the following version.
4. Close the released milestone on Github.
5. Generate the changelog from Git and put in the milestone's description.
6. Announce the new version of AFC on the mailing lists.

## D) PHASE 4: NEW DEVELOPMENT VERSION

1. Revert step B.1 and B.2.
2. Compiling locally without error.

```sh
$> rm -rf $HOME/.m2/repository
$> mvn clean install deploy
```

3. Commit and push to Github:

```sh
$> git commit
$> git push --all
```


