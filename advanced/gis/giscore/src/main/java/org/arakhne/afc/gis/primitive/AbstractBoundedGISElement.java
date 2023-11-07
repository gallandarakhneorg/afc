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

package org.arakhne.afc.gis.primitive;

import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** Element of a GIS application which is bounded.
 *
 * @param <C> is the type of the container of this element.
 * @param <T> is the type of this element, used by the cloning feature.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class AbstractBoundedGISElement<C extends GISContainer<?>, T extends AbstractBoundedGISElement<C, T>>
		extends AbstractGISElement<C, T> implements BoundedGISElement {

	private static final long serialVersionUID = 2804454630472447980L;

	private transient Rectangle2d bounds;

	/** Constructor.
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param attributeSource is the source of the attributes for this map element.
	 * @since 4.0
	 */
	public AbstractBoundedGISElement(UUID id, AttributeCollection attributeSource) {
		super(id, attributeSource);
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("bounds", getBoundingBox()); //$NON-NLS-1$
	}

	/** Clone this object to obtain a valid copy.
     *
     * @return a copy
     */
	@Override
	@Pure
    public T clone() {
		final T element = super.clone();
		element.resetBoundingBox();
		return element;
    }

	@Override
	@Pure
	public Rectangle2d getBoundingBox() {
		if (this.bounds == null) {
			this.bounds = calcBounds();
		}
		return this.bounds;
	}

	@Override
	public void resetBoundingBox() {
		this.bounds = null;
	}

	/** Set the bounding box of this element.
	 * This function does not check if the given bounds are
	 * enclosing the content of this element.
	 *
	 * @param bounds is the bounds to combine with the bounds of
	 *     this element.
	 */
	protected void setBoundingBox(Rectangle2d bounds) {
		this.bounds = bounds;
	}

	/** Compute the bounds of this element.
	 * This function does not update the internal
	 * attribute replied by {@link #getBoundingBox()}
	 *
	 * @return the bounds
	 */
	@Pure
	protected abstract Rectangle2d calcBounds();

}
