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

package org.arakhne.afc.ui.awt;

import java.awt.geom.Dimension2D;

/** This class implements a {@link Dimension2D} with
 * double precision floating point values. 
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class DoubleDimension extends Dimension2D {

	private double width;
	private double height;

	/**
	 */
	public DoubleDimension() {
		this.width = this.height = 0.;
	}

	/**
	 * @param w
	 * @param h
	 */
	public DoubleDimension(double w, double h) {
		this.width = w;
		this.height = h;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getWidth() {
		return this.width;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getHeight() {
		return this.height;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "(w="+this.width+";h="+this.height+")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
