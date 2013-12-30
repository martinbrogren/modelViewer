import com.sun.j3d.utils.picking.*;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.geometry.*;

import javax.imageio.ImageIO;
import javax.media.j3d.*;

import javax.vecmath.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.Enumeration;
import java.util.Vector;
import com.sun.j3d.utils.image.TextureLoader;
import javax.swing.Timer;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
//import com.sun.org.apache.xerces.internal.util.URI;

import org.jdesktop.j3d.utils.view.ViewUtils;
import org.jdesktop.j3d.utils.view.ViewUtils.Axis;

import java.applet.Applet;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.URI;

import java.io.*;

public class appartmentWithColladaApplet extends Applet implements ActionListener, MouseListener {

//	static {	 
//		try {	
//			URL lib_url_l32 = getClass().getResource("/l32");
//			System.out.println("lib url l32: " + lib_url_l32);
//			
//			NativeUtils.loadLibraryFromJar(lib_url_l32 + "/libj3dcore-ogl.so");	 
//		} catch (IOException e) {	 
//			e.printStackTrace(); // This is probably not the best way to handle exception :-)	 
//		}	 
//	}	

	
	
	//Define colors
	private Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
	private Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
	
	private SimpleUniverse universe;

	private AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .0f);
	private float _acSpeed = 0.05f;
	private float _acMax = .5f;
	
	private Timer timer;
	
	private float sign = 1.0f; // going up or down
	
	private int xSize = 256*3;
	private int ySize = xSize;
	
	private Toolkit toolkit;
	private Image image;
	
	private PickCanvas pickCanvas; 
	
	private float alphaMargin = 0.1f;
	
	private boolean useCollada = true;
	
//	private String _colladaFile = "/home/martin/Code/JavaVisualizer/data/OscarVisualSFM/sketchup_model2.dae";
	
//	private String _colladaFile = "/data/sketchup_model2.dae";
//	private String _colladaFile = "/home/martin/Code/JavaVisualizer/data/Oscars_appartment_v01/collada.dae";
//	private String _colladaFile = "/data/collada.dae";
	private String _colladaFile = "/data/Oscars_appartment_v01/collada.dae";
	float _sizeFactor = 100.0f; //For adding cameras
	float _posFactor = 1.0f; //For adding cameras
	boolean _useDegrees = false;
//	float _factor = 100.0f; //For adding cameras
	float _zSign = 1.0f; //When adding cameras
	private Axis axis = Axis.POSITIVE_Y_AXIS;
//	private String _colladaFile = "/home/martin/Code/FlashViewer/TestProject3/bin/geometry/geometry.dae";
//	float _factor = 1.0f;
//	float _zSign = -1.0f;
//	private Axis axis = Axis.NEGATIVE_Y_AXIS;
	private boolean _addViews = true;
//	private String _cameraPath = "/home/martin/Code/JavaVisualizer/data/Oscars_appartment_v01/"; //File must be names views.xml
	private String _cameraPath = "/data/"; //File must be names views.xml
//	private String _cameraPath = "/home/martin/Code/FlashViewer/TestProject3/bin/detail/"; //File must be names views.xml/
	
