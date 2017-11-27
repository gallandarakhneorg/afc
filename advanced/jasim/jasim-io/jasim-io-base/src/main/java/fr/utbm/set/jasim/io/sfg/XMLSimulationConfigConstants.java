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
package fr.utbm.set.jasim.io.sfg;

/**
 * Set of constants for the XML parser of simulation configurations.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
interface XMLSimulationConfigConstants {

	/** &lt;simulation /&gt;
	 */
	public static final String XML_NODE_SIMULATION = "simulation"; //$NON-NLS-1$
	/** &lt;time /&gt;
	 */
	public static final String XML_NODE_TIME = "time"; //$NON-NLS-1$
	/** &lt;environment /&gt;
	 */
	public static final String XML_NODE_ENVIRONMENT = "environment"; //$NON-NLS-1$
	/** &lt;places /&gt;
	 */
	public static final String XML_NODE_PLACES = "places"; //$NON-NLS-1$
	/** &lt;place /&gt;
	 */
	public static final String XML_NODE_PLACE = "place"; //$NON-NLS-1$
	/** &lt;portals /&gt;
	 */
	public static final String XML_NODE_PORTALS = "portals"; //$NON-NLS-1$
	/** &lt;portal /&gt;
	 */
	public static final String XML_NODE_PORTAL = "portal"; //$NON-NLS-1$
	/** &lt;groundEnvironment /&gt;
	 */
	public static final String XML_NODE_GROUNDENVIRONMENT = "groundEnvironment"; //$NON-NLS-1$
	/** &lt;heightmap /&gt;
	 */
	public static final String XML_NODE_HEIGHTMAP = "heightmap"; //$NON-NLS-1$
	/** &lt;indoorGround /&gt;
	 */
	public static final String XML_NODE_INDOORGROUND = "indoorGround"; //$NON-NLS-1$
	/** &lt;alignedArea /&gt;
	 */
	public static final String XML_NODE_ALIGNEDAREA = "alignedArea"; //$NON-NLS-1$
	/** &lt;orientedArea /&gt;
	 */
	public static final String XML_NODE_ORIENTEDAREA = "orientedArea"; //$NON-NLS-1$
	/** &lt;staticEnvironment /&gt;
	 */
	public static final String XML_NODE_STATICENVIRONMENT = "staticEnvironment"; //$NON-NLS-1$
	/** &lt;dynamicEnvironment /&gt;
	 */
	public static final String XML_NODE_DYNAMICENVIRONMENT = "dynamicEnvironment"; //$NON-NLS-1$
	/** &lt;agent /&gt;
	 */
	public static final String XML_NODE_AGENT = "agent"; //$NON-NLS-1$
	/** &lt;agentSemantic /&gt;
	 */
	public static final String XML_NODE_AGENTSEMANTIC = "agentSemantic"; //$NON-NLS-1$
	/** &lt;agentSemantics /&gt;
	 */
	public static final String XML_NODE_AGENTSEMANTICS = "agentSemantics"; //$NON-NLS-1$
	/** &lt;environmentProbes /&gt;
	 */
	public static final String XML_NODE_ENVIRONMENTPROBES = "environmentProbes"; //$NON-NLS-1$
	/** &lt;environmentProbe /&gt;
	 */
	public static final String XML_NODE_ENVIRONMENTPROBE = "environmentProbe"; //$NON-NLS-1$
	/** &lt;portalSource /&gt;
	 */
	public static final String XML_NODE_PORTALSOURCE = "portalSource"; //$NON-NLS-1$
	/** &lt;portalTarget /&gt;
	 */
	public static final String XML_NODE_PORTALTARGET = "portalTarget"; //$NON-NLS-1$
	/** &lt;spawners /&gt;
	 */
	public static final String XML_NODE_SPAWNERS = "spawners"; //$NON-NLS-1$
	/** &lt;spawner /&gt;
	 */
	public static final String XML_NODE_SPAWNER = "spawner"; //$NON-NLS-1$
	/** &lt;entity /&gt;
	 */
	public static final String XML_NODE_ENTITY = "entity"; //$NON-NLS-1$
	/** &lt;frustums /&gt;
	 */
	public static final String XML_NODE_FRUSTUMS = "frustums"; //$NON-NLS-1$
	/** &lt;frustum /&gt;
	 */
	public static final String XML_NODE_FRUSTUM = "frustum"; //$NON-NLS-1$
	/** &lt;body /&gt;
	 */
	public static final String XML_NODE_BODY = "body"; //$NON-NLS-1$
	/** &lt;waypoints /&gt;
	 */
	public static final String XML_NODE_WAYPOINTS = "waypoints"; //$NON-NLS-1$
	/** &lt;waypoint /&gt;
	 */
	public static final String XML_NODE_WAYPOINT = "waypoint"; //$NON-NLS-1$
	/** &lt;goals /&gt;
	 */
	public static final String XML_NODE_GOALS = "goals"; //$NON-NLS-1$
	/** &lt;goal /&gt;
	 */
	public static final String XML_NODE_GOAL = "goal"; //$NON-NLS-1$
	/** &lt;generationLaw /&gt;
	 */
	public static final String XML_NODE_GENERATIONLAW = "generationLaw"; //$NON-NLS-1$
	/** &lt;lawParam /&gt;
	 */
	public static final String XML_NODE_LAWPARAM = "lawParam"; //$NON-NLS-1$
	/** &lt;attributes /&gt;
	 */
	public static final String XML_NODE_ATTRIBUTES = "attributes"; //$NON-NLS-1$
	/** &lt;attr /&gt;
	 */
	public static final String XML_NODE_ATTRIBUTE = "attr"; //$NON-NLS-1$
	/** &lt;probes /&gt;
	 */
	public static final String XML_NODE_PROBES = "probes"; //$NON-NLS-1$
	/** &lt;probe /&gt;
	 */
	public static final String XML_NODE_PROBE = "probe"; //$NON-NLS-1$
	
	//---------------------------------------------------------------------------------
	
	/** perceptionAlgo=""
	 */
	public static final String XML_ATTR_PERCEPTIONALGO = "perceptionAlgo"; //$NON-NLS-1$
	/** perceptionTraversal=""
	 */
	public static final String XML_ATTR_PERCEPTIONTRAVERSAL = "perceptionTraversal"; //$NON-NLS-1$
	/** name=""
	 */
	public static final String XML_ATTR_NAME = "name"; //$NON-NLS-1$
	/** description=""
	 */
	public static final String XML_ATTR_DESCRIPTION = "description"; //$NON-NLS-1$
	/** authors=""
	 */
	public static final String XML_ATTR_AUTHORS = "authors"; //$NON-NLS-1$
	/** version=""
	 */
	public static final String XML_ATTR_VERSION = "version"; //$NON-NLS-1$
	/** date=""
	 */
	public static final String XML_ATTR_DATE = "date"; //$NON-NLS-1$
	/** type=""
	 */
	public static final String XML_ATTR_TYPE = "type"; //$NON-NLS-1$
	/** unit=""
	 */
	public static final String XML_ATTR_UNIT = "unit"; //$NON-NLS-1$
	/** timeStep=""
	 */
	public static final String XML_ATTR_TIMESTEP = "timeStep"; //$NON-NLS-1$
	/** dimension=""
	 */
	public static final String XML_ATTR_DIMENSION = "dimension"; //$NON-NLS-1$
	/** id=""
	 */
	public static final String XML_ATTR_ID = "id"; //$NON-NLS-1$
	/** preBuiltResource=""
	 */
	public static final String XML_ATTR_PREBUILDRESOURCE = "prebuildResource"; //$NON-NLS-1$
	/** place=""
	 */
	public static final String XML_ATTR_PLACE = "place"; //$NON-NLS-1$
	/** x=""
	 */
	public static final String XML_ATTR_X = "x"; //$NON-NLS-1$
	/** y=""
	 */
	public static final String XML_ATTR_Y = "y"; //$NON-NLS-1$
	/** z=""
	 */
	public static final String XML_ATTR_Z = "z"; //$NON-NLS-1$
	/** road=""
	 */
	public static final String XML_ATTR_ROAD = "road"; //$NON-NLS-1$
	/** url=""
	 */
	public static final String XML_ATTR_URL = "url"; //$NON-NLS-1$
	/** minx=""
	 */
	public static final String XML_ATTR_MINX = "minx"; //$NON-NLS-1$
	/** miny=""
	 */
	public static final String XML_ATTR_MINY = "miny"; //$NON-NLS-1$
	/** minz=""
	 */
	public static final String XML_ATTR_MINZ = "minz"; //$NON-NLS-1$
	/** maxx=""
	 */
	public static final String XML_ATTR_MAXX = "maxx"; //$NON-NLS-1$
	/** maxy=""
	 */
	public static final String XML_ATTR_MAXY = "maxy"; //$NON-NLS-1$
	/** maxz=""
	 */
	public static final String XML_ATTR_MAXZ = "maxz"; //$NON-NLS-1$
	/** centerx=""
	 */
	public static final String XML_ATTR_CENTERX = "centerx"; //$NON-NLS-1$
	/** centery=""
	 */
	public static final String XML_ATTR_CENTERY = "centery"; //$NON-NLS-1$
	/** Rx=""
	 */
	public static final String XML_ATTR_RX = "Rx"; //$NON-NLS-1$
	/** Ry=""
	 */
	public static final String XML_ATTR_RY = "Ry"; //$NON-NLS-1$
	/** Sx=""
	 */
	public static final String XML_ATTR_SX = "Sx"; //$NON-NLS-1$
	/** Sy=""
	 */
	public static final String XML_ATTR_SY = "Sy"; //$NON-NLS-1$
	/** groundZero=""
	 */
	public static final String XML_ATTR_GROUNDZERO = "groundZero"; //$NON-NLS-1$
	/** rawGroundZero=""
	 */
	public static final String XML_ATTR_RAWGROUNDZERO = "rawGroundZero"; //$NON-NLS-1$
	/** semantic=""
	 */
	public static final String XML_ATTR_SEMANTIC = "semantic"; //$NON-NLS-1$
	/** class=""
	 */
	public static final String XML_ATTR_CLASS = "class"; //$NON-NLS-1$
	/** agentType=""
	 */
	public static final String XML_ATTR_AGENTTYPE = "agentType"; //$NON-NLS-1$
	/** onleft=""
	 */
	public static final String XML_ATTR_ONLEFT = "onleft"; //$NON-NLS-1$
	/** x1=""
	 */
	public static final String XML_ATTR_X1 = "x1"; //$NON-NLS-1$
	/** x2=""
	 */
	public static final String XML_ATTR_X2 = "x2"; //$NON-NLS-1$
	/** y1=""
	 */
	public static final String XML_ATTR_Y1 = "y1"; //$NON-NLS-1$
	/** y2=""
	 */
	public static final String XML_ATTR_Y2 = "y2"; //$NON-NLS-1$
	/** dx=""
	 */
	public static final String XML_ATTR_DX = "dx"; //$NON-NLS-1$
	/** dy=""
	 */
	public static final String XML_ATTR_DY = "dy"; //$NON-NLS-1$
	/** startAngle=""
	 */
	public static final String XML_ATTR_STARTANGLE = "startAngle"; //$NON-NLS-1$
	/** endAngle=""
	 */
	public static final String XML_ATTR_ENDANGLE = "endAngle"; //$NON-NLS-1$
	/** startDate=""
	 */
	public static final String XML_ATTR_STARTDATE = "startDate"; //$NON-NLS-1$
	/** endDate=""
	 */
	public static final String XML_ATTR_ENDDATE = "endDate"; //$NON-NLS-1$
	/** width=""
	 */
	public static final String XML_ATTR_WIDTH = "width"; //$NON-NLS-1$
	/** height=""
	 */
	public static final String XML_ATTR_HEIGHT = "height"; //$NON-NLS-1$
	/** budget=""
	 */
	public static final String XML_ATTR_BUDGET = "budget"; //$NON-NLS-1$
	/** eyePosition=""
	 */
	public static final String XML_ATTR_EYEPOSITION = "eyePosition"; //$NON-NLS-1$
	/** farDistance=""
	 */
	public static final String XML_ATTR_FARDISTANCE = "farDistance"; //$NON-NLS-1$
	/** sideSize=""
	 */
	public static final String XML_ATTR_SIDESIZE = "sideSize"; //$NON-NLS-1$
	/** verticalSize=""
	 */
	public static final String XML_ATTR_VERTICALSIZE = "verticalSize"; //$NON-NLS-1$
	/** nearDistance=""
	 */
	public static final String XML_ATTR_NEARDISTANCE = "nearDistance"; //$NON-NLS-1$
	/** fieldOfViewHAngle=""
	 */
	public static final String XML_ATTR_HORIZONTALFOVANGLE = "fieldOfViewHAngle"; //$NON-NLS-1$
	/** fieldOfViewVAngle=""
	 */
	public static final String XML_ATTR_VERTICALFOVANGLE = "fieldOfViewVAngle"; //$NON-NLS-1$
	/** radius=""
	 */
	public static final String XML_ATTR_RADIUS = "radius"; //$NON-NLS-1$
	/** time=""
	 */
	public static final String XML_ATTR_TIME = "time"; //$NON-NLS-1$
	/** velocity=""
	 */
	public static final String XML_ATTR_VELOCITY = "velocity"; //$NON-NLS-1$
	/** tangentX=""
	 */
	public static final String XML_ATTR_TANGENTX = "tangentX"; //$NON-NLS-1$
	/** tangentY=""
	 */
	public static final String XML_ATTR_TANGENTY = "tangentY"; //$NON-NLS-1$
	/** tangentZ=""
	 */
	public static final String XML_ATTR_TANGENTZ = "tangentZ"; //$NON-NLS-1$
	/** value=""
	 */
	public static final String XML_ATTR_VALUE = "value"; //$NON-NLS-1$
	/** shapeFile=""
	 */
	public static final String XML_ATTR_SHAPEFILE = "shapeFile"; //$NON-NLS-1$
	/** dBaseFile=""
	 */
	public static final String XML_ATTR_DBASEFILE = "dBaseFile"; //$NON-NLS-1$
	/** direction=""
	 */
	public static final String XML_ATTR_DIRECTION = "direction"; //$NON-NLS-1$
	/** curvilineSize=""
	 */
	public static final String XML_ATTR_CURVILINESIZE = "curvilineSize"; //$NON-NLS-1$
	/** lateralSize=""
	 */
	public static final String XML_ATTR_LATERALSIZE = "lateralSize"; //$NON-NLS-1$
	/** activated=""
	 */
	public static final String XML_ATTR_ACTIVATE = "activate"; //$NON-NLS-1$
	
	//---------------------------------------------------------------------------------
	
	/** "constant".
	 */
	public static final String XML_VALUE_CONSTANT = "constant"; //$NON-NLS-1$
	/** "alignedIndoor".
	 */
	public static final String XML_VALUE_ALIGNEDINDOOR = "alignedIndoor"; //$NON-NLS-1$
	/** "orientedIndoor".
	 */
	public static final String XML_VALUE_ORIENTEDINDOOR = "orientedIndoor"; //$NON-NLS-1$
	/** "repulsion".
	 */
	public static final String XML_VALUE_REPULSION = "repulsion"; //$NON-NLS-1$
	/** "repulsionHeightmap".
	 */
	public static final String XML_VALUE_REPULSIONHEIGHMAP = "repulsionHeightmap"; //$NON-NLS-1$
	/** "simple".
	 */
	public static final String XML_VALUE_SIMPLE = "simple"; //$NON-NLS-1$
	/** "heightmap".
	 */
	public static final String XML_VALUE_HEIGHTMAP = "heightmap"; //$NON-NLS-1$
	/** "any".
	 */
	public static final String XML_VALUE_ANY = "any"; //$NON-NLS-1$
	/** "none".
	 */
	public static final String XML_VALUE_NONE = "none"; //$NON-NLS-1$
	/** Default 1D direction.
	 */
	public static final String XML_VALUE_DEFAULT_DIRECTION = "segment"; //$NON-NLS-1$
	/** Reversed 1D direction.
	 */
	public static final String XML_VALUE_REVERSED_DIRECTION = "reverse"; //$NON-NLS-1$
	/** Default and Reversed 1D direction in same time.
	 */
	public static final String XML_VALUE_BOTH_DIRECTION = "both"; //$NON-NLS-1$
	/** Point.
	 */
	public static final String XML_VALUE_POINT = "point"; //$NON-NLS-1$
	/** Area.
	 */
	public static final String XML_VALUE_AREA = "area"; //$NON-NLS-1$
	/** "step".
	 */
	public static final String XML_VALUE_STEP = "step"; //$NON-NLS-1$
	/** "millisecond".
	 */
	public static final String XML_VALUE_MILLISECOND = "millisecond"; //$NON-NLS-1$
	/** "minute".
	 */
	public static final String XML_VALUE_MINUTE = "minute"; //$NON-NLS-1$
	/** "hour".
	 */
	public static final String XML_VALUE_HOUR = "hour"; //$NON-NLS-1$
	/** "sequential".
	 */
	public static final String XML_VALUE_SEQUENTIAL = "sequential"; //$NON-NLS-1$
	/** "parallel".
	 */
	public static final String XML_VALUE_PARALLEL = "parallel"; //$NON-NLS-1$
	/** "topdown".
	 */
	public static final String XML_VALUE_TOPDOWN = "topdown"; //$NON-NLS-1$
	/** "bottomup".
	 */
	public static final String XML_VALUE_BOTTOMUP = "bottomup"; //$NON-NLS-1$
	
}
