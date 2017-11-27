/*
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.io.sfg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.arakhne.afc.vmutil.FileSystem;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import fr.utbm.set.io.xml.DefaultXMLEntityResolver;

/**
 * Parse an XML file to extract simulation parameters.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class XMLSimulationConfigParser implements XMLSimulationConfigConstants {

	/** Parse the resource and replied the DOM tree.
	 *
	 * @param inputStream is the stream to read.
	 * @return the DOM tree
	 * @throws IOException in case of input error.
	 */
	public static Document readDOM(InputStream inputStream) throws IOException {
		// Create the XML parser
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e) {
			throw new IOException(e);
		}
		
		// Force the builder to use the entity resolver devoted
		// to the support of GIP's dtd.
		factory.setValidating(true);
		builder.setEntityResolver(new XMLEntityResolver());
		builder.setErrorHandler(new XMLErrorHandler());
		
		// Read the input stream and extract the XML tree
		Document xmlDocument;		
		try {
			xmlDocument = builder.parse(inputStream);
		}
		catch (SAXException e) {
			throw new IOException(e);
		}
		inputStream.close();

		return xmlDocument;
	}

	private final URL url;
	private final File file;
	private final InputStream stream;
	private URL defaultDirectory = null;
	
	/**
	 * @param resource is the path to the XML resource.
	 */
	public XMLSimulationConfigParser(File resource) {
		this.url = null;
		this.file = resource;
		this.stream = null;
	}
	
	/**
	 * @param resource is the path to the XML resource.
	 */
	public XMLSimulationConfigParser(URL resource) {
		this.url = resource;
		this.file = null;
		this.stream = null;
	}

	/**
	 * @param resource is the path to the XML resource.
	 */
	public XMLSimulationConfigParser(InputStream resource) {
		this.url = null;
		this.file = null;
		this.stream = resource;
	}
	
	/** Set the default directory used to retreive absolute filenames
	 * from a relative filename.
	 * If the given directory is <code>null</code>, the default user
	 * home directory will be use insteed.
	 * 
	 * @param defaultDirectory
	 * @throws MalformedURLException when no URL for the given file is avalaible. 
	 */
	public void setDefaultDirectory(File defaultDirectory) throws MalformedURLException {
		setDefaultDirectory(defaultDirectory.toURI().toURL());
	}

	/** Set the default directory used to retreive absolute filenames
	 * from a relative filename.
	 * If the given directory is <code>null</code>, the default user
	 * home directory will be use insteed.
	 * 
	 * @param defaultDirectory
	 */
	public void setDefaultDirectory(URL defaultDirectory) {
		this.defaultDirectory = defaultDirectory;
	}
	
	/** Replies the default directory used to retreive absolute filenames
	 * from a relative filename.
	 * 
	 * @return the default directory, never <code>null</code>
	 */
	public URL getDefaultDirectory() {
		if (this.defaultDirectory==null) {
			try {
				return FileSystem.getUserHomeDirectory().toURI().toURL();
			}
			catch (Exception _) {
				try {
					return new File(System.getProperty("user.home")).toURI().toURL(); //$NON-NLS-1$
				}
				catch(MalformedURLException __) {
					//
				}
			}
		}
		return this.defaultDirectory;
	}
	
	/** Parse the resource.
	 * 
	 * @return the parameter set
	 * @throws IOException in case of input error.
	 */
	public SimulationParameterSet parse() throws IOException {
		// Open the stream
		InputStream inputStream = this.stream;
		if (inputStream==null) {
			if (this.url!=null) inputStream = this.url.openStream();
			if (inputStream==null) {
				assert(this.file!=null);
				inputStream = new FileInputStream(this.file);
			}
		}
		
		Document xmlDocument = readDOM(inputStream); 
		
		DocumentType type = xmlDocument.getDoctype();
		String systemId = type.getSystemId();
		
		AbstractParser aParser = null;
		
		SFGFileType fileType = SFGFileType.fromSystemId(systemId);
		
		if (fileType!=null) {
			aParser = createParser(fileType);
		}
		
		if (aParser==null) throw new IOException("Cannot find a parser for system id: "+systemId); //$NON-NLS-1$
		
		aParser.setDefaultDirectory(getDefaultDirectory());
		return aParser.parse(xmlDocument);
	}
	
	/** Create a parser.
	 * 
	 * @param fileType is the type of file to read.
	 * @return a parser.
	 */
	protected abstract AbstractParser createParser(SFGFileType fileType);
	
	/**
	 * This class provides error handler on XML.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class XMLErrorHandler implements ErrorHandler {

		/**
		 */
		public XMLErrorHandler() {
			//
		}

		/** {@inheritDoc}
		 */
		@Override
		public void error(SAXParseException exception) throws SAXException {
			throw exception;
		}

		/** {@inheritDoc}
		 */
		@Override
		public void fatalError(SAXParseException exception) throws SAXException {
			throw exception;			
		}

		/** {@inheritDoc}
		 */
		@Override
		public void warning(SAXParseException exception) throws SAXException {
			throw exception;			
		}
		
	}
	
	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class XMLEntityResolver extends DefaultXMLEntityResolver {

		/**
		 */
		public XMLEntityResolver() {
			//
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			if (systemId!=null && !"".equals(systemId)) { //$NON-NLS-1$
				String sId = systemId;
				if (sId.contains("/fr/utbm/set/jasim/controller/config/")) { //$NON-NLS-1$
					sId = systemId.replace(
							"/fr/utbm/set/jasim/controller/config/", //$NON-NLS-1$
							"/fr/utbm/set/jasim/io/sfg/"); //$NON-NLS-1$
				}
				return super.resolveEntity(publicId, sId);
			}
			return null;
		}
		
	}

}
