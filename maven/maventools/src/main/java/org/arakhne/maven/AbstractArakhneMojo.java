/* 
 * $Id$
 * 
 * Copyright (C) 2010-12 Stephane GALLAND This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
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
import org.apache.maven.artifact.factory.DefaultArtifactFactory;
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
import org.apache.maven.repository.RepositorySystem;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.sonatype.plexus.build.incremental.BuildContext;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;

/**
 * Abstract implementation for all Arakhn&ecirc; maven modules. This implementation is thread safe.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * 
 * @component
 */
public abstract class AbstractArakhneMojo extends AbstractMojo {

	/** Replies the preferred URL for the given contributor.
	 * 
	 * @param contributor
	 * @param log
	 * @return the URL or <code>null</code> if no URL could be built.
	 */
	protected static URL getContributorURL(Contributor contributor, Log log) {
		URL url = null;
		if (contributor!=null) {
			String s = contributor.getUrl();
			if (s!=null && !EMPTY_STRING.equals(s)) {
				try {
					url = new URL(s);
				}
				catch(Throwable _) {
					url = null;
				}
			}
			if (url==null) {
				s = contributor.getEmail();
				if (s!=null && !EMPTY_STRING.equals(s)) {
					try {
						url = new URL("mailto:"+s); //$NON-NLS-1$
					}
					catch(Throwable _) {
						url = null;
					}
				}
			}
		}
		return url;
	}

	/**
	 * Empty string constant.
	 */
	public static final String EMPTY_STRING = ExtendedArtifact.EMPTY_STRING;

	/**
	 * Maven tag for artifcat id
	 */
	public static final String PROP_ARTIFACTID = "artifactId"; //$NON-NLS-1$

	/**
	 * Maven tag for goup id
	 */
	public static final String PROP_GROUPID = "groupId"; //$NON-NLS-1$

	/**
	 * Maven tag for version description
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

	/**
	 * Copy a directory.
	 * 
	 * @param in
	 * @param out
	 * @param skipHiddenFiles indicates if the hidden files should be ignored.
	 * @throws IOException
	 * @since 3.3
	 */
	public final void dirCopy(File in, File out, boolean skipHiddenFiles) throws IOException {
		assert (in != null);
		assert (out != null);
		getLog().debug(in.toString()+"->"+out.toString());  //$NON-NLS-1$
		getLog().debug("Ignore hidden files: "+skipHiddenFiles);  //$NON-NLS-1$
		out.mkdirs();
		LinkedList<File> candidates = new LinkedList<File>();
		candidates.add(in);
		File f, targetFile;
		File[] children;
		while (!candidates.isEmpty()) {
			f = candidates.removeFirst();
			getLog().debug("Scanning: "+f); //$NON-NLS-1$
			if (f.isDirectory()) {
				children = f.listFiles();
				if (children!=null && children.length>0) {
					// Non empty directory
					for(File c : children) {
						if (!skipHiddenFiles || !c.isHidden()) {
							getLog().debug("Discovering: "+c); //$NON-NLS-1$
							candidates.add(c);
						}
					}
				}
			}
			else {
				// not a directory
				targetFile = toOutput(in, f, out);
				targetFile.getParentFile().mkdirs();
				fileCopy(f, targetFile);
			}
		}		
	}

