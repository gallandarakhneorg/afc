/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
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
package org.arakhne.afc.math.matrix;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.matrix.Transform2D;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Transform2DTest extends AbstractMathTestCase {

	private Transform2D t;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.t = new Transform2D(1f, 2f, 3f, 4f, 5f, 6f);
	}

	@Override
	protected void tearDown() throws Exception {
		this.t = null;
		super.tearDown();
	}

	/**
	 */
	public void testSetTranslationFloatFloat() {
		this.t.setTranslation(3.4f, 5.6f);
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(3.4f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(5.6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testSetTranslationTuple2D() {
		this.t.setTranslation(new Point2f(3.4f, 5.6f));
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(3.4f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(5.6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testTranslateFloatFloat() {
		this.t.translate(3.4f, 5.6f);
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(17.6f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(47.6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());

		this.t.translate(-3.4f, -5.6f);
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testTranslateFloatFloat_withRot() {
		this.t.translate(3.4f, 5.6f);
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(17.6f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(47.6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());

		this.t.rotate(6.7f);        

		this.t.translate(-3.4f, -5.6f);
		assertEpsilonEquals(1.724082989468516f, this.t.getM00());
		assertEpsilonEquals(1.423916375854041f, this.t.getM01());
		assertEpsilonEquals(3.764186131024417f, this.t.getM02());
		assertEpsilonEquals(5.681782196024269f, this.t.getM10());
		assertEpsilonEquals(2.952516058710204f, this.t.getM11());
		assertEpsilonEquals(11.747850604740339f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testTranslateVector2f() {
		this.t.translate(new Vector2f(3.4f, 5.6f));
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(17.6f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(47.6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());

		this.t.translate(new Vector2f(-3.4f, -5.6f));
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testTranslateVector2f_withRot() {
		this.t.translate(new Vector2f(3.4f, 5.6f));
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(17.6f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(47.6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());

		this.t.rotate(6.7f);        

		this.t.translate(new Vector2f(-3.4f, -5.6f));
		assertEpsilonEquals(1.724082989468516f, this.t.getM00());
		assertEpsilonEquals(1.423916375854041f, this.t.getM01());
		assertEpsilonEquals(3.764186131024417f, this.t.getM02());
		assertEpsilonEquals(5.681782196024269f, this.t.getM10());
		assertEpsilonEquals(2.952516058710204f, this.t.getM11());
		assertEpsilonEquals(11.747850604740339f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testGetTranslationX() {
		assertEpsilonEquals(this.t.getM02(), this.t.getTranslationX());
	}

	/**
	 */
	public void testGetTranslationY() {
		assertEpsilonEquals(this.t.getM12(), this.t.getTranslationY());
	}

	/**
	 */
	public void testGetTranslationVector() {
		Vector2f v = this.t.getTranslationVector();
		assertEpsilonEquals(this.t.getM02(), v.getX());
		assertEpsilonEquals(this.t.getM12(), v.getY());
	}

	/**
	 */
	public void testGetRotation() {
		assertEpsilonEquals(0f, this.t.getRotation());
		this.t.setRotation(6.7f);
		assertEpsilonEquals(MathUtil.clampRadianMinusPIToPI(6.7f), this.t.getRotation());
	}

	/**
	 */
	public void testSetRotationFloat() {
		this.t.setRotation(6.7f);
		assertEpsilonEquals(0.914383148235319f, this.t.getM00());
		assertEpsilonEquals(-0.404849920616598f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(0.404849920616598f, this.t.getM10());
		assertEpsilonEquals(0.914383148235319f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testRotateFloat() {
		this.t.rotate(6.7f);
		assertEpsilonEquals(1.724082989468516f, this.t.getM00());
		assertEpsilonEquals(1.423916375854041f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(5.681782196024269f, this.t.getM10());
		assertEpsilonEquals(2.952516058710204f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());

		this.t.rotate(-6.7f);
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testRotate_withTrans() {
		this.t.rotate(6.7f);
		assertEpsilonEquals(1.724082989468516f, this.t.getM00());
		assertEpsilonEquals(1.423916375854041f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(5.681782196024269f, this.t.getM10());
		assertEpsilonEquals(2.952516058710204f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());

		this.t.translate(3.4f, 5.6f);

		this.t.rotate(-6.7f);
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(16.835813868975581f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(41.852149395259651f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testSetScaleFloatFloat() {
		this.t.setScale(8.9f, 10.11f);
		assertEpsilonEquals(8.9f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(10.11f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testSetScaleTuple2D() {
		this.t.setScale(new Vector2f(8.9f, 10.11f));
		assertEpsilonEquals(8.9f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(10.11f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testScaleFloatFloat() {
		this.t.scale(8.9f, 10.11f);
		assertEpsilonEquals(8.9f, this.t.getM00());
		assertEpsilonEquals(20.22f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(35.6f, this.t.getM10());
		assertEpsilonEquals(50.55f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());

		this.t.scale(1f/8.9f, 1f/10.11f);
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testScaleFloatFloat_withTrans() {
		this.t.scale(8.9f, 10.11f);
		assertEpsilonEquals(8.9f, this.t.getM00());
		assertEpsilonEquals(20.22f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(35.6f, this.t.getM10());
		assertEpsilonEquals(50.55f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());

		this.t.translate(3.4f, 5.6f);

		this.t.scale(1f/8.9f, 1f/10.11f);
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(146.492f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(410.12f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testScaleFloatFloat_withRot() {
		this.t.scale(8.9f, 10.11f);
		assertEpsilonEquals(8.9f, this.t.getM00());
		assertEpsilonEquals(20.22f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(35.6f, this.t.getM10());
		assertEpsilonEquals(50.55f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());

		this.t.rotate(6.7f);

		this.t.scale(1f/8.9f, 1f/10.11f);
		assertEpsilonEquals(1.834165776872130f, this.t.getM00());
		assertEpsilonEquals(1.472370223919924f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(5.956989164533305f, this.t.getM10());
		assertEpsilonEquals(3.146331450973738f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testScaleTuple2D() {
		this.t.scale(new Vector2f(8.9f, 10.11f));
		assertEpsilonEquals(8.9f, this.t.getM00());
		assertEpsilonEquals(20.22f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(35.6f, this.t.getM10());
		assertEpsilonEquals(50.55f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());

		this.t.scale(new Vector2f(1f/8.9f, 1f/10.11f));
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testScaleTuple2D_withTrans() {
		this.t.scale(new Vector2f(8.9f, 10.11f));
		assertEpsilonEquals(8.9f, this.t.getM00());
		assertEpsilonEquals(20.22f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(35.6f, this.t.getM10());
		assertEpsilonEquals(50.55f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());

		this.t.translate(3.4f, 5.6f);

		this.t.scale(new Vector2f(1f/8.9f, 1f/10.11f));
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(2f, this.t.getM01());
		assertEpsilonEquals(146.492f, this.t.getM02());
		assertEpsilonEquals(4f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(410.12f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testScaleTuple2D_withRot() {
		this.t.scale(new Vector2f(8.9f, 10.11f));
		assertEpsilonEquals(8.9f, this.t.getM00());
		assertEpsilonEquals(20.22f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(35.6f, this.t.getM10());
		assertEpsilonEquals(50.55f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());

		this.t.rotate(6.7f);

		this.t.scale(new Vector2f(1f/8.9f, 1f/10.11f));
		assertEpsilonEquals(1.834165776872130f, this.t.getM00());
		assertEpsilonEquals(1.472370223919924f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(5.956989164533305f, this.t.getM10());
		assertEpsilonEquals(3.146331450973738f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testGetScaleX() {
		assertEpsilonEquals(this.t.getM00(), this.t.getScaleX());
	}

	/**
	 */
	public void testGetScaleY() {
		assertEpsilonEquals(this.t.getM11(), this.t.getScaleY());
	}

	/**
	 */
	public void testGetScaleVector() {
		Vector2f v = this.t.getScaleVector();
		assertEpsilonEquals(this.t.getM00(), v.getX());
		assertEpsilonEquals(this.t.getM11(), v.getY());
	}

	/**
	 */
	public void testSetShearFloatFloat() {
		this.t.setShear(8.9f, 10.11f);
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(8.9f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(10.11f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testSetShearTuple2D() {
		this.t.setShear(new Vector2f(8.9f, 10.11f));
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(8.9f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(10.11f, this.t.getM10());
		assertEpsilonEquals(5f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testShearFloatFloat() {
		this.t.shear(8.9f, 10.11f);
		assertEpsilonEquals(21.22f, this.t.getM00());
		assertEpsilonEquals(10.9f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(54.55f, this.t.getM10());
		assertEpsilonEquals(40.6f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());

		this.t.shear(-8.9f, -10.11f);
		assertEpsilonEquals(-88.979f, this.t.getM00());
		assertEpsilonEquals(-177.958f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(-355.916f, this.t.getM10());
		assertEpsilonEquals(-444.895f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testShearTuple2D() {
		this.t.shear(new Vector2f(8.9f, 10.11f));
		assertEpsilonEquals(21.22f, this.t.getM00());
		assertEpsilonEquals(10.9f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(54.55f, this.t.getM10());
		assertEpsilonEquals(40.6f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());

		this.t.shear(new Vector2f(-8.9f, -10.11f));
		assertEpsilonEquals(-88.979f, this.t.getM00());
		assertEpsilonEquals(-177.958f, this.t.getM01());
		assertEpsilonEquals(3f, this.t.getM02());
		assertEpsilonEquals(-355.916f, this.t.getM10());
		assertEpsilonEquals(-444.895f, this.t.getM11());
		assertEpsilonEquals(6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testGetShearX() {
		assertEpsilonEquals(this.t.getM01(), this.t.getShearX());
	}

	/**
	 */
	public void testGetShearY() {
		assertEpsilonEquals(this.t.getM10(), this.t.getShearY());
	}

	/**
	 */
	public void testGetShearVector() {
		Vector2f v = this.t.getShearVector();
		assertEpsilonEquals(this.t.getM01(), v.getX());
		assertEpsilonEquals(this.t.getM10(), v.getY());
	}

	/**
	 */
	public void testMakeRotationMatrix() {
		this.t.makeRotationMatrix(6.7f);
		assertEpsilonEquals(0.914383148235319f, this.t.getM00());
		assertEpsilonEquals(-0.404849920616598f, this.t.getM01());
		assertEpsilonEquals(0f, this.t.getM02());
		assertEpsilonEquals(0.404849920616598f, this.t.getM10());
		assertEpsilonEquals(0.914383148235319f, this.t.getM11());
		assertEpsilonEquals(0f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testMakeTranslationMatrix() {
		this.t.makeTranslationMatrix(3.4f, 5.6f);
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(0f, this.t.getM01());
		assertEpsilonEquals(3.4f, this.t.getM02());
		assertEpsilonEquals(0f, this.t.getM10());
		assertEpsilonEquals(1f, this.t.getM11());
		assertEpsilonEquals(5.6f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testMakeScaleMatrix() {
		this.t.makeScaleMatrix(8.9f, 10.11f);
		assertEpsilonEquals(8.9f, this.t.getM00());
		assertEpsilonEquals(0f, this.t.getM01());
		assertEpsilonEquals(0f, this.t.getM02());
		assertEpsilonEquals(0f, this.t.getM10());
		assertEpsilonEquals(10.11f, this.t.getM11());
		assertEpsilonEquals(0f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testMakeShearMatrix() {
		this.t.makeShearMatrix(8.9f, 10.11f);
		assertEpsilonEquals(1f, this.t.getM00());
		assertEpsilonEquals(8.9f, this.t.getM01());
		assertEpsilonEquals(0f, this.t.getM02());
		assertEpsilonEquals(10.11f, this.t.getM10());
		assertEpsilonEquals(1f, this.t.getM11());
		assertEpsilonEquals(0f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testTransformTuple2D() {
		Point2f p = new Point2f();

		p.set(2.3f, 4.5f);
		this.t.transform(p);
		assertEpsilonEquals(14.3f, p.getX());
		assertEpsilonEquals(37.7f, p.getY());

		p.set(-2.3f, -4.5f);
		this.t.transform(p);
		assertEpsilonEquals(-8.3f, p.getX());
		assertEpsilonEquals(-25.7f, p.getY());
	}

	/**
	 */
	public void testTransformFloatFloat() {
		Point2D p;

		p = this.t.transform(2.3f, 4.5f);
		assertEpsilonEquals(14.3f, p.getX());
		assertEpsilonEquals(37.7f, p.getY());

		p = this.t.transform(-2.3f, -4.5f);
		assertEpsilonEquals(-8.3f, p.getX());
		assertEpsilonEquals(-25.7f, p.getY());
	}

	/**
	 */
	public void testTransformTuple2DTuple2D() {
		Point2f p = new Point2f();
		Point2f r = new Point2f();

		p.set(2.3f, 4.5f);
		this.t.transform(p, r);
		assertEpsilonEquals(2.3f, p.getX());
		assertEpsilonEquals(4.5f, p.getY());
		assertEpsilonEquals(14.3f, r.getX());
		assertEpsilonEquals(37.7f, r.getY());

		p.set(-2.3f, -4.5f);
		this.t.transform(p,r);
		assertEpsilonEquals(-2.3f, p.getX());
		assertEpsilonEquals(-4.5f, p.getY());
		assertEpsilonEquals(-8.3f, r.getX());
		assertEpsilonEquals(-25.7f, r.getY());
	}

	/**
	 */
	public void testSet() {
		this.t.set(1.2f, 3.4f, 5.6f, 7.8f, 9.10f, 11.12f);
		assertEpsilonEquals(1.2f, this.t.getM00());
		assertEpsilonEquals(3.4f, this.t.getM01());
		assertEpsilonEquals(5.6f, this.t.getM02());
		assertEpsilonEquals(7.8f, this.t.getM10());
		assertEpsilonEquals(9.10f, this.t.getM11());
		assertEpsilonEquals(11.12f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

	/**
	 */
	public void testCreateInverse() {
		Transform2D ti = this.t.createInverse();
		assertEpsilonEquals(-1.666666666666667f, ti.getM00());
		assertEpsilonEquals(0.666666666666667f, ti.getM01());
		assertEpsilonEquals(1f, ti.getM02());
		assertEpsilonEquals(1.333333333333333f, ti.getM10());
		assertEpsilonEquals(-0.333333333333333f, ti.getM11());
		assertEpsilonEquals(-2f, ti.getM12());
		assertEpsilonEquals(0f, ti.getM20());
		assertEpsilonEquals(0f, ti.getM21());
		assertEpsilonEquals(1f, ti.getM22());
	}

	/**
	 */
	public void testInvert() {
		this.t.invert();
		assertEpsilonEquals(-1.666666666666667f, this.t.getM00());
		assertEpsilonEquals(0.666666666666667f, this.t.getM01());
		assertEpsilonEquals(1f, this.t.getM02());
		assertEpsilonEquals(1.333333333333333f, this.t.getM10());
		assertEpsilonEquals(-0.333333333333333f, this.t.getM11());
		assertEpsilonEquals(-2f, this.t.getM12());
		assertEpsilonEquals(0f, this.t.getM20());
		assertEpsilonEquals(0f, this.t.getM21());
		assertEpsilonEquals(1f, this.t.getM22());
	}

}