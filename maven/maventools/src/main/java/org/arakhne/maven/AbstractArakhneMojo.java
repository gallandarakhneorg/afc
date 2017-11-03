/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.maven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EventListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Developer;
import org.apache.maven.model.License;
import org.apache.maven.model.Model;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Scm;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.sonatype.plexus.build.incremental.BuildContext;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;

/**
 * Abstract implementation for all Arakhn&ecirc; maven modules. This implementation is thread safe.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 *
 * @component
 */
@SuppressWarnings({"checkstyle:classfanoutcomplexity", "checkstyle:classdataabstractioncoupling",
				"checkstyle:methodcount"})
public abstract class AbstractArakhneMojo extends AbstractMojo {

	/**
	 * Empty string constant.
	 */
	public static final String EMPTY_STRING = ExtendedArtifact.EMPTY_STRING;

	/**
	 * Maven tag for artifcat id.
	 */
	public static final String PROP_ARTIFACTID = "artifactId"; //$NON-NLS-1$

	/**
	 * Maven tag for goup id.
	 */
	public static final String PROP_GROUPID = "groupId"; //$NON-NLS-1$

	/**
	 * Maven tag for version description.
	 */
	public static final String PROP_VERSION = "version"; //$NON-NLS-1$

	/**
	 * Preferred charset for the new MacOS and Linux operating systems.
	 */
	public static final String PREFERRED_CHARSET_UNIX = "UTF-8"; //$NON-NLS-1$

	/**
	 * Preferred charset for the Windows operating systems.
	 */
	public static final String PREFERRED_CHARSET_WINDOWS = "windows-1250"; //$NON-NLS-1$

	/**
	 * Preferred charset for the old MacOS operating systems.
	 */
	public static final String PREFERRED_CHARSET_MACOS = "MacRoman"; //$NON-NLS-1$

	/**
	 * Preferred charset for the Java virtual machine (internal charset).
	 */
	public static final String PREFERRED_CHARSET_JVM = "UTF-16"; //$NON-NLS-1$

	private static final int FILE_BUFFER = 4096;

	/**
	 * Invocation date.
	 */
	protected final Date invocationDate = new Date();

	/**
	 * Map the directory of pom.xml files to the definition of the corresponding maven module.
	 */
	private final Map<File, ExtendedArtifact> localArtifactDescriptions = new TreeMap<>();

	/**
	 * Map the artifact id to the definition of the corresponding maven module.
	 */
	private final Map<String, ExtendedArtifact> remoteArtifactDescriptions = new TreeMap<>();

	/**
	 * Manager of the SVN repository.
	 */
	private SVNClientManager svnManager;

	/**
	 * Are the preferred charset in the preferred order.
	 */
	private Charset[] preferredCharsets;

	/** Construct.
	 */
	public AbstractArakhneMojo() {
		final List<Charset> availableCharsets = new ArrayList<>();

		// New Mac OS and Linux OS
		addCharset(availableCharsets, PREFERRED_CHARSET_UNIX);
		// Windows OS
		addCharset(availableCharsets, PREFERRED_CHARSET_WINDOWS);
		// Old Mac OS
		addCharset(availableCharsets, PREFERRED_CHARSET_MACOS);
		// Java Internal
		addCharset(availableCharsets, PREFERRED_CHARSET_JVM);

		this.preferredCharsets = new Charset[availableCharsets.size()];
		availableCharsets.toArray(this.preferredCharsets);
		availableCharsets.clear();
	}

	/** Replies the preferred URL for the given contributor.
	 *
	 * @param contributor the contributor.
	 * @param log the log.
	 * @return the URL or <code>null</code> if no URL could be built.
	 */
	protected static URL getContributorURL(Contributor contributor, Log log) {
		URL url = null;
		if (contributor != null) {
			String rawUrl = contributor.getUrl();
			if (rawUrl != null && !EMPTY_STRING.equals(rawUrl)) {
				try {
					url = new URL(rawUrl);
				} catch (Throwable exception) {
					url = null;
				}
			}
			if (url == null) {
				rawUrl = contributor.getEmail();
				if (rawUrl != null && !EMPTY_STRING.equals(rawUrl)) {
					try {
						url = new URL("mailto:" + rawUrl); //$NON-NLS-1$
					} catch (Throwable exception) {
						url = null;
					}
				}
			}
		}
		return url;
	}

	/**
	 * Copy a directory.
	 *
	 * @param in input directory.
	 * @param out output directory.
	 * @param skipHiddenFiles indicates if the hidden files should be ignored.
	 * @throws IOException on error.
	 * @since 3.3
	 */
	public final void dirCopy(File in, File out, boolean skipHiddenFiles) throws IOException {
		assert in != null;
		assert out != null;
		getLog().debug(in.toString() + "->" + out.toString()); //$NON-NLS-1$
		getLog().debug("Ignore hidden files: " + skipHiddenFiles); //$NON-NLS-1$
		out.mkdirs();
		final LinkedList<File> candidates = new LinkedList<>();
		candidates.add(in);
		File[] children;
		while (!candidates.isEmpty()) {
			final File f = candidates.removeFirst();
			getLog().debug("Scanning: " + f); //$NON-NLS-1$
			if (f.isDirectory()) {
				children = f.listFiles();
				if (children != null && children.length > 0) {
					// Non empty directory
					for (final File c : children) {
						if (!skipHiddenFiles || !c.isHidden()) {
							getLog().debug("Discovering: " + c); //$NON-NLS-1$
							candidates.add(c);
						}
					}
				}
			} else {
				// not a directory
				final File targetFile = toOutput(in, f, out);
				targetFile.getParentFile().mkdirs();
				fileCopy(f, targetFile);
			}
		}
	}

