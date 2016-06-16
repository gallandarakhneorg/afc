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

package org.arakhne.afc.math.discrete.object2d;

import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.matrix.Transform2D;

/** 2D shape with integer  points.
 *
 * @param <T> is the type of the shape implemented by the instance of this class.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link org.arakhne.afc.math.geometry.d2.i.AbstractShape2i}
 */
@Deprecated
@SuppressWarnings("all")
public abstract class AbstractShape2i<T extends Shape2i> implements Shape2i {

	private static final long serialVersionUID = -3663448743772835647L;

	/**
	 */
	public AbstractShape2i() {
		//
	}

	@Override
	public T clone()  {
		try {
			return (T)super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Shape2i createTransformedShape(Transform2D transform) {
		return new Path2i(getPathIterator(transform));
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final boolean contains(Point2D p) {
		return contains(p.x(), p.y());
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float distance(Point2D p) {
		 return (float)Math.sqrt(distanceSquared(p));
	}

	/** {@inheritDoc}
	 */
	@Override
	public final PathIterator2i getPathIterator() {
		return getPathIterator(null);
	}
	
}
