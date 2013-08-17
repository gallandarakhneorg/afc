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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.repository.RepositorySystem;

/**
 * Provide a bridge for compatibility
 * between the Sonatype and Eclipse implementations
 * of Aether.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AetherBridge {

	/** Indicates if the current implementation of the 
	 * Aether API in from Eclipse (if <code>true</code>),
	 * or from Sonatype (if <code>false</code>).
	 */
	public static final boolean IS_ECLIPSE_API;

	static {
		boolean isEclipse;
		try {
			Class.forName("org.eclipse.aether.artifact.DefaultArtifact"); //$NON-NLS-1$
			isEclipse = true;
		}
		catch(Throwable _) {
			isEclipse = false;
		}
		IS_ECLIPSE_API = isEclipse;
	}

	/**
	 */
	private AetherBridge() {
		//
	}
	/*	
	<!-- Eclipse Aether for Maven 3.1.x -->
	<dependency>
		<groupId>org.eclipse.aether</groupId>
		<artifactId>aether-api</artifactId>
	</dependency>
	<!-- Sonatype Aether for Maven 3.0.x -->
	<dependency>
		<groupId>org.sonatype.aether</groupId>
		<artifactId>aether-api</artifactId>
	</dependency>
	 */

	private static <T> T newInstance(String classname, Class<T> instanceType, Object... parameters) throws MojoExecutionException {
		try {
			Class<?> type = Class.forName(classname);
			Class<?>[] formalParameters = Arrays.copyOfRange(
					parameters, 0, parameters.length/2, Class[].class);
			Object[] realParameters = Arrays.<Object>copyOfRange(parameters,
					formalParameters.length, parameters.length-1);
			Constructor<?> cons = type.getConstructor(formalParameters);
			Object instance = cons.newInstance(realParameters);
			return instanceType.cast(instance);
		}
		catch(Throwable e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private static Class<?> getType(String classname)  throws MojoExecutionException {
		try {
			return Class.forName(classname);
		}
		catch(Throwable e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private static void callMethod(Object instance, String functionName, Object... parameters) throws MojoExecutionException {
		try {
			Class<?> type = instance.getClass();
			Class<?>[] formalParameters = Arrays.copyOfRange(
					parameters, 0, parameters.length/2, Class[].class);
			Object[] realParameters = Arrays.<Object>copyOfRange(parameters,
					formalParameters.length, parameters.length-1);
			Method meth = type.getMethod(functionName, formalParameters);
			meth.invoke(realParameters);
		}
		catch(Throwable e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private static <T> T callFunction(Object instance, String functionName, Class<T> returnType, Object... parameters) throws MojoExecutionException {
		try {
			Class<?> type = instance.getClass();
			Class<?>[] formalParameters = Arrays.copyOfRange(
					parameters, 0, parameters.length/2, Class[].class);
			Object[] realParameters = Arrays.<Object>copyOfRange(parameters,
					formalParameters.length, parameters.length-1);
			Method meth = type.getMethod(functionName, formalParameters);
			Object value = meth.invoke(realParameters);
			return value==null ? null : returnType.cast(value);
		}
		catch(Throwable e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	/** Create an artifact.
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
	public static Artifact createArtifact(String groupId, String artifactId, String version) throws MojoExecutionException {
		String classname;
		if (IS_ECLIPSE_API) {
			classname = "org.eclipse.aether.artifact.DefaultArtifact"; //$NON-NLS-1$
		}
		else {
			classname = "org.sonatype.aether.util.artifact.DefaultArtifact"; //$NON-NLS-1$
		}
		return newInstance(
				classname,
				Artifact.class,
				String.class, String.class, String.class, String.class,
				groupId, artifactId, "jar", version); //$NON-NLS-1$
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
	 * @param remoteRepositories is the list of the remote repositories given by Maven.
	 * @param repositorySystem is the system of repositories given by Maven.
	 * @param repositorySystemSession is the session of the repository system given by Maven.
	 * @return the artifact definition.
	 * @throws MojoExecutionException 
	 */
	public static Artifact resolveArtifact(
			String groupId, String artifactId, String version,
			List<?> remoteRepositories,
			RepositorySystem repositorySystem,
			Object repositorySystemSession)
					throws MojoExecutionException {
		return resolveArtifact(
				createArtifact(groupId, artifactId, version),
				remoteRepositories,
				repositorySystem,
				repositorySystemSession);
	}

	/**
	 * Retreive the extended artifact definition of the given artifact.
	 * @param mavenArtifact - the artifact to resolve
	 * @param remoteRepositories is the list of the remote repositories given by Maven.
	 * @param repositorySystem is the system of repositories given by Maven.
	 * @param repositorySystemSession is the session of the repository system given by Maven.
	 * @return the artifact definition.
	 * @throws MojoExecutionException
	 */
	public static Artifact resolveArtifact(Artifact mavenArtifact,
			List<?> remoteRepositories,
			RepositorySystem repositorySystem,
			Object repositorySystemSession)
					throws MojoExecutionException {		
		assert(mavenArtifact!=null);

		Class<?> resultType, sessionType, requestType, artifactType;
		if (IS_ECLIPSE_API) {
			resultType = getType("org.eclipse.aether.resolution.ArtifactResult"); //$NON-NLS-1$
			sessionType = getType("org.eclipse.aether.RepositorySystemSession"); //$NON-NLS-1$
			requestType = getType("org.eclipse.aether.resolution.ArtifactRequest"); //$NON-NLS-1$
			artifactType = getType("org.eclipse.aether.artifact.Artifact"); //$NON-NLS-1$
		}
		else {
			resultType = getType("org.sonatype.aether.resolution.ArtifactResult"); //$NON-NLS-1$
			sessionType = getType("org.sonatype.aether.RepositorySystemSession"); //$NON-NLS-1$
			requestType = getType("org.sonatype.aether.resolution.ArtifactRequest"); //$NON-NLS-1$
			artifactType = getType("org.sonatype.aether.artifact.Artifact"); //$NON-NLS-1$
		}

		try {
			Object request = requestType.newInstance();
			callMethod(request, "setArtifact",  //$NON-NLS-1$
					artifactType,
					mavenArtifact);
			callMethod(request, "setRepositories",  //$NON-NLS-1$
					List.class,
					remoteRepositories);

			Object result = callFunction(
					repositorySystem,
					"resolveArtifact", //$NON-NLS-1$
					resultType,
					sessionType, requestType,
					repositorySystemSession, request);

			return callFunction(
					result,
					"getArtifact", //$NON-NLS-1$
					Artifact.class);
		}
		catch (Throwable e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

}