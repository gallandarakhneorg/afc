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
package fr.utbm.set.jasim.environment.model.world;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import fr.utbm.set.collection.IteratorIterator;
import fr.utbm.set.geom.PseudoHamelDimension;
import fr.utbm.set.geom.bounds.bounds1d5.CombinableBounds1D5;
import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.transform.Transform1D5;
import fr.utbm.set.gis.road.primitive.RoadConnection;
import fr.utbm.set.gis.road.primitive.RoadNetwork;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.gis.road.SubRoadNetwork;
import fr.utbm.set.graph.GraphIterationElement;
import fr.utbm.set.graph.GraphIterator;
import fr.utbm.set.graph.SubGraph;
import fr.utbm.set.graph.SubGraphBuildAdapter;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody1D5;
import fr.utbm.set.jasim.environment.interfaces.body.InfluencerMobileEntity1D5;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.InterestFilter;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perceiver;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perceptions;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.PhysicalPerceptionAlterator;
import fr.utbm.set.jasim.environment.model.influencereaction.EnvironmentalAction1D5;
import fr.utbm.set.jasim.environment.model.perception.frustum.CullingResult1D5;
import fr.utbm.set.jasim.environment.model.perception.frustum.Frustum1D5;
import fr.utbm.set.jasim.environment.model.perception.frustum.Frustum1D5Util;
import fr.utbm.set.jasim.environment.model.perception.percepts.DefaultGroundPerception1D5;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList1D5;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList1D51D;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList1D51D5;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList1D52D;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList1D53D;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGenerator;
import fr.utbm.set.jasim.environment.model.place.Place1D5;
import fr.utbm.set.jasim.environment.time.Clock;

