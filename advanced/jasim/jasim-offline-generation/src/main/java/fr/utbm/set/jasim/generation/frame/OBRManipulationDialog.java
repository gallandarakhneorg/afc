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
package fr.utbm.set.jasim.generation.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.bounds.bounds2d.Bounds2D;
import fr.utbm.set.geom.bounds.bounds2d.MinimumBoundingRectangle;
import fr.utbm.set.geom.bounds.bounds2d.OrientedBoundingRectangle;
import fr.utbm.set.geom.bounds.bounds2d.OrientedBounds2D;
import fr.utbm.set.geom.bounds.bounds3d.OrientedCombinableBounds3D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.ui.window.JOkCancelBtDialog;

/**
 * Window for changing the oriented of an oriented bounding rectangle.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OBRManipulationDialog extends JOkCancelBtDialog
implements ChangeListener {

	private static final long serialVersionUID = 6009372667484972709L;

	/** Displayed entities.
	 */
	final Collection<? extends Entity3D<? extends OrientedCombinableBounds3D>> entities;
	
	/** Oriented bounding rectangle around the the entities.
	 */
	final OrientedBoundingRectangle boundingRectangle = new OrientedBoundingRectangle();

	/** Aligned bounding rectangle around the the entities.
	 */
	final MinimumBoundingRectangle alignedRectangle = new MinimumBoundingRectangle();

	/** List of 2D bounds.
	 */
	final Collection<Bounds2D> bounds2D;
	
	private final Vector2d R = new Vector2d();
	
	private final WorldPanel worldPanel;
	private final JSlider angleSlider;

	/**
	 * @param parent
	 * @param entities
	 */
	public OBRManipulationDialog(Component parent, Collection<? extends Entity3D<? extends OrientedCombinableBounds3D>> entities) {
		super(parent, "OBR Settings"); //$NON-NLS-1$
		
		this.entities = entities;
		this.bounds2D = new ArrayList<Bounds2D>(this.entities.size());
		OrientedCombinableBounds3D bb;
		for(Entity3D<? extends OrientedCombinableBounds3D> entity : this.entities) {
			bb = entity.getBounds();
			if (bb!=null) {
				this.bounds2D.add(bb.toBounds2D());
				this.alignedRectangle.combine(bb.toBounds2D());
			}
		}
		this.boundingRectangle.reset();
		this.boundingRectangle.combine(this.bounds2D);
		this.R.set(this.boundingRectangle.getR());
		
		setModal(true);
		this.topPane.setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		this.worldPanel = new WorldPanel();
		JScrollPane scrollPane = new JScrollPane(this.worldPanel);
		this.topPane.add(scrollPane, BorderLayout.CENTER);
		
		this.angleSlider = new JSlider(-40, 40);
		this.angleSlider.setExtent(1);
		this.angleSlider.setValue(0);
		this.angleSlider.setSnapToTicks(true);
		this.angleSlider.addChangeListener(this);
		this.topPane.add(this.angleSlider, BorderLayout.SOUTH);
		
		this.worldPanel.updateContent();
		
		centerDialog();
	}
	
	/** Replies the current OBR.
	 * 
	 * @return the current OBR.
	 */
	public OrientedBoundingRectangle getOBR() {
		return this.boundingRectangle;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		int value = this.angleSlider.getValue();
		double angle = value / 10.;
		
		Vector2d newR = new Vector2d(this.R);
		if (angle!=0.) {
			GeometryUtil.turnVector(newR, angle);
		}
		
		this.boundingRectangle.setFromBounds(newR, this.bounds2D);
		
		this.worldPanel.updateContent();
	}
	
	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class WorldPanel extends JPanel {

		private static final long serialVersionUID = -6747171385412513566L;
		
		private double dx, dy, sx, sy, scale;
		
		/**
		 */
		public WorldPanel() {
			//
		}

		/** Update the content of the panel.
		 */
		public void updateContent() {
			double wmxx = Math.max(
					OBRManipulationDialog.this.alignedRectangle.getMaxX(),
					OBRManipulationDialog.this.boundingRectangle.getMaxX());
			double wmnx = Math.min(
					OBRManipulationDialog.this.alignedRectangle.getMinX(),
					OBRManipulationDialog.this.boundingRectangle.getMinX());
			double wmxy = Math.max(
					OBRManipulationDialog.this.alignedRectangle.getMaxY(),
					OBRManipulationDialog.this.boundingRectangle.getMaxY());
			double wmny = Math.min(
					OBRManipulationDialog.this.alignedRectangle.getMinY(),
					OBRManipulationDialog.this.boundingRectangle.getMinY());
			double wsx = wmxx - wmnx;
			double wsy = wmxy - wmny;

			this.dx = -wmnx;
			this.dy = wmxy;

			if (wsx >= wsy) {
				this.scale = wsy / wsx;
				this.sx = 1024;
				this.sy = (int) (this.scale * 1024.);
				this.scale = 1024. / wsx;
			} else {
				this.scale = wsx / wsy;
				this.sy = 1024;
				this.sx = (int) (this.scale * 1024.);
				this.scale = 1024. / wsy;
			}
			
			setPreferredSize(new Dimension(((int)this.sx)+1, ((int)this.sy)+1));
			validate();
			repaint();
		}
		
		private Shape toShape(Bounds2D bb, double dx, double dy, double scale) {
			GeneralPath path = new GeneralPath();

			if (bb instanceof OrientedBounds2D) {
				OrientedBounds2D obb = (OrientedBounds2D)bb;
				Point2d c = obb.getCenter();
				Vector2d R = obb.getR();
				Vector2d S = obb.getS();
				// Top
				drawFace(path, 
						new EuclidianPoint3D(c.x, c.y, 0.), 
						new Vector3d(R.x, R.y, 0.),
						new Vector3d(S.x, S.y, 0.),
						new Vector3d(0.,0.,1.),
						obb.getRExtent(), 
						obb.getSExtent(), 
						0.,
						dx, 
						dy, 
						scale);
			}
			else {
				Point2d c = bb.getCenter();
				Vector3d R = new Vector3d(1., 0., 0.);
				Vector3d S = new Vector3d(0., 1., 0.);
				Vector3d T = new Vector3d(0., 0., 1.);
				double Re = bb.getSizeX()/2.;
				double Se = bb.getSizeY()/2.;
				double Te = 0.;

				// Top
				drawFace(path, 
						new EuclidianPoint3D(c.x, c.y, 0.),
						R, S, T, 
						Re, Se, Te, 
						dx, dy, scale);
			}

			return path;
		}

		private void drawFace(GeneralPath path, EuclidianPoint3D center, Vector3d v1, Vector3d v2, Vector3d v3, double e1, double e2, double e3, double dx, double dy, double scale) {
			Vector3d v = new Vector3d();
			Point3d p = new Point3d();
			Point3d p1 = new Point3d();

			p.set(center);
			v.set(v1);
			v.scale(e1);
			p.add(v);
			v.set(v2);
			v.scale(e2);
			p.add(v);
			v.set(v3);
			v.scale(e3);
			p.add(v);
			toPictureCS(p, dx, dy, scale);
			p1.set(p);
			path.moveTo(p.x, p.y);

			p.set(center);
			v.set(v1);
			v.scale(e1);
			p.add(v);
			v.set(v2);
			v.scale(-e2);
			p.add(v);
			v.set(v3);
			v.scale(e3);
			p.add(v);
			toPictureCS(p, dx, dy, scale);
			path.lineTo(p.x, p.y);

			p.set(center);
			v.set(v1);
			v.scale(-e1);
			p.add(v);
			v.set(v2);
			v.scale(-e2);
			p.add(v);
			v.set(v3);
			v.scale(e3);
			p.add(v);
			toPictureCS(p, dx, dy, scale);
			path.lineTo(p.x, p.y);

			p.set(center);
			v.set(v1);
			v.scale(-e1);
			p.add(v);
			v.set(v2);
			v.scale(e2);
			p.add(v);
			v.set(v3);
			v.scale(e3);
			p.add(v);
			toPictureCS(p, dx, dy, scale);
			path.lineTo(p.x, p.y);

			path.lineTo(p1.x, p1.y);
		}

		private void toPictureCS(Point3d p, double dx, double dy, double scale) {
			double x = (int) ((p.x + dx) * scale);
			double y = (int) ((dy - p.y) * scale);
			p.set(x, y, p.z);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			
			Graphics2D g2d = (Graphics2D)g;
			
			g2d.setColor(Color.BLUE);
			g2d.draw(toShape(OBRManipulationDialog.this.alignedRectangle, 
					this.dx, this.dy, this.scale));

			g2d.setColor(Color.GREEN);
			for(Bounds2D bb : OBRManipulationDialog.this.bounds2D) {
				g2d.draw(toShape(bb, this.dx, this.dy, this.scale));
			}
			
			g2d.setColor(Color.RED);
			g2d.draw(toShape(OBRManipulationDialog.this.boundingRectangle, 
					this.dx, this.dy, this.scale));
		}
		
	}

}
