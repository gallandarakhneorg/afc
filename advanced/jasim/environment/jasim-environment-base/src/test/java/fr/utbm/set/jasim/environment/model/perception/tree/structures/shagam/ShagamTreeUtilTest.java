/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.tree.structures.shagam;

import junit.framework.TestCase;
import fr.utbm.set.geom.object.EuclidianPoint3D;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ShagamTreeUtilTest extends TestCase {

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.tree.structures.shagam.ShagamTreeUtil#classifiesBox(double, double, double, fr.utbm.set.geom.object.EuclidianPoint, fr.utbm.set.geom.object.EuclidianPoint)}.
	 */
	public static void testClassifiesBox() {
		assertEquals(
				ShagamTreeZone.SOUTH_WEST_FRONT_VOXEL.ordinal(),
				ShagamTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(0.,0.,0.),
						new EuclidianPoint3D(1.,1.,1.)));

		assertEquals(
				ShagamTreeZone.SOUTH_WEST_BACK_VOXEL.ordinal(),
				ShagamTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(0.,1000.,0.),
						new EuclidianPoint3D(1.,1001.,1.)));

		assertEquals(
				ShagamTreeZone.SOUTH_EAST_FRONT_VOXEL.ordinal(),
				ShagamTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(1000.,0.,0.),
						new EuclidianPoint3D(1001.,1.,1.)));

		assertEquals(
				ShagamTreeZone.SOUTH_EAST_BACK_VOXEL.ordinal(),
				ShagamTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(1000.,1000.,0.),
						new EuclidianPoint3D(1001.,1001.,1.)));

		assertEquals(
				ShagamTreeZone.NORTH_WEST_FRONT_VOXEL.ordinal(),
				ShagamTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(0.,0.,1000.),
						new EuclidianPoint3D(1.,1.,1001.)));

		assertEquals(
				ShagamTreeZone.NORTH_WEST_BACK_VOXEL.ordinal(),
				ShagamTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(0.,1000.,1000.),
						new EuclidianPoint3D(1.,1001.,1001.)));

		assertEquals(
				ShagamTreeZone.NORTH_EAST_FRONT_VOXEL.ordinal(),
				ShagamTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(1000.,0.,1000.),
						new EuclidianPoint3D(1001.,1.,1001.)));

		assertEquals(
				ShagamTreeZone.NORTH_EAST_BACK_VOXEL.ordinal(),
				ShagamTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(1000.,1000.,1000.),
						new EuclidianPoint3D(1001.,1001.,1001.)));

		assertEquals(
				ShagamTreeZone.ICOSEP_YZ_SOUTH_FRONT_PLANE.ordinal(),
				ShagamTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(0.,0.,0.),
						new EuclidianPoint3D(1000.,1.,1.)));

		assertEquals(
				ShagamTreeZone.ICOSEP_XZ_SOUTH_WEST_PLANE.ordinal(),
				ShagamTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(0.,0.,0.),
						new EuclidianPoint3D(1.,1000.,1.)));

		assertEquals(
				ShagamTreeZone.ICOSEP_XY_WEST_FRONT_PLANE.ordinal(),
				ShagamTreeUtil.classifiesBox(100., 100., 100., 
						new EuclidianPoint3D(0.,0.,0.),
						new EuclidianPoint3D(1.,1.,1000.)));
	}

}