	private static File toOutput(File root, File file, File newRoot) {
		String filename = file.getAbsolutePath();
		String rootPath = root.getAbsolutePath();
		return new File(filename.replaceAll("^\\Q"+rootPath+"\\E", newRoot.getAbsolutePath()));  //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Delete a directory and its content.
	 * 
	 * @param dir
	 * @throws IOException
	 * @since 3.3
	 */
	public final void dirRemove(File dir) throws IOException {
		if (dir!=null) {
			getLog().debug("Deleting tree: "+dir.toString()); //$NON-NLS-1$
			LinkedList<File> candidates = new LinkedList<File>();
			candidates.add(dir);
			File f;
			File[] children;
			BuildContext buildContext = getBuildContext();
			while (!candidates.isEmpty()) {
				f = candidates.getFirst();
				getLog().debug("Scanning: "+f); //$NON-NLS-1$
				if (f.isDirectory()) {
					children = f.listFiles();
					if (children!=null && children.length>0) {
						// Non empty directory
						for(File c : children) {
							getLog().debug("Discovering: "+c); //$NON-NLS-1$
							candidates.push(c);
						}
					}
					else {
						// empty directory
						getLog().debug("Deleting: "+f); //$NON-NLS-1$
						candidates.removeFirst();
						f.delete();
						buildContext.refresh(f.getParentFile());
					}
				}
				else {
					// not a directory
					candidates.removeFirst();
					if (f.exists()) {
						getLog().debug("Deleting: "+f); //$NON-NLS-1$
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
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public final void fileCopy(File in, File out) throws IOException {
		assert (in != null);
		assert (out != null);
		getLog().debug("Copying file: "+in.toString()+" into "+out.toString()); //$NON-NLS-1$ //$NON-NLS-2$
		FileInputStream fis = new FileInputStream(in);
		FileChannel inChannel = fis.getChannel();
		FileOutputStream fos = new FileOutputStream(out);
		FileChannel outChannel = fos.getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		}
		finally {
			outChannel.close();
			fos.close();
			inChannel.close();
			fis.close();
			getBuildContext().refresh(out);
		}
	}

	/**
	 * Copy a file.
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public final void fileCopy(URL in, File out) throws IOException {
		assert (in != null);
		InputStream inStream = in.openStream();
		OutputStream outStream = new FileOutputStream(out);
		try { 
			byte[] buf = new byte[4096];
			int len;
			while ((len = inStream.read(buf)) > 0) {
				outStream.write(buf, 0, len);
			}
		}
		finally {
			outStream.close();
			inStream.close();
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
		ResourceBundle rb = ResourceBundle.getBundle(source.getCanonicalName());
		String text = rb.getString(label);
		text = text.replaceAll("[\\n\\r]", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		text = text.replaceAll("\\t", "\t"); //$NON-NLS-1$ //$NON-NLS-2$
		text = MessageFormat.format(text, params);
		return text;
	}

	/**
	 * Remove the path prefix from a file.
	 * 
	 * @param prefix
	 * @param file
	 * @return the <var>file</var> without the prefix.
	 */
	public static final String removePathPrefix(File prefix, File file) {
		String r = file.getAbsolutePath().replaceFirst(
				"^"+ //$NON-NLS-1$
						Pattern.quote(prefix.getAbsolutePath()),
						EMPTY_STRING);
		if (r.startsWith(File.separator))
			return r.substring(File.separator.length());
		return r;
	}

	/**
	 * Invocation date.
	 */
	protected final Date invocationDate = new Date();

	/**
	 * Map the directory of pom.xml files to the definition of the corresponding maven module.
	 */
	private final Map<File, ExtendedArtifact> localArtifactDescriptions = new TreeMap<File,ExtendedArtifact>();

	/**
	 * Map the artifact id to the definition of the corresponding maven module.
	 */
	private final Map<String, ExtendedArtifact> remoteArtifactDescriptions = new TreeMap<String,ExtendedArtifact>();

	/**
	 * Manager of the SVN repository.
	 */
	private SVNClientManager svnManager = null;

	/**
	 * Are the preferred charset in the preferred order.
	 */
	private Charset[] preferredCharsets;

	/**
	 */
	public AbstractArakhneMojo() {
		List<Charset> availableCharsets = new ArrayList<Charset>();

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

	private static void addCharset(List<Charset> availableCharsets, String csName) {
		try {
			Charset cs = Charset.forName(csName);
			if (!availableCharsets.contains(cs)) {
				availableCharsets.add(cs);
			}
		} catch (Throwable _) {
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
	 * <p>
	 * It is an attribute defined as: <code><pre>
	 * <span>/</span>* <span>@</span>component
	 * <span>*</span>/
	 * private ArtifactHandlerManager manager;
	 * </pre></code>
	 * 
	 * @return the artifact resolver.
	 */
	public abstract ArtifactHandlerManager getArtifactHandlerManager();

	/**
	 * Replies the output directory of the project. Basically it is <code>getRootDirectory()+"/target"</code>.
	 * <p>
	 * It is an attribute defined as: <code><pre>
	 * <span>/</span>* <span>@</span>parameter expression="&dollar;{project.build.directory}"
	 * <span>*</span>/
	 * private File outputDirectory;
	 * </pre></code>
	 * 
	 * @return the output directory.
	 */
	public abstract File getOutputDirectory();

	/**
	 * Replies the root directory of the project. Basically it is the value stored inside the Maven property named <code>project.basedir</code>.
	 * <p>
	 * It is an attribute defined as: <code><pre>
	 * <span>/</span>* <span>@</span>parameter expression="${project.basedir}"
	 * <span>*</span>/
	 * private File baseDirectory;
	 * </pre></code>
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
	 * <p>
	 * It is an attribute defined as: <code><pre>
	 * <span>/</span>* <span>@</span>component role="org.apache.maven.project.MavenProjectBuilder"
	 * * <span>@</span>required
	 * * <span>@</span>readonly
	 * <span>*</span>/
	 * private MavenProjectBuilder projectBuilder;
	 * </pre></code>
	 * 
	 * @return the maven session
	 */
	public abstract MavenProjectBuilder getMavenProjectBuilder();

	/**
	 * Replies the current project builder. Basically it is an internal component of Maven.
	 * <p>
	 * It is an attribute defined as: <code><pre>
	 * <span>/</span>* <span>@</span>parameter expression="&dollar;{session}"
	 * * <span>@</span>required
	 * <span>*</span>/
	 * private MavenSession mvnSession;
	 * </pre></code>
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
		String filename = removePathPrefix(getBaseDirectory(), file);

		getLog().debug("Retreiving module for " + filename); //$NON-NLS-1$

		File theFile = file;
		File pomDirectory = null;
		while (theFile != null && pomDirectory == null) {
			if (theFile.isDirectory()) {
				File pomFile = new File(theFile, "pom.xml"); //$NON-NLS-1$
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

		BuildContext buildContext = getBuildContext();
		buildContext.addMessage(file,
				1, 1,
				"The maven module for this file cannot be retreived.", //$NON-NLS-1$
				BuildContext.SEVERITY_WARNING, null);
		return null;
	}

	/**
	 * Replies the project's remote repositories to use for the resolution of plugins and their dependencies..
	 * <p>
	 * It is an attribute defined as: <code><pre>
	 * <span>/</span>* <span>@</span>parameter default-value="&dollar;{project.remoteProjectRepositories}"
	 * <span>*</span>/
	 * private List<RemoteRepository> remoteRepos;
	 * </pre></code>
	 * 
	 * @return the repository system
	 */
	public abstract  List<?> getRemoteRepositoryList();

	/**
	 * Replies the repository system used by this maven instance. Basically it is an internal component of Maven.
	 * <p>
	 * It is an attribute defined as: <code><pre>
	 * <span>/</span>* <span>@</span>component
	 * <span>*</span>/
	 * private RepositorySystem repoSystem;
	 * </pre></code>
	 * 
	 * @return the repository system
	 */
	public abstract RepositorySystem getRepositorySystem();

	/**
	 * Replies the current repository/network configuration of Maven..
	 * <p>
	 * It is an attribute defined as: <code><pre>
	 * <span>/</span>* <span>@</span>parameter default-value="&dollar;{repositorySystemSession}"
	 * <span>@</span>readonly
	 * <span>*</span>/
	 * private RepositorySystemSession repoSession;
	 * </pre></code>
	 * 
	 * @return the repository system
	 */
	public abstract Object getRepositorySystemSession();

	/**
	 * Retreive the extended artifact definition of the given artifact.
	 * @param mavenArtifact - the artifact to resolve
	 * @return the artifact definition.
	 * @throws MojoExecutionException
	 */
	public final Artifact resolveArtifact(Artifact mavenArtifact) throws MojoExecutionException {
		return AetherBridge.resolveArtifact(
				mavenArtifact,
				getRemoteRepositoryList(),
				getRepositorySystem(),
				getRepositorySystemSession());
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
	 * @throws MojoExecutionException 
	 */
	public final Artifact resolveArtifact(String groupId, String artifactId, String version) throws MojoExecutionException {
		return AetherBridge.resolveArtifact(
				groupId, artifactId, version,
				getRemoteRepositoryList(),
				getRepositorySystem(),
				getRepositorySystemSession());
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
		Collection<File> files = new ArrayList<File>();
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
			List<File> candidates = new ArrayList<File>();

			String relativePath = removePathPrefix(getBaseDirectory(), directory);

			getLog().debug("Retreiving " //$NON-NLS-1$
					+ filter.toString() + " files from " //$NON-NLS-1$
					+ relativePath);

			candidates.add(directory);
			int nbFiles = 0;

			while (!candidates.isEmpty()) {
				candidate = candidates.remove(0);
				if (candidate.isDirectory()) {
					File[] children = candidate.listFiles(filter);
					if (children != null) {
						for (File child : children) {
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
	 * Replies a map of files which are found on the file system. The map has the found files as keys and the search directory as values.
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
	 * Replies a map of files which are found on the file system. The map has the found files as keys and the search directory as values.
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
			List<File> candidates = new ArrayList<File>();

			String relativePath = removePathPrefix(getBaseDirectory(), directory);

			getLog().debug("Retreiving " //$NON-NLS-1$
					+ filter.toString() + " files from " //$NON-NLS-1$
					+ relativePath);

			candidates.add(directory);
			int nbFiles = 0;

			while (!candidates.isEmpty()) {
				candidate = candidates.remove(0);
				if (candidate.isDirectory()) {
					File[] children = candidate.listFiles();
					if (children != null) {
						for (File child : children) {
							if (child != null && child.isDirectory()) {
								candidates.add(child);
							}
							else if (filter.accept(child)) {
								fileOut.put(child, directory);
								++nbFiles;
							}
							else if (listener!=null) {
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
	public synchronized final ExtendedArtifact readPom(File pomDirectory) {
		return readPomFile(new File(pomDirectory, "pom.xml")); //$NON-NLS-1$
	}

	/**
	 * Replies the maven artifact which is described by the given <code>pom.xml</code>.
	 * 
	 * @param pomFile
	 *            is the <code>pom.xml</code> file.
	 * @return the artifact or <code>null</code>.
	 */
	public synchronized final ExtendedArtifact readPomFile(File pomFile) {
		String groupId = null;
		String artifactId = null;
		String name = null;
		String version = null;
		String url = null;
		Organization organization = null;
		Scm scm = null;
		List<Developer> developers;
		List<Contributor> contributors;
		List<License> licenses;
		Parent parent = null;

		getLog().debug("Read pom file: " + pomFile.toString()); //$NON-NLS-1$

		if (!pomFile.canRead())
			return null;

		MavenXpp3Reader pomReader = new MavenXpp3Reader();
		try {
			FileReader fr = new FileReader(pomFile);
			try {
				Model model = pomReader.read(fr);
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
			}
			catch (IOException e) {
				return null;
			}
			catch (XmlPullParserException e) {
				return null;
			}
			finally {
				fr.close();
			}
		}
		catch (IOException e) {
			return null;
		}

		if (developers == null) {
			developers = new ArrayList<Developer>();
		} else {
			List<Developer> list = new ArrayList<Developer>();
			list.addAll(developers);
			developers = list;
		}
		if (contributors == null) {
			contributors = new ArrayList<Contributor>();
		} else {
			List<Contributor> list = new ArrayList<Contributor>();
			list.addAll(contributors);
			contributors = list;
		}
		if (licenses == null) {
			licenses = new ArrayList<License>();
		} else {
			List<License> list = new ArrayList<License>();
			list.addAll(licenses);
			licenses = list;
		}

		if (parent != null) {
			String relPath = parent.getRelativePath();
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
						String key = ArtifactUtils.key(parent.getGroupId(), parent.getArtifactId(), parent.getVersion());
						Artifact artifact = createArtifact(parent.getGroupId(), parent.getArtifactId(), parent.getVersion());
						ArtifactRepository repo = getMavenSession().getLocalRepository();
						String artifactPath = repo.pathOf(artifact);
						artifactPath = artifactPath.replaceFirst("\\.jar$", ".pom"); //$NON-NLS-1$ //$NON-NLS-2$
						File artifactFile = new File(repo.getBasedir(), artifactPath);
						getLog().debug("Getting pom file in local repository for " //$NON-NLS-1$
								+ key + ": " + artifactFile.getAbsolutePath()); //$NON-NLS-1$
						BuildContext buildContext = getBuildContext();
						buildContext.removeMessages(pomFile);
						if (artifactFile.canRead()) {
							parentArtifact = readPomFile(artifactFile);
							if (parentArtifact != null) {
								this.remoteArtifactDescriptions.put(key, parentArtifact);
								getLog().debug("Add remote module description for " //$NON-NLS-1$
										+ parentArtifact.toString());
							}
							else {
								buildContext.addMessage(
										pomFile,
										1, 1,
										"Unable to retreive the pom file of " + key, //$NON-NLS-1$
										BuildContext.SEVERITY_WARNING, null);
							}
						}
						else {
							buildContext.addMessage(
									pomFile,
									1, 1,
									"Cannot read the file for '" + key + "': " + artifactFile.getAbsolutePath(), //$NON-NLS-1$ //$NON-NLS-2$
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
			if (version==null || version.isEmpty()) {
				version = parent.getVersion();
			}

			if (groupId==null || groupId.isEmpty()) {
				groupId = parent.getGroupId();
			}
		}

		String scmRevision = null;

		try {
			SVNClientManager svnManager = getSVNClientManager();
			SVNInfo svnInfo = svnManager.getWCClient().doInfo(pomFile.getParentFile(), SVNRevision.UNDEFINED);
			if (svnInfo != null) {
				SVNRevision revision = svnInfo.getRevision();
				if (revision != null) {
					scmRevision = Long.toString(revision.getNumber());
				}
			}
		} catch (SVNException _) {
			//
		}

		Artifact a = createArtifact(groupId, artifactId, version);
		return new ExtendedArtifact(a, name, url, organization, scmRevision, scm, developers, contributors, licenses);
	}



	/**
	 * Create an Jar runtime artifact from the given values.
	 * 
	 * @param groupId
	 * @param artifactId
	 * @param version
	 * @return the artifact
	 */
	public final Artifact createArtifact(String groupId, String artifactId, String version) {
		return createArtifact(groupId, artifactId, version, "runtime", "jar"); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Create an artifact from the given values.
	 * 
	 * @param groupId
	 * @param artifactId
	 * @param version
	 * @param scope
	 * @param type
	 * @return the artifact
	 * @see DefaultArtifactFactory
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

		ArtifactHandler handler = getArtifactHandlerManager().getArtifactHandler(type);

		return new org.apache.maven.artifact.DefaultArtifact(
				groupId, artifactId, versionRange, 
				desiredScope, type, null, // classifier
				handler, false); // optional
	}

	/**
	 * Check if the values of the attributes of this Mojo are correctly set. This function may be overridden by subclasses to test subclasse's attributes.
	 * 
	 * @throws MojoExecutionException
	 */
	protected abstract void checkMojoAttributes() throws MojoExecutionException;

	private static String getLogType(Object o) {
		if (o instanceof Boolean || o instanceof AtomicBoolean)
			return "B"; //$NON-NLS-1$
		if (o instanceof Byte)
			return "b"; //$NON-NLS-1$
		if (o instanceof Short)
			return "s"; //$NON-NLS-1$
		if (o instanceof Integer || o instanceof AtomicInteger)
			return "i"; //$NON-NLS-1$
		if (o instanceof Long || o instanceof AtomicLong)
			return "l"; //$NON-NLS-1$
		if (o instanceof Float)
			return "f"; //$NON-NLS-1$
		if (o instanceof Double)
			return "d"; //$NON-NLS-1$
		if (o instanceof BigDecimal)
			return "D"; //$NON-NLS-1$
		if (o instanceof BigInteger)
			return "I"; //$NON-NLS-1$
		if (o instanceof CharSequence)
			return "s"; //$NON-NLS-1$
		if (o instanceof Array) {
			Array a = (Array) o;
			return a.getClass().getComponentType().getName() + "[]"; //$NON-NLS-1$
		}
		if (o instanceof Set<?>)
			return "set"; //$NON-NLS-1$
		if (o instanceof Map<?, ?>)
			return "map"; //$NON-NLS-1$
		if (o instanceof List<?>)
			return "list"; //$NON-NLS-1$
		if (o instanceof Collection<?>)
			return "col"; //$NON-NLS-1$
		return "o"; //$NON-NLS-1$
	}

	/**
	 * Throw an exception when the given object is null.
	 * 
	 * @param message
	 *            is the message to put in the exception.
	 * @param o
	 */
	protected final void assertNotNull(String message, Object o) {
		if (getLog().isDebugEnabled()) {
			getLog().debug(
					"\t(" //$NON-NLS-1$
					+	getLogType(o)
					+	") " //$NON-NLS-1$
					+	message
					+	" = " //$NON-NLS-1$
					+	o);
		}
		if (o == null)
			throw new AssertionError("assertNotNull: " + message); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
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
	 * @throws MojoExecutionException
	 */
	protected abstract void executeMojo() throws MojoExecutionException;

	/**
	 * Join the values with the given joint
	 * 
	 * @param joint
	 * @param values
	 * @return the jointed values
	 */
	public static String join(String joint, String... values) {
		StringBuilder b = new StringBuilder();
		for (String value : values) {
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
		FileInputStream fis = new FileInputStream(file);
		ReadableByteChannel channel = Channels.newChannel(fis);
		Reader reader = Channels.newReader(channel, decoder, -1);
		BufferedReader bReader = new BufferedReader(reader);
		try {
			String line = bReader.readLine();
			while (line != null) {
				line = bReader.readLine();
			}
		}
		finally {
			bReader.close();
			reader.close();
			channel.close();
			fis.close();
		}
	}

	/**
	 * Try to detect and reply the encoding of the given file. This function uses the charsets replied by {@link #getPreferredCharsets()} to select a charset when many are possible.
	 * 
	 * @param file
	 *            is the file to read.
	 * @return the encoding charset of the given file or <code>null</code> if the encoding could not be detected.
	 * @see #getPreferredCharsets()
	 * @see #setPreferredCharsets(Charset...)
	 */
	public final Charset detectEncoding(File file) {
		Collection<Charset> fittingCharsets = new TreeSet<Charset>();
		for (Charset c : Charset.availableCharsets().values()) {
			CharsetDecoder decoder = c.newDecoder();
			try {
				detectEncoding(file, decoder);
				fittingCharsets.add(c);
			} catch (Throwable e) {
				//
			}
		}
		if (getLog().isDebugEnabled()) {
			getLog().debug("Valid charsets for " + file.getName() + ":\n" + fittingCharsets.toString()); //$NON-NLS-1$ //$NON-NLS-2$
		}

		for (Charset prefCharset : getPreferredCharsets()) {
			if (prefCharset.canEncode() && fittingCharsets.contains(prefCharset)) {
				getLog().debug("Use preferred charset for " + file.getName() + ": " + prefCharset.displayName()); //$NON-NLS-1$ //$NON-NLS-2$
				return prefCharset;
			}
		}

		Charset platformCharset = Charset.defaultCharset();

		if (platformCharset.canEncode() && fittingCharsets.contains(platformCharset)) {
			getLog().debug("Use platform default charset for " + file.getName() + ": " + platformCharset.displayName()); //$NON-NLS-1$ //$NON-NLS-2$
			return Charset.defaultCharset();
		}

		Iterator<Charset> iterator = fittingCharsets.iterator();
		while (iterator.hasNext()) {
			Charset c = iterator.next();
			if (c.canEncode()) {
				getLog().debug("Use first valid charset for " + file.getName() + ": " + c.displayName()); //$NON-NLS-1$ //$NON-NLS-2$
				return c;
			}
		}

		return null;
	}

	/** Replies the dependencies specified in the the Maven configuration
	 * of the current project.
	 * 
	 * @param isTransitive indicates if the dependencies of dependencies
	 * must also be replied by the iterator.
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
	 * must also be replied by the iterator.
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
	 * must also be replied by the iterator.
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
	 * must also be replied by the iterator.
	 * @return the iterator.
	 * @see #getPlugins(boolean)
	 */
	public final Iterator<Plugin> getPlugins(MavenProject project, boolean isTransitive) {
		return new PluginIterator(project, isTransitive);
	}

	/** Load the Maven project for the given artifact.
	 * 
	 * @param artifact
	 * @return the maven project.
	 */
	public MavenProject getMavenProject(Artifact artifact) {
		try {
			MavenSession session = getMavenSession();
			MavenProject current = session.getCurrentProject();
			MavenProject prj = getMavenProjectBuilder().buildFromRepository(
					artifact,
					current.getRemoteArtifactRepositories(),
					session.getLocalRepository());
			return prj;
		}
		catch (ProjectBuildingException e) {
			getLog().warn(e);
		}
		return null;
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class DependencyIterator implements Iterator<MavenProject> {

		private final List<ArtifactRepository> remoteRepositiories;
		private final boolean isTransitive;
		private final File projectFile;
		private List<Dependency> dependencies = new ArrayList<Dependency>();
		private Set<String> treated = new TreeSet<String>();
		private MavenProject next;

		/**
		 * @param project is the project for which the dependencies must
		 * be replied.
		 * @param isTransitive indicates if the dependencies of dependencies must also be replied
		 * by the iterator.
		 */
		public DependencyIterator(MavenProject project, boolean isTransitive) {
			this.isTransitive = isTransitive;
			this.remoteRepositiories = project.getRemoteArtifactRepositories();
			this.dependencies.addAll(project.getDependencies());
			this.projectFile = project.getFile();
			getBuildContext().removeMessages(this.projectFile);
			searchNext();
		}

		private void searchNext() {
			this.next = null;

			while (this.next==null && !this.dependencies.isEmpty()) {
				Dependency dependency = this.dependencies.remove(0);
				if (dependency!=null) {
					String artifactId = dependency.getGroupId()+":"+dependency.getArtifactId()+":"+dependency.getVersion();  //$NON-NLS-1$//$NON-NLS-2$
					if (!this.treated.contains(artifactId)) {
						boolean isTreated = false;
						try {
							Artifact dependencyArtifact = createArtifact(
									dependency.getGroupId(),
									dependency.getArtifactId(),
									dependency.getVersion(),
									dependency.getScope(),
									dependency.getType());
							resolveArtifact(dependencyArtifact);
							MavenProjectBuilder builder = getMavenProjectBuilder();
							MavenProject dependencyProject = builder.buildFromRepository(
									dependencyArtifact,
									this.remoteRepositiories,
									getMavenSession().getLocalRepository());
							if (dependencyProject!=null) {
								if (this.isTransitive) {
									this.dependencies.addAll(dependencyProject.getDependencies());
								}
								this.next = dependencyProject;
								isTreated = true;
							}
						}
						catch (MojoExecutionException e) {
							getBuildContext().addMessage(
									this.projectFile,
									1,1,
									"Unable to retreive the Maven plugin: "+artifactId, //$NON-NLS-1$
									BuildContext.SEVERITY_WARNING,
									e);
							isTreated = true;
						}
						catch (ProjectBuildingException e) {
							getBuildContext().addMessage(
									this.projectFile,
									1,1,
									"Unable to retreive the Maven plugin: "+artifactId, //$NON-NLS-1$
									BuildContext.SEVERITY_WARNING,
									e);
							isTreated = true;
						}
						if (isTreated) this.treated.add(artifactId);
					}
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public MavenProject next() {
			MavenProject n = this.next;
			if (n==null) throw new NoSuchElementException();
			searchNext();
			return n;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	} // class DependencyIterator

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class PluginIterator implements Iterator<Plugin> {

		private final Iterator<MavenProject> dependencyIterator;
		private Iterator<org.apache.maven.model.Plugin> pluginIterator;
		private Plugin next;

		/**
		 * @param project
		 * @param isTransitive
		 */
		public PluginIterator(MavenProject project, boolean isTransitive) {
			this.dependencyIterator = getDependencies(project, isTransitive);
			searchNext();
		}

		private void searchNext() {
			this.next = null;
			while (this.next==null) {
				if (this.pluginIterator!=null && this.pluginIterator.hasNext()) {
					this.next = this.pluginIterator.next();
				}
				else if (this.dependencyIterator.hasNext()) {
					MavenProject project = this.dependencyIterator.next();
					List<Plugin> buildPlugins = project.getBuildPlugins();
					if (buildPlugins!=null) {
						this.pluginIterator = buildPlugins.iterator();
					}
				}
				else {
					return;
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Plugin next() {
			org.apache.maven.model.Plugin n = this.next;
			if (n==null) throw new NoSuchElementException();
			searchNext();
			return n;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	} // class PluginIterator

	/**
	 * Abstract implementation for all Arakhn&ecirc; maven modules. This implementation is thread safe.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * 
	 * @component
	 */
	public static interface FindFileListener extends EventListener {

		/** Invoked when a file which is not matching the file filter was found.
		 * 
		 * @param file is the file that is not matching the file filter.
		 * @param rootDirectory is the root directory in which the file was found. 
		 */
		public void findFile(File file, File rootDirectory);

	} // interface FindFileListener

}