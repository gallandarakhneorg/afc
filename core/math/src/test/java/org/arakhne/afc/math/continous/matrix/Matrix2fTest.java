/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math.continous.matrix;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.matrix.Matrix2f;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Matrix2fTest extends AbstractMathTestCase {
	
	/**
	 */
	public void testCovMatrix2Tuple2dArray_theory() {
		Vector2f v1 = new Vector2f(1, 3);
		Vector2f v2 = new Vector2f(4, -2);
		Vector2f m = new Vector2f();
		m.add(v1,v2);
		m.scale(.5f);
		
		Matrix2f expected = new Matrix2f();
		expected.m00 = ((v1.getX()-m.getX()) * (v1.getX()-m.getX()) + (v2.getX()-m.getX()) * (v2.getX()-m.getX())) / 2f;
		expected.m01 = ((v1.getX()-m.getX()) * (v1.getY()-m.getY()) + (v2.getX()-m.getX()) * (v2.getY()-m.getY())) / 2f;
		expected.m10 = ((v1.getY()-m.getY()) * (v1.getX()-m.getX()) + (v2.getY()-m.getY()) * (v2.getX()-m.getX())) / 2f;
		expected.m11 = ((v1.getY()-m.getY()) * (v1.getY()-m.getY()) + (v2.getY()-m.getY()) * (v2.getY()-m.getY())) / 2f;
		
		Matrix2f mat = new Matrix2f();
		Tuple2f<?> mean = mat.cov(v1, v2);
		
		assertEpsilonEquals(m, mean);
		for(int i=0; i<2; ++i) {
			for(int j=0; j<2; ++j) {
				assertEpsilonEquals("i="+i+"; j="+j, //$NON-NLS-1$ //$NON-NLS-2$
						expected.getElement(i, j), 
						mat.getElement(i, j));
			}
		}
	}
	
	/**
	 */
	public void testCovMatrix2Tuple2dArray_example() {
		// From "Mathematics for 3D Game Programming and Computer Graphics" pp.220
		// Adapted to 2D by Stephane Galland
		//
		// P1 = [ -1, -2 ]
		// P2 = [ 1, 0 ]
		// P3 = [ 2, -1 ]
		// P4 = [ 2, -1 ]
		//
		// average: m = [ 1, -1 ]
		//
		// Cov = [ 1.5 ,  .5 ]
		//       [  .5 ,  .5 ]
		
		Point2f p1 = new Point2f(-1, -2);
		Point2f p2 = new Point2f(1, 0);
		Point2f p3 = new Point2f(2, -1);
		Point2f p4 = new Point2f(2, -1);
		
		Matrix2f cov = new Matrix2f();
		Tuple2f<?> mean = cov.cov(p1, p2, p3, p4);
		
		Point2f expectedMean = new Point2f(1, -1); 
		Matrix2f expectedCov = new Matrix2f();
		expectedCov.m00 = 1.5f;
		expectedCov.m01 = .5f;
		expectedCov.m10 = .5f;
		expectedCov.m11 = .5f;
		
		assertEpsilonEquals(expectedMean, mean);
		for(int i=0; i<2; ++i) {
			for(int j=0; j<2; ++j) {
				assertEpsilonEquals("i="+i+"; j="+j, //$NON-NLS-1$ //$NON-NLS-2$
						expectedCov.getElement(i, j), 
						cov.getElement(i, j));
			}
		}
	}
	
}