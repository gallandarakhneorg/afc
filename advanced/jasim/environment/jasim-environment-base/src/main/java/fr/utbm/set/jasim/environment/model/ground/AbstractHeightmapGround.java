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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import javax.imageio.ImageIO;

import fr.utbm.set.io.filefilter.ImageFileFilter;
import fr.utbm.set.io.heightmap.HeightMapImageReader;
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
public abstract class AbstractHeightmapGround extends AbstractGround implements HeightmapGround {
	
	/**
	 * Load a heightmap-based ground from the given file.
	 * 
	 * @param picture is the picture that contains the heightmap
	 * @param id is the identifier of the created ground
	 * @return a new ground
	 * @throws IOException
	 */
	public static OneComponentDescription readHeightMapPicture(String picture, UUID id) throws IOException {
		try {
			return readHeightMapPicture(new URL(picture), id);
		}
		catch(Exception _) {
			//
		}
		try {
			File file = new File(picture); 
			if (file.exists()) {
				return readHeightMapPicture(file, id);
			}
		}
		catch(Exception _) {
			//
		}
		throw new IOException("Invalid resource type for ground: only image is allowed"); //$NON-NLS-1$
	}

	/**
	 * Load a heightmap-based ground from the given file.
	 * 
	 * @param picture is the picture that contains the heightmap
	 * @param id is the identifier of the created ground
	 * @return a new ground
	 * @throws IOException
	 */
	public static OneComponentDescription readHeightMapPicture(URL picture, UUID id) throws IOException {
		if (ImageFileFilter.isImageFile(picture)) {
			try {
				byte minHeight, maxHeight;
				byte[][] heights;
				{
					HeightMapImageReader reader = new HeightMapImageReader(ImageIO.read(picture));
					heights = reader.readHeightmap();
					assert(heights!=null);
					minHeight = reader.getLowerHeightByte();
					maxHeight = reader.getUpperHeightByte();
				}
				
				return new OneComponentDescription(heights, minHeight, maxHeight);
			}
			catch (IllegalArgumentException e) {
				throw new IOException(e);
			}
			catch (SecurityException e) {
				throw new IOException(e);
			}
		}
		throw new IOException("Invalid resource type for ground: only image is allowed"); //$NON-NLS-1$
	}

	/**
	 * Load a heightmap-based ground from the given file.
	 * 
	 * @param picture is the picture that contains the heightmap
	 * @param id is the identifier of the created ground
	 * @return a new ground
	 * @throws IOException
	 */
	public static OneComponentDescription readHeightMapPicture(File picture, UUID id) throws IOException {
		if (ImageFileFilter.isImageFile(picture)) {
			try {
				byte minHeight, maxHeight;
				byte[][] heights;
				{
					HeightMapImageReader reader = new HeightMapImageReader(ImageIO.read(picture));
					heights = reader.readHeightmap();
					assert(heights!=null);
					minHeight = reader.getLowerHeightByte();
					maxHeight = reader.getUpperHeightByte();
				}
				
				return new OneComponentDescription(heights, minHeight, maxHeight);
			}
			catch (IllegalArgumentException e) {
				throw new IOException(e);
			}
			catch (SecurityException e) {
				throw new IOException(e);
			}
		}
		throw new IOException("Invalid resource type for ground: only image is allowed"); //$NON-NLS-1$
	}

	/**
	 * Load a heightmap-based ground with repulsion vectors from the given file.
	 * 
	 * @param picture is the picture that contains the heightmap
	 * @param id is the identifier of the created ground
	 * @return a new ground
	 * @throws IOException
	 */
	public static ThreeComponentDescription readRepulsiveHeightMapPicture(String picture, UUID id) throws IOException {
		try {
			return readRepulsiveHeightMapPicture(new URL(picture), id);
		}
		catch(Exception _) {
			//
		}
		try {
			File file = new File(picture); 
			if (file.exists()) {
				return readRepulsiveHeightMapPicture(file, id);
			}
		}
		catch(Exception _) {
			//
		}
		throw new IOException("Invalid resource type for ground: only image is allowed"); //$NON-NLS-1$
	}

