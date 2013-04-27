/* 
 * $Id$
 * 
 * Copyright (C) 2011-12 Stephane GALLAND This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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
package org.arakhne.maven.plugins.licenseinstaller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Developer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.arakhne.maven.AbstractArakhneMojo;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.repository.RemoteRepository;

/**
 * Add license files in the META-INF directory of the target directory. The created files are:
 * <ul>
 * <li>LICENSE.soft.txt: the text of the license of the soft</li>
 * <li>NOTICE.soft.txt: a list of the authors and contributors</li>
 * </ul>
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * 
 * @goal installlicense
 * @phase compile
 * @requireProject true
 * @threadSafe
 */
public class InstallLicenseMojo extends AbstractArakhneMojo implements Constants {

	/**
	 * @component role="org.apache.maven.project.MavenProjectBuilder"
	 * @required
	 * @readonly
	 */
	private MavenProjectBuilder mavenProjectBuilder;

	/**
	 * @component
	 */
	private ArtifactHandlerManager artifactHandlerManager;

	/**
	 * @parameter property="project.basedir"
	 */
	private File baseDirectory;

	/**
	 * @parameter property="project.build.directory"
	 */
	private File outputDirectory;

	/**
	 * Licenses to install.
	 * 
	 * @parameter
	 * @required
	 */
	private String[] licenses;

	/**
	 * Third-party licenses. A file to license name map.
	 * 
	 * @parameter
	 */
	private String[] thirdPartyLicenses;

	/**
	 * Copyright dates.
	 * 
	 * @parameter
	 * @required
	 */
	private String copyrightDates;

	/**
	 * Copyrighters.
	 * 
	 * @parameter
	 * @required
	 */
	private String copyrighters;

	/**
	 * Project url.
	 * 
	 * @parameter default-value="${project.url}"
	 * @required
	 */
	private URL projectURL;

	/**
	 * Reference to the current maven project.
	 * 
	 * @parameter property="project"
	 */
	private MavenProject mavenProject;

	/**
	 * Name of the pom project.
	 * 
	 * @parameter property="project.name"
	 * @required
	 */
	private String name;

	/**
	 * Reference to the current session.
	 * 
	 * @parameter property="session"
	 * @required
	 */
	private MavenSession mavenSession;

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
	private RepositorySystemSession repoSession;

