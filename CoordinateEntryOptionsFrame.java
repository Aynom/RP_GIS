package rp_GIS;

import java.net.URL;

import java.awt.*;
import com.borland.jbcl.layout.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Title:        RP_GIS
 * Description:  The GIS interface of RationalPeak Model
 * Copyright:    Copyright (c) 2009
 * @author Aynom Teweldebrhan
 * @version 1.0
 */
public class CoordinateEntryOptionsFrame extends JFrame {
  XYLayout xYLayout1 = new XYLayout();
  JLabel drawNewFeatureOptionsLabel = new JLabel();
  JToggleButton byClickToggleButton = new JToggleButton();
  JToggleButton manualEntryToggleButton = new JToggleButton();

  public CoordinateEntryOptionsFrame() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    xYLayout1.setWidth(326);
    xYLayout1.setHeight(177);
    this.getContentPane().setLayout(xYLayout1);
    this.setTitle("RP_GIS:  Coordinate Entry Options");
    drawNewFeatureOptionsLabel.setText("Select An Option To Draw New Feature");
    byClickToggleButton.setText("By Click On Base Map");
    byClickToggleButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        byClickToggleButton_actionPerformed(e);
      }
    });
    manualEntryToggleButton.setText("Through Manual Entry of Coordinates");
    manualEntryToggleButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manualEntryToggleButton_actionPerformed(e);
      }
    });
    this.getContentPane().add(drawNewFeatureOptionsLabel,   new XYConstraints(37, 19, 219, 22));
    this.getContentPane().add(manualEntryToggleButton, new XYConstraints(39, 114, -1, -1));
    this.getContentPane().add(byClickToggleButton, new XYConstraints(38, 66, 236, -1));
  }

  static String CEOption = "";
  void byClickToggleButton_actionPerformed(ActionEvent e) {
   CEOption = "click";
   setCoordinateEntryOption(CEOption);
   dispose();
  }

  void manualEntryToggleButton_actionPerformed(ActionEvent e) {
  CEOption = "manualEntry";
  setCoordinateEntryOption(CEOption);
  m_ManualEntry_FrameOpner();
  dispose();
  }

  public void setCoordinateEntryOption(String coordEntOpn){
  CEOption = coordEntOpn;
  }

  public String getCoordinateEntryOption(){
  return CEOption;
  }

   boolean  packFrame = false;
   public void m_ManualEntry_FrameOpner() {
    ManualEntryFrame frame = new ManualEntryFrame();
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


}