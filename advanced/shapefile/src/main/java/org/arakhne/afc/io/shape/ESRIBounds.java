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

package org.arakhne.afc.io.shape;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/**
 * Bounds for a ESRI shape file.
 *
 * <p>xyz are the coordinates of a point, m is an user measure.
 *
 * <p>The specification of the ESRI Shape file format is described in
 * <a href="./doc-files/esri_specs_0798.pdf">the July 98 specification document</a>.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class ESRIBounds implements Comparable<ESRIBounds> {

	/** min x.
	 */
	private double minx;

	/** min y.
	 */
	private double miny;

	/** min z.
	 */
	private double minz;

	/** min m.
	 */
	private double minm;

	/** max x.
	 */
	private double maxx;

	/** max y.
	 */
	private double maxy;

	/** max z.
	 */
	private double maxz;

	/** max m.
	 */
	private double maxm;

	/** Constructor.
	 */
	public ESRIBounds() {
		this.minm = Double.NaN;
		this.maxm = Double.NaN;
	}

	/** Constructor.
	 *
	 * @param point the point to copy.
	 */
	public ESRIBounds(ESRIPoint point) {
		this.minx = point.getX();
		this.maxx = this.minx;
		this.miny = point.getY();
		this.maxy = this.miny;
		this.minz = point.getZ();
		this.maxz = this.minz;
		this.minm = point.getM();
		this.maxm = this.minm;
	}

	/** Constructor.
	 * @param minx the min x.
	 * @param maxx the max x.
	 * @param miny the min y.
	 * @param maxy the max y.
	 */
	public ESRIBounds(double minx, double maxx, double miny, double maxy) {
		this.minx = minx;
		this.maxx = maxx;
		this.miny = miny;
		this.maxy = maxy;
		this.minm = Double.NaN;
		this.maxm = Double.NaN;
		ensureMinMax();
	}

	/** Constructor.
	 * @param minx the min x.
	 * @param maxx the max x.
	 * @param miny the min y.
	 * @param maxy the max y.
	 * @param minz the min z.
	 * @param maxz the max z.
	 * @param minm the min m.
	 * @param maxm the max m.
	 */
	public ESRIBounds(double minx, double maxx, double miny, double maxy, double minz, double maxz, double minm, double maxm) {
		this.minx = minx;
		this.maxx = maxx;
		this.miny = miny;
		this.maxy = maxy;
		this.minz = minz;
		this.maxz = maxz;
		this.minm = minm;
		this.maxm = maxm;
		ensureMinMax();
	}

	/** Constructor.
	 *
	 * @param bounds the bounds to copy.
	 */
	public ESRIBounds(ESRIBounds bounds) {
		this.minx = bounds.minx;
		this.maxx = bounds.maxx;
		this.miny = bounds.miny;
		this.maxy = bounds.maxy;
		this.minz = bounds.minz;
		this.maxz = bounds.maxz;
		this.minm = bounds.minm;
		this.maxm = bounds.maxm;
		ensureMinMax();
	}

	/** Create and replies an union of this bounds and the given bounds.
	 *
	 * @param bounds the bounds to make the union from.
	 * @return the union of this bounds and the given one.
	 */
	public ESRIBounds createUnion(ESRIBounds bounds) {
		final ESRIBounds eb = new ESRIBounds();
		eb.minx = (bounds.minx < this.minx) ? bounds.minx : this.minx;
		eb.maxx = (bounds.maxx < this.maxx) ? this.maxx : bounds.maxx;
		eb.miny = (bounds.miny < this.miny) ? bounds.miny : this.miny;
		eb.maxy = (bounds.maxy < this.maxy) ? this.maxy : bounds.maxy;
		eb.minz = (bounds.minz < this.minz) ? bounds.minz : this.minz;
		eb.maxz = (bounds.maxz < this.maxz) ? this.maxz : bounds.maxz;
		eb.minm = (bounds.minm < this.minm) ? bounds.minm : this.minm;
		eb.maxm = (bounds.maxm < this.maxm) ? this.maxm : bounds.maxm;
		return eb;
	}

	/** Add a point to this bounds.
	 *
	 * @param point the point to add.
	 */
	public void add(ESRIPoint point) {
		if (point.getX() < this.minx) {
			this.minx = point.getX();
		}
		if (point.getX() > this.maxx) {
			this.maxx = point.getX();
		}
		if (point.getY() < this.miny) {
			this.miny = point.getY();
		}
		if (point.getY() > this.maxy) {
			this.maxy = point.getY();
		}
		if (point.getZ() < this.minz) {
			this.minz = point.getZ();
		}
		if (point.getZ() > this.maxz) {
			this.maxz = point.getZ();
		}
		if (point.getM() < this.minm) {
			this.minm = point.getM();
		}
		if (point.getM() > this.maxm) {
			this.maxm = point.getM();
		}
	}

	@Override
	@Pure
	public boolean equals(Object obj) {
		if (obj instanceof ESRIBounds) {
			return compareTo((ESRIBounds) obj) == 0;
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Double.hashCode(this.minx);
		result = prime * result + Double.hashCode(this.miny);
		result = prime * result + Double.hashCode(this.minz);
		result = prime * result + Double.hashCode(this.minm);
		result = prime * result + Double.hashCode(this.maxx);
		result = prime * result + Double.hashCode(this.maxy);
		result = prime * result + Double.hashCode(this.maxz);
		result = prime * result + Double.hashCode(this.maxm);
		return result;
	}

	@Pure
	@Override
	public int compareTo(ESRIBounds bounds) {
		if (bounds == null) {
			return -1;
		}
		int cmp = (int) Math.signum(bounds.minx - this.minx);
		if (cmp != 0) {
			return cmp;
		}
		cmp = (int) Math.signum(bounds.maxx - this.maxx);
		if (cmp != 0) {
			return cmp;
		}
		cmp = (int) Math.signum(bounds.miny - this.miny);
		if (cmp != 0) {
			return cmp;
		}
		cmp = (int) Math.signum(bounds.maxy - this.maxy);
		if (cmp != 0) {
			return cmp;
		}
		cmp = (int) Math.signum(bounds.minz - this.minz);
		if (cmp != 0) {
			return cmp;
		}
		cmp = (int) Math.signum(bounds.maxz - this.maxz);
		if (cmp != 0) {
			return cmp;
		}
		cmp = (int) Math.signum(bounds.minm - this.minm);
		if (cmp != 0) {
			return cmp;
		}
		return (int) Math.signum(bounds.maxm - this.maxm);
	}

	/** Replies the min x.
	 *
	 * @return the min x.
	 */
	@Pure
	public double getMinX() {
		return this.minx;
	}

	/** Replies the center x.
	 *
	 * @return the center x.
	 */
	@Pure
	public double getCenterX() {
		return (this.minx + this.maxx) / 2.;
	}

	/** Replies the max x.
	 *
	 * @return the max x.
	 */
	@Pure
	public double getMaxX() {
		return this.maxx;
	}

	/** Replies the center y.
	 *
	 * @return the center y.
	 */
	@Pure
	public double getCenterY() {
		return (this.miny + this.maxy) / 2.;
	}

	/** Replies the min y.
	 *
	 * @return the min y.
	 */
	@Pure
	public double getMinY() {
		return this.miny;
	}

	/** Replies the max y.
	 *
	 * @return the max y.
	 */
	@Pure
	public double getMaxY() {
		return this.maxy;
	}

	/** Replies the min z.
	 *
	 * @return the min z.
	 */
	@Pure
	public double getMinZ() {
		return this.minz;
	}

	/** Replies the center z.
	 *
	 * @return the center z.
	 */
	@Pure
	public double getCenterZ() {
		return (this.minz + this.maxz) / 2.;
	}

	/** Replies the max z.
	 *
	 * @return the max z.
	 */
	@Pure
	public double getMaxZ() {
		return this.maxz;
	}

	/** Replies the min m.
	 *
	 * @return the min m.
	 */
	@Pure
	public double getMinM() {
		return this.minm;
	}

	/** Replies the center m.
	 *
	 * @return the center m.
	 */
	@Pure
	public double getCenterM() {
		return (this.minm + this.maxm) / 2.;
	}

	/** Replies the max m.
	 *
	 * @return the max m.
	 */
	@Pure
	public double getMaxM() {
		return this.maxm;
	}

	/** Replies the 2D bounds.
	 *
	 * @return the 2D bounds
	 */
	@Pure
	public Rectangle2d toRectangle2d() {
		final Rectangle2d bounds = new Rectangle2d();
		bounds.setFromCorners(this.minx, this.miny, this.maxx, this.maxy);
		return bounds;
	}

	/** Ensure that min and max values are correctly ordered.
	 */
	public void ensureMinMax() {
		double t;
		if (this.maxx < this.minx) {
			t = this.minx;
			this.minx = this.maxx;
			this.maxx = t;
		}
		if (this.maxy < this.miny) {
			t = this.miny;
			this.miny = this.maxy;
			this.maxy = t;
		}
		if (this.maxz < this.minz) {
			t = this.minz;
			this.minz = this.maxz;
			this.maxz = t;
		}
		if (this.maxm < this.minm) {
			t = this.minm;
			this.minm = this.maxm;
			this.maxm = t;
		}
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append('(');
		b.append(this.minx);
		b.append(';');
		b.append(this.miny);
		b.append(';');
		b.append(this.minz);
		b.append(")-("); //$NON-NLS-1$
		b.append(this.maxx);
		b.append(';');
		b.append(this.maxy);
		b.append(';');
		b.append(this.maxz);
		b.append(")("); //$NON-NLS-1$
		b.append(this.minm);
		b.append(';');
		b.append(this.maxm);
		b.append(')');
		return b.toString();
	}

}
