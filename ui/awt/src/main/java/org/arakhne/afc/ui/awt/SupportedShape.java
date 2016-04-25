/* $Id$
 * 
 * This is a part of NetEditor project from Arakhne.org:
 * package org.arakhne.neteditor.zoompanel.
 * 
 * Copyright (C) 2002  St&eacute;phane GALLAND, Mahdi Hannoun
 * Copyright (C) 2004-2012  St&eacute;phane GALLAND
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

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/** This enumeration list all the supported Shape types in a zoomable context.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public enum SupportedShape {

	/** {@link Area}
	 */
	AREA(Area.class),
	/** {@link CubicCurve2D}
	 */
	CUBIC_CURVE(CubicCurve2D.class),
	/** {@link Line2D}
	 */
	LINE(Line2D.class),
	/** {@link Path2D}
	 */
	PATH(Path2D.class),
	/** {@link Polygon}
	 */
	POLYGON(Polygon.class),
	/** {@link QuadCurve2D}
	 */
	QUAD_CURVE(QuadCurve2D.class),
	/** {@link Rectangle2D}
	 */
	RECTANGLE2D(Rectangle2D.class),
	/** {@link Arc2D}
	 */
	ARC(Arc2D.class),
	/** {@link RoundRectangle2D}
	 */
	ROUND_RECTANGLE(RoundRectangle2D.class),
	/** {@link Ellipse2D}
	 */
	ELLIPSE(Ellipse2D.class),
	/** {@link Shape}
	 */
	VIRTUALIZABLE_SHAPE(VirtualizableShape.class);

	private final Class<? extends Shape> type;

	SupportedShape(Class<? extends Shape> type) {
		this.type = type;
	}
	
	/** Replies the AWT class that is corresponding to this shape type.
	 * 
	 * @return the AWT class that is corresponding to this shape type.
	 */
	public Class<? extends Shape> awtType() {
		return this.type;
	}

	/**
	 * Indicates if the given class corresponds to this supported shape.
	 * 
	 * @param shapeType is the class of shape to test against with type.
	 * @return <code>true</code> is the given class is compatible with this
	 * type of shape, otherwise <code>false</code> 
	 */
	public boolean isTypeOf(Class<?> shapeType) {
		return ((this.type!=null)&&(this.type.isAssignableFrom(shapeType)));
	}

	/**
	 * Replies the supported shape type that corresponding to the given class.
	 * 
	 * @param type
	 * @return the supported shape type that corresponding to the given class.
	 */
	public static SupportedShape getTypeOf(Class<?> type) {
		if (type!=null && Shape.class.isAssignableFrom(type)) {
			SupportedShape[] values = SupportedShape.values();
			for (SupportedShape stype : values) {
				if (stype.isTypeOf(type)) return stype;
			}
		}
		return null;
	}

	/**
	 * Replies the supported shape type that corresponding to the given shape.
	 * 
	 * @param shape
	 * @return the supported shape type that corresponding to the given class.
	 */
	public static SupportedShape getTypeOf(Object shape) {
		if (shape instanceof Shape) {
			SupportedShape[] values = SupportedShape.values();
			for (SupportedShape type : values) {
				if (type.awtType().isInstance(shape))
					return type;
			}
		}
		return null;
	}

}
