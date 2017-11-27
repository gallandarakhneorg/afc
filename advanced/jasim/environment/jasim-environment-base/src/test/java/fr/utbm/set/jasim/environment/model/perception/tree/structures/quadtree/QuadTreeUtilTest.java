/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.tree.structures.quadtree;

import junit.framework.TestCase;
import fr.utbm.set.geom.object.EuclidianPoint2D;
import fr.utbm.set.tree.node.IcosepQuadTreeNode.IcosepQuadTreeZone;
import fr.utbm.set.tree.node.QuadTreeNode.QuadTreeZone;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class QuadTreeUtilTest extends TestCase {

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.tree.structures.quadtree.QuadTreeUtil#classifiesBox(double, double, fr.utbm.set.geom.object.EuclidianPoint, fr.utbm.set.geom.object.EuclidianPoint)}.
	 */
	public static void testClassifiesBox() {
		assertEquals(
				QuadTreeZone.SOUTH_WEST.ordinal(),
				QuadTreeUtil.classifiesBox(100., 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1.,1.)));

		assertEquals(
				QuadTreeZone.NORTH_EAST.ordinal(),
				QuadTreeUtil.classifiesBox(-100., -100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1.,1.)));

		assertEquals(
				QuadTreeZone.SOUTH_EAST.ordinal(),
				QuadTreeUtil.classifiesBox(-100., 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1.,1.)));

		assertEquals(
				QuadTreeZone.NORTH_WEST.ordinal(),
				QuadTreeUtil.classifiesBox(100., -100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1.,1.)));

		assertEquals(
				-1,
				QuadTreeUtil.classifiesBox(100., 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1000.,1.)));

		assertEquals(
				-2,
				QuadTreeUtil.classifiesBox(100., 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1.,1000.)));

		assertEquals(
				-1,
				QuadTreeUtil.classifiesBox(100., 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1000.,1000.)));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.tree.structures.quadtree.QuadTreeUtil#classifiesBoxWithIcosep(double, double, fr.utbm.set.geom.object.EuclidianPoint, fr.utbm.set.geom.object.EuclidianPoint)}.
	 */
	public static void testClassifiesBoxWithIcosep() {
		assertEquals(
				IcosepQuadTreeZone.SOUTH_WEST.ordinal(),
				QuadTreeUtil.classifiesBoxWithIcosep(100., 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1.,1.)));

		assertEquals(
				IcosepQuadTreeZone.NORTH_EAST.ordinal(),
				QuadTreeUtil.classifiesBoxWithIcosep(-100., -100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1.,1.)));

		assertEquals(
				IcosepQuadTreeZone.SOUTH_EAST.ordinal(),
				QuadTreeUtil.classifiesBoxWithIcosep(-100., 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1.,1.)));

		assertEquals(
				IcosepQuadTreeZone.NORTH_WEST.ordinal(),
				QuadTreeUtil.classifiesBoxWithIcosep(100., -100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1.,1.)));

		assertEquals(
				IcosepQuadTreeZone.ICOSEP.ordinal(),
				QuadTreeUtil.classifiesBoxWithIcosep(100., 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1000.,1.)));

		assertEquals(
				IcosepQuadTreeZone.ICOSEP.ordinal(),
				QuadTreeUtil.classifiesBoxWithIcosep(100., 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1.,1000.)));

		assertEquals(
				IcosepQuadTreeZone.ICOSEP.ordinal(),
				QuadTreeUtil.classifiesBoxWithIcosep(100., 100., 
						new EuclidianPoint2D(0.,0.),
						new EuclidianPoint2D(1000.,1000.)));
	}

}
