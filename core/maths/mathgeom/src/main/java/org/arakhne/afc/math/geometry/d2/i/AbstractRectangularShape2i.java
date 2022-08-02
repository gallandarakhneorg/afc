/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.i;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.ai.RectangularShape2ai;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A rectangular shape with 2 integer numbers.
 *
 * @param <IT> is the type of the implementation of this shape.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractRectangularShape2i<IT extends AbstractRectangularShape2i<?>>
		extends AbstractShape2i<IT>
		implements RectangularShape2ai<Shape2i<?>, IT, PathElement2i, Point2i, Vector2i, Rectangle2i> {

	private static final long serialVersionUID = -6441506445885829836L;

	private int minx;

	private int miny;

	private int maxx;

	private int maxy;

	/** Construct an empty rectangular shape.
	 */
	public AbstractRectangularShape2i() {
		//
	}

	/** Constructor by copy.
	 * @param shape the shape to copy.
	 */
	public AbstractRectangularShape2i(RectangularShape2ai<?, ?, ?, ?, ?, ?> shape) {
		assert shape != null : AssertMessages.notNullParameter();
		this.minx = shape.getMinX();
		this.miny = shape.getMinY();
		this.maxx = shape.getMaxX();
		this.maxy = shape.getMaxY();
	}

	@Override
	public void setFromCorners(int x1, int y1, int x2, int y2) {
		final int a;
		final int b;
		final int c;
		final int d;
		if (x1 <= x2) {
			a = x1;
			b = x2;
		} else {
			a = x2;
			b = x1;
		}
		if (y1 <= y2) {
			c = y1;
			d = y2;
		} else {
			c = y2;
			d = y1;
		}
		if (this.minx != a || this.maxx != b || this.miny != c || this.maxy != d) {
			this.minx = a;
			this.maxx = b;
			this.miny = c;
			this.maxy = d;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getMinX() {
		return this.minx;
	}

	@Override
	public void setMinX(int x) {
		if (this.minx != x) {
			this.minx = x;
			if (this.maxx < x) {
				this.maxx = x;
			}
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getMaxX() {
		return this.maxx;
	}

	@Override
	public void setMaxX(int x) {
		if (this.maxx != x) {
			this.maxx = x;
			if (this.minx > x) {
				this.minx = x;
			}
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getMinY() {
		return this.miny;
	}

	@Override
	public void setMinY(int y) {
		if (this.miny != y) {
			this.miny = y;
			if (this.maxy < y) {
				this.maxy = y;
			}
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getMaxY() {
		return this.maxy;
	}

	@Override
	public void setMaxY(int y) {
		if (this.maxy != y) {
			this.maxy = y;
			if (this.miny > y) {
				this.miny = y;
			}
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Integer.hashCode(this.minx);
		bits = 31 * bits + Integer.hashCode(this.miny);
		bits = 31 * bits + Integer.hashCode(this.maxx);
		bits = 31 * bits + Integer.hashCode(this.maxy);
		return bits ^ (bits >> 31);
	}

}
