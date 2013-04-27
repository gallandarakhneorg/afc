/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;

import org.arakhne.afc.ui.Graphics2DLOD;

/** This graphic context permits to display
 *  something with a level of details.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultLODGraphics2D extends AbstractLODGraphics2D<Graphics2D> {

	////////////////////////////////////////////////////////////
	// Constructor

	/** Construct a new ZoomableGraphics2D.
	 *
	 * @param target is the graphics in which draws will be done.
	 * @param target_component is the target component (used to display an icon).
	 * @param antialiasing permits to force the anti-aliasing flag for the target graphical context
	 * @param is_for_printing indicates if this graphics environment is for printing or not.
	 * @param lod indicates the desired LOD used by this graphical context.
	 */
	public DefaultLODGraphics2D(Graphics2D target, Component target_component, boolean antialiasing, boolean is_for_printing, Graphics2DLOD lod) {
		super(target, target_component, antialiasing, is_for_printing, lod);
	}

	/** Construct a new ZoomableGraphics2D.
	 *
	 * @param target is the graphics in which draws will be done.
	 * @param target_component is the target component (used to display an icon).
	 * @param antialiasing permits to force the anti-aliasing flag for the target graphical context
	 * @param lod indicates the desired LOD used by this graphical context.
	 */
	public DefaultLODGraphics2D(Graphics2D target, Component target_component, boolean antialiasing, Graphics2DLOD lod) {
		super(target, target_component, antialiasing, lod);
	}

	/** Construct a new ZoomableGraphics2D.
	 *
	 * @param target is the graphics in which draws will be done.
	 * @param target_component is the target component (used to display an icon).
	 * @param lod indicates the desired LOD used by this graphical context.
	 */
	public DefaultLODGraphics2D(Graphics2D target, Component target_component, Graphics2DLOD lod) {
		super(target, target_component, lod);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Graphics create() {
		return new DefaultLODGraphics2D(
				(Graphics2D)this.target.create(),
				this.targetComponent.get(),
				isAntiAliased(),
				isPrinting(),
				getLOD());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected float polyX(float x) {
		return x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected float polyY(float y) {
		return y;
	}
	
	private static Path2D toPath(PathIterator path) {
		GeneralPath gp = new GeneralPath(path.getWindingRule());
		float[] coords = new float[6];
		while (!path.isDone()) {
			switch(path.currentSegment(coords)) {
			case PathIterator.SEG_MOVETO:
				gp.moveTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_LINETO:
				gp.lineTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_QUADTO:
				gp.quadTo(coords[0], coords[1], coords[2], coords[3]);
				break;
			case PathIterator.SEG_CUBICTO:
				gp.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
				break;
			case PathIterator.SEG_CLOSE:
				gp.closePath();
				break;
			default:
			}
			path.next();
		}
		return gp;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(PathIterator pi) {
		this.target.draw(toPath(pi));
	}

	@Override
	public void fill(PathIterator pi) {
		this.target.fill(toPath(pi));
	}

}
