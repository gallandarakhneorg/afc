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

package org.arakhne.afc.inputoutput.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import org.arakhne.afc.vmutil.asserts.AssertMessages;

/**
 * This interface permits to create several XML elements.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface XMLBuilder {

	/** Replies the document associated to this builder.
	 *
	 * @return the document associated to this builder.
	 * @throws DOMException if it is impossible to get the document.
	 */
	Document getDocument() throws DOMException;

	/**
	 * Creates an element of the type specified. Note that the instance
	 * returned implements the {@code Element} interface, so attributes
	 * can be specified directly on the returned object.
	 * <br>In addition, if there are known attributes with default values,
	 * {@code Attr} nodes representing them are automatically created
	 * and attached to the element.
	 * <br>To create an element with a qualified name and namespace URI, use
	 * the {@code createElementNS} method.
	 * @param tagName The name of the element type to instantiate. For XML,
	 *     this is case-sensitive, otherwise it depends on the
	 *     case-sensitivity of the markup language in use. In that case, the
	 *     name is mapped to the canonical form of that markup by the DOM
	 *     implementation.
	 * @return A new {@code Element} object with the
	 *   {@code nodeName} attribute set to {@code tagName}, and
	 *   {@code localName}, {@code prefix}, and
	 *   {@code namespaceURI} set to {@code null}.
	 * @throws DOMException
	 *     INVALID_CHARACTER_ERR: Raised if the specified name is not an XML
	 *     name according to the XML version in use specified in the
	 *     {@code Document.xmlVersion} attribute.
	 */
	default Element createElement(String tagName) throws DOMException {
		assert tagName != null && !tagName.isEmpty() : AssertMessages.notNullParameter();
		return getDocument().createElement(tagName);
	}

	/**
	 * Creates an empty {@code DocumentFragment} object.
	 * @return A new {@code DocumentFragment}.
	 */
	default DocumentFragment createDocumentFragment() {
		return getDocument().createDocumentFragment();
	}

	/**
	 * Creates a {@code Text} node given the specified string.
	 * @param data The data for the node.
	 * @return The new {@code Text} object.
	 * @see #createTextElement(String, String)
	 */
	default Text createTextNode(String data) {
		return getDocument().createTextNode(data);
	}

	/**
	 * Creates an element that contains the given text.
	 *
	 * <p>Note that the instance
	 * returned implements the {@code Element} interface, so attributes
	 * can be specified directly on the returned object.
	 * <br>In addition, if there are known attributes with default values,
	 * {@code Attr} nodes representing them are automatically created
	 * and attached to the element.
	 *
	 * <p>This function is equivalent to:
	 * <pre>{@code 
	 * Element e = createElement(tagName);
	 * Text t = createText(data);
	 * e.appendChild(t);
	 * return e;
	 * }</pre>
	 *
	 * @param tagName The name of the element type to instantiate. For XML,
	 *     this is case-sensitive, otherwise it depends on the
	 *     case-sensitivity of the markup language in use. In that case, the
	 *     name is mapped to the canonical form of that markup by the DOM
	 *     implementation.
	 * @param data The data for the node.
	 * @return the Element with the Text inside.
	 * @see #createTextNode(String)
	 * @see #createElement(String)
	 */
	default Element createTextElement(String tagName, String data) {
		assert tagName != null && !tagName.isEmpty() : AssertMessages.notNullParameter();
		final Document doc = getDocument();
		final Element e = doc.createElement(tagName);
		e.appendChild(doc.createTextNode(data));
		return e;
	}

	/**
	 * Creates a {@code Comment} node given the specified string.
	 * @param data The data for the node.
	 * @return The new {@code Comment} object.
	 */
	default Comment createComment(String data) {
		return getDocument().createComment(data);
	}

	/**
	 * Creates a {@code CDATASection} node whose value is the specified
	 * string.
	 * @param data The data for the {@code CDATASection} contents.
	 * @return The new {@code CDATASection} object.
	 * @throws DOMException
	 *   NOT_SUPPORTED_ERR: Raised if this document is an HTML document.
	 */
	default CDATASection createCDATASection(String data) throws DOMException {
		return getDocument().createCDATASection(data);
	}

	/**
	 * Creates a {@code ProcessingInstruction} node given the specified
	 * name and data strings.
	 * @param target The target part of the processing instruction.Unlike
	 *     {@code Document.createElementNS} or
	 *     {@code Document.createAttributeNS}, no namespace well-formed
	 *     checking is done on the target name. Applications should invoke
	 *     {@code Document.normalizeDocument()} with the parameter "
	 *     namespaces" set to {@code true} in order to ensure that the
	 *     target name is namespace well-formed.
	 * @param data The data for the node.
	 * @return The new {@code ProcessingInstruction} object.
	 * @throws DOMException
	 *     INVALID_CHARACTER_ERR: Raised if the specified target is not an XML
	 *     name according to the XML version in use specified in the
	 *     {@code Document.xmlVersion} attribute.
	 *     <br>NOT_SUPPORTED_ERR: Raised if this document is an HTML document.
	 */
	default ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
		assert target != null && !target.isEmpty() : AssertMessages.notNullParameter();
		return getDocument().createProcessingInstruction(target, data);
	}

	/**
	 * Creates an {@code Attr} of the given name. Note that the
	 * {@code Attr} instance can then be set on an {@code Element}
	 * using the {@code setAttributeNode} method.
	 * <br>To create an attribute with a qualified name and namespace URI, use
	 * the {@code createAttributeNS} method.
	 * @param name The name of the attribute.
	 * @return A new {@code Attr} object with the {@code nodeName}
	 *     attribute set to {@code name}, and {@code localName},
	 *     {@code prefix}, and {@code namespaceURI} set to
	 *     {@code null}. The value of the attribute is the empty string.
	 * @throws DOMException
	 *     INVALID_CHARACTER_ERR: Raised if the specified name is not an XML
	 *     name according to the XML version in use specified in the
	 *     {@code Document.xmlVersion} attribute.
	 */
	default Attr createAttribute(String name) throws DOMException {
		assert name != null && !name.isEmpty() : AssertMessages.notNullParameter();
		return getDocument().createAttribute(name);
	}

	/**
	 * Creates an {@code EntityReference} object. In addition, if the
	 * referenced entity is known, the child list of the
	 * {@code EntityReference} node is made the same as that of the
	 * corresponding {@code Entity} node.
	 *
	 * <p><b>Note:</b> If any descendant of the {@code Entity} node has
	 * an unbound namespace prefix, the corresponding descendant of the
	 * created {@code EntityReference} node is also unbound; (its
	 * {@code namespaceURI} is {@code null}). The DOM Level 2 and
	 * 3 do not support any mechanism to resolve namespace prefixes in this
	 * case.
	 * @param name The name of the entity to reference.Unlike
	 *     {@code Document.createElementNS} or
	 *     {@code Document.createAttributeNS}, no namespace well-formed
	 *     checking is done on the entity name. Applications should invoke
	 *     {@code Document.normalizeDocument()} with the parameter "
	 *     namespaces" set to {@code true} in order to ensure that the
	 *     entity name is namespace well-formed.
	 * @return The new {@code EntityReference} object.
	 * @throws DOMException
	 *     INVALID_CHARACTER_ERR: Raised if the specified name is not an XML
	 *     name according to the XML version in use specified in the
	 *     {@code Document.xmlVersion} attribute.
	 *     <br>NOT_SUPPORTED_ERR: Raised if this document is an HTML document.
	 */
	default EntityReference createEntityReference(String name) throws DOMException {
		assert name != null && !name.isEmpty() : AssertMessages.notNullParameter();
		return getDocument().createEntityReference(name);
	}

	/**
	 * Creates an element of the given qualified name and namespace URI.
	 * <br>Per [<a href='http://www.w3.org/TR/1999/REC-xml-names-19990114/'>XML Namespaces</a>]
	 * , applications must use the value {@code null} as the
	 * namespaceURI parameter for methods if they wish to have no namespace.
	 * @param namespaceURI The namespace URI of the element to create.
	 * @param qualifiedName The qualified name of the element type to
	 *     instantiate.
	 * @return A new {@code Element} object with the following
	 *     attributes:
	 * <table border='1' cellpadding='3' summary="Attributes">
	 * <tr>
	 * <th>Attribute</th>
	 * <th>Value</th>
	 * </tr>
	 * <tr>
	 * <td valign='top' rowspan='1' colspan='1'>{@code Node.nodeName}</td>
	 * <td valign='top' rowspan='1' colspan='1'>
	 *   {@code qualifiedName}</td>
	 * </tr>
	 * <tr>
	 * <td valign='top' rowspan='1' colspan='1'>{@code Node.namespaceURI}</td>
	 * <td valign='top' rowspan='1' colspan='1'>
	 *   {@code namespaceURI}</td>
	 * </tr>
	 * <tr>
	 * <td valign='top' rowspan='1' colspan='1'>{@code Node.prefix}</td>
	 * <td valign='top' rowspan='1' colspan='1'>prefix, extracted
	 *   from {@code qualifiedName}, or {@code null} if there is
	 *   no prefix</td>
	 * </tr>
	 * <tr>
	 * <td valign='top' rowspan='1' colspan='1'>{@code Node.localName}</td>
	 * <td valign='top' rowspan='1' colspan='1'>local name, extracted from
	 *   {@code qualifiedName}</td>
	 * </tr>
	 * <tr>
	 * <td valign='top' rowspan='1' colspan='1'>{@code Element.tagName}</td>
	 * <td valign='top' rowspan='1' colspan='1'>
	 *   {@code qualifiedName}</td>
	 * </tr>
	 * </table>
	 * @throws DOMException
	 *     INVALID_CHARACTER_ERR: Raised if the specified
	 *     {@code qualifiedName} is not an XML name according to the XML
	 *     version in use specified in the {@code Document.xmlVersion}
	 *     attribute.
	 *     <br>NAMESPACE_ERR: Raised if the {@code qualifiedName} is a
	 *     malformed qualified name, if the {@code qualifiedName} has a
	 *     prefix and the {@code namespaceURI} is {@code null}, or
	 *     if the {@code qualifiedName} has a prefix that is "xml" and
	 *     the {@code namespaceURI} is different from "<a href='http://www.w3.org/XML/1998/namespace'>
	 *     http://www.w3.org/XML/1998/namespace</a>" [<a href='http://www.w3.org/TR/1999/REC-xml-names-19990114/'>XML Namespaces</a>],
	 *     or if the {@code qualifiedName} or its prefix is "xmlns" and
	 *     the {@code namespaceURI} is different from "<a href='http://www.w3.org/2000/xmlns/'>http://www.w3.org/2000/xmlns/</a>", or if the {@code namespaceURI} is "<a href='http://www.w3.org/2000/xmlns/'>http://www.w3.org/2000/xmlns/</a>" and neither the {@code qualifiedName} nor its prefix is "xmlns".
	 *     <br>NOT_SUPPORTED_ERR: Always thrown if the current document does not
	 *     support the {@code "XML"} feature, since namespaces were
	 *     defined by XML.
	 * @since DOM Level 2
	 */
	default Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
		assert namespaceURI != null && !namespaceURI.isEmpty() : AssertMessages.notNullParameter();
		assert qualifiedName != null && !qualifiedName.isEmpty() : AssertMessages.notNullParameter();
		return getDocument().createElementNS(namespaceURI, qualifiedName);
	}

	/**
	 * Creates an attribute of the given qualified name and namespace URI.
	 * <br>Per [<a href='http://www.w3.org/TR/1999/REC-xml-names-19990114/'>XML Namespaces</a>]
	 * , applications must use the value {@code null} as the
	 * {@code namespaceURI} parameter for methods if they wish to have
	 * no namespace.
	 * @param namespaceURI The namespace URI of the attribute to create.
	 * @param qualifiedName The qualified name of the attribute to
	 *     instantiate.
	 * @return A new {@code Attr} object with the following attributes:
	 * <table border='1' cellpadding='3' summary="Attributes">
	 * <tr>
	 * <th>
	 *   Attribute</th>
	 * <th>Value</th>
	 * </tr>
	 * <tr>
	 * <td valign='top' rowspan='1' colspan='1'>{@code Node.nodeName}</td>
	 * <td valign='top' rowspan='1' colspan='1'>qualifiedName</td>
	 * </tr>
	 * <tr>
	 * <td valign='top' rowspan='1' colspan='1'>
	 *   {@code Node.namespaceURI}</td>
	 * <td valign='top' rowspan='1' colspan='1'>{@code namespaceURI}</td>
	 * </tr>
	 * <tr>
	 * <td valign='top' rowspan='1' colspan='1'>
	 *   {@code Node.prefix}</td>
	 * <td valign='top' rowspan='1' colspan='1'>prefix, extracted from
	 *   {@code qualifiedName}, or {@code null} if there is no
	 *   prefix</td>
	 * </tr>
	 * <tr>
	 * <td valign='top' rowspan='1' colspan='1'>{@code Node.localName}</td>
	 * <td valign='top' rowspan='1' colspan='1'>local name, extracted from
	 *   {@code qualifiedName}</td>
	 * </tr>
	 * <tr>
	 * <td valign='top' rowspan='1' colspan='1'>{@code Attr.name}</td>
	 * <td valign='top' rowspan='1' colspan='1'>
	 *   {@code qualifiedName}</td>
	 * </tr>
	 * <tr>
	 * <td valign='top' rowspan='1' colspan='1'>{@code Node.nodeValue}</td>
	 * <td valign='top' rowspan='1' colspan='1'>the empty
	 *   string</td>
	 * </tr>
	 * </table>
	 * @throws DOMException
	 *     INVALID_CHARACTER_ERR: Raised if the specified
	 *     {@code qualifiedName} is not an XML name according to the XML
	 *     version in use specified in the {@code Document.xmlVersion}
	 *     attribute.
	 *     <br>NAMESPACE_ERR: Raised if the {@code qualifiedName} is a
	 *     malformed qualified name, if the {@code qualifiedName} has a
	 *     prefix and the {@code namespaceURI} is {@code null}, if
	 *     the {@code qualifiedName} has a prefix that is "xml" and the
	 *     {@code namespaceURI} is different from "<a href='http://www.w3.org/XML/1998/namespace'>
	 *     http://www.w3.org/XML/1998/namespace</a>", if the {@code qualifiedName} or its prefix is "xmlns" and the
	 *     {@code namespaceURI} is different from "<a href='http://www.w3.org/2000/xmlns/'>http://www.w3.org/2000/xmlns/</a>", or if the {@code namespaceURI} is "<a href='http://www.w3.org/2000/xmlns/'>http://www.w3.org/2000/xmlns/</a>" and neither the {@code qualifiedName} nor its prefix is "xmlns".
	 *     <br>NOT_SUPPORTED_ERR: Always thrown if the current document does not
	 *     support the {@code "XML"} feature, since namespaces were
	 *     defined by XML.
	 * @since DOM Level 2
	 */
	default Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
		assert namespaceURI != null && !namespaceURI.isEmpty() : AssertMessages.notNullParameter();
		assert qualifiedName != null && !qualifiedName.isEmpty() : AssertMessages.notNullParameter();
		return getDocument().createAttributeNS(namespaceURI, qualifiedName);
	}

}
