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

package org.arakhne.afc.bootique.log4j.configs;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.charset.Charset;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Strings;
import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;

/**
 * Configuration for a console-based log4j appenders.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@BQConfig("Appender that prints its output into a file.")
@JsonTypeName("file")
public class FileAppenderConfig extends AppenderConfig {

	private static final String DEFAULT_FILE = "log.txt"; //$NON-NLS-1$

	private File file;

	private boolean append;

	private String encoding;

	private boolean immediateFlush = true;

	private Level threshold;

	/** Replies the target file.
	 *
	 * @return the target.
	 */
	public File getFile() {
		return this.file;
	}

	/** Change the target file.
	 *
	 * @param file the target.
	 */
	@BQConfigProperty("Whether the log should be write the log. Default file is " + DEFAULT_FILE)
	public void setFile(File file) {
		this.file = file;
	}

	/** Replies if the appender writes at the end of the existing file or not.
	 *
	 * @return {@code true} if log messages are appended at the end of thefile, {@code false} if the previous
	 *     content of the file is destroyed.
	 */
	public boolean getAppend() {
		return this.append;
	}

	/** Set if the appender writes at the end of the existing file or not.
	 *
	 * @param append {@code true} if log messages are appended at the end of thefile, {@code false} if the previous
	 *     content of the file is destroyed.
	 */
	@BQConfigProperty("Indicates if the appender writes at the end of the existing file or not. Default is false.")
	public void setAppend(boolean append) {
		this.append = append;
	}

	/** Replies the console encoding.
	 *
	 * @return the encoding.
	 */
	public String getEncoding() {
		if (Strings.isNullOrEmpty(this.encoding)) {
			return Charset.defaultCharset().displayName();
		}
		return this.encoding;
	}

	/** Change the console encoding.
	 *
	 * @param encoding the console encoding.
	 */
	@BQConfigProperty("The console encoding.")
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/** Replies if the text is flushed as soon as it it given to the logger.
	 *
	 * @return {@code true} for immediate flushing.
	 */
	public boolean getImmediateFlush() {
		return this.immediateFlush;
	}

	/** Change if the text is flushed as soon as it it given to the logger.
	 *
	 * @param flush {@code true} for immediate flushing.
	 */
	@BQConfigProperty("If the ImmediateFlush option is set to true, the appender will flush at the end of each write. "
			+ "This is the default behavior. If the option is set to false, then the underlying stream can defer writing "
			+ "to physical medium to a later time.")
	public void setImmediateFlush(boolean flush) {
		this.immediateFlush = flush;
	}

	/** Replies if the text is flushed as soon as it it given to the logger.
	 *
	 * @return {@code true} for immediate flushing.
	 */
	public Level getThreshold() {
		return this.threshold;
	}

	/** Set the threshold level. All log events with lower level
     than the threshold level are ignored by the appender.
	 *
	 * @param threshold the threshold level.
	 */
	@BQConfigProperty("All log events with lower level than the threshold level are ignored by the appender.")
	public void setThreshold(Level threshold) {
		this.threshold = threshold;
	}

	@Override
	public Appender createAppender(String defaultLogFormat) {
		try {
			File outputFile = getFile();
			if (outputFile == null) {
				outputFile = new File(DEFAULT_FILE);
			}
			final FileAppender appender = new FileAppender(
					createLayout(defaultLogFormat),
					outputFile.getAbsolutePath(), getAppend());
			appender.setName("file"); //$NON-NLS-1$
			appender.setEncoding(getEncoding());
			appender.setImmediateFlush(getImmediateFlush());
			if (getThreshold() != null) {
				appender.setThreshold(getThreshold().toLog4j());
			}
			return appender;
		} catch (IOException exception) {
			throw new IOError(exception);
		}
	}

}
