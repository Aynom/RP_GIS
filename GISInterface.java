package rp_GIS;


/**
 * Title:        RP_GIS
 * Description:  The GIS interface of RationalPeak Model
 * Copyright:    Copyright (c) 2009
 * @author Aynom Teweldebrhan
 * @version 1.0
 */

import rationalpeak.Model.GUI.*;
import rationalpeak.Help.HelpDialog;

import java.net.URL;
import java.io.*;

import com.vstl.swing.jbchart.*;
import java.util.Vector;
import java.util.StringTokenizer;

import uk.ac.leeds.ccg.geotools.*;
import uk.ac.leeds.ccg.widgets.Gazetteer;
import uk.ac.leeds.ccg.widgets.ToolBar;
import uk.ac.leeds.ccg.widgets.*;
import uk.ac.leeds.ccg.dataentry.PointEntryTool;
import uk.ac.leeds.ccg.widgets.ThemePanel;

//import uk.ac.leeds.ccg.raster.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GISInterface extends JFrame {

  JPanel contentPane;
  JMenuBar menuBar1 = new JMenuBar();
  JMenuBar menuBar2 = new JMenuBar();

  JMenu menuFile = new JMenu();
  JMenuItem menuFileNew = new JMenuItem();
  JMenuItem menuFilePrint = new JMenuItem();
  JMenuItem menuFileSaveAs = new JMenuItem();
  JMenuItem menuFileExit = new JMenuItem();

  JMenu menuAddLayer = new JMenu();
  JMenuItem menuAddContour = new JMenuItem();
  JMenuItem menuAddStream = new JMenuItem();
  JMenuItem menuAddLandCover = new JMenuItem();

  JMenu menuDraw = new JMenu();
  JMenuItem menuDrawNew = new JMenuItem();
  JMenuItem menuDrawPoint = new JMenuItem();
  JMenuItem menuDrawLine = new JMenuItem();
  JMenuItem menuDrawPolygon = new JMenuItem();

  JMenu menuHelp = new JMenu();
  JMenuItem menuHelpAbout = new JMenuItem();
  JMenuItem menuHelpRationalPeak = new JMenuItem();

  JToolBar toolBar = new JToolBar();

  Viewer mapViewer = new Viewer();

  ToolBar toolbar = new ToolBar(mapViewer, true);

  ZoomLevelPicker zLevel = new ZoomLevelPicker(mapViewer);
  PointLayer pLayer = new PointLayer(true);
  GeoPoint gPoint = new GeoPoint();
  PointEntryTool peTool = new PointEntryTool(pLayer);

  ThemePanel themePanel = new ThemePanel(mapViewer);

  JPanel keys = new JPanel();

  XYDisplay xyd=new XYDisplay();

  JLabel statusBar = new JLabel();

  BorderLayout borderLayout1 = new BorderLayout();

  JScrollPane jScrollPane1 = new JScrollPane();

  JFileChooser jFileChooser1 = new JFileChooser();
  String currFileName = null;

  GridLayout gridLayout1 = new GridLayout();

  JTextField x_CoordValue = new JTextField();
  JTextField y_CoordValue = new JTextField();

  public GISInterface() {
    this.pack();
    try {
      start();
      this.show();
//          JOptionPane.showMessageDialog(this, "First need to load a base map using the AddLayer Menu",
//                                         "RationalPeak", JOptionPane.INFORMATION_MESSAGE) ;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

    //for use in SelectionaWriter
  public GISInterface(JLabel outpDest/*this argument is currently serving only for differentiating with the main constructor*/) {

      Toolkit toolkit = Toolkit.getDefaultToolkit();
      URL iconURL = getClass().getResource("/Images/RPIcon.gif");
      Image frameIcon = toolkit.getImage(iconURL);
      this.setIconImage(frameIcon);

  }


  // for use when running RP_GIS as a stand alone program 
  public static void main(String[] args) {
    try  {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      GISInterface RPGIS = new GISInterface();
    }
    catch (Exception e) {
      System.out.println("An error has occured.");
      System.out.println(e.getClass().getName()+ " :   " + e.getMessage());
      e.printStackTrace(System.out);

      /* Exit the program. */
      System.out.println("Program closed.");
      System.exit(0);
    }
  }


  //Component initialization
  private void start() throws Exception  {

      contentPane = (JPanel) this.getContentPane();

      this.setSize(new Dimension(600, 450));

      contentPane.setLayout(borderLayout1);
      this.setTitle("RationaPeak: GIS_Interface (RP_GIS)");
      statusBar.setText(" ");

      menuFile.setText("File");
      menuFileNew.setText("New");
      menuFileNew.addActionListener(new GISInterface_menuFileNew_actionAdapter(this));
      menuFileSaveAs.setText("Save As");
      menuFileSaveAs.addActionListener(new GISInterface_menuFileSaveAs_actionAdapter(this));
      menuFileExit.setText("Exit");
      menuFileExit.addActionListener(new GISInterface_menuFileExit_ActionAdapter(this));

      menuAddLayer.setText("Add Layer");
      menuAddContour.setText("Contour");
      menuAddContour.addActionListener(new GISInterface_menuAddContour_actionAdapter(this));
      menuAddStream.setText("Stream");
      menuAddStream.addActionListener(new GISInterface_menuAddStream_actionAdapter(this));
      menuAddLandCover.setText("LandCover");
      menuAddLandCover.addActionListener(new GISInterface_menuAddLandCover_actionAdapter(this));

      menuDraw.setText("Draw");
      menuDrawNew.setText("New");
      menuDrawNew.addActionListener(new GISInterface_menuDrawNew_actionAdapter(this));
      menuDrawPoint.setText("Point");
      menuDrawPoint.addActionListener(new GISInterface_menuDrawPoint_actionAdapter(this));
      menuDrawLine.setText("Line");
      menuDrawLine.addActionListener(new GISInterface_menuDrawLine_actionAdapter(this));
      menuDrawPolygon.setText("Polygon");
      menuDrawPolygon.addActionListener(new GISInterface_menuDrawPolygon_actionAdapter(this));

      menuHelp.setText("Help");
      menuHelpAbout.setText("About");
      menuHelpAbout.addActionListener(new GISInterface_menuHelpAbout_ActionAdapter(this));

      menuHelpRationalPeak.setText("RationalPeak");
      menuHelpRationalPeak.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        helpRationalPeak_actionPerformed(e);
      }
    });

      mapViewer.addMouseMotionListener(xyd);
      Gazetteer gaz = new Gazetteer("", "", mapViewer);

      mapViewer.addViewerClickedListener(new uk.ac.leeds.ccg.geotools.ViewerClickedListener() {
      public void viewerClicked(uk.ac.leeds.ccg.geotools.ViewerClickedEvent  e) {
        mapViewer_mouseClicked(e);
      }
      });


      toolbar.addTool(gaz);
      toolbar.addTool(peTool);

      toolBar.add(toolbar);//add the mapviewer tool bar to the frame tool bar
      toolBar.add(xyd);
      zLevel.setLevelList(new double[]{50,75,100,150,200,250,300,400,500,1000,2000});
      zLevel.setCurrentZoomLevel(100);
      toolBar.add(zLevel);

      menuFile.add(menuFileNew);
      menuFile.add(menuFileSaveAs);
      menuFile.add(menuFilePrint);
      menuFile.addSeparator();
      menuFile.add(menuFileExit);

      menuAddLayer.add(menuAddContour);
      menuAddLayer.add(menuAddStream);
      menuAddLayer.add(menuAddLandCover);

      menuDraw.add(menuDrawNew);
      menuDraw.add(menuDrawPoint);
      menuDraw.add(menuDrawLine);
      menuDraw.add(menuDrawPolygon);

      menuHelp.add(menuHelpAbout);
      menuHelp.add(menuHelpRationalPeak);

      menuBar1.add(menuFile);
      menuBar1.add(menuAddLayer);
      menuBar1.add(menuDraw);
      menuBar1.add(menuHelp);

      this.setJMenuBar(menuBar1);
      contentPane.add(toolBar, BorderLayout.SOUTH);
      contentPane.add(statusBar, BorderLayout.NORTH);

      contentPane.add(jScrollPane1, BorderLayout.EAST);
      jScrollPane1.getViewport().add(themePanel, null);

      Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
      int screenHeight = screenDimension.height;
      int screenWidth = screenDimension.width;
      int frameHeight = screenHeight * 4/5;
      int frameWidth = screenWidth * 4/5;
      Dimension frameDimension = new Dimension(frameWidth, frameHeight);
      this.setSize(frameDimension);

      mapViewer.setBackground(Color.white);
      contentPane.add(mapViewer, BorderLayout.CENTER);
      mapViewer.setPreferredSize(new Dimension(frameWidth * 1/2, frameHeight * 1/2));
      contentPane.add(themePanel,BorderLayout.EAST);

      loadDefaultBaseMap();
  }


  void loadDefaultBaseMap(){
//        GeoPoint gPoint = new GeoPoint(492795.82,1695644.66);//set the default geopoint center of Asmara
//        PointLayer pointLayer = new PointLayer();
//        pointLayer.addGeoPoint(gPoint);
//        Shader sh = new RampShader(0.0,10.0);
//        sh.setMissingValueCode(0.0);
//        Theme pointTheme = new Theme(pointLayer);
//        mapViewer.addTheme(pointTheme);


  }


   void mapViewer_mouseClicked(uk.ac.leeds.ccg.geotools.ViewerClickedEvent e) {
   //System.out.println("newMenuToDrawNewPointCalled is  "+newMenuToDrawNewPointCalled);
       if(newMenuToDrawNewPointCalled||newMenuToDrawNewLineCalled||newMenuToDrawNewPolygonCalled){//any spatial analysis need to start using the new submenu of the draw menu
        int totVertex = pLayer.getGeoShapes().capacity()+1;
        int newVertexNo = totVertex - startVertex;
        statusBar.setText("Total vertex number is:  "+totVertex+" and new vertex number is:   "+newVertexNo);
     }else if(baseFileAvailable){//if did not use the new submenu of draw menu and if basefile is not loaded
        JOptionPane.showMessageDialog(this, "Use the New Submenu of the Draw Menu to start any spatial analysis",
                                       "RationalPeak: RP_GIS", JOptionPane.INFORMATION_MESSAGE) ;
           }else{// if it did not start with the new submenu of draw menu but basemap is loaded
        JOptionPane.showMessageDialog(this, "First need to load a base map using the AddLayer Menu",
                                       "RationalPeak: RP_GIS", JOptionPane.INFORMATION_MESSAGE) ;

           }
    }



  String currentDirectory = "";

  void fileOpen() {
  try{
      currentDirectory = System.getProperty("user.dir");
     }
    catch (Exception e)  {
      e.printStackTrace();
      //exitApplication();
    }
  File defaultOpenFile = new File(currentDirectory + "/Input_Output/contour.shp");
  jFileChooser1.setSelectedFile(defaultOpenFile);

  String newFileName = "";

   //System.out.println("the file absolute file name is    "+defaultOpenFile.getName());

    // Use the OPEN version of the dialog, test return for Approve/Cancel
 if (JFileChooser.APPROVE_OPTION == jFileChooser1.showOpenDialog(this)) {

      fileName = jFileChooser1.getSelectedFile().getPath();

         // exclude the extensions
         for(int z = 0;z<=fileName.length()-1;z++){
             String charOfFileName = String.valueOf(fileName.charAt(z));
            if(!charOfFileName.equalsIgnoreCase(".")){//if the specific character is not fullstope consider the character as part of the path of the file name
            newFileName = newFileName+charOfFileName;
                  if(!charOfFileName.equalsIgnoreCase("\\")){//consider the folder or file name as specific file name if it doesn't end with backward slash
                   specificFileName=specificFileName+charOfFileName;
                  }else{//otherwise disregard the previously considered as specific file name so as to start considering afresh
                   specificFileName = "";
                  }//end of if the specific character is different than \
             }else{
             fileName = newFileName;
             }

          }
    }
    this.repaint();
  }//end of file open method


  boolean baseFileAvailable = false;//used to check if a base file is first loaded before any spatial analysis attempt
  Theme contourTheme;
  Theme streamsTheme;
  String fileName = "";
  String specificFileName = "";
  boolean gridOption = false;//an option to avoid opening the grid theme more than once

  // Open named file; read shapefile and report to statusBar.
  void addContourLayer()  {
    try
    {
      fileOpen();
      int fileNameLen = fileName.length();
      System.out.println("the opened file's path name is  "+fileName+" and the specific file's name is   "+specificFileName);
      URL urlContour = new URL("file:/"+fileName);
      ShapefileReader contourReader = new ShapefileReader(urlContour);
      contourTheme = contourReader.getTheme();
      contourTheme.setName(specificFileName);
      GeoData tipsContour = contourReader.readData("ELEV");
      contourTheme.setTipData(tipsContour);
      ShadeStyle contourShadeStyle = new ShadeStyle(false, true);
      contourShadeStyle.setLineColor(Color.pink);
      contourTheme.setStyle(contourShadeStyle);
      mapViewer.addTheme(contourTheme, 3);
      themePanel.addTheme(contourTheme);
      baseFileAvailable = true;
      this.show();
      }
          catch (Exception e)
    {
      statusBar.setText("Error opening "+fileName);
    }
  }


  void addStreamLayer()  {
      fileOpen();
      try{
      int fileNameLen = fileName.length();
      System.out.println("the opened file name is"+fileName);
      URL urlStreams = new URL("file:/"+fileName);
      ShapefileReader streamsReader = new ShapefileReader(urlStreams);
      streamsTheme = streamsReader.getTheme();
      streamsTheme.setName(specificFileName);
      //GeoData streamsTips = streamsReader.readData("Feature");
      //streamsTheme.setTipData(streamsTips);
      ShadeStyle streamsShadeStyle = new ShadeStyle(false, true);
      streamsShadeStyle.setLineColor(Color.blue);
      streamsTheme.setStyle(streamsShadeStyle);
      mapViewer.addTheme(streamsTheme, 2);
      themePanel.addTheme(streamsTheme);
      baseFileAvailable = true;
      this.show();
     }
         catch (Exception e)
    {
      statusBar.setText("Error opening "+fileName);
    }
 }

 void addLandCoverLayer()  {//land cover theme

      fileOpen();
      try{
      int fileNameLen = fileName.length();
      System.out.println("the opened file name is"+fileName);
      URL urlLandCover = new URL("file:/"+fileName);
      ShapefileReader landCoverReader = new ShapefileReader(urlLandCover);
      String shadeby2 = "LCID";
      GeoData shadeData2 = landCoverReader.readData(shadeby2);
      //UniqueShader uniqueShade = new UniqueShader();
      Shader landCoverShader = new ClassificationShader(shadeData2,13,ClassificationShader/*.DIFFERENCE, QUANTILE, EQUAL_INTERVAL*/.BREAKS,Color.pink/*.green*/,Color.cyan/*.orange*/);
      Theme landCoverTheme = landCoverReader.getTheme();
//      t2.setShader(uniqueShade);
      landCoverTheme.setShader(landCoverShader);
      landCoverTheme.setGeoData(shadeData2);
      landCoverTheme.setName(specificFileName);
      GeoData landCoverTips = landCoverReader.readData("LANDCOVER");
      landCoverTheme.setTipData(landCoverTips);
      keys.add(landCoverShader.getKey());

      HighlightManager hm = new HighlightManager();
      landCoverTheme.setHighlightManager(hm);
      ShadeStyle hs = landCoverTheme.getHighlightShadeStyle();
      hs.setFillColor(Color.magenta);
      hm.addHighlightChangedListener(new HighlightWriter(landCoverTheme, landCoverTips, statusBar));

      mapViewer.addTheme(landCoverTheme, 1);
      themePanel.addTheme(landCoverTheme);
      baseFileAvailable = true;
      this.show();//serves as a sort of refresh after adding the new theme
      }
          catch (Exception e)
    {
      statusBar.setText("Error opening "+fileName);
    }
  }




