/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.tree.structures.octree;

import junit.framework.TestCase;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.tree.node.IcosepOctTreeNode.IcosepOctTreeZone;
import fr.utbm.set.tree.node.OctTreeNode.OctTreeZone;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OctTreeUtilTest extends TestCase {

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.tree.structures.octree.OctTreeUtil#classifiesBox(double, double, double, fr.utbm.set.geom.object.EuclidianPoint, fr.utbm.set.geom.object.EuclidianPoint)}.
	 */
	public static void testClassifiesBox() {
		assertEquals(
				OctTreeZone.SOUTH_WEST_FRONT.ordinal(),
				OctTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(0.,0.,0.),
						new EuclidianPoint3D(1.,1.,1.)));

		assertEquals(
				OctTreeZone.SOUTH_WEST_BACK.ordinal(),
				OctTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(0.,1000.,0.),
						new EuclidianPoint3D(1.,1001.,1.)));

		assertEquals(
				OctTreeZone.SOUTH_EAST_FRONT.ordinal(),
				OctTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(1000.,0.,0.),
						new EuclidianPoint3D(1001.,1.,1.)));

		assertEquals(
				OctTreeZone.SOUTH_EAST_BACK.ordinal(),
				OctTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(1000.,1000.,0.),
						new EuclidianPoint3D(1001.,1001.,1.)));

		assertEquals(
				OctTreeZone.NORTH_WEST_FRONT.ordinal(),
				OctTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(0.,0.,1000.),
						new EuclidianPoint3D(1.,1.,1001.)));

		assertEquals(
				OctTreeZone.NORTH_WEST_BACK.ordinal(),
				OctTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(0.,1000.,1000.),
						new EuclidianPoint3D(1.,1001.,1001.)));

		assertEquals(
				OctTreeZone.NORTH_EAST_FRONT.ordinal(),
				OctTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(1000.,0.,1000.),
						new EuclidianPoint3D(1001.,1.,1001.)));

		assertEquals(
				OctTreeZone.NORTH_EAST_BACK.ordinal(),
				OctTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(1000.,1000.,1000.),
						new EuclidianPoint3D(1001.,1001.,1001.)));

		assertEquals(
				-1,
				OctTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(0.,0.,0.),
						new EuclidianPoint3D(1000.,1.,1.)));

		assertEquals(
				-2,
				OctTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(0.,0.,0.),
						new EuclidianPoint3D(1.,1000.,1.)));

		assertEquals(
				-3,
				OctTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(0.,0.,0.),
						new EuclidianPoint3D(1.,1.,1000.)));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.tree.structures.octree.OctTreeUtil#classifiesBoxWithIcosep(double, double, double, fr.utbm.set.geom.object.EuclidianPoint, fr.utbm.set.geom.object.EuclidianPoint)}.
	 */
	public static void testClassifiesBoxWithIcosep() {
		assertEquals(
				IcosepOctTreeZone.SOUTH_WEST_FRONT.ordinal(),
				OctTreeUtil.classifiesBoxWithIcosep(100., 100., 100., 
						new EuclidianPoint3D(0.,0.,0.),
						new EuclidianPoint3D(1.,1.,1.)));

		assertEquals(
				IcosepOctTreeZone.SOUTH_WEST_BACK.ordinal(),
				OctTreeUtil.classifiesBoxWithIcosep(100., 100., 100., 
						new EuclidianPoint3D(0.,1000.,0.),
						new EuclidianPoint3D(1.,1001.,1.)));

		assertEquals(
				IcosepOctTreeZone.SOUTH_EAST_FRONT.ordinal(),
				OctTreeUtil.classifiesBoxWithIcosep(100., 100., 100., 
						new EuclidianPoint3D(1000.,0.,0.),
						new EuclidianPoint3D(1001.,1.,1.)));

		assertEquals(
				IcosepOctTreeZone.SOUTH_EAST_BACK.ordinal(),
				OctTreeUtil.classifiesBoxWithIcosep(100., 100., 100., 
						new EuclidianPoint3D(1000.,1000.,0.),
						new EuclidianPoint3D(1001.,1001.,1.)));

		assertEquals(
				IcosepOctTreeZone.NORTH_WEST_FRONT.ordinal(),
				OctTreeUtil.classifiesBoxWithIcosep(100., 100., 100., 
						new EuclidianPoint3D(0.,0.,1000.),
						new EuclidianPoint3D(1.,1.,1001.)));

		assertEquals(
				IcosepOctTreeZone.NORTH_WEST_BACK.ordinal(),
				OctTreeUtil.classifiesBoxWithIcosep(100., 100., 100., 
						new EuclidianPoint3D(0.,1000.,1000.),
						new EuclidianPoint3D(1.,1001.,1001.)));

		assertEquals(
				IcosepOctTreeZone.NORTH_EAST_FRONT.ordinal(),
				OctTreeUtil.classifiesBoxWithIcosep(100., 100., 100., 
						new EuclidianPoint3D(1000.,0.,1000.),
						new EuclidianPoint3D(1001.,1.,1001.)));

		assertEquals(
				IcosepOctTreeZone.NORTH_EAST_BACK.ordinal(),
				OctTreeUtil.classifiesBoxWithIcosep(100., 100., 100., 
						new EuclidianPoint3D(1000.,1000.,1000.),
						new EuclidianPoint3D(1001.,1001.,1001.)));

		assertEquals(
				IcosepOctTreeZone.ICOSEP.ordinal(),
				OctTreeUtil.classifiesBoxWithIcosep(100., 100., 100., 
						new EuclidianPoint3D(0.,0.,0.),
						new EuclidianPoint3D(1000.,1.,1.)));

		assertEquals(
				IcosepOctTreeZone.ICOSEP.ordinal(),
				OctTreeUtil.classifiesBoxWithIcosep(100., 100., 100., 
						new EuclidianPoint3D(0.,0.,0.),
						new EuclidianPoint3D(1.,1000.,1.)));

		assertEquals(
				IcosepOctTreeZone.ICOSEP.ordinal(),
				OctTreeUtil.classifiesBoxWithIcosep(100., 100., 100., 
						new EuclidianPoint3D(0.,0.,0.),
						new EuclidianPoint3D(1.,1.,1000.)));
	}

}