	private static File toOutput(File root, File file, File newRoot) {
		final String filename = file.getAbsolutePath();
		final String rootPath = root.getAbsolutePath();
		return new File(filename.replaceAll("^\\Q" + rootPath + "\\E", //$NON-NLS-1$ //$NON-NLS-2$
				newRoot.getAbsolutePath()));
	}

	/**
	 * Delete a directory and its content.
	 *
	 * @param dir the directory to remove.
	 * @throws IOException on error.
	 * @since 3.3
	 */
	public final void dirRemove(File dir) throws IOException {
		if (dir != null) {
			getLog().debug("Deleting tree: " + dir.toString()); //$NON-NLS-1$
			final LinkedList<File> candidates = new LinkedList<>();
			candidates.add(dir);
			File[] children;
			final BuildContext buildContext = getBuildContext();
			while (!candidates.isEmpty()) {
				final File f = candidates.getFirst();
				getLog().debug("Scanning: " + f); //$NON-NLS-1$
				if (f.isDirectory()) {
					children = f.listFiles();
					if (children != null && children.length > 0) {
						// Non empty directory
						for (final File c : children) {
							getLog().debug("Discovering: " + c); //$NON-NLS-1$
							candidates.push(c);
						}
					} else {
						// empty directory
						getLog().debug("Deleting: " + f); //$NON-NLS-1$
						candidates.removeFirst();
						f.delete();
						buildContext.refresh(f.getParentFile());
					}
				} else {
					// not a directory
					candidates.removeFirst();
					if (f.exists()) {
						getLog().debug("Deleting: " + f); //$NON-NLS-1$
						f.delete();
						buildContext.refresh(f.getParentFile());
					}
				}
			}
			getLog().debug("Deletion done"); //$NON-NLS-1$
		}
	}

	/**
	 * Copy a file.
	 *
	 * @param in input file.
	 * @param out output file.
	 * @throws IOException on error.
	 */
	public final void fileCopy(File in, File out) throws IOException {
		assert in != null;
		assert out != null;
		getLog().debug("Copying file: " + in.toString() + " into " + out.toString()); //$NON-NLS-1$ //$NON-NLS-2$
		try (FileInputStream fis = new FileInputStream(in)) {
			try (FileChannel inChannel = fis.getChannel()) {
				try (FileOutputStream fos = new FileOutputStream(out)) {
					try (FileChannel outChannel = fos.getChannel()) {
						inChannel.transferTo(0, inChannel.size(), outChannel);
					}
				}
			}
		} finally {
			getBuildContext().refresh(out);
		}
	}

	/**
	 * Copy a file.
	 *
	 * @param in input file.
	 * @param out output file.
	 * @throws IOException on error.
	 */
	public final void fileCopy(URL in, File out) throws IOException {
		assert in != null;
		try (InputStream inStream = in.openStream()) {
			try (OutputStream outStream = new FileOutputStream(out)) {
				final byte[] buf = new byte[FILE_BUFFER];
				int len;
				while ((len = inStream.read(buf)) > 0) {
					outStream.write(buf, 0, len);
				}
			}
		} finally {
			getBuildContext().refresh(out);
		}
	}