void gridLayer(){

  try{
  if(gridOption==false){

//      LatLongGrid gridLayer = new LatLongGrid();
//      Theme gridTheme = new Theme(gridLayer);
//      gridTheme.setName("Grids");
//       if(fileNameRecog.equals("streams")){
//      gridLayer.setBounds(streamsTheme.getLayer().getBounds());
//      }else{
//      gridLayer.setBounds(contourTheme.getLayer().getBounds());
//      }
//      mapViewer.addTheme(gridTheme, 4);
//      themePanel.addTheme(gridTheme);
//      mapViewer.setThemeIsVisible(gridTheme,false);
//      this.show();
      gridOption = true;
       }
    }
    catch (Exception e)
    {
      statusBar.setText("Error opening "+fileName);
    }
  }



double [] x;
double [] y;
double doubleValueX;
double doubleValueY;
 int i;

    public void recordClicks(){
      int totVertex = pLayer.getGeoShapes().capacity();
      //System.out.println("capacity in method recordClicks is    "+totVertex);
       x = new double[totVertex];
       y = new double[totVertex];

      String strValueX = " " ;
      String strValueY= " ";
      double doubleValueX = 0.0,doubleValueY=0.0;

        for (i=0; i<totVertex;i++){//for each geopoint recordded as object
          //System.out.println(" Vertex number in method recordClicks is     "+i);
          String vecPoint = String.valueOf(pLayer.getGeoShapes().elementAt(i)).toString();
          int vecPointLen = vecPoint.length();

            for(int z=1; z<=vecPointLen;z++){//with in each geopoint
                String subStrVecPoint = vecPoint.substring(z-1,z);
                if(subStrVecPoint.equals(",")){
                 subStrVecPoint = vecPoint.substring(z+1);

                 strValueY = vecPoint.substring(z);//assign value of y as those beyond the comma
                 strValueX = vecPoint.substring(9,z-1);//assign value of x as those between the geopoint followed by space (tot 10 characters) and the comma

                 if(!strValueY.equalsIgnoreCase("NaN")&&!strValueX.equalsIgnoreCase("NaN")){//convert the string values of x and y to double values if the values are not Nan
                 doubleValueY = Double.parseDouble(strValueY);
                 doubleValueX = Double.parseDouble(strValueX);
                 }
                 z= vecPointLen;
                 }
              }//end of for with in each geopoint

        //assign each geopoint to x and y coordinates
        x[i]=doubleValueX;
        y[i]=doubleValueY;

      }//end of for statement
    }//end of the recordClicks method


          int startVertex = 0;

          int pointNo = 1, subPoint = 1;
          int startVertexToDrawPoint = startVertex;//this is done to avoid the effect of modifications done in the drawPoint method to the drawLine and drawPolyg methods
          boolean newMenuToDrawNewPointCalled = false;
          //the following method is used to draw a single or multiple points by clicking on the viewer or by manual entry of
          //GPS read coordinate points
          public void drowPoint() {
              //GPSRead_Frame m_GPSRead_Frame= new GPSRead_Frame();
              int newNoOfVertex = i-startVertexToDrawPoint;
              boolean newPoint = true;
              Theme prevPointTheme= new Theme();
              CoordinateEntryOptionsFrame m_CoordinateEntryOptionsFrame = new CoordinateEntryOptionsFrame();
              coordinateEntryOption = m_CoordinateEntryOptionsFrame.getCoordinateEntryOption();

                if(coordinateEntryOption.equalsIgnoreCase("click")){
                      for(int v = startVertex; v<i; v++){//for each vertex process and draw it on the map
                          GeoPoint gPoint = new GeoPoint(x[v],y[v]);//draw each coordinate point starting from the new one
                          PointLayer pointLayer = new PointLayer();
                          pointLayer.addGeoPoint(gPoint);
                          //RasterLayer rLayer = new RasterLayer(cr);
                          Shader sh = new RampShader(0.0,10.0);
                          sh.setMissingValueCode(0.0);
                          Theme pointTheme = new Theme(pointLayer);
                          if(newPoint && newMenuToDrawNewPointCalled){// if the New sub-menu to draw new Point is called and this is the first point of the vector to draw assign new name
                              pointTheme.setName("Point_"+pointNo);
                              prevPointTheme = pointTheme;
                              newPoint = false;
                          }else{// if the drawPoint was already called and a second and following points are getting drawn by the loop assign similar name as the previous point
                              pointNo = pointNo-1;
                              pointTheme.setName("Point_"+pointNo+"."+subPoint);//label the point themes by point followed  by sub-point
                              subPoint=subPoint+1;
                          }
                          pointNo++;
                          pointTheme.getShadeStyle().setLineWidth(3);
                          pointTheme.getShadeStyle().setFillColor(java.awt.Color.red);
                          pointTheme.getShadeStyle().setLineColor(java.awt.Color.red);
                          mapViewer.addTheme(pointTheme);
                          themePanel.addTheme(pointTheme);
                          double x_coord = gPoint.getX();
                          double y_coord = gPoint.getY();
                          x_coord = Math.round(x_coord*100.0)/100.0;
                          y_coord = Math.round(y_coord*100.0)/100.0;
                          statusBar.setText("x_coord is :   "+x_coord+",  and y_coord is :   "+y_coord);

                          startVertexToDrawPoint =startVertexToDrawPoint+1;//set the start vertex for drawing point to one level up so as to avoid repetition of theme names for the previously drawn points

                          newMenuToDrawNewPointCalled = false;//set the option that the New Menu to draw new point is called to false
                      }//end of for each vertex draw on map
                    }//end of if by click option

                 else if(coordinateEntryOption.equalsIgnoreCase("manualEntry")){

                        try{
                         readLocationFile();
                           }catch(Exception r){
                         System.out.println(r);
                        }
                        for(int v=1; v<rLat.length; v++){//for each vertex process and draw it on the map
                          GeoPoint gPoint = new GeoPoint(rLat[v],rLon[v]);
                          PointLayer pointLayer = new PointLayer();
                          pointLayer.addGeoPoint(gPoint);
                          //RasterLayer rLayer = new RasterLayer(cr);
                          Shader sh = new RampShader(0.0,10.0);
                          sh.setMissingValueCode(0.0);
                          Theme pointTheme = new Theme(pointLayer);
                          if(pointNo>1){
                              pointTheme.setName("Point_"+pointNo);
                          }else{
                              pointTheme.setName("Point_1");
                          }
                          pointNo++;
                          pointTheme.getShadeStyle().setLineWidth(3);
                          pointTheme.getShadeStyle().setFillColor(java.awt.Color.red);
                          pointTheme.getShadeStyle().setLineColor(java.awt.Color.red);
                          mapViewer.addTheme(pointTheme);
                          themePanel.addTheme(pointTheme);
                          double x_coord = gPoint.getX();
                          double y_coord = gPoint.getY();
                          x_coord = Math.round(x_coord*100.0)/100.0;
                          y_coord = Math.round(y_coord*100.0)/100.0;

                          statusBar.setText("x_coord is :   "+x_coord+", and y_coord is :   "+y_coord);
                      }//end of for each vertex draw on map
                }//end of if manual entry
            }//end of drawPoint method


          /**
           * the following method is used to draw a single or poly lines by clicking on the viewer or by manual entry of
           * GPS read coordinate points
          **/

    int lineNo = 1, subLine = 1;
    boolean newMenuToDrawNewLineCalled = false;

    public void drowLine() {

        //GPSRead_Frame m_GPSRead_Frame= new GPSRead_Frame();
        int newNoOfVertex = i-startVertex;
        CoordinateEntryOptionsFrame m_CoordinateEntryOptionsFrame = new CoordinateEntryOptionsFrame();
        coordinateEntryOption = m_CoordinateEntryOptionsFrame.getCoordinateEntryOption();
        double length =0.0;

          double [] lat = new double[newNoOfVertex];
          double [] lon = new double[newNoOfVertex];
          int j=0;

        /**
         * transfer data objects created after the new sub menu is called from the x and y vectors to lat and lon vectors
        **/
        for(int n=startVertex; n<i; n++){
           lat[j] = x[n];
           lon[j] = y[n];
           j++;
        }

          if(coordinateEntryOption.equalsIgnoreCase("click")){
                GeoLine gLine = new GeoLine(startVertex,lat,lon,newNoOfVertex);//draw the line starting from the new coordinate
                LineLayer lineLayer = new LineLayer();
                lineLayer.addGeoLine(gLine);
                Shader sh = new RampShader(0.0,10.0);
                sh.setMissingValueCode(0.0);
                Theme lineTheme = new Theme(lineLayer);
                if(newMenuToDrawNewLineCalled){// if the drawLine is called and this is the first line of the vector to draw assign new name
                    lineTheme.setName("Line_"+lineNo);
                }else{// if the drawLine was already called and a successive lines are getting drawn by the loop assign similar name as the previous line
                    lineNo = lineNo-1;
                    lineTheme.setName("Line_"+lineNo+"."+subLine);//label the line themes by Line followed  by sub-line
                    subLine=subLine+1;
                }
                lineNo++;
                lineTheme.getShadeStyle().setLineWidth(3);
                lineTheme.getShadeStyle().setLineColor(java.awt.Color.black);
                mapViewer.addTheme(lineTheme);
                themePanel.addTheme(lineTheme);
                length = gLine.getPerimeter();
                newMenuToDrawNewLineCalled = false;//set the option that the drawNew menu is called to false
          }//end of if by click option

       else if(coordinateEntryOption.equalsIgnoreCase("manualEntry")){

              try{
               readLocationFile();
                 }catch(Exception r){
               System.out.println(r);
              }
              for(int v=1; v<rLat.length; v++){//for each vertex starting from vertex-1 moove to one level down in the vector so as to avoid the 0.0 value in the first vertex.
               rLat[v-1]=rLat[v];
               rLon[v-1]=rLon[v];
                }
                GeoLine gLine = new GeoLine(1,rLat,rLon,rLat.length);//draw the line starting from the first coordinate and including all points in rLat and rLon vectors
                LineLayer lineLayer = new LineLayer();
                lineLayer.addGeoLine(gLine);
                Shader sh = new RampShader(0.0,10.0);
                sh.setMissingValueCode(0.0);
                Theme lineTheme = new Theme(lineLayer);
                if(lineNo>1){
                lineTheme.setName("Line_"+lineNo);
                }else{
                lineTheme.setName("Line_1");
                }
                lineNo++;
                lineTheme.getShadeStyle().setLineWidth(3);
                lineTheme.getShadeStyle().setLineColor(java.awt.Color.black);
                mapViewer.addTheme(lineTheme);
                themePanel.addTheme(lineTheme);
                length = gLine.getPerimeter();
                //length = gLine.getSegmentLength(1,rLat.length);
      }//end of if manual entry

      length = Math.round(length*100.0)/100.0;
      String StrLength = String.valueOf(length).toString();
      statusBar.setText("Line length is :   "+StrLength+" m");
      this.show();
  }//end of drawLine method


          int polygonNo = 1, subPolygon = 1;
          boolean newMenuToDrawNewPolygonCalled = false;
          /**
           * the following method is used to draw a single or multiple polygons by clicking on the viewer or by manual entry of
           * GPS read coordinate points
          **/
           public void drowPolygon() {
              //GPSRead_Frame m_GPSRead_Frame= new GPSRead_Frame();
              int newNoOfVertex = i-startVertex;
              CoordinateEntryOptionsFrame m_CoordinateEntryOptionsFrame = new CoordinateEntryOptionsFrame();
              coordinateEntryOption = m_CoordinateEntryOptionsFrame.getCoordinateEntryOption();
              double area =0.0, perimeter=0.0;

                double [] lat = new double[newNoOfVertex];
                double [] lon = new double[newNoOfVertex];
                int j=0;

              /**
               * transfer data objects created after the new sub menu is called from the x and y vectors to lat and lon vectors
              **/
              for(int n=startVertex; n<i; n++){
                 lat[j] = x[n];
                 lon[j] = y[n];
                 j++;
              }
              if(coordinateEntryOption.equalsIgnoreCase("click")){
                    GeoPolygon gPolygon = new GeoPolygon(startVertex,lat,lon,newNoOfVertex);//draw the polygon starting from the new coordinate
                    PolygonLayer polygLayer = new PolygonLayer();
                    polygLayer.addGeoPolygon(gPolygon);
                    Shader sh = new RampShader(0.0,10.0);
                    sh.setMissingValueCode(0.0);
                    Theme polygTheme = new Theme(polygLayer);
                    if(newMenuToDrawNewPolygonCalled){// if the drawPolygon is called and this is the first polygon of the vector to draw assign new name
                        polygTheme.setName("Polygon_"+polygonNo);
                    }else{// if the drawPolygon was already called and a successive polygons are getting drawn by the loop assign similar name as the previous polygon
                        polygonNo = polygonNo-1;
                        polygTheme.setName("Polygon_"+polygonNo+"."+subPolygon);//label the polygon themes as "Polygon" followed  by sub-polygon number
                        subPolygon=subPolygon+1;
                    }
                    polygonNo++;
                    polygTheme.getShadeStyle().setLineWidth(5);
                    polygTheme.getShadeStyle().setLineColor(java.awt.Color.black);
                    mapViewer.addTheme(polygTheme);
                    themePanel.addTheme(polygTheme);

                    gPolygon.addPoint(lat[0],lon[0]);//add the first vertex to the gpolygon vector so as to avoid errors in area and perimeter computation
                    area = gPolygon.getArea();
                    perimeter = gPolygon.getPerimeter();

                    SelectionManager sm = new SelectionManager();
                    polygTheme.setSelectionManager(sm);
                    //gaz.setSelectionManager(sm);
                    ShadeStyle ss = polygTheme.getSelectionShadeStyle();
                    ss.setFillColor(Color.orange);
                    sm.addSelectionChangedListener(new SelectionWriter(polygTheme, gPolygon, statusBar));

                    newMenuToDrawNewPolygonCalled = false;//set the option that the drawNewPolygon in new sub menu is called to false
              }//end of if by click option

             else if(coordinateEntryOption.equalsIgnoreCase("manualEntry")){
                    try{
                     readLocationFile();
                       }catch(Exception r){
                     System.out.println(r);
                    }
                  for(int v=1; v<rLat.length; v++){//for each vertex starting from vertex-1 moove to one level down in the vector so as to avoid the 0.0 value in the first vertex.
                     rLat[v-1]=rLat[v];
                     rLon[v-1]=rLon[v];
                      }
                      GeoPolygon gPolygon = new GeoPolygon(1,rLat,rLon,rLat.length);//draw the polygon starting from the new coordinate
                      PolygonLayer polygLayer = new PolygonLayer();
                      polygLayer.addGeoPolygon(gPolygon);
                      Shader sh = new RampShader(0.0,10.0);
                      sh.setMissingValueCode(0.0);
                      Theme polygTheme = new Theme(polygLayer);
                      if(polygonNo>1){
                      polygTheme.setName("Polygon_"+polygonNo);
                      }else{
                      polygTheme.setName("Polygon_1");
                      }
                      polygonNo++;
                      polygTheme.getShadeStyle().setLineWidth(5);
                      polygTheme.getShadeStyle().setLineColor(java.awt.Color.black);
                      mapViewer.addTheme(polygTheme);
                      themePanel.addTheme(polygTheme);

                      gPolygon.addPoint(rLat[0],rLon[0]);//add the first vertex to the gpolygon vector so as to avoid errors in area and perimeter computation
                      area = gPolygon.getArea();
                      perimeter = gPolygon.getPerimeter();

                      SelectionManager sm = new SelectionManager();
                      polygTheme.setSelectionManager(sm);
                      //gaz.setSelectionManager(sm);
                      ShadeStyle ss = polygTheme.getSelectionShadeStyle();
                      ss.setFillColor(Color.orange);
                      sm.addSelectionChangedListener(new SelectionWriter(polygTheme, gPolygon, statusBar));


            }//end of if manual entry

              area = Math.round((area/1E6)*1000000.0)/1000000.0;
              perimeter = Math.round(perimeter*100.0)/100.0;
              String StrArea = String.valueOf(area).toString();
              statusBar.setText("Polygon area is :   "+StrArea+"  sq. Km  and Perimeter is:  "+perimeter+" m");
              this.show();
        }//end of drawPolygon method


    boolean okToAbandon() {
       int value =  JOptionPane.showConfirmDialog(this, "Save map as JPG file?",
                                             "RationalPeak: RP_GIS", JOptionPane.YES_NO_CANCEL_OPTION) ;
        switch (value) {
           case JOptionPane.YES_OPTION:
             return saveAsFile();
           case JOptionPane.NO_OPTION:
             return true;
           case JOptionPane.CANCEL_OPTION:
           default:
             // cancel
             return false;
        }
      }

   boolean saveAsFile(){
      JBChartXLite chart = new JBChartXLite();
         try
        {
        JFileChooser chooser = new JFileChooser();
              String currentDirectory = System.getProperty("user.dir");
              File file = new File(currentDirectory + "/Input_Output/*.*");
              chooser.setSelectedFile(file);
        int i = chooser.showSaveDialog(this);
        if(i == JFileChooser.APPROVE_OPTION)
        file = new File(chooser.getSelectedFile().getAbsolutePath());
        String f = file.getAbsolutePath();
        f += ".jpg";
        file = new File(f);

        mapViewer.print(mapViewer.getGraphics().create());
        //chart.add(/*mapViewer*/);
        chart.convert2Image(file,1);
        file = null;
        return true;
        }

        catch(Exception ioe)
        {
        statusBar.setText("Error Saving "+fileName);
        ioe.printStackTrace();
        return false;
        }
     }

  //File | Exit action performed
  public void fileExit_actionPerformed(ActionEvent e) {
    if (okToAbandon()) {
      //System.exit(0);
      dispose();
      MainMenuOpener();
    }
  }


  boolean pointLocation = false;
  boolean  packFrame = false;

   public void coordinateEntryOptionsFrameOpener() {
    CoordinateEntryOptionsFrame frame = new CoordinateEntryOptionsFrame();
      Toolkit toolkit = Toolkit.getDefaultToolkit();
      URL iconURL = getClass().getResource("/Images/RPIcon.gif");
      Image frameIcon = toolkit.getImage(iconURL);
      frame.setIconImage(frameIcon);

    //Validate frames that have preset sizes
    //Pack frames that have useful preferred size info, e.g. from their layout
//    if (packFrame) {
      frame.pack();
//    }
//    else {
//      frame.validate();
//    }
    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);
  }

  double [] rLat ;
  double [] rLon ;
 /**The readLocationFile method is used to read manually entered coordinates from the default file of LocationFile.rp
  * This file is called by each method whenever it is required to draw a feature by manual entry.
  */

  public void readLocationFile()throws Exception{
       int q = 1;//vertex number of the rLat and rLon
       ManualEntryFrame mEntryFrame = new ManualEntryFrame();
       int manualEntryNoOfRecords = mEntryFrame.getNoOfRecords();
       rLat = new double[manualEntryNoOfRecords+1];
       rLon = new double[manualEntryNoOfRecords+1];
       double valueX = 0.0;
       double valueY = 0.0;
       String currentDirectory = System.getProperty("user.dir");
       String inputFile = currentDirectory + "\\Input_Output\\LocationFile.rp";
       BufferedReader in
                    = new BufferedReader(
                           new FileReader(inputFile));
       String line = "Initialise String";
       String token = "Initialise Token";

        while ( (line = in.readLine()) != null)
        {
         StringTokenizer thisLine = new StringTokenizer(line," ()");
         String parameter = thisLine.nextToken().trim();

        //the following algorithm is to assign value of x and y coordinates excluding the delimiters i.e. the commas
           boolean endOfXValue = false;
           String xValue = "",yValue="";

          for(int z = 0;z<=parameter.length()-1;z++){
             String charOfParameter = String.valueOf(parameter.charAt(z));
            if(!charOfParameter.equalsIgnoreCase(",")){
                if((!endOfXValue)){
                  xValue = xValue+charOfParameter;
                 }
            }else{
              endOfXValue = true;
              String Y_Value = parameter.substring(z+1);

                  for(int f = 0;f<=Y_Value.length()-1;f++){
                      String charOfY_Value = String.valueOf(Y_Value.charAt(f));
                        yValue = yValue+charOfY_Value;
                    }
              }
           }//end of the for statement to assign x and y values in string format

          int lengthOfxValue = xValue.length();
          xValue = xValue.substring(1,lengthOfxValue-1);//excluded the cottation marks at the begining and end of the xValue string
          int lengthOfyValue = yValue.length();
          yValue = yValue.substring(1,lengthOfyValue-1);//excluded the cottation marks at the begining and end of the yValue string
          valueX = Double.parseDouble(xValue);
          valueY = Double.parseDouble(yValue);
          //System.out.println("XValue is "+xValue+ "  and YValue is  "+yValue);
          rLat[q]=valueX;
          rLon[q]=valueY;

          q++;//increrease vertex number by one
          }//end of the while statement
      }//end of the readLocation file method


       public void MainMenuOpener() {
        MainMenu_Frame frame = new MainMenu_Frame();
          Toolkit toolkit = Toolkit.getDefaultToolkit();
          URL iconURL = getClass().getResource("/Images/RPIcon.gif");
          Image frameIcon = toolkit.getImage(iconURL);
          frame.setIconImage(frameIcon);

        //Validate frames that have preset sizes
        //Pack frames that have useful preferred size info, e.g. from their layout
        if (packFrame) {
          frame.pack();
        }
        else {
          frame.validate();
        }
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
          frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
          frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
      }

 //Display the About box.
      void helpAbout() {
        About_Box dlg = new About_Box(this);
        dlg.setSize(230, 180);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.show();
      }

        void helpRationalPeak_actionPerformed(ActionEvent e) {
        HelpDialog dlg = new HelpDialog(this);
        dlg.setSize(600, 450);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frameSize = getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point loc = getLocation();
        dlg.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        dlg.setModal(true);
        dlg.show();
      }


    void newGISInterfaceOpener(){
        GISInterface frame = new  GISInterface();
          Toolkit toolkit = Toolkit.getDefaultToolkit();
          URL iconURL = getClass().getResource("/Images/RPIcon.gif");
          Image frameIcon = toolkit.getImage(iconURL);
          frame.setIconImage(frameIcon);

        //Validate frames that have preset sizes
        //Pack frames that have useful preferred size info, e.g. from their layout
        if (packFrame) {
          frame.pack();
        }
        else {
          frame.validate();
        }
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
          frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
          frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
      }


  //Help | About action performed
  public void helpAbout_actionPerformed(ActionEvent e) {
    helpAbout();
  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {

    super.processWindowEvent(e);

    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
        //fileExit_actionPerformed(null);
        //if(okToAbandon()){

        MainMenuOpener();
         dispose();
        //}
    }
  }

  void menuFileNew_actionPerformed(ActionEvent e) {

      if(okToAbandon()){
      dispose();
      //GISInterface GISInt = new GISInterface();
      newGISInterfaceOpener();

      }
   }

  void menuAddContour_actionPerformed(ActionEvent e) {
    addContourLayer();
  }

  void menuAddStream_actionPerformed(ActionEvent e) {
    addStreamLayer();
  }

  void menuAddLandCover_actionPerformed(ActionEvent e) {
    addLandCoverLayer();
  }


  String coordinateEntryOption = "";

  void menuDrawNew_actionPerformed(ActionEvent e) {
    if(baseFileAvailable == true){
     coordinateEntryOptionsFrameOpener();
     CoordinateEntryOptionsFrame m_CoordinateEntryOptionsFrame = new CoordinateEntryOptionsFrame();
     recordClicks();
     startVertex = i;
     newMenuToDrawNewPointCalled = true;
     newMenuToDrawNewLineCalled = true;
     newMenuToDrawNewPolygonCalled = true;
     subPoint = 1;
     subLine = 1;
     }else{
        JOptionPane.showMessageDialog(this, "First need to load a base map using the AddLayer Menu",
                                       "RationalPeak: RP_GIS", JOptionPane.INFORMATION_MESSAGE) ;
     }
  }

  void menuDrawPoint_actionPerformed(ActionEvent e) {
   recordClicks();
   drowPoint();
  }

  void menuDrawLine_actionPerformed(ActionEvent e) {
   recordClicks();
   drowLine();
  }

  void menuDrawPolygon_actionPerformed(ActionEvent e) {
    recordClicks();
    drowPolygon();
  }

  void menuFileSaveAs_actionPerformed(ActionEvent e) {
    saveAsFile();
  }

  }//end of the GISInterface Class



 class GISInterface_menuFileExit_ActionAdapter implements ActionListener {
  GISInterface adaptee;

  GISInterface_menuFileExit_ActionAdapter(GISInterface adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.fileExit_actionPerformed(e);
  }
}

