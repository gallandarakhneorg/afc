/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree;

import junit.framework.TestCase;
import fr.utbm.set.geom.object.EuclidianPoint2D;
import fr.utbm.set.tree.node.BinaryTreeNode.BinaryTreeZone;
import fr.utbm.set.tree.node.IcosepBinaryTreeNode.IcosepBinaryTreeZone;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BspTreeUtilTest extends TestCase {

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree.BspTreeUtil#classifiesOnCoordinates(int, double, fr.utbm.set.geom.object.EuclidianPoint, fr.utbm.set.geom.object.EuclidianPoint)}.
	 */
	public static void testClassifiesOnCoordinates() {
		assertEquals(
				BinaryTreeZone.LEFT.ordinal(),
				BspTreeUtil.classifiesOnCoordinates(0, 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1.,0.)));

		assertEquals(
				BinaryTreeZone.RIGHT.ordinal(),
				BspTreeUtil.classifiesOnCoordinates(0, 100., 
						new EuclidianPoint2D(1000.,0.),
						new EuclidianPoint2D(1001.,0.)));

		assertEquals(
				-1,
				BspTreeUtil.classifiesOnCoordinates(0, 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1000.,0.)));

		assertEquals(
				BinaryTreeZone.LEFT.ordinal(),
				BspTreeUtil.classifiesOnCoordinates(1, 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(0.,1.)));

		assertEquals(
				BinaryTreeZone.RIGHT.ordinal(),
				BspTreeUtil.classifiesOnCoordinates(1, 100., 
						new EuclidianPoint2D(0.,1000.),
						new EuclidianPoint2D(0.,1001.)));

		assertEquals(
				-1,
				BspTreeUtil.classifiesOnCoordinates(1, 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(0.,1000.)));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree.BspTreeUtil#classifiesOnCoordinatesWithIcosep(int, double, fr.utbm.set.geom.object.EuclidianPoint, fr.utbm.set.geom.object.EuclidianPoint)}.
	 */
	public static void testClassifiesOnCoordinatesWithIcosep() {
		assertEquals(
				IcosepBinaryTreeZone.LEFT.ordinal(),
				BspTreeUtil.classifiesOnCoordinatesWithIcosep(0, 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1.,0.)));

		assertEquals(
				IcosepBinaryTreeZone.RIGHT.ordinal(),
				BspTreeUtil.classifiesOnCoordinatesWithIcosep(0, 100., 
						new EuclidianPoint2D(1000.,0.),
						new EuclidianPoint2D(1001.,0.)));

		assertEquals(
				IcosepBinaryTreeZone.ICOSEP.ordinal(),
				BspTreeUtil.classifiesOnCoordinatesWithIcosep(0, 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1000.,0.)));

		assertEquals(
				IcosepBinaryTreeZone.LEFT.ordinal(),
				BspTreeUtil.classifiesOnCoordinatesWithIcosep(1, 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(0.,1.)));

		assertEquals(
				IcosepBinaryTreeZone.RIGHT.ordinal(),
				BspTreeUtil.classifiesOnCoordinatesWithIcosep(1, 100., 
						new EuclidianPoint2D(0.,1000.),
						new EuclidianPoint2D(0.,1001.)));

		assertEquals(
				IcosepBinaryTreeZone.ICOSEP.ordinal(),
				BspTreeUtil.classifiesOnCoordinatesWithIcosep(1, 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(0.,1000.)));
	}

}
