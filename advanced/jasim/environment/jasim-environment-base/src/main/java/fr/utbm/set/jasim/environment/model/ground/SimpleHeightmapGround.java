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

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.jasim.environment.semantics.GroundType;

/** This class representes a ground stored as a height map.
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
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SimpleHeightmapGround extends AbstractHeightmapGround {

	/** matrix of heights.
	 */
	private final byte[][] heights;
	
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
	
	/** Height of the ground zero in spatial coordinate system.
	 */
	private final double groundZeroHeight;

	/**
	 * Load a heightmap-based ground from the given file.
	 * 
	 * @param picture is the picture that contains the heightmap
	 * @param id is the identifier of the created ground
	 * @param min is the lower point coordinates of the bounding box of the ground
	 * @param max is the upper point coordinates of the bounding box of the ground
	 * @param groundZero is the height in the given heightmap that is corresponds to the height zero, ie the first height which is traversable. 
	 * @throws IOException
	 */
	public SimpleHeightmapGround(URL picture, UUID id, Point3d min, Point3d max, double groundZero) throws IOException {
		this(id,
				min, max,
				groundZero,
				readHeightMapPicture(picture, id));
	}

	/**
	 * Load a heightmap-based ground from the given file.
	 * 
	 * @param picture is the picture that contains the heightmap
	 * @param id is the identifier of the created ground
	 * @param groundZeroColor is the height color in the given heightmap that is corresponds to the height zero, ie the first height which is traversable. 
	 * @param min is the lower point coordinates of the bounding box of the ground
	 * @param max is the upper point coordinates of the bounding box of the ground
	 * @throws IOException
	 */
	public SimpleHeightmapGround(URL picture, UUID id, byte groundZeroColor, Point3d min, Point3d max) throws IOException {
		this(id,
				groundZeroColor,
				min, max,
				readHeightMapPicture(picture, id));
	}

	private SimpleHeightmapGround(UUID id, Point3d min, Point3d max, double groundZero, OneComponentDescription desc) {
		this(id,
				min.x, min.y, min.z, max.x, max.y, max.z,
				(byte)(((groundZero-min.z)*desc.maxHeight/(max.z-min.z))-128),
				desc.heights,
				null);
	}

	private SimpleHeightmapGround(UUID id, byte groundZeroColor, Point3d min, Point3d max, OneComponentDescription desc) {
		this(id,
				min.x, min.y, min.z, max.x, max.y, max.z,
				groundZeroColor,
				desc.heights,
				null);
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
	 * @param heights is the list of heights
	 */
	public SimpleHeightmapGround(UUID id, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, byte groundZero, byte[][] heights) {
		this(id, minX, minY, minZ, maxX, maxY, maxZ, groundZero, heights, null);
	}
		
	/**
	 * @param id is the identifier of this ground.
	 * @param minX is the lowest X-coordinate of the map. 
	 * @param minY is the lowest Y-coordinate of the map. 
	 * @param minZ is the lowest height of the map (it corresponds to <code>-128</code> in the height matrix). 
	 * @param maxX is the highest X-coordinate of the map. 
	 * @param maxY is the highest Y-coordinate of the map. 
	 * @param maxZ is the highest height of the map (it corresponds to <code>127</code> in the height matrix). 
	 * @param groundZero is the height in the given heightmap that is corresponds to the height zero. 
	 * @param heights is the list of heights
	 * @param defaultSemantic is the default semantic associated to the ground.
	 */
	public SimpleHeightmapGround(UUID id, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, byte groundZero, byte[][] heights, GroundType defaultSemantic) {
		super(id, minX, minY, minZ, maxX, maxY, maxZ, groundZero, defaultSemantic);
		
		Byte maxHeight = null;
		Byte minHeight = null;
		for(int i=0; i<heights.length; ++i) {
			for(int j=0; j<heights[i].length; ++j) {
				if (maxHeight==null || maxHeight<heights[i][j])
					maxHeight = heights[i][j];
				if (minHeight==null || minHeight>heights[i][j])
					minHeight = heights[i][j];
			}
		}
		
		int smallestHeight = minHeight==null ? 0 : minHeight+128;
		int highestHeight = maxHeight==null ? 0 : maxHeight+128;
		
		int rawHeightSize = highestHeight-smallestHeight;
		
		this.width = heights.length;
		this.height = heights[0].length;
		this.heights = heights;
		this.cellSizeX = getSizeX() / this.width;
		this.cellSizeY = getSizeY() / this.height;
		this.int2double = getSizeZ() / rawHeightSize;
		this.groundZeroHeight = (groundZero+128) * this.int2double + minZ;
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
			double ix = (x - mx) / this.cellSizeX;
			double iy = (y - my) / this.cellSizeY;

			int lx = (int)Math.floor(ix);
			int ly = (int)Math.floor(iy);
			int ux = (int)Math.ceil(ix);
			int uy = (int)Math.ceil(iy);
			
			// lx and ly are assumed to have valid value
			// but ux and uy could be outside the matrix of heights
			assert(lx>=0 && lx<this.width && ly>=0 && ly<this.height);
			if (ux>=0 && ux<this.width && uy>=0 && uy<this.height) {
				return bilinearInterpolation(x, y, lx, ly, ux, uy);
			}
		}
		return Double.NaN;
	}
	
	/**
	 * @return the estimated height.
	 */
	private double bilinearInterpolation(double x, double y, int lx, int ly, int ux, int uy) {
		double z = Double.NaN;

		int q11 = this.heights[lx][ly]+128;
		int q22 = this.heights[ux][uy]+128;
		int q12 = this.heights[lx][uy]+128;
		int q21 = this.heights[ux][ly]+128;
		
		int gz = getGroundZero() + 128;
		
		if (q11>=gz && q12>=gz && q21>=gz && q22>=gz) {
			double mx = getMinX();
			double my = getMinY();
			double mz = getMinZ();
			
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
			
			z   =  fq11/denom * dx2*dy2
				+  fq21/denom * dx1*dy2
				+  fq12/denom * dx2*dy1
				+  fq22/denom * dx1*dy1;
			
			// Be sure that the interpolated value is still traversable
			assert(this.groundZeroHeight==gz*this.int2double + mz);
			if (GeometryUtil.epsilonCompareTo(z, this.groundZeroHeight)<0) z = Double.NaN;
		}
		
		return z;
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
			
			int h1 = this.heights[lx][ly]+128;
			int h2 = this.heights[ux][uy]+128;
			int h3 = this.heights[lx][uy]+128;
			int h4 = this.heights[ux][ly]+128;
			
			int gz = getGroundZero() + 128;

			if (h1>=gz && h2>=gz && h3>=gz && h4>=gz) {
				return true;
			}
			
		}
		return false;
	}

	/** Replies the value in the heightmap at the specified row and column.
	 * 
	 * @param row
	 * @param column
	 * @return the height value stored in the heightmap.
	 */
	public final byte getByteHeightValueAt(int row, int column) {
		return this.heights[column][row];
	}

	/** Replies the value in the heightmap at the specified row and column.
	 * 
	 * @param row
	 * @param column
	 * @return the height in user space coordinate system.
	 */
	public final double getDoubleHeightValueAt(int row, int column) {
		return (this.heights[column][row]+128)*this.int2double + getMinZ();
	}
	
	/** Replies the size of one cell along the X axis.
	 * 
	 * @return the cell size.
	 */
	public final double getCellSizeX() {
		return this.cellSizeX;
	}

	/** Replies the size of one cell along the Y axis.
	 * 
	 * @return the cell size.
	 */
	public final double getCellSizeY() {
		return this.cellSizeY;
	}

}