class GISInterface_menuHelpAbout_ActionAdapter implements ActionListener {
  GISInterface adaptee;

  GISInterface_menuHelpAbout_ActionAdapter(GISInterface adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.helpAbout_actionPerformed(e);
  }
}

class GISInterface_menuFileNew_actionAdapter implements java.awt.event.ActionListener {
  GISInterface adaptee;

  GISInterface_menuFileNew_actionAdapter(GISInterface adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileNew_actionPerformed(e);
  }
}

class GISInterface_menuFileSaveAs_actionAdapter implements java.awt.event.ActionListener {
  GISInterface adaptee;

  GISInterface_menuFileSaveAs_actionAdapter(GISInterface adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileSaveAs_actionPerformed(e);
  }
}

class GISInterface_menuAddContour_actionAdapter implements java.awt.event.ActionListener {
  GISInterface adaptee;

  GISInterface_menuAddContour_actionAdapter(GISInterface adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuAddContour_actionPerformed(e);
  }
}

class GISInterface_menuAddStream_actionAdapter implements java.awt.event.ActionListener {
  GISInterface adaptee;

  GISInterface_menuAddStream_actionAdapter(GISInterface adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuAddStream_actionPerformed(e);
  }
}

class GISInterface_menuAddLandCover_actionAdapter implements java.awt.event.ActionListener {
  GISInterface adaptee;

