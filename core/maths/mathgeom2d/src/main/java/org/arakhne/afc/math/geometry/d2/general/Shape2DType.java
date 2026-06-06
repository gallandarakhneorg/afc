/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d2.general;

import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Ellipse2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.math.geometry.d2.ai.Circle2ai;
import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.eclipse.xtext.xbase.lib.Pure;

/** Type of 2D shape.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public enum Shape2DType {

	/** A 2D rectangle that is aligned on the global axes.
	 */
	RECTANGLE {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredContinuousShapeType() {
			return Rectangle2afp.class;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredDiscreteShapeType() {
			return Rectangle2ai.class;
		}
	},

	/** A 2D rectangle that is not aligned on the global axes.
	 */
	ORIENTED_RECTANGLE {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredContinuousShapeType() {
			return OrientedRectangle2afp.class;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredDiscreteShapeType() {
			return null;
		}
	},

	/** A 2D rectangle that is aligned on the global axes and with rounded corners.
	 */
	ROUND_RECTANGLE {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredContinuousShapeType() {
			return RoundRectangle2afp.class;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredDiscreteShapeType() {
			return null;
		}
	},

	/** A 2D circle.
	 */
	CIRCLE {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredContinuousShapeType() {
			return Circle2afp.class;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredDiscreteShapeType() {
			return Circle2ai.class;
		}
	},

	/** A 2D Ellipse.
	 */
	ELLIPSE {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredContinuousShapeType() {
			return Ellipse2afp.class;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredDiscreteShapeType() {
			return null;
		}
	},

	/** A AD segment or line.
	 */
	SEGMENT {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredContinuousShapeType() {
			return Segment2afp.class;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredDiscreteShapeType() {
			return Segment2ai.class;
		}
	},

	/** A 2D triangle.
	 */
	TRIANGLE {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredContinuousShapeType() {
			return Triangle2afp.class;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredDiscreteShapeType() {
			return null;
		}
	},

	/** A 2D general path.
	 */
	PATH {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredContinuousShapeType() {
			return Path2afp.class;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredDiscreteShapeType() {
			return Path2ai.class;
		}
	},

	/** A shape composed of multiple disjoint shapes.
	 */
	MULTISHAPE {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredContinuousShapeType() {
			return MultiShape2afp.class;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredDiscreteShapeType() {
			return MultiShape2ai.class;
		}
	},

	/** A 2D parallelogram.
	 */
	PARALLELOGRAM {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredContinuousShapeType() {
			return Parallelogram2afp.class;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape2D> getPreferredDiscreteShapeType() {
			return null;
		}
	};

	/** Replies the preferred Shape2D java type for this type of shape.
	 *
	 * @return the Java type.
	 */
	@Pure
	@SuppressWarnings("rawtypes")
	public abstract Class<? extends Shape2D> getPreferredContinuousShapeType();

	/** Replies the preferred Shape2D java type for this type of shape.
	 *
	 * @return the Java type.
	 */
	@Pure
	@SuppressWarnings("rawtypes")
	public abstract Class<? extends Shape2D> getPreferredDiscreteShapeType();

}
