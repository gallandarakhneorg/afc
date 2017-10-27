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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.d.Point3d;

/**
 * A {@code Point3d} with a measure.
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
public class ESRIPoint extends Point3d {

	private static final long serialVersionUID = -6733000156946667895L;

	/** Measure associated to this point.
	 */
	private double mesure;

	/**
	 * Constructs a Poin4d.
	 */
	public ESRIPoint() {
		super();
		this.mesure = Double.NaN;
	}

	/**
	 * Constructs and initializes a Poin4d from the specified xy coordinates.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public ESRIPoint(double x, double y) {
		super(x, y, 0);
		this.mesure = Double.NaN;
	}

	/**
	 * Constructs and initializes a Point4d from the specified xyz coordinates.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public ESRIPoint(double x, double y, double z) {
		super(x, y, z);
		this.mesure = Double.NaN;
	}

	/**
	 * Constructs and initializes a Point4d from the specified xy coordinates.
	 * @param point the point
	 */
	public ESRIPoint(Tuple2D<?> point) {
		super(point.getX(), point.getY(), 0);
		this.mesure = Double.NaN;
	}

	/**
	 * Constructs and initializes a Point4d from the specified xyz coordinates.
	 * @param point the point.
	 */
	public ESRIPoint(Tuple3D<?> point) {
		super(point.getX(), point.getY(), point.getZ());
		this.mesure = Double.NaN;
	}

	/**
	 * Constructs and initializes a Point4d from the specified xyz coordinates.
	 * @param point the point.
	 */
	public ESRIPoint(ESRIPoint point) {
		super(point.getX(), point.getY(), point.getZ());
		this.mesure = point.getM();
	}

	/**
	 * Constructs and initializes a Point4d from the specified xyzm coordinates.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @param measure the measure
	 */
	public ESRIPoint(double x, double y, double z, double measure) {
		super(x, y, z);
		this.mesure = measure;
	}

	/** Replies the measure associated to this point.
	 *
	 * @return the measure associated to this point.
	 */
	@Pure
	public double getM() {
		return this.mesure;
	}

	/** Set the measure associated to this point.
	 *
	 * @param mesure is the measure associated to this point.
	 */
	public void setM(double mesure) {
		this.mesure = mesure;
	}

	@Override
	@Pure
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append('(');
		b.append(this.getX());
		b.append(',');
		b.append(this.getY());
		b.append(',');
		b.append(this.getZ());
		b.append(',');
		b.append(this.getM());
		b.append(')');
		return b.toString();
	}

}
