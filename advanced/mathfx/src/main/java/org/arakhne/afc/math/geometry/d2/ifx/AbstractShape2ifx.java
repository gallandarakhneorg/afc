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

package org.arakhne.afc.math.geometry.d2.ifx;

import javafx.beans.property.ObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.PathIterator2D;

/** Abstract shape with 2 integer FX properties.
 *
 * @param <T> the type of the shape.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractShape2ifx<T extends AbstractShape2ifx<?>> implements Shape2ifx<T> {

	private static final long serialVersionUID = -2142220243877033508L;

	/** Bounding box property.
	 */
	protected ObjectProperty<Rectangle2ifx> boundingBox;

	@SuppressWarnings("unchecked")
	@Override
	public T clone() {
		try {
			return (T) super.clone();
		} catch (CloneNotSupportedException exception) {
			throw new InternalError(exception);
		}
	}

	private boolean isEqualsToShape(Object obj) {
		try {
			return equalsToShape((T) obj);
		} catch (ClassCastException exception) {
			return equalsToPathIterator((PathIterator2D<?>) obj);
		} catch (Throwable exception) {
			//
		}
		return  false;
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		return isEqualsToShape(obj);
	}

	@Pure
	@Override
    public abstract int hashCode();

	@Override
	public final GeomFactory2ifx getGeomFactory() {
		return GeomFactory2ifx.SINGLETON;
	}

}
