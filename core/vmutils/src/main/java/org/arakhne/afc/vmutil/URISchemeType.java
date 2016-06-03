/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.vmutil;

import java.net.URI;
import java.net.URL;

import org.eclipse.xtext.xbase.lib.Pure;

/** Commonly supported types of schemes for URL.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 5.0
 */
public enum URISchemeType {

	/**
	 * One of the most widely used URL scheme is the http scheme.
	 * The http URL scheme is used to locate documents that
	 * reside on Web servers.
	 *
	 * <p>A Web server is more accurately called an HTTP server.
	 * HTTP stands for Hypertext Transfer Protocol, and is a
	 * protocol designed to transfer hypertext documents over
	 * the Internet. It is used to transfer almost all of
	 * the documents you download using your Web browser.
	 */
	HTTP {
		@Override
		public boolean isFileBasedScheme() {
			return true;
		}
	},

	/**
	 * One of the most widely used URL scheme is the https scheme.
	 * The https URL scheme is used to locate documents that
	 * reside on Securized Web servers.
	 *
	 * <p>https stands for Hypertext Transfer Protocol Secure. It
	 * is a combination of the Hypertext Transfer Protocol (http) with the
	 * <a href="http://en.wikipedia.org/wiki/Transport_Layer_Security">SSL/TLS</a>
	 * protocol to provide encryption and secure identification
	 * of the server. HTTPS connections are often used for
	 * payment transactions on the World Wide Web and for
	 * sensitive transactions in corporate information systems.
	 * HTTPS should not be confused with Secure HTTP (S-HTTP)
	 * specified in
	 * <a href="http://tools.ietf.org/html/rfc2660">RFC 2660</a>.
	 */
	HTTPS {
		@Override
		public boolean isFileBasedScheme() {
			return true;
		}
	},

	/**
	 * The ftp scheme is very similar to the http scheme, and is
	 * used to locate files available via FTP
	 * (File Transfer Protocol). The syntax is very similar to http syntax:
	 * <pre><code>
	 * ftp://sunsite.unc.edu/pub/Linux/ls-lR.gz
	 * </code></pre>
	 *
	 * <p>The above URL points to the FTP server on sunsite.unc.edu,
	 * to the file ls-lR.gz in the /pub/Linux directory. It is
	 * also possible to specify a username and optionally, a
	 * password for the connection. The syntax is like this:
	 * <pre><code>
	 * ftp://user@host/path/file
	 * ftp://user:password@host/path/file
	 * </code></pre>
	 *
	 * <p>Note that supplying a password like this is sometimes a
	 * bad idea. Some people might tell you this is a huge
	 * security risk, but this is not really true: a URL like
	 * the one above is typed into your browser will only be a
	 * risk if someone peeps over your shoulder and sees the
	 * password. The password itself is transmitted unencrypted
	 * anyway, and can be intercepted in transit. Before you
	 * go paranoid about this, remember that if your read
	 * your mail through POP (Post Office Protocol), like
	 * most people do, then your mail password is also
	 * transmitted in the clear. The lesson in this is that
	 * if you're going to be paranoid, at least do it for
	 * the right reasons.
	 */
	FTP {
		@Override
		public boolean isFileBasedScheme() {
			return true;
		}
	},

	/**
	 * The file scheme is used to point to files on your computer.
	 * It is slightly tricky, because (most) absolute file URLs
	 * aren't really absolute; they're always relative to your
	 * computer. However, you can specify the hostname in a file
	 * URL. Remember that a URL just tells you where a resource
	 * is located, not how to locate it. So this does make sense.
	 * If the hostname is omitted, the current host is assumed.
	 * If a URL is encountered by a program with a hostname
	 * that's different than the one it's working on, it will
	 * most likely decide that it cannot access the file, but
	 * this has nothing to do with the URL itself. The syntax
	 * is again much like the http syntax, only omitting the
	 * port numbers, like this:
	 * <pre><code>
	 * file:///home/stephanos/public_html/myface.jpg
	 * file://localhost/temp/install_log.txt
	 * </code></pre>
	 *
	 * <p>Note that the pathname here represents a path name in the
	 * local filesystem, so the slashes are usually replaced by
	 * a more appropriate character before the file is accessed.
	 * Unix uses slashes, Windows uses backslashes, Macintosh and
	 * other operating systems use other conventions.
	 */
	FILE {
		@Override
		public boolean isFileBasedScheme() {
			return true;
		}
	},