/**
 * This class manage the perception data structures.
 * But default, it uses an SeTGIS road network to store static and mobiles entities.
 * 
 * @param <SB> is the bounds of the static entities.
 * @param <MB> is the bounds of the mobile entities.
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public class WorldModelManager1D5<SB extends CombinableBounds1D5<RoadSegment>, MB extends CombinableBounds1D5<RoadSegment>> 
implements PerceptionGenerator, 
		   WorldModelActuator<EnvironmentalAction1D5,MobileEntity1D5<MB>>,
		   WorldModelContainer<Entity1D5<SB>, MobileEntity1D5<MB>> {

	private static final String MOBILE_ENTITIES = "MobileEntities"; //$NON-NLS-1$
	private static final String STATIC_ENTITIES = "StaticEntities"; //$NON-NLS-1$
	
	//=============================================================
	// WORLD MODEL
	//=============================================================
	
	/**
	 * Weak Reference to the enclosing place.
	 */
	private WeakReference<Place1D5<SB,MB>> place;

	/**
	 * Strong Reference to the road network to avoid garbage collecting.
	 */
	private RoadNetwork roadNetwork;

	/**
	 * Static entity segments.
	 */
	private final Collection<RoadSegment> immobileObjectSegments;

	/**
	 * Mobile entity segments.
	 */
	private final Collection<RoadSegment> mobileObjectSegments;

	/**
	 * Map associating the address of a holon to its 
	 * corresponding computed set of percepts
	 */
	private final Map<AgentBody1D5<?,?,MB>,PerceptionList1D5<SB,MB,?>> perceptions;
	
	/** Buffered registered actions.
	 */
	private Iterable<EnvironmentalAction1D5> bufferedActions = null;

	//=============================================================
	// CONSTRUCTORS
	//=============================================================
	
	/**
	 * Creates a new PerceptionManager1D5
	 */
	public WorldModelManager1D5() {
		this.roadNetwork = null;
		Comparator<RoadSegment> comparator =
				new Comparator<RoadSegment>() {
					@Override
					public int compare(RoadSegment o1, RoadSegment o2) {
						return System.identityHashCode(o2) - System.identityHashCode(o1);
					}						
				};
		this.immobileObjectSegments = new TreeSet<RoadSegment>(comparator);
		this.mobileObjectSegments = new TreeSet<RoadSegment>(comparator);

		this.perceptions = new TreeMap<AgentBody1D5<?,?,MB>,PerceptionList1D5<SB,MB,?>>(
				new Comparator<AgentBody1D5<?,?,MB>>() {				
				@Override
				public int compare(AgentBody1D5<?,?,MB> b1, AgentBody1D5<?,?,MB> b2) {
					return b1.getIdentifier().compareTo(b2.getIdentifier());
				}});
	}
	
	/**
	 * Initializes the PerceptionManager with the given data.
	 * 
	 * @param roads are all the road network.
	 * @param staticEntitiesIterator is the structure containing the static entities
	 * @param mobileEntitiesIterator is the structure containing the mobile entities
	 * @param placeInstance is the place that owns this perception manager.
	 */
	public void init(
					RoadNetwork roads,
					Iterator<? extends Entity1D5<SB>> staticEntitiesIterator, 
					Iterator<? extends MobileEntity1D5<MB>> mobileEntitiesIterator, 
					Place1D5<SB,MB> placeInstance) {
		Entity1D5<SB> staticEntity;
		MobileEntity1D5<MB> mobileEntity;
		RoadSegment segment;

		// Save the reference on the road network
		assert(roads!=null);
		this.roadNetwork = roads;
		
		this.place = placeInstance==null ? null : new WeakReference<Place1D5<SB,MB>>(placeInstance);

		// Populates the segments		
		if (staticEntitiesIterator != null) {
			while (staticEntitiesIterator.hasNext()) {
				staticEntity = staticEntitiesIterator.next();
				segment = staticEntity.getRoadSegment();
				if (this.roadNetwork.contains(segment)) {
					segment.addUserData(STATIC_ENTITIES, staticEntity);
					this.immobileObjectSegments.add(segment);
				}
			}
		}
		if (mobileEntitiesIterator != null) {
			while (mobileEntitiesIterator.hasNext()) {
				mobileEntity = mobileEntitiesIterator.next();
				segment = mobileEntity.getRoadSegment();
				if (this.roadNetwork.contains(segment)) {
					segment.addUserData(MOBILE_ENTITIES, mobileEntity);
					if (mobileEntity instanceof InfluencerMobileEntity1D5<?,?>) {
						// Register influencers
						((InfluencerMobileEntity1D5<?,?>)mobileEntity).setEnvironment(placeInstance);
					}
					// Register standard mobile entity
					this.mobileObjectSegments.add(segment);
				}
			}
		}
	}
	
	/**
	 * End this manager.
	 */
	public void destroy() {
		this.perceptions.clear();
		
		for(RoadSegment segment : this.immobileObjectSegments) {
			segment.clearUserData(STATIC_ENTITIES);
		}
		this.immobileObjectSegments.clear();
		
		for(RoadSegment segment : this.mobileObjectSegments) {
			segment.clearUserData(MOBILE_ENTITIES);
		}
		this.mobileObjectSegments.clear();
	}

	private void doFrustumPerception(
			Frustum1D5 frustum,
			AgentBody1D5<?,?,MB> body,
			PerceptionList1D5<SB,MB,?> perceptionList) {
		RoadSegment segment = frustum.getRoadSegment();
		Point1D5 position1d5 = frustum.getEye();
		RoadConnection entryPoint = body.getRoadEntry();
		boolean reverseOrder = segment.getEndPoint().equals(entryPoint);

		// Forward perception
		{
			double position = (reverseOrder) ?
					segment.getLength() - position1d5.getCurvilineCoordinate()
					: position1d5.getCurvilineCoordinate();
			double distance = frustum.getForwardDistance();
			
			GraphIterator<RoadSegment,RoadConnection> iterator = 
				this.roadNetwork.depthIterator(segment, distance, position, entryPoint, false, false);

			SubRoadNetwork subNetwork = new SubRoadNetwork();
			PerceptionBuilder builder = new PerceptionBuilder(frustum, body, perceptionList);
			subNetwork.build(iterator, builder);

			perceptionList.setGroundPerception(
					new DefaultGroundPerception1D5(subNetwork));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void computeAgentPerceptions() {
		AgentBody1D5<?,?,MB> body;
		Collection<? extends Frustum1D5> frustums;
		PhysicalPerceptionAlterator physicFilter;
		InterestFilter interestFilter;
		PerceptionList1D5<SB,MB,?> list;
		PseudoHamelDimension dimension;
		Collection<? extends MobileEntity1D5<MB>> entities;
		
		this.perceptions.clear();
		
		for(RoadSegment segment : this.mobileObjectSegments) {

			entities = segment.getUserDataCollection(MOBILE_ENTITIES);
			if (entities!=null) {

				for(MobileEntity1D5<MB> entity : entities) {
					
					if (entity instanceof AgentBody1D5) {
						body = (AgentBody1D5<?,?,MB>)entity;
						dimension = body.getPreferredMathematicalDimension();
						
						list = null;
						switch(dimension) {
						case DIMENSION_1D:
							list = new PerceptionList1D51D<SB,MB>();
							break;
						case DIMENSION_1D5:
							list = new PerceptionList1D51D5<SB,MB>();
							break;
						case DIMENSION_2D:
							list = new PerceptionList1D52D<SB,MB>();
							break;
						case DIMENSION_2D5:
						case DIMENSION_3D:
							list = new PerceptionList1D53D<SB,MB>();
							break;
						default:
						}
						
						if (list!=null) {
							frustums = body.getFrustums();			
							physicFilter = body.getPerceptionFilter();
							if (physicFilter!=null)
								throw new IllegalArgumentException("physical perception filter not yet supported"); //$NON-NLS-1$
							interestFilter = body.getInterestFilter();
							if (interestFilter!=null)
								throw new IllegalArgumentException("interest perception filter not yet supported"); //$NON-NLS-1$
							
							//Iterate of the various frustums associated to the current body
							Iterator<? extends Frustum1D5> frustumIterator = frustums.iterator();
							Frustum1D5 frustum;
							while (frustumIterator.hasNext()) {			
								frustum = frustumIterator.next();
								doFrustumPerception(frustum, body, list);
							} // End of frustum iteration
							
							this.perceptions.put(body, list);
						}
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <PT extends Perception> Perceptions<PT> getPerceptions(Perceiver perceiver, Class<PT> perceptionType) {
		Perceptions<PT> perceptionsToReply = null;
		PerceptionList1D5<SB,MB,?> list;
		list = this.perceptions.get(perceiver);
		if (list!=null && list.isSupportedPerceptionType(perceptionType)) {
			perceptionsToReply = (Perceptions<PT>)list;
		}
		return perceptionsToReply;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <WM> WM getInnerWorldModel(Class<WM> type) {
		if (this.roadNetwork!=null && type.isInstance(this.roadNetwork)) {
			return type.cast(this.roadNetwork);
		}
		return null;
	}

	@Override
	public void registerActions(Clock time,
			Iterable<EnvironmentalAction1D5> actions) {
		this.bufferedActions = actions;
	}

	@Override
	public void commit() {
		if (this.bufferedActions!=null) {
			MobileEntity1D5<?> obj;
			Transform1D5<RoadSegment> transform;
			for(EnvironmentalAction1D5 action : this.bufferedActions) {
				obj = action.getEnvironmentalObject(MobileEntity1D5.class);
				if (obj!=null) {
					transform = action.getTransformation();

					RoadSegment previousSegment = obj.getRoadSegment();
					
					obj.transform(transform); // Frustums must be also transformed if the entity is an agent body
					
					RoadSegment currentSegment = obj.getRoadSegment();
					
					if (!currentSegment.equals(previousSegment)) {
						previousSegment.removeUserData(MOBILE_ENTITIES, obj);
						if (!previousSegment.hasUserData(MOBILE_ENTITIES)) {
							this.mobileObjectSegments.remove(previousSegment);
						}

						currentSegment.addUserData(MOBILE_ENTITIES, obj);
						this.mobileObjectSegments.add(currentSegment);
					}
				}
			}
			
			this.bufferedActions = null;
		}	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerMobileEntity(MobileEntity1D5<MB> entity) {
		RoadSegment segment = entity.getRoadSegment();
		if (entity instanceof InfluencerMobileEntity1D5<?,?>) {
			((InfluencerMobileEntity1D5<?,?>)entity).setEnvironment(
					this.place==null ? null : this.place.get());
		}
		this.mobileObjectSegments.add(segment);
		segment.addUserData(MOBILE_ENTITIES, entity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unregisterMobileEntity(MobileEntity1D5<MB> entity) {
		RoadSegment segment = entity.getRoadSegment();
		segment.removeUserData(MOBILE_ENTITIES, entity);
		if (entity instanceof AgentBody1D5<?,?,?>) {
			this.perceptions.remove(entity);
		}
		if (entity instanceof InfluencerMobileEntity1D5<?,?>) {
			((InfluencerMobileEntity1D5<?,?>)entity).setEnvironment(null);
		}
		if (!segment.hasUserData(MOBILE_ENTITIES)) {
			this.mobileObjectSegments.remove(segment);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<? extends MobileEntity1D5<MB>> getMobileEntities() {
		return new RoadEntityIterator<MobileEntity1D5<MB>>(MOBILE_ENTITIES, this.mobileObjectSegments.iterator());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<? extends Entity1D5<SB>> getStaticEntities() {
		return new RoadEntityIterator<Entity1D5<SB>>(STATIC_ENTITIES, this.immobileObjectSegments.iterator());
	}

	/**
	 * Perception builder and selector.
	 * 
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid fr.utbm.set.sfc.jasim
	 * @mavenartifactid jasim-environment1d5
	 */
	private class PerceptionBuilder extends SubGraphBuildAdapter<RoadSegment,RoadConnection> {
		
		private final PerceptionList1D5<SB,MB,?> frustumList;
		private final double perceptionDistance;
		private final UUID bodyId;
		private final UUID frustumId;
		private final MB bounds;
		private final Point1D5 position;
		private final double lowerJutt, upperJutt;
		
		public PerceptionBuilder(Frustum1D5 frustum,
				  				 AgentBody1D5<?,?,MB> body,
				  				 PerceptionList1D5<SB,MB,?> list) {
			this.bounds = body.getBounds();
			this.frustumList = list;
			this.perceptionDistance = frustum.getForwardDistance();
			this.bodyId = body.getIdentifier();
			this.frustumId = frustum.getIdentifier();
			this.position = this.bounds.getPosition();
			double demiSize = this.bounds.getLateralSize()/2.;
			this.lowerJutt = this.position.getJuttingDistance() - demiSize;
			this.upperJutt = this.position.getJuttingDistance() + demiSize;
		}

		@Override
		public void segmentAdded(
				SubGraph<RoadSegment, RoadConnection, ?> graph,
				GraphIterationElement<RoadSegment, RoadConnection> elementDescription) {
			Collection<? extends Entity1D5<? extends CombinableBounds1D5<RoadSegment>>> mobileEntities;
			Collection<? extends Entity1D5<? extends CombinableBounds1D5<RoadSegment>>> staticEntities;
			
			RoadSegment segment = elementDescription.getSegment();
			RoadConnection point = elementDescription.getPoint();

			mobileEntities = segment.getUserDataCollection(MOBILE_ENTITIES);
			if (mobileEntities==null) mobileEntities = Collections.emptyList();
			
			staticEntities = segment.getUserDataCollection(STATIC_ENTITIES);
			if (staticEntities==null) staticEntities = Collections.emptyList();
			
			doPrecisePerception(
					new IteratorIterator<Entity1D5<? extends CombinableBounds1D5<RoadSegment>>>(mobileEntities.iterator(), staticEntities.iterator()), 
					elementDescription.getDistanceToReachSegment(),
					segment,
					point,
					segment.getBeginPoint().equals(point));
		}
		
		private void doPrecisePerception(Iterator<Entity1D5<? extends CombinableBounds1D5<RoadSegment>>> entities, 
										 double distanceToReachSegment,
										 RoadSegment segment,
										 RoadConnection entryPoint,
										 boolean onSegmentDirection) {
			Entity1D5<? extends CombinableBounds1D5<RoadSegment>> entity;
			Point1D5 p1d5;
			double relativeDistance;
			boolean isInFront;
			IntersectionType curvilineClassification, juttingClassification, fullClassification;
			double demiSize, ljutt, ujutt;
			CullingResult1D5<Entity1D5<? extends CombinableBounds1D5<RoadSegment>>> result;
			RoadSegment currentSegment = segment;
			
			while (entities.hasNext()) {
				entity = entities.next();
				if (!entity.getIdentifier().equals(this.bodyId)) {
					
					p1d5 = entity.getPosition1D5();
					currentSegment = entity.getRoadSegment();
					relativeDistance = distanceToReachSegment;

					// Agent pos always relative to start point, so if the iterator placed us on the end connection we must add the remaining length
					if (onSegmentDirection)
						relativeDistance += p1d5.getCurvilineCoordinate();
					else
						relativeDistance += currentSegment.getLength() - p1d5.getCurvilineCoordinate();

					isInFront = (relativeDistance>=0.);
					
					// Remove the back distance of the other body 
					// to the distance, assuming BoundingRect1D5
					double perceivedDemiSize = entity.getBounds().getSizeX()/2.;
					double farDistance = relativeDistance + perceivedDemiSize;
					relativeDistance -= perceivedDemiSize;
					
					// Compute the type of intersection
					curvilineClassification = Frustum1D5Util.classifiesOnDirection(
							0, this.perceptionDistance, 
							relativeDistance, farDistance);

					demiSize = entity.getBounds().getLateralSize()/2.;
					ljutt = p1d5.getJuttingDistance() - demiSize;
					ujutt = p1d5.getJuttingDistance() + demiSize;
					juttingClassification = Frustum1D5Util.classifiesOnDirection(
							this.lowerJutt, this.upperJutt, 
							ljutt, ujutt);
					
					fullClassification = curvilineClassification.and(juttingClassification);

					 // -> vehicle inside frustum					
					if (fullClassification!=IntersectionType.OUTSIDE) {
						boolean sameDirection;
						
						if ( entity instanceof MobileEntity1D5<?> ) {
							sameDirection = ((MobileEntity1D5<? extends CombinableBounds1D5<RoadSegment>>)entity).getRoadEntry().equals(entryPoint);
						}
						else {
							sameDirection = false;
						}

						result = new CullingResult1D5<Entity1D5<? extends CombinableBounds1D5<RoadSegment>>>(
								this.frustumId,
								fullClassification,
								entity,
								Math.max(relativeDistance, 0.), // relative curviline distance
								p1d5.getJuttingDistance() - this.position.getJuttingDistance(), // relative shift distance
								isInFront,
								sameDirection,
								null);
						
						if ( entity instanceof MobileEntity1D5<?>) {
							this.frustumList.addDynamicPerception(result);
						}
						else {
							this.frustumList.addStaticPerception(result);
						}
					}
				}					
			}
		}

	}  // class PerceptionBuilder 

	/**
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid fr.utbm.set.sfc.jasim
	 * @mavenartifactid jasim-environment1d5
	 */
	private static class RoadEntityIterator<M extends Entity1D5<?>> implements Iterator<M> {

		private final String key;
		private final Iterator<? extends RoadSegment> iterator;
		private Iterator<? extends M> iterator2;
		private M next;

		/**
		 * @param k
		 * @param iterator
		 */
		public RoadEntityIterator(String k, Iterator<? extends RoadSegment> iterator) {
			this.key = k;
			this.iterator = iterator;
			this.iterator2 = null;
			this.next = getNext();
		}
		
		private M getNext() {
			RoadSegment sgmt;
			Collection<? extends M> collection = null;
			while ((this.iterator2==null || !this.iterator2.hasNext())&&(this.iterator.hasNext())) {
				this.iterator2 = null;
				sgmt = this.iterator.next();
				if (sgmt!=null) {
					collection = sgmt.<M>getUserDataCollection(this.key);
					if (collection!=null) {
						this.iterator2 = collection.iterator();
					}
				}
			}
			return (this.iterator2!=null && this.iterator2.hasNext())
					? this.iterator2.next() : null;
		}

		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		@Override
		public M next() {
			if (this.next==null) throw new NoSuchElementException();
			M n = this.next;
			this.next = getNext();
			return n;
		}

		@Override
		public void remove() {
			//
		}
		
	} // class RoadEntityIterator
	
}
