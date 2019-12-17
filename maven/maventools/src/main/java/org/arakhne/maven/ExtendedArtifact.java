/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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
@SuppressWarnings({"checkstyle:methodcount"})
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

	/** Constructor.
	 * @param artifact the artifact.
	 * @param name name of the artifact.
	 * @param website website.
	 * @param organization organization.
	 * @param scmRevision url of the SCM.
	 * @param scm SCM.
	 * @param developers developers.
	 * @param contributors constributors.
	 * @param licenses licenses.
	 */
	public ExtendedArtifact(
			Artifact artifact, String name,
			String website, Organization organization,
			String scmRevision,
			Scm scm,
			List<? extends Developer> developers,
			List<? extends Contributor> contributors,
			List<? extends License> licenses) {
		this.original = artifact;
		this.artifactName = name;
		this.developers = developers;
		this.contributors = contributors;
		this.website = website;
		this.organization = organization;
		this.scm = scm;
		this.scmRevision = scmRevision;
		this.licenses = licenses;
	}

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
		return this.contributors == null ? Collections.<Contributor>emptyList() : this.contributors;
	}

	/** Replies the list of developers.
	 *
	 * @return the list of developers.
	 */
	public List<? extends Developer> getDevelopers() {
		return this.developers == null ? Collections.<Developer>emptyList() : this.developers;
	}

	/** Replies the people with the given login.
	 * This function checks the peoples replied
	 * by {@link #getDevelopers()} and
	 * {@link #getContributors()}.
	 *
	 * @param login contributor login
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
	 * @param login login.
	 * @param logger logger.
	 * @return the people or <code>null</code>
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "npathcomplexity"})
	public Contributor getPeople(String login, Log logger) {
		for (final Developer devel : getDevelopers()) {
			if (devel != null) {
				if (logger != null && logger.isDebugEnabled()) {
					logger.debug(
							"Comparing '" + login //$NON-NLS-1$
							+ " to the developer [ID=" + devel.getId() //$NON-NLS-1$
							+ ";NAME=" + devel.getName() //$NON-NLS-1$
							+ ";EMAIL=" + devel.getEmail() //$NON-NLS-1$
							+ "]"); //$NON-NLS-1$
				}
				String idprop = null;
				final Properties props = devel.getProperties();
				if (props != null) {
					idprop = props.getProperty("id", null); //$NON-NLS-1$
					if (idprop == null) {
						idprop = props.getProperty("login", null); //$NON-NLS-1$
					}
				}
				if (login.equals(devel.getId())
						|| login.equals(devel.getName())
						|| login.equals(devel.getEmail())
						|| login.equals(idprop)) {
					if (logger != null && logger.isDebugEnabled()) {
						logger.debug(
								"Selecting the developer [ID=" + devel.getId() //$NON-NLS-1$
								+ ";NAME=" + devel.getName() //$NON-NLS-1$
								+ ";EMAIL=" + devel.getEmail() //$NON-NLS-1$
								+ "]"); //$NON-NLS-1$
					}
					return devel;
				}
			}
		}
		for (final Contributor contrib : getContributors()) {
			if (contrib != null) {
				if (logger != null && logger.isDebugEnabled()) {
					logger.debug(
							"Comparing '" + login //$NON-NLS-1$
							+ " to the contributor [NAME=" + contrib.getName() //$NON-NLS-1$
							+ ";EMAIL=" + contrib.getEmail() //$NON-NLS-1$
							+ "]"); //$NON-NLS-1$
				}
				String idprop = null;
				final Properties props = contrib.getProperties();
				if (props != null) {
					idprop = props.getProperty("id", null); //$NON-NLS-1$
					if (idprop == null) {
						idprop = props.getProperty("login", null); //$NON-NLS-1$
					}
				}
				if (login.equals(contrib.getName())
						|| login.equals(contrib.getEmail())
						|| login.equals(idprop)) {
					if (logger != null && logger.isDebugEnabled()) {
						logger.debug(
								"Selecting the contributor [NAME=" + contrib.getName() //$NON-NLS-1$
								+ ";EMAIL=" + contrib.getEmail() //$NON-NLS-1$
								+ "]"); //$NON-NLS-1$
					}
					return contrib;
				}
			}
		}
		if (logger != null && logger.isDebugEnabled()) {
			logger.debug("No people found for: " + login); //$NON-NLS-1$
			logger.debug("Developers are: " + this.developers.toString()); //$NON-NLS-1$
			logger.debug("Contributors are: " + this.contributors.toString()); //$NON-NLS-1$
		}
		return null;
	}

	/** Replies the name of the artifact.
	 *
	 * @return the name of the artifact.
	 */
	public String getName() {
		if (this.artifactName == null || EMPTY_STRING.equals(this.artifactName)) {
			return getArtifactId();
		}
		return this.artifactName;
	}

	@Override
	public ArtifactHandler getArtifactHandler() {
		return this.original.getArtifactHandler();
	}

	@Override
	public String getArtifactId() {
		return this.original.getArtifactId();
	}

	@Override
	public List<ArtifactVersion> getAvailableVersions() {
		return this.original.getAvailableVersions();
	}

	@Override
	public String getBaseVersion() {
		return this.original.getBaseVersion();
	}

	@Override
	public String getClassifier() {
		return this.original.getClassifier();
	}

	@Override
	public String getDependencyConflictId() {
		return this.original.getDependencyConflictId();
	}

	@Override
	public ArtifactFilter getDependencyFilter() {
		return this.original.getDependencyFilter();
	}

	@Override
	public List<String> getDependencyTrail() {
		return this.original.getDependencyTrail();
	}

	@Override
	public String getDownloadUrl() {
		return this.original.getDownloadUrl();
	}

	@Override
	public File getFile() {
		return this.original.getFile();
	}

	@Override
	public String getGroupId() {
		return this.original.getGroupId();
	}

	@Override
	public String getId() {
		return this.original.getId();
	}

	@Override
	public ArtifactRepository getRepository() {
		return this.original.getRepository();
	}

	@Override
	public String getScope() {
		return this.original.getScope();
	}

	@Override
	public ArtifactVersion getSelectedVersion() throws OverConstrainedVersionException {
		return this.original.getSelectedVersion();
	}

	@Override
	public String getType() {
		return this.original.getType();
	}

	@Override
	public String getVersion() {
		return this.original.getVersion();
	}

	@Override
	public VersionRange getVersionRange() {
		return this.original.getVersionRange();
	}

	@Override
	public boolean hasClassifier() {
		return this.original.hasClassifier();
	}

	@Override
	public boolean isOptional() {
		return this.original.isOptional();
	}

	@Override
	public boolean isRelease() {
		return this.original.isRelease();
	}

	@Override
	public boolean isResolved() {
		return this.original.isResolved();
	}

	@Override
	public boolean isSelectedVersionKnown() throws OverConstrainedVersionException {
		return this.original.isSelectedVersionKnown();
	}

	@Override
	public boolean isSnapshot() {
		return this.original.isSnapshot();
	}

	@Override
	public void selectVersion(String version) {
		this.original.selectVersion(version);
	}

	@Override
	public void setArtifactHandler(ArtifactHandler handler) {
		this.original.setArtifactHandler(handler);
	}

	@Override
	public void setArtifactId(String artifactId) {
		this.original.setArtifactId(artifactId);
	}

	@Override
	public void setBaseVersion(String baseVersion) {
		this.original.setBaseVersion(baseVersion);
	}

	@Override
	public void setDependencyFilter(ArtifactFilter artifactFilter) {
		this.original.setDependencyFilter(artifactFilter);
	}

	@Override
	public void setDownloadUrl(String downloadUrl) {
		this.original.setDownloadUrl(downloadUrl);
	}

	@Override
	public void setFile(File destination) {
		this.original.setFile(destination);
	}

	@Override
	public void setGroupId(String groupId) {
		this.original.setGroupId(groupId);
	}

	@Override
	public void setRelease(boolean release) {
		this.original.setRelease(release);
	}

	@Override
	public void setRepository(ArtifactRepository remoteRepository) {
		this.original.setRepository(remoteRepository);
	}

	@Override
	public void setResolved(boolean resolved) {
		this.original.setResolved(resolved);
	}

	@Override
	public void setResolvedVersion(String version) {
		this.original.setResolvedVersion(version);
	}

	@Override
	public void setScope(String scope) {
		this.original.setScope(scope);
	}

	@Override
	public void setVersion(String version) {
		this.original.setVersion(version);
	}

	@Override
	public void setVersionRange(VersionRange newRange) {
		this.original.setVersionRange(newRange);
	}

	@Override
	public void updateVersion(String version, ArtifactRepository localRepository) {
		this.original.updateVersion(version, localRepository);
	}

	@Override
	public int compareTo(Artifact artifact) {
		return this.original.compareTo(artifact);
	}

	@Override
	public void setAvailableVersions(List<ArtifactVersion> versions) {
		this.original.setAvailableVersions(versions);
	}

	@Override
	public void setDependencyTrail(List<String> dependencyTrail) {
		this.original.setDependencyTrail(dependencyTrail);
	}

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

		final ArtifactMetadata m = this.metadataMap.get(metadata.getKey());
		if (m != null) {
			m.merge(metadata);
		} else {
			this.metadataMap.put(metadata.getKey(), metadata);
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
