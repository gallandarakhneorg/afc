/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.attrs.xml;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import org.w3c.dom.Element;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.inputoutput.path.PathBuilder;
import org.arakhne.afc.inputoutput.xml.DateFormatException;
import org.arakhne.afc.inputoutput.xml.XMLBuilder;
import org.arakhne.afc.inputoutput.xml.XMLResources;
import org.arakhne.afc.inputoutput.xml.XMLUtil;

/**
 * This class provides XML utilities related to attributes.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public final class XMLAttributeUtil {

	/** <code>&lt;attribute /&gt;</code>. */
	public static final String NODE_ATTRIBUTE = "attribute"; //$NON-NLS-1$

	/** <code>&lt;attributes /&gt;</code>. */
	public static final String NODE_ATTRIBUTES = "attributes"; //$NON-NLS-1$

	/** <code>geoid=""</code>. */
	public static final String ATTR_GEOID = "geoId"; //$NON-NLS-1$

	/** <code>type=""</code>. */
	public static final String ATTR_TYPE = "type"; //$NON-NLS-1$

	/** <code>value=""</code>. */
	public static final String ATTR_VALUE = "value"; //$NON-NLS-1$

	private XMLAttributeUtil() {
		//
	}

	/** Put in the given XML element the attributes stored in the given container.
	 * This function ignores the attributes with the names "id", "name", "color",
	 * "icon", and "geoId" if the parameter <var>writeStandardAttribute</var>
	 * is <code>false</code>.
	 *
	 * @param element is the XML element to fill.
	 * @param container is the container of attributes.
	 * @param builder is the tool to create XML nodes.
	 * @param resources is the tool that permits to gather the resources.
	 * @param writeStandardAttribute indicates if the attributes "id", "name", "color",
	 *     and "geoId" should be write or not.
	 */
	@SuppressWarnings("checkstyle:cyclomaticcomplexity")
	public static void writeAttributeContainer(Element element, AttributeProvider container, XMLBuilder builder,
			XMLResources resources, boolean writeStandardAttribute) {
		final Element attrsNode = builder.createElement(NODE_ATTRIBUTES);
		for (final Attribute attr : container.attributes()) {
			final AttributeType attrType = attr.getType();
			if (attr.isAssigned()) {
				try {
					final String name = attr.getName();
					if (writeStandardAttribute
						|| (!XMLUtil.ATTR_ID.equalsIgnoreCase(name)
								&& !XMLUtil.ATTR_NAME.equalsIgnoreCase(name)
								&& !XMLUtil.ATTR_COLOR.equalsIgnoreCase(name)
								&& !ATTR_GEOID.equalsIgnoreCase(name))) {
						final Element attrNode;
						switch (attrType) {
						case DATE:
							attrNode = builder.createElement(NODE_ATTRIBUTE);
							attrNode.setAttribute(XMLUtil.ATTR_NAME, attr.getName());
							attrNode.setAttribute(ATTR_TYPE, attrType.name());
							attrNode.setAttribute(ATTR_VALUE, XMLUtil.toString(attr.getDate()));
							break;
						case URL:
							attrNode = builder.createElement(NODE_ATTRIBUTE);
							attrNode.setAttribute(XMLUtil.ATTR_NAME, attr.getName());
							attrNode.setAttribute(ATTR_TYPE, attrType.name());
							attrNode.setAttribute(ATTR_VALUE, resources.add(attr.getURL()));
							break;
							//$CASES-OMITTED$
						default:
							final String value = attr.getString();
							if (value != null && !"".equals(value)) { //$NON-NLS-1$
								attrNode = builder.createElement(NODE_ATTRIBUTE);
								attrNode.setAttribute(XMLUtil.ATTR_NAME, attr.getName());
								attrNode.setAttribute(ATTR_VALUE, value);
								attrNode.setAttribute(ATTR_TYPE, attrType.name());
								attrsNode.appendChild(attrNode);
							}
						}
					}
				} catch (AssertionError e) {
					throw e;
				} catch (Throwable exception) {
					//
				}
			}
		}
		if (attrsNode.getChildNodes().getLength() > 0) {
			element.appendChild(attrsNode);
		}
	}

	/** Put the attributes in the given container from the given XML element.
	 * This function ignores the attributes with the names "id", "name", "color",
	 * and "geoId" if the parameter <var>readStandardAttribute</var>
	 * is <code>false</code>.
	 *
	 * @param element is the XML element to fill.
	 * @param container is the container of attributes.
	 * @param pathBuilder is the tool to make paths absolute.
	 * @param resources is the tool that permits to gather the resources.
	 * @param readStandardAttribute indicates if the attributes "id", "name", "color",
	 *     and "geoId" should be write or not.
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:nestedifdepth"})
	public static void readAttributeContainer(Element element, AttributeCollection container,
			PathBuilder pathBuilder, XMLResources resources, boolean readStandardAttribute) throws IOException {
		for (final Element attrNode : XMLUtil.getElementsFromPath(element, NODE_ATTRIBUTES, NODE_ATTRIBUTE)) {
			final String name = XMLUtil.getAttributeValue(attrNode, XMLUtil.ATTR_NAME);
			if (name != null && (readStandardAttribute
					|| (!"".equals(name) //$NON-NLS-1$
							&& !XMLUtil.ATTR_ID.equalsIgnoreCase(name)
							&& !XMLUtil.ATTR_NAME.equalsIgnoreCase(name)
							&& !XMLUtil.ATTR_COLOR.equalsIgnoreCase(name)
							&& !ATTR_GEOID.equalsIgnoreCase(name)))) {
				final String type = XMLUtil.getAttributeValue(attrNode, ATTR_TYPE);
				if (type != null && !"".equals(type)) { //$NON-NLS-1$
					AttributeType attrType;
					try {
						attrType = AttributeType.valueOf(type);
					} catch (Throwable exception) {
						attrType = null;
					}
					if (attrType != null) {
						final String value;
						switch (attrType) {
						case DATE:
							value = XMLUtil.getAttributeValue(attrNode, ATTR_VALUE);
							if (value != null && !"".equals(value)) { //$NON-NLS-1$
								try {
									final Date d = XMLUtil.parseDate(value);
									final AttributeImpl attr = new AttributeImpl(name, d);
									container.setAttribute(attr);
								} catch (DateFormatException e) {
									throw new IOException(e);
								} catch (AttributeException e) {
									throw new IOException(e);
								}
							}
							break;
						case URL:
							value = XMLUtil.getAttributeValue(attrNode, ATTR_VALUE);
							if (value != null && !"".equals(value)) { //$NON-NLS-1$
								try {
									final long id = XMLResources.getNumericalIdentifier(value);
									final URL url = resources.getResourceURL(id);
									if (url != null) {
										final AttributeImpl attr = new AttributeImpl(name, url);
										container.setAttribute(attr);
									}
								} catch (IllegalArgumentException e) {
									throw new IOException(e);
								} catch (AttributeException e) {
									throw new IOException(e);
								}
							}
							break;
							//$CASES-OMITTED$
						default:
							value = XMLUtil.getAttributeValue(attrNode, ATTR_VALUE);
							if (value != null && !"".equals(value)) { //$NON-NLS-1$
								final AttributeImpl attr = new AttributeImpl(name, value);
								attr.cast(attrType);
								try {
									container.setAttribute(attr);
								} catch (AttributeException e) {
									throw new IOException(e);
								}
							}
							break;
						}
					}
				}
			}
		}
	}

}
