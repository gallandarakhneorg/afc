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

import org.arakhne.afc.util.HashCodeUtil;

/** This class implements a {@link Dimension2D} with
 * singgle precision floating point values. 
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class FloatDimension extends Dimension2D {

	private float width;
	private float height;

	/**
	 */
	public FloatDimension() {
		this.width = this.height = 0f;
	}

	/**
	 * @param w
	 * @param h
	 */
	public FloatDimension(float w, float h) {
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
		this.width = (float)width;
		this.height = (float)height;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "(w="+this.width+";h="+this.height+")";   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj==this) return true;
		if (obj instanceof Dimension2D) {
			Dimension2D d = (Dimension2D)obj;
			return d.getWidth()==getWidth() && d.getHeight()==getHeight();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int h = HashCodeUtil.hash(this.width);
		h = HashCodeUtil.hash(h, this.height);
		return h;
	}

}
