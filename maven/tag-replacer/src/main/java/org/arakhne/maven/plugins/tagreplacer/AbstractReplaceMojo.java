/* 
 * $Id$
 * 
 * Copyright (C) 2010-12 Stephane GALLAND
 * 
 * This library is free software; you can redistribute it and/or
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
package org.arakhne.maven.plugins.tagreplacer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Organization;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.repository.RepositorySystem;
import org.arakhne.maven.AbstractArakhneMojo;
import org.arakhne.maven.ExtendedArtifact;
import org.sonatype.plexus.build.incremental.BuildContext;

/**
 * Replace all the tags variables by the corresponding values. Supported variables are:
 * <table>
 * <thead>
 * <tr>
 * <th>Name (case-sensitive)</th>
 * <th>Description</th>
 * </tr>
 * </thead> <tbody>
 * <tr>
 * <td>&dollar;ArtifactId&dollar;</td>
 * <td>The artifact id of the Maven module</td>
 * </tr>
 * <tr>
 * <td>&dollar;Author: id&dollar;</td>
 * <td>The name and link to the author with the given id. The id is the identifier of the author or contributor defined the <code>pom.xml</code> file; or it is the email address of the author.</td>
 * </tr>
 * <tr>
 * <td>&dollar;Date&dollar;</td>
 * <td>The date of the last compilation of the Maven module</td>
 * </tr>
 * <tr>
 * <td>&dollar;Filename&dollar;</td>
 * <td>The name of file</td>
 * </tr>
 * <tr>
 * <td>&dollar;FullVersion&dollar;</td>
 * <td>The name, version, revision and date of the Maven module. It is equivalent to<br>
 * <code>&dollar;Version&dollar;&nbsp;(rev:&dollar;Revision&dollar;)&nbsp;-&nbsp;&dollar;Date&dollar;</code></td>
 * </tr>
 * <tr>
 * <td>&dollar;GroupId&dollar;</td>
 * <td>The group id of the Maven module</td>
 * </tr>
 * <tr>
 * <td>&dollar;Id&dollar;</td>
 * <td>The id of file</td>
 * </tr>
 * <tr>
 * <td>&dollar;InceptionYear&dollar;</td>
 * <td>The inception year of the maven module, or the current year if the inception year was not defined</td>
 * </tr>
 * <tr>
 * <td>&dollar;Name&dollar;</td>
 * <td>The name of the Maven module</td>
 * </tr>
 * <tr>
 * <td>&dollar;Organization&dollar;</td>
 * <td>The name of organization that publishs the Maven module</td>
 * </tr>
 * <tr>
 * <td>&dollar;Prop: name&dollar;</td>
 * <td>The value of the property with the specified name and that is defined in the pom.xml of the maven module</td>
 * </tr>
 * <tr>
 * <td>&dollar;Revision&dollar;</td>
 * <td>The SCM/SVN revision number of the Maven module</td>
 * </tr>
 * <tr>
 * <td>&dollar;Version&dollar;</td>
 * <td>The version of the Maven module</td>
 * </tr>
 * <tr>
 * <td>&dollar;Website&dollar;</td>
 * <td>The link to the website of the Maven module</td>
 * </tr>
 * <tr>
 * <td>&dollar;Year&dollar;</td>
 * <td>The current year</td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractReplaceMojo extends AbstractArakhneMojo implements Macros {

	/**
	 * @component
	 */
	private ArtifactHandlerManager artifactHandlerManager;

	/**
	 * @component role="org.apache.maven.project.MavenProjectBuilder"
	 * @required
	 * @readonly
	 */
	private MavenProjectBuilder mavenProjectBuilder;
	
	/**
	 * @parameter property="project.basedir"
	 */
	private File baseDirectory;

	/**
	 * @parameter property="project.build.directory"
	 */
	private File outputDirectory;

	/**
	 * @parameter property="project.build.outputDirectory"
	 */
	private File classDirectory;

	/**
	 * @parameter default-value="${project.basedir}/src"
	 */
	private File sourceDirectory;

	/**
	 * @parameter default-value="${project.build.directory}/generated-sources"
	 */
	private File generatedSourceDirectory;

	/**
	 * @parameter default-value="${project.build.directory}/site"
	 * @since 2.3
	 */
	private File siteDirectory;

	/**
	 * Is the artifact id that should replace the project's artifact id.
	 * 
	 * @parameter default-value="${project.artifactId}"
	 */
	private String projectArtifactId;

	/**
	 * Is the group id that should replace the project's group id.
	 * 
	 * @parameter default-value="${project.groupId}"
	 */
	private String projectGroupId;

	/**
	 * Is the file encoding.
	 * 
	 * @parameter default-value="${project.build.sourceEncoding}"
	 */
	private String encoding;

	/**
	 * Indicates if the group id and artifact id of the current project should be replaced by <var>userArtifactId</var> and <var>artifactId</var>
	 * 
	 * @parameter default-value="true"
	 */
	private boolean overrideArtifactGroup;

	/**
	 * Reference to the current maven project.
	 * 
	 * @parameter property="project"
	 * @required
	 */
	private MavenProject mavenProject;

	/**
	 * Reference to the current session.
	 * 
	 * @parameter property="session"
	 * @required
	 */
	private MavenSession mavenSession;

	/** The context of building, compatible with M2E and CLI.
	 * @component
	 */
	private BuildContext buildContext;

	/**
	 * The entry point to Aether, i.e. the component doing all the work.
	 * 
	 * @component
	 */
	private RepositorySystem repoSystem;

	/**
	 * The current repository/network configuration of Maven.
	 * 
	 * @parameter default-value="${repositorySystemSession}"
	 * @readonly
	 */
	private Object repoSession;

	/**
	 * The directory where Maven may found the sources. 
	 * 
	 * @parameter default-value="${project.basedir}/src/main/java"
	 */
	private File javaSourceRoot;

	/**
	 * The project's remote repositories to use for the resolution of plugins and their dependencies.
	 * 
	 * @parameter default-value="${project.remoteProjectRepositories}"
	 * @readonly
	 */
	private List<?> remoteRepos;

	private final Set<File> replacementTreatedFiles = new TreeSet<File>();
	
	private String ensureArtifactId(ExtendedArtifact artifact) {
		if (artifact != null) {
			if (this.overrideArtifactGroup && this.mavenProject.getArtifact().equals(artifact)) {
				String a = this.projectArtifactId;
				if (a != null && !EMPTY_STRING.equals(a)) {
					return this.projectArtifactId;
				}
			}
			return artifact.getArtifactId();
		}
		return EMPTY_STRING;
	}

	private String ensureGroupId(ExtendedArtifact artifact) {
		if (artifact != null) {
			if (this.overrideArtifactGroup && this.mavenProject.getArtifact().equals(artifact)) {
				String g = this.projectGroupId;
				if (g != null && !EMPTY_STRING.equals(g)) {
					return this.projectGroupId;
				}
			}
			return artifact.getGroupId();
		}
		return EMPTY_STRING;
	}

	/**
	 * Replace the Javadoc tags in the given file only if the file was never
	 * treated before.
	 * 
	 * @param sourceFile
	 *            is the name of the file to read out. It may be <code>null</code>
	 * @param targetFile
	 *            is the name of the file to write in. It cannot be <code>null</code>
	 * @param replacementType
	 *            is the type of replacement to be done.
	 * @param classpath
	 *            are the directories from which the file is extracted.
	 * @param detectEncoding
	 *            when <code>true</code> the encoding of the file will be detected and preserved. When <code>false</code> the encoding may be loose.
	 * @throws MojoExecutionException
	 * @see #replaceInFile(File, File, ReplacementType, File[], boolean)
	 */
	protected synchronized void replaceInFileBuffered(File sourceFile, File targetFile, ReplacementType replacementType, File[] classpath, boolean detectEncoding) throws MojoExecutionException {
		if (this.replacementTreatedFiles.contains(targetFile)
			&& targetFile.exists()) {
			getLog().debug("Skiping "+targetFile+" because is was already treated for replacements");  //$NON-NLS-1$//$NON-NLS-2$
			return;
		}
		replaceInFile(sourceFile, targetFile, replacementType, classpath, detectEncoding);
	}

	/**
	 * Replace the Javadoc tags in the given file.
	 * 
	 * @param sourceFile
	 *            is the name of the file to read out. It may be <code>null</code>
	 * @param targetFile
	 *            is the name of the file to write in. It cannot be <code>null</code>
	 * @param replacementType
	 *            is the type of replacement to be done.
	 * @param classpath
	 *            are the directories from which the file is extracted.
	 * @param detectEncoding
	 *            when <code>true</code> the encoding of the file will be detected and preserved. When <code>false</code> the encoding may be loose.
	 * @throws MojoExecutionException
	 * @see #replaceInFileBuffered(File, File, ReplacementType, File[], boolean)
	 */
	protected synchronized void replaceInFile(File sourceFile, File targetFile, ReplacementType replacementType, File[] classpath, boolean detectEncoding) throws MojoExecutionException {
		File outputFile, inputFile;
		assert (targetFile != null);

		if (sourceFile == null) {
			inputFile = targetFile;
			outputFile = new File(targetFile.getAbsolutePath() + ".maven.tmp"); //$NON-NLS-1$
		} else {
			inputFile = sourceFile;
			outputFile = targetFile;
		}

		BuildContext buildContext = getBuildContext();
		buildContext.removeMessages(sourceFile);
		buildContext.removeMessages(targetFile);
		
		ExtendedArtifact artifact = searchArtifact(inputFile);

		String filename = removePathPrefix(getBaseDirectory(), inputFile);

		String shortFilename = null;
		for (int i = 0; shortFilename == null && i < classpath.length; ++i) {
			if (inputFile.getAbsolutePath().startsWith(classpath[i].getAbsolutePath())) {
				shortFilename = removePathPrefix(classpath[i], inputFile);
			}
		}

		if (artifact != null) {
			getLog().debug("Replacing in " + filename + " with artifact " + //$NON-NLS-1$ //$NON-NLS-2$
					ArtifactUtils.key(artifact));
		} else {
			getLog().debug("Replacing in " + filename + " without artifact"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		try {
			outputFile.getParentFile().mkdirs();
			Reader r = null;

			Charset charset = null;

			if (detectEncoding) {
				charset = detectEncoding(inputFile);
			}

			if (charset == null)
				charset = Charset.defaultCharset();

			getLog().debug("Copying file '" //$NON-NLS-1$
					+ inputFile.getName() + "' with '" //$NON-NLS-1$
					+ charset.displayName() + "' encoding"); //$NON-NLS-1$

			FileInputStream fis = new FileInputStream(inputFile);
			ReadableByteChannel channel = Channels.newChannel(fis);
			try {
				r = Channels.newReader(channel, charset.newDecoder(), -1);

				if (r == null) {
					r = new FileReader(inputFile);
				}

				BufferedReader br = new BufferedReader(r);
				FileOutputStream fos = new FileOutputStream(outputFile);
				WritableByteChannel wChannel = Channels.newChannel(fos);
				Writer w = Channels.newWriter(wChannel, charset.newEncoder(), -1);
				try {
					String line;
					int nLine = 1;
		
					while ((line = br.readLine()) != null) {
						line = replaceInString(inputFile, nLine, shortFilename, artifact, line, replacementType);
						w.write(line);
						w.write("\n"); //$NON-NLS-1$
						++nLine;
					}
					w.flush();
				}
				finally {
					w.close();
					wChannel.close();
					fos.close();
					br.close();
				}
			}
			finally {
				if (r!=null) r.close();
				channel.close();
				fis.close();
			}
			
			if (sourceFile == null) {
				fileCopy(outputFile, targetFile);
				outputFile.delete();
				buildContext.refresh(outputFile.getParentFile());
			}
		} catch (IOException e) {
			throw new MojoExecutionException(e.getLocalizedMessage(), e);
		} finally {
			if (sourceFile == null && outputFile.exists()) {
				outputFile.delete();
				buildContext.refresh(outputFile.getParentFile());
			}
			else if (outputFile.exists()) {
				buildContext.refresh(outputFile);
			}
		}
	}

	private static Pattern buildMacroPattern(String macroName) {
		StringBuilder b = new StringBuilder();
		b.append(Pattern.quote("$")); //$NON-NLS-1$
		b.append(macroName);
		b.append("(?:"); //$NON-NLS-1$
		b.append(Pattern.quote(":")); //$NON-NLS-1$
		b.append("[^\\$]*)?"); //$NON-NLS-1$
		b.append(Pattern.quote("$")); //$NON-NLS-1$
		return Pattern.compile(b.toString(), Pattern.CASE_INSENSITIVE);
	}

	private static Pattern buildMacroPatternWithGroup(String macroName) {
		StringBuilder b = new StringBuilder();
		b.append(Pattern.quote("$")); //$NON-NLS-1$
		b.append(macroName);
		b.append("(?:"); //$NON-NLS-1$
		b.append(Pattern.quote(":")); //$NON-NLS-1$
		b.append("([^\\$]*))?"); //$NON-NLS-1$
		b.append(Pattern.quote("$")); //$NON-NLS-1$
		return Pattern.compile(b.toString(), Pattern.CASE_INSENSITIVE);
	}

	/**
	 * Utility function that replace the macros by the replacement text in the given text.
	 * 
	 * @param macroName
	 *            is the name of the macro to replace.
	 * @param text
	 *            is the text in which the replacement should occur
	 * @param replacement
	 *            is the replacement text.
	 * @param type
	 *            is the type of replacement to be done.
	 * @param sourceFile
	 *            is the filename in which the replacement is done.
	 * @param sourceLine
	 *            is the line at which the replacement is done. 
	 * @return the result of the replacement
	 */
	protected final String replaceMacro(String macroName, String text, String replacement, ReplacementType type, File sourceFile, int sourceLine) {
		return replaceMacro(macroName, text, replacement, type, false, sourceFile, sourceLine);
	}

	/**
	 * Utility function that replace the macros by the replacement text in the given text.
	 * 
	 * @param macroName
	 *            is the name of the macro to replace.
	 * @param text
	 *            is the text in which the replacement should occur
	 * @param replacement
	 *            is the replacement text.
	 * @param type
	 *            is the type of replacement to be done.
	 * @param enableWarning
	 *            indicates if the warnings should be output or not.
	 * @param sourceFile
	 *            is the filename in which the replacement is done.
	 * @param sourceLine
	 *            is the line at which the replacement is done. 
	 * @return the result of the replacement
	 */
	protected synchronized final String replaceMacro(String macroName, String text, String replacement, ReplacementType type, boolean enableWarning, File sourceFile, int sourceLine) {
		if (replacement != null && !EMPTY_STRING.equals(replacement)) {
			Pattern p = buildMacroPattern(macroName);
			Matcher m = p.matcher(text);
			return m.replaceAll(Matcher.quoteReplacement(replacement));
		}
		if (enableWarning) {
			getBuildContext().addMessage(
					sourceFile,
					sourceLine, 1,
					"cannot replace empty macro $" + macroName + "$", //$NON-NLS-1$ //$NON-NLS-2$
					BuildContext.SEVERITY_WARNING, null);
		}
		return text;
	}

	/**
	 * Replace the author information tags in the given text.
	 * 
	 * @param sourceFile
	 *            is the filename in which the replacement is done.
	 * @param sourceLine
	 *            is the line at which the replacement is done. 
	 * @param text
	 *            is the text in which the author tags should be replaced
	 * @param artifact
	 * @param replacementType
	 *            is the type of replacement.
	 * @return the result of the replacement.
	 * @throws MojoExecutionException
	 */
	protected synchronized String replaceAuthor(File sourceFile, int sourceLine, String text, ExtendedArtifact artifact, ReplacementType replacementType) throws MojoExecutionException {
		String result = text;
		Pattern p = buildMacroPatternWithGroup(MACRO_AUTHOR);
		Matcher m = p.matcher(text);
		boolean hasResult = m.find();

		if (hasResult) {
			StringBuffer sb = new StringBuffer();
			StringBuilder replacement = new StringBuilder();
			String login;
			URL url;
			Contributor contributor;
			do {
				login = m.group(1);
				if (login != null) {
					login = login.trim();
					if (login.length() > 0) {
						replacement.setLength(0);
						if (artifact != null) {
							contributor = artifact.getPeople(login, getLog());
							if (contributor != null) {
								url = getContributorURL(contributor, getLog());
								if (url == null) {
									replacement.append(contributor.getName());
								}
								else if (replacementType == ReplacementType.HTML) {
									replacement.append("<a target=\"_blank\" href=\""); //$NON-NLS-1$
									replacement.append(url.toExternalForm());
									replacement.append("\">"); //$NON-NLS-1$
									replacement.append(contributor.getName());
									replacement.append("</a>"); //$NON-NLS-1$
								}								
								else {
									replacement.append(contributor.getName());
									replacement.append(" ["); //$NON-NLS-1$
									replacement.append(url.toExternalForm());
									replacement.append("]"); //$NON-NLS-1$
								}
							}
							else {
								getBuildContext().addMessage(
										sourceFile,
										sourceLine, 1,
										"unable to find a developer or a contributor with an id, a name or an email equal to: " + login, //$NON-NLS-1$
										BuildContext.SEVERITY_WARNING, null);
							}
						}
						if (replacement.length() != 0) {
							m.appendReplacement(sb, Matcher.quoteReplacement(replacement.toString()));
						}
					}
					else {
						getBuildContext().addMessage(
								sourceFile,
								sourceLine, 1,
								"no login for Author tag: " + m.group(0), //$NON-NLS-1$
								BuildContext.SEVERITY_WARNING, null);
					}
				}
				else {
					getBuildContext().addMessage(
							sourceFile,
							sourceLine, 1,
							"no login for Author tag: " + m.group(0), //$NON-NLS-1$
							BuildContext.SEVERITY_WARNING, null);
				}
				hasResult = m.find();
			} while (hasResult);

			m.appendTail(sb);

			result = sb.toString();
		}
		return result;
	}

	/**
	 * Replace the property information tags in the given text.
	 * 
	 * @param sourceFile
	 *            is the filename in which the replacement is done.
	 * @param sourceLine
	 *            is the line at which the replacement is done. 
	 * @param text
	 *            is the text in which the author tags should be replaced
	 * @param project
	 * @param replacementType
	 *            is the type of replacement.
	 * @return the result of the replacement.
	 * @throws MojoExecutionException
	 */
	protected synchronized String replaceProp(File sourceFile, int sourceLine, String text, MavenProject project, ReplacementType replacementType) throws MojoExecutionException {
		String result = text;
		Pattern p = buildMacroPatternWithGroup(MACRO_PROP);
		Matcher m = p.matcher(text);
		boolean hasResult = m.find();
		
		Properties props = null;
		if (project != null) {
			props = project.getProperties();
		}

		if (hasResult) {
			StringBuffer sb = new StringBuffer();
			StringBuilder replacement = new StringBuilder();
			String propName;
			do {
				propName = m.group(1);
				if (propName != null) {
					propName = propName.trim();
					if (propName.length() > 0) {
						replacement.setLength(0);
						if (props!=null) {
							String value = props.getProperty(propName);
							if (value!=null && !value.isEmpty()) {
								replacement.append(value);
							}
						}
						if (replacement.length() != 0) {
							m.appendReplacement(sb, Matcher.quoteReplacement(replacement.toString()));
						}
					}
					else {
						getBuildContext().addMessage(
								sourceFile,
								sourceLine, 1,
								"no property name for Prop tag: " + m.group(0), //$NON-NLS-1$
								BuildContext.SEVERITY_WARNING, null);
					}
				}
				else {
					getBuildContext().addMessage(
							sourceFile,
							sourceLine, 1,
							"no property name for Prop tag: " + m.group(0), //$NON-NLS-1$
							BuildContext.SEVERITY_WARNING, null);
				}
				hasResult = m.find();
			}
			while (hasResult);

			m.appendTail(sb);

			result = sb.toString();
		}
		return result;
	}

	/**
	 * Replace Javadoc tags in a string.
	 * 
	 * @param sourceFile
	 *            is the filename in which the replacement is done.
	 * @param sourceLine
	 *            is the line at which the replacement is done. 
	 * @param file
	 *            is the name of the file in the hierarchy from which the string was extracted.
	 * @param artifact
	 *            is the artifact in which the file is located. If <code>null</code> the tags dedicated to the artifact will be replaced by the empty string.
	 * @param line
	 *            is the line in which the tags should be replaced.
	 * @param replacementType
	 *            is the type of replacement to be done.
	 * @return the result of the replacement.
	 * @throws MojoExecutionException
	 */
	protected synchronized String replaceInString(File sourceFile, int sourceLine, String file, ExtendedArtifact artifact, String line, ReplacementType replacementType) throws MojoExecutionException {
		// No possibility of replacement
		if (!line.contains("$")) return line; //$NON-NLS-1$
		
		String nline = line;

		String replacementName = (artifact == null) ? null : artifact.getName();
		String replacementVersion = (artifact == null) ? null : artifact.getVersion();
		String replacementRevision = (artifact == null) ? null : artifact.getScmRevision();
		String replacementArtifactId = ensureArtifactId(artifact);
		String replacementGroupId = ensureGroupId(artifact);
		String replacementWebsite = (artifact == null) ? null : artifact.getWebsite();
		String replacementFilename = (artifact == null) ? null : file;

		String replacementOrganization = null;

		if (artifact != null) {
			Organization orga = artifact.getOrganization();
			if (orga != null) {
				replacementOrganization = orga.getName();
			}
		}

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		String currentDate = fmt.format(this.invocationDate);
		
		fmt = new SimpleDateFormat("yyyy"); //$NON-NLS-1$
		String year = fmt.format(this.invocationDate);
		
		String inceptionYear = null;
		MavenProject project = getMavenProject(artifact);
		if (project!=null) {
			inceptionYear = project.getInceptionYear();
		}
		if (inceptionYear==null || inceptionYear.isEmpty()) {
			inceptionYear = year;
		}
		
		String replacementFullVersion;
		if (artifact == null) {
			replacementFullVersion = null;
		} else {
			String rev = null;
			if (replacementRevision != null) {
				rev = "(rev:" + //$NON-NLS-1$
						replacementRevision +
						")"; //$NON-NLS-1$
			}
			replacementFullVersion = join(" ", //$NON-NLS-1$
					replacementVersion, rev, currentDate);
		}

		nline = replaceMacro(MACRO_NAME, nline, replacementName, replacementType, sourceFile, sourceLine);
		nline = replaceMacro(MACRO_REVISION, nline, replacementRevision, replacementType, sourceFile, sourceLine);
		nline = replaceMacro(MACRO_VERSION, nline, replacementVersion, replacementType, sourceFile, sourceLine);
		nline = replaceMacro(MACRO_ARTIFACTID, nline, replacementArtifactId, replacementType, sourceFile, sourceLine);
		nline = replaceMacro(MACRO_GROUPID, nline, replacementGroupId, replacementType, sourceFile, sourceLine);
		nline = replaceMacro(MACRO_WEBSITE, nline, replacementWebsite, replacementType, sourceFile, sourceLine);
		nline = replaceMacro(MACRO_ORGANIZATION, nline, replacementOrganization, replacementType, sourceFile, sourceLine);
		nline = replaceMacro(MACRO_DATE, nline, currentDate, replacementType, sourceFile, sourceLine);
		nline = replaceMacro(MACRO_YEAR, nline, year, replacementType, sourceFile, sourceLine);
		nline = replaceMacro(MACRO_INCEPTIONYEAR, nline, inceptionYear, replacementType, sourceFile, sourceLine);
		nline = replaceMacro(MACRO_FULLVERSION, nline, replacementFullVersion, replacementType, sourceFile, sourceLine);
		nline = replaceMacro(MACRO_FILENAME, nline, replacementFilename, replacementType, sourceFile, sourceLine);

		StringBuilder buffer = new StringBuilder();
		buffer.setLength(0);
		// Split the string int "$" and "Id" to avoid the plugin to replace the tag in its own source fole
		buffer.append("$"+"Id: "); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append(file);
		buffer.append(' ');
		if (replacementRevision != null) {
			buffer.append("rev:"); //$NON-NLS-1$
			buffer.append(replacementRevision);
			buffer.append(' ');
		}
		if (replacementVersion != null) {
			buffer.append('v');
			buffer.append(replacementVersion);
			buffer.append(' ');
		}
		buffer.append(currentDate);
		buffer.append("$"); //$NON-NLS-1$
		nline = replaceMacro(MACRO_ID, nline, buffer.toString(), ReplacementType.TEXT, sourceFile, sourceLine);

		nline = replaceAuthor(sourceFile, sourceLine, nline, artifact, replacementType);
		nline = replaceProp(sourceFile, sourceLine, nline, project, replacementType);

		return nline;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void checkMojoAttributes() throws MojoExecutionException {
		assertNotNull("outputDirectory", this.outputDirectory); //$NON-NLS-1$
		assertNotNull("sourceDirectory", this.sourceDirectory); //$NON-NLS-1$
		assertNotNull("generatedSourceDirectory", this.generatedSourceDirectory); //$NON-NLS-1$
		assertNotNull("artifactHandlerManager", this.artifactHandlerManager); //$NON-NLS-1$
		assertNotNull("mavenProject", this.mavenProject); //$NON-NLS-1$
		assertNotNull("mavenSession", this.mavenSession); //$NON-NLS-1$
		assertNotNull("repositorySystem", this.repoSystem); //$NON-NLS-1$
		assertNotNull("repositorySystemSession", this.repoSession); //$NON-NLS-1$
		assertNotNull("remoteRepositoryList", this.remoteRepos); //$NON-NLS-1$
		assertNotNull("artifactId", this.projectArtifactId); //$NON-NLS-1$
		assertNotNull("groupId", this.projectGroupId); //$NON-NLS-1$
		assertNotNull("buildContext", this.buildContext); //$NON-NLS-1$
		if (this.encoding == null)
			this.encoding = System.getProperty("file.encoding"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MavenProjectBuilder getMavenProjectBuilder() {
		return this.mavenProjectBuilder;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BuildContext getBuildContext() {
		return this.buildContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MavenSession getMavenSession() {
		return this.mavenSession;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getBaseDirectory() {
		return this.baseDirectory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getOutputDirectory() {
		return this.outputDirectory;
	}
	
	/** Replace the current source directory by the given directory.
	 * <p>
	 * CAUTION: this function override the source directory for all
	 * the Maven plugins. Invoking this function may cause failures
	 * in other maven plugins.
	 * 
	 * @param newSourceDirectory
	 */
	protected void setSourceDirectoryForAllMojo(File newSourceDirectory) {
		List<String> sourceRoots = this.mavenProject.getCompileSourceRoots();
		getLog().debug("Old source roots: "+sourceRoots.toString()); //$NON-NLS-1$
		Iterator<String> iterator = sourceRoots.iterator();
		String removableSourcePath = this.javaSourceRoot.getAbsolutePath();
		getLog().debug("Removable source root: "+removableSourcePath); //$NON-NLS-1$
		String path;
		while (iterator.hasNext()) {
			path = iterator.next();
			if (path!=null && path.equals(removableSourcePath)) {
				getLog().debug("Removing source root: "+path); //$NON-NLS-1$
				iterator.remove();
			}
		}
		getLog().debug("Adding source root: "+newSourceDirectory.getAbsolutePath()); //$NON-NLS-1$
		this.mavenProject.addCompileSourceRoot(newSourceDirectory.toString());
		this.sourceDirectory = newSourceDirectory;
	}

	/**
	 * Replies the directory where original source are located.
	 * 
	 * @return the directory where original source are located.
	 * @see #getGeneratedSourceDirectory()
	 * @see #getClassDirectory()
	 */
	protected File getSourceDirectory() {
		return this.sourceDirectory;
	}

	/**
	 * Replies the directory where generated source are located.
	 * 
	 * @return the directory where generated source are located.
	 * @see #getSourceDirectory()
	 * @see #getClassDirectory()
	 */
	protected File getGeneratedSourceDirectory() {
		return this.generatedSourceDirectory;
	}

	/**
	 * Replies the directory where generated site are located.
	 * 
	 * @return the directory where generated site are located.
	 * @see #getSourceDirectory()
	 * @see #getClassDirectory()
	 */
	protected File getSiteDirectory() {
		return this.siteDirectory;
	}

	/**
	 * Replies the directory where generated classes are located.
	 * 
	 * @return the directory where generated classes are located.
	 * @see #getSourceDirectory()
	 * @see #getOutputDirectory()
	 */
	protected File getClassDirectory() {
		return this.classDirectory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArtifactHandlerManager getArtifactHandlerManager() {
		return this.artifactHandlerManager;
	}

	/**
	 * Types of replacement.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected enum ReplacementType {
		/**
		 * HTML replacement.
		 */
		HTML,

		/**
		 * Raw text replacement.
		 */
		TEXT;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getRepositorySystemSession() {
		return this.repoSession;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<?> getRemoteRepositoryList() {
		return this.remoteRepos;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RepositorySystem getRepositorySystem() {
		return this.repoSystem;
	}

}
