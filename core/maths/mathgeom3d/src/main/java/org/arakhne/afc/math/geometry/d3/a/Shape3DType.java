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

package org.arakhne.afc.math.geometry.d3.a;

import org.arakhne.afc.math.geometry.base.d3.Shape3D;
import org.eclipse.xtext.xbase.lib.Pure;

/** Type of 3D shape.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public enum Shape3DType {

	/** A 3D box that is aligned on the global axes.
	 */
	ALIGNED_BOX {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape3D> getPreferredContinuousShapeType() {
			return null;
			//TODO: return AlignedBox3afp.class;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape3D> getPreferredDiscreteShapeType() {
			return null;
			//TODO: return AlignedBox3ai.class;
		}
	},

	/** A 3D sphere.
	 */
	SPHERE {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape3D> getPreferredContinuousShapeType() {
			return null;
			//TODO: return Sphere3afp.class;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape3D> getPreferredDiscreteShapeType() {
			return null;
			//TODO: return Sphere3ai.class;
		}
	},

	/** A 3D segment or line.
	 */
	SEGMENT {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape3D> getPreferredContinuousShapeType() {
			return null;
			//TODO: return Segment3afp.class;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape3D> getPreferredDiscreteShapeType() {
			return null;
			//TODO: return Segment3ai.class;
		}
	},

	/** A 3D general path.
	 */
	PATH {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape3D> getPreferredContinuousShapeType() {
			return null;
			//TODO: return Path3afp.class;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape3D> getPreferredDiscreteShapeType() {
			return null;
			//TODO: return Path3ai.class;
		}
	},

	/** A shape composed of multiple disjoint shapes.
	 */
	MULTISHAPE {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape3D> getPreferredContinuousShapeType() {
			return null;
			//TODO: return MultiShape3afp.class;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends Shape3D> getPreferredDiscreteShapeType() {
			return null;
			//TODO: return MultiShape3ai.class;
		}
	};

	/** Replies the preferred Shape3D java type for this type of shape.
	 *
	 * @return the Java type.
	 */
	@Pure
	@SuppressWarnings("rawtypes")
	public abstract Class<? extends Shape3D> getPreferredContinuousShapeType();

	/** Replies the preferred Shape3D java type for this type of shape.
	 *
	 * @return the Java type.
	 */
	@Pure
	@SuppressWarnings("rawtypes")
	public abstract Class<? extends Shape3D> getPreferredDiscreteShapeType();

}
