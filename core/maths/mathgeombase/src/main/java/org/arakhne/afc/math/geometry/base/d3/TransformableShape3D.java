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

package org.arakhne.afc.math.geometry.base.d3;

import org.arakhne.afc.vmutil.annotations.ScalaOperator;
import org.arakhne.afc.vmutil.annotations.XtextOperator;
import org.eclipse.xtext.xbase.lib.Pure;

/** 3D shape on which transformation could be applied.
 *
 * @param <ST> is the general type of all the shapes.
 * @param <IT> is the type of the implementation of this shape.
 * @param <I> is the type of the iterator used to obtain the elements of the path.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <Q> is the type of the quaternions.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public interface TransformableShape3D<
		ST extends Shape3D<?, I, P, V, Q, B>,
		IT extends Shape3D<?, I, P, V, Q, B>,
		I extends PathIterator3D<?>,
		P extends Point3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>,
		B extends Shape3D<?, I, P, V, Q, B>>
	extends Shape3D<IT, I, P, V, Q, B> {

	/** Transform the current segment.
	 * This function changes the current segment.
	 *
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape
	 */
	void transform(Transform3D transform);

    /** Create a new shape by applying the given transformation: {@code this * t}
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * @param t the transformation
     * @return the transformed shape.
     * @see #createTransformedShape(Transform3D)
     */
    @Pure
    @XtextOperator("*")
    ST operator_multiply(Transform3D t);

	/** Create a new shape by applying the given transformation: {@code this * t}
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param t the transformation
	 * @return the transformed shape.
	 * @see #createTransformedShape(Transform3D)
	 */
	@Pure
	@ScalaOperator("*")
	ST $times(Transform3D t);

    /** Apply the transformation to the shape and reply the result.
     * This function does not change the current shape.
     *
     * @param transform is the transformation to apply to the shape.
     * @return the result of the transformation.
     */
    @Pure
    ST createTransformedShape(Transform3D transform);

}
