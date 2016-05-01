/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.geometry.coordinatesystem;

import java.util.Random;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.d2.continuous.Point2f;
import org.arakhne.afc.math.geometry.d2.continuous.Vector2f;
import org.arakhne.afc.math.geometry.d3.continuous.Point3f;
import org.arakhne.afc.math.geometry.d3.continuous.Quaternion;
import org.arakhne.afc.math.geometry.d3.continuous.Transform3D;
import org.arakhne.afc.math.geometry.d3.continuous.Vector3f;
import org.arakhne.afc.math.matrix.Matrix4f;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for {@link CoordinateSystem3D}.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class CoordinateSystem3DTest extends AbstractMathTestCase {

	private final Random random = new Random(); 
	
	private static Quaternion newAxisAngle(double x, double y, double z, double a) {
		Quaternion q = new Quaternion();
		q.setAxisAngle(x, y, z, a);
		return q;
	}
	
	private Quaternion randomQuaternion() {
		Quaternion q = new Quaternion();
		q.setAxisAngle(
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * MathConstants.TWO_PI);
		return q;
	}
	
	private Point3f rotate(Point3f p, Quaternion q) {
		Transform3D m = new Transform3D();
		m.setRotation(q);
		Point3f pp = new Point3f(p);
		m.transform(pp);
		return pp;
	}

	@Test
	public void getDefaultCoordinateSystem() {
		assertSame(CoordinateSystemConstants.SIMULATION_3D,
				CoordinateSystem3D.getDefaultCoordinateSystem());
	}
	/**
	 */
	@Test
	public void getDefaultSimulationCoordinateSystem() {
		CoordinateSystem3D cs = CoordinateSystemConstants.SIMULATION_3D;
		assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND,cs);
	}

	/**
	 */
	@Test
	public void getDefaultJava3DCoordinateSystem() {
		CoordinateSystem3D cs = CoordinateSystemConstants.JAVA3D_3D;
		assertSame(CoordinateSystem3D.XZY_RIGHT_HAND,cs);
	}
	
	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_0() {
		CoordinateSystem3D.fromVectors(0., 0., 0., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_1() {
		CoordinateSystem3D.fromVectors(0., 0., 0., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_2() {
		CoordinateSystem3D.fromVectors(0., 0., 0., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_3() {
		CoordinateSystem3D.fromVectors(0., 0., 1., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_4() {
		CoordinateSystem3D.fromVectors(0., 0., -1., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_5() {
		CoordinateSystem3D.fromVectors(0., 0., 1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_6() {
		CoordinateSystem3D.fromVectors(0., 0., 1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_7() {
		CoordinateSystem3D.fromVectors(0., 0., -1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_8() {
		CoordinateSystem3D.fromVectors(0., 0., -1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_9() {
		CoordinateSystem3D.fromVectors(0., 1., 0., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_10() {
		CoordinateSystem3D.fromVectors(0., -1., 0., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_11() {
		CoordinateSystem3D.fromVectors(0., 1., 0., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_12() {
		CoordinateSystem3D.fromVectors(0., 1., 0., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_13() {
		CoordinateSystem3D.fromVectors(0., -1., 0., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_14() {
		CoordinateSystem3D.fromVectors(0., -1., 0., -1.);
	}

	@Test
	public void fromVectorDoubleDoubleDoubleDouble_15() {
		assertSame(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.fromVectors(
				0., 1., 1., 0.));
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_16() {
		CoordinateSystem3D.fromVectors(0., 1., -1., 0.);
	}

	@Test
	public void fromVectorDoubleDoubleDoubleDouble_17() {
		assertSame(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.fromVectors(
				0., -1., 1., 0.));
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_18() {
		CoordinateSystem3D.fromVectors(0., -1., -1., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_19() {
		CoordinateSystem3D.fromVectors(0., 1., 1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_20() {
		CoordinateSystem3D.fromVectors(0., 1., 1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_21() {
		CoordinateSystem3D.fromVectors(0., 1., -1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_22() {
		CoordinateSystem3D.fromVectors(0., -1., 1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_23() {
		CoordinateSystem3D.fromVectors(0., 1., -1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_24() {
		CoordinateSystem3D.fromVectors(0., -1., 1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_25() {
		CoordinateSystem3D.fromVectors(0., -1., -1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_26() {
		CoordinateSystem3D.fromVectors(0., -1., -1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_27() {
		CoordinateSystem3D.fromVectors(1., 0., 0., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_28() {
		CoordinateSystem3D.fromVectors(-1., 0., 0., 0.);
	}

	@Test
	public void fromVectorDoubleDoubleDoubleDouble_29() {
		assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.fromVectors(
				1., 0., 0., 1.));
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_30() {
		CoordinateSystem3D.fromVectors(1., 0., 0., -1.);
	}

	@Test
	public void fromVectorDoubleDoubleDoubleDouble_31() {
		assertSame(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.fromVectors(
				-1., 0., 0., 1.));
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_32() {
		CoordinateSystem3D.fromVectors(-1., 0., 0., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_33() {
		CoordinateSystem3D.fromVectors(1., 0., 1., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_34() {
		CoordinateSystem3D.fromVectors(1., 0., -1., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_35() {
		CoordinateSystem3D.fromVectors(-1., 0., 1., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_36() {
		CoordinateSystem3D.fromVectors(-1., 0., -1., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_37() {
		CoordinateSystem3D.fromVectors(1., 0., 1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_38() {
		CoordinateSystem3D.fromVectors(1., 0., 1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_39() {
		CoordinateSystem3D.fromVectors(1., 0., -1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_40() {
		CoordinateSystem3D.fromVectors(-1., 0., 1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_41() {
		CoordinateSystem3D.fromVectors(1., 0., -1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_42() {
		CoordinateSystem3D.fromVectors(-1., 0., 1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_43() {
		CoordinateSystem3D.fromVectors(-1., 0., -1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_44() {
		CoordinateSystem3D.fromVectors(-1., 0., -1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_45() {
		CoordinateSystem3D.fromVectors(1., 1., 0., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_46() {
		CoordinateSystem3D.fromVectors(1., -1., 0., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_47() {
		CoordinateSystem3D.fromVectors(-1., 1., 0., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_48() {
		CoordinateSystem3D.fromVectors(-1., -1., 0., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_49() {
		CoordinateSystem3D.fromVectors(1., 1., 0., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_50() {
		CoordinateSystem3D.fromVectors(1., 1., 0., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_51() {
		CoordinateSystem3D.fromVectors(1., -1., 0., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_52() {
		CoordinateSystem3D.fromVectors(-1., 1., 0., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_53() {
		CoordinateSystem3D.fromVectors(1., -1., 0., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_54() {
		CoordinateSystem3D.fromVectors(-1., 1., 0., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_55() {
		CoordinateSystem3D.fromVectors(-1., -1., 0., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_56() {
		CoordinateSystem3D.fromVectors(-1., -1., 0., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_57() {
		CoordinateSystem3D.fromVectors(1., 1., 1., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_58() {
		CoordinateSystem3D.fromVectors(1., 1., -1., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_59() {
		CoordinateSystem3D.fromVectors(1., -1., 1., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_60() {
		CoordinateSystem3D.fromVectors(-1., 1., 1., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_61() {
		CoordinateSystem3D.fromVectors(1., -1., -1., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_62() {
		CoordinateSystem3D.fromVectors(-1., 1., -1., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_63() {
		CoordinateSystem3D.fromVectors(-1., -1., 1., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_64() {
		CoordinateSystem3D.fromVectors(-1., -1., -1., 0.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_65() {
		CoordinateSystem3D.fromVectors(1., 1., 1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_66() {
		CoordinateSystem3D.fromVectors(1., 1., 1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_67() {
		CoordinateSystem3D.fromVectors(1., 1., -1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_68() {
		CoordinateSystem3D.fromVectors(1., -1., 1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_69() {
		CoordinateSystem3D.fromVectors(-1., 1., 1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_70() {
		CoordinateSystem3D.fromVectors(1., 1., -1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_71() {
		CoordinateSystem3D.fromVectors(1., -1., 1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_72() {
		CoordinateSystem3D.fromVectors(-1., 1., 1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_73() {
		CoordinateSystem3D.fromVectors(1., -1., -1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_74() {
		CoordinateSystem3D.fromVectors(-1., 1., -1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_75() {
		CoordinateSystem3D.fromVectors(-1., -1., 1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_76() {
		CoordinateSystem3D.fromVectors(1., -1., -1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_77() {
		CoordinateSystem3D.fromVectors(-1., 1., -1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_78() {
		CoordinateSystem3D.fromVectors(-1., -1., 1., -1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_79() {
		CoordinateSystem3D.fromVectors(-1., -1., -1., 1.);
	}

	@Test(expected=CoordinateSystemNotFoundException.class)
	public void fromVectorsDoubleDoubleDoubleDouble_80() {
		CoordinateSystem3D.fromVectors(-1., -1., -1., -1.);
	}
	
	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_0() {
		CoordinateSystem3D.fromVectors(0, 0, 0, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_1() {
		CoordinateSystem3D.fromVectors(0, 0, 0, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_2() {
		CoordinateSystem3D.fromVectors(0, 0, 0, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_3() {
		CoordinateSystem3D.fromVectors(0, 0, 1, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_4() {
		CoordinateSystem3D.fromVectors(0, 0, -1, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_5() {
		CoordinateSystem3D.fromVectors(0, 0, 1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_6() {
		CoordinateSystem3D.fromVectors(0, 0, 1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_7() {
		CoordinateSystem3D.fromVectors(0, 0, -1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_8() {
		CoordinateSystem3D.fromVectors(0, 0, -1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_9() {
		CoordinateSystem3D.fromVectors(0, 1, 0, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_10() {
		CoordinateSystem3D.fromVectors(0, -1, 0, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_11() {
		CoordinateSystem3D.fromVectors(0, 1, 0, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_12() {
		CoordinateSystem3D.fromVectors(0, 1, 0, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_13() {
		CoordinateSystem3D.fromVectors(0, -1, 0, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_14() {
		CoordinateSystem3D.fromVectors(0, -1, 0, -1);
	}

	@Test
	public void fromVectorsIntIntIntInt_15() {
		assertSame(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.fromVectors(
				0, 1, 1, 0));
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_16() {
		CoordinateSystem3D.fromVectors(0, 1, -1, 0);
	}
	
	@Test
	public void fromVectorsIntIntIntInt_17() {
		assertSame(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.fromVectors(
				0, -1, 1, 0));
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_18() {
		CoordinateSystem3D.fromVectors(0, -1, -1, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_19() {
		CoordinateSystem3D.fromVectors(0, 1, 1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_20() {
		CoordinateSystem3D.fromVectors(0, 1, 1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_21() {
		CoordinateSystem3D.fromVectors(0, 1, -1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_22() {
		CoordinateSystem3D.fromVectors(0, -1, 1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_23() {
		CoordinateSystem3D.fromVectors(0, 1, -1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_24() {
		CoordinateSystem3D.fromVectors(0, -1, 1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_25() {
		CoordinateSystem3D.fromVectors(0, -1, -1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_26() {
		CoordinateSystem3D.fromVectors(0, -1, -1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_27() {
		CoordinateSystem3D.fromVectors(1, 0, 0, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_28() {
		CoordinateSystem3D.fromVectors(-1, 0, 0, 0);
	}

	@Test
	public void fromVectorsIntIntIntInt_29() {
		assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.fromVectors(
				1, 0, 0, 1));
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_30() {
		CoordinateSystem3D.fromVectors(1, 0, 0, -1);
	}

	@Test
	public void fromVectorsIntIntIntInt_31() {
		assertSame(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.fromVectors(
				-1, 0, 0, 1));
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_32() {
		CoordinateSystem3D.fromVectors(-1, 0, 0, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_33() {
		CoordinateSystem3D.fromVectors(1, 0, 1, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_34() {
		CoordinateSystem3D.fromVectors(1, 0, -1, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_35() {
		CoordinateSystem3D.fromVectors(-1, 0, 1, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_36() {
		CoordinateSystem3D.fromVectors(-1, 0, -1, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_37() {
		CoordinateSystem3D.fromVectors(1, 0, 1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_38() {
		CoordinateSystem3D.fromVectors(1, 0, 1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_39() {
		CoordinateSystem3D.fromVectors(1, 0, -1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_40() {
		CoordinateSystem3D.fromVectors(-1, 0, 1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_41() {
		CoordinateSystem3D.fromVectors(1, 0, -1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_42() {
		CoordinateSystem3D.fromVectors(-1, 0, 1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_43() {
		CoordinateSystem3D.fromVectors(-1, 0, -1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_44() {
		CoordinateSystem3D.fromVectors(-1, 0, -1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_45() {
		CoordinateSystem3D.fromVectors(1, 1, 0, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_46() {
		CoordinateSystem3D.fromVectors(1, -1, 0, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_47() {
		CoordinateSystem3D.fromVectors(-1, 1, 0, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_48() {
		CoordinateSystem3D.fromVectors(-1, -1, 0, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_49() {
		CoordinateSystem3D.fromVectors(1, 1, 0, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_50() {
		CoordinateSystem3D.fromVectors(1, 1, 0, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_51() {
		CoordinateSystem3D.fromVectors(1, -1, 0, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_52() {
		CoordinateSystem3D.fromVectors(-1, 1, 0, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_53() {
		CoordinateSystem3D.fromVectors(1, -1, 0, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_54() {
		CoordinateSystem3D.fromVectors(-1, 1, 0, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_55() {
		CoordinateSystem3D.fromVectors(-1, -1, 0, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_56() {
		CoordinateSystem3D.fromVectors(-1, -1, 0, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_57() {
		CoordinateSystem3D.fromVectors(1, 1, 1, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_58() {
		CoordinateSystem3D.fromVectors(1, 1, -1, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_59() {
		CoordinateSystem3D.fromVectors(1, -1, 1, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_60() {
		CoordinateSystem3D.fromVectors(-1, 1, 1, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_61() {
		CoordinateSystem3D.fromVectors(1, -1, -1, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_62() {
		CoordinateSystem3D.fromVectors(-1, 1, -1, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_63() {
		CoordinateSystem3D.fromVectors(-1, -1, 1, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_64() {
		CoordinateSystem3D.fromVectors(-1, -1, -1, 0);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_65() {
		CoordinateSystem3D.fromVectors(1, 1, 1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_66() {
		CoordinateSystem3D.fromVectors(1, 1, 1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_67() {
		CoordinateSystem3D.fromVectors(1, 1, -1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_68() {
		CoordinateSystem3D.fromVectors(1, -1, 1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_69() {
		CoordinateSystem3D.fromVectors(-1, 1, 1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_70() {
		CoordinateSystem3D.fromVectors(1, 1, -1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_71() {
		CoordinateSystem3D.fromVectors(1, -1, 1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_72() {
		CoordinateSystem3D.fromVectors(-1, 1, 1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_73() {
		CoordinateSystem3D.fromVectors(1, -1, -1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_74() {
		CoordinateSystem3D.fromVectors(-1, 1, -1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_75() {
		CoordinateSystem3D.fromVectors(-1, -1, 1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_76() {
		CoordinateSystem3D.fromVectors(1, -1, -1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_77() {
		CoordinateSystem3D.fromVectors(-1, 1, -1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_78() {
		CoordinateSystem3D.fromVectors(-1, -1, 1, -1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_79() {
		CoordinateSystem3D.fromVectors(-1, -1, -1, 1);
	}

	@Test(expected = CoordinateSystemNotFoundException.class)
	public void fromVectorsIntIntIntInt_80() {
		CoordinateSystem3D.fromVectors(-1, -1, -1, -1);
	}

	/**
	 */
	@Test
	public void toDefaultPoint3f() {
		Point3f pt, pt2, pt3;
		
		pt = randomPoint3f();
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toDefault(pt2);
		assertEpsilonEquals(pt, pt2);
		
		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), -pt.getY(), pt.getZ());
		CoordinateSystem3D.XYZ_LEFT_HAND.toDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), -pt.getZ(), pt.getY());
		CoordinateSystem3D.XZY_RIGHT_HAND.toDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		CoordinateSystem3D.XZY_LEFT_HAND.toDefault(pt2);
		assertEpsilonEquals(pt3, pt2);
	}

	/**
	 */
	@Test
	public void fromDefaultPoint3f() {
		Point3f pt, pt2, pt3;
		
		pt = randomPoint3f();
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt, pt2);
		
		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), -pt.getY(), pt.getZ());
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), pt.getZ(), -pt.getY());
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);
	}

	/**
	 */
	@Test
	public void toDefaultVector3f() {
		Vector3f vt, vt2, vt3;
		
		vt = randomVector3f();
		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toDefault(vt2);
		assertEpsilonEquals(vt, vt2);
		
		vt2 = new Vector3f(vt);
		vt3 = new Vector3f(vt.getX(), -vt.getY(), vt.getZ());
		CoordinateSystem3D.XYZ_LEFT_HAND.toDefault(vt2);
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		vt3 = new Vector3f(vt.getX(), -vt.getZ(), vt.getY());
		CoordinateSystem3D.XZY_RIGHT_HAND.toDefault(vt2);
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), vt.getY());
		CoordinateSystem3D.XZY_LEFT_HAND.toDefault(vt2);
		assertEpsilonEquals(vt3, vt2);
	}

	/**
	 */
	@Test
	public void fromDefaultVector3f() {
		Point3f pt, pt2, pt3;
		
		pt = randomPoint3f();
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt, pt2);
		
		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), -pt.getY(), pt.getZ());
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), pt.getZ(), -pt.getY());
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);
	}

	private void assertToDefaultQuaternion(CoordinateSystem3D system, Quaternion actual, double x, double y, double z, double a) {
		Quaternion expected = new Quaternion();
		expected.setAxisAngle(x, y, z, a);

		Quaternion defaultActual = new Quaternion(actual);
		system.toDefault(defaultActual);

		assertEpsilonEquals(expected, defaultActual);
	}

	/**
	 */
	@Test
	public void toDefaultQuaternion() {
		Quaternion expected;
		
		Quaternion q = newAxisAngle(1, 2, 3, 4);
		
		assertToDefaultQuaternion(CoordinateSystem3D.XYZ_RIGHT_HAND, q, 1, 2, 3, 4);
		assertToDefaultQuaternion(CoordinateSystem3D.XZY_RIGHT_HAND, q, 1, 3, 2, 4);
		assertToDefaultQuaternion(CoordinateSystem3D.XYZ_LEFT_HAND, q, 1, -2, 3, -4);
		assertToDefaultQuaternion(CoordinateSystem3D.XZY_LEFT_HAND, q, 1, 3, -2, -4);
	}

	private void matrixToDefault(CoordinateSystem3D system, Transform3D m) {
		Quaternion expectedRotation = new Quaternion();
		Vector3f expectedTranslation = new Vector3f();
		Quaternion rotation = new Quaternion();
		Vector3f translation = new Vector3f();
		
		expectedRotation = m.getRotation();
		expectedTranslation = m.getTranslation();
		system.toDefault(expectedRotation);
		system.toDefault(expectedTranslation);
		
		Transform3D mm = m.clone();
		system.toDefault(mm);
		rotation = mm.getRotation();
		translation = mm.getTranslation();
		
		assertEpsilonEquals(expectedRotation, rotation);
		assertEpsilonEquals(expectedTranslation, translation);
	}

	/**
	 */
	@Test
	public void toDefaultTransform3D() {
		Transform3D matrix = new Transform3D();
		Quaternion q = randomQuaternion();
		Vector3f t = randomVector3f();
		matrix.setRotation(q);
		matrix.setTranslation(t);
		
		matrixToDefault(CoordinateSystem3D.XYZ_RIGHT_HAND, matrix);
		matrixToDefault(CoordinateSystem3D.XZY_RIGHT_HAND, matrix);
		matrixToDefault(CoordinateSystem3D.XYZ_LEFT_HAND, matrix);
		matrixToDefault(CoordinateSystem3D.XZY_LEFT_HAND, matrix);
	}

	private void assertFromDefaultQuaternion(CoordinateSystem3D system, Quaternion actual, double x, double y, double z, double a) {
		Quaternion expected = new Quaternion();
		expected.setAxisAngle(x, y, z, a);

		Quaternion defaultActual = new Quaternion(actual);
		system.fromDefault(defaultActual);

		assertEpsilonEquals(expected, defaultActual);
	}

	/**
	 */
	@Test
	public void fromDefaultQuaternion() {
		Quaternion q;
		
		q = newAxisAngle(1, 2, 3, 4);
		
		assertFromDefaultQuaternion(CoordinateSystem3D.XYZ_RIGHT_HAND, q, 1, 2, 3, 4);
		assertFromDefaultQuaternion(CoordinateSystem3D.XYZ_RIGHT_HAND, q, 1, 3, 2, 4);
		assertFromDefaultQuaternion(CoordinateSystem3D.XYZ_LEFT_HAND, q, 1, -2, 3, -4);
		assertFromDefaultQuaternion(CoordinateSystem3D.XZY_LEFT_HAND, q, 1, 3, -2, -4);
	}

	private void matrixFromDefault(CoordinateSystem3D system, Transform3D m) {
		Quaternion expectedRotation = new Quaternion();
		Vector3f expectedTranslation = new Vector3f();
		Quaternion rotation = new Quaternion();
		Vector3f translation = new Vector3f();
		
		expectedRotation = m.getRotation();
		expectedTranslation = m.getTranslation();
		system.fromDefault(expectedRotation);
		system.fromDefault(expectedTranslation);
		
		Transform3D mm = m.clone();
		system.fromDefault(mm);
		rotation = mm.getRotation();
		translation = mm.getTranslation();
		
		assertEpsilonEquals(expectedRotation, rotation);
		assertEpsilonEquals(expectedTranslation, translation);
	}

	/**
	 */
	@Test
	public void fromDefaultTransform3D() {
		Transform3D matrix = new Transform3D();
		Quaternion q = randomQuaternion();
		Vector3f t = randomVector3f();
		matrix.setRotation(q);
		matrix.setTranslation(t);
		
		matrixFromDefault(CoordinateSystem3D.XYZ_RIGHT_HAND, matrix);
		matrixFromDefault(CoordinateSystem3D.XZY_RIGHT_HAND, matrix);
		matrixFromDefault(CoordinateSystem3D.XYZ_LEFT_HAND, matrix);
		matrixFromDefault(CoordinateSystem3D.XZY_LEFT_HAND, matrix);
	}

	/**
	 */
	@Test
	public void toSystemPoint3fCoordinateSystem3D() {
		Point3f pt, pt2, pt3;
		
		pt = randomPoint3f();
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), -pt.getY(), pt.getZ());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), -pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		//---
		
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), -pt.getY(), pt.getZ());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), -pt.getY());
		assertEpsilonEquals(pt3, pt2);

		//---
		
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), -pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getY(), -pt.getZ());
		assertEpsilonEquals(pt3, pt2);

		//---
		
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), -pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getY(), -pt.getZ());
		assertEpsilonEquals(pt3, pt2);
	}

	/**
	 */
	@Test
	public void toSystemVector3fCoordinateSystem3D() {
		Vector3f vt, vt2, vt3;
		
		vt = randomVector3f();
		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), -vt.getY(), vt.getZ());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), -vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		//---
		
		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), -vt.getY(), vt.getZ());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), -vt.getY());
		assertEpsilonEquals(vt3, vt2);

		//---
		
		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), -vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getY(), -vt.getZ());
		assertEpsilonEquals(vt3, vt2);

		//---
		
		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), -vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getY(), -vt.getZ());
		assertEpsilonEquals(vt3, vt2);
	}

	private void assertToSystemQuaternion(CoordinateSystem3D source, CoordinateSystem3D target, Quaternion actual,
			double x, double y, double z, double a) {
		Quaternion expected = new Quaternion();
		expected.setAxisAngle(x, y, z, a);

		Quaternion transActual = new Quaternion(actual);
		source.toSystem(transActual, target);

		assertEpsilonEquals(expected, transActual);
	}

	/**
	 */
	public void toSystemQuaternionCoordinateSystem3D() {
		Quaternion q = newAxisAngle(1, 2, 3, 4);
		
		assertToSystemQuaternion(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND, q,
				1, 2, 3, 4);
		assertToSystemQuaternion(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND, q,
				1, 3, 2, 4);
		assertToSystemQuaternion(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND, q,
				1, -2, 3, -4);
		assertToSystemQuaternion(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND, q,
				1, 3, -2, -4);

		assertToSystemQuaternion(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND, q, 1, 3, 2, 4);
		assertToSystemQuaternion(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND, q, 1, 2, 3, 4);
		assertToSystemQuaternion(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND, q, 1, 3, 2, -4);
		assertToSystemQuaternion(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND, q, 1, 2, 3, -4);

		assertToSystemQuaternion(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND, q, 1, -2, 3, -4);
		assertToSystemQuaternion(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND, q, 1, 3, -2, -4);
		assertToSystemQuaternion(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND, q, 1, 2, 3, 4);
		assertToSystemQuaternion(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XZY_LEFT_HAND, q, 1, 3, 2, 4);

		assertToSystemQuaternion(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND, q, 1, 3, 2, -4);
		assertToSystemQuaternion(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND, q, 1, 2, 3, -4);
		assertToSystemQuaternion(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND, q, 1, 3, 2, 4);
		assertToSystemQuaternion(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XZY_LEFT_HAND, q, 1, 2, 3, 4);
	}
	
	/**
	 */
	@Test
	public void heightTuple3f() {
		Point3f tuple = randomPoint3f();
		
		assertEpsilonEquals(tuple.getZ(),CoordinateSystem3D.XYZ_LEFT_HAND.height(tuple));
		assertEpsilonEquals(tuple.getZ(),CoordinateSystem3D.XYZ_RIGHT_HAND.height(tuple));
		assertEpsilonEquals(tuple.getY(),CoordinateSystem3D.XZY_LEFT_HAND.height(tuple));
		assertEpsilonEquals(tuple.getY(),CoordinateSystem3D.XZY_RIGHT_HAND.height(tuple));
	}

	/**
	 */
	@Test
	public void setSideTuple3fFloat() {
		Point3f o = randomPoint3f();
		double side = o.getX()+o.getY()+o.getZ();
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.setSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(side, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.setSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(side, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(side, tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(side, tuple.getZ());
	}
	
	/**
	 */
	@Test
	public void setViewTuple3fFloat() {
		Point3f o = randomPoint3f();
		double view = o.getX()+o.getY()+o.getZ();
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.setView(tuple, view);
		assertEpsilonEquals(view, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.setView(tuple, view);
		assertEpsilonEquals(view, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setView(tuple, view);
		assertEpsilonEquals(view, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setView(tuple, view);
		assertEpsilonEquals(view, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
	}

	/**
	 */
	@Test
	public void addSideTuple3fFloat() {
		Point3f o = randomPoint3f();
		double side = o.getX()+o.getY()+o.getZ();
		double expectedY = o.getY() + side; 
		double expectedZ = o.getZ() + side; 
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.addSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(expectedY, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.addSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(expectedY, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(expectedZ, tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(expectedZ, tuple.getZ());
	}

	/**
	 */
	@Test
	public void addViewTuple3fFloat() {
		Point3f o = randomPoint3f();
		double view = o.getX()+o.getY()+o.getZ();
		double expected = o.getX() + view; 
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.addView(tuple, view);
		assertEpsilonEquals(expected, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.addView(tuple, view);
		assertEpsilonEquals(expected, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addView(tuple, view);
		assertEpsilonEquals(expected, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addView(tuple, view);
		assertEpsilonEquals(expected, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
	}

	/**
	 */
	@Test
	public void setHeightTuple3fFloat() {
		Point3f o = randomPoint3f();
		double height = o.getX()+o.getY()+o.getZ();
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.setHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(height, tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.setHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(height, tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(height, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(height, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
	}

	/**
	 */
	@Test
	public void addHeightTuple3fFloat() {
		Point3f o = randomPoint3f();
		double height = o.getX()+o.getY()+o.getZ();
		double expectedY = o.getY() + height; 
		double expectedZ = o.getZ() + height; 
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.addHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(expectedZ, tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.addHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(expectedZ, tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(expectedY, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(expectedY, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
	}

	/**
	 */
	@Test
	public void getHeightCoordinateIndex() {
		assertEquals(2, CoordinateSystem3D.XYZ_LEFT_HAND.getHeightCoordinateIndex());
		assertEquals(2, CoordinateSystem3D.XYZ_RIGHT_HAND.getHeightCoordinateIndex());
		assertEquals(1, CoordinateSystem3D.XZY_LEFT_HAND.getHeightCoordinateIndex());
		assertEquals(1, CoordinateSystem3D.XZY_RIGHT_HAND.getHeightCoordinateIndex());
	}
	
	/**
	 */
	@Test
	public void getSideCoordinateIndex() {
		assertEquals(1, CoordinateSystem3D.XYZ_LEFT_HAND.getSideCoordinateIndex());
		assertEquals(1, CoordinateSystem3D.XYZ_RIGHT_HAND.getSideCoordinateIndex());
		assertEquals(2, CoordinateSystem3D.XZY_LEFT_HAND.getSideCoordinateIndex());
		assertEquals(2, CoordinateSystem3D.XZY_RIGHT_HAND.getSideCoordinateIndex());
	}

	/**
	 */
	@Test
	public void getViewCoordinateIndex() {
		assertEquals(0, CoordinateSystem3D.XYZ_LEFT_HAND.getViewCoordinateIndex());
		assertEquals(0, CoordinateSystem3D.XYZ_RIGHT_HAND.getViewCoordinateIndex());
		assertEquals(0, CoordinateSystem3D.XZY_LEFT_HAND.getViewCoordinateIndex());
		assertEquals(0, CoordinateSystem3D.XZY_RIGHT_HAND.getViewCoordinateIndex());
	}

	/**
	 */
	@Test
	public void heightFloatFloatFloat() {
		Point3f tuple = randomPoint3f();
		
		assertEpsilonEquals(tuple.getZ(),CoordinateSystem3D.XYZ_LEFT_HAND.height(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEpsilonEquals(tuple.getZ(),CoordinateSystem3D.XYZ_RIGHT_HAND.height(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEpsilonEquals(tuple.getY(),CoordinateSystem3D.XZY_LEFT_HAND.height(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEpsilonEquals(tuple.getY(),CoordinateSystem3D.XZY_RIGHT_HAND.height(tuple.getX(),tuple.getY(),tuple.getZ()));
	}

	/**
	 */
	@Test
	public void viewFloatFloatFloat() {
		Point3f tuple = randomPoint3f();
		
		assertEpsilonEquals(tuple.getX(),CoordinateSystem3D.XYZ_LEFT_HAND.view(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEpsilonEquals(tuple.getX(),CoordinateSystem3D.XYZ_RIGHT_HAND.view(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEpsilonEquals(tuple.getX(),CoordinateSystem3D.XZY_LEFT_HAND.view(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEpsilonEquals(tuple.getX(),CoordinateSystem3D.XZY_RIGHT_HAND.view(tuple.getX(),tuple.getY(),tuple.getZ()));
	}

	/**
	 */
	@Test
	public void sideFloatFloatFloat() {
		Point3f tuple = randomPoint3f();
		
		assertEpsilonEquals(tuple.getY(),CoordinateSystem3D.XYZ_LEFT_HAND.side(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEpsilonEquals(tuple.getY(),CoordinateSystem3D.XYZ_RIGHT_HAND.side(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEpsilonEquals(tuple.getZ(),CoordinateSystem3D.XZY_LEFT_HAND.side(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEpsilonEquals(tuple.getZ(),CoordinateSystem3D.XZY_RIGHT_HAND.side(tuple.getX(),tuple.getY(),tuple.getZ()));
	}

	/**
	 */
	@Test
	public void toCoordinateSystem2DVoid() {
		assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D());
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D());
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D());
		assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D());
	}

	/**
	 */
	@Test
	public void toCoordinateSystem2DPoint2f() {
		Point3f pt;
		Point2fx pt2;
		
		pt = randomPoint3f();

		pt2 = new Point2fx(pt.getX(),pt.getY());
		assertEpsilonEquals(pt2, CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(pt));

		pt2 = new Point2fx(pt.getX(),pt.getY());
		assertEpsilonEquals(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(pt));

		pt2 = new Point2fx(pt.getX(),pt.getZ());
		assertEpsilonEquals(pt2, CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(pt));

		pt2 = new Point2fx(pt.getX(),pt.getZ());
		assertEpsilonEquals(pt2, CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(pt));
	}

	/**
	 */
	@Test
	public void toCoordinateSystem2DVector3f() {
		Vector3f vt;
		Vector2fx vt2;
		
		vt = randomVector3f();

		vt2 = new Vector2fx(vt.getX(),vt.getY());
		assertEpsilonEquals(vt2, CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(vt));

		vt2 = new Vector2fx(vt.getX(),vt.getY());
		assertEpsilonEquals(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(vt));

		vt2 = new Vector2fx(vt.getX(),vt.getZ());
		assertEpsilonEquals(vt2, CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(vt));

		vt2 = new Vector2fx(vt.getX(),vt.getZ());
		assertEpsilonEquals(vt2, CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(vt));
	}

	private double clamp(double angle) {
		double a = angle;
		while (a<=-Math.PI) a += MathConstants.TWO_PI;
		while (a>Math.PI) a -= MathConstants.TWO_PI;
		return a;
	}

	/**
	 */
	@Test
	public void toCoordinateSystem2DQuaternion() {
		Quaternion quat;
		double a;
		
		for(a=-Math.PI; a<=Math.PI; a+=Math.PI/500.) {
			quat = newAxisAngle(1,0,0,a);
			assertEpsilonEquals(0, clamp(CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals(0, clamp(CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals(0, clamp(CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals(0, clamp(CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(quat)));
		}

		quat = newAxisAngle(0,1,0,-Math.PI);
		assertEpsilonEquals(Math.PI, clamp(CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(quat)));
		assertEpsilonEquals(Math.PI, clamp(CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(quat)));
		assertEpsilonEquals(Math.PI, clamp(CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(quat)));
		assertEpsilonEquals(Math.PI, clamp(CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(quat)));

		for(a=-Math.PI+Math.PI/500.; a<=Math.PI; a+=Math.PI/500.) {
			quat = newAxisAngle(0,1,0,a);
			assertEpsilonEquals(a, clamp(CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals((a>=-Math.PI/2.)&&(a<=Math.PI/2.) ? 0 : Math.PI, clamp(CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals((a>=-Math.PI/2.)&&(a<=Math.PI/2.) ? 0 : Math.PI, clamp(CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals(a, clamp(CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(quat)));
		}

		quat = newAxisAngle(0,0,1,-Math.PI);
		assertEpsilonEquals(Math.PI, clamp(CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(quat)));
		assertEpsilonEquals(Math.PI, clamp(CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(quat)));
		assertEpsilonEquals(Math.PI, clamp(CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(quat)));
		assertEpsilonEquals(Math.PI, clamp(CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(quat)));

		for(a=-Math.PI+Math.PI/500.; a<=Math.PI; a+=Math.PI/500.) {
			quat = newAxisAngle(0,0,1,a);
			assertEpsilonEquals((a>=-Math.PI/2.)&&(a<=Math.PI/2.) ? 0 : Math.PI, clamp(CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals(a, clamp(CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals(a, clamp(CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals((a>=-Math.PI/2.)&&(a<=Math.PI/2.) ? 0 : Math.PI, clamp(CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(quat)));
		}
	}

	/**
	 */
	@Test
	public void fromCoordinateSystem2DPoint2f() {
		Point3f pt3;
		Point2fx pt2;
		
		pt2 = randomPoint2f();

		pt3 = new Point3f(pt2.getX(),pt2.getY(),0);
		assertEpsilonEquals(pt3, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(pt2));

		pt3 = new Point3f(pt2.getX(),pt2.getY(),0);
		assertEpsilonEquals(pt3, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(pt2));

		pt3 = new Point3f(pt2.getX(),0,pt2.getY());
		assertEpsilonEquals(pt3, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(pt2));

		pt3 = new Point3f(pt2.getX(),0,pt2.getY());
		assertEpsilonEquals(pt3, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(pt2));
	}

	/**
	 */
	@Test
	public void fromCoordinateSystem2DVector2fFloat() {
		Vector3f vt3;
		Vector2fx vt2;
		double tc;
		
		tc = this.random.nextDouble();
		vt2 = randomVector2f();

		vt3 = new Vector3f(vt2.getX(),vt2.getY(),tc);
		assertEpsilonEquals(vt3, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(vt2, tc));

		vt3 = new Vector3f(vt2.getX(),vt2.getY(),tc);
		assertEpsilonEquals(vt3, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(vt2, tc));

		vt3 = new Vector3f(vt2.getX(),tc,vt2.getY());
		assertEpsilonEquals(vt3, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(vt2, tc));

		vt3 = new Vector3f(vt2.getX(),tc,vt2.getY());
		assertEpsilonEquals(vt3, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(vt2, tc));
	}

	/**
	 */
	@Test
	public void fromCoordinateSystem2DVector2f() {
		Vector3f vt3;
		Vector2fx vt2;

		vt2 = randomVector2f();

		vt3 = new Vector3f(vt2.getX(),vt2.getY(),0);
		assertEpsilonEquals(vt3, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(vt2));

		vt3 = new Vector3f(vt2.getX(),vt2.getY(),0);
		assertEpsilonEquals(vt3, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(vt2));

		vt3 = new Vector3f(vt2.getX(),0,vt2.getY());
		assertEpsilonEquals(vt3, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(vt2));

		vt3 = new Vector3f(vt2.getX(),0,vt2.getY());
		assertEpsilonEquals(vt3, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(vt2));
	}

	/**
	 */
	@Test
	public void pointRotation_Quaternion_AroundZ() {
		// test if points are correctly rotated when the rotation is
		// expressed with an axis-angle in the different coordinate systems
		// See the description of CoordinateSystem3D for theoritical details.
		Point3f pOrig, pExpected;
		Quaternion axisangle;
		Quaternion q1, q2;

		pOrig = new Point3f(1,0,0);

		// Rotation around vectical vector about PI/2 radian
		axisangle = newAxisAngle(0,0,1,MathConstants.DEMI_PI);
		q1 = new Quaternion();
		q1.set(axisangle);
		
		q2 = new Quaternion(q1);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));

		q2 = new Quaternion(q1);
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,0,-1);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));

		q2 = new Quaternion(q1);
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,-1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));

		q2 = new Quaternion(q1);
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,0,1);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));

		// Rotation around vectical vector about -PI/2 radian
		axisangle = newAxisAngle(0,0,1,-MathConstants.DEMI_PI);
		q1.set(axisangle);

		q2 = new Quaternion(q1);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,-1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));

		q2 = new Quaternion(q1);
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,0,1);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));

		q2 = new Quaternion(q1);
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));

		q2 = new Quaternion(q1);
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,0,-1);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));
	}

	/**
	 */
	@Test
	public void getBackVectorVector3f() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getBackVector(v);
		assertEpsilonEquals(new Vector3f(-1,0,0), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getBackVector(v);
		assertEpsilonEquals(new Vector3f(-1,0,0), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getBackVector(v);
		assertEpsilonEquals(new Vector3f(-1,0,0), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getBackVector(v);
		assertEpsilonEquals(new Vector3f(-1,0,0), v);
	}
	/**
	 */
	@Test
	public void getDownVectorVector3f() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getDownVector(v);
		assertEpsilonEquals(new Vector3f(0,0,-1), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getDownVector(v);
		assertEpsilonEquals(new Vector3f(0,0,-1), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getDownVector(v);
		assertEpsilonEquals(new Vector3f(0,-1,0), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getDownVector(v);
		assertEpsilonEquals(new Vector3f(0,-1,0), v);
	}

	/**
	 */
	@Test
	public void getLeftVectorVector3f() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getLeftVector(v);
		assertEpsilonEquals(new Vector3f(0,-1,0), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getLeftVector(v);
		assertEpsilonEquals(new Vector3f(0,1,0), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getLeftVector(v);
		assertEpsilonEquals(new Vector3f(0,0,1), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getLeftVector(v);
		assertEpsilonEquals(new Vector3f(0,0,-1), v);
	}

	/**
	 */
	@Test
	public void getRightVectorVector3f() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getRightVector(v);
		assertEpsilonEquals(new Vector3f(0,1,0), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getRightVector(v);
		assertEpsilonEquals(new Vector3f(0,-1,0), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getRightVector(v);
		assertEpsilonEquals(new Vector3f(0,0,-1), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getRightVector(v);
		assertEpsilonEquals(new Vector3f(0,0,1), v);
	}

	/**
	 */
	@Test
	public void getUpVectorVector3f() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getUpVector(v);
		assertEpsilonEquals(new Vector3f(0,0,1), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getUpVector(v);
		assertEpsilonEquals(new Vector3f(0,0,1), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getUpVector(v);
		assertEpsilonEquals(new Vector3f(0,1,0), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getUpVector(v);
		assertEpsilonEquals(new Vector3f(0,1,0), v);
	}

	/**
	 */
	@Test
	public void getViewVectorVector3f() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getViewVector(v);
		assertEpsilonEquals(new Vector3f(1,0,0), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getViewVector(v);
		assertEpsilonEquals(new Vector3f(1,0,0), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getViewVector(v);
		assertEpsilonEquals(new Vector3f(1,0,0), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getViewVector(v);
		assertEpsilonEquals(new Vector3f(1,0,0), v);
	}

}