	/**
	 * Read a resource property and replace the parametrized macros by the given parameters.
	 *
	 * @param source
	 *            is the source of the properties.
	 * @param label
	 *            is the name of the property.
	 * @param params
	 *            are the parameters to replace.
	 * @return the read text.
	 */
	public static final String getLString(Class<?> source, String label, Object... params) {
		final ResourceBundle rb = ResourceBundle.getBundle(source.getCanonicalName());
		String text = rb.getString(label);
		text = text.replaceAll("[\\n\\r]", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		text = text.replaceAll("\\t", "\t"); //$NON-NLS-1$ //$NON-NLS-2$
		text = MessageFormat.format(text, params);
		return text;
	}

	/**
	 * Remove the path prefix from a file.
	 *
	 * @param prefix path prefix to remove.
	 * @param file input filename.
	 * @return the {@code file} without the prefix.
	 */
	public static final String removePathPrefix(File prefix, File file) {
		final String r = file.getAbsolutePath().replaceFirst(
				"^" //$NON-NLS-1$
				+ Pattern.quote(prefix.getAbsolutePath()),
				EMPTY_STRING);
		if (r.startsWith(File.separator)) {
			return r.substring(File.separator.length());
		}
		return r;
	}

	private static void addCharset(List<Charset> availableCharsets, String csName) {
		try {
			final Charset cs = Charset.forName(csName);
			if (!availableCharsets.contains(cs)) {
				availableCharsets.add(cs);
			}
		} catch (Throwable exception) {
			//
		}
	}

	/**
	 * Replies the preferred charsets in the preferred order of use.
	 *
	 * @return the preferred charsets in the preferred order of use.
	 */
	public final Charset[] getPreferredCharsets() {
		return this.preferredCharsets;
	}

	/**
	 * Set the preferred charsets in the preferred order of use.
	 *
	 * @param charsets
	 *            are the preferred charsets in the preferred order of use.
	 */
	public final void setPreferredCharsets(Charset... charsets) {
		this.preferredCharsets = charsets;
	}

	/**
	 * Replies the manager of the SVN repository.
	 *
	 * @return the manager of the SVN repository.
	 */
	public final synchronized SVNClientManager getSVNClientManager() {
		if (this.svnManager == null) {
			this.svnManager = SVNClientManager.newInstance();
		}
		return this.svnManager;
	}

	/**
	 * Replies the artifact handler manager.
	 *
	 * <p>It is an attribute defined as: <pre><code>
	 * <span>/</span>* <span>@</span>component
	 * <span>*</span>/
	 * private ArtifactHandlerManager manager;
	 * </code></pre>
	 *
	 * @return the artifact resolver.
	 */
	public abstract ArtifactHandlerManager getArtifactHandlerManager();

	/**
	 * Replies the output directory of the project. Basically it is <code>getRootDirectory()+"/target"</code>.
	 *
	 * <p>It is an attribute defined as: <pre><code>
	 * <span>/</span>* <span>@</span>parameter expression="&#36;{project.build.directory}"
	 * <span>*</span>/
	 * private File outputDirectory;
	 * </code></pre>
	 *
	 * @return the output directory.
	 */
	public abstract File getOutputDirectory();

	/**
	 * Replies the root directory of the project. Basically it is the value stored inside the
	 * Maven property named <code>project.basedir</code>.
	 *
	 * <p>It is an attribute defined as: <pre><code>
	 * <span>/</span>* <span>@</span>parameter expression="${project.basedir}"
	 * <span>*</span>/
	 * private File baseDirectory;
	 * </code></pre>
	 *
	 * @return the root directory.
	 */
	public abstract File getBaseDirectory();

	/** Replies the build context that may be used during Mojo execution.
	 * This build context permits to be used inside and outside the
	 * Eclipse IDE.
	 *
	 * @return the build context.
	 */
	public abstract BuildContext getBuildContext();

	/**
	 * Replies the current maven session. Basically it is an internal component of Maven.
	 *
	 * <p>It is an attribute defined as: <pre><code>
	 * <span>/</span>* <span>@</span>component role="org.apache.maven.project.MavenProjectBuilder"
	 * * <span>@</span>required
	 * * <span>@</span>readonly
	 * <span>*</span>/
	 * private MavenProjectBuilder projectBuilder;
	 * </code></pre>
	 *
	 * @return the maven session
	 */
	public abstract MavenProjectBuilder getMavenProjectBuilder();

	/**
	 * Replies the current project builder. Basically it is an internal component of Maven.
	 *
	 * <p>It is an attribute defined as: <pre><code>
	 * <span>/</span>* <span>@</span>parameter expression="&#36;{session}"
	 * * <span>@</span>required
	 * <span>*</span>/
	 * private MavenSession mvnSession;
	 * </code></pre>
	 *
	 * @return the maven session
	 */
	public abstract MavenSession getMavenSession();

	/**
	 * Search and reply the maven artifact which is corresponding to the given file.
	 *
	 * @param file
	 *            is the file for which the maven artifact should be retreived.
	 * @return the maven artifact or <code>null</code> if none.
	 */
	public final synchronized ExtendedArtifact searchArtifact(File file) {
		final String filename = removePathPrefix(getBaseDirectory(), file);

		getLog().debug("Retreiving module for " + filename); //$NON-NLS-1$

		File theFile = file;
		File pomDirectory = null;
		while (theFile != null && pomDirectory == null) {
			if (theFile.isDirectory()) {
				final File pomFile = new File(theFile, "pom.xml"); //$NON-NLS-1$
				if (pomFile.exists()) {
					pomDirectory = theFile;
				}
			}
			theFile = theFile.getParentFile();
		}

		if (pomDirectory != null) {
			ExtendedArtifact a = this.localArtifactDescriptions.get(pomDirectory);
			if (a == null) {
				a = readPom(pomDirectory);
				this.localArtifactDescriptions.put(pomDirectory, a);
				getLog().debug("Found local module description for " //$NON-NLS-1$
						+ a.toString());
			}
			return a;
		}

		final BuildContext buildContext = getBuildContext();
		buildContext.addMessage(file,
				1, 1,
				"The maven module for this file cannot be retreived.", //$NON-NLS-1$
				BuildContext.SEVERITY_WARNING, null);
		return null;
	}

	/**
	 * Replies the project's remote repositories to use for the resolution of plugins and their dependencies..
	 *
	 * <p>It is an attribute defined as: <pre><code>
	 * <span>/</span>* <span>@</span>parameter default-value="&#36;{project.remoteProjectRepositories}"
	 * <span>*</span>/
	 * private List&lt;RemoteRepository&gt; remoteRepos;
	 * </code></pre>
	 *
	 * @return the repository system
	 */
	public abstract  List<RemoteRepository> getRemoteRepositoryList();

	/**
	 * Replies the repository system used by this maven instance. Basically it is an internal component of Maven.
	 *
	 * <p>It is an attribute defined as: <pre><code>
	 * <span>/</span>* <span>@</span>component
	 * <span>*</span>/
	 * private RepositorySystem repoSystem;
	 * </code></pre>
	 *
	 * @return the repository system
	 */
	public abstract RepositorySystem getRepositorySystem();

	/**
	 * Replies the current repository/network configuration of Maven..
	 *
	 * <p>It is an attribute defined as: <pre><code>
	 * <span>/</span>* <span>@</span>parameter default-value="&#36;{repositorySystemSession}"
	 * <span>@</span>readonly
	 * <span>*</span>/
	 * private RepositorySystemSession repoSession;
	 * </code></pre>
	 *
	 * @return the repository system
	 */
	public abstract RepositorySystemSession getRepositorySystemSession();

	/**
	 * Retreive the extended artifact definition of the given artifact.
	 * @param mavenArtifact - the artifact to resolve
	 * @return the artifact definition.
	 * @throws MojoExecutionException on error.
	 */
	public final Artifact resolveArtifact(Artifact mavenArtifact) throws MojoExecutionException {
		final org.eclipse.aether.artifact.Artifact aetherArtifact = createArtifact(mavenArtifact);
		final ArtifactRequest request = new ArtifactRequest();
		request.setArtifact(aetherArtifact);
		request.setRepositories(getRemoteRepositoryList());
		final ArtifactResult result;
		try {
			result = getRepositorySystem().resolveArtifact(getRepositorySystemSession(), request);
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
		return createArtifact(result.getArtifact());
	}

	/**
	 * Retreive the extended artifact definition of the given artifact id.
	 *
	 * @param groupId
	 *            is the identifier of the group.
	 * @param artifactId
	 *            is the identifier of the artifact.
	 * @param version
	 *            is the version of the artifact to retreive.
	 * @return the artifact definition.
	 * @throws MojoExecutionException on error.
	 */
	public final Artifact resolveArtifact(String groupId, String artifactId, String version) throws MojoExecutionException {
		return resolveArtifact(createArtifact(groupId, artifactId, version));
	}

	/**
	 * Replies a list of files which are found on the file system.
	 *
	 * @param directory
	 *            is the directory to search in.
	 * @param filter
	 *            is the file selector
	 * @return the list of files.
	 */
	public final Collection<File> findFiles(File directory, FileFilter filter) {
		final Collection<File> files = new ArrayList<>();
		findFiles(directory, filter, files);
		return files;
	}

	/**
	 * Replies a list of files which are found on the file system.
	 *
	 * @param directory
	 *            is the directory to search in.
	 * @param filter
	 *            is the file selector
	 * @param fileOut
	 *            is the list of files to fill.
	 */
	public final synchronized void findFiles(File directory, FileFilter filter, Collection<? super File> fileOut) {
		if (directory != null && filter != null) {
			File candidate;
			final List<File> candidates = new ArrayList<>();

			final String relativePath = removePathPrefix(getBaseDirectory(), directory);

			getLog().debug("Retreiving " //$NON-NLS-1$
					+ filter.toString() + " files from " //$NON-NLS-1$
					+ relativePath);

			candidates.add(directory);
			int nbFiles = 0;

			while (!candidates.isEmpty()) {
				candidate = candidates.remove(0);
				if (candidate.isDirectory()) {
					final File[] children = candidate.listFiles(filter);
					if (children != null) {
						for (final File child : children) {
							if (child != null && child.isDirectory()) {
								candidates.add(child);
							} else {
								fileOut.add(child);
								++nbFiles;
							}
						}
					}
				}
			}

			getLog().debug("Found " //$NON-NLS-1$
					+ nbFiles + " file(s)"); //$NON-NLS-1$
		}
	}

	/**
	 * Replies a map of files which are found on the file system. The map has the
	 * found files as keys and the search directory as values.
	 *
	 * @param directory
	 *            is the directory to search in.
	 * @param filter
	 *            is the file selector
	 * @param fileOut
	 *            is the list of files to fill.
	 */
	public final synchronized void findFiles(File directory, FileFilter filter, Map<? super File, File> fileOut) {
		findFiles(directory, filter, fileOut, null);
	}

	/**
	 * Replies a map of files which are found on the file system. The map has the
	 * found files as keys and the search directory as values.
	 *
	 * @param directory
	 *            is the directory to search in.
	 * @param filter
	 *            is the file selector
	 * @param fileOut
	 *            is the list of files to fill.
	 * @param listener on the files that are not matching the file filter.
	 */
	public final synchronized void findFiles(File directory, FileFilter filter, Map<? super File, File> fileOut,
			FindFileListener listener) {
		if (directory != null && filter != null) {
			File candidate;
			final List<File> candidates = new ArrayList<>();

			final String relativePath = removePathPrefix(getBaseDirectory(), directory);

			getLog().debug("Retreiving " //$NON-NLS-1$
					+ filter.toString() + " files from " //$NON-NLS-1$
					+ relativePath);

			candidates.add(directory);
			int nbFiles = 0;

			while (!candidates.isEmpty()) {
				candidate = candidates.remove(0);
				if (candidate.isDirectory()) {
					final File[] children = candidate.listFiles();
					if (children != null) {
						for (final File child : children) {
							if (child != null && child.isDirectory()) {
								candidates.add(child);
							} else if (filter.accept(child)) {
								fileOut.put(child, directory);
								++nbFiles;
							} else if (listener != null) {
								listener.findFile(child, directory);
							}
						}
					}
				}
			}

			getLog().debug("Found " //$NON-NLS-1$
					+ nbFiles + " file(s)"); //$NON-NLS-1$
		}
	}

	/**
	 * Replies the maven artifact which is described by the <code>pom.xml</code> file in the given directory.
	 *
	 * @param pomDirectory
	 *            is the directory where to find the <code>pom.xml</code> file.
	 * @return the artifact or <code>null</code>.
	 */
	public final synchronized ExtendedArtifact readPom(File pomDirectory) {
		return readPomFile(new File(pomDirectory, "pom.xml")); //$NON-NLS-1$
	}

	/**
	 * Replies the maven artifact which is described by the given <code>pom.xml</code>.
	 *
	 * @param pomFile
	 *            is the <code>pom.xml</code> file.
	 * @return the artifact or <code>null</code>.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity", "checkstyle:nestedifdepth"})
	public final synchronized ExtendedArtifact readPomFile(File pomFile) {
		String groupId;
		final String artifactId;
		final String name;
		String version;
		final String url;
		final Organization organization;
		final Scm scm;
		List<Developer> developers;
		List<Contributor> contributors;
		List<License> licenses;
		final Parent parent;

		getLog().debug("Read pom file: " + pomFile.toString()); //$NON-NLS-1$

		if (!pomFile.canRead()) {
			return null;
		}

		final MavenXpp3Reader pomReader = new MavenXpp3Reader();
		try (FileReader fr = new FileReader(pomFile)) {
			final Model model = pomReader.read(fr);
			groupId = model.getGroupId();
			artifactId = model.getArtifactId();
			name = model.getName();
			version = model.getVersion();
			url = model.getUrl();
			organization = model.getOrganization();
			scm = model.getScm();

			developers = model.getDevelopers();
			contributors = model.getContributors();
			licenses = model.getLicenses();

			parent = model.getParent();
		} catch (IOException | XmlPullParserException e) {
			return null;
		}
		if (developers == null) {
			developers = new ArrayList<>();
		} else {
			final List<Developer> list = new ArrayList<>();
			list.addAll(developers);
			developers = list;
		}
		if (contributors == null) {
			contributors = new ArrayList<>();
		} else {
			final List<Contributor> list = new ArrayList<>();
			list.addAll(contributors);
			contributors = list;
		}
		if (licenses == null) {
			licenses = new ArrayList<>();
		} else {
			final List<License> list = new ArrayList<>();
			list.addAll(licenses);
			licenses = list;
		}

		if (parent != null) {
			final String relPath = parent.getRelativePath();
			File parentPomDirectory = new File(pomFile.getParentFile(), relPath);
			try {
				parentPomDirectory = parentPomDirectory.getCanonicalFile();
				if (!parentPomDirectory.isDirectory()) {
					parentPomDirectory = parentPomDirectory.getParentFile();
				}
				ExtendedArtifact parentArtifact = this.localArtifactDescriptions.get(parentPomDirectory);
				if (parentArtifact == null) {
					parentArtifact = readPom(parentPomDirectory);
					if (parentArtifact != null) {
						this.localArtifactDescriptions.put(parentPomDirectory, parentArtifact);
						getLog().debug("Add local module description for " //$NON-NLS-1$
								+ parentArtifact.toString());
					} else {
						final String key = ArtifactUtils.key(parent.getGroupId(), parent.getArtifactId(), parent.getVersion());
						final Artifact artifact = createArtifact(parent.getGroupId(),
								parent.getArtifactId(), parent.getVersion());
						final ArtifactRepository repo = getMavenSession().getLocalRepository();
						String artifactPath = repo.pathOf(artifact);
						artifactPath = artifactPath.replaceFirst("\\.jar$", ".pom"); //$NON-NLS-1$ //$NON-NLS-2$
						final File artifactFile = new File(repo.getBasedir(), artifactPath);
						getLog().debug("Getting pom file in local repository for " //$NON-NLS-1$
								+ key + ": " + artifactFile.getAbsolutePath()); //$NON-NLS-1$
						final BuildContext buildContext = getBuildContext();
						buildContext.removeMessages(pomFile);
						if (artifactFile.canRead()) {
							parentArtifact = readPomFile(artifactFile);
							if (parentArtifact != null) {
								this.remoteArtifactDescriptions.put(key, parentArtifact);
								getLog().debug("Add remote module description for " //$NON-NLS-1$
										+ parentArtifact.toString());
							} else {
								buildContext.addMessage(
										pomFile,
										1, 1,
										"Unable to retreive the pom file of " + key, //$NON-NLS-1$
										BuildContext.SEVERITY_WARNING, null);
							}
						} else {
							buildContext.addMessage(
									pomFile,
									1, 1,
									"Cannot read the file for '" + key + "': " //$NON-NLS-1$ //$NON-NLS-2$
									+ artifactFile.getAbsolutePath(),
									BuildContext.SEVERITY_WARNING, null);
						}
					}
				}
				if (parentArtifact != null) {
					developers.addAll(parentArtifact.getDevelopers());
					contributors.addAll(parentArtifact.getContributors());
				}
			} catch (IOException e) {
				getLog().warn(e);
			}

			// Be sure that the optional fields version and groupId are correctly set.
			if (version == null || version.isEmpty()) {
				version = parent.getVersion();
			}

			if (groupId == null || groupId.isEmpty()) {
				groupId = parent.getGroupId();
			}
		}

		String scmRevision = null;

		try {
			final SVNClientManager svnManager = getSVNClientManager();
			final SVNInfo svnInfo = svnManager.getWCClient().doInfo(pomFile.getParentFile(), SVNRevision.UNDEFINED);
			if (svnInfo != null) {
				final SVNRevision revision = svnInfo.getRevision();
				if (revision != null) {
					scmRevision = Long.toString(revision.getNumber());
				}
			}
		} catch (SVNException exception) {
			//
		}

		final Artifact a = createArtifact(groupId, artifactId, version);
		return new ExtendedArtifact(a, name, url, organization, scmRevision, scm, developers, contributors, licenses);
	}



	/**
	 * Create an Jar runtime artifact from the given values.
	 *
	 * @param groupId group id.
	 * @param artifactId artifact id.
	 * @param version version number.
	 * @return the artifact
	 */
	public final Artifact createArtifact(String groupId, String artifactId, String version) {
		return createArtifact(groupId, artifactId, version, "runtime", "jar"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** Convert the maven artifact to Aether artifact.
	 *
	 * @param artifact - the maven artifact.
	 * @return the Aether artifact.
	 */
	protected static final org.eclipse.aether.artifact.Artifact createArtifact(Artifact artifact) {
		return new DefaultArtifact(
				artifact.getGroupId(),
				artifact.getArtifactId(),
				artifact.getClassifier(),
				artifact.getType(),
				artifact.getVersion());
	}

	/** Convert the Aether artifact to maven artifact.
	 *
	 * @param artifact - the Aether artifact.
	 * @return the maven artifact.
	 */
	protected final Artifact createArtifact(org.eclipse.aether.artifact.Artifact artifact) {
		return createArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion());
	}

	/**
	 * Create an artifact from the given values.
	 *
	 * @param groupId group id.
	 * @param artifactId artifact id.
	 * @param version version number.
	 * @param scope artifact scope.
	 * @param type artifact type.
	 * @return the artifact
	 */
	public final Artifact createArtifact(String groupId, String artifactId, String version, String scope, String type) {
		VersionRange versionRange = null;
		if (version != null) {
			versionRange = VersionRange.createFromVersion(version);
		}
		String desiredScope = scope;

		if (Artifact.SCOPE_TEST.equals(desiredScope)) {
			desiredScope = Artifact.SCOPE_TEST;
		}

		if (Artifact.SCOPE_PROVIDED.equals(desiredScope)) {
			desiredScope = Artifact.SCOPE_PROVIDED;
		}

		if (Artifact.SCOPE_SYSTEM.equals(desiredScope)) {
			// system scopes come through unchanged...
			desiredScope = Artifact.SCOPE_SYSTEM;
		}

		final ArtifactHandler handler = getArtifactHandlerManager().getArtifactHandler(type);

		return new org.apache.maven.artifact.DefaultArtifact(
				groupId, artifactId, versionRange,
				desiredScope, type, null,
				handler, false);
	}

	/**
	 * Check if the values of the attributes of this Mojo are correctly set. This function may
	 * be overridden by subclasses to test subclasse's attributes.
	 *
	 * @throws MojoExecutionException on error.
	 */
	protected abstract void checkMojoAttributes() throws MojoExecutionException;

	@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	private static String getLogType(Object obj) {
		if (obj instanceof Boolean || obj instanceof AtomicBoolean) {
			return "B"; //$NON-NLS-1$
		}
		if (obj instanceof Byte) {
			return "b"; //$NON-NLS-1$
		}
		if (obj instanceof Short) {
			return "s"; //$NON-NLS-1$
		}
		if (obj instanceof Integer || obj instanceof AtomicInteger) {
			return "i"; //$NON-NLS-1$
		}
		if (obj instanceof Long || obj instanceof AtomicLong) {
			return "l"; //$NON-NLS-1$
		}
		if (obj instanceof Float) {
			return "f"; //$NON-NLS-1$
		}
		if (obj instanceof Double) {
			return "d"; //$NON-NLS-1$
		}
		if (obj instanceof BigDecimal) {
			return "D"; //$NON-NLS-1$
		}
		if (obj instanceof BigInteger) {
			return "I"; //$NON-NLS-1$
		}
		if (obj instanceof CharSequence) {
			return "s"; //$NON-NLS-1$
		}
		if (obj instanceof Array) {
			final Array array = (Array) obj;
			return array.getClass().getComponentType().getName() + "[]"; //$NON-NLS-1$
		}
		if (obj instanceof Set<?>) {
			return "set"; //$NON-NLS-1$
		}
		if (obj instanceof Map<?, ?>) {
			return "map"; //$NON-NLS-1$
		}
		if (obj instanceof List<?>) {
			return "list"; //$NON-NLS-1$
		}
		if (obj instanceof Collection<?>) {
			return "col"; //$NON-NLS-1$
		}
		return "o"; //$NON-NLS-1$
	}

	/**
	 * Throw an exception when the given object is null.
	 *
	 * @param message
	 *            is the message to put in the exception.
	 * @param obj the object to test.
	 */
	protected final void assertNotNull(String message, Object obj) {
		if (getLog().isDebugEnabled()) {
			getLog().debug(
					"\t(" //$NON-NLS-1$
					+	getLogType(obj)
					+	") " //$NON-NLS-1$
					+	message
					+	" = " //$NON-NLS-1$
					+	obj);
		}
		if (obj == null) {
			throw new AssertionError("assertNotNull: " + message); //$NON-NLS-1$
		}
	}

	@Override
	public final void execute() throws MojoExecutionException {
		try {
			checkMojoAttributes();
			executeMojo();
		} finally {
			clearInternalBuffers();
		}
	}

	/**
	 * Clear internal buffers.
	 */
	protected synchronized void clearInternalBuffers() {
		this.localArtifactDescriptions.clear();
		this.remoteArtifactDescriptions.clear();
	}

	/**
	 * Invoked when the Mojo should be executed.
	 *
	 * @throws MojoExecutionException on error.
	 */
	protected abstract void executeMojo() throws MojoExecutionException;

	/** Join the values with the given joint.
	 *
	 * @param joint the joint.
	 * @param values the values.
	 * @return the jointed values
	 */
	public static String join(String joint, String... values) {
		final StringBuilder b = new StringBuilder();
		for (final String value : values) {
			if (value != null && !EMPTY_STRING.equals(value)) {
				if (b.length() > 0) {
					b.append(joint);
				}
				b.append(value);
			}
		}
		return b.toString();
	}

	private static void detectEncoding(File file, CharsetDecoder decoder) throws IOException, CharacterCodingException {
		decoder.onMalformedInput(CodingErrorAction.REPORT);
		decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
		try (FileInputStream fis = new FileInputStream(file)) {
			try (ReadableByteChannel channel = Channels.newChannel(fis)) {
				try (Reader reader = Channels.newReader(channel, decoder, -1)) {
					try (BufferedReader bReader = new BufferedReader(reader)) {
						String line = bReader.readLine();
						while (line != null) {
							line = bReader.readLine();
						}
					}
				}
			}
		}
	}

	/**
	 * Try to detect and reply the encoding of the given file. This function uses the
	 * charsets replied by {@link #getPreferredCharsets()} to select a charset when many are possible.
	 *
	 * @param file
	 *            is the file to read.
	 * @return the encoding charset of the given file or <code>null</code> if the encoding could not be detected.
	 * @see #getPreferredCharsets()
	 * @see #setPreferredCharsets(Charset...)
	 */
	@SuppressWarnings("checkstyle:npathcomplexity")
	public final Charset detectEncoding(File file) {
		final Collection<Charset> fittingCharsets = new TreeSet<>();
		for (final Charset c : Charset.availableCharsets().values()) {
			final CharsetDecoder decoder = c.newDecoder();
			try {
				detectEncoding(file, decoder);
				fittingCharsets.add(c);
			} catch (Throwable e) {
				//
			}
		}
		if (getLog().isDebugEnabled()) {
			getLog().debug("Valid charsets for " + file.getName() + ":\n" //$NON-NLS-1$ //$NON-NLS-2$
					+ fittingCharsets.toString());
		}

		for (final Charset prefCharset : getPreferredCharsets()) {
			if (prefCharset.canEncode() && fittingCharsets.contains(prefCharset)) {
				getLog().debug("Use preferred charset for " + file.getName() //$NON-NLS-1$
						+ ": " + prefCharset.displayName()); //$NON-NLS-1$
				return prefCharset;
			}
		}

		final Charset platformCharset = Charset.defaultCharset();

		if (platformCharset.canEncode() && fittingCharsets.contains(platformCharset)) {
			getLog().debug("Use platform default charset for " + file.getName() + ": " //$NON-NLS-1$ //$NON-NLS-2$
					+ platformCharset.displayName());
			return Charset.defaultCharset();
		}

		final Iterator<Charset> iterator = fittingCharsets.iterator();
		while (iterator.hasNext()) {
			final Charset c = iterator.next();
			if (c.canEncode()) {
				getLog().debug("Use first valid charset for " + file.getName() + ": " //$NON-NLS-1$ //$NON-NLS-2$
						+ c.displayName());
				return c;
			}
		}

		return null;
	}

	/** Replies the dependencies specified in the the Maven configuration
	 * of the current project.
	 *
	 * @param isTransitive indicates if the dependencies of dependencies
	 *     must also be replied by the iterator.
	 * @return the iterator.
	 * @see #getDependencies(MavenProject, boolean)
	 */
	public final Iterator<MavenProject> getDependencies(boolean isTransitive) {
		return getDependencies(getMavenSession().getCurrentProject(), isTransitive);
	}

	/** Replies the dependencies specified in the the Maven configuration
	 * of the given project.
	 *
	 * @param project is the maven project for which the dependencies must be replied.
	 * @param isTransitive indicates if the dependencies of dependencies
	 *     must also be replied by the iterator.
	 * @return the iterator.
	 * @see #getDependencies(boolean)
	 */
	public final Iterator<MavenProject> getDependencies(MavenProject project, boolean isTransitive) {
		return new DependencyIterator(project, isTransitive);
	}

	/** Replies the plugins specified in the the Maven configuration
	 * of the current project.
	 *
	 * @param isTransitive indicates if the plugins of dependencies
	 *     must also be replied by the iterator.
	 * @return the iterator.
	 * @see #getPlugins(MavenProject, boolean)
	 */
	public final Iterator<Plugin> getPlugins(boolean isTransitive) {
		return getPlugins(getMavenSession().getCurrentProject(), isTransitive);
	}

	/** Replies the plugins specified in the the Maven configuration
	 * of the given project.
	 *
	 * @param project is the maven project for which the plugins must be replied.
	 * @param isTransitive indicates if the plugins of dependencies
	 *     must also be replied by the iterator.
	 * @return the iterator.
	 * @see #getPlugins(boolean)
	 */
	public final Iterator<Plugin> getPlugins(MavenProject project, boolean isTransitive) {
		return new PluginIterator(project, isTransitive);
	}

	/** Load the Maven project for the given artifact.
	 *
	 * @param artifact the artifact.
	 * @return the maven project.
	 */
	public MavenProject getMavenProject(Artifact artifact) {
		try {
			final MavenSession session = getMavenSession();
			final MavenProject current = session.getCurrentProject();
			final MavenProject prj = getMavenProjectBuilder().buildFromRepository(
					artifact,
					current.getRemoteArtifactRepositories(),
					session.getLocalRepository());
			return prj;
		} catch (ProjectBuildingException e) {
			getLog().warn(e);
		}
		return null;
	}

	/** Dependency iterator.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class DependencyIterator implements Iterator<MavenProject> {

		private final List<ArtifactRepository> remoteRepositiories;

		private final boolean isTransitive;

		private final File projectFile;

		private List<Dependency> dependencies = new ArrayList<>();

		private Set<String> treated = new TreeSet<>();

		private MavenProject next;

		/** Constuctor.
		 * @param project is the project for which the dependencies must
		 *     be replied.
		 * @param isTransitive indicates if the dependencies of dependencies must also be replied
		 *     by the iterator.
		 */
		DependencyIterator(MavenProject project, boolean isTransitive) {
			this.isTransitive = isTransitive;
			this.remoteRepositiories = project.getRemoteArtifactRepositories();
			this.dependencies.addAll(project.getDependencies());
			this.projectFile = project.getFile();
			getBuildContext().removeMessages(this.projectFile);
			searchNext();
		}

		private void searchNext() {
			this.next = null;

			while (this.next == null && !this.dependencies.isEmpty()) {
				final Dependency dependency = this.dependencies.remove(0);
				if (dependency != null) {
					final String artifactId = dependency.getGroupId() + ":" + dependency.getArtifactId() //$NON-NLS-1$
							+ ":" + dependency.getVersion(); //$NON-NLS-1$
					if (!this.treated.contains(artifactId)) {
						boolean isTreated = false;
						try {
							final Artifact dependencyArtifact = createArtifact(
									dependency.getGroupId(),
									dependency.getArtifactId(),
									dependency.getVersion(),
									dependency.getScope(),
									dependency.getType());
							resolveArtifact(dependencyArtifact);
							final MavenProjectBuilder builder = getMavenProjectBuilder();
							final MavenProject dependencyProject = builder.buildFromRepository(
									dependencyArtifact,
									this.remoteRepositiories,
									getMavenSession().getLocalRepository());
							if (dependencyProject != null) {
								if (this.isTransitive) {
									this.dependencies.addAll(dependencyProject.getDependencies());
								}
								this.next = dependencyProject;
								isTreated = true;
							}
						} catch (MojoExecutionException | ProjectBuildingException e) {
							getBuildContext().addMessage(
									this.projectFile,
									1, 1,
									"Unable to retreive the Maven plugin: " + artifactId, //$NON-NLS-1$
									BuildContext.SEVERITY_WARNING,
									e);
							isTreated = true;
						}
						if (isTreated) {
							this.treated.add(artifactId);
						}
					}
				}
			}
		}

		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public MavenProject next() {
			final MavenProject n = this.next;
			if (n == null) {
				throw new NoSuchElementException();
			}
			searchNext();
			return n;
		}

	}

	/** Plugin iterator.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class PluginIterator implements Iterator<Plugin> {

		private final Iterator<MavenProject> dependencyIterator;

		private Iterator<org.apache.maven.model.Plugin> pluginIterator;

		private Plugin next;

		/** Constructor.
		 * @param project the project.
		 * @param isTransitive indicates if the dependency is transitive.
		 */
		PluginIterator(MavenProject project, boolean isTransitive) {
			this.dependencyIterator = getDependencies(project, isTransitive);
			searchNext();
		}

		private void searchNext() {
			this.next = null;
			while (this.next == null) {
				if (this.pluginIterator != null && this.pluginIterator.hasNext()) {
					this.next = this.pluginIterator.next();
				} else if (this.dependencyIterator.hasNext()) {
					final MavenProject project = this.dependencyIterator.next();
					final List<Plugin> buildPlugins = project.getBuildPlugins();
					if (buildPlugins != null) {
						this.pluginIterator = buildPlugins.iterator();
					}
				} else {
					return;
				}
			}
		}

		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public Plugin next() {
			final org.apache.maven.model.Plugin n = this.next;
			if (n == null) {
				throw new NoSuchElementException();
			}
			searchNext();
			return n;
		}

	}

	/**
	 * Abstract implementation for all Arakhn&ecirc; maven modules. This implementation is thread safe.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 *
	 * @component
	 */
	@FunctionalInterface
	public interface FindFileListener extends EventListener {

		/** Invoked when a file which is not matching the file filter was found.
		 *
		 * @param file is the file that is not matching the file filter.
		 * @param rootDirectory is the root directory in which the file was found.
		 */
		void findFile(File file, File rootDirectory);

	}

}