	/**
	 * The project's remote repositories to use for the resolution of plugins and their dependencies.
	 * 
	 * @parameter default-value="${project.remoteProjectRepositories}"
	 * @readonly
	 */
	private List<RemoteRepository> remoteRepos;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void checkMojoAttributes() {
		assertNotNull("copyrightDates", this.copyrightDates); //$NON-NLS-1$
		assertNotNull("copyrighters", this.copyrighters); //$NON-NLS-1$
		assertNotNull("licenses", this.licenses); //$NON-NLS-1$
		assertNotNull("projectURL", this.projectURL); //$NON-NLS-1$
		assertNotNull("outputDirectory", this.outputDirectory); //$NON-NLS-1$
		assertNotNull("artifactHandlerManager", this.artifactHandlerManager); //$NON-NLS-1$
		assertNotNull("mavenProject", this.mavenProject); //$NON-NLS-1$
		assertNotNull("mavenSession", this.mavenSession); //$NON-NLS-1$
		assertNotNull("repositorySystem", this.repoSystem); //$NON-NLS-1$
		assertNotNull("repositorySystemSession", this.repoSession); //$NON-NLS-1$
		assertNotNull("remoteRepositoryList", this.remoteRepos); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void executeMojo() throws MojoExecutionException {
		if ("pom".equalsIgnoreCase(this.mavenProject.getPackaging())) { //$NON-NLS-1$
			String key = ArtifactUtils.key(this.mavenProject.getGroupId(), this.mavenProject.getArtifactId(), this.mavenProject.getVersion());
			info("Ignoring project ", //$NON-NLS-1$
					key, " because it is a source-less project."); //$NON-NLS-1$
			return;
		}

		File classesDirectory = new File(getOutputDirectory(), CLASSES_DIR);
		File metainfDirectory = new File(classesDirectory, METAINF_DIR);
		File licenseDirectory = new File(metainfDirectory, LICENSE_DIR);

		licenseDirectory.mkdirs();

		Set<License> coreInstalled = new HashSet<License>();
		License lic;
		URL resource;
		String filename;
		File licFile;

		Map<String, License> includedLicenses = new HashMap<String,License>();
		Pattern re1 = Pattern.compile("^([^:]+):(.+):(.+)$"); //$NON-NLS-1$
		Pattern re2 = Pattern.compile("^([^:]+):(.+)$"); //$NON-NLS-1$
		if (this.thirdPartyLicenses != null) {
			for (String tpl : this.thirdPartyLicenses) {
				Matcher matcher = re1.matcher(tpl);
				if (matcher.matches()) {
					String l = matcher.group(1);
					String f = matcher.group(2);
					lic = License.parse(l, null);
					if (lic != null) {
						StringBuilder sb = new StringBuilder();
						sb.append(this.name);
						sb.append("_"); //$NON-NLS-1$
						String b = f.replaceAll("[/\\:]", "."); //$NON-NLS-1$ //$NON-NLS-2$
						b = b.replaceAll("^\\.", ""); //$NON-NLS-1$ //$NON-NLS-2$
						sb.append(b);
						includedLicenses.put(sb.toString(), lic);
					}
				}
				else {
					matcher = re2.matcher(tpl);
					if (matcher.matches()) {
						String l = matcher.group(1);
						String g = matcher.group(2);
						String a = matcher.group(3);
						lic = License.parse(l, null);
						if (lic != null) {
							String depName = "mvndep_"+g+"_"+a; //$NON-NLS-1$ //$NON-NLS-2$
							includedLicenses.put(depName, lic);
						}
					}
				}
			}
		}

		try {

			// ----------------------------------------------------
			// SOFTWARE LICENCES
			// ----------------------------------------------------

			for (String license : this.licenses) {
				lic = License.parse(license, License.GPLv3);
				if (!coreInstalled.contains(lic)) {
					resource = lic.getFullTextResource();
					if (resource != null) {

						filename = LICENSE_FILENAME_PATTERN.replaceAll("%s", this.name); //$NON-NLS-1$
						filename = filename.replaceAll("%l", lic.name()); //$NON-NLS-1$

						info("Installing license: ", //$NON-NLS-1$
								lic.getLicenseName(), ", into ", //$NON-NLS-1$
								filename);

						licFile = new File(licenseDirectory, filename);
						fileCopy(resource, licFile);

						coreInstalled.add(lic);
					} else {
						throw new MojoExecutionException("Unable to find the full text of the license: " + lic.getLicenseName()); //$NON-NLS-1$
					}
				}
			}

			// ----------------------------------------------------
			// THIRD-PARTY LICENCES
			// ----------------------------------------------------

			for (Entry<String, License> licenseMap : includedLicenses.entrySet()) {
				lic = licenseMap.getValue();
				resource = lic.getFullTextResource();
				if (resource != null) {
					filename = LICENSE_FILENAME_PATTERN.replaceAll("%s", licenseMap.getKey()); //$NON-NLS-1$ 
					filename = filename.replaceAll("%l", lic.name()); //$NON-NLS-1$

					info("Installing included source code license: ", //$NON-NLS-1$
							lic.getLicenseName(), ", into ", //$NON-NLS-1$
							filename);

					licFile = new File(licenseDirectory, filename);
					fileCopy(resource, licFile);
				} else {
					throw new MojoExecutionException("Unable to find the full text of the license: " + lic.getLicenseName()); //$NON-NLS-1$
				}
			}

			// ----------------------------------------------------
			// NOTICES
			// ----------------------------------------------------

			filename = NOTICE_FILENAME_PATTERN.replaceAll("%s", this.name); //$NON-NLS-1$ 

			info("Installing license notice: ", //$NON-NLS-1$
					filename);

			StringBuilder addParts = new StringBuilder();
			for (Entry<String, License> licenseMap : includedLicenses.entrySet()) {
				lic = licenseMap.getValue();
				if (lic != null) {
					String s = getLString(InstallLicenseMojo.class, "NOTICE_SENTENCE", //$NON-NLS-1$
							lic.getLicenseName(), licenseMap.getKey());
					addParts.append(s);
				}
			}

			StringBuilder fullLicenseText = new StringBuilder();
			int n = 0;
			for (License l : coreInstalled) {
				if (n > 0) {
					if (n == coreInstalled.size() - 1) {
						fullLicenseText.append(getLString(InstallLicenseMojo.class, "AND")); //$NON-NLS-1$
					} else {
						fullLicenseText.append(getLString(InstallLicenseMojo.class, "COMMA")); //$NON-NLS-1$
					}
				}
				fullLicenseText.append(l.getLicenseName());
				++n;
			}

			String noticeText = getLString(InstallLicenseMojo.class, "NOTICE_TEXT", //$NON-NLS-1$
					this.name, this.copyrightDates, this.copyrighters, fullLicenseText.toString(), addParts.toString());

			File noticeFile = new File(metainfDirectory, filename);
			FileWriter fileWriter = new FileWriter(noticeFile);
			try {
				fileWriter.write(noticeText);
			}
			finally {
				fileWriter.close();
			}

			// ----------------------------------------------------
			// AUTHORS
			// ----------------------------------------------------

			filename = AUTHOR_FILENAME_PATTERN.replaceAll("%s", this.name); //$NON-NLS-1$ 

			info("Installing authors: ", //$NON-NLS-1$
					filename);

			StringBuilder authorsText = new StringBuilder();
			if (this.projectURL != null) {
				authorsText.append(getLString(InstallLicenseMojo.class, "AUTHOR_INTRO", this.projectURL.toExternalForm())); //$NON-NLS-1$
			} else {
				authorsText.append(getLString(InstallLicenseMojo.class, "AUTHOR_INTRO", "")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			authorsText.append("\n\n"); //$NON-NLS-1$
			authorsText.append(getLString(InstallLicenseMojo.class, "DEVELOPER_TITLE", this.name)); //$NON-NLS-1$
			authorsText.append("\n\n"); //$NON-NLS-1$
			for (Developer developer : this.mavenProject.getDevelopers()) {
				authorsText.append(getLString(InstallLicenseMojo.class, "DEVELOPER", //$NON-NLS-1$
						developer.getName(), developer.getEmail()));
				authorsText.append("\n"); //$NON-NLS-1$
			}

			authorsText.append("\n\n\n"); //$NON-NLS-1$
			authorsText.append(getLString(InstallLicenseMojo.class, "CONTRIBUTOR_TITLE", this.name)); //$NON-NLS-1$
			authorsText.append("\n\n"); //$NON-NLS-1$
			for (Contributor contributor : this.mavenProject.getContributors()) {
				authorsText.append(getLString(InstallLicenseMojo.class, "DEVELOPER", //$NON-NLS-1$
						contributor.getName(), contributor.getEmail()));
				authorsText.append("\n"); //$NON-NLS-1$
			}

			File authorFile = new File(metainfDirectory, filename);
			fileWriter = new FileWriter(authorFile);
			try {
				fileWriter.write(authorsText.toString());
			}
			finally {
				fileWriter.close();
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getOutputDirectory() {
		return this.outputDirectory;
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
	public ArtifactHandlerManager getArtifactHandlerManager() {
		return this.artifactHandlerManager;
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
	public RepositorySystemSession getRepositorySystemSession() {
		return this.repoSession;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RemoteRepository> getRemoteRepositoryList() {
		return this.remoteRepos;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RepositorySystem getRepositorySystem() {
		return this.repoSystem;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MavenProjectBuilder getMavenProjectBuilder() {
		return this.mavenProjectBuilder;
	}

}
