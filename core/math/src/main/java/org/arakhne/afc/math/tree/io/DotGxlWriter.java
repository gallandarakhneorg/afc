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

package org.arakhne.afc.math.tree.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.math.tree.TreeNode;


/**
 * This is a writer of .gxl file from a tree.
 *
 * <p>The .gxl file format is an XML dialect for the .dot file format.
 * It is defined on <a href="http://www.gupro.de/GXL/">Graph eXchange Language page</a>.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class DotGxlWriter {

	/** Common extension used for <code>.dot</code> files.
	 */
	public static final String EXTENSION = ".gxl"; 

	private final Writer writer;

	private int graphIndex;

	/**
	 * Create a new gxl writer that output inside the given output stream.
	 *
	 * @param outputStream is the stream to write in.
	 */
	public DotGxlWriter(OutputStream outputStream) {
		this(new OutputStreamWriter(outputStream));
	}

	/**
	 * Create a new gxl writer that output inside the given output stream.
	 *
	 * @param outputStream is the stream to write in.
	 */
	public DotGxlWriter(Writer outputStream) {
		assert outputStream != null;
		this.writer = outputStream;
		this.graphIndex = 1;
	}

	/**
	 * Write the given tree inside the .gxl output stream.
	 *
	 * @param tree is the tree to write
	 * @throws IOException in case of error
	 */
	public void write(Tree<?, ?> tree) throws IOException {
		this.writer.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n"); 
		this.writer.append("<gxl>\n"); 

		if (tree != null) {
			this.writer.append("\t<graph id=\""); 
			this.writer.append(Integer.toHexString(System.identityHashCode(new Integer(tree.hashCode()))));
			this.writer.append("-"); 
			this.writer.append(Integer.toString(this.graphIndex++));
			this.writer.append("\" edgeids=\"true\" edgemode=\"directed\">\n"); 

			// Write the node attributes
			Iterator<? extends TreeNode<?, ?>> iterator = tree.broadFirstIterator();
			TreeNode<?, ?> node;
			int dataCount;
			while (iterator.hasNext()) {
				node = iterator.next();
				dataCount = node.getUserDataCount();
				final String name = "NODE" + Integer.toHexString(node.hashCode()); 
				final String label = Integer.toString(dataCount);

				this.writer.append("\t\t<node id=\""); 
				this.writer.append(name);
				this.writer.append("\">\n"); 

				this.writer.append("\t\t\t<attr name=\"label\">\n"); 
				this.writer.append("\t\t\t\t<string>"); 
				this.writer.append(label);
				this.writer.append("</string>\n"); 
				this.writer.append("\t\t\t</attr>\n"); 

				this.writer.append("\t\t</node>\n"); 
			}

			// Write the node attributes
			iterator = tree.broadFirstIterator();
			TreeNode<?, ?> child;
			String childName;
			while (iterator.hasNext()) {
				node = iterator.next();
				final String name = "NODE" + Integer.toHexString(node.hashCode()); 
				if (!node.isLeaf()) {
					final int childCount = node.getChildCount();
					for (int i = 0; i < childCount; ++i) {
						child = node.getChildAt(i);
						if (child != null) {
							childName = "NODE" + Integer.toHexString(child.hashCode()); 

							this.writer.append("\t\t<edge id=\""); 
							this.writer.append(name);
							this.writer.append("--"); 
							this.writer.append(childName);
							this.writer.append("\" isdirected=\"true\" from=\""); 
							this.writer.append(name);
							this.writer.append("\" to=\""); 
							this.writer.append(childName);
							this.writer.append("\">\n"); 

							this.writer.append("\t\t\t<attr name=\"label\">\n"); 
							this.writer.append("\t\t\t\t<string>"); 
							this.writer.append(Integer.toString(i));
							this.writer.append("</string>\n"); 
							this.writer.append("\t\t\t</attr>\n"); 

							this.writer.append("\t\t</edge>\n"); 
						}
					}
				}
			}

			this.writer.append("\t</graph>\n"); 
		}

		this.writer.append("</gxl>\n"); 
	}

	/** Close the output stream.
	 *
	 * @throws IOException in case of error.
	 */
	public void close() throws IOException {
		this.writer.close();
	}

}
