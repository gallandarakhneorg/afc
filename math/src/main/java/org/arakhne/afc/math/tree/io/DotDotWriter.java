/* 
 * $Id$
 * 
 * Copyright (c) 2008-11, Multiagent Team,
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
 * This is a writer of .dot file from a tree.
 * <p>
 * The .dot file format is defined by the <a href="http://www.graphviz.org/">GraphViz project</a>.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DotDotWriter {

	/** Common extension used for <code>.dot</code> files.
	 */
	public static final String EXTENSION = ".dot"; //$NON-NLS-1$
	
	private final Writer writer;
	private int graphIndex;
	
	/**
	 * Create a new dot writer that output inside the given output stream.
	 * 
	 * @param outputStream is the stream to write in.
	 */
	public DotDotWriter(OutputStream outputStream) {
		this(new OutputStreamWriter(outputStream));
	}
	
	/**
	 * Create a new dot writer that output inside the given output stream.
	 * 
	 * @param outputStream is the stream to write in.
	 */
	public DotDotWriter(Writer outputStream) {
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
	 * Write the given tree inside the .dot output stream.
	 * 
	 * @param tree is the tree to write
	 * @throws IOException in case of error
	 */
	public void write(Tree<?,?> tree) throws IOException {
		this.writer.append("digraph G"); //$NON-NLS-1$
		this.writer.append(Integer.toString(this.graphIndex++));
		this.writer.append(" {\n"); //$NON-NLS-1$
		
		if (tree!=null) {
			// Write the node attributes
			Iterator<? extends TreeNode<?,?>> iterator = tree.broadFirstIterator();
			TreeNode<?,?> node;
			String label, name;
			int dataCount;
			while (iterator.hasNext()) {
				node = iterator.next();
				dataCount = node.getUserDataCount();
				name = "NODE"+Integer.toHexString(System.identityHashCode(node)); //$NON-NLS-1$
				label = Integer.toString(dataCount);
				
				this.writer.append("\t"); //$NON-NLS-1$
				this.writer.append(name);
				this.writer.append(" [label=\""); //$NON-NLS-1$
				this.writer.append(label);
				this.writer.append("\"]\n"); //$NON-NLS-1$
			}
			
			this.writer.append("\n"); //$NON-NLS-1$
		
			// Write the node links
			iterator = tree.broadFirstIterator();
			Class<? extends Enum<?>> partitionType;
			TreeNode<?,?> child;
			String childName;
			while (iterator.hasNext()) {
				node = iterator.next();
				name = "NODE"+Integer.toHexString(System.identityHashCode(node)); //$NON-NLS-1$
				if (!node.isLeaf()) {
					partitionType = node.getPartitionEnumeration();
					for(int i=0; i<node.getChildCount(); ++i) {
						child = node.getChildAt(i);
						if (child!=null) {
							childName = "NODE"+Integer.toHexString(System.identityHashCode(child)); //$NON-NLS-1$
							if (partitionType!=null) {
								label = partitionType.getEnumConstants()[i].name();
							}
							else {
								label = Integer.toString(i);
							}
							this.writer.append("\t"); //$NON-NLS-1$
							this.writer.append(name);
							this.writer.append("->"); //$NON-NLS-1$
							this.writer.append(childName);
							this.writer.append(" [label=\""); //$NON-NLS-1$
							this.writer.append(label);
							this.writer.append("\"]\n"); //$NON-NLS-1$
						}
					}
				}
			}
		}
		
		this.writer.append("}\n\n"); //$NON-NLS-1$
	}
	
	/** Close the output stream.
	 * 
	 * @throws IOException in case of error.
	 */
	public void close() throws IOException {
		this.writer.close();
	}

}