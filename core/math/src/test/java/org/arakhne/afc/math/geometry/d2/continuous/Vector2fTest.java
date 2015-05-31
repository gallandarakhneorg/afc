/* 
 * $Id$
 * 
 * Copyright (C) 2010-2012 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d2.continuous;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.continuous.Point2f;
import org.arakhne.afc.math.geometry.d2.continuous.Vector2f;
import org.junit.Test;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class Vector2fTest extends AbstractMathTestCase {

	@Test
	public void testClone() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void angleVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void dotVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void perpVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void mulMatrix2f() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void perpendicularize() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void length() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void lengthSquared() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void normalizeVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void normalize() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void signedAngleVector2D() {
		Vector2f v1 = new Vector2f(this.random.nextDouble(), this.random.nextDouble());
		Vector2f v2 = new Vector2f(this.random.nextDouble(), this.random.nextDouble());

		assertEpsilonEquals(
				0.f,
				v1.signedAngle(v1));
		assertEpsilonEquals(
				0.f,
				v2.signedAngle(v2));

		double sAngle1 = v1.signedAngle(v2);
		double sAngle2 = v2.signedAngle(v1);

		assertEpsilonEquals(-sAngle1, sAngle2);
		assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
		assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));

		double sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();

		if (sin < 0) {
			assertTrue(sAngle1 <= 0);
			assertTrue(sAngle2 >= 0);
		} else {
			assertTrue(sAngle1 >= 0);
			assertTrue(sAngle2 <= 0);
		}
	}

	@Test
	public void turnVectorDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void toOrientationVector() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void getOrientationAngle() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void addVector2DVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void addVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void scaleAddIntVector2DVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void scaleAddDoubleVector2DVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void scaleAddIntVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void scaleAddDoubleVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void subVector2DVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void subPoint2DPoint2D() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void subVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setLength() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void isUnitVector() {
		Point2f TestOn[] = {new Point2f(-0.9999469028, -0.0103049327), new Point2f(-0.9759531478, -0.2179803965), new Point2f(-0.9093055572, -0.416129071), new Point2f(-0.8029169503, -0.5960909082), new Point2f(-0.66143702, -0.7500007124), new Point2f(-0.4910491179, -0.8711318866), new Point2f(-0.299200013, -0.9541904172), new Point2f(-0.0942744319, -0.9955462478), new Point2f(0.1147713943, -0.9933919302), new Point2f(0.3188011598, -0.9478216185), new Point2f(0.5088977849, -0.8608269539), new Point2f(0.6767531348, -0.7362100207), new Point2f(0.8150311253, -0.5794171768), new Point2f(0.9176883447, -0.397301022), new Point2f(0.9802381798, -0.1978209061), new Point2f(0.9999469028, 0.0103049327), new Point2f(0.9759531478, 0.2179803965), new Point2f(0.9093055572, 0.416129071), new Point2f(0.8029169503, 0.5960909082), new Point2f(0.66143702, 0.7500007124), new Point2f(0.4910491179, 0.8711318866), new Point2f(0.299200013, 0.9541904172), new Point2f(0.0942744319, 0.9955462478), new Point2f(-0.1147713943, 0.9933919302), new Point2f(-0.3188011598, 0.9478216185), new Point2f(-0.5088977849, 0.8608269539), new Point2f(-0.6767531348, 0.7362100207), new Point2f(-0.8150311253, 0.5794171768), new Point2f(-0.9176883447, 0.397301022), new Point2f(-0.9802381798, 0.1978209061)};
		Point2f TestIn[] = {new Point2f(-0.9999490037, -0.0099995), new Point2f(-0.9760187059, -0.2176820751), new Point2f(-0.9094317071, -0.4158508988), new Point2f(-0.8030981788, -0.5958450429), new Point2f(-0.6616654065, -0.7497978994), new Point2f(-0.4913146808, -0.8709809897), new Point2f(-0.299491146, -0.9540980314), new Point2f(-0.094578411, -0.9955164108), new Point2f(0.1144678544, -0.9934259461), new Point2f(0.3185113253, -0.9479180005), new Point2f(0.5086343229, -0.8609814897), new Point2f(0.6765275599, -0.7364159563), new Point2f(0.8148532962, -0.579665512), new Point2f(0.9175660333, -0.3975809032), new Point2f(0.9801767318, -0.1981201011), new Point2f(0.9999490037, 0.0099995), new Point2f(0.9760187059, 0.2176820751), new Point2f(0.9094317071, 0.4158508988), new Point2f(0.8030981788, 0.5958450429), new Point2f(0.6616654065, 0.7497978994), new Point2f(0.4913146808, 0.8709809897), new Point2f(0.299491146, 0.9540980314), new Point2f(0.094578411, 0.9955164108), new Point2f(-0.1144678544, 0.9934259461), new Point2f(-0.3185113253, 0.9479180005), new Point2f(-0.5086343229, 0.8609814897), new Point2f(-0.6765275599, 0.7364159563), new Point2f(-0.8148532962, 0.579665512), new Point2f(-0.9175660333, 0.3975809032), new Point2f(-0.9801767318, 0.1981201011)};
		Point2f TestOut[] = {new Point2f(-0.9999388432, -0.0111494339), new Point2f(-0.9757696827, -0.2188047677), new Point2f(-0.9089547048, -0.4168972831), new Point2f(-0.8024140446, -0.5967693867), new Point2f(-0.6608040403, -0.7505598046), new Point2f(-0.4903137286, -0.8715471574), new Point2f(-0.2983943541, -0.9544437173), new Point2f(-0.0934337145, -0.9956265068), new Point2f(0.1156104268, -0.9932956404), new Point2f(0.3196018377, -0.9475529882), new Point2f(0.5096251146, -0.8603977235), new Point2f(0.6773753286, -0.7356389496), new Point2f(0.8155209903, -0.5787292237), new Point2f(0.9180244714, -0.3965262536), new Point2f(0.980405878, -0.1969931836), new Point2f(0.9999388432, 0.0111494339), new Point2f(0.9757696827, 0.2188047677), new Point2f(0.9089547048, 0.4168972831), new Point2f(0.8024140446, 0.5967693867), new Point2f(0.6608040403, 0.7505598046), new Point2f(0.4903137286, 0.8715471574), new Point2f(0.2983943541, 0.9544437173), new Point2f(0.0934337145, 0.9956265068), new Point2f(-0.1156104268, 0.9932956404), new Point2f(-0.3196018377, 0.9475529882), new Point2f(-0.5096251146, 0.8603977235), new Point2f(-0.6773753286, 0.7356389496), new Point2f(-0.8155209903, 0.5787292237), new Point2f(-0.9180244714, 0.3965262536), new Point2f(-0.980405878, 0.1969931836)};
		
		for (int i = 0; i < TestOn.length; i++) {
			assertTrue("i " + i, new Vector2f(TestOn[i].getX(), TestOn[i].getY()).isUnitVector());
		}
		for (int i = 0; i < TestIn.length; i++) {
			assertFalse("i " + i, new Vector2f(TestIn[i].getX(), TestIn[i].getY()).isUnitVector());
		}
		for (int i = 0; i < TestOut.length; i++) {
			assertFalse("i " + i, new Vector2f(TestOut[i].getX(), TestOut[i].getY()).isUnitVector());
		}	
	}

	@Test
	public void angleOfVectorDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void angleOfVectorDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void signedAngleDoubleDoubleDoubleDouble() {
		Vector2f v1 = new Vector2f(this.random.nextDouble(), this.random.nextDouble());
		Vector2f v2 = new Vector2f(this.random.nextDouble(), this.random.nextDouble());

		assertEpsilonEquals(
				0.f,
				Vector2f.signedAngle(v1.getX(), v1.getY(), v1.getX(),
						v1.getY()));
		assertEpsilonEquals(
				0.f,
				Vector2f.signedAngle(v2.getX(), v2.getY(), v2.getX(),
						v2.getY()));

		double sAngle1 = Vector2f.signedAngle(v1.getX(), v1.getY(),
				v2.getX(), v2.getY());
		double sAngle2 = Vector2f.signedAngle(v2.getX(), v2.getY(),
				v1.getX(), v1.getY());

		assertEpsilonEquals(-sAngle1, sAngle2);
		assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
		assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));

		double sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();

		if (sin < 0) {
			assertTrue(sAngle1 <= 0);
			assertTrue(sAngle2 >= 0);
		} else {
			assertTrue(sAngle1 >= 0);
			assertTrue(sAngle2 <= 0);
		}
	}

	@Test
	public void perpProductDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void dotProductDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

}