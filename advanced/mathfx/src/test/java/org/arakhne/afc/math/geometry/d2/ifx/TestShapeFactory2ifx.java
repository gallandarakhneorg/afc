/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.ifx;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.Circle2ai;
import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.arakhne.afc.math.test.geometry.d2.ai.TestShapeFactory;

@SuppressWarnings("all")
public class TestShapeFactory2ifx implements TestShapeFactory<Point2ifx, Vector2ifx, Rectangle2ifx> {
	
	public static final TestShapeFactory2ifx SINGLETON = new TestShapeFactory2ifx();
	
	@Override
	public Segment2ai<?, ?, ?, Point2ifx, Vector2ifx, Rectangle2ifx> createSegment(int x1, int y1, int x2, int y2) {
		return new Segment2ifx(x1, y1, x2, y2);
	}
	
	@Override
	public Rectangle2ifx createRectangle(int x, int y, int width, int height) {
		return new Rectangle2ifx(x, y, width, height);
	}

	@Override
	public Circle2ai<?, ?, ?, Point2ifx, Vector2ifx, Rectangle2ifx> createCircle(int x, int y, int radius) {
		return new Circle2ifx(x, y, radius);
	}
	
	@Override
	public Point2D createPoint(int x, int y) {
		return new Point2ifx(x, y);
	}

	@Override
	public Vector2D createVector(int x, int y) {
		return new Vector2ifx(x, y);
	}

	@Override
	public Path2ai<?, ?, ?, Point2ifx, Vector2ifx, Rectangle2ifx> createPath(PathWindingRule rule) {
		if (rule == null) {
			return new Path2ifx();
		}
		return new Path2ifx(rule);

	}
	
	@Override
	public MultiShape2ai<?, ?, ?, ?, Point2ifx, Vector2ifx, Rectangle2ifx> createMultiShape() {
		return new MultiShape2ifx();
	}

}
