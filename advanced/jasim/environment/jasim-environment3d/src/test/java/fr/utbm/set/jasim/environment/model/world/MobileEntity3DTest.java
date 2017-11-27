/**
 * 
 */
package fr.utbm.set.jasim.environment.model.world;

import java.util.concurrent.TimeUnit;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.OrientedBoundingBox;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.jasim.environment.model.place.Place;
import fr.utbm.set.jasim.environment.semantics.ObjectType;
import fr.utbm.set.jasim.environment.semantics.PedestrianType;
import fr.utbm.set.jasim.environment.time.AbstractClock;
import fr.utbm.set.math.MathConstants;
import fr.utbm.set.math.MeasureUnitUtil;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MobileEntity3DTest extends AbstractTestCase {

	private AlignedBoundingBox box;
	private OrientedBoundingBox box2;
	private Mesh3D mesh;
	private MobileEntity3D<AlignedBoundingBox> tested;
	private MobileEntity3D<OrientedBoundingBox> tested2;
	private Place<?,?,MobileEntity3D<AlignedBoundingBox>> place;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.mesh = new Mesh3D(true, randomPoints3D());
		this.place = new PlaceStub(new ClockStub(1., .5));
		createAlignedBox();
	}

	private void createAlignedBox() {
		this.box = new AlignedBoundingBox();
		this.box.combine(this.mesh.getPoints());
		this.tested = new MobileEntity3D<AlignedBoundingBox>(
				this.box, ObjectType.OBJECTTYPE_SINGLETON, true, this.mesh);
		this.tested.addSemantic(PedestrianType.PEDESTRIANTYPE_SINGLETON);
		this.tested.setPlace(this.place);
	}
	
	private void createOrientedBox() {
		this.box2 = new OrientedBoundingBox();
		this.box2.combine(this.mesh.getPoints());
		this.tested2 = new MobileEntity3D<OrientedBoundingBox>(
				this.box2, ObjectType.OBJECTTYPE_SINGLETON, true, this.mesh.clone());
		this.tested2.addSemantic(PedestrianType.PEDESTRIANTYPE_SINGLETON);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.box = null;
		this.mesh = null;
		this.place = null;
		this.tested = null;
		this.box2 = null;
		this.tested2 = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#getMesh()}.
	 */
	public void testGetMesh() {
		assertSame(this.mesh, this.tested.getMesh());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setTransform(fr.utbm.set.geom.transform.Transform3D)}.
	 */
	public void testSetTransform_Aligned_TranslationOnly_WithoutPivot() {
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomPoint3D());
		this.tested.setTransform(tr1);
		assertEpsilonEquals(tr1.getTranslation(), this.tested.getPosition3D());
		assertEpsilonEquals(tr1.getQuaternion(), this.tested.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(randomPoint3D());
		this.tested.setTransform(tr2);
		assertEpsilonEquals(tr2.getTranslation(), this.tested.getPosition3D());
		assertEpsilonEquals(tr2.getQuaternion(), this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setTransform(fr.utbm.set.geom.transform.Transform3D)}.
	 */
	public void testSetTransform_Oriented_TranslationOnly_WithoutPivot() {
		createOrientedBox();
		
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomPoint3D());
		this.tested2.setTransform(tr1);
		assertEpsilonEquals(tr1.getTranslation(), this.tested2.getPosition3D());
		assertEpsilonEquals(tr1.getQuaternion(), this.tested2.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(randomPoint3D());
		this.tested2.setTransform(tr2);
		assertEpsilonEquals(tr2.getTranslation(), this.tested2.getPosition3D());
		assertEpsilonEquals(tr2.getQuaternion(), this.tested2.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setTransform(fr.utbm.set.geom.transform.Transform3D)}.
	 */
	public void testSetTransform_Aligned_TranslationOnly_WithPivot() {
		this.tested.setPivot(10.,-10,10);
		
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomPoint3D());
		
		this.tested.setTransform(tr1);
		
		assertEpsilonEquals(tr1.getTranslation(), this.tested.getPosition3D());
		assertEpsilonEquals(tr1.getQuaternion(), this.tested.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(randomPoint3D());
		this.tested.setTransform(tr2);
		assertEpsilonEquals(tr2.getTranslation(), this.tested.getPosition3D());
		assertEpsilonEquals(tr2.getQuaternion(), this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setTransform(fr.utbm.set.geom.transform.Transform3D)}.
	 */
	public void testSetTransform_Oriented_TranslationOnly_WithPivot() {
		createOrientedBox();
		
		this.tested2.setPivot(10.,-10,10);
		
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomPoint3D());
		
		this.tested2.setTransform(tr1);
		
		assertEpsilonEquals(tr1.getTranslation(), this.tested2.getPosition3D());
		assertEpsilonEquals(tr1.getQuaternion(), this.tested2.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(randomPoint3D());
		this.tested2.setTransform(tr2);
		assertEpsilonEquals(tr2.getTranslation(), this.tested2.getPosition3D());
		assertEpsilonEquals(tr2.getQuaternion(), this.tested2.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setTransform(fr.utbm.set.geom.transform.Transform3D)}.
	 */
	public void testSetTransform_Aligned_RotationOnly_WithoutPivot() {
		Transform3D tr1 = new Transform3D();
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		Point3d pos = new Point3d(); // position is expected to be (0,0,0) because of the values of the transformation matrix.
		
		this.tested.setTransform(tr1);
		
		assertEpsilonEquals(pos, this.tested.getPosition3D());
		assertEpsilonEquals(tr1.getQuaternion(), this.tested.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));

		this.tested.setTransform(tr2);
		
		assertEpsilonEquals(pos, this.tested.getPosition3D());
		assertEpsilonEquals(tr2.getQuaternion(), this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setTransform(fr.utbm.set.geom.transform.Transform3D)}.
	 */
	public void testSetTransform_Oriented_RotationOnly_WithoutPivot() {
		createOrientedBox();
		
		Transform3D tr1 = new Transform3D();
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		Point3d pos = new Point3d(); // position is expected to be (0,0,0) because of the values of the transformation matrix.
		
		this.tested2.setTransform(tr1);
		
		assertEpsilonEquals(pos, this.tested2.getPosition3D());
		assertEpsilonEquals(tr1.getQuaternion(), this.tested2.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));

		this.tested2.setTransform(tr2);
		
		assertEpsilonEquals(pos, this.tested2.getPosition3D());
		assertEpsilonEquals(tr2.getQuaternion(), this.tested2.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setTransform(fr.utbm.set.geom.transform.Transform3D)}.
	 */
	public void testSetTransform_Aligned_RotationOnly_WithPivot() {
		this.tested.setPivot(10., -10., 10.);
		
		Transform3D tr1 = new Transform3D();
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		Point3d pos = new Point3d(); // position is expected to be (0,0,0) because of the values of the transformation matrix.
		
		this.tested.setTransform(tr1);
		
		assertEpsilonEquals(pos, this.tested.getPosition3D());
		assertEpsilonEquals(tr1.getQuaternion(), this.tested.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));

		this.tested.setTransform(tr2);
		
		assertEpsilonEquals(pos, this.tested.getPosition3D());
		assertEpsilonEquals(tr2.getQuaternion(), this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setTransform(fr.utbm.set.geom.transform.Transform3D)}.
	 */
	public void testSetTransform_Oriented_RotationOnly_WithPivot() {
		createOrientedBox();
		
		this.tested2.setPivot(10.,-10.,-10.);
		
		Transform3D tr1 = new Transform3D();
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		Point3d pos = new Point3d(); // position is expected to be (0,0,0) because of the values of the transformation matrix.
		
		this.tested2.setTransform(tr1);
		
		assertEpsilonEquals(pos, this.tested2.getPosition3D());
		assertEpsilonEquals(tr1.getQuaternion(), this.tested2.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));

		this.tested2.setTransform(tr2);
		
		assertEpsilonEquals(pos, this.tested2.getPosition3D());
		assertEpsilonEquals(tr2.getQuaternion(), this.tested2.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setTransform(fr.utbm.set.geom.transform.Transform3D)}.
	 */
	public void testSetTransform_Aligned_TranslationAndRotation_WithoutPivot() {
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomPoint3D());
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		Point3d pos = new Point3d(tr1.getTranslation());
		
		this.tested.setTransform(tr1);
		
		assertEpsilonEquals(pos, this.tested.getPosition3D());
		assertEpsilonEquals(tr1.getQuaternion(), this.tested.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(randomPoint3D());
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));

		pos = new Point3d(tr2.getTranslation());

		this.tested.setTransform(tr2);
		
		assertEpsilonEquals(pos, this.tested.getPosition3D());
		assertEpsilonEquals(tr2.getQuaternion(), this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setTransform(fr.utbm.set.geom.transform.Transform3D)}.
	 */
	public void testSetTransform_Oriented_TranslationAndRotation_WithoutPivot() {
		createOrientedBox();
		
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomPoint3D());
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		Point3d pos = new Point3d(tr1.getTranslation());
		
		this.tested2.setTransform(tr1);
		
		assertEpsilonEquals(pos, this.tested2.getPosition3D());
		assertEpsilonEquals(tr1.getQuaternion(), this.tested2.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(randomPoint3D());
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));

		pos = new Point3d(tr2.getTranslation());

		this.tested2.setTransform(tr2);
		
		assertEpsilonEquals(pos, this.tested2.getPosition3D());
		assertEpsilonEquals(tr2.getQuaternion(), this.tested2.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setTransform(fr.utbm.set.geom.transform.Transform3D)}.
	 */
	public void testSetTransform_Aligned_TranslationAndRotation_WithPivot() {
		this.tested.setPivot(10., -10., -10.);
		
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomPoint3D());
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		Point3d pos = new Point3d(tr1.getTranslation());
		
		this.tested.setTransform(tr1);
		
		assertEpsilonEquals(pos, this.tested.getPosition3D());
		assertEpsilonEquals(tr1.getQuaternion(), this.tested.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(randomPoint3D());
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));

		pos = new Point3d(tr2.getTranslation());

		this.tested.setTransform(tr2);
		
		assertEpsilonEquals(pos, this.tested.getPosition3D());
		assertEpsilonEquals(tr2.getQuaternion(), this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setTransform(fr.utbm.set.geom.transform.Transform3D)}.
	 */
	public void testSetTransform_Oriented_TranslationAndRotation_WithPivot() {
		createOrientedBox();
		
		this.tested2.setPivot(10., -10., -10.);

		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomPoint3D());
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		Point3d pos = new Point3d(tr1.getTranslation());
		
		this.tested2.setTransform(tr1);
		
		assertEpsilonEquals(pos, this.tested2.getPosition3D());
		assertEpsilonEquals(tr1.getQuaternion(), this.tested2.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(randomPoint3D());
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));

		pos = new Point3d(tr2.getTranslation());

		this.tested2.setTransform(tr2);
		
		assertEpsilonEquals(pos, this.tested2.getPosition3D());
		assertEpsilonEquals(tr2.getQuaternion(), this.tested2.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#transform(Transform3D)}.
	 */
	public void testTransform_Aligned_TranslationOnly_WithoutPivot() {
		Point3d expectedPosition;
		Quat4d expectedOrientation;
		
		expectedOrientation = new Quat4d(this.tested.getQuaternion());
		
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomPoint3D());
		
		expectedPosition = new Point3d(this.tested.getPosition3D());
		expectedPosition.add(tr1.getTranslation());
		
		this.tested.transform(tr1);
		
		assertEpsilonEquals(expectedPosition, this.tested.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(new Point3d(100., -99., 98.));
		
		expectedPosition = new Point3d(this.tested.getPosition3D());
		expectedPosition.add(tr2.getTranslation());
		
		this.tested.transform(tr2);
		
		assertEpsilonEquals(expectedPosition, this.tested.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#transform(Transform3D)}.
	 */
	public void testTransform_Oriented_TranslationOnly_WithoutPivot() {
		createOrientedBox();
		
		Point3d expectedPosition;
		Quat4d expectedOrientation;
		
		expectedOrientation = new Quat4d(this.tested2.getQuaternion());
		
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomPoint3D());
		
		expectedPosition = new Point3d(this.tested2.getPosition3D());
		expectedPosition.add(tr1.getTranslation());
		
		this.tested2.transform(tr1);
		
		assertEpsilonEquals(expectedPosition, this.tested2.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested2.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(new Point3d(100., -99., 98.));
		
		expectedPosition = new Point3d(this.tested2.getPosition3D());
		expectedPosition.add(tr2.getTranslation());
		
		this.tested2.transform(tr2);
		
		assertEpsilonEquals(expectedPosition, this.tested2.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested2.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#transform(Transform3D)}.
	 */
	public void testTransform_Aligned_TranslationOnly_WithPivot() {
		Point3d expectedPosition;
		Quat4d expectedOrientation;
		
		this.tested.setPivot(randomPoint3D());
		
		expectedOrientation = new Quat4d(this.tested.getQuaternion());
		
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomPoint3D());
		
		expectedPosition = new Point3d(this.tested.getPosition3D());
		expectedPosition.add(tr1.getTranslation());
		
		this.tested.transform(tr1);
		
		assertEpsilonEquals(expectedPosition, this.tested.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(new Point3d(100., -99., 98.));
		
		expectedPosition = new Point3d(this.tested.getPosition3D());
		expectedPosition.add(tr2.getTranslation());
		
		this.tested.transform(tr2);
		
		assertEpsilonEquals(expectedPosition, this.tested.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#transform(Transform3D)}.
	 */
	public void testTransform_Oriented_TranslationOnly_WithPivot() {
		createOrientedBox();
		
		this.tested2.setPivot(randomPoint3D());
		
		Point3d expectedPosition;
		Quat4d expectedOrientation;
		
		expectedOrientation = new Quat4d(this.tested2.getQuaternion());
		
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomPoint3D());
		
		expectedPosition = new Point3d(this.tested2.getPosition3D());
		expectedPosition.add(tr1.getTranslation());
		
		this.tested2.transform(tr1);
		
		assertEpsilonEquals(expectedPosition, this.tested2.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested2.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(new Point3d(100., -99., 98.));
		
		expectedPosition = new Point3d(this.tested2.getPosition3D());
		expectedPosition.add(tr2.getTranslation());
		
		this.tested2.transform(tr2);
		
		assertEpsilonEquals(expectedPosition, this.tested2.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested2.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#transform(Transform3D)}.
	 */
	public void testTransform_Aligned_RotationOnly_WithoutPivot() {
		Point3d expectedPosition;
		Quat4d expectedOrientation;
		
		expectedPosition = new Point3d(this.tested.getPosition3D());
		
		Transform3D tr1 = new Transform3D();
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested.getQuaternion());
		expectedOrientation.mul(tr1.getQuaternion(), expectedOrientation);
		
		this.tested.transform(tr1);
		
		assertEpsilonEquals(expectedPosition, this.tested.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested.getQuaternion());
		expectedOrientation.mul(tr2.getQuaternion(), expectedOrientation);
		
		this.tested.transform(tr2);
		
		assertEpsilonEquals(expectedPosition, this.tested.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#transform(Transform3D)}.
	 */
	public void testTransform_Oriented_RotationOnly_WithoutPivot() {
		createOrientedBox();
		
		Point3d expectedPosition;
		Quat4d expectedOrientation;
		
		expectedPosition = new Point3d(this.tested2.getPosition3D());
		
		Transform3D tr1 = new Transform3D();
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested2.getQuaternion());
		expectedOrientation.mul(tr1.getQuaternion(), expectedOrientation);
		
		this.tested2.transform(tr1);
		
		assertEpsilonEquals(expectedPosition, this.tested2.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested2.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested2.getQuaternion());
		expectedOrientation.mul(tr2.getQuaternion(), expectedOrientation);
		
		this.tested2.transform(tr2);
		
		assertEpsilonEquals(expectedPosition, this.tested2.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested2.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#transform(Transform3D)}.
	 */
	public void testTransform_Aligned_RotationOnly_WithPivot() {
		Point3d expectedPosition, pivot;
		Quat4d expectedOrientation;
		
		this.tested.setPivot(10., -10., 10.);
		
		Transform3D tr1 = new Transform3D();
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested.getQuaternion());
		expectedOrientation.mul(tr1.getQuaternion(), expectedOrientation);

		expectedPosition = new Point3d(this.tested.getPosition3D());
		pivot = new Point3d(expectedPosition);
		pivot.add(this.tested.getPivot());
		Matrix4d m = GeometryUtil.rotateAround(tr1.getQuaternion(), pivot);
		m.transform(expectedPosition);
		
		this.tested.transform(tr1);
		
		assertEpsilonEquals(expectedPosition, this.tested.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested.getQuaternion());
		expectedOrientation.mul(tr2.getQuaternion(), expectedOrientation);
		
		expectedPosition = new Point3d(this.tested.getPosition3D());
		pivot = new Point3d(expectedPosition);
		pivot.add(this.tested.getPivot());
		m = GeometryUtil.rotateAround(tr2.getQuaternion(), pivot);
		m.transform(expectedPosition);

		this.tested.transform(tr2);
		
		assertEpsilonEquals(expectedPosition, this.tested.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#transform(Transform3D)}.
	 */
	public void testTransform_Oriented_RotationOnly_WithPivot() {
		createOrientedBox();
		
		Point3d expectedPosition, pivot;
		Quat4d expectedOrientation;
		
		this.tested2.setPivot(10., -10., 10.);
		
		Transform3D tr1 = new Transform3D();
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested2.getQuaternion());
		expectedOrientation.mul(tr1.getQuaternion(), expectedOrientation);

		expectedPosition = new Point3d(this.tested2.getPosition3D());
		pivot = new Point3d(expectedPosition);
		pivot.add(this.tested2.getPivot());
		Matrix4d m = GeometryUtil.rotateAround(tr1.getQuaternion(), pivot);
		m.transform(expectedPosition);
		
		this.tested2.transform(tr1);
		
		assertEpsilonEquals(expectedPosition, this.tested2.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested2.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested2.getQuaternion());
		expectedOrientation.mul(tr2.getQuaternion(), expectedOrientation);
		
		expectedPosition = new Point3d(this.tested2.getPosition3D());
		pivot = new Point3d(expectedPosition);
		pivot.add(this.tested2.getPivot());
		m = GeometryUtil.rotateAround(tr2.getQuaternion(), pivot);
		m.transform(expectedPosition);

		this.tested2.transform(tr2);
		
		assertEpsilonEquals(expectedPosition, this.tested2.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested2.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#transform(Transform3D)}.
	 */
	public void testTransform_Aligned_TranslationAndRotation_WithoutPivot() {
		Point3d expectedPosition;
		Quat4d expectedOrientation;
		
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomVector3D());
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested.getQuaternion());
		expectedOrientation.mul(tr1.getQuaternion(), expectedOrientation);
		
		expectedPosition = new Point3d(this.tested.getPosition3D());
		expectedPosition.add(tr1.getTranslation());

		this.tested.transform(tr1);
		
		assertEpsilonEquals(expectedPosition, this.tested.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(randomVector3D());
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested.getQuaternion());
		expectedOrientation.mul(tr2.getQuaternion(), expectedOrientation);
		
		expectedPosition = new Point3d(this.tested.getPosition3D());
		expectedPosition.add(tr2.getTranslation());

		this.tested.transform(tr2);
		
		assertEpsilonEquals(expectedPosition, this.tested.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#transform(Transform3D)}.
	 */
	public void testTransform_Oriented_TranslationAndRotation_WithoutPivot() {
		createOrientedBox();
		
		Point3d expectedPosition;
		Quat4d expectedOrientation;
		
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomVector3D());
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested2.getQuaternion());
		expectedOrientation.mul(tr1.getQuaternion(), expectedOrientation);
		
		expectedPosition = new Point3d(this.tested2.getPosition3D());
		expectedPosition.add(tr1.getTranslation());

		this.tested2.transform(tr1);
		
		assertEpsilonEquals(expectedPosition, this.tested2.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested2.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(randomVector3D());
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested2.getQuaternion());
		expectedOrientation.mul(tr2.getQuaternion(), expectedOrientation);
		
		expectedPosition = new Point3d(this.tested2.getPosition3D());
		expectedPosition.add(tr2.getTranslation());

		this.tested2.transform(tr2);
		
		assertEpsilonEquals(expectedPosition, this.tested2.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested2.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#transform(Transform3D)}.
	 */
	public void testTransform_Aligned_TranslationAndRotation_WithPivot() {
		Point3d expectedPosition, pivot;
		Quat4d expectedOrientation;
		
		this.tested.setPivot(10., -15., 10.);
		
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomVector3D());
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested.getQuaternion());
		expectedOrientation.mul(tr1.getQuaternion(), expectedOrientation);
		
		expectedPosition = new Point3d(this.tested.getPosition3D());
		pivot = new Point3d(expectedPosition);
		pivot.add(this.tested.getPivot());
		Matrix4d m = GeometryUtil.rotateAround(tr1.getQuaternion(), pivot);
		m.transform(expectedPosition);
		expectedPosition.add(tr1.getTranslation());

		this.tested.transform(tr1);
		
		assertEpsilonEquals(expectedPosition, this.tested.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(randomVector3D());
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested.getQuaternion());
		expectedOrientation.mul(tr2.getQuaternion(), expectedOrientation);
		
		expectedPosition = new Point3d(this.tested.getPosition3D());
		pivot = new Point3d(expectedPosition);
		pivot.add(this.tested.getPivot());
		m = GeometryUtil.rotateAround(tr2.getQuaternion(), pivot);
		m.transform(expectedPosition);
		expectedPosition.add(tr2.getTranslation());

		this.tested.transform(tr2);
		
		assertEpsilonEquals(expectedPosition, this.tested.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#transform(Transform3D)}.
	 */
	public void testTransform_Oriented_TranslationAndRotation_WithPivot() {
		createOrientedBox();
		
		Point3d expectedPosition, pivot;
		Quat4d expectedOrientation;
		
		this.tested2.setPivot(10., -15., 10.);
		
		Transform3D tr1 = new Transform3D();
		tr1.setTranslation(randomVector3D());
		tr1.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested2.getQuaternion());
		expectedOrientation.mul(tr1.getQuaternion(), expectedOrientation);
		
		expectedPosition = new Point3d(this.tested2.getPosition3D());
		pivot = new Point3d(expectedPosition);
		pivot.add(this.tested2.getPivot());
		Matrix4d m = GeometryUtil.rotateAround(tr1.getQuaternion(), pivot);
		m.transform(expectedPosition);
		expectedPosition.add(tr1.getTranslation());

		this.tested2.transform(tr1);
		
		assertEpsilonEquals(expectedPosition, this.tested2.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested2.getQuaternion());

		Transform3D tr2 = new Transform3D();
		tr2.setTranslation(randomVector3D());
		tr2.setRotation(new AxisAngle4d(
				CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
				(this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * MathConstants.TWO_PI));
		
		expectedOrientation = new Quat4d(this.tested2.getQuaternion());
		expectedOrientation.mul(tr2.getQuaternion(), expectedOrientation);
		
		expectedPosition = new Point3d(this.tested2.getPosition3D());
		pivot = new Point3d(expectedPosition);
		pivot.add(this.tested2.getPivot());
		m = GeometryUtil.rotateAround(tr2.getQuaternion(), pivot);
		m.transform(expectedPosition);
		expectedPosition.add(tr2.getTranslation());

		this.tested2.transform(tr2);
		
		assertEpsilonEquals(expectedPosition, this.tested2.getPosition3D());
		assertEpsilonEquals(expectedOrientation, this.tested2.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#getTransformMatrix()}.
	 */
	public void testGetTransformMatrix() {
		Transform3D tr = new Transform3D();
		tr.setIdentity();
		
		Point3d p = this.box.getCenter();
		p.z = this.box.getMinZ();
		tr.setTranslation(p);
		
		Transform3D actual = this.tested.getTransformMatrix();
		assertEpsilonEquals(tr.getTranslation(), actual.getTranslation());
		assertEpsilonEquals(tr.getQuaternion(), actual.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setTranslation(double, double, double)}.
	 */
	public void testSetTranslationDoubleDoubleDouble() {
		Point3d expected1 = randomPoint3D();
		this.tested.setTranslation(expected1.x, expected1.y, expected1.z);
		assertEpsilonEquals(expected1, this.tested.getPosition3D());

		Point3d expected2 = randomPoint3D();
		this.tested.setTranslation(expected2.x, expected2.y, expected2.z);
		assertEpsilonEquals(expected2, this.tested.getPosition3D());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setTranslation(javax.vecmath.Point3d)}.
	 */
	public void testSetTranslationPoint3d() {
		Point3d expected1 = randomPoint3D();
		this.tested.setTranslation(expected1);
		assertEpsilonEquals(expected1, this.tested.getPosition3D());

		Point3d expected2 = randomPoint3D();
		this.tested.setTranslation(expected2);
		assertEpsilonEquals(expected2, this.tested.getPosition3D());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#translate(double, double, double)}.
	 */
	public void testTranslateDoubleDoubleDouble() {
		EuclidianPoint3D lower = this.box.getLower();
		EuclidianPoint3D upper = this.box.getUpper();
		Point3d p = new Point3d();
		p.add(lower, upper);
		p.scale(.5);
		p.z = lower.z;

		Vector3d expected1 = randomVector3D();
		this.tested.translate(expected1.x, expected1.y, expected1.z);
		p.add(expected1);
		assertEpsilonEquals(p, this.tested.getPosition3D());

		Vector3d expected2 = randomVector3D();
		this.tested.translate(expected2.x, expected2.y, expected2.z);
		p.add(expected2);
		assertEpsilonEquals(p, this.tested.getPosition3D());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#translate(javax.vecmath.Vector3d)}.
	 */
	public void testTranslateVector3d() {
		EuclidianPoint3D lower = this.box.getLower();
		EuclidianPoint3D upper = this.box.getUpper();
		Point3d p = new Point3d();
		p.add(lower, upper);
		p.scale(.5);
		p.z = lower.z;

		Vector3d expected1 = randomVector3D();
		this.tested.translate(expected1);
		p.add(expected1);
		assertEpsilonEquals(p, this.tested.getPosition3D());

		Vector3d expected2 = randomVector3D();
		this.tested.translate(expected2);
		p.add(expected2);
		assertEpsilonEquals(p, this.tested.getPosition3D());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setRotation(javax.vecmath.Quat4d)}.
	 */
	public void testSetRotationQuat4d() {
		Quat4d expected1 = randomQuat4d();
		this.tested.setRotation(expected1);
		assertEpsilonEquals(expected1, this.tested.getQuaternion());

		Quat4d expected2 = randomQuat4d();
		this.tested.setRotation(expected2);
		assertEpsilonEquals(expected2, this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setRotation(javax.vecmath.AxisAngle4d)}.
	 */
	public void testSetRotationAxisAngle4d() {
		AxisAngle4d expected1 = randomAxisAngle4d();
		this.tested.setRotation(expected1);
		assertEpsilonEquals(expected1, this.tested.getAxisAngle());

		AxisAngle4d expected2 = randomAxisAngle4d();
		this.tested.setRotation(expected2);
		assertEpsilonEquals(expected2, this.tested.getAxisAngle());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setRotation(javax.vecmath.AxisAngle4d, javax.vecmath.Point3d)}.
	 */
	public void testSetRotationAxisAngle4dPoint3d() {
		Point3d pivot = new Point3d(10., -10., 10.);

		AxisAngle4d axisangle = randomAxisAngle4d();
		Quat4d quaternion = new Quat4d();
		quaternion.set(axisangle);
		
		Matrix4d m = GeometryUtil.rotateAround(quaternion, pivot);
		Point3d position = new Point3d(this.tested.getPosition3D());
		m.transform(position);
		
		this.tested.setRotation(axisangle, pivot);
		
		assertEpsilonEquals(position, this.tested.getPosition3D());
		assertEpsilonEquals(quaternion, this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setRotation(javax.vecmath.Quat4d, javax.vecmath.Point3d)}.
	 */
	public void testSetRotationQuat4dPoint3d() {
		Point3d pivot = new Point3d(10., -10., 10.);

		AxisAngle4d axisangle = randomAxisAngle4d();
		Quat4d quaternion = new Quat4d();
		quaternion.set(axisangle);
		
		Matrix4d m = GeometryUtil.rotateAround(quaternion, pivot);
		Point3d position = new Point3d(this.tested.getPosition3D());
		m.transform(position);
		
		this.tested.setRotation(quaternion, pivot);
		
		assertEpsilonEquals(position, this.tested.getPosition3D());
		assertEpsilonEquals(quaternion, this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setRotation(javax.vecmath.Quat4d, double, double, double)}.
	 */
	public void testSetRotationQuat4dDoubleDoubleDouble() {
		Point3d pivot = new Point3d(10., -10., 10.);

		AxisAngle4d axisangle = randomAxisAngle4d();
		Quat4d quaternion = new Quat4d();
		quaternion.set(axisangle);
		
		Matrix4d m = GeometryUtil.rotateAround(quaternion, pivot);
		Point3d position = new Point3d(this.tested.getPosition3D());
		m.transform(position);
		
		this.tested.setRotation(quaternion, pivot.x, pivot.y, pivot.z);
		
		assertEpsilonEquals(position, this.tested.getPosition3D());
		assertEpsilonEquals(quaternion, this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setRotation(javax.vecmath.AxisAngle4d, double, double, double)}.
	 */
	public void testSetRotationAxisAngle4dDoubleDoubleDouble() {
		Point3d pivot = new Point3d(10., -10., 10.);

		AxisAngle4d axisangle = randomAxisAngle4d();
		Quat4d quaternion = new Quat4d();
		quaternion.set(axisangle);
		
		Matrix4d m = GeometryUtil.rotateAround(quaternion, pivot);
		Point3d position = new Point3d(this.tested.getPosition3D());
		m.transform(position);
		
		this.tested.setRotation(axisangle, pivot.x, pivot.y, pivot.z);
		
		assertEpsilonEquals(position, this.tested.getPosition3D());
		assertEpsilonEquals(quaternion, this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#rotate(javax.vecmath.Quat4d)}.
	 */
	public void testRotateQuat4d() {
		Quat4d expectedRotation = new Quat4d(this.tested.getQuaternion());
		
		Quat4d rotation = randomQuat4d();
		
		expectedRotation.mul(rotation, expectedRotation);
		
		this.tested.rotate(rotation);
		
		assertEpsilonEquals(expectedRotation, this.tested.getQuaternion());

		rotation = randomQuat4d();
		
		expectedRotation.mul(rotation, expectedRotation);
		
		this.tested.rotate(rotation);
		
		assertEpsilonEquals(expectedRotation, this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#rotate(javax.vecmath.Quat4d, javax.vecmath.Point3d)}.
	 */
	public void testRotateQuat4dPoint3d() {
		Point3d position = new Point3d(this.tested.getPosition3D());
		Quat4d rotation = randomQuat4d();
		Point3d pivot = new Point3d(position);
		pivot.add(new Vector3d(10, -10, 10));
		
		Point3d expectedPosition = new Point3d(position);
		Matrix4d m = GeometryUtil.rotateAround(rotation, pivot);
		m.transform(expectedPosition);
		
		Quat4d expectedRotation = new Quat4d(this.tested.getQuaternion());
		expectedRotation.mul(rotation, expectedRotation);
		
		this.tested.rotate(rotation, pivot);
		
		assertEpsilonEquals(expectedPosition, this.tested.getPosition3D());
		assertEpsilonEquals(expectedRotation, this.tested.getQuaternion());

		position = this.tested.getPosition3D();
		expectedPosition = new Point3d(position);
		pivot = new Point3d(expectedPosition);
		pivot.add(new Vector3d(10, -10, 10));
		rotation = randomQuat4d();		
		m = GeometryUtil.rotateAround(rotation, pivot);
		m.transform(expectedPosition);
		
		expectedRotation.mul(rotation, expectedRotation);
		
		this.tested.rotate(rotation, pivot);
		
		assertEpsilonEquals(expectedRotation, this.tested.getQuaternion());
		assertEpsilonEquals(expectedPosition, this.tested.getPosition3D());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#setIdentityTransform()}.
	 */
	public void testSetIdentityTransform() {
		this.tested.setIdentityTransform();
		assertEpsilonEquals(new Point3d(), this.tested.getPosition3D());
		assertEpsilonEquals(new Quat4d(0.,0.,0.,1.), this.tested.getQuaternion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#getLinearSpeed()}.
	 */
	public void testGetLinearSpeed() {
		assertEpsilonEquals(0., this.tested.getLinearSpeed());

		Vector3d v;
		
		v = new Vector3d(100., 0., 0.);
		this.tested.translate(v);
		assertEpsilonEquals(200., this.tested.getLinearSpeed());

		v = new Vector3d(-10., -230., 0.);
		this.tested.translate(v);
		assertEpsilonEquals(v.length()*2., this.tested.getLinearSpeed());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#getAngularSpeed()}.
	 */
	public void testGetAngularSpeed() {
		assertEpsilonEquals(0., this.tested.getAngularSpeed());

		this.tested.rotate(new AxisAngle4d(0,0,1,Math.PI));
		assertEpsilonEquals(Math.PI*2., this.tested.getAngularSpeed());

		this.tested.rotate(new AxisAngle4d(1,0,0,MathConstants.DEMI_PI));
		assertEpsilonEquals(MathConstants.DEMI_PI*2., this.tested.getAngularSpeed());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#getLinearAcceleration()}.
	 */
	public void testGetLinearAcceleration() {
		assertEpsilonEquals(0., this.tested.getLinearAcceleration());
		
		Vector3d v;
		
		v = new Vector3d(100., 0., 0.);
		this.tested.translate(v);
		assertEpsilonEquals(v.length()*4.,  this.tested.getLinearAcceleration());

		double l = v.length();
		
		v = new Vector3d(-10., -230., 0.);
		l = v.length() - l;
		this.tested.translate(v);
		assertEpsilonEquals(l*4., this.tested.getLinearAcceleration());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.MobileEntity3D#getAngularAcceleration()}.
	 * @throws Exception
	 */
	public void testGetAngularAcceleration() throws Exception {
		assertEpsilonEquals(0., this.tested.getAngularAcceleration());
		
		AxisAngle4d a;
		
		a = new AxisAngle4d(0., 0., 1., Math.PI);
		this.tested.rotate(a);
		assertEpsilonEquals(Math.PI*4.,  this.tested.getAngularAcceleration());

		a = new AxisAngle4d(0., 0., 1., Math.PI);
		this.tested.rotate(a);
		assertEpsilonEquals(0., this.tested.getAngularAcceleration());

		a = new AxisAngle4d(0., 0., 1., 0.);
		this.tested.rotate(a);
		assertEpsilonEquals(-4.*Math.PI, this.tested.getAngularAcceleration());

		a = new AxisAngle4d(0., 0., 1., -Math.PI);
		this.tested.rotate(a);
		assertEpsilonEquals(4.*Math.PI, this.tested.getAngularAcceleration());
	}

	/**
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid fr.utbm.set.sfc.jasim
	 * @mavenartifactid jasim-environment-base
	 */
	private final class ClockStub extends AbstractClock {

		/**
		 */
		double currentTime;
		/**
		 */
		double duration;
		
		/**
		 * @param time
		 * @param duration
		 */
		public ClockStub(double time, double duration) {
			super(TimeUnit.SECONDS);
			this.currentTime = time;
			this.duration = duration;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public double getSimulationStepDuration(TimeUnit desired_unit) {
			return MeasureUnitUtil.convert(this.duration, TimeUnit.SECONDS, desired_unit);
		}

		/** {@inheritDoc}
		 */
		@Override
		public double getSimulationTime(TimeUnit desired_unit) {
			return MeasureUnitUtil.convert(this.currentTime, TimeUnit.SECONDS, desired_unit);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long getSimulationStepCount() {
			return 0;
		}
		
	}

}
