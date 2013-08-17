/* 
 * $Id$
 * 
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.ui.awt;

import java.awt.geom.Dimension2D;

/** This class implements a {@link Dimension2D} with
 * double precision floating point values. 
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
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
		return "(w="+this.width+";h="+this.height+")";   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}

}
