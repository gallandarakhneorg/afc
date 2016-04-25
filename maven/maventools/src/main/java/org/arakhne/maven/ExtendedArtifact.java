/* 
 * $Id$
 * 
 * Copyright (C) 2010-11 Stephane GALLAND This library is free software; you can redistribute it and/or
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

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Developer;
import org.apache.maven.model.License;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Scm;
import org.apache.maven.plugin.logging.Log;

/**
 * Artifact implementation which also includes the artifact name,
 * contributors, authors, and website.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("deprecation")
public final class ExtendedArtifact implements Artifact {

	/** Empty string constant.
	 */
	public static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private final String artifactName;
	private final Artifact original;
	private final List<? extends Developer> developers;
	private final List<? extends Contributor> contributors;
	private final String website;
	private final Organization organization;
	private final Scm scm;
	private final String scmRevision;
	private final List<? extends License> licenses;
	private Map<Object, ArtifactMetadata> metadataMap;

	/**
	 * @param a
	 * @param name
	 * @param website
	 * @param organization
	 * @param scmRevision
	 * @param scm
	 * @param developers
	 * @param contributors
	 * @param licenses
	 */
	public ExtendedArtifact(
			Artifact a, String name,
			String website, Organization organization,
			String scmRevision,
			Scm scm,
			List<? extends Developer> developers,
			List<? extends Contributor> contributors,
			List<? extends License> licenses) {
		this.original = a;
		this.artifactName = name;
		this.developers = developers;
		this.contributors = contributors;
		this.website = website;
		this.organization = organization;
		this.scm = scm;
		this.scmRevision = scmRevision;
		this.licenses = licenses;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.original.toString();
	}

	/** Replies the licenses of this artifact.
	 * 
	 * @return the licenses of this artifact.
	 */
	public List<? extends License> getLicenses() {
		return this.licenses;
	}

	/** Replies the SCM definition of this artifact.
	 * 
	 * @return the SCM definition of this artifact.
	 */
	public Scm getScm() {
		return this.scm;
	}

	/** Replies the organization of this artifact.
	 * 
	 * @return the organization of this artifact.
	 */
	public Organization getOrganization() {
		return this.organization;
	}

	/** Replies the URL of the website for this artifact.
	 * 
	 * @return the URL of the website for this artifact.
	 */
	public String getWebsite() {
		return this.website;
	}

	/** Replies the list of contributors.
	 * 
	 * @return the list of contributors.
	 */
	public List<? extends Contributor> getContributors() {
		return this.contributors==null ? Collections.<Contributor>emptyList() : this.contributors;
	}

	/** Replies the list of developers.
	 * 
	 * @return the list of developers.
	 */
	public List<? extends Developer> getDevelopers() {
		return this.developers==null ? Collections.<Developer>emptyList() : this.developers;
	}

	/** Replies the people with the given login.
	 * This function checks the peoples replied
	 * by {@link #getDevelopers()} and
	 * {@link #getContributors()}.
	 * 
	 * @param login
	 * @return the people or <code>null</code>
	 */
	public Contributor getPeople(String login) {
		return getPeople(login, null);
	}
	
	/** Replies the people with the given login.
	 * This function checks the peoples replied
	 * by {@link #getDevelopers()} and
	 * {@link #getContributors()}.
	 * 
	 * @param login
	 * @param logger
	 * @return the people or <code>null</code>
	 */
	public Contributor getPeople(String login, Log logger) {
		for(Developer devel : getDevelopers()) {
			if (devel!=null) {
				if (logger!=null && logger.isDebugEnabled()) {
					logger.debug(
							"Comparing '"+login //$NON-NLS-1$
							+" to the developer [ID="+devel.getId() //$NON-NLS-1$
							+";NAME="+devel.getName() //$NON-NLS-1$
							+";EMAIL="+devel.getEmail() //$NON-NLS-1$
							+"]"); //$NON-NLS-1$
				}
				String idprop = null;
				Properties props = devel.getProperties();
				if (props!=null) {
					idprop = props.getProperty("id", null); //$NON-NLS-1$
					if (idprop==null)
						idprop = props.getProperty("login", null); //$NON-NLS-1$
				}
				if (login.equals(devel.getId())
					||login.equals(devel.getName())
					||login.equals(devel.getEmail())
					||login.equals(idprop)) {
					if (logger!=null && logger.isDebugEnabled()) {
						logger.debug(
								"Selecting the developer [ID="+devel.getId() //$NON-NLS-1$
								+";NAME="+devel.getName() //$NON-NLS-1$
								+";EMAIL="+devel.getEmail() //$NON-NLS-1$
								+"]"); //$NON-NLS-1$
					}
					return devel;
				}
			}
		}
		for(Contributor contrib : getContributors()) {
			if (contrib!=null) {
				if (logger!=null && logger.isDebugEnabled()) {
					logger.debug(
							"Comparing '"+login //$NON-NLS-1$
							+" to the contributor [NAME="+contrib.getName() //$NON-NLS-1$
							+";EMAIL="+contrib.getEmail() //$NON-NLS-1$
							+"]"); //$NON-NLS-1$
				}
				String idprop = null;
				Properties props = contrib.getProperties();
				if (props!=null) {
					idprop = props.getProperty("id", null); //$NON-NLS-1$
					if (idprop==null)
						idprop = props.getProperty("login", null); //$NON-NLS-1$
				}
				if (login.equals(contrib.getName())
					||login.equals(contrib.getEmail())
					||login.equals(idprop)) {
					if (logger!=null && logger.isDebugEnabled()) {
						logger.debug(
								"Selecting the contributor [NAME="+contrib.getName() //$NON-NLS-1$
								+";EMAIL="+contrib.getEmail() //$NON-NLS-1$
								+"]"); //$NON-NLS-1$
					}
					return contrib;
				}
			}
		}
		if (logger!=null && logger.isDebugEnabled()) {
			logger.debug("No people found for: "+login); //$NON-NLS-1$
			logger.debug("Developers are: "+this.developers.toString()); //$NON-NLS-1$
			logger.debug("Contributors are: "+this.contributors.toString()); //$NON-NLS-1$
		}
		return null;
	}

	/** Replies the name of the artifact.
	 * 
	 * @return the name of the artifact.
	 */
	public String getName() {
		if (this.artifactName==null || EMPTY_STRING.equals(this.artifactName))
			return getArtifactId();
		return this.artifactName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArtifactHandler getArtifactHandler() {
		return this.original.getArtifactHandler();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getArtifactId() {
		return this.original.getArtifactId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ArtifactVersion> getAvailableVersions() {
		return this.original.getAvailableVersions();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBaseVersion() {
		return this.original.getBaseVersion();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getClassifier() {
		return this.original.getClassifier();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDependencyConflictId() {
		return this.original.getDependencyConflictId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArtifactFilter getDependencyFilter() {
		return this.original.getDependencyFilter();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getDependencyTrail() {
		return this.original.getDependencyTrail();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDownloadUrl() {
		return this.original.getDownloadUrl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getFile() {
		return this.original.getFile();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getGroupId() {
		return this.original.getGroupId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return this.original.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArtifactRepository getRepository() {
		return this.original.getRepository();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getScope() {
		return this.original.getScope();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArtifactVersion getSelectedVersion() throws OverConstrainedVersionException {
		return this.original.getSelectedVersion();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getType() {
		return this.original.getType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getVersion() {
		return this.original.getVersion();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VersionRange getVersionRange() {
		return this.original.getVersionRange();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasClassifier() {
		return this.original.hasClassifier();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOptional() {
		return this.original.isOptional();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRelease() {
		return this.original.isRelease();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isResolved() {
		return this.original.isResolved();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelectedVersionKnown() throws OverConstrainedVersionException {
		return this.original.isSelectedVersionKnown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSnapshot() {
		return this.original.isSnapshot();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selectVersion(String version) {
		this.original.selectVersion(version);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setArtifactHandler(ArtifactHandler handler) {
		this.original.setArtifactHandler(handler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setArtifactId(String artifactId) {
		this.original.setArtifactId(artifactId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBaseVersion(String baseVersion) {
		this.original.setBaseVersion(baseVersion);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDependencyFilter(ArtifactFilter artifactFilter) {
		this.original.setDependencyFilter(artifactFilter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDownloadUrl(String downloadUrl) {
		this.original.setDownloadUrl(downloadUrl);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFile(File destination) {
		this.original.setFile(destination);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGroupId(String groupId) {
		this.original.setGroupId(groupId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRelease(boolean release) {
		this.original.setRelease(release);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRepository(ArtifactRepository remoteRepository) {
		this.original.setRepository(remoteRepository);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResolved(boolean resolved) {
		this.original.setResolved(resolved);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResolvedVersion(String version) {
		this.original.setResolvedVersion(version);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setScope(String scope) {
		this.original.setScope(scope);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVersion(String version) {
		this.original.setVersion(version);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVersionRange(VersionRange newRange) {
		this.original.setVersionRange(newRange);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateVersion(String version, ArtifactRepository localRepository) {
		this.original.updateVersion(version, localRepository);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Artifact o) {
		return this.original.compareTo(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAvailableVersions(List<ArtifactVersion> versions) {
		this.original.setAvailableVersions(versions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDependencyTrail(List<String> dependencyTrail) {
		this.original.setDependencyTrail(dependencyTrail);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOptional(boolean optional) {
		this.original.setOptional(optional);
	}
	
	/** Replies the SCM revision for the artifact.
	 * 
	 * @return the revision number in the SCM repository.
	 */
	public String getScmRevision() {
		return this.scmRevision;
	}

	@Override
	public void addMetadata(ArtifactMetadata metadata) {
		if (this.metadataMap == null) {
            this.metadataMap = new HashMap<>();
        }

        ArtifactMetadata m = this.metadataMap.get(metadata.getKey());
        if (m != null) {
            m.merge( metadata );
        } else {
            this.metadataMap.put( metadata.getKey(), metadata );
        }
	}

	@Override
	public Collection<ArtifactMetadata> getMetadataList() {
		 if (this.metadataMap == null) {
			 return Collections.emptyList();
		 }
		 return this.metadataMap.values();
	}

}