	/**
	 * The mailto scheme is an example of an opaque URI
	 * scheme. mailto URLs identify someone's
	 * e-mail address. Their syntax is simple.
	 * You have the scheme name, the colon, and then
	 * the e-mail address. If you're someone who has a
	 * thing for collecting e-mail addresses, you
	 * might refer to yourself in the following ways:
	 * <pre><code>
	 * mailto:someone@arakhne.org
	 * mailto:someone@arakhne.org?subject=Feedback
	 * </code></pre>
	 */
	MAILTO {
		@Override
		public boolean isFileBasedScheme() {
			return false;
		}
	},

	/**
	 * The news scheme is another opaque URL scheme.
	 * It is used to refer to Usenet newsgroups or
	 * specific messages within these newsgroups.
	 * It has two possible syntaxes. One is the name
	 * of a Usenet newsgroup, and the other is the
	 * message id of a Usenet post. Note that the
	 * message id must be entered without the usual
	 * angle brackets (&lt; and &gt;).
	 * <pre><code>
	 * news:comp.infosystems.www.authoring.html
	 * news:ba-ciwah-1998Jun4-013702@mud.stack.nl
	 * news:*
	 * </code></pre>
	 *
	 * <p>The third example points to all available
	 * newsgroups and can be used to refer to Usenet
	 * in general.
	 */
	NEWS {
		@Override
		public boolean isFileBasedScheme() {
			return false;
		}
	},

	/**
	 * The telnet scheme has identical syntax to the ftp scheme,
	 * with the exception that there is no pathame. Only a
	 * hostname, and optionally a port, username and password
	 * may be supplied.
	 * <pre><code>
	 * telnet://user:password@somehost.internet.com:35/
	 * </code></pre>
	 *
	 * <p>The above indicates a telnet session for user "user"
	 * with password "password" on port 35 of
	 * somehost.internet.com.
	 */
	TELNET {
		@Override
		public boolean isFileBasedScheme() {
			return false;
		}
	},

	/**
	 * The ssh scheme (also known as sftp - for Secure FTP)
	 * is very similar to the ftp scheme, and is
	 * used to locate files available via SSH (Secure SHell).
	 * The syntax is very similar to ftp syntax:
	 * <pre><code>
	 * ssh://sunsite.unc.edu/pub/Linux/ls-lR.gz
	 * </code></pre>
	 *
	 * <p>The above URL points to the SSH server on sunsite.unc.edu,
	 * to the file ls-lR.gz in the /pub/Linux directory. It is
	 * also possible to specify a username and optionally, a
	 * password for the connection. The syntax is like this:
	 * <pre><code>
	 * ssh://user@host/path/file
	 * ssh://user:password@host/path/file
	 * </code></pre>
	 *
	 * <p>Secure Shell is a network protocol that allows data to
	 * be exchanged using a secure channel between two
	 * networked devices. Used primarily on GNU/Linux and
	 * Unix based systems to access shell accounts, SSH was
	 * designed as a replacement for Telnet and other insecure
	 * remote shells, which send information, notably passwords,
	 * in plaintext, rendering them susceptible to packet
	 * analyzation. The encryption used by SSH provides
	 * confidentiality and integrity of data over an insecure
	 * network, such as the Internet.
	 */
	SSH {
		@Override
		public boolean isFileBasedScheme() {
			return true;
		}
	},

