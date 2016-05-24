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

package org.arakhne.afc.math.continous.object2d;

import org.arakhne.afc.math.generic.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiablePoint2D;

/** This class implements a Point2f that cannot be modified by
 * the setters.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link UnmodifiablePoint2D}
 */
@Deprecated
@SuppressWarnings("all")
public class UnmodifiablePoint2f extends Point2f {

	private static final long serialVersionUID = -8670105082548919880L;

	/**
	 */
	public UnmodifiablePoint2f() {
		super();
	}

	/**
	 * @param x
	 * @param y
	 */
	public UnmodifiablePoint2f(float x, float y) {
		super(x, y);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UnmodifiablePoint2f clone() {
		return (UnmodifiablePoint2f)super.clone();
	}

	@Override
	public void set(float x, float y) {
		//
	}

	@Override
	public void set(float[] t) {
		//
	}
	
	@Override
	public void set(int x, int y) {
		//
	}
	
	@Override
	public void set(int[] t) {
		//
	}
	
	@Override
	public void set(Tuple2D<?> t1) {
		//
	}
	
	@Override
	public void setX(float x) {
		//
	}
	
	@Override
	public void setX(int x) {
		//
	}
	
	@Override
	public void setY(float y) {
		//
	}
	
	@Override
	public void setY(int y) {
		//
	}
	
}
