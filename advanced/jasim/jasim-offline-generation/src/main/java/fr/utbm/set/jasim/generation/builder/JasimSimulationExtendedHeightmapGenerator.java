/*
 * $Id$
 * 
 * Copyright (c) 2008-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.generation.builder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.vecmath.Vector2d;

import fr.utbm.set.math.MathConstants;

/**
 * Main class that enables to create heightmap from a ground mesh with force field computed
 * on the second and third color components.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class JasimSimulationExtendedHeightmapGenerator implements JasimSimulationHeightmapCreatorConstants {
	
	/** Maximal force weight (between 0 and 255 inclusive).
	 */
	public static final int MAX_FORCE_WEIGHT = 3;

	/** Size of an arrow
	 */
	public static final int ARROW_SIZE = 10;

	/** Does the borders are repulsives
	 */
	public static final boolean DEFAULT_BORDER_REPULSION = true;

	private final File sourceMap;
	private final File repulsionFieldMap;
	private final File repulsionFieldMapX;
	private final File repulsionFieldMapY;
	private final File repulsionFieldMapArrow;
	private final File mergedRepulsionFields;
	private final String defaultPictureType;
	private final int traversableTerrainHeight;
	private final int maxForceWeight;
	private boolean repulsiveBorders;
	
	/**
	 * @param terrainMap is the path to the picture to load.
	 * @param repulsionField is the path to the output repulsion field.
	 * @param repulsionFieldX is the path to the output repulsion field along X axis.
	 * @param repulsionFieldY is the path to the output repulsion field along Y axis.
	 * @param repulsionFieldArrow is the path to the output repulsion field with drawn arrows.
	 * @param mergedRepulsionFields is the path to the output repulsion fields.
	 */
	public JasimSimulationExtendedHeightmapGenerator(File terrainMap, File repulsionField, File repulsionFieldX, File repulsionFieldY, File repulsionFieldArrow, File mergedRepulsionFields) {
		this(terrainMap, repulsionField, repulsionFieldX, repulsionFieldY, repulsionFieldArrow, mergedRepulsionFields, DEFAULT_GROUND_ZERO+1, MAX_FORCE_WEIGHT, DEFAULT_HEIGHTMAP_TYPE, DEFAULT_BORDER_REPULSION);
	}

	/**
	 * @param terrainMap is the path to the picture to load.
	 * @param repulsionField is the path to the output repulsion field.
	 * @param repulsionFieldX is the path to the output repulsion field along X axis.
	 * @param repulsionFieldY is the path to the output repulsion field along X axis.
	 * @param repulsionFieldArrow is the path to the output repulsion field with drawn arrows.
	 * @param mergedRepulsionFields is the path to the output repulsion fields.
	 * @param traversableTerrainHeight is the height of the lowest traversable terrain area.
	 * @param maxForceWeight is the maximal weight of the forces.
	 * @param pictureType is the type of the picture ("png", "jpeg"...).
	 * @param repulsiveBorders indicates if the image borders are repulsive. 
	 */
	public JasimSimulationExtendedHeightmapGenerator(File terrainMap, File repulsionField, File repulsionFieldX, File repulsionFieldY, File repulsionFieldArrow, File mergedRepulsionFields, int traversableTerrainHeight, int maxForceWeight, String pictureType, boolean repulsiveBorders) {
		this.sourceMap = terrainMap;
		this.repulsionFieldMap = repulsionField;
		this.repulsionFieldMapX = repulsionFieldX;
		this.repulsionFieldMapY = repulsionFieldY;
		this.repulsionFieldMapArrow = repulsionFieldArrow;
		this.mergedRepulsionFields = mergedRepulsionFields;
		this.traversableTerrainHeight = traversableTerrainHeight;
		this.maxForceWeight = maxForceWeight;
		this.defaultPictureType = pictureType;
		this.repulsiveBorders = repulsiveBorders;
	}
	
	/** Extract mesh vertices.
	 * 
	 * @return success state
	 * @throws Exception
	 */
	public boolean generate() throws Exception {
		// Generate the default gray-scale picture
		RepulsionVector[][] field = readGrayScalePicture();
		computeField(field);
		saveFieldPicture(field);
		saveNormalizedPictureX(field);
		saveNormalizedPictureY(field);
		saveArrowField(field);
		return saveMergePicture(field);
	}
	
	private void computeRepulsionFieldForHole(RepulsionVector[][] field, int width, int height, int ox, int oy, int x, int y, int force) {
		LinkedList<FieldCell> availableCells = new LinkedList<FieldCell>();
		Collection<FieldCell> consumedCells = new TreeSet<FieldCell>();
		FieldCell cell, firstCell;
		int cx, cy;
		FieldCell newCell;

		int repulsionForce = force;
		
		firstCell = null;
		availableCells.add(new FieldCell(x,y,repulsionForce));
		
		while (!availableCells.isEmpty()) {
			cell = availableCells.removeFirst();
			consumedCells.add(cell);
			cx = cell.X;
			cy = cell.Y;
			repulsionForce = cell.FORCE;
			if (cx>=0 && cx<width && cy>=0 && cy<height && field[cx][cy]!=null && repulsionForce>0) {
				
				cell.REPULSION.set(cx-ox,cy-oy);
				cell.REPULSION.normalize();
				cell.REPULSION.scale(repulsionForce);
				
				if (firstCell==null || firstCell.REPULSION.angle(cell.REPULSION)<=MathConstants.DEMI_PI) {
				
					field[cx][cy].add(cell.REPULSION);
					field[cx][cy].setMaxForce(repulsionForce);
					
					--repulsionForce;
	
					if (repulsionForce>0) {
						for(int i=-1; i<=1; ++i) {
							for(int j=-1; j<=1; ++j) {
								if (i!=0 || j!=0) {
									newCell = new FieldCell(cx+i,cy+j,repulsionForce);
									if (!consumedCells.contains(newCell)) {
										availableCells.add(newCell);
									}
								}
							}
						}
					}
					
					if (firstCell==null) firstCell = cell;
					
				}
				
			}
		}
	}
	
	/** Find an replies the holes on the field.
	 * 
	 * @param field is the field to update.
	 * @return the holes
	 */
	private Collection<Hole> findHoles(Vector2d[][] field) {
		Collection<Hole> holes = new TreeSet<Hole>();
		int width = field.length;
		int height = field[0].length;
		Hole hole, nextHole;
		Iterator<Hole> iterator;

		System.out.print("Adding border holes..."); //$NON-NLS-1$
		Hole leftHole = new Hole(true);
		Hole rightHole = new Hole(true);
		Hole topHole = new Hole(true);
		Hole bottomHole = new Hole(true);
		for(int y=0; y<height; ++y) {
			leftHole.CELLS.add(new Cell(-1,y,true));
			rightHole.CELLS.add(new Cell(width,y,true));
		}
		for(int x=0; x<width; ++x) {
			topHole.CELLS.add(new Cell(x,-1,true));
			bottomHole.CELLS.add(new Cell(x,height,true));
		}
		holes.add(leftHole);
		holes.add(rightHole);
		holes.add(topHole);
		holes.add(bottomHole);
		System.out.println("done"); //$NON-NLS-1$

		System.out.println("Extracting holes..."); //$NON-NLS-1$
		for(int y=0; y<height; ++y) {
			for(int x=0; x<width; ++x) {
				if (field[x][y]==null) {
					hole = null;
					iterator = holes.iterator();
					while (hole==null && iterator.hasNext()) {
						nextHole = iterator.next();
						if (nextHole.isAddableToHole(x,y)) {
							hole = nextHole;
						}
					}
					if (hole==null) {
						hole = new Hole(false);
						holes.add(hole);
					}
					hole.CELLS.add(new Cell(x,y,true));
				}
			}
			System.out.println("Extracting holes from map line..."+(y+1)+"/"+height);  //$NON-NLS-1$//$NON-NLS-2$
		}
		
		System.out.println("Extracting holes...done"); //$NON-NLS-1$

		System.out.print("Clearing hole contents..."); //$NON-NLS-1$
		iterator = holes.iterator();
		while (iterator.hasNext()) {
			hole = iterator.next();
			hole.clearInnerCells();
		}
		System.out.println("done\n"); //$NON-NLS-1$

		return holes;
	}
	
	private static boolean isHole(Vector2d[][] field, int x, int y) {
		int width = field.length;
		int height = field[0].length;
		return x<0 || x>=width || y<0 || y>=height || field[x][y]==null;
	}
	
	/** Compute the repulsion field
	 * 
	 * @param field is the field to update.
	 */
	private boolean computeField(RepulsionVector[][] field) {
		boolean changed = false;
		int width = field.length;
		int height = field[0].length;
		
		// Find holes
		Collection<Hole> holes = findHoles(field);

		// Send repulsions from holes
		int holeCount = 0;
		int x, y;
		for(Hole hole : holes) {
			++holeCount;
			System.out.println("Computing repulsive field from hole..."+holeCount+"/"+holes.size());  //$NON-NLS-1$//$NON-NLS-2$
			if (!hole.BORDER_HOLE || this.repulsiveBorders) {
				for(Cell holeCell : hole.CELLS) {
					x = holeCell.X;
					y = holeCell.Y;
					if (!isHole(field,x+1,y)) {
						computeRepulsionFieldForHole(field, width, height, x, y, x+1, y, this.maxForceWeight);
					}
					if (!isHole(field,x-1,y)) {
						computeRepulsionFieldForHole(field, width, height, x, y, x-1, y, this.maxForceWeight);
					}
					if (!isHole(field,x,y+1)) {
						computeRepulsionFieldForHole(field, width, height, x, y, x, y+1, this.maxForceWeight);
					}
					if (!isHole(field,x,y-1)) {
						computeRepulsionFieldForHole(field, width, height, x, y, x, y-1, this.maxForceWeight);
					}
				}
			}
		}
		
		for(y=0; y<height; ++y) {
			System.out.println("Normalizing repulsive forces..."+(y+1)+"/"+height);  //$NON-NLS-1$//$NON-NLS-2$
			for(x=0; x<width; ++x) {
				if (field[x][y]!=null) {
					field[x][y].normalizeFieldForce();
				}
			}
		}		
		
		return changed;
	}
	
	/**
	 * Read the heighmap.
	 * 
	 * @return the heightmap
	 * @throws IOException
	 */
	private RepulsionVector[][] readGrayScalePicture() throws IOException {
		System.out.print("Reading heightmap..."); //$NON-NLS-1$
		BufferedImage image = ImageIO.read(this.sourceMap);
		RepulsionVector[][] field = new RepulsionVector[image.getWidth()][image.getHeight()];
		int rgba, logicalY;
		for(int x=0; x<image.getWidth(); ++x) {
			for(int y=0; y<image.getHeight(); ++y) {
				rgba = image.getRGB(x, y);
				Color col = new Color(rgba,false);
				logicalY = image.getHeight() - y - 1; // because a picture has a inverted Y axis than the simulation
				if (col.getRed()>=this.traversableTerrainHeight) {
					field[x][logicalY] = new RepulsionVector();
				}
				else {
					field[x][logicalY] = null;
				}
			}
		}
		System.out.println("done"); //$NON-NLS-1$
		return field;
	}
		
	/**
	 * Save the extended heighmap.
	 * 
	 * @param field
	 * @throws IOException
	 */
	private void saveFieldPicture(Vector2d[][] field) throws IOException {
		if (this.repulsionFieldMap!=null) {
			System.out.print("Saving repulsion field '"+this.repulsionFieldMap.getAbsolutePath()+"'..."); //$NON-NLS-1$ //$NON-NLS-2$
			BufferedImage image = new BufferedImage(field.length,field[0].length,BufferedImage.TYPE_INT_RGB);
			Vector2d v;
			int red, green, blue;
			int realY;
			int pictureHeight = field[0].length;
			
			red = 0;
			
			for(int ix=0; ix<field.length; ++ix) {
				for(int iy=0; iy<field[ix].length; ++iy) {
					v = field[ix][iy];
					
					if (v==null || (v.x==0 && v.y==0)) {
						green = blue = 0;
					}
					else {
						green = size2color(v.x); // X
						assert (green>0 && green<=255) : green;
						blue = size2color(v.y); // Y
						assert (blue>0 && blue<=255) : blue;
					}
					
					realY = pictureHeight - iy - 1; // because a picture has a inverted Y axis than the simulation
					image.setRGB(ix, realY, new Color(red,green,blue).getRGB());
				}
			}
			ImageIO.write(image, this.defaultPictureType, this.repulsionFieldMap);
			System.out.println("done"); //$NON-NLS-1$
		}
	}
	
	/**
	 * Save the field drawn with arrows.
	 * 
	 * @param field
	 * @throws IOException
	 */
	private void saveArrowField(Vector2d[][] field) throws IOException {
		saveArrowFieldTo(field, this.repulsionFieldMapArrow);
	}

	/**
	 * Save the field drawn with arrows.
	 * 
	 * @param field
	 * @throws IOException
	 */
	private void saveArrowFieldTo(Vector2d[][] field, File output) throws IOException {
		if (output!=null) {
			System.out.print("Saving repulsion field arrows in '"+output.getAbsolutePath()+"'..."); //$NON-NLS-1$ //$NON-NLS-2$
			BufferedImage image = new BufferedImage(field.length*ARROW_SIZE,field[0].length*ARROW_SIZE,BufferedImage.TYPE_INT_RGB);
			Vector2d v;
			int realY;
			int pictureWidth = field.length;
			int pictureHeight = field[0].length;
			
			Graphics2D g2d = (Graphics2D)image.getGraphics();
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
			
			for(int ix=0; ix<pictureWidth; ++ix) {
				for(int iy=0; iy<pictureHeight; ++iy) {
					v = field[ix][iy];
					if (v!=null && !(v.x==0. && v.y==0.)) {
						drawArrow(g2d,pictureHeight,v,ix,iy);
					}
				}
			}
			
			g2d.setColor(Color.LIGHT_GRAY);
			for(int ix=0; ix<=pictureWidth; ++ix) {
				g2d.drawLine(ix*ARROW_SIZE, 0, ix*ARROW_SIZE, image.getHeight());
			}
			for(int iy=0; iy<=pictureHeight; ++iy) {
				realY = pictureHeight - iy - 1; // because a picture has a inverted Y axis than the simulation
				g2d.drawLine(0, realY*ARROW_SIZE, image.getWidth(), realY*ARROW_SIZE);
			}
			
			ImageIO.write(image, this.defaultPictureType, output);
			System.out.println("done"); //$NON-NLS-1$
		}
	}
	
	private void drawArrow(Graphics2D g, int pictureHeight, Vector2d vect, double x, double y) {
		double demi = ARROW_SIZE/2.;
		double quart = ARROW_SIZE/4.;
		
		double xx = x*ARROW_SIZE + demi;
		double realY = pictureHeight - y; // because a picture has a inverted Y axis than the simulation
		realY = realY*ARROW_SIZE - demi;
		
		
		int red = Math.max(0, Math.min(255, (int)(vect.length()*255./this.maxForceWeight)));
		
		g.setColor(new Color(red,0,0));
		
		Vector2d v = new Vector2d(vect);
		v.normalize();
		v.scale(quart);

		int sx = (int)(xx - v.x); 
		int sy = (int)(realY + v.y); 
		int ex = (int)(xx + v.x); 
		int ey = (int)(realY - v.y); 
		
		g.drawLine(sx,sy,ex,ey);
		g.fillOval(sx-1,sy-1,3,3);
	}

	/**
	 * Save the extended heighmap.
	 * 
	 * @param field
	 * @throws IOException
	 */
	private boolean saveMergePicture(Vector2d[][] field) throws IOException {
		if (this.mergedRepulsionFields!=null) {
			System.out.print("Saving complete ground map '"+this.mergedRepulsionFields.getAbsolutePath()+"'..."); //$NON-NLS-1$ //$NON-NLS-2$
			BufferedImage image = ImageIO.read(this.sourceMap);
			Vector2d v;
			Color c;
			int red, green, blue;
			int realY;
			int pictureHeight = field[0].length;
			
			for(int ix=0; ix<field.length; ++ix) {
				for(int iy=0; iy<field[ix].length; ++iy) {
					v = field[ix][iy];
					realY = pictureHeight - iy - 1; // because a picture has a inverted Y axis than the simulation
					
					c = new Color(image.getRGB(ix, realY));
					red = c.getRed();
					
					if (v==null || (v.x==0 && v.y==0) || (red<this.traversableTerrainHeight)) {
						green = blue = 0;
					}
					else {
						green = size2color(v.x); // X
						assert (green>=0 && green<=255) : green;
						blue = size2color(v.y); // Y
						assert (blue>=0 && blue<=255) : blue;
					}
					
					image.setRGB(ix, realY, new Color(red,green,blue).getRGB());
				}
			}
			ImageIO.write(image, this.defaultPictureType, this.mergedRepulsionFields);
			System.out.println("done"); //$NON-NLS-1$
			return true;
		}
		
		return false;
	}

	/**
	 * Save the extended heighmap.
	 * 
	 * @param field
	 * @throws IOException
	 */
	private void saveNormalizedPictureX(Vector2d[][] field) throws IOException {
		if (this.repulsionFieldMapX!=null) {
			System.out.print("Saving normalized repulsion field '"+this.repulsionFieldMapX.getAbsolutePath()+"'..."); //$NON-NLS-1$ //$NON-NLS-2$
			BufferedImage image = ImageIO.read(this.sourceMap);
			Vector2d v;
			int red, green, blue;
			int realY;
			int pictureHeight = field[0].length;
			
			for(int ix=0; ix<field.length; ++ix) {
				for(int iy=0; iy<field[ix].length; ++iy) {
					v = field[ix][iy];
					
					red = green = blue = 0;
					
					if (v!=null && (v.x!=0 || v.y!=0)) {
						if (v.x>0) blue = 255;
						else if (v.x<0) red = 255;
						else green = 255;
					}
					
					realY = pictureHeight - iy - 1; // because a picture has a inverted Y axis than the simulation
					image.setRGB(ix, realY, new Color(red,green,blue).getRGB());
				}
			}
			ImageIO.write(image, this.defaultPictureType, this.repulsionFieldMapX);
			System.out.println("done"); //$NON-NLS-1$
		}
		
	}

	/**
	 * Save the extended heighmap.
	 * 
	 * @param field
	 * @throws IOException
	 */
	private void saveNormalizedPictureY(Vector2d[][] field) throws IOException {
		if (this.repulsionFieldMapY!=null) {
			System.out.print("Saving normalized repulsion field '"+this.repulsionFieldMapY.getAbsolutePath()+"'..."); //$NON-NLS-1$ //$NON-NLS-2$
			BufferedImage image = ImageIO.read(this.sourceMap);
			Vector2d v;
			int red, green, blue;
			int realY;
			int pictureHeight = field[0].length;
			
			for(int ix=0; ix<field.length; ++ix) {
				for(int iy=0; iy<field[ix].length; ++iy) {
					v = field[ix][iy];
					
					red = green = blue = 0;
					
					if (v!=null && (v.x!=0 || v.y!=0)) {
						if (v.y>0) blue = 255;
						else if (v.y<0) red = 255;
						else green = 255;
					}
					
					realY = pictureHeight - iy - 1; // because a picture has a inverted Y axis than the simulation
					image.setRGB(ix, realY, new Color(red,green,blue).getRGB());
				}
			}
			ImageIO.write(image, this.defaultPictureType, this.repulsionFieldMapY);
			System.out.println("done"); //$NON-NLS-1$
		}
	}

	private int size2color(double v) {
		if (this.maxForceWeight>0.) {
			int c = (int)Math.round((v * 127.) / this.maxForceWeight);
			// C is in [-127..127]
			c += 128; // to be sure that 0 is not inside the valid values
			// ensure in [1..255]
			if (c<=0) c = 1;
			else if (c>255) c = 255;
			return c;
		}
		return 0;
	}

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class Cell implements Comparable<Cell> {
		/**
		 */
		public final int X;
		/**
		 */
		public final int Y;
		/**
		 */
		public final boolean IS_HOLE;
		
		/**
		 * @param x
		 * @param y
		 * @param isHole
		 */
		public Cell(int x, int y, boolean isHole) {
			this.X = x;
			this.Y = y;
			this.IS_HOLE = isHole;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuffer b = new StringBuffer();
			b.append("{x="); //$NON-NLS-1$
			b.append(this.X);
			b.append("; y="); //$NON-NLS-1$
			b.append(this.Y);
			b.append(")}"); //$NON-NLS-1$
			return b.toString();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object o) {
			if (o instanceof Cell) {
				return compareTo((Cell)o)==0;
			}
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			final int PRIME = 31;
	        int result = 1;
	        result = PRIME * result + this.X;
	        result = PRIME * result + this.Y;
	        return result;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(Cell o) {
			int cmp = this.X - o.X;
			if (cmp==0) {
				cmp = this.Y - o.Y;
			}
			return cmp;
		}
		
		/** Replies if the given cell is a direct neightbour of
		 * this cell.
		 * 
		 * @param x
		 * @param y
		 * @return <code>true</code> or <code>false</code>
		 */
		public boolean isNeightbour(int x, int y) {
			return (Math.abs(x-this.X)<=1) && (Math.abs(y-this.Y)<=1);
		}

	}
	
	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class FieldCell extends Cell {
		/**
		 */
		public final int FORCE;
		/**
		 */
		public final Vector2d REPULSION = new Vector2d();
		
		/**
		 * @param x
		 * @param y
		 * @param force
		 */
		public FieldCell(int x, int y, int force) {
			super(x, y, false);
			this.FORCE = force;
		}
		
		/**
		 * @param x
		 * @param y
		 * @param isHole
		 */
		public FieldCell(int x, int y, boolean isHole) {
			super(x, y, isHole);
			this.FORCE = 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			if (this.IS_HOLE) {
				return super.toString();
			}
			StringBuffer b = new StringBuffer();
			b.append("{x="); //$NON-NLS-1$
			b.append(this.X);
			b.append("; y="); //$NON-NLS-1$
			b.append(this.Y);
			b.append("; F="); //$NON-NLS-1$
			b.append(this.FORCE);
			b.append("; R=("); //$NON-NLS-1$
			b.append(this.REPULSION.x);
			b.append(","); //$NON-NLS-1$
			b.append(this.REPULSION.y);
			b.append(")}"); //$NON-NLS-1$
			return b.toString();
		}

	}

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class Hole implements Comparable<Hole> {

		public final Collection<Cell> CELLS = new TreeSet<Cell>();
		public final boolean BORDER_HOLE;
		
		public Hole(boolean border) {
			this.BORDER_HOLE = border;
		}
		
		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("{"); //$NON-NLS-1$
			buffer.append(this.CELLS.size());
			buffer.append(";"); //$NON-NLS-1$
			buffer.append(this.BORDER_HOLE);
			buffer.append("}"); //$NON-NLS-1$
			return buffer.toString();
		}
	
		public boolean isAddableToHole(int x, int y) {
			for(Cell holeCell : this.CELLS) {
				if (holeCell.isNeightbour(x,y)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public int compareTo(Hole o) {
			return o.hashCode() - hashCode();
		}
		
		public void clearInnerCells() {
			Iterator<Cell> iterator = this.CELLS.iterator();
			Cell cell;
			while (iterator.hasNext()) {
				cell = iterator.next();
				if ((this.CELLS.contains(new Cell(cell.X,cell.Y-1,true)))
					&&
					(this.CELLS.contains(new Cell(cell.X-1,cell.Y,true)))
					&&
					(this.CELLS.contains(new Cell(cell.X+1,cell.Y,true)))
					&&
					(this.CELLS.contains(new Cell(cell.X,cell.Y+1,true)))) {
					iterator.remove();
				}
			}
		}

	}

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class RepulsionVector extends Vector2d {

		private static final long serialVersionUID = -5590603704485047133L;
		
		public int maxForce = 0;

		/**
		 */
		public RepulsionVector() {
			super();
		}
		
		/**
		 * @param force
		 */
		public void setMaxForce(int force) {
			if (force>this.maxForce)
				this.maxForce = force;
		}
		
		public void normalizeFieldForce() {
			normalize();
			scale(this.maxForce);
		}
		
	}
	
}