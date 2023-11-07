/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.inputoutput.path;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.FileSystem;

/** Interface that permits to build absolute paths from relative paths
 * and relative paths from absolute paths.
 *
 * <p>This interface supports both {@link File} and {@link URL}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public interface PathBuilder {

	/** Set the default/current directory used to make absolute the reltive paths.
	 *
	 * <p>This function tries to build an {@link URL}, and if it fails it assumed that the given
	 * string is a filename to pass to {@link File}.
	 *
	 * @param currentDirectory is the default/current directory used to make absolute the reltive paths.
	 */
	void setCurrentDirectory(String currentDirectory);

	/** Set the default/current directory used to make absolute the reltive paths.
	 *
	 * @param currentDirectory is the default/current directory used to make absolute the reltive paths.
	 */
	void setCurrentDirectory(File currentDirectory);

	/** Set the default/current directory used to make absolute the reltive paths.
	 *
	 * @param currentDirectory is the default/current directory used to make absolute the reltive paths.
	 */
	void setCurrentDirectory(URL currentDirectory);

	/** Replies the default/current directory used to make absolute the reltive paths.
	 *
	 * <p>This function replies the external form of the value replied by
	 * {@link #getCurrentDirectoryURL()}
	 *
	 * @return the default/current directory used to make absolute the reltive paths.
	 * @see #getCurrentDirectoryFile()
	 * @see #getCurrentDirectoryURL()
	 */
	@Pure
	@Inline(value = "getCurrentDirectoryURL().toExternalForm()")
	default String getCurrentDirectoryString() {
		return getCurrentDirectoryURL().toExternalForm();
	}

	/** Replies the default/current directory used to make absolute the reltive paths.
	 *
	 * <p>If the current directory is a {@link File}, it is replied.
	 * If the current directory is an {@link URL} with "file" protocol, its path is replied.
	 * In all other cases the user home directory is replied, or the default directory if
	 * the user home is unavailable.
	 *
	 * @return the default/current directory used to make absolute the reltive paths.
	 * @see #getCurrentDirectoryString()
	 * @see #getCurrentDirectoryURL()
	 */
	@Pure
	File getCurrentDirectoryFile();

	/** Replies the default/current directory used to make absolute the reltive paths.
	 *
	 * <p>This function replies the URL of the current directory even if the current
	 * directory was set with a {@link File}.
	 *
	 * @return the default/current directory used to make absolute the reltive paths.
	 * @see #getCurrentDirectoryFile()
	 * @see #getCurrentDirectoryString()
	 */
	@Pure
	URL getCurrentDirectoryURL();

	/** Make the given filename absolute.
	 *
	 * @param filename the file.
	 * @return absolute equivalent name.
	 */
	@Pure
	default URL makeAbsolute(File filename) {
		return FileSystem.makeAbsolute(filename, getCurrentDirectoryURL());
	}

	/** Make the given filename absolute.
	 *
	 * @param filename the file.
	 * @return absolute equivalent name.
	 */
	@Pure
	default URL makeAbsolute(URL filename) {
		if (filename == null) {
			return null;
		}
		return FileSystem.makeAbsolute(filename, getCurrentDirectoryURL());
	}

	/** Make the given filename absolute.
	 *
	 * <p>If the given filename is an URL, the external form of the URL is replied.
	 *
	 * @param filename the file.
	 * @return absolute equivalent name or {@code null} if it is impossible to obtain an URL.
	 */
	@Pure
	default URL makeAbsolute(String filename) {
		if (filename == null || filename.length() == 0) {
			return null;
		}
		final URL url = FileSystem.convertStringToURL(filename, true, false);
		if (url != null) {
			return makeAbsolute(url);
		}
		return makeAbsolute(new File(filename));
	}

	/** Make the given filename relative.
	 *
	 * @param filename the file.
	 * @return relative equivalent name.
	 * @throws IOException in case of error.
	 */
	@Pure
	default File makeRelative(File filename) throws IOException {
		return FileSystem.makeRelative(filename, getCurrentDirectoryURL());
	}

	/** Make the given filename relative.
	 *
	 * @param filename the file.
	 * @return relative equivalent name.
	 * @throws IOException in case of error.
	 */
	@Pure
	default File makeRelative(URL filename) throws IOException {
		if (filename == null) {
			return null;
		}
		return FileSystem.makeRelative(filename, getCurrentDirectoryURL());
	}

	/** Make the given filename relative.
	 *
	 * <p>If the given filename is an URL, the external form of the URL is replied.
	 *
	 * @param filename filename.
	 * @return relative equivalent name or {@code null} if it is impossible to obtain an File.
	 * @throws IOException in case of error.
	 */
	@Pure
	default File makeRelative(String filename) throws IOException {
		if (filename == null || filename.length() == 0) {
			return null;
		}
		final URL url = FileSystem.convertStringToURL(filename, true, false);
		if (url != null) {
			return makeRelative(url);
		}
		return makeRelative(new File(filename));
	}

	/** Try to make the given URL shorter in its form.
	 *
	 * @param url the URL to convert.
	 * @return the shorter URL or the url itself.
	 */
	@Pure
	default URL toShorterURL(URL url) {
		return FileSystem.toShortestURL(url);
	}

}