  GISInterface_menuAddLandCover_actionAdapter(GISInterface adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuAddLandCover_actionPerformed(e);
  }
}

class GISInterface_menuDrawLine_actionAdapter implements java.awt.event.ActionListener {
  GISInterface adaptee;

  GISInterface_menuDrawLine_actionAdapter(GISInterface adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuDrawLine_actionPerformed(e);
  }
}

class GISInterface_menuDrawNew_actionAdapter implements java.awt.event.ActionListener {
  GISInterface adaptee;

  GISInterface_menuDrawNew_actionAdapter(GISInterface adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuDrawNew_actionPerformed(e);
  }
}

class GISInterface_menuDrawPoint_actionAdapter implements java.awt.event.ActionListener {
  GISInterface adaptee;

  GISInterface_menuDrawPoint_actionAdapter(GISInterface adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuDrawPoint_actionPerformed(e);
  }
}

class GISInterface_menuDrawPolygon_actionAdapter implements java.awt.event.ActionListener {
  GISInterface adaptee;

  GISInterface_menuDrawPolygon_actionAdapter(GISInterface adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuDrawPolygon_actionPerformed(e);
  }
}

class SelectionWriter implements SelectionChangedListener {

  Theme theme;
  double areas, perimeter;
  JLabel outputDestination;

