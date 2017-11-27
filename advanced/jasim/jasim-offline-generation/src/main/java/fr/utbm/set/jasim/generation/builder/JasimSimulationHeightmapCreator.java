/*
 * $Id$
 * 
 * Copyright (c) 2008-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.vecmath.Point3d;

import org.arakhne.afc.vmutil.FileSystem;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.io.collada.ColladaReader;
import fr.utbm.set.io.magic.FileType;
import fr.utbm.set.live.dataelement.rendable.GroundDataElement;
import fr.utbm.set.live.dataelement.rendable.GroundDataElement.UncompiledGroundException;
import fr.utbm.set.live.entity.EntityInterface;
import fr.utbm.set.live.entity.GroundEntity;
import fr.utbm.set.live.entity.RendableEntityInterface;
import fr.utbm.set.live.entity.factory.LiveFactory;
import fr.utbm.set.live.entity.factory.build.DynamicTypeLiveFactory;
import fr.utbm.set.live.scenegraph.SceneGraphElementData;
import fr.utbm.set.util.MIMEConstants;

/**
 * Main class that enables to create heightmap from a ground mesh.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class JasimSimulationHeightmapCreator implements JasimSimulationHeightmapCreatorConstants {

	private AlignedBoundingBox bounds;
	private Point3d lower;
	private float[][] heights;
	
	private final File terrainMap;
	private final File heightmapPicture;
	private final float pixelsPerMeterX;
	private final float pixelsPerMeterY;
	private final String defaultPictureType;
	private final int groundZero;
	private final HeightmapPictureType heightmapType;

    /**
     * Processor used to read collada file
     */
    private ColladaReader colladaReader = null;

    /**
     * Factory used to build a scenegraph from a reader of a given 3D file format
     */
    private LiveFactory groundFactory = null;
	
	/**
	 * @param terrainMap is the path to the COLLADA file .DAE to load.
	 * @param heightmapPicture is the path to the heightmap picture to generate.
	 * @param pixelsPerMeterX is precision in pixels per meter along the X axis. 
	 * @param pixelsPerMeterY is precision in pixels per meter along the Y axis.
	 * @param groundZero is the color under which (inclusive) the terrain is not traversable.
	 * @param heightmapType is the type of heightmap to generate.
	 * @param pictureType is the type of the picture ("png", "jpeg"...).
	 * the entityDescriptors to ignore. <code>null</code> means no filtering.
	 */
	public JasimSimulationHeightmapCreator(File terrainMap, File heightmapPicture, float pixelsPerMeterX, float pixelsPerMeterY, int groundZero, HeightmapPictureType heightmapType, String pictureType) {
		this.terrainMap = terrainMap;
		this.heightmapPicture = heightmapPicture;
		this.pixelsPerMeterX = pixelsPerMeterX;
		this.pixelsPerMeterY = pixelsPerMeterY;
		this.defaultPictureType = pictureType;
		this.heightmapType = heightmapType;
		this.groundZero = groundZero;
	}	
	
	/** Read a Collada file and replies the 3D entities.
	 * 
	 * @param filePath is the path of the Collada file.
	 * @return the list of entities red from the Collada file. 
	 * @throws IOException
	 */
    protected List<EntityInterface> readColladaFile(URL filePath) throws IOException {
        if (this.colladaReader == null) {
        	this.colladaReader = new ColladaReader();
        }

        if (this.groundFactory == null) {
            this.groundFactory = new LiveFactory(new DynamicTypeLiveFactory(),true,false);
        }
        this.groundFactory.clear();

        this.colladaReader.setURL(filePath);

        this.colladaReader.addMeshListener(this.groundFactory);
        this.colladaReader.addNodeListener(this.groundFactory);
        this.colladaReader.addMeshAttachmentListener(this.groundFactory);
        this.colladaReader.addErrorListener(this.groundFactory);

        this.colladaReader.process();

        this.colladaReader.removeMeshListener(this.groundFactory);
        this.colladaReader.removeNodeListener(this.groundFactory);
        this.colladaReader.removeMeshAttachmentListener(this.groundFactory);
        this.colladaReader.removeErrorListener(this.groundFactory);

        return this.groundFactory.getRoots();
    }
	
	/** Extract mesh vertices.
	 * 
	 * @return the success state.
	 * @throws Exception
	 */
	public boolean generate() throws Exception {
		List<EntityInterface> grounds;
		GroundEntity ground = null;
		
		
		String content = FileType.getContentType(this.terrainMap);
		if (MIMEConstants.MIME_COLLADA.getMimeConstant().equals(content)) {
			grounds = readColladaFile(this.terrainMap.toURI().toURL());
		}
		else {
			return false;
		}
		
		for(EntityInterface entity : grounds) {//Takes the first ground entity and assign ground to it 
			if (ground == null && entity instanceof GroundEntity) {
				ground = (GroundEntity)entity;
			}
		}

		if(ground != null) {		
			if (computeBounds(ground)) {
				//Generate the quadtree associate to the ground Elements
				GroundDataElement groundElem = null;
				for( SceneGraphElementData dataElem : ground.getRootElement().getAllAssociateData()) {
					groundElem = ((GroundDataElement) dataElem);
					if(!groundElem.isCompiled()) {
						groundElem.compile();
					}
				}
				
				int pictureWidth = (int)Math.round(this.bounds.getSizeX() * this.pixelsPerMeterX);
				int pictureHeight = (int)Math.round(this.bounds.getSizeY() * this.pixelsPerMeterY);
				
				generateHeights(pictureWidth, pictureHeight, ground);
				generatePicture(pictureWidth, pictureHeight);
				
				return true;
			}		
		}
		
		return false;
	}
	
	/**
	 * Generate the heights.
	 * 
	 * @param pictureWidth is the width of the heightmap.
	 * @param pictureHeight is the height of the heightmap.
	 * @param entityDescriptors
	 * @throws UncompiledGroundException 
	 */
	private void generateHeights(int pictureWidth, int pictureHeight, GroundEntity ground)
	throws UncompiledGroundException {
		// Extract entityDescriptors
		System.out.print("Extracting vertices..."); //$NON-NLS-1$
		
		try {
			this.heights = new float[pictureWidth][pictureHeight];					
			double x = 0;
			double y= 0;
			double height;
			for(int i = 0; i<pictureWidth; ++i) {
				for(int j = 0; j<pictureHeight; ++j) {				
					x = ((i*this.bounds.getSizeX())/(this.heights.length-1))+this.lower.x;
					y = ((j*this.bounds.getSizeY())/(this.heights[0].length-1))+this.lower.y;
					//System.out.println("i: "+i+" j: "+j+" x: "+x+" y: "+y);
					height = ground.getHeight(x, y);
					this.heights[i][j] = Double.valueOf(height).floatValue();
				}	
			}
		} finally {
			System.out.println("done"); //$NON-NLS-1$
		}
	}

	/** Compute the bounds of the map.
	 */
	private boolean computeBounds(RendableEntityInterface ground) throws IOException {
		// Compute bounds
		System.out.print("Computing the bounding box..."); //$NON-NLS-1$
		
		this.lower = null;
        this.bounds = new AlignedBoundingBox(); 
        this.bounds.set(ground.getGlobalOBB(true));
		
		System.out.println("done"); //$NON-NLS-1$

		if (this.bounds!=null) {
			File summaryFile = new File(FileSystem.removeExtension(this.heightmapPicture)+"_summary.txt"); //$NON-NLS-1$

			PrintStream ps = new PrintStream(new FileOutputStream(summaryFile));
			
			Point3d lowerPt = this.bounds.getLower();
			Point3d centerPt = this.bounds.getCenter();
			Point3d upperPt = this.bounds.getUpper();
			this.lower = lowerPt;
			ps.println("Bounds: "+this.bounds.toString()); //$NON-NLS-1$
			ps.println("Bounds X = [ "+lowerPt.x //$NON-NLS-1$
					+" ... "+centerPt.x //$NON-NLS-1$
					+" ... "+upperPt.x //$NON-NLS-1$
					+" ] => "+this.bounds.getSizeX()); //$NON-NLS-1$
			ps.println("Bounds Y = [ "+lowerPt.y //$NON-NLS-1$
					+" ... "+centerPt.y //$NON-NLS-1$
					+" ... "+upperPt.y //$NON-NLS-1$
					+" ] => "+this.bounds.getSizeY()); //$NON-NLS-1$
			ps.println("Bounds Z = [ "+lowerPt.z //$NON-NLS-1$
					+" ... "+centerPt.z //$NON-NLS-1$
					+" ... "+upperPt.z //$NON-NLS-1$
					+" ] => "+this.bounds.getSizeZ()); //$NON-NLS-1$
			ps.println("UP direction is Z by default"); //$NON-NLS-1$
			
			ps.close();
			
			return true;
		}
		System.err.println("EMPTY BOUNDING BOX"); //$NON-NLS-1$
		return false;
	}
	
	/** Generate the JPEG.
	 * 
	 * @param pictureWidth is the width of the heightmap.
	 * @param pictureHeight is the height of the heightmap.
	 * @throws IOException 
	 */
	private void generatePicture(int pictureWidth, int pictureHeight) throws IOException {
		// Generate the image
		System.out.print("Saving heightmap..."); //$NON-NLS-1$
		BufferedImage image = new BufferedImage(pictureWidth,pictureHeight,BufferedImage.TYPE_INT_RGB);
		int byteColor;
		float z;
		int realY;
		int availableColors = 255 - Math.min(255, Math.max(this.groundZero,0));
		for(int ix=0; ix<pictureWidth; ++ix) {
			for(int iy=0; iy<pictureHeight; ++iy) {
				realY = pictureHeight - iy - 1; // because a picture has a inverted Y axis than the simulation
				z = this.heights[ix][iy];
				if (Float.isNaN(z)) {
					byteColor = this.groundZero;
				}
				else {
					byteColor = Math.min(255, 1+this.groundZero+(int)(((z-this.lower.z)*availableColors)/this.bounds.getSizeZ()));
				}
				
				int red, blue, green;
				
				if (byteColor>=0 && byteColor<=255) {
					red = blue = green = 0;
					switch(this.heightmapType) {
					case GRAYSCALE:
						red = blue = green = byteColor;
						break;
					case RED_ZERO_PIXEL:
						red = byteColor;
						break;
					case RED_ZERO_VECTOR:
						red = byteColor;
						blue = green = 127;
						break;
					default:
					}
				}
				else {
					red = blue = green = 255;
				}
				image.setRGB(ix, realY, new Color(red,blue,green).getRGB());
			}
		}
		ImageIO.write(image, this.defaultPictureType, this.heightmapPicture);
		System.out.println("done"); //$NON-NLS-1$
	}
	
}