//	private String _colladaFile = "/home/martin/Code/FlashViewer/TestProject3/bin/geometry/geometry_duck.dae";
//	private String _colladaFile = "/home/martin/Code/JavaVisualizer/data/testModel.dae";
//	private String _colladaFile = "/home/martin/Code/JavaVisualizer/data/testModel4.dae";
//	private Axis axis = Axis.POSITIVE_Z_AXIS;
//	private boolean _addViews = false;
		
	private boolean showViewPath = true;
	private int pathSize = 33;
	private int _translationStart = 0;
	private int _translationStop = 25;
	private int quadStart = 15;
	private int _quadStop = 28;
	private int _fovStart = 29;
	private int _fovStop = pathSize - 1;
	private Vector3d [] viewPathPos = new Vector3d[pathSize];
	private Quat4d [] viewPathQuat = new Quat4d[pathSize];
	private double [] viewPathFOV = new double[pathSize];
	private int viewPathIdx = 0;
	
	URL _dae_url;
	
	public appartmentWithColladaApplet()
	{		

//		try {	
////			String nativeLibPathL32 = NativeUtils.createTempLibraryFromJar("/l32/libj3dcore-ogl.so");
////			nativeLibPathL32 = NativeUtils.createTempLibraryFromJar("/l32/libj3dcore-ogl-cg.so");
//			
//			String nativeLibPathL64 = NativeUtils.createTempLibraryFromJar("/l64/libj3dcore-ogl.so");
////			
//			String nativeLibPathW32 = NativeUtils.createTempLibraryFromJar("/w32/j3dcore-d3d.dll");
//			nativeLibPathW32 = NativeUtils.createTempLibraryFromJar("/w32/j3dcore-ogl.dll");
//			nativeLibPathW32 = NativeUtils.createTempLibraryFromJar("/w32/j3dcore-ogl-cg.dll");
//			nativeLibPathW32 = NativeUtils.createTempLibraryFromJar("/w32/j3dcore-ogl-chk.dll");
////			
////			String nativeLibPathW64 = NativeUtils.createTempLibraryFromJar("/w64/j3dcore-ogl.dll");
//			
////			System.setProperty( "java.library.path", nativeLibPathL32 + File.pathSeparator + nativeLibPathL64 + File.pathSeparator + nativeLibPathW32 + File.pathSeparator + nativeLibPathW64);
////			System.setProperty( "java.library.path", nativeLibPathL32 + File.pathSeparator + nativeLibPathW32);
//			System.setProperty( "java.library.path", nativeLibPathL64 + File.pathSeparator + nativeLibPathW32);
//		} catch (IOException e) {	
//			e.printStackTrace(); // This is probably not the best way to handle exception :-)	 
//		}
//
//		
//		
////		System.setProperty( "java.library.path", lib_url_l32.getPath() + File.pathSeparator + lib_url_l64.getPath() + File.pathSeparator + lib_url_w32.getPath() + File.pathSeparator + lib_url_w64.getPath() + File.pathSeparator + lib_url_mac.getPath());
////		System.setProperty( "java.library.path", lib_url_l32 + File.pathSeparator + lib_url_l64 + File.pathSeparator + lib_url_w32 + File.pathSeparator + lib_url_w64 + File.pathSeparator + lib_url_mac);
////		System.setProperty( "java.library.path", "jar:" + lib_url_l32 + "/nativelibU32.jar!/");
////		System.setProperty( "java.library.path", "");
//		
//		
//		java.lang.reflect.Field fieldSysPath;
//		try {
//			fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
//			fieldSysPath.setAccessible( true );
//			try {
//				fieldSysPath.set( null, null );
//			} catch (IllegalArgumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (SecurityException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (NoSuchFieldException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		System.out.println("LD Library Path:" + System.getProperty("java.library.path"));
		String workingDir = appartmentWithColladaApplet.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		System.out.println("Working directory: " + appartmentWithColladaApplet.class.getProtectionDomain().getCodeSource().getLocation().getFile());
		
		_dae_url = getClass().getResource(_colladaFile);
		System.out.println("collada url: " + _dae_url);
		
		setLayout(new BorderLayout());

		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

		toolkit = Toolkit.getDefaultToolkit();
		image = null;
		
		// Create a Canvas3D using the preferred configuration
		Canvas3D canvas3d = new Canvas3D(config)
		{
			public void postRender()
			{
				if(image == null)
					return;
				//FIX ME. Why is this called twice all the time? Why is the rendering so bad? Virtual machine?
				this.getGraphics2D().drawImage(image, 0, 0, this.getSize().width, this.getSize().height, null);
				this.getGraphics2D().setComposite(ac);
//				System.out.println("ac: " + ac.getAlpha());
				this.getGraphics2D().flush(false);
			}
		};
		
		add("Center", canvas3d);
		
//		canvas3d.setSize(xSize, ySize);

		timer = new Timer(100,this);
		   
		universe = new SimpleUniverse(canvas3d);

		universe.getViewingPlatform().setNominalViewingTransform();
		
		BranchGroup scene = null;
		if(useCollada)
		{
			scene = createSceneGraphFromColladaFile();
		}
		else
		{
			axis = Axis.POSITIVE_Z_AXIS;
			scene = createSceneGraph();
		}
		
		setModelInView(scene);
		
		addLights(scene);
		
		universe.addBranchGraph(scene);
				
		//Add functionality for selection objects
		pickCanvas = new PickCanvas(canvas3d, scene);
		pickCanvas.setMode(PickCanvas.GEOMETRY);
		canvas3d.addMouseListener(this); 

		System.out.println("");
		System.out.println("FIX ME:");
		System.out.println("Modellen är spegelvänd. Var det därför jag var tvungen att lägga till ett minus på z för kamerorna?");
		System.out.println("Modellen roterar kring ena hörnet. Inte så viktigt... men antagligen bara att translatera allt lite.");
		System.out.println("MouseControl funkar lite olika på olika modeller. Inte så viktigt just nu.");
		System.out.println("Enbart azimuth rotation supportad just nu. Lägg till roll och elevation");
		System.out.println("Hur sätter man vertical fov?????");
		System.out.println("Testa på ny model av lägenhet som inte är spegelvänd. Passar bilderna bra in?");
		System.out.println("Grafiken hackar. 3D acceleration not enabled for VM");
		System.out.println("Testa att exportera viewern. Integrera den i webläsare, java applet, eller java webstart");
		System.out.println("Lägg in rätblock och flytta runt i modelen. Möblera");
		System.out.println("Hitta z-switch i colladamodellen. Det verkar inte finnas någon sådan...");
		System.out.println("Skapa olika mode för viewern. Lås frihetsgrader, ha bilderna i lista bredvid modellen, highlighta cameror när man kör mouseover på bilderna.");
		System.out.println("Clean up applet version");
		
		System.out.println("HTML version hittar inte native libs i windows");
		System.out.println("Mac funkar inte. Mouse behaviour is sealed");
		System.out.println("Checka in på nytt repository");
		System.out.println("Lägg in en mäklarsida som bakgrund på htlm versionen");
		
		System.out.println("FIX ME:");
		System.out.println("");
	}

	
	public BranchGroup createSceneGraph() {

		//Setup basics
		TransformGroup boxTransformGroup;
		BranchGroup group = new BranchGroup();

		//Setup initial rotation
		TransformGroup tgRoom = new TransformGroup();
		Transform3D transformRoom1 = new Transform3D();
		tgRoom.setTransform(transformRoom1);

		//Create room
//		float buildingBlocksWidth = 0.01f;
		float roomWidth = 0.5f;
		float roomDepth = 0.7f;
		float roomHeight = 0.2f;
		Appearance ap = new Appearance();
		ap.setMaterial(new Material(white, black, new Color3f(0.9f, 0.9f, 0.9f), black, 1.0f));

		//Create texture from image
		// Set up the texture map
		TextureLoader loader = new TextureLoader("/home/martin/Pictures/Arizona.jpg", "RGB", new Container());
		Texture texture = loader.getTexture();

		// Set up the texture attributes
		TextureAttributes texAttr = new TextureAttributes();
		Appearance apImage = new Appearance();
		apImage.setTexture(texture);
		apImage.setTextureAttributes(texAttr);

		//set up the material
		TexCoordGeneration texCoordGeneration = new TexCoordGeneration();
		apImage.setTexCoordGeneration(texCoordGeneration);

		//Create room using planes from xml-file
		XMLFilePlaneReader xmlReader = new XMLFilePlaneReader();
		Vector<plane> planeVector = new Vector<plane>(50);
		xmlReader.getPlanes(planeVector);

		for(int planeIdx = 0; planeIdx < planeVector.size(); planeIdx++) {
			Box myFloor = new Box(planeVector.get(planeIdx).xdim, planeVector.get(planeIdx).ydim, planeVector.get(planeIdx).zdim, ap);
			TransformGroup tgFloor = new TransformGroup();
			Transform3D transformFloor = new Transform3D();
			Vector3d vectorFloor = new Vector3d( planeVector.get(planeIdx).xtransform, planeVector.get(planeIdx).ytransform, planeVector.get(planeIdx).ztransform);
			transformFloor.setTranslation(vectorFloor);
			tgFloor.setTransform(transformFloor);
			tgFloor.addChild(myFloor);
			tgRoom.addChild(tgFloor);
		}
		
		//Add a camera to the scene
		Cone myCamera1 = new Cone(0.05f, 0.1f);
		view viewData = new view();
		Canvas3D canvas3d = universe.getCanvas();
		canvas3d = universe.getCanvas();
		View v = canvas3d.getView();
		double currentFieldOfView = v.getFieldOfView();
		viewData.fovHori = (float)(currentFieldOfView*180.0/Math.PI);
		viewData.fovVert = (float)(currentFieldOfView*180.0/Math.PI);
		viewData.imageFile = "/home/martin/Code/FlashViewer/TestProject3/bin/detail/vrum_mot_balkong.JPG";
		myCamera1.setUserData(viewData);
		myCamera1.setAppearance(apImage);
		
		TransformGroup tgCamera11 = new TransformGroup();
		Transform3D transformCamera11 = new Transform3D();
		transformCamera11.rotX(Math.PI/2);
		tgCamera11.setTransform(transformCamera11);
		tgCamera11.addChild(myCamera1);
		
		TransformGroup tgCamera12 = new TransformGroup();
		Transform3D transformCamera12 = new Transform3D();
		transformCamera12.rotY(Math.PI/4);
		Vector3d vectorCamera12 = new Vector3d( 2*roomWidth/3, 0f, 0f);
		transformCamera12.setTranslation(vectorCamera12);
		tgCamera12.setTransform(transformCamera12);
		
		tgCamera12.addChild(tgCamera11);
		tgRoom.addChild(tgCamera12);

		//Add a camera to the scene
		Cone myCamera2 = new Cone(0.05f, 0.1f);
		myCamera2.setUserData(viewData);
		myCamera2.setAppearance(apImage);

		TransformGroup tgCamera21 = new TransformGroup();
		Transform3D transformCamera21 = new Transform3D();
		transformCamera21.rotX(Math.PI/2);
		tgCamera21.setTransform(transformCamera21);
		tgCamera21.addChild(myCamera2);

		TransformGroup tgCamera22 = new TransformGroup();
		Transform3D transformCamera22 = new Transform3D();
		transformCamera22.rotY(-Math.PI/4*3);
		Vector3d vectorCamera22 = new Vector3d( -roomWidth/3, roomHeight/2, roomDepth/2);
		transformCamera22.setTranslation(vectorCamera22);
		tgCamera22.setTransform(transformCamera22);

		tgCamera22.addChild(tgCamera21);
		tgRoom.addChild(tgCamera22);

		//Enable mouse control
		boxTransformGroup = new TransformGroup();
		
		boxTransformGroup.addChild(tgRoom);
		
		Transform3D transformAll = new Transform3D();
		transformAll.setRotation(new AxisAngle4d(1.0, .0, .0, Math.PI/2));
		Vector3d vectorAll = new Vector3d( .0f, .0f, .0f);
		transformAll.setTranslation(vectorAll);
		boxTransformGroup.setTransform(transformAll);
	
		group.addChild(boxTransformGroup);
				
		addFunctionalityToChildren(group, true, false, null);

		return group;
	}
	
	public BranchGroup createSceneGraphFromColladaFile() {
		BranchGroup scene = new BranchGroup();
		DAELoader daeloader = new DAELoader();
		Scene colladaScene = null;
		boolean addMouseControl = true;
		try {
//			colladaScene = daeloader.load(_colladaFile);
			colladaScene = daeloader.load(_dae_url);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncorrectFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		scene = colladaScene.getSceneGroup();

		addFunctionalityToChildren(scene, addMouseControl, _addViews, _cameraPath);
		
		return scene;
	}
	
	public void addFunctionalityToChildren(BranchGroup scene, boolean addMouseControl, boolean addViews, String cameraPath)
	{
//		System.out.println("Number children: " + scene.numChildren());
		Enumeration enum2 = scene.getAllChildren();
		Node obj = null;
		while(enum2.hasMoreElements() != false)
		{
			obj = (Node) enum2.nextElement();
//			System.out.println("Class: " + obj.getClass().getName());
			if(addViews)
				addCameras(obj, cameraPath);
			if(addMouseControl)
				addMouseControlToTransformGroup(obj);
		}		
	}
	
	public void setModelInView(BranchGroup scene)
	{
		// Ensure the entire model is in view
		ViewingPlatform viewingPlatform = universe.getViewingPlatform();
		double fov = viewingPlatform.getViewers()[0].getView().getFieldOfView();
		TransformGroup viewTG = viewingPlatform.getViewPlatformTransform();
		double distance = ViewUtils.setViewpoint(viewTG, scene.getBounds(), fov, axis);
		System.out.println("distance: " + distance);
		viewingPlatform.getViewers()[0].getView().setFrontClipDistance(0.001);
		viewingPlatform.getViewers()[0].getView().setBackClipDistance(distance*1.25);
	}
	
	public void addLights(BranchGroup scene)
	{
		float lightFactor = 1.0f;
		BoundingSphere lightBounds = (BoundingSphere)scene.getBounds();
		double lightDist = lightBounds.getRadius()*10;
		
		float lightStrength = lightFactor * .5f;
		float lightStrengthR = lightStrength;
		float lightStrengthG = lightStrength;
		float lightStrengthB = lightStrength;
		Color3f light1Color = new Color3f(lightStrengthR, lightStrengthG, lightStrengthB);
		Vector3f light1Direction = new Vector3f((float)lightDist, .0f, .0f);
		DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
		light1.setInfluencingBounds(lightBounds);
		scene.addChild(light1);

		lightStrength = lightFactor * .4f;
		lightStrengthR = lightStrength;
		lightStrengthG = lightStrength;
		lightStrengthB = lightStrength;
		Color3f light2Color = new Color3f(lightStrengthR, lightStrengthG, lightStrengthB);
		Vector3f light2Direction = new Vector3f(-(float)lightDist, .0f, .0f);
		DirectionalLight light2 = new DirectionalLight(light2Color, light2Direction);
		light2.setInfluencingBounds(lightBounds);
		scene.addChild(light2);

		lightStrength = lightFactor * .6f;
		lightStrengthR = lightStrength;
		lightStrengthG = lightStrength;
		lightStrengthB = lightStrength;
		Color3f light3Color = new Color3f(lightStrengthR, lightStrengthG, lightStrengthB);
		Vector3f light3Direction = new Vector3f(.0f, -(float)lightDist, .0f);
		DirectionalLight light3 = new DirectionalLight(light3Color, light3Direction);
		light3.setInfluencingBounds(lightBounds);
		scene.addChild(light3);

		lightStrength = lightFactor * .45f;
		lightStrengthR = lightStrength;
		lightStrengthG = lightStrength;
		lightStrengthB = lightStrength;
		Color3f light4Color = new Color3f(lightStrengthR, lightStrengthG, lightStrengthB);
		Vector3f light4Direction = new Vector3f(.0f, (float)lightDist, .0f);
		DirectionalLight light4 = new DirectionalLight(light4Color, light4Direction);
		light4.setInfluencingBounds(lightBounds);
		scene.addChild(light4);

		lightStrength = lightFactor * .55f;
		lightStrengthR = lightStrength;
		lightStrengthG = lightStrength;
		lightStrengthB = lightStrength;
		Color3f light5Color = new Color3f(lightStrengthR, lightStrengthG, lightStrengthB);
		Vector3f light5Direction = new Vector3f(.0f, .0f, -(float)lightDist);
		DirectionalLight light5 = new DirectionalLight(light5Color, light5Direction);
		light5.setInfluencingBounds(lightBounds);
		scene.addChild(light5);

		lightStrength = lightFactor * .35f;
		lightStrengthR = lightStrength;
		lightStrengthG = lightStrength;
		lightStrengthB = lightStrength;
		Color3f light6Color = new Color3f(lightStrengthR, lightStrengthG, lightStrengthB);
		Vector3f light6Direction = new Vector3f(.0f, .0f, (float)lightDist);
		DirectionalLight light6 = new DirectionalLight(light6Color, light6Direction);
		light6.setInfluencingBounds(lightBounds);
		scene.addChild(light6);

		// Set up the ambient light
		lightStrength = 1.0f;
		lightStrengthR = lightStrength;
		lightStrengthG = lightStrength;
		lightStrengthB = lightStrength;
		Color3f ambientColor = new Color3f(lightStrengthR, lightStrengthG, lightStrengthB);
		AmbientLight ambientLightNode = new AmbientLight(ambientColor);
		ambientLightNode.setInfluencingBounds(lightBounds);
		scene.addChild(ambientLightNode);		
	}

	public void addCameras(Node obj, String cameraPath) {
		if(obj.getClass().getName() != "javax.media.j3d.TransformGroup")
			return;

		TransformGroup tg = (TransformGroup)obj;
		//Create cameras using views from xml-file
		XMLFileViewsReader xmlReader = new XMLFileViewsReader();
		Vector<view> viewVector = new Vector<view>(50);
		xmlReader.getViews(cameraPath, viewVector);
		
		Appearance ap = new Appearance();
		ap.setMaterial(new Material(black, black, new Color3f(0.02f, 0.02f, 0.02f), new Color3f(0.2f, 0.2f, 0.2f), 1.0f));

		for(int viewIdx = 0; viewIdx < viewVector.size(); viewIdx++) {
			
			//Add frustum. OBS! Size is not consistent with fov
			Cone newCone = new Cone(.25f * _sizeFactor, .4f* _sizeFactor);
			newCone.setAppearance(ap);
			newCone.setUserData(viewVector.get(viewIdx));
			
			
			//Set camera box behind objective
			TransformGroup tgCamera1 = new TransformGroup();
			Transform3D transformCamera1 = new Transform3D();
			if(_zSign > 0)
				transformCamera1.rotY(Math.PI);
			tgCamera1.setTransform(transformCamera1);
			tgCamera1.addChild(newCone);

			
			
			TransformGroup tgCamera = new TransformGroup();
			Transform3D transformCamera = new Transform3D();
			Vector3d vectorCamera = new Vector3d( viewVector.get(viewIdx).x, viewVector.get(viewIdx).y, _zSign*viewVector.get(viewIdx).z); //OBS!! FIX ME. Added minus to z to get correct position. The whole model is mirrored as well. Might be connected.
			vectorCamera.scale(_posFactor);
			if(_useDegrees)
			{
				transformCamera.rotZ(viewVector.get(viewIdx).azimuth*Math.PI/180.0 + Math.PI); //OBS!! Added extra PI to get correct rotation	
			}
			else
			{
				transformCamera.rotZ(viewVector.get(viewIdx).azimuth + Math.PI/2); //OBS!! Added extra PI/2 to get correct rotation
			}
			//FIX ME. Only setting azimuth now. Rotations effect each other. Wait for example data from Oscar and solve it then.
			//					transformCamera.rotX(viewVector.get(viewIdx).elevation*Math.PI/180.0);
			//					transformCamera.rotY(viewVector.get(viewIdx).roll*Math.PI/180.0);
			//					Vector3d eulerAngles = new Vector3d(viewVector.get(viewIdx).elevation*Math.PI/180.0, viewVector.get(viewIdx).roll*Math.PI/180.0, viewVector.get(viewIdx).azimuth*Math.PI/180.0 + Math.PI);
			//					transformCamera.setEuler(eulerAngles);
			if(viewVector.get(viewIdx).elevation != 0 || viewVector.get(viewIdx).roll != 0)
			{
				System.out.println("Not supporting other rotations than azimuth yet. FIX ME!!!!!");
			}
			transformCamera.setTranslation(vectorCamera);
			tgCamera.setTransform(transformCamera);
//			tgCamera.addChild(newCone);
			tgCamera.addChild(tgCamera1);
			tg.addChild(tgCamera);

			//Add a camera box too make it look like a camera
			Box newBox = new Box(.3f*_sizeFactor, .15f*_sizeFactor, .2f*_sizeFactor, ap);
			newBox.setPickable(false);
			
			//Set camera box behind objective
			TransformGroup tgBox1 = new TransformGroup();
			Transform3D transformBox1 = new Transform3D();
			Vector3d translationBox1 = new Vector3d(.0f, .1f, .0f);
			translationBox1.scale(_sizeFactor);
			if(_zSign > 0)
				transformBox1.rotY(Math.PI);
			transformBox1.setTranslation(translationBox1);
			tgBox1.setTransform(transformBox1);
			tgBox1.addChild(newBox);
						
			//Rotate and translate box to correct position
			TransformGroup tgBox = new TransformGroup();
			Transform3D transformBox = new Transform3D();
			Vector3d translationBox = new Vector3d(viewVector.get(viewIdx).x, viewVector.get(viewIdx).y, _zSign*viewVector.get(viewIdx).z);
			translationBox.scale(_posFactor);
			if(_useDegrees)
			{
				transformBox.rotZ(viewVector.get(viewIdx).azimuth*Math.PI/180.0 + Math.PI); //OBS!! Added extra PI to get correct rotation
			}
			else
			{
				transformBox.rotZ(viewVector.get(viewIdx).azimuth + Math.PI/2); //OBS!! Added extra PI/2 to get correct rotation
			}
			transformBox.setTranslation(translationBox);
			tgBox.setTransform(transformBox);
			tgBox.addChild(tgBox1);
			tg.addChild(tgBox);
		}		
	}

	
	public void addMouseControlToTransformGroup(Node obj) {
		if(obj.getClass().getName() != "javax.media.j3d.TransformGroup")
			return;

		TransformGroup tg = (TransformGroup)obj;
		Enumeration enum2 = tg.getAllChildren();
		Node child = null;
		TransformGroup boxTransformGroup = new TransformGroup();
		while(enum2.hasMoreElements() != false)
		{
			child = (Node) enum2.nextElement();

			tg.removeChild(child);
			boxTransformGroup.addChild(child);
		}
		//Enable mouse control
		MouseRotate behaviorRotate = new MouseRotate();
		MouseTranslate behaviorTranslate = new MouseTranslate();
		MouseWheelZoom behaviorWheelZoom = new MouseWheelZoom();
		boxTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		boxTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		behaviorRotate.setTransformGroup(boxTransformGroup);
		behaviorTranslate.setTransformGroup(boxTransformGroup);
		behaviorWheelZoom.setTransformGroup(boxTransformGroup);
		boxTransformGroup.addChild(behaviorRotate);
		boxTransformGroup.addChild(behaviorTranslate);
		boxTransformGroup.addChild(behaviorWheelZoom);
		BoundingSphere bounds2 = new BoundingSphere(new Point3d(0.0,0.0,0.0), 10000.0);
		behaviorRotate.setSchedulingBounds(bounds2);	
		behaviorTranslate.setSchedulingBounds(bounds2);
		behaviorWheelZoom.setSchedulingBounds(bounds2);

		tg.addChild(boxTransformGroup);		
	}

	public void actionPerformed(ActionEvent e ) {

		//Perform following actions:
		//Click on camera object, view position is then interpolated towards the camera object position. 
		//After this an image is gradually displayed on top of the 3D-model. When clicking on the image 
		//these actions is performed again but in the other direction. 
		
		if( showViewPath && (
				(sign < 0 && viewPathIdx > 0 && Math.abs(ac.getAlpha()) < 1e-5) || 
				(sign > 0 && viewPathIdx < (viewPathPos.length-1)) 
				) )
		{
			//Interpolate view position
			TransformGroup ViewTransformGroup = universe.getViewingPlatform().getViewPlatformTransform();
			Transform3D viewTransform = new Transform3D();
			ViewTransformGroup.getTransform(viewTransform);
			
			viewPathIdx += sign * 1;
			
			viewTransform.setRotation(viewPathQuat[viewPathIdx]);
			viewTransform.setTranslation(viewPathPos[viewPathIdx]);
			
			
			ViewTransformGroup.setTransform(viewTransform);
//			System.out.format("New view in action set: Pos: %.2f %.2f %.2f  , quat: %.2f %.2f %.2f %.2f %n", newViewPosition.x, newViewPosition.y, newViewPosition.z, newViewQuat.x, newViewQuat.y, newViewQuat.z, newViewQuat.w);
			
			Canvas3D canvas3d = universe.getCanvas();
			canvas3d = universe.getCanvas();
			View v = canvas3d.getView();
			v.setFieldOfView(viewPathFOV[viewPathIdx]);
//			System.out.format("FOV: %f %n", v.getFieldOfView());
			
			if(viewPathIdx == 0 && sign < 0)
			{	
				//Stop action when view position has been moved back to original position
				timer.stop();
				sign = sign*-1.0f;				
			}
		}
		
		if(showViewPath == false || viewPathIdx == (viewPathPos.length-1) )
		{
			ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0.0f, Math.min(ac.getAlpha() + _acSpeed*sign, 1.0f)));
			universe.getCanvas().postRender();
			if(Math.abs(ac.getAlpha() - _acMax) < 1e-5 || Math.abs(ac.getAlpha()) < 1e-5)
			{
				if(showViewPath == false)
				{
					timer.stop();
					sign = sign*-1.0f;
				}
				else if(sign > 0)
				{
					//Stop actions when image has been displayed fully
					timer.stop();
					sign = sign*-1.0f;
				}

				//Set alpha to 0 or 1 as steady state.
				if(ac.getAlpha() < _acMax/2)
				{
					ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f);
					universe.getCanvas().postRender();
				}
				else
				{
					ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, _acMax);
					universe.getCanvas().postRender();	
				}
			}
		}
	}
	
	 
	public void mouseExited(MouseEvent e)
	{

	}

	public void mouseEntered(MouseEvent e)
	{

	}

	public void mousePressed(MouseEvent e)
	{

	} 

	public void mouseReleased(MouseEvent e)
	{

	}

	public void mouseClicked(MouseEvent e)
	{			
		if(ac.getAlpha() > (_acMax - alphaMargin)) {
			//Start timer if image is displayed
			if (!timer.isRunning()) {
				timer.start();
			}
			return;
		}

		//If 3D model is displayed, check if camera object is clicked. 
	    pickCanvas.setShapeLocation(e);
	    PickResult result = pickCanvas.pickClosest();
	    if (result != null) {
	       Primitive p = (Primitive)result.getNode(PickResult.PRIMITIVE);
	       if(p != null && p.getClass().getName() == "com.sun.j3d.utils.geometry.Cone" && ac.getAlpha() < alphaMargin) {
	    	   //Camera object is clicked. Get object position and rotation and calculate view path from current view position and rotation to the camera position and rotation
	    	   Transform3D cameraObjTransform = new Transform3D(); 
	    	   Vector3d cameraObjPosition = new Vector3d();
	    	   p.getLocalToVworld(cameraObjTransform);
	    	   cameraObjTransform.get(cameraObjPosition);
	    	   Quat4d cameraObjQuat = new Quat4d();
	    	   cameraObjTransform.get(cameraObjQuat);
	    	   	    	   
	    	   Transform3D viewTransform = new Transform3D();
	    	   TransformGroup ViewTransformGroup = universe.getViewingPlatform().getViewPlatformTransform();
	    	   ViewTransformGroup.getTransform(viewTransform);
	    	   Vector3d viewPosition = new Vector3d();
	    	   viewTransform.get(viewPosition);
	    	   Quat4d viewQuat = new Quat4d();
	    	   viewTransform.get(viewQuat);
	    	   
	    	   //Get image for the current view
	    	   Object tmpView = p.getUserData();
	    	   view coneView = (view)tmpView;
	    	   
	    	   System.out.println("image: " + coneView.imageFile);
	    	  
	    	   try {
	    		   image = ImageIO.read(getClass().getResourceAsStream(coneView.imageFile));
	    	   } catch (IOException ee) {
	               // TODO Auto-generated catch block
	               ee.printStackTrace();
	           }

	    	   
	    	   Canvas3D canvas3d = universe.getCanvas();
	    	   canvas3d = universe.getCanvas();
	    	   View v = canvas3d.getView();
	    	   double currentFieldOfView = v.getFieldOfView();
	    	   double newFieldOfView = coneView.fovHori*Math.PI/180.0f;
//	    	   System.out.println("currentFieldOfView: " + currentFieldOfView + ", newFieldOfView: " + newFieldOfView);
	    	   if(coneView.fovHori != coneView.fovVert)
	    	   {
	    		   System.out.println("Physical width: " + canvas3d.getPhysicalWidth() + ", physical height: " + canvas3d.getPhysicalHeight());
		    	   canvas3d.setSize(canvas3d.getWidth(), canvas3d.getHeight() + 200);
		    	   System.out.println("Physical width: " + canvas3d.getPhysicalWidth() + ", physical height: " + canvas3d.getPhysicalHeight());
	    		   System.out.println("Vertical fov not set. FIX ME");
	    	   } 	   
	    	   
	    	   
	    	   Quat4d rotationQuat = new Quat4d();
	    	   rotationQuat.set(new AxisAngle4d(1.0, .0, .0, -90.0*Math.PI/180.0));
	    	   rotationQuat.normalize();
	    	   cameraObjQuat.mul(rotationQuat);
	    	   cameraObjQuat.normalize();
	    	   
//	    	   System.out.format("cameraObjPosition: %.2f %.2f %.2f, viewPosition: %.2f %.2f %.2f %n", cameraObjPosition.x, cameraObjPosition.y, cameraObjPosition.z, viewPosition.x, viewPosition.y, viewPosition.z);
//	    	   System.out.format("cameraObjQuat: %.2f %.2f %.2f %.2f, viewQuat: %.2f %.2f %.2f %.2f %n", cameraObjQuat.x, cameraObjQuat.y, cameraObjQuat.z, cameraObjQuat.w, viewQuat.x, viewQuat.y, viewQuat.z, viewQuat.w);
	    	   
	    	   for(int i = 0; i < viewPathPos.length; i++)
	    	   {
	    		   if(viewPathPos[i] == null)
	    			   viewPathPos[i] = new Vector3d();
	    		   viewPathPos[i].interpolate(viewPosition, cameraObjPosition, Math.min(1.0, Math.max(0.0, (double)(i - _translationStart) / (double)(_translationStop - _translationStart))) );
	    		   
	    		   if(viewPathQuat[i] == null)
	    			   viewPathQuat[i] = new Quat4d();
	    		   viewPathQuat[i].interpolate(viewQuat, cameraObjQuat, Math.min(1.0, Math.max(0.0, (double)(i - quadStart) / (double)(_quadStop - quadStart))) );
	    		   
	    		   viewPathFOV[i] = currentFieldOfView + (newFieldOfView - currentFieldOfView)*Math.min(1.0, Math.max(0.0, (double)(i - _fovStart) / (double)(_fovStop - _fovStart)));
	    	   }
	    	   viewPathIdx = 0;
	    	   
	    	   if (!timer.isRunning()) {
	    		   timer.start();
	    	   }
	       }
	    }	        
	}
	
	public static void main( String[] args ) {
		appartmentWithColladaApplet appartment = new appartmentWithColladaApplet();
		MainFrame mf = new MainFrame(appartment, appartment.xSize, appartment.ySize);
	}

} // end of class Appartment