	/**
	 * Load a heightmap-based ground with repulsion vectors from the given file.
	 * 
	 * @param picture is the picture that contains the heightmap
	 * @param id is the identifier of the created ground
	 * @return a new ground
	 * @throws IOException
	 */
	public static ThreeComponentDescription readRepulsiveHeightMapPicture(URL picture, UUID id) throws IOException {
		if (ImageFileFilter.isImageFile(picture)) {
			try {
				byte minHeight, maxHeight;
				int[][] data;
				{
					HeightMapImageReader reader = new HeightMapImageReader(ImageIO.read(picture));
					data = reader.readAllComponents();
					assert(data!=null);
					minHeight = reader.getLowerHeightByte();
					maxHeight = reader.getUpperHeightByte();
				}
				
				return new ThreeComponentDescription(data, minHeight, maxHeight);
			}
			catch (IllegalArgumentException e) {
				throw new IOException(e);
			}
			catch (SecurityException e) {
				throw new IOException(e);
			}
		}
		throw new IOException("Invalid resource type for ground: only image is allowed"); //$NON-NLS-1$
	}

	/**
	 * Load a heightmap-based ground with repulsion vectors from the given file.
	 * 
	 * @param picture is the picture that contains the heightmap
	 * @param id is the identifier of the created ground
	 * @return a new ground
	 * @throws IOException
	 */
	public static ThreeComponentDescription readRepulsiveHeightMapPicture(File picture, UUID id) throws IOException {
		if (ImageFileFilter.isImageFile(picture)) {
			try {
				byte minHeight , maxHeight;
				int[][] data;
				{
					HeightMapImageReader reader = new HeightMapImageReader(ImageIO.read(picture));
					data = reader.readAllComponents();
					assert(data!=null);
					minHeight = reader.getLowerHeightByte();
					maxHeight = reader.getUpperHeightByte();
				}
				
				return new ThreeComponentDescription(data, minHeight, maxHeight);
			}
			catch (IllegalArgumentException e) {
				throw new IOException(e);
			}
			catch (SecurityException e) {
				throw new IOException(e);
			}
		}
		throw new IOException("Invalid resource type for ground: only image is allowed"); //$NON-NLS-1$
	}

	/** Height of ground zero.
	 */
	private final byte groundZero; 

	/** Name of the image file that permits to create this ground object.
	 */
	private URL filename = null;
	
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
	 */
	public AbstractHeightmapGround(UUID id, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, byte groundZero) {
		this(id, minX, minY, minZ, maxX, maxY, maxZ, groundZero, null);
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
	 * @param defaultSemantic is the default semantic associated to the ground.
	 */
	public AbstractHeightmapGround(UUID id, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, byte groundZero, GroundType defaultSemantic) {
		super(id, minX, minY, minZ, maxX, maxY, maxZ, defaultSemantic);
		this.groundZero = groundZero;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public URL getImageFile() {
		return this.filename;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void setImageFile(URL filename) {
		this.filename = filename;
	}

	/** {@inheritDoc}
	 */
	@Override
	public byte getGroundZero() {
		return this.groundZero;
	}

	/** This class representes a ground stored as a height map.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: ngaud$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public final static class OneComponentDescription {

		/** Set of heights.
		 */
		public final byte[][] heights;
		
		/** Smallest height in the map.
		 */
		public final byte minHeight;

		/** Greatest height in the map.
		 */
		public final byte maxHeight;
		
		/**
		 * @param heights arethe heights
		 * @param minHeight is the minimal height in the heights
		 * @param maxHeight is the maximal height in the heights
		 */
		OneComponentDescription(byte[][] heights, byte minHeight, byte maxHeight) {
			this.heights = heights;
			this.minHeight = minHeight;
			this.maxHeight = maxHeight;
		}
		
	}
	
	/** This class representes a ground stored as a color components (red, green blue).
	 *
	 * @author $Author: sgalland$
	 * @author $Author: ngaud$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public final static class ThreeComponentDescription {

		/** Set of heights with associated repulsion vectors.
		 */
		public final int[][] data;
		
		/** Smallest height in the map.
		 */
		public final byte minHeight;

		/** Greatest height in the map.
		 */
		public final byte maxHeight;
		
		/**
		 * @param data are the heights and the repulsion vectors
		 * @param minHeight is the minimal height in the heights
		 * @param maxHeight is the maximal height in the heights
		 */
		ThreeComponentDescription(int[][] data, byte minHeight, byte maxHeight) {
			this.data = data;
			this.minHeight = minHeight;
			this.maxHeight = maxHeight;
		}
		
	}

}