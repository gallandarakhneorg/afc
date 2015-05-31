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

import java.util.Map;
import java.util.Random;

import org.arakhne.afc.math.MathException;
import org.arakhne.afc.math.MathFunctionRange;

/**
 * Law that representes a triangular density.
 * <p>
 * Reference:
 * <a href="http://mathworld.wolfram.com/TriangularDistribution.html">Triangular Distribution</a>.
 * <p>
 * This class uses the uniform random number distribution provided by {@link Random}. 
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class TriangularStochasticLaw extends StochasticLaw {
	
	/** Replies a random value that respect
	 * the current stochastic law.
	 * 
	 * @param minX is the lower bound where {@code f(minX) = 0}
	 * @param mode is the maxima point of the distribution {@code f(mode) = max(f(x))} 
	 * @param maxX is the upper bound where {@code f(maxX) = 0}
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException 
	 */
	public static double random(double minX, double mode, double maxX) throws MathException {
		return StochasticGenerator.generateRandomValue(new TriangularStochasticLaw(minX, mode, maxX));
	}

	private final double minX;
	private final double mode;
	private final double maxX;
	
	private final double Dxmode;
	private final double delta1;
	private final double delta2;
	
	/**
	 * Construct a law with the following parameters.
	 * <ul>
	 * <li><code>minX</code></li>
	 * <li><code>mode</code></li>
	 * <li><code>maxX</code></li>
	 * </ul>
	 * 
	 * @param parameters is the set of accepted paramters.
	 * @throws LawParameterNotFoundException if the list of parameters does not permits to create the law.
	 */
	public TriangularStochasticLaw(Map<String,String> parameters) throws LawParameterNotFoundException {
		this(paramFloat("minX",parameters), //$NON-NLS-1$
			paramFloat("mode",parameters), //$NON-NLS-1$
			paramFloat("maxX",parameters)); //$NON-NLS-1$
	}

	/**
	 * @param minX is the lower bound where {@code f(minX) = 0}
	 * @param mode is the maxima point of the distribution {@code f(mode) = max(f(x))} 
	 * @param maxX is the upper bound where {@code f(maxX) = 0}
	 */
	public TriangularStochasticLaw(double minX, double mode, double maxX) {
		double t;
		double i = minX;
		double a = maxX;
		double m = mode;
		if (i>a) {
			t = a;
			a = i;
			i = t;
		}
		if (i>m) {
			t = m;
			m = i;
			i = t;
		}
		if (m>a) {
			t = a;
			a = m;
			m = t;
		}
		this.minX = i;
		this.mode = m;
		this.maxX = a;

		
		this.delta1 = (this.maxX-this.minX)*(this.mode-this.minX);
		this.delta2 = (this.maxX-this.minX)*(this.maxX-this.mode);

		this.Dxmode = (this.mode-this.minX) / (this.maxX-this.minX);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("TRIANGULAR(minX="); //$NON-NLS-1$
		b.append(this.minX);
		b.append(",mode="); //$NON-NLS-1$
		b.append(this.mode);
		b.append(",maxX="); //$NON-NLS-1$
		b.append(this.maxX);
		b.append(')');
		return b.toString();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double f(double x)  throws MathException {
		if ((x<this.minX)||(x>this.maxX))
			throw new OutsideDomainException(x);
		
		double denom = (this.maxX-this.minX);
		
		if (x <= this.mode) {
			double xm = 2.f * (x - this.minX);
			return xm / (denom * (this.mode - this.minX));
		}
		
		double xm = 2.f * (this.maxX - x);
		return 1.f - (xm / (denom * (this.maxX - this.mode)));
			
	}

	/** {@inheritDoc}
	 */
	@Override
	public MathFunctionRange[] getRange() {
		return MathFunctionRange.createSet(this.minX, this.maxX);
	}
	
	/** Replies the x according to the value of the distribution function.
	 * 
	 * @param u is a value given by the uniform random variable generator {@code U(0,1)}.
	 * @return {@code F<sup>-1</sup>(u)}
	 * @throws MathException in case {@code F<sup>-1</sup>(u)} could not be computed
	 */
	@Override
	public double inverseF(double u) throws MathException {
		if ((u<0)||(u>1)) throw new OutsideDomainException(u);
		
		if (u<this.Dxmode)
			return Math.sqrt(u * this.delta1) + this.minX;
		
		return this.maxX - Math.sqrt((1.f-u)*this.delta2);
	}

}