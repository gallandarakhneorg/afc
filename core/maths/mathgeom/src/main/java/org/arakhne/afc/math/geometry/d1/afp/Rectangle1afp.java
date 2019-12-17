/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d1.afp;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d1.Point1D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.Vector1D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Fonctional interface that represented a 2D rectangle on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <S> is the type of the segments.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("checkstyle:methodcount")
public interface Rectangle1afp<
		ST extends Shape1afp<?, ?, P, V, S, B>,
		IT extends Rectangle1afp<?, ?, P, V, S, B>,
		P extends Point1D<? super P, ? super V, ? super S>,
		V extends Vector1D<? super V, ? super P, ? super S>,
		S extends Segment1D<?, ?>,
		B extends Rectangle1afp<?, ?, P, V, S, B>>
			extends RectangularShape1afp<ST, IT, P, V, S, B> {

    @Pure
    @Override
    default double getDistanceSquared(Point1D<?, ?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        if (pt.getSegment() != getSegment()) {
        	throw new IllegalStateException();
        }
        final double dx;
        if (pt.getX() < getMinX()) {
            dx = getMinX() - pt.getX();
        } else if (pt.getX() > getMaxX()) {
            dx = pt.getX() - getMaxX();
        } else {
            dx = 0f;
        }
        final double dy;
        if (pt.getY() < getMinY()) {
            dy = getMinY() - pt.getY();
        } else if (pt.getY() > getMaxY()) {
            dy = pt.getY() - getMaxY();
        } else {
            dy = 0f;
        }
        return dx * dx + dy * dy;
    }

    @Pure
    @Override
    default double getDistanceL1(Point1D<?, ?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        if (pt.getSegment() != getSegment()) {
        	throw new IllegalStateException();
        }
        final double dx;
        if (pt.getX() < getMinX()) {
            dx = getMinX() - pt.getX();
        } else if (pt.getX() > getMaxX()) {
            dx = pt.getX() - getMaxX();
        } else {
            dx = 0f;
        }
        final double dy;
        if (pt.getY() < getMinY()) {
            dy = getMinY() - pt.getY();
        } else if (pt.getY() > getMaxY()) {
            dy = pt.getY() - getMaxY();
        } else {
            dy = 0f;
        }
        return dx + dy;
    }

    @Pure
    @Override
    default double getDistanceLinf(Point1D<?, ?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        if (pt.getSegment() != getSegment()) {
        	throw new IllegalStateException();
        }
        final double dx;
        if (pt.getX() < getMinX()) {
            dx = getMinX() - pt.getX();
        } else if (pt.getX() > getMaxX()) {
            dx = pt.getX() - getMaxX();
        } else {
            dx = 0f;
        }
        final double dy;
        if (pt.getY() < getMinY()) {
            dy = getMinY() - pt.getY();
        } else if (pt.getY() > getMaxY()) {
            dy = pt.getY() - getMaxY();
        } else {
            dy = 0f;
        }
        return Math.max(dx, dy);
    }

    @Pure
    @Override
    default P getClosestPointTo(Point1D<?, ?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        if (pt.getSegment() != getSegment()) {
        	throw new IllegalStateException();
        }
        final double x;
        int same = 0;
        if (pt.getX() < getMinX()) {
            x = getMinX();
        } else if (pt.getX() > getMaxX()) {
            x = getMaxX();
        } else {
            x = pt.getX();
            ++same;
        }
        final double y;
        if (pt.getY() < getMinY()) {
            y = getMinY();
        } else if (pt.getY() > getMaxY()) {
            y = getMaxY();
        } else {
            y = pt.getY();
            ++same;
        }
        if (same == 2) {
            return getGeomFactory().convertToPoint(pt);
        }
        return getGeomFactory().newPoint(getSegment(), x, y);
    }

    @Override
    default P getClosestPointTo(Rectangle1afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        if (rectangle.getSegment() != getSegment()) {
        	throw new IllegalStateException();
        }
        final P point = getGeomFactory().newPoint(getSegment());
        Rectangle2afp.findsClosestPointRectangleRectangle(getMinX(), getMinY(), getMaxX(), getMaxY(),
                rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY(),
                point);
        return point;
    }

    @Pure
    @Override
    default P getFarthestPointTo(Point1D<?, ?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        if (pt.getSegment() != getSegment()) {
        	throw new IllegalStateException();
        }
        final double x;
        if (pt.getX() <= getCenterX()) {
            x = getMaxX();
        } else {
            x = getMinX();
        }
        final double y;
        if (pt.getY() <= getCenterY()) {
            y = getMaxY();
        } else {
            y = getMinY();
        }
        return getGeomFactory().newPoint(getSegment(), x, y);
    }

    @Pure
    @Override
    default boolean intersects(Rectangle1afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        if (rectangle.getSegment() != getSegment()) {
        	throw new IllegalStateException();
        }
        return Rectangle2afp.intersectsRectangleRectangle(
                getMinX(), getMinY(),
                getMaxX(), getMaxY(),
                rectangle.getMinX(), rectangle.getMinY(),
                rectangle.getMaxX(), rectangle.getMaxY());
    }

	@Pure
	@Override
	default boolean contains(Segment1D<?, ?> segment, double x, double y) {
        if (segment != getSegment()) {
        	throw new IllegalStateException();
        }
		return (x >= getMinX() && x <= getMaxX())
                &&
                (y >= getMinY() && y <= getMaxY());
	}

	@Pure
	@Override
	default boolean contains(Rectangle1afp<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : AssertMessages.notNullParameter();
        if (rectangle.getSegment() != getSegment()) {
        	throw new IllegalStateException();
        }
        return Rectangle2afp.containsRectangleRectangle(
                getMinX(), getMinY(), getMaxX(), getMaxY(),
                rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY());
	}

	@Pure
	@Override
	default boolean equalsToShape(IT shape) {
		if (shape == null) {
            return false;
        }
        if (shape == this) {
            return true;
        }
        return getMinX() == shape.getMinX()
                && getMinY() == shape.getMinY()
                && getMaxX() == shape.getMaxX()
                && getMaxY() == shape.getMaxY();
	}

}
