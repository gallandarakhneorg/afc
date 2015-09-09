/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.convexhull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d3.continuous.Plane4f;
import org.arakhne.afc.math.geometry.d3.continuous.PlaneClassification;
import org.arakhne.afc.math.geometry.d3.continuous.Point3f;
import org.arakhne.afc.math.geometry.d3.continuous.Triangle3f;
import org.arakhne.afc.math.geometry.d3.continuous.Tuple3f;


/** This class permits to create convex hull from a
 * set of points with the divide and conquer algorithm.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DivideAndConquerAlgorithm implements ConvexHullAlgorithm {

	private Point3f[] pointList = null;
	private final Collection<HullObject<Point3f>> convexHull = new ArrayList<HullObject<Point3f>>();
	private Map<Integer,DInteger> indexes = new TreeMap<Integer,DInteger>();
	
	/**
	 * Select the points that corresponds to the convex envelop
	 * of the set of points.
	 * <p>
	 * The divide-and-conquer algorithm is an algorithm for
	 * computing the convex hull of a set of points in two
	 * or more dimensions.
	 * <ol>
	 * 
	 * <li>Divide the points into two equal sized sets <code>L</code>
	 * and <code>R</code> such that all points of <code>L</code> are
	 * to the left of the most leftmost points in <code>R</code>.<br>
	 * Recursively find the convex hull of <code>L</code> (shown in
	 * light blue) and <code>R</code> (shown in purple).</li>
	 * 
	 * <li>To merge in 3D need to construct a cylinder of triangles
	 * connecting the left hull and the right hull.</li>
	 * 
	 * <li>One edge of the cylinder <code>AB</code> is just the lower
	 * common tangent. The upper common tangent can be found in
	 * linear time by scanning around the left hull in a clockwise
	 * direction and around the right hull in an anti-clockwise
	 * direction. The two tangents divide each hull into two pieces.
	 * The edges belonging to one of thse pieces must be deleted.</li>
	 * 
	 * <li>We next need to find a triangle ABC belonging to the
	 * cylinder which has AB as one of its edges. The third vertex
	 * of the triangle (C) must belong to either the left hull or the
	 * right hull. (In this case it belongs to the right hull.)
	 * Consequently, either AC is an edge of the left hull or BC is
	 * an edge of the right hull. (In this case BC is an edge of the
	 * right hull.) Hence, when considering possible candidates for the
	 * third vertex it is only necessary to consider candidates that
	 * are adjacent to A in the left hull or adjacent to B in the
	 * right hull.<br>
	 * If we use a data structure for the hulls that allow us to find
	 * an adjacent vertex in constant time then the search for vertex
	 * C takes time proportional to the number of candidate vertices
	 * checked.</li>
	 * 
	 * <li>After finding triangle ABC we now have a new edge AC that
	 * joins the left hull and the right hull. We can find triangle
	 * ACD just as we did ABC. Continuing in this way, we can find all
	 * the triangles belonging to the cylinder, ending when we get back
	 * to AB.<br>
	 * The total time to find the cylinder is O(n) so the algorithm
	 * takes O(n log n) time.</li>
	 * 
	 * </ol>
	 * 
	 * @param listOfPoint is the set of points from which the
	 * convex hull must be generated.
	 * @return the convex hull
	 * @throws ConcurrentModificationException
	 */
	@Override
	public Point3f[] computeConvexHull(Point3f... listOfPoint) {
		
		// Compute the convex hull
		Collection<HullObject<Point3f>> hull = computeConvexHullTriangles(listOfPoint);

		// Rebuild the list of points
		Set<Tuple3f> lConvexHull = new HashSet<Tuple3f>();
		for (HullObject<Point3f> hullObject : hull) {
			Tuple3f[] pts = hullObject.getObjectPoints(listOfPoint);
			for (Tuple3f tuple3d : pts) {
				lConvexHull.add(tuple3d);
			}
		}
		hull.clear();
		
		Point3f[] tab = new Point3f[lConvexHull.size()];
		lConvexHull.toArray(tab);
		lConvexHull.clear();
		
		return tab;
	}
	
	/**
	 * Select the points that corresponds to the convex envelop
	 * of the set of points.
	 * <p>
	 * The divide-and-conquer algorithm is an algorithm for
	 * computing the convex hull of a set of points in two
	 * or more dimensions.
	 * <ol>
	 * 
	 * <li>Divide the points into two equal sized sets <code>L</code>
	 * and <code>R</code> such that all points of <code>L</code> are
	 * to the left of the most leftmost points in <code>R</code>.<br>
	 * Recursively find the convex hull of <code>L</code> (shown in
	 * light blue) and <code>R</code> (shown in purple).</li>
	 * 
	 * <li>To merge in 3D need to construct a cylinder of triangles
	 * connecting the left hull and the right hull.</li>
	 * 
	 * <li>One edge of the cylinder <code>AB</code> is just the lower
	 * common tangent. The upper common tangent can be found in
	 * linear time by scanning around the left hull in a clockwise
	 * direction and around the right hull in an anti-clockwise
	 * direction. The two tangents divide each hull into two pieces.
	 * The edges belonging to one of thse pieces must be deleted.</li>
	 * 
	 * <li>We next need to find a triangle ABC belonging to the
	 * cylinder which has AB as one of its edges. The third vertex
	 * of the triangle (C) must belong to either the left hull or the
	 * right hull. (In this case it belongs to the right hull.)
	 * Consequently, either AC is an edge of the left hull or BC is
	 * an edge of the right hull. (In this case BC is an edge of the
	 * right hull.) Hence, when considering possible candidates for the
	 * third vertex it is only necessary to consider candidates that
	 * are adjacent to A in the left hull or adjacent to B in the
	 * right hull.<br>
	 * If we use a data structure for the hulls that allow us to find
	 * an adjacent vertex in constant time then the search for vertex
	 * C takes time proportional to the number of candidate vertices
	 * checked.</li>
	 * 
	 * <li>After finding triangle ABC we now have a new edge AC that
	 * joins the left hull and the right hull. We can find triangle
	 * ACD just as we did ABC. Continuing in this way, we can find all
	 * the triangles belonging to the cylinder, ending when we get back
	 * to AB.<br>
	 * The total time to find the cylinder is O(n) so the algorithm
	 * takes O(n log n) time.</li>
	 * 
	 * </ol>
	 * 
	 * @param listOfPoints is the set of points from which the
	 * convex hull must be generated.
	 * @return the convex hull
	 */
	@Override
	public Collection<HullObject<Point3f>> computeConvexHullTriangles(Point3f... listOfPoints) {
		
		if (this.pointList!=null)
			 throw new ConcurrentModificationException();

		this.pointList = listOfPoints;
		this.convexHull.clear();
		
		// Trivial case
		{
			if (createFacesForBasicCases(0, 0, listOfPoints.length-1))
				return this.convexHull;
		}
		
		// Sort the points
		Arrays.sort(listOfPoints, new Comparator<Point3f>() {
			@Override
			public int compare(Point3f o1, Point3f o2) {
				int c = Double.compare(o1.getX(),o2.getX());
				if (c!=0) return c;
				c = Double.compare(o1.getY(),o2.getY());
				if (c!=0) return c;
				return Double.compare(o1.getZ(),o2.getZ());
			}
		});
		
		// Compute the convex hull
		divideAndConquer(1, 0, listOfPoints.length-1);
		
		return this.convexHull;
	}
	
	/** Divide and Conquer Algorithm.
	 */
	private Map<Integer,DInteger> divideAndConquer(int creationLevel, final int startIndex, final int endIndex) {
		// Trivial case: convex hull is a canonical 3D entity
		if (createFacesForBasicCases(creationLevel, startIndex, endIndex)) {
			if (creationLevel>1) return consumeLastInsertedIndexes();
			return null;
		}

		//
		// STEP 1: Compute two convex hulls that respectively contains an half
		//         of the points
		//
		
		// Recurse on the two groups to find the convex hulls
		List<Integer> leftIndexes, rightIndexes;
		
		{
			final int midIndex = (startIndex+endIndex) / 2;
			Map<Integer,DInteger> leftMap = divideAndConquer(creationLevel+1, startIndex, midIndex);
			Map<Integer,DInteger> rightMap = divideAndConquer(creationLevel+1, midIndex+1, endIndex);
			
			if ((leftMap==null)||(leftMap.isEmpty())) {
				if (creationLevel>1) return rightMap;
				return null;
			}
			if ((rightMap==null)||(rightMap.isEmpty())) {
				if (creationLevel>1) return leftMap;
				return null;
			}
			
			// Fill the index reference count
			leftIndexes = new ArrayList<Integer>();
			rightIndexes = new ArrayList<Integer>();
			
			for (Entry<Integer,DInteger> p : leftMap.entrySet()) {
				incrementIndexReference(p.getKey(),p.getValue().intValue());
				leftIndexes.add(p.getKey());
			}
			Collections.sort(leftIndexes);
			
			for (Entry<Integer,DInteger> p : rightMap.entrySet()) {
				incrementIndexReference(p.getKey(),p.getValue().intValue());
				rightIndexes.add(p.getKey());
			}			
			Collections.sort(rightIndexes);
			
		}
		
		//
		// STEP 2: Find the edge which is tangent to the two
		//         convex hulls.
		//

		// Indicate if the solution could only be an edge
	    final boolean solutionIsEdge = (leftIndexes.size()==1)&&(rightIndexes.size()==1);
	    
		int leftCandidate;
		int rightCandidate;

    	if (!solutionIsEdge) {
        	int[] t = computeCandidates(leftIndexes, rightIndexes);
        	leftCandidate = t[0];
        	rightCandidate = t[1];
    	}
    	else {
    		leftCandidate = leftIndexes.get(leftIndexes.size()-1);
    		rightCandidate = rightIndexes.get(0);
    	}

	    // Save the tangent as an edge, because no face could be computed
	    if (solutionIsEdge) {
	    	addIntoResult(new HullEdge3D<Point3f>(this.pointList,leftCandidate,rightCandidate,creationLevel));
	    }
	    else {
	    	createHullCylinder(
	    			leftIndexes, rightIndexes, leftCandidate, rightCandidate, creationLevel);
	    }
		
	    System.gc();
		
		return consumeLastInsertedIndexes();
	}
	
	/** Compute a plane that permits to detect candidate points.
	 */
	private void computeCandidatePlanes(Plane4f tangentPlane, Point3f leftCandidatePoint, Point3f rightCandidatePoint, boolean yOrder) {
		double dx, dy, dz;
		
		if (yOrder) {
			dy = 0;
			dz = 1;
		}
		else {
			dy = 1;
			dz = 0;
		}
		
    	Plane4f tangentCandidate = new Plane4f(
	    		leftCandidatePoint.getX(),leftCandidatePoint.getY(),leftCandidatePoint.getZ(),
	    		rightCandidatePoint.getX(),rightCandidatePoint.getY(),rightCandidatePoint.getZ(),
	    		leftCandidatePoint.getX(),leftCandidatePoint.getY()+dy,leftCandidatePoint.getZ()+dz);
    	boolean valid = tangentCandidate.isValid();
    	
	    if (!valid) {
			if (yOrder) {
				dx = 1;
				dz = 0;
			}
			else {
				dx = 0;
				dz = 1;
			}
	    	tangentCandidate = new Plane4f(
		    		leftCandidatePoint.getX(),leftCandidatePoint.getY(),leftCandidatePoint.getZ(),
		    		rightCandidatePoint.getX(),rightCandidatePoint.getY(),rightCandidatePoint.getZ(),
		    		leftCandidatePoint.getX()+dx,leftCandidatePoint.getY(),leftCandidatePoint.getZ()+dz);
	    	valid = tangentCandidate.isValid();
	    }
	    
	    if (valid) {
			if (yOrder) {
				dx = 0;
				dy = 1;
				dz = 0;
			}
			else {
				dx = 0;
				dy = 0;
				dz = 1;
			}
			
			double dot = MathUtil.dotProduct(dx, dy, dz, tangentCandidate.getEquationComponentA(), tangentCandidate.getEquationComponentB(), tangentCandidate.getEquationComponentC());
	    	
	    	if (dot==0) {
	    		// Because the two vectors are perpendicular,
	    		// compute the determinant of the vectors to
	    		// determine on which side the vector lies.
	    		//
	    		// The determinant is equals to 1 or -1 because
	    		// the vector are normalized
	        	dot = MathUtil.determinant(tangentCandidate.getEquationComponentA(), tangentCandidate.getEquationComponentB(), tangentCandidate.getEquationComponentC(), dx, dy, dz);
	    	}
	    	
	    	if (dot<0) {
	    		tangentCandidate.negate();
	    	}
    		tangentPlane.set(tangentCandidate);
    	}
	}

	/** Compute the left and right candidates.
	 * 
	 * @param pointList is the list of the points
	 * @param leftIndexes is the list of point's indexes for the left part
	 * @param rightIndexes is the list of point's indexes for the right part
	 * @return an array contains the left candidate and the right candidate
	 */
	private int[] computeCandidates(List<Integer> leftIndexes, List<Integer> rightIndexes) {
		int leftCandidate = leftIndexes.get(leftIndexes.size()-1);
		int rightCandidate = rightIndexes.get(0);

		Point3f leftCandidatePoint = this.pointList[leftCandidate];
		Point3f rightCandidatePoint = this.pointList[rightCandidate];

    	boolean allCoincident;
    	boolean yOrder = true;
	    boolean changed; // has the tangent changed?
	    Plane4f tangentCandidate = new Plane4f();
	    computeCandidatePlanes(tangentCandidate, leftCandidatePoint, rightCandidatePoint, yOrder);
	    
	    do {
	    	allCoincident = true;
	    	
	    	// Search for a point which is in the other side
	    	// of the tangent plane
	    	int lastCandidate = leftCandidate;
	    	for(int idxIdx=leftIndexes.size()-1; idxIdx>=0; --idxIdx) {
	    		int candidate = leftIndexes.get(idxIdx); 
	    		if (candidate!=leftCandidate) {
	    			PlaneClassification classification = tangentCandidate.classifies(this.pointList[candidate]);
	    			switch(classification) {
	    			case BEHIND:
		    			leftCandidate = candidate;
		    	    	leftCandidatePoint = this.pointList[leftCandidate];
		    	    	allCoincident = false;
		    	    	computeCandidatePlanes(tangentCandidate, leftCandidatePoint, rightCandidatePoint, yOrder);
		    	    	break;
	    			case COINCIDENT:
	    				break;
	    			default:
	    				// Ignore point
		    	    	allCoincident = false;
		    		}
	    		}
	    	}
	    	changed = (lastCandidate!=leftCandidate);
	    	
	    	// Search for a point which is in the other side
	    	// of the tangent plane
	    	lastCandidate = rightCandidate;
	    	for(int idxIdx=0; idxIdx<rightIndexes.size(); ++idxIdx) {
	    		int candidate = rightIndexes.get(idxIdx); 
	    		if (candidate!=rightCandidate) {
	    			PlaneClassification classification = tangentCandidate.classifies(this.pointList[candidate]);
	    			switch(classification) {
	    			case BEHIND:
		    			rightCandidate = candidate;
		    	    	rightCandidatePoint = this.pointList[rightCandidate];
		    	    	allCoincident = false;
		    	    	computeCandidatePlanes(tangentCandidate, leftCandidatePoint, rightCandidatePoint, yOrder);
		    	    	break;
	    			case COINCIDENT:
	    				break;
	    			default:
	    				// Ignore point
		    	    	allCoincident = false;
		    		}
	    		}
	    	}
	    	changed |= (lastCandidate!=rightCandidate);

	    	if ((yOrder)&&(allCoincident)) {
	    		// Special case: all the points are in the plane c.y+d=0
	    		changed = true;
	    		yOrder = !yOrder;
	    	    computeCandidatePlanes(tangentCandidate, leftCandidatePoint, rightCandidatePoint, yOrder);
	    	}

	    }
	    while (changed);
	    
	    return new int[] {leftCandidate,rightCandidate};
	}
	
	/** Create the hull cylinder that connect the two convex sub-hull.
	 * 
	 * @param leftIndexes is the point's indexes for the left part.
	 * @param rightIndexes is the point's indexes for the left part.
	 * @param leftCandidate is the candidate point in the left part.
	 * @param rightCandidate is the candidate point in the right part.
	 * @param creationLevel is the current creation level
	 */
	private void createHullCylinder(List<Integer> leftIndexes, List<Integer> rightIndexes, int leftCandidate, int rightCandidate, int creationLevel) {
		final int minIndex = leftIndexes.get(0);
		final int maxIndex = rightIndexes.get(rightIndexes.size()-1);
		//
		// STEP 4: find faces that connect
	    //         the two small hulls
	    //
		int l = leftCandidate;
		int r = rightCandidate;
		
	    do {
	    	int cand = (l==leftIndexes.get(0)) ? 1 : 0;
	    	int idx = (cand<leftIndexes.size()) ? leftIndexes.get(cand) : rightIndexes.get(cand-leftIndexes.size());
	    	
	    	Point3f p1 = this.pointList[l];
	    	Point3f p2 = this.pointList[r];
	    	Point3f p3 = this.pointList[idx];

	    	Plane4f candh;
	    	
			if (triangleOverlaps(minIndex, maxIndex,l, idx, r)) {
				candh = new Plane4f(0,0,0,0);
			}
			else {
				candh = new Plane4f(
			    		p1.getX(),p1.getY(),p1.getZ(),
			    		p2.getX(),p2.getY(),p2.getZ(),
			    		p3.getX(),p3.getY(),p3.getZ());
			}

			for(int i=cand+1; i<(leftIndexes.size()+rightIndexes.size()); ++i) {
	    		int iidx = (i<leftIndexes.size()) ? leftIndexes.get(i) : rightIndexes.get(i-leftIndexes.size());
	    		if ((iidx!=l)&&(iidx!=r)) {
	    			boolean newPlane = true;
		    		boolean isValid = candh.isValid();
		    		if (isValid) {
		    			switch(candh.classifies(this.pointList[iidx])) {
		    			case IN_FRONT_OF:
		    			case COINCIDENT:
		    				newPlane = false;
		    				break;
		    			case BEHIND:
		    				break;
		    			}
		    		}
		    		else if ((!isValid)&&
	    					 (triangleOverlaps(minIndex, maxIndex,l, iidx, r))) {
		    			newPlane = false;
    				}
    				else {
    					newPlane = !isValid;
    				}
		    		
		    		if (newPlane) {
						cand = i;
						idx = iidx;
						p3 = this.pointList[idx];
						candh = new Plane4f(
								p1.getX(),p1.getY(),p1.getZ(),
								p2.getX(),p2.getY(),p2.getZ(),
								p3.getX(),p3.getY(),p3.getZ());
		    		}
	    		}
	    	}
	    	
	    	if (candh.isValid()) {
	    		// The normal of the triangle is oriented to the ouside
	    		// of the under-construction hull.
		    	addIntoResult(new HullTriangle3D<Point3f>(this.pointList,l,idx,r,creationLevel));
		    	addIntoResult(new HullTriangle3D<Point3f>(this.pointList,l,r,idx,creationLevel));
		    	
		    	if (cand<leftIndexes.size()) {
	    			l = idx;
		    	}
		    	else {
	    			r = idx;
		    	}
	    	}
	    	else {
	    		if (l!=leftCandidate)
	    			l = leftCandidate;
	    		else
	    			r = rightCandidate;
	    	}		    	
	    	
	    }
	    while ((leftCandidate!=l)||(r!=rightCandidate));

	    // Remove the redundant triangles which are directed
	    // to at least one of the other vertices of the hull.
	    deleteFaces(creationLevel, rightIndexes.get(0), maxIndex);
	    deleteFaces(creationLevel, minIndex, leftIndexes.get(leftIndexes.size()-1));
	}

	/** Create the faces in the basic cases: 0 to 4 vertices.
	 * 
	 * @return <code>true</code> if a default case was handled, otherwhise <code>false</code>.
	 */
	private boolean createFacesForBasicCases(final int creationLevel, int startIndex, int endIndex) {
		int pointCount = endIndex - startIndex + 1;
		if (pointCount<=4) {
			final int i0 = startIndex;
			final int i1 = startIndex+1;
			final int i2 = startIndex+2;
			final int i3 = startIndex+3;
			switch(pointCount) {
			case 4:
				addIntoResult(new HullTriangle3D<Point3f>(this.pointList,i1,i0,i2,creationLevel));
				addIntoResult(new HullTriangle3D<Point3f>(this.pointList,i1,i2,i3,creationLevel));
				addIntoResult(new HullTriangle3D<Point3f>(this.pointList,i0,i3,i2,creationLevel));
				addIntoResult(new HullTriangle3D<Point3f>(this.pointList,i0,i1,i3,creationLevel));
				return true;
			case 3:
				addIntoResult(new HullTriangle3D<Point3f>(this.pointList,i0,i1,i2,creationLevel));
				addIntoResult(new HullTriangle3D<Point3f>(this.pointList,i0,i2,i1,creationLevel));
				return true;
			case 2:
				addIntoResult(new HullEdge3D<Point3f>(this.pointList,i0,i1,creationLevel));
				return true;
			case 1:
				addIntoResult(new HullVertex3D<Point3f>(this.pointList,i0,creationLevel));
				return true;
			}
		}
		return false;
	}

	/** Mark as deleted faces that can see a point from the other hull.
	 * This function also remove all the 3D edges.
	 */
	private void deleteFaces(final int creationLevel, final int startIndex, final int endIndex) {
		//int deletionLevel = creationLevel+1;
		boolean remove;
		Iterator<HullObject<Point3f>> iterator = this.convexHull.iterator();
		while (iterator.hasNext()) {
			remove = false;
			HullObject<Point3f> obj = iterator.next();
			int insideCount = obj.indexesInRange(startIndex,endIndex);
			if (obj instanceof HullTriangle3D<?>) {
				// Test if the object has one point outside the index range
				// It means that the object was generated for another
				// hull part
				if (insideCount<=1) {
					HullTriangle3D<Point3f> classifier = (HullTriangle3D<Point3f>)obj;
					for (int j=startIndex; j<=endIndex; ++j) {
						PlaneClassification classification = classifier.classifies(this.pointList[j]); 
						if (classification==PlaneClassification.IN_FRONT_OF) {
							remove = true;
							break; // Exit from the for internal loop
						}
					}
				}
			}
			else if (insideCount==0) {
				/*if (obj.getCreationLevel()>deletionLevel)*/
				// Remove the edges that are too old
				remove = true;
			}
			
			// Do the removal action
			if (remove) {
				iterator.remove();
				int[] cindexes = obj.getObjectIndexes();
				for (int i : cindexes) {
					decrementIndexReference(i);
				}
			}
		}
	}
	
	/** Add an artefact into the result list.
	 */
	private void addIntoResult(HullObject<Point3f> obj) {
		this.convexHull.add(obj);
		for(int p : obj.getObjectIndexes()) {
			incrementIndexReference(p, 1);
		}
	}
	
	/** Increment the reference on an point's index.
	 */
	private void incrementIndexReference(int pointIndex, int inc) {
		DInteger nb = this.indexes.get(pointIndex);
		if (nb==null) {
			nb = new DInteger();
			this.indexes.put(pointIndex, nb);
		}
		nb.value += inc;
	}
	
	/** Decrement the reference on an point's index.
	 */
	private void decrementIndexReference(int pointIndex) {
		DInteger nb = this.indexes.get(pointIndex);
		if (nb==null) return;
		nb.value --;
		if (nb.value<=0)
			this.indexes.remove(pointIndex);
	}

	/** Extract the last inserted point indexes.
	 */
	private Map<Integer,DInteger> consumeLastInsertedIndexes() {
		Map<Integer,DInteger> old = this.indexes;
		this.indexes = new TreeMap<Integer,DInteger>();
		return old;
	}
	
	/** Replies if the specified triangle overlaps another existing triangle.
	 * <p>
	 * A triangle overlaps another one when they are coplanar and and intersection
	 * existing between their edges.
	 */
	private boolean triangleOverlaps(int minIndex, int maxIndex, int p1, int p2, int p3) {
		final HullTriangle3D<Point3f> triangle = new HullTriangle3D<Point3f>(this.pointList,p1,p2,p3, -1);
		
		for (HullObject<Point3f> object : this.convexHull) {
			if (object instanceof HullTriangle3D<?>) {
				HullTriangle3D<Point3f> t = (HullTriangle3D<Point3f>)object;
				if (t.hasSamePoints(triangle)) return true;

				if ((t.a>=minIndex)&&(t.a<=maxIndex)&&
					(t.b>=minIndex)&&(t.b<=maxIndex)&&
					(t.c>=minIndex)&&(t.c<=maxIndex)) {
					if ((t.isCoplanar(triangle))&&
						(Triangle3f.overlapsCoplanarTriangle(
								this.pointList[triangle.a],
								this.pointList[triangle.b],
								this.pointList[triangle.c], 
								this.pointList[t.a],
								this.pointList[t.b],
								this.pointList[t.c])))
						return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class DInteger extends Number implements Comparable<Integer> {

		private static final long serialVersionUID = 5407180795619273240L;

		/** value if this integer.
		 */
		public int value;

		/**
		 */
		public DInteger() {
			this.value = 0;
		}

		/** {@inheritDoc}
		 */
		@Override
		public int compareTo(Integer o) {
			return o.compareTo(this.value);
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public float floatValue() {
			return this.value;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public int intValue() {
			return this.value;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public long longValue() {
			return this.value;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public String toString() {
			return Integer.toString(this.value);
		}

		@Override
		public double doubleValue() {
			return this.value;
		}
	}

	@Override
	public Point3f[] computeConvexHull(
			Point3f... pointList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<HullObject<Point3f>> computeConvexHullTriangles(
			Point3f... pointList) {
		// TODO Auto-generated method stub
		return null;
	}
	
}