	/**
	 * The jar scheme describes a Java ARchive (JAR) file or
	 * an entry in a JAR file. The syntax of a JAR URL is:
	 * <pre><code>
	 * jar:&lt;url&gt;!/{entry}
	 * </code></pre>
	 * where <code>&lt;url&gt;</code> is an URL of the JAR file,
	 * and <code>{entry}</code> is the absolute path of a file
	 * inside the JAR file; for example:
	 * <pre><code>
	 * jar:http://www.foo.com/bar/baz.jar!/COM/foo/Quux.class
	 * </code></pre>
	 *
	 * <p>Jar URLs should be used to refer to a JAR file or
	 * entries in a JAR file. The example above is a JAR URL
	 * which refers to a JAR entry. If the entry name is
	 * omitted, the URL refers to the whole JAR file:
	 * <pre><code>
	 * jar:http://www.foo.com/bar/baz.jar!/
	 * </code></pre>
	 *
	 * <p>When constructing a JAR url via new URL(context, spec),
	 * the following rules apply:
	 * <ul>
	 * <li>if there is no context URL and the specification passed
	 * to the URL constructor doesn't contain a separator, the
	 * URL is considered to refer to a JarFile.</li>
	 * <li>if there is a context URL, the context URL is assumed
	 * to refer to a JAR file or a Jar directory.</li>
	 * <li>if the specification begins with a '/', the Jar directory
	 * is ignored, and the spec is considered to be at the root
	 * of the Jar file.</li>
	 * </ul>
	 */
	JAR {
		@Override
		public boolean isFileBasedScheme() {
			return true;
		}
	},

	/**
	 * In the Java programming language a resource is a piece of data
	 * that can be accessed by the code of an application. An
	 * application can access its resources through Uniform Resource
	 * Locators (URL), like web resources, but the resources are
	 * usually contained within the JAR file(s) of the application, or
	 * inside one directory of the JVM class paths.
	 *
	 * <p>The resource scheme is simulator to the file scheme.
	 * It is used to point to files somewhere in the class paths.
	 * It is always absolute filename from one of the roots given
	 * in class paths.
	 * The syntax is again much like the http syntax, only omitting
	 * the port numbers, like this:
	 * <pre><code>
	 * resource:/org/arakhne/vmutil/resource.txt
	 * resource:/org/arakhne/vmutil/resource.jpg
	 * </code></pre>
	 *
	 * <p>Because the pathname here represents a path name in the
	 * class paths, the slashes are mandatory.
	 */
	RESOURCE {
		@Override
		public boolean isFileBasedScheme() {
			return true;
		}
	},

	/**
	 * This value indicates that the scheme is not recognized.
	 */
	UNSUPPORTED {
		@Override
		public boolean isFileBasedScheme() {
			return false;
		}
	};

	/** Replies the scheme string ended with a column character.
	 *
	 * @return the scheme string with a column.
	 */
	@Override
	@Pure
	public String toString() {
		return name().toLowerCase() + ":"; //$NON-NLS-1$
	}

	/** Replies if the given URL uses this scheme.
	 *
	 * @param url the URL to test.
	 * @return <code>true</code> if the url uses this scheme,
	 *     otherwise <code>false</code>.
	 */
	@Pure
	public boolean isURL(URL url) {
		return getSchemeType(url) == this;
	}

	/** Replies if the given URI uses this scheme.
	 *
	 * @param uri the URI to test.
	 * @return <code>true</code> if the uri uses this scheme,
	 *     otherwise <code>false</code>
	 */
	public boolean isURI(URI uri) {
		return getSchemeType(uri) == this;
	}

