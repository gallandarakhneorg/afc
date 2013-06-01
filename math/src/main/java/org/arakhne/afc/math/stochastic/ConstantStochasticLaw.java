/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.stochastic;

import java.util.Random;

import org.arakhne.afc.math.MathBounds;
import org.arakhne.afc.math.MathException;

/**
 * Law that representes an uniform density with as its upper and lower bounds
 * equal.
 * <p>
 * This class uses the uniform random number distribution provided by {@link Random}.
 * This class replies the value of the law.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ConstantStochasticLaw extends StochasticLaw {

	private final float value;

	/** Create a constant stochastic law.
	 * 
	 * @param value is the value replied by this law.
	 */
	public ConstantStochasticLaw(float value) {
		this.value = value;
	}

	@Override
	public float inverseF(float u) throws MathException {
		return this.value;
	}

	@Override
	public float f(float x) throws MathException {
		return (x!=this.value) ? 0 : 1.f; 
	}

	@Override
	public MathBounds[] getBounds() {
		return new MathBounds[] {
				new MathBounds(this.value)
		};
	}

}