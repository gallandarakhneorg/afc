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

package org.arakhne.afc.math.geometry.d2.i;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.Circle2ai;
import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.arakhne.afc.math.geometry.d2.ai.TestShapeFactory;

@SuppressWarnings("all")
public class TestShapeFactory2i implements TestShapeFactory<Point2i, Vector2i, Rectangle2i> {
	
	public static final TestShapeFactory2i SINGLETON = new TestShapeFactory2i();
	
	public Segment2ai<?, ?, ?, Point2i, Vector2i, Rectangle2i> createSegment(int x1, int y1, int x2, int y2) {
		return new Segment2i(x1, y1, x2, y2);
	}
	
	public Rectangle2i createRectangle(int x, int y, int width, int height) {
		return new Rectangle2i(x, y, width, height);
	}

	public Circle2ai<?, ?, ?, Point2i, Vector2i, Rectangle2i> createCircle(int x, int y, int radius) {
		return new Circle2i(x, y, radius);
	}
	
	public Point2D createPoint(int x, int y) {
		return new Point2i(x, y);
	}

	public Vector2D createVector(int x, int y) {
		return new Vector2i(x, y);
	}

	public Path2ai<?, ?, ?, Point2i, Vector2i, Rectangle2i> createPath(PathWindingRule rule) {
		if (rule == null) {
			return new Path2i();
		}
		return new Path2i(rule);
	}
	
	public MultiShape2ai<?, ?, ?, ?, Point2i, Vector2i, Rectangle2i> createMultiShape() {
		return new MultiShape2i();
	}

}