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

package org.arakhne.afc.math;


/**
 * Define a mathematic function.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated since 13.0, see {@link org.arakhne.afc.math.stochastic.MathFunction}
 */
@Deprecated
@SuppressWarnings("checkstyle:all")
public interface MathFunction {

	/** Replies the value of the function.
	 *
	 * @param x
	 * @return the value of {@code f(x)}.
	 * @throws MathException in case {@code f(x)} could not be computed
	 */
	public double f(double x)  throws MathException;

	/** Replies the range of the function.
	 *
	 * @return a array of ranges. It corresponds to a list of couples that defined the set of valid values.
	 */
	public MathFunctionRange[] getRange();

}
