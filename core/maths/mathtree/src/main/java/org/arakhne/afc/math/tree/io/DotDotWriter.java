/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

import org.arakhne.afc.math.tree.Tree;

/** This is a writer of .dot file from a tree.
 *
 * <p>The .dot file format is defined by the <a href="http://www.graphviz.org/">GraphViz project</a>.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class DotDotWriter {

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
        assert outputStream != null;
        this.writer = outputStream;
        this.graphIndex = 1;
    }

    /**
     * Write the given tree inside the .dot output stream.
     *
     * @param tree is the tree to write
     * @throws IOException in case of error
     */
    public void write(Tree<?, ?> tree) throws IOException {
        this.writer.append("digraph G"); //$NON-NLS-1$
        this.writer.append(Integer.toString(this.graphIndex++));
        this.writer.append(" {\n"); //$NON-NLS-1$

        if (tree != null) {
        	// Write the node attributes
        	var iterator = tree.breadthFirstIterator();
            while (iterator.hasNext()) {
                final var node = iterator.next();
                final var dataCount = node.getUserDataCount();
                final String name = "NODE" + Integer.toHexString(System.identityHashCode(node)); //$NON-NLS-1$
                final String label = Integer.toString(dataCount);

                this.writer.append("\t"); //$NON-NLS-1$
                this.writer.append(name);
                this.writer.append(" [label=\""); //$NON-NLS-1$
                this.writer.append(label);
                this.writer.append("\"]\n"); //$NON-NLS-1$
            }

            this.writer.append("\n"); //$NON-NLS-1$

            // Write the node links
            iterator = tree.breadthFirstIterator();
            while (iterator.hasNext()) {
                final var node = iterator.next();
                final var name = "NODE" + Integer.toHexString(System.identityHashCode(node)); //$NON-NLS-1$
                if (!node.isLeaf()) {
                    final var partitionType = node.getPartitionEnumeration();
                    final var childCount = node.getChildCount();
                    for (var i = 0; i < childCount; ++i) {
                        final var child = node.getChildAt(i);
                        if (child != null) {
                            final var childName = "NODE" + Integer.toHexString(System.identityHashCode(child)); //$NON-NLS-1$
                            final String label;
                            if (partitionType != null) {
                                label = partitionType.getEnumConstants()[i].name();
                            } else {
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
