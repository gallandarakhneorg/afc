/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.environment.model.ground;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;

import fr.utbm.set.jasim.environment.model.ground.PerceivableGround;
import fr.utbm.set.jasim.environment.semantics.GroundType;

/** This class representes a ground stored as a height map and that is providing repulsion field.
 * <p>
 * A height map is a matrix of integers between -128 and 127.
 * The matrix discretizes the ground area into regular cells.
 * The height of a point (x,y) is computed with a bilinear interpolation
 * with the four corners of the cell in which (x,y) lies.
 * <p>
 * A point is traversable if its coordinates (x,y) are on the ground
 * (ie, between the min and max values given at the constructor) AND
 * if the cell of (x,y) is traversable.
 * A cell is traversable if one of the corner as a height greater or equals
 * to the ground zero level (given as constructor's parameter).
 * <p>
 * A repulsion field is composed of repulsion 2D vector that could be used
 * by entities to know where to go.  
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class RepulsionHeightmapGround extends AbstractHeightmapGround implements PerceivableGround {

	/** matrix of heights.
	 */
	private final int[][] data;
	
	/** Size of a cell on X.
	 */
	private final double cellSizeX;

	/** Size of a cell on Y.
	 */
	private final double cellSizeY;
	
	/** Translation factor which permits to convert
	 * a byte height into a double height.
	 */
	private final double int2double;

	/** Width of the picture.
	 */
	private final int width;

	/** Height of the picture.
	 */
	private final int height;

	/** Maximal weight of the repulsive forces
	 */
	private final double maxForceWeight;

	/**
	 * Load a heightmap-based ground from the given file.
	 * 
	 * @param picture is the picture that contains the heightmap
	 * @param id is the identifier of the created ground
	 * @param min is the lower point coordinates of the bounding box of the ground
	 * @param max is the upper point coordinates of the bounding box of the ground
	 * @param groundZero is the height in the given heightmap that is corresponds to the height zero. 
	 * @throws IOException
	 */
	public RepulsionHeightmapGround(URL picture, UUID id, Point3d min, Point3d max, byte groundZero) throws IOException {
		this(id,
				min.x, min.y, min.z, max.x, max.y, max.z,
				groundZero,
				readRepulsiveHeightMapPicture(picture, id).data);
	}

	/**
	 * Load a heightmap-based ground from the given file.
	 * 
	 * @param picture is the picture that contains the heightmap
	 * @param id is the identifier of the created ground
	 * @param min is the lower point coordinates of the bounding box of the ground
	 * @param max is the upper point coordinates of the bounding box of the ground
	 * @param groundZero is the height below which the ground is not traversable.
	 * @throws IOException
	 */
	public RepulsionHeightmapGround(String picture, UUID id, Point3d min, Point3d max, double groundZero) throws IOException {
		this(id,
				min, max, groundZero,
				readRepulsiveHeightMapPicture(picture, id));
	}

	private RepulsionHeightmapGround(UUID id, Point3d min, Point3d max, double groundZero, ThreeComponentDescription desc) {
		this(id,
				min.x, min.y, min.z, max.x, max.y, max.z,
				(byte)(((groundZero-min.z)*desc.maxHeight/(max.z-min.z))-128),
				desc.data);
	}

	/**
	 * @param id is the identifier of this ground.
	 * @param minX is the lowest X-coordinate of the map. 
	 * @param minY is the lowest Y-coordinate of the map. 
	 * @param minZ is the lowest height of the map (it corresponds to <code>-128</code> in the height matrix). 
	 * @param maxX is the highest X-coordinate of the map. 
	 * @param maxY is the highest Y-coordinate of the map. 
	 * @param maxZ is the highest height of the map (it corresponds to <code>128</code> in the height matrix).
	 * @param groundZero is the height in the given heightmap that is corresponds to the height zero (between <code>-128</code>
	 * and <code>127</code>). 
	 * @param data is the list of heights and repulsive heights
	 */
	public RepulsionHeightmapGround(UUID id, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, byte groundZero, int[][] data) {
		this(id, minX, minY, minZ, maxX, maxY, maxZ, groundZero, data, null, Double.NaN);
	}
		
	/**
	 * @param id is the identifier of this ground.
	 * @param minX is the lowest X-coordinate of the map. 
	 * @param minY is the lowest Y-coordinate of the map. 
	 * @param minZ is the lowest height of the map (it corresponds to <code>-128</code> in the height matrix). 
	 * @param maxX is the highest X-coordinate of the map. 
	 * @param maxY is the highest Y-coordinate of the map. 
	 * @param maxZ is the highest height of the map (it corresponds to <code>128</code> in the height matrix). 
	 * @param groundZero is the height in the given heightmap that is corresponds to the height zero. 
	 * @param heights is the list of heights
	 * @param defaultSemantic is the default semantic associated to the ground.
	 */
	public RepulsionHeightmapGround(UUID id, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, byte groundZero, int[][] heights, GroundType defaultSemantic) {
		this(id, minX, minY, minZ, maxX, maxY, maxZ, groundZero, heights, defaultSemantic, Double.NaN);
	}

	/**
	 * @param id is the identifier of this ground.
	 * @param minX is the lowest X-coordinate of the map. 
	 * @param minY is the lowest Y-coordinate of the map. 
	 * @param minZ is the lowest height of the map (it corresponds to <code>-128</code> in the height matrix). 
	 * @param maxX is the highest X-coordinate of the map. 
	 * @param maxY is the highest Y-coordinate of the map. 
	 * @param maxZ is the highest height of the map (it corresponds to <code>128</code> in the height matrix). 
	 * @param groundZero is the height in the given heightmap that is corresponds to the height zero. 
	 * @param heights is the list of heights
	 * @param defaultSemantic is the default semantic associated to the ground.
	 * @param maxForceWeight is the maximal weight of the repulsive forces.
	 */
	public RepulsionHeightmapGround(UUID id, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, byte groundZero, int[][] heights, GroundType defaultSemantic, double maxForceWeight) {
		super(id, minX, minY, minZ, maxX, maxY, maxZ, groundZero, defaultSemantic);
		
		this.width = heights.length;
		this.height = heights[0].length;
		
		Byte maxHeight = null;
		Byte minHeight = null;
		byte h;
		for(int i=0; i<this.width; ++i) {
			for(int j=0; j<this.height; ++j) {
				h = getSignedHeight(heights[i][j]);
				if (maxHeight==null || maxHeight<h) maxHeight = h;
				if (minHeight==null || minHeight>h) minHeight = h;
			}
		}
		
		int smallestHeight = minHeight==null ? 0 : minHeight+128;
		int highestHeight = maxHeight==null ? 0 : maxHeight+128;
		
		int rawHeightSize = highestHeight-smallestHeight;
		
		this.data = heights;
		this.cellSizeX = getSizeX() / this.width;
		this.cellSizeY = getSizeY() / this.height;
		this.int2double = getSizeZ() / rawHeightSize;
		
		if (Double.isNaN(maxForceWeight)) {
			this.maxForceWeight = 5. * Math.sqrt(this.cellSizeX*this.cellSizeX+this.cellSizeY*this.cellSizeY);
		}
		else {
			assert(maxForceWeight>=0.);
			this.maxForceWeight = maxForceWeight;
		}
	}
	
	/** Replies the height stored inside the given data.
	 *
	 * @param rawData
	 * @return the height stored inside the given data.
	 */
	protected static int getUnsignedHeight(int rawData) {
		return ((rawData & 0xFF0000) >> 16);
	}
	
	/** Replies the height stored inside the given data.
	 *
	 * @param rawData
	 * @return the height stored inside the given data.
	 */
	protected static byte getSignedHeight(int rawData) {
		return (byte)(((rawData & 0xFF0000) >> 16) - 128);
	}
	
	private double color2size(int color) {
		// The color is in [1..255], zero is reserved for special cases.
		// But because the picture heightmap could be wrong generated,
		// we assume 0 as valid value:
		// 					color -= 128;
		// Now the color is in [-127..127] or equal to -128 in case of original 0.
		return ((color-128) * this.maxForceWeight) / 127.;
	}

	/** Replies the the repulsion vector.
	 *
	 * @param rawData is the pixel color (3 components).
	 * @param result is the repulsion vector.
	 */
	protected final void computeRepulsion(int rawData, Vector2d result) {
		int greenColor = (rawData >> 8) & 0xFF;
		int blueColor = rawData & 0xFF;
		if (greenColor==0 && blueColor==0) { 
			// special case where no repulsion was given for this point.
			result.set(0,0);
		}
		else {
			result.set(color2size(greenColor), color2size(blueColor));
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getHeightAt(double x, double y) {
		double mx = getMinX();
		double my = getMinY();
		double ax = getMaxX();
		double ay = getMaxY();
		if (x>=mx && y>=my && x<=ax && y<=ay) {
			double ix = ((x - mx)*(this.width-1)) / getSizeX();
			double iy = ((y - my)*(this.height-1)) / getSizeY();

			int lx = (int)Math.floor(ix);
			int ly = (int)Math.floor(iy);
			int ux = (int)Math.ceil(ix);
			int uy = (int)Math.ceil(iy);
			
			return bilinearInterpolation(x, y, lx, ly, ux, uy);
		}
		return Double.NaN;
	}
	
	/**
	 * @return the estimated height.
	 */
	private double bilinearInterpolation(double x, double y, int lx, int ly, int ux, int uy) {
		double mx = getMinX();
		double my = getMinY();
		double mz = getMinZ();
		
		int q11 = getUnsignedHeight(this.data[lx][ly]);
		int q22 = getUnsignedHeight(this.data[ux][uy]);
		int q12 = getUnsignedHeight(this.data[lx][uy]);
		int q21 = getUnsignedHeight(this.data[ux][ly]);
		
		int gz = getGroundZero() + 128;

		if (q11>=gz && q12>=gz && q21>=gz && q22>=gz) {
			double minx = mx+(lx*this.cellSizeX);
			double maxx = minx + this.cellSizeX;

			double miny = my+(ly*this.cellSizeY);
			double maxy = miny + this.cellSizeY;

			double fq11 = q11*this.int2double + mz;
			double fq22 = q22*this.int2double + mz;
			double fq12 = q12*this.int2double + mz;
			double fq21 = q21*this.int2double + mz;
			
			double denom = this.cellSizeX*this.cellSizeY;
			
			double dx1 = x - minx;
			double dx2 = maxx - x;
			double dy1 = y - miny;
			double dy2 = maxy - y;
			
			return fq11/denom * dx2*dy2
				+  fq21/denom * dx1*dy2
				+  fq12/denom * dx2*dy1
				+  fq22/denom * dx1*dy1; 
		}
		
		return Double.NaN;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isTraversable(double x, double y) {
		double mx = getMinX();
		double my = getMinY();
		double ax = getMaxX();
		double ay = getMaxY();
		if (x>=mx && y>=my && x<=ax && y<=ay) {
			double ix = ((x - mx)*(this.width-1)) / getSizeX();
			double iy = ((y - my)*(this.height-1)) / getSizeY();

			int lx = (int)Math.floor(ix);
			int ly = (int)Math.floor(iy);
			int ux = (int)Math.ceil(ix);
			int uy = (int)Math.ceil(iy);
			
			int h1 = getUnsignedHeight(this.data[lx][ly]);
			int h2 = getUnsignedHeight(this.data[ux][uy]);
			int h3 = getUnsignedHeight(this.data[lx][uy]);
			int h4 = getUnsignedHeight(this.data[ux][ly]);
			
			int gz = getGroundZero() + 128;

			if (h1>=gz && h2>=gz && h3>=gz && h4>=gz) {
				return true;
			}
			
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double getMaxForceWeight() {
		return this.maxForceWeight;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Vector2d getRepulsion(double x, double y) {
		Vector2d vect = new Vector2d();
		
		double mx = getMinX();
		double my = getMinY();
		double ax = getMaxX();
		double ay = getMaxY();
		
		if (x>=mx && y>=my && x<=ax && y<=ay) {
			double ix = ((x - mx)*(this.width-1)) / getSizeX();
			double iy = ((y - my)*(this.height-1)) / getSizeY();

			int lx = (int)Math.floor(ix);
			int ly = (int)Math.floor(iy);
			
			computeRepulsion(this.data[lx][ly], vect);
		}
		
		return vect;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Vector2d getAttraction(double x, double y) {
		Vector2d vect = getRepulsion(x, y);
		vect.negate();
		return vect;
	}

}