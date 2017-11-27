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

import fr.utbm.set.math.MathUtil;

/**
 * Graphic 2D utils
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GraphicUtil {

	/** Fill a triangle in the given bitmap. Based on Bresenham algorithm.
	 * 
	 * @param bitmap
	 * @param x1 is the coordinate of the first point to fire.
	 * @param y1 is the coordinate of the first point to fire.
	 * @param color1 is the color of the first pixel.
	 * @param x2 is the coordinate of the second point to fire.
	 * @param y2 is the coordinate of the second point to fire.
	 * @param color2 is the color of the first pixel.
	 * @param x3 is the coordinate of the third point to fire.
	 * @param y3 is the coordinate of the third point to fire.
	 * @param color3 is the color of the first pixel.
	 * @param setWhenHeigher indicates if the colors must be set only if they are higher
	 * than the colors already inside the bitmap.
	 * @return success state.
	 */
	public static boolean fillTriangle(float[][] bitmap,
							 int x1, int y1, float color1,
							 int x2, int y2, float color2,
							 int x3, int y3, float color3,
							 boolean setWhenHeigher) {
		int height = (int)MathUtil.max(Math.abs(y3-y1), Math.abs(y3-y2), Math.abs(y2-y1));
		
		if (height>0) {
			int miny = (int)MathUtil.min(y1, y2, y3);
			TriangleFillingLine[] triangleLines = new TriangleFillingLine[height];
			
			buildBresenhamLine(triangleLines, miny, x1, y1, x2, y2, color1, color2);
			buildBresenhamLine(triangleLines, miny, x2, y2, x3, y3, color2, color3);
			buildBresenhamLine(triangleLines, miny, x3, y3, x1, y1, color3, color1);
			
			fireTriangle(bitmap, triangleLines, miny, setWhenHeigher);
		}
		
		return true;
	}
	
	/**
	 */
	static int ppp = 0;
	
	private static void fireTriangle(float[][] bitmap, TriangleFillingLine[] triangleLines, int y, boolean setWhenHigher) {
		TriangleFillingLine line;
		float interpolatedColor;
		float colorDistance, minColor; 
		int distance;
		int yy = y;
		for(int i=0; i<triangleLines.length; ++i, ++yy) {
			line = triangleLines[i];
			assert(line!=null);
			distance = line.getMaxX() - line.getMinX();
			minColor = line.getMinColor();
			colorDistance = line.getMaxColor() - minColor;
			for(int x=line.getMinX(), j=0; x<=line.getMaxX(); ++x, ++j) {
				// not based on a color incrementation to avoid color approximation error
				interpolatedColor = ((j * colorDistance) / distance) + minColor;
				
				if (setWhenHigher)
					firePixelWhenHigher(bitmap, x, yy, interpolatedColor, 1);
				else
					firePixel(bitmap, x, yy, interpolatedColor, 1);
			}
		}
	}
	
	/** Bresenham's Line-Drawing Algorithm with color interpolation.
	 * 
	 * @param triangleLines
	 * @param miny
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param color1
	 * @param color2
	 */
	public static void buildBresenhamLine(TriangleFillingLine[] triangleLines, int miny, int x1, int y1, int x2, int y2, float color1, float color2) {      
		int d;
		int dx, dy;
		int stepX, stepY;
		boolean steep;

		steep = false;

		int xx1 = x1;
		int yy1 = y1;
		
		dx = Math.abs(x2-xx1);
		dy = Math.abs(y2-yy1);

		if((x2 - xx1) > 0) stepX = 1;
		else stepX = -1;

		if((y2 - yy1) > 0) stepY = 1;
		else stepY = -1;

		if(dy > dx) {
			steep = true;
			int myTemp;

			//--- swap x1, y1-----//
			myTemp = xx1;
			xx1 = yy1;
			yy1 = myTemp;

			//--- swap dx, dy----//
			myTemp = dx;
			dx = dy;
			dy = myTemp;      

			//--- swap stepX, stepY----//
			myTemp = stepX;
			stepX = stepY;
			stepY = myTemp;
		}

		d = 2*dy - dx;
		
		float interpolatedColor;
		float colorDistance = color2 - color1;
		
		for(int coord=0; coord<dx; ++coord) {
			// not based on a color incrementation to avoid color approximation error
			interpolatedColor = ((coord * colorDistance) / dx) + color1;
			
			if (steep) {
				// here y1 is pixel by pixel
				addToTriangleLine(triangleLines, miny, yy1, xx1, interpolatedColor);
			}
			else {
				// here x1 is pixel by pixel
				addToTriangleLine(triangleLines, miny, xx1, yy1, interpolatedColor);
			}
			
			while(d>=0) {
				yy1 = yy1 + stepY;
				d = d - (dx * 2);
			}

			xx1 = xx1 + stepX;
			d = d + (dy * 2);      
		}

		addToTriangleLine(triangleLines, miny, xx1, yy1, color2);
	}

	/** Bresenham's Line-Drawing Algorithm with color interpolation.
	 * 
	 * @param bitmap
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param color1
	 * @param color2
	 */
	public static void drawBresenhamLine(float[][] bitmap, int x1, int y1, int x2, int y2, float color1, float color2) {      
		int d;
		int dx, dy;
		int stepX, stepY;
		boolean steep;

		int xx1 = x1;
		int yy1 = y1;
		
		steep = false;

		dx = Math.abs(x2-xx1);
		dy = Math.abs(y2-yy1);

		if((x2 - xx1) > 0) stepX = 1;
		else stepX = -1;

		if((y2 - yy1) > 0) stepY = 1;
		else stepY = -1;

		if(dy > dx) {
			steep = true;
			int myTemp;

			//--- swap x1, y1-----//
			myTemp = xx1;
			xx1 = yy1;
			yy1 = myTemp;

			//--- swap dx, dy----//
			myTemp = dx;
			dx = dy;
			dy = myTemp;      

			//--- swap stepX, stepY----//
			myTemp = stepX;
			stepX = stepY;
			stepY = myTemp;
		}

		d = 2*dy - dx;
		
		float interpolatedColor;
		float colorDistance = color2 - color1;
		
		for(int coord=0; coord<dx; ++coord) {
			// not based on a color incrementation to avoid color approximation error
			interpolatedColor = ((coord * colorDistance) / dx) + color1;
			
			if (steep) {
				assert(firePixel(bitmap, yy1, xx1, interpolatedColor));
			}
			else {
				assert(firePixel(bitmap, xx1, yy1, interpolatedColor));
			}

			while(d>=0) {
				yy1 = yy1 + stepY;
				d = d - (dx * 2);
			}

			xx1 = xx1 + stepX;
			d = d + (dy * 2);      
		}
		
		firePixel(bitmap, xx1, yy1, color2);
	}
	
	/** Light on a pixel.
	 * 
	 * @param bitmap
	 * @param x
	 * @param y
	 * @param color
	 * @return <code>true</code> if fired, otherwise <code>false</code>
	 */
	public static boolean firePixel(float[][] bitmap, int x, int y, float color) {
		if (x>=0 && x<bitmap.length) {
			if (y>=0 && y<bitmap[x].length) {
				bitmap[x][y] = color;
				return true;
			}
		}
		return false;
	}
	
	/** Light on a pixel.
	 * 
	 * @param bitmap
	 * @param x
	 * @param y
	 * @param color
	 * @param width
	 * @return <code>true</code> if fired, otherwise <code>false</code>
	 */
	public static boolean firePixel(float[][] bitmap, int x, int y, float color, int width) {
		boolean all = true;
		for(int dx=x-width; dx<=x+width; ++dx) {
			for(int dy=y-width; dy<=y+width; ++dy) {
				if (dx>=0 && dx<bitmap.length) {
					if (dy>=0 && dy<bitmap[dx].length) {
						bitmap[dx][dy] = color;
					}
					else all = false;
				}
				else all = false;
			}
		}
		return all;
	}

	/** Light on a pixel.
	 * 
	 * @param bitmap
	 * @param x1
	 * @param y1
	 * @param color
	 * @param width
	 * @return <code>true</code> if fired, otherwise <code>false</code>
	 */
	private static boolean firePixelWhenHigher(float[][] bitmap, int x, int y, float color, int width) {
		boolean all = true;
		for(int dx=x-width; dx<=x+width; ++dx) {
			for(int dy=y-width; dy<=y+width; ++dy) {
				if (dx>=0 && dx<bitmap.length) {
					if (dy>=0 && dy<bitmap[dx].length && (Float.isNaN(bitmap[dx][dy]) || color > bitmap[dx][dy])) {
						bitmap[dx][dy] = color;
					}
					else all = false;
				}
				else all = false;
			}
		}
		return all;
	}

	private static boolean addToTriangleLine(TriangleFillingLine[] triangleLines, int miny, int x, int y, float color) {
		int yy = y - miny;
		if (yy>=0 && yy<triangleLines.length) {
			if (triangleLines[yy]==null) {
				triangleLines[yy] = new TriangleFillingLine(x, color);
				return true;
			}
			return triangleLines[yy].add(x, color);
		}
		return false;
	}

	/**
	 * Graphic 2D utils
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class TriangleFillingLine {

		private int x1, x2;
		private float color1, color2;
		
		public TriangleFillingLine(int x, float color) {
			this.x1 = this.x2 = x;
			this.color1 = this.color2 = color;
		}
		
		public int getMinX() {
			return this.x1;
		}
		
		public int getMaxX() {
			return this.x2;
		}

		public float getMinColor() {
			return this.color1;
		}

		public float getMaxColor() {
			return this.color2;
		}
		
		public boolean add(int x, float color) {
			if (x<this.x1) {
				this.x1 = x;
				this.color1 = color;
				return true;
			}
			if (x>this.x2) {
				this.x2 = x;
				this.color2 = color;
				return true;
			}
			return false;
		}

	}
	
}
