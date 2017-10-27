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

package org.arakhne.afc.io.shape;

import java.io.IOException;

import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.attrs.collection.HeapAttributeCollection;

/**
 * This class describes a service which is creating
 * element instances from shpae file data.
 *
 * @param <E> is the type of elements which is red by this reader.
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class AbstractElementFactory<E> implements ElementFactory<E> {

	//---- PRE

	@Override
	public void preReadingStage() throws IOException {
		//
	}

	@Override
	public void postHeaderReadingStage() throws IOException {
		//
	}

	//---- CREATION

	@Override
	public AttributeCollection createAttributeCollection(int elementIndex) {
		return new HeapAttributeCollection();
	}

	@Override
	public E createPolyline(AttributeCollection provider, int shapeIndex, int[] parts, ESRIPoint[] points, boolean hasZ) {
		return null;
	}

	@Override
	public E createPolygon(AttributeCollection provider, int shapeIndex, int[] parts, ESRIPoint[] points, boolean hasZ) {
		return null;
	}

	@Override
	public E createMultiPoint(AttributeCollection provider, int shapeIndex, ESRIPoint[] points, boolean hasZ) {
		return null;
	}

	@Override
	public E createPoint(AttributeCollection provider, int shape_index, ESRIPoint point) {
		return null;
	}

	@Override
	public E createMultiPatch(AttributeCollection provider, int shapeIndex, int[] parts,
			ShapeMultiPatchType[] partTypes, ESRIPoint[] points) {
		return null;
	}

	@Override
	public void putAttributeIn(E element, String attributeName, AttributeValue value) {
		//
	}

	//---- POST

	@Override
	public boolean postEntryReadingStage(E element) throws IOException {
		return true;
	}

	@Override
	public boolean postAttributeReadingStage(E element) throws IOException {
		return true;
	}

	@Override
	public boolean postShapeReadingStage(E element) throws IOException {
		return true;
	}

	@Override
	public void postReadingStage(boolean success) throws IOException {
		//
	}

}
