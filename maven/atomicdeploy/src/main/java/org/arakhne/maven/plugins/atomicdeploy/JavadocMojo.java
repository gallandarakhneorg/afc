/* 
 * $Id$
 * 
 * Copyright (C) 2011 Stephane GALLAND This library is free software;
 * you can redistribute it and/or
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
package org.arakhne.maven.plugins.atomicdeploy;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Site;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.arakhne.maven.AbstractArakhneMojo;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;
import org.sonatype.plexus.build.incremental.BuildContext;

/**
 * Deploy the generated javadoc directory into the site deployment location.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * 
 * @goal javadoc
 * @phase deploy
 * @requireProject true
 * @threadSafe
 */
public class JavadocMojo extends AbstractArakhneMojo {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void checkMojoAttributes() {
		assertNotNull("javadocDirectory", this.javadocDirectory); //$NON-NLS-1$
		assertNotNull("outputDirectory", this.outputDirectory); //$NON-NLS-1$
		assertNotNull("artifactHandlerManager", this.artifactHandlerManager); //$NON-NLS-1$
		assertNotNull("baseDirectory", this.baseDirectory); //$NON-NLS-1$
		assertNotNull("mavenProjectBuilder", this.mavenProjectBuilder); //$NON-NLS-1$
		assertNotNull("mavenSession", this.mavenSession); //$NON-NLS-1$
		assertNotNull("repositorySystem", this.repoSystem); //$NON-NLS-1$
		assertNotNull("repositorySystemSession", this.repoSession); //$NON-NLS-1$
		assertNotNull("remoteRepositoryList", this.remoteRepos); //$NON-NLS-1$
		assertNotNull("targetDirectoryName", this.targetDirectoryName); //$NON-NLS-1$
		assertNotNull("buildContext", this.buildContext); //$NON-NLS-1$
	}

	//-----------------------------------------------------
	// Inherited behavior attributes

	/**
	 * @component
	 */
	private ArtifactHandlerManager artifactHandlerManager;

	/** The context of building, compatible with M2E and CLI.
	 * @component
	 */
	private BuildContext buildContext;

	/**
	 * @parameter property="project.basedir"
	 */
	private File baseDirectory;

	/**
	 * @parameter property="project.build.directory"
	 */
	private File outputDirectory;

	/**
	 * @component role="org.apache.maven.project.MavenProjectBuilder"
	 * @required
	 * @readonly
	 */
	private MavenProjectBuilder mavenProjectBuilder;

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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BuildContext getBuildContext() {
		return this.buildContext;
	}

	//-----------------------------------------------------
	// Mojo dedicated stuffs
	
	private static URL getSiteDeploymentURL(MavenProject project, String targetDirectoryName) {
		DistributionManagement distManagement = project.getDistributionManagement();
		if (distManagement!=null) {
			Site site = distManagement.getSite();
			if (site!=null) {
				String url = site.getUrl();
				if (url!=null && url.length()>0) {
					try {
						return new URL(url+"/"+targetDirectoryName); //$NON-NLS-1$
					}
					catch(Throwable e) {
						//
					}
				}
			}
		}
		return null;
	}

	private static URL getInheritedSiteDeploymentURL(MavenProject project, String targetDirectoryName) {
		URL deployementURL = null;
		MavenProject p = project; 
		while(deployementURL==null && p!=null) {
			deployementURL = getSiteDeploymentURL(p, targetDirectoryName);
			p = p.getParent();
		}
		return deployementURL;
	}

	/**
	 * Directory of the javadoc.
	 * 
	 * @parameter default-value="${project.build.directory}/site/apidocs"
	 */
	private String javadocDirectory;

	/**
	 * Name of the target subdirectory in the site deployment directory.
	 * 
	 * @parameter default-value="apidocs"
	 */
	private String targetDirectoryName;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void executeMojo() throws MojoExecutionException {
		MavenProject project = this.mavenSession.getCurrentProject();
		File javadocDir = new File(this.javadocDirectory);
		File indexFile = new File(javadocDir, "index.html"); //$NON-NLS-1$
		if (javadocDir.isDirectory() && indexFile.isFile()) {
			URL deployementURL = getInheritedSiteDeploymentURL(project, this.targetDirectoryName);
			BuildContext buildContext = getBuildContext();
			buildContext.removeMessages(project.getFile());
			if (deployementURL!=null) {
				if ("file".equalsIgnoreCase(deployementURL.getProtocol())) { //$NON-NLS-1$
					File output = new File(deployementURL.getPath());
					getLog().info("Input: "+javadocDir); //$NON-NLS-1$
					getLog().info("Output: "+output); //$NON-NLS-1$
					try {
						dirRemove(output);
						dirCopy(javadocDir, output, true);
					}
					catch (IOException e) {
						throw new MojoExecutionException(e.getMessage(), e);
					}
				}
				else {
					String message = "Unsupported transport protocol: "+deployementURL.getProtocol(); //$NON-NLS-1$
					buildContext.addMessage(
							project.getFile(), 
							1, 1, 
							message, 
							BuildContext.SEVERITY_ERROR, null);
				}
			}
			else {
				String message = "No site deployement URL found"; //$NON-NLS-1$
				buildContext.addMessage(
						project.getFile(), 
						1, 1, 
						message, 
						BuildContext.SEVERITY_ERROR, null);
			}
		}
		else {
			getLog().info("Skipping project, no javadoc directory found"); //$NON-NLS-1$
		}
	}

}
