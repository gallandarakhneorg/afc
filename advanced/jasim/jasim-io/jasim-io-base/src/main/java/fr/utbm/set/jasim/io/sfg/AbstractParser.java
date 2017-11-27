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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.arakhne.afc.vmutil.FileSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.io.AbsolutePathBuilder;

/**
 * Parse an XML file to extract simulation parameters.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
abstract class AbstractParser implements XMLSimulationConfigConstants {

	/**
	 * Tool to make absolute paths according to a given current directory.
	 */
	protected final AbsolutePathBuilder pathBuilder = new AbsolutePathBuilder();
	
	private final SFGFileType fileType;
	
	/**
	 * @param type is the current type of the file to parse.
	 */
	public AbstractParser(SFGFileType type) {
		assert(type!=null);
		this.fileType = type;
	}
	
	/** Set the default directory used to retreive absolute filenames
	 * from a relative filename.
	 * If the given directory is <code>null</code>, the default user
	 * home directory will be use insteed.
	 * 
	 * @param defaultDirectory
	 */
	public void setDefaultDirectory(File defaultDirectory) {
		this.pathBuilder.setCurrentDirectory(defaultDirectory);
	}
	
	/** Set the default directory used to retreive absolute filenames
	 * from a relative filename.
	 * If the given directory is <code>null</code>, the default user
	 * home directory will be use insteed.
	 * 
	 * @param defaultDirectory
	 */
	public void setDefaultDirectory(URL defaultDirectory) {
		this.pathBuilder.setCurrentDirectory(defaultDirectory);
	}

	/** Replies if the currently parsed document uses a syntaxe version
	 * greater or equals to the given one.
	 * 
	 * @param major
	 * @param minor
	 * @return <code>true</code> if the used version in file is greater or equals
	 * to the given one, otherwise <code>false</code>
	 */
	protected final boolean isXMLVersion(int major, int minor) {
		int smajor = this.fileType.getSupportedMajorVersion();
		int sminor = this.fileType.getSupportedMinorVersion();
		return ((smajor>major)
				||
				((smajor==major)&&(sminor>=minor)));
	}

	/** Replies if the currently parsed document uses a syntaxe version
	 * greater or equals to the given one.
	 * 
	 * @param version
	 * @return <code>true</code> if the used version in file is greater or equals
	 * to the given one, otherwise <code>false</code>
	 */
	protected final boolean isXMLVersion(double version) {
		double minor = version - (int)version;
		while (minor!=(int)minor) {
			minor *= 10.;
		}
		return isXMLVersion((int)version, (int)minor);
	}
	
	/** Replies if the given XML node is activated or not.
	 * <p>
	 * An XML node is activated if its "activate" attribute
	 * is <code>true</code>. If a node is activated, it is
	 * parsed. If a node is not activated, it is skipped.
	 * 
	 * @param node
	 * @return <code>true</code> if the given XML node is
	 * activated, otherwise <code>false</code>
	 */
	protected static boolean isActivatedNode(Element node) {
		try {
			return getOptBoolean(node, XML_ATTR_ACTIVATE, true);
		}
		catch(IOException _) {
			return true;
		}
	}

	/** Parse the XML document.
	 * 
	 * @param document
	 * @return the parameter set
	 * @throws IOException in case of input error.
	 */
	public abstract SimulationParameterSet parse(Document document) throws IOException;
	
	/** Replies the first element in the given node that has the given name.
	 *
	 * @param node
	 * @param name
	 * @return the element or <code>null</code>
	 */
	protected static Element getFirstXMLChild(Node node, String name) {
		assert(name!=null);
		assert(node!=null);
		NodeList children = node.getChildNodes();
		Node child;
		Element element;
		for(int idx=0; idx<children.getLength(); ++idx) {
			child = children.item(idx);
			if (child instanceof Element) {
				element = (Element)child;
				if (name.equalsIgnoreCase(element.getNodeName())) {
					return element;
				}
			}
		}
		return null;
	}
	
	/** Replies the elements in the given node that has the given name.
	 *
	 * @param node
	 * @param name
	 * @return the elements, never <code>null</code>
	 */
	protected static List<Element> getXMLChildren(Node node, String name) {
		assert(name!=null);
		assert(node!=null);
		NodeList children = node.getChildNodes();
		List<Element> list = new ArrayList<Element>(children.getLength());
		Node child;
		Element element;
		for(int idx=0; idx<children.getLength(); ++idx) {
			child = children.item(idx);
			if (child instanceof Element) {
				element = (Element)child;
				if (name.equalsIgnoreCase(element.getNodeName())) {
					list.add(element);
				}
			}
		}
		return list;
	}
	
	/** Replies the Date in the given attribute.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @return the Date or <code>null</code>
	 */	
	protected static Date getMandatoryDate(Element node, String attributeName) {
		assert(node!=null);
		String xmlDate = node.getAttribute(attributeName);
		return XMLSimulationParserUtil.parseDate(xmlDate);
	}
	
	/** Replies the Date in the given attribute.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @param defaultValue is the value replies if no attribute value was found in XML node.
	 * @return the Date or <code>null</code>
	 * @throws IOException
	 */	
	protected static Date getOptDate(Element node, String attributeName, Date defaultValue) throws IOException {
		if (node!=null) {
			String xmlDate = node.getAttribute(attributeName);
			if (xmlDate!=null && !"".equals(xmlDate)) { //$NON-NLS-1$
				try {
					return XMLSimulationParserUtil.parseDate(xmlDate);
				}
				catch(Throwable e) {
					throw new IOException(e);
				}
			}
		}
		return defaultValue;
	}

	/** Replies the URL in the given attribute.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @param defaultValue is the URL replied if no URL could be extracted from XML node.
	 * @return the URL or <code>null</code>
	 */
	protected URL getOptURL(Element node, String attributeName, URL defaultValue) {
		if (node!=null) {
			String str = node.getAttribute(attributeName);
			return this.pathBuilder.makeAbsolute(str);
		}
		return defaultValue;
	}

	/** Replies the URL in the given attribute.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @return the URL or <code>null</code>
	 * @throws IOException 
	 */
	protected URL getMandatoryURL(Element node, String attributeName) throws IOException {
		assert(node!=null);
		String str = node.getAttribute(attributeName);
		URL lurl = FileSystem.convertStringToURL(str, true);
		if (lurl==null) throw new IOException("no attribute found '"+attributeName+"' in node '"+node.getNodeName()+"', or the given resource is unloadable"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return this.pathBuilder.makeAbsolute(lurl);
	}

	/** Replies the class in the given attribute.
	 * 
	 * @param <T>
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @return the class or <code>null</code>
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	protected static <T> Class<T> getMandatoryClass(Element node, String attributeName) throws IOException {
		assert(node!=null);
		String str = node.getAttribute(attributeName);
		if ((str!=null)&&(!"".equals(str))) { //$NON-NLS-1$
			try {
				return (Class<T>)Class.forName(str);
			}
			catch(Exception e) {
				throw new IOException("invalid attribute value for '"+attributeName+"' in node '"+node.getNodeName()+"': "+str, e); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
		throw new IOException("no attribute found '"+attributeName+"' in node '"+node.getNodeName()+"'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/** Replies the the given attribute.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @return the string, never <code>null</code>
	 * @throws IOException 
	 */
	protected static String getMandatoryString(Element node, String attributeName) throws IOException {
		assert(node!=null);
		String str = node.getAttribute(attributeName);
		if ((str!=null)&&(!"".equals(str))) { //$NON-NLS-1$
			return str;
		}
		throw new IOException("no attribute found '"+attributeName+"' in node '"+node.getNodeName()+"'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/** Replies the the given attribute.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @return the value, never <code>null</code>
	 * @throws IOException 
	 */
	protected static UUID getMandatoryUUID(Element node, String attributeName) throws IOException {
		assert(node!=null);
		String str = node.getAttribute(attributeName);
		if ((str!=null)&&(!"".equals(str))) { //$NON-NLS-1$
			return UUID.fromString(str);
		}
		throw new IOException("no attribute found '"+attributeName+"' in node '"+node.getNodeName()+"'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/** Extract optional 1D direction.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @return the class or <code>null</code>
	 * @throws IOException 
	 */
	protected static Direction1D getOptDirection1D(Element node, String attributeName) throws IOException {
		Direction1D direction = Direction1D.BOTH_DIRECTIONS;
		String strDirection = getOptString(node, attributeName, null);
		if (XML_VALUE_DEFAULT_DIRECTION.equalsIgnoreCase(strDirection)) {
			direction = Direction1D.SEGMENT_DIRECTION;
		}
		else if (XML_VALUE_REVERSED_DIRECTION.equalsIgnoreCase(strDirection)) {
			direction = Direction1D.REVERTED_DIRECTION;
		}
		return direction;
	}

	/** Extract mandatory 1D direction.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @return the class or <code>null</code>
	 * @throws IOException 
	 */
	protected static Direction1D getMandatoryDirection1D(Element node, String attributeName) throws IOException {
		assert(node!=null);
		Direction1D direction = Direction1D.BOTH_DIRECTIONS;
		String strDirection = getMandatoryString(node, attributeName);
		if (XML_VALUE_DEFAULT_DIRECTION.equalsIgnoreCase(strDirection)) {
			direction = Direction1D.SEGMENT_DIRECTION;
		}
		else if (XML_VALUE_REVERSED_DIRECTION.equalsIgnoreCase(strDirection)) {
			direction = Direction1D.REVERTED_DIRECTION;
		}
		return direction;
	}

	/** Replies the class in the given attribute.
	 * 
	 * @param <T>
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @return the class or <code>null</code>
	 * @throws IOException 
	 */
	protected static <T> Class<T> getOptClass(Element node, String attributeName) throws IOException {
		return getOptClass(node, attributeName, null);
	}

	/** Replies the class in the given attribute.
	 * 
	 * @param <T>
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @param defaultValue is the default value to reply of the attribute was not found.
	 * @return the class or <code>null</code>
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	protected static <T> Class<T> getOptClass(Element node, String attributeName, Class<T> defaultValue) throws IOException {
		if (node!=null) {
			String str = node.getAttribute(attributeName);
			if ((str!=null)&&(!"".equals(str))) { //$NON-NLS-1$
				try {
					return (Class<T>)Class.forName(str);
				}
				catch(Exception _) {
					//
				}
			}
		}
		return defaultValue;
	}

	/** Replies the double in the given attribute.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @return the value
	 * @throws IOException 
	 */
	protected static double getMandatoryDouble(Element node, String attributeName) throws IOException {
		assert(node!=null);
		String str = node.getAttribute(attributeName);
		if ((str!=null)&&(!"".equals(str))) { //$NON-NLS-1$
			try {
				return Double.parseDouble(str);
			}
			catch(Exception _) {
				//
			}
		}
		throw new IOException("no attribute found '"+attributeName+"' in node '"+node.getNodeName()+"'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/** Replies the byte in the given attribute.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @return the value
	 * @throws IOException 
	 */
	protected static byte getMandatoryByte(Element node, String attributeName) throws IOException {
		assert(node!=null);
		String str = node.getAttribute(attributeName);
		if ((str!=null)&&(!"".equals(str))) { //$NON-NLS-1$
			try {
				return Byte.parseByte(str);
			}
			catch(Exception _) {
				//
			}
		}
		throw new IOException("no attribute found '"+attributeName+"' in node '"+node.getNodeName()+"'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/** Replies the int in the given attribute.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @return the value
	 * @throws IOException 
	 */
	protected static int getMandatoryInteger(Element node, String attributeName) throws IOException {
		assert(node!=null);
		String str = node.getAttribute(attributeName);
		if ((str!=null)&&(!"".equals(str))) { //$NON-NLS-1$
			try {
				return Integer.parseInt(str);
			}
			catch(Exception _) {
				//
			}
		}
		throw new IOException("no attribute found '"+attributeName+"' in node '"+node.getNodeName()+"'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/** Extract a position from the XML.
	 * A position could be:<ul>
	 * <li>1D: <code>x</code>, <code>road</code>;
	 * <li>1.5D: <code>x</code>, <code>y</code>, <code>road</code>;
	 * <li>2D: <code>x</code>, <code>y</code>;
	 * <li>2.5D: <code>x</code>, <code>y</code>, <code>z</code>;
	 * <li>3D: <code>x</code>, <code>y</code>, <code>z</code>;
	 * </ul>
	 * 
	 * @param currentParameter is the parameter for which the point must be extracted.
	 * @param node is the node to explore
	 * @return the position description
	 * @throws IOException 
	 */
	protected static PositionParameter getMandatoryPosition(AbstractParameter currentParameter, Element node) throws IOException {
		return getMandatoryPosition(currentParameter, node, getOptUUID(node, XML_ATTR_PLACE, null));
	}

	/** Extract a position from the XML.
	 * A position could be:<ul>
	 * <li>1D: <code>x</code>, <code>road</code>;
	 * <li>1.5D: <code>x</code>, <code>y</code>, <code>road</code>;
	 * <li>2D: <code>x</code>, <code>y</code>;
	 * <li>2.5D: <code>x</code>, <code>y</code>, <code>z</code>;
	 * <li>3D: <code>x</code>, <code>y</code>, <code>z</code>;
	 * </ul>
	 * 
	 * @param currentParameter is the parameter for which the point must be extracted.
	 * @param node is the node to explore
	 * @param placeId is identifier of the place where the position is located.
	 * @return the position description
	 * @throws IOException 
	 */
	protected static PositionParameter getMandatoryPosition(AbstractParameter currentParameter, Element node, UUID placeId) throws IOException {
		assert(node!=null);
		float dimension = currentParameter.getEnvironmentDimension();
		PositionParameter position;
		if (dimension==1f) {
			double x = getMandatoryDouble(node, XML_ATTR_X);
			String road = getMandatoryString(node, XML_ATTR_ROAD);
			position = new PositionParameter(road,x);
		}		
		else if (dimension==1.5f) {
			double x = getMandatoryDouble(node, XML_ATTR_X);
			double y = getOptDouble(node, XML_ATTR_Y, 0.);
			String road = getMandatoryString(node, XML_ATTR_ROAD);
			position = new PositionParameter(road,x,y);
		}		
		else if (dimension==2f) {
			double x = getMandatoryDouble(node, XML_ATTR_X);
			double y = getMandatoryDouble(node, XML_ATTR_Y);
			position = new PositionParameter(x,y);
		}
		else {
			double x = getMandatoryDouble(node, XML_ATTR_X);
			double y = getMandatoryDouble(node, XML_ATTR_Y);
			double z = getMandatoryDouble(node, XML_ATTR_Z);
			position = new PositionParameter(x,y,z);
		}
		
		position.setPlace(placeId);
		
		return position;
	}

	/** Replies the double in the given attribute.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @param defaultValue is the default value replies if the attribute value was not specified in the XML file.
	 * @return the value
	 * @throws IOException 
	 */
	protected static double getOptDouble(Element node, String attributeName, double defaultValue) throws IOException {
		if (node!=null) {
			String str = node.getAttribute(attributeName);
			if ((str!=null)&&(!"".equals(str))) { //$NON-NLS-1$
				try {
					return Double.parseDouble(str);
				}
				catch(Exception e) {
					throw new IOException("invalid attribute syntax for '"+attributeName+"'",e); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		return defaultValue;
	}

	/** Replies the String in the given attribute.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @param defaultValue is the default value replies if the attribute value was not specified in the XML file.
	 * @return the value
	 * @throws IOException 
	 */
	protected static String getOptString(Element node, String attributeName, String defaultValue) throws IOException {
		if (node!=null) {
			if (node.hasAttribute(attributeName)) {
				String str = node.getAttribute(attributeName);
				if (str!=null) return str;
			}
		}
		return defaultValue;
	}

	/** Replies the UUID in the given attribute.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @param defaultValue is the default value replies if the attribute value was not specified in the XML file.
	 * @return the value
	 * @throws IOException 
	 */
	protected static UUID getOptUUID(Element node, String attributeName, UUID defaultValue) throws IOException {
		if (node!=null && node.hasAttribute(attributeName)) {
			String str = node.getAttribute(attributeName);
			if (str!=null) {
				try {
					return UUID.fromString(str);
				}
				catch(Throwable _) {
					//
				}
			}
		}
		return defaultValue;
	}

	/** Replies the boolean in the given attribute.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @param defaultValue is the default value replies if the attribute value was not specified in the XML file.
	 * @return the value
	 * @throws IOException 
	 */
	protected static boolean getOptBoolean(Element node, String attributeName, boolean defaultValue) throws IOException {
		if (node!=null) {
			String str = node.getAttribute(attributeName);
			if ((str!=null)&&(!"".equals(str))) { //$NON-NLS-1$
				try {
					return Boolean.parseBoolean(str);
				}
				catch(Exception e) {
					throw new IOException("invalid attribute syntax for '"+attributeName+"'",e); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		return defaultValue;
	}

	/** Replies the long in the given attribute.
	 * 
	 * @param node is the element from which the attribute must be red.
	 * @param attributeName is the name of the attribute to read.
	 * @param defaultValue is the default value replies if the attribute value was not specified in the XML file.
	 * @return the value
	 * @throws IOException 
	 */
	protected static Long getOptLong(Element node, String attributeName, Long defaultValue) throws IOException {
		if (node!=null) {
			String str = node.getAttribute(attributeName);
			if ((str!=null)&&(!"".equals(str))) { //$NON-NLS-1$
				try {
					return Long.parseLong(str);
				}
				catch(Exception e) {
					throw new IOException("invalid attribute syntax for '"+attributeName+"'",e); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		return defaultValue;
	}
		
}
