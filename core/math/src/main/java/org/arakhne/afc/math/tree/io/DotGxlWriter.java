/* 
 * $Id$
 * 
 * Copyright (c) 2009-11, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
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
 * <p>
 * The .gxl file format is an XML dialect for the .dot file format.
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
	public static final String EXTENSION = ".gxl"; //$NON-NLS-1$
	
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
		assert(outputStream!=null);
		this.writer = outputStream;
		this.graphIndex = 1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	/**
	 * Write the given tree inside the .gxl output stream.
	 * 
	 * @param tree is the tree to write
	 * @throws IOException in case of error
	 */
	public void write(Tree<?,?> tree) throws IOException {
		this.writer.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n"); //$NON-NLS-1$
		this.writer.append("<gxl>\n"); //$NON-NLS-1$
		
		if (tree!=null) {
			this.writer.append("\t<graph id=\""); //$NON-NLS-1$
			this.writer.append(Integer.toHexString(System.identityHashCode(new Integer(tree.hashCode()))));
			this.writer.append("-"); //$NON-NLS-1$
			this.writer.append(Integer.toString(this.graphIndex++));
			this.writer.append("\" edgeids=\"true\" edgemode=\"directed\">\n"); //$NON-NLS-1$

			// Write the node attributes
			Iterator<? extends TreeNode<?,?>> iterator = tree.broadFirstIterator();
			TreeNode<?,?> node;
			String label, name;
			int dataCount;
			while (iterator.hasNext()) {
				node = iterator.next();
				dataCount = node.getUserDataCount();
				name = "NODE"+Integer.toHexString(node.hashCode()); //$NON-NLS-1$
				label = Integer.toString(dataCount);
				
				this.writer.append("\t\t<node id=\""); //$NON-NLS-1$
				this.writer.append(name);
				this.writer.append("\">\n"); //$NON-NLS-1$

				this.writer.append("\t\t\t<attr name=\"label\">\n"); //$NON-NLS-1$
				this.writer.append("\t\t\t\t<string>"); //$NON-NLS-1$
				this.writer.append(label);
				this.writer.append("</string>\n"); //$NON-NLS-1$
				this.writer.append("\t\t\t</attr>\n"); //$NON-NLS-1$
				
				this.writer.append("\t\t</node>\n"); //$NON-NLS-1$
			}
			
			// Write the node attributes
			iterator = tree.broadFirstIterator();
			TreeNode<?,?> child;
			String childName;
			while (iterator.hasNext()) {
				node = iterator.next();
				name = "NODE"+Integer.toHexString(node.hashCode()); //$NON-NLS-1$
				if (!node.isLeaf()) {
					for(int i=0; i<node.getChildCount(); ++i) {
						child = node.getChildAt(i);
						if (child!=null) {
							childName = "NODE"+Integer.toHexString(child.hashCode()); //$NON-NLS-1$
							
							this.writer.append("\t\t<edge id=\""); //$NON-NLS-1$
							this.writer.append(name);
							this.writer.append("--"); //$NON-NLS-1$
							this.writer.append(childName);
							this.writer.append("\" isdirected=\"true\" from=\""); //$NON-NLS-1$
							this.writer.append(name);
							this.writer.append("\" to=\""); //$NON-NLS-1$
							this.writer.append(childName);
							this.writer.append("\">\n"); //$NON-NLS-1$

							this.writer.append("\t\t\t<attr name=\"label\">\n"); //$NON-NLS-1$
							this.writer.append("\t\t\t\t<string>"); //$NON-NLS-1$
							this.writer.append(Integer.toString(i));
							this.writer.append("</string>\n"); //$NON-NLS-1$
							this.writer.append("\t\t\t</attr>\n"); //$NON-NLS-1$

							this.writer.append("\t\t</edge>\n"); //$NON-NLS-1$
						}
					}
				}
			}
			
			this.writer.append("\t</graph>\n"); //$NON-NLS-1$
		}
		
		this.writer.append("</gxl>\n"); //$NON-NLS-1$
	}
	
	/** Close the output stream.
	 * 
	 * @throws IOException in case of error.
	 */
	public void close() throws IOException {
		this.writer.close();
	}

}