  public SelectionWriter(Theme t, GeoPolygon gPolygon, JLabel outputDest) {
    theme = t;
    areas = gPolygon.getArea();
    areas = Math.round((areas/1E6)*1000000.0)/1000000.0;

    perimeter = gPolygon.getPerimeter();
    perimeter = Math.round(perimeter*100.0)/100.0;


    outputDestination = outputDest;
    }

  public void selectionChanged(SelectionChangedEvent sce) {

    JLabel outputDes = new JLabel();
    if (sce.getSelection().length > 0) {
      String text = theme.getTipText(sce.getSelection()[sce.getSelection().length - 1]);
      double area = areas;
      String selectedPolygName = theme.getName();
      outputDestination.setForeground(Color.black);
      outputDestination.setText(theme.getName() + ": Area = " + area + " sq. km"+"  Perimeter = " + perimeter + " m");

      String stringMessage = selectedPolygName+":   \n"+"Area =  "+String.valueOf(area)+ " sq. km\n"+"Perimeter = "+perimeter+ " m";
              JOptionPane.showMessageDialog(new GISInterface(outputDes), stringMessage,
                                       "RationalPeak: RP_GIS", JOptionPane.INFORMATION_MESSAGE) ;

    } else {
      outputDestination.setText("");
    }
   }
}

class HighlightWriter implements HighlightChangedListener {
  Theme theme;
  GeoData areas;
  JLabel outputDestination;

  public HighlightWriter(Theme t, GeoData areaData, JLabel outputDest) {
    theme = t;
    areas = areaData;
    outputDestination = outputDest;
  }

  public void highlightChanged(HighlightChangedEvent hce) {
    if (hce.getHighlighted() != -1) {
      String text = theme.getTipText(hce.getHighlighted());
      outputDestination.setForeground(Color.magenta);
      outputDestination.setText("Dominant Land Cover :  " + text );
    } else {
      outputDestination.setText("");
    }
  }
}

