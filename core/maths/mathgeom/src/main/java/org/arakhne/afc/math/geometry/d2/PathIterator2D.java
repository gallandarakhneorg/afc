/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d2;

import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.PathWindingRule;

/** This interface describes an interator on path elements.
 *
 * @param <T> the type of the path elements.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface PathIterator2D<T extends PathElement2D> extends Iterator<T> {

	/** Replies the winding rule for the path.
	 *
	 * @return the winding rule for the path.
	 */
	@Pure
	PathWindingRule getWindingRule();

	/** Replies the path is composed only by
	 * one {@code MOVE_TO}, and a sequence of {@code LINE_TO}
	 * primitives.
	 *
	 * @return {@code true} if the path does not
	 *     contain curve primitives, {@code false}
	 *     otherwise.
	 */
	@Pure
	boolean isPolyline();

	/** Replies the path contains a curve.
	 *
	 * @return {@code true} if the path contains
	 *     curve primitives, {@code false}
	 *     otherwise.
	 */
	@Pure
	boolean isCurved();

	/** Replies the path has multiple parts, i.e. multiple {@code MOVE_TO} are inside.
	 * primitives.
	 *
	 * @return {@code true} if the path has multiple move-to primitive, {@code false}
	 *     otherwise.
	 */
	@Pure
	boolean isMultiParts();

	/** Replies the path is composed only by
	 * one {@code MOVE_TO}, a sequence of {@code LINE_TO}
	 * or {@code QUAD_TO} or {@code CURVE_TO}
	 * or {@code ARC_TO}, and a
	 * single {@code CLOSE} primitives.
	 *
	 * @return {@code true} if the path does not
	 *     contain curve primitives, {@code false}
	 *     otherwise.
	 */
	@Pure
	boolean isPolygon();

	/** Replies a reset instance of this iterator.
	 *
	 * <p>The reset instance enables to restart iterations with the replied iterator.
	 *
	 * @return the reset iterator.
	 */
	PathIterator2D<T> restartIterations();

	/** Replies the factory of geometrical elements.
	 *
	 * @return the factory.
	 */
	GeomFactory2D<?, ?> getGeomFactory();

}