	/** Replies if the given string corresponds to this scheme.
	 *
	 * @param string the scheme to test.
	 * @return <code>true</code> if the string corresponds to this scheme,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean isScheme(String string) {
		return getSchemeType(string) == this;
	}

	/** Remove this URI scheme from the given string.
	 *
	 * @param string the string to change.
	 * @return the {@code string} without the URI scheme.
	 */
	@Pure
	public String removeScheme(String string) {
		if (string != null) {
			final int idx = string.indexOf(':');
			if (idx >= 0) {
				String result = string.substring(0, idx + 1);
				if (toString().equalsIgnoreCase(result)) {
					result = string.substring(idx + 1);
					if (result.startsWith("//")) { //$NON-NLS-1$
						return result.substring(2);
					}
					return result;
				}
			}
		}
		return string;
	}

	/** Remove any supported URI scheme from the given string.
	 *
	 * @param string the string to change.
	 * @return the {@code string} without the URI scheme.
	 */
	@Pure
	public static String removeAnyScheme(String string) {
		if (string != null) {
			final int idx = string.indexOf(':');
			if (idx >= 0) {
				String result = string.substring(0, idx + 1);
				for (final URISchemeType type : values()) {
					if (type.toString().equalsIgnoreCase(result)) {
						result = string.substring(idx + 1);
						if (result.startsWith("//")) { //$NON-NLS-1$
							return result.substring(2);
						}
						return result;
					}
				}
			}
		}
		return string;
	}

	@SuppressWarnings({"checkstyle:returncount", "checkstyle:npathcomplexity"})
	private static URISchemeType getSchemeType(String protocol) {
		String proto = protocol;
		final int idx = protocol.indexOf(':');
		if (idx >= 0) {
			proto = protocol.substring(0, idx);
		}
		if (proto == null
				|| "".equals(protocol) //$NON-NLS-1$
				|| "file".equalsIgnoreCase(proto)) { //$NON-NLS-1$
			return FILE;
		}
		if ("http".equalsIgnoreCase(proto)) { //$NON-NLS-1$
			return HTTP;
		}
		if ("https".equalsIgnoreCase(proto)) { //$NON-NLS-1$
			return HTTPS;
		}
		if ("ftp".equalsIgnoreCase(proto)) { //$NON-NLS-1$
			return FTP;
		}
		if ("mailto".equalsIgnoreCase(proto)) { //$NON-NLS-1$
			return MAILTO;
		}
		if ("news".equalsIgnoreCase(proto)) { //$NON-NLS-1$
			return NEWS;
		}
		if ("ssh".equalsIgnoreCase(proto) //$NON-NLS-1$
				|| "sftp".equalsIgnoreCase(proto)) { //$NON-NLS-1$
			return SSH;
		}
		if ("telnet".equalsIgnoreCase(proto)) { //$NON-NLS-1$
			return TELNET;
		}
		if ("jar".equalsIgnoreCase(proto)) { //$NON-NLS-1$
			return JAR;
		}
		if ("resource".equalsIgnoreCase(proto)) { //$NON-NLS-1$
			return RESOURCE;
		}
		return URISchemeType.UNSUPPORTED;
	}

	/** Replies the type of scheme for the given URL.
	 *
	 * @param url the url.
	 * @return the type of scheme for the given URL.
	 */
	@Pure
	public static URISchemeType getSchemeType(URL url) {
		if (url != null) {
			return getSchemeType(url.getProtocol());
		}
		return URISchemeType.UNSUPPORTED;
	}

	/** Replies the type of scheme for the given URI.
	 *
	 * @param uri the uri.
	 * @return the type of scheme for the given URI.
	 */
	@Pure
	public static URISchemeType getSchemeType(URI uri) {
		if (uri != null) {
			return getSchemeType(uri.getScheme());
		}
		return URISchemeType.UNSUPPORTED;
	}

	/** Replies if this URI scheme represents a local or remote file.
	 *
	 * @return <code>true</code> if this scheme is file-based,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public abstract boolean isFileBasedScheme();

	static {
		URLHandlerUtil.installArakhneHandlers();
	}

}

