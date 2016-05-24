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
