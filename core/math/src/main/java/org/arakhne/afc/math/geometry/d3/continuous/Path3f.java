package org.arakhne.afc.math.geometry.d3.continuous;

import java.lang.ref.SoftReference;
import java.util.Iterator;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Path2D;
import org.arakhne.afc.math.geometry.d2.continuous.AbstractPathElement2F;
import org.arakhne.afc.math.geometry.d2.continuous.AbstractShape2F;
import org.arakhne.afc.math.geometry.d2.continuous.Path2f;
import org.arakhne.afc.math.geometry.d2.continuous.PathIterator2f;
import org.arakhne.afc.math.geometry.d2.continuous.Rectangle2f;
import org.arakhne.afc.math.geometry.d2.continuous.Shape2F;
import org.arakhne.afc.math.geometry.d3.Path3D;
import org.arakhne.afc.math.geometry.d3.Point3D;

//TODO : verify if we need to use OrientedBox3f or AlignedBox3f
/** A generic 3D-path.
*
* @author $Author: hjaffali$
* @version $FullVersion$
* @mavengroupid $GroupId$
* @mavenartifactid $ArtifactId$
*/
public class Path3f extends AbstractShape3F<Path3f> implements Path3D<Shape3F,AbstractOrientedBox3F,AbstractPathElement3F,PathIterator3f> {

	private static final long serialVersionUID = -8167977956565440101L;
	
	/** Multiple of cubic & quad curve size.
	 */
	//TODO : VERIFY THIS CONSTANT FOR 3D UTILISATION
	static final int GROW_SIZE = 24;
	
	
	
	/** Array of types.
	 */
	PathElementType[] types;

	/** Array of coords.
	 */
	double[] coords;

	/** Number of types in the array.
	 */
	int numTypes = 0;

	/** Number of coords in the array.
	 */
	int numCoords = 0;

	/** Winding rule for the path.
	 */
	PathWindingRule windingRule;

	/** Indicates if the path is empty.
	 * The path is empty when there is no point inside, or
	 * all the points are at the same coordinate, or
	 * when the path does not represents a drawable path
	 * (a path with a line or a curve).
	 */
	private Boolean isEmpty = Boolean.TRUE;

	/** Indicates if the path contains base primitives
	 * (no curve).
	 */
	private Boolean isPolyline = Boolean.TRUE;

	/** Buffer for the bounds of the path that corresponds
	 * to the points really on the path (eg, the pixels
	 * drawn). The control points of the curves are
	 * not considered in this bounds.
	 */
	private SoftReference<AbstractOrientedBox3F> graphicalBounds = null;

	/** Buffer for the bounds of the path that corresponds
	 * to all the points added in the path.
	 */
	private SoftReference<AbstractOrientedBox3F> logicalBounds = null;

	/**
	 */
	public Path3f() {
		this(PathWindingRule.NON_ZERO);
	}

	/**
	 * @param iterator
	 */
	public Path3f(Iterator<AbstractPathElement3F> iterator) {
		this(PathWindingRule.NON_ZERO, iterator);
	}

	/**
	 * @param windingRule1
	 */
	public Path3f(PathWindingRule windingRule1) {
		assert(windingRule1!=null);
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new double[GROW_SIZE];
		this.windingRule = windingRule1;
	}

	/**
	 * @param windingRule1
	 * @param iterator
	 */
	public Path3f(PathWindingRule windingRule1, Iterator<AbstractPathElement3F> iterator) {
		assert(windingRule1!=null);
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new double[GROW_SIZE];
		this.windingRule = windingRule1;
		add(iterator);
	}

	/**
	 * @param p
	 */
	public Path3f(Path3f p) {
		this.coords = p.coords.clone();
		this.isEmpty = p.isEmpty;
		this.isPolyline = p.isPolyline;
		this.numCoords = p.numCoords;
		this.types = p.types.clone();
		this.windingRule = p.windingRule;
		AbstractOrientedBox3F box;
		box = p.graphicalBounds==null ? null : p.graphicalBounds.get();
		if (box!=null) {
			this.graphicalBounds = new SoftReference<>(box.clone());
		}
		box = p.logicalBounds==null ? null : p.logicalBounds.get();
		if (box!=null) {
			this.logicalBounds = new SoftReference<>(box.clone());
		}
	}
	@Override
	public boolean isEmpty() {
		if (this.isEmpty==null) {
			this.isEmpty = Boolean.TRUE;
			PathIterator3f pi = getPathIterator();
			AbstractPathElement3F pe;
			while (this.isEmpty==Boolean.TRUE && pi.hasNext()) {
				pe = pi.next();
				if (pe.isDrawable()) { 
					this.isEmpty = Boolean.FALSE;
				}
			}
		}
		return this.isEmpty.booleanValue();
	}

	@Override
	public void clear() {
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new double[GROW_SIZE];
		this.windingRule = PathWindingRule.NON_ZERO;
		this.numCoords = 0;
		this.numTypes = 0;
		this.isEmpty = Boolean.TRUE;
		this.isPolyline = Boolean.TRUE;
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	@Override
	public Point3D getClosestPointTo(Point3D p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point3D getFarthestPointTo(Point3D p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set(Shape3F s) {
		clear();
		add(s.getPathIterator());
	}

	@Override
	public AbstractBoxedShape3F<?> toBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toBoundingBox(AbstractBoxedShape3F<?> box) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double distanceSquared(Point3D p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double distanceL1(Point3D p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double distanceLinf(Point3D p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void transform(Transform3D transformationMatrix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void translate(double dx, double dy, double dz) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(double x, double y, double z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(AlignedBox3f s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(AbstractSphere3F s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(AbstractSegment3F s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(Triangle3f s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(AbstractCapsule3F s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(AbstractOrientedBox3F s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(Plane3D<?> p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PathWindingRule getWindingRule() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPolyline() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PathIterator3f getPathIterator(double flatness) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PathIterator3f getPathIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AbstractBoxedShape3F<?> toBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PathIterator3f getPathIterator(Transform3D transform) {
		// TODO Auto-generated method stub
		return null;
	}
  
	
	
}
