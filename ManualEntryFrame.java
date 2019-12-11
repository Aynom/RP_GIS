package rp_GIS;

import java.net.URL;
import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.borland.dx.dataset.*;
import com.borland.dbswing.*;

/**
 * Title:        RP_GIS
 * Description:  The GIS interface of RationalPeak Model
 * Copyright:    Copyright (c) 2009
 * @author Aynom Teweldebrhan
 * @version 1.0
 */
 
public class ManualEntryFrame extends JFrame {
  JPanel contentPane;
  TextDataFile textDataFile1 = new TextDataFile();
  TableDataSet tableDataSet1 = new TableDataSet();
  Column x_coord = new Column();
  Column y_coord = new Column();
  Column remark = new Column();
  JdbNavToolBar jdbNavToolBar1 = new JdbNavToolBar();
  TableScrollPane tableScrollPane1 = new TableScrollPane();
  JdbTable jdbTable1 = new JdbTable();
  JButton saveChangesButton = new JButton();
  JButton saveAsButton = new JButton();

  //Construct the frame
  public ManualEntryFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception  {
    x_coord.setCaption("X_Coord");
    x_coord.setColumnName("X_Coord");
    x_coord.setDataType(com.borland.dx.dataset.Variant.STRING);
    x_coord.setServerColumnName("Newx_coord");
    x_coord.setSqlType(0);
    y_coord.setCaption("Y_Coord");
    y_coord.setColumnName("Y_Coord");
    y_coord.setDataType(com.borland.dx.dataset.Variant.STRING);
    y_coord.setServerColumnName("Newy_coord");
    y_coord.setSqlType(0);

    tableDataSet1.setDataFile(textDataFile1);

    String currentDirectory = System.getProperty("user.dir");

    textDataFile1.setFileName(currentDirectory+"/Input_Output/LocationFile.rp");

    textDataFile1.setSeparator(",");
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(null);
    this.setSize(new Dimension(533, 350));
    this.setTitle("RP_GIS:  Manual Entry of Coordinates");
    tableDataSet1.setColumns(new Column[] {x_coord, y_coord/*, remark*/});
    jdbTable1.setDataSet(tableDataSet1);
    tableScrollPane1.setBounds(new Rectangle(16, 44, 354, 201));
    jdbNavToolBar1.setBounds(new Rectangle(1, 1, 400, 39));
    saveChangesButton.setText("Save changes");
    saveChangesButton.setBounds(new Rectangle(391, 79, 123, 23));
    saveChangesButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        saveChangesButton_actionPerformed(e);
      }
    });
    closeButton.setText("Close");
    closeButton.setBounds(new Rectangle(421, 216, 69, 23));
    closeButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        closeButton_actionPerformed(e);
      }
    });
    saveAsButton.setText("Save As");
    saveAsButton.setBounds(new Rectangle(407, 119, 86, 24));
    saveAsButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveAsButton_actionPerformed(e);
      }
    });
    jdbTextArea1.setToolTipText("Status Bar");
    jdbTextArea1.setEditable(false);
    jdbTextArea1.setBounds(new Rectangle(23, 253, 483, 55));
    jdbTextArea1.setBackground(SystemColor.activeCaptionBorder);
    jdbTextArea1.setFont(new Font("Dialog", 0, 12));
    jdbTextArea1.setBorder(BorderFactory.createLoweredBevelBorder());
    jdbTextArea1.setText("* Use the Tool Bar to add, insert, edit or delete records\n"+"** Save before you close the Manual Entry Frame");

    contentPane.add(tableScrollPane1, null);
    contentPane.add(jdbNavToolBar1, null);
    contentPane.add(saveChangesButton, null);
    contentPane.add(saveAsButton, null);
    contentPane.add(closeButton, null);
    contentPane.add(jdbTextArea1, null);
    tableScrollPane1.getViewport().add(jdbTable1, null);
  }

  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
  }

  static int noOfRecords = 0;
  JButton closeButton = new JButton();
  void saveChangesButton_actionPerformed(ActionEvent e) {
    saveChanges();
  }

  public int getNoOfRecords(){
  return noOfRecords;
  }

  void closeButton_actionPerformed(ActionEvent e) {
     close();
  }

  void close(){
     if(okToAbandon()){
        dispose();
       }
  }

  boolean saveChanges(){
      try {
      noOfRecords = tableDataSet1.getRowCount();
      tableDataSet1.getDataFile().save(tableDataSet1);
      jdbTextArea1.setText("Changes Saved");
       return true;
    }
    catch (Exception ex) {
      jdbTextArea1.setText("Changes NOT saved : "+ex);
      return false;
    }

  }


   String fileName = "";
  JdbTextArea jdbTextArea1 = new JdbTextArea();
   boolean saveAsFile(){
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
        f += ".rp";

        textDataFile1.setFileName(f);
        tableDataSet1.getDataFile().save(tableDataSet1);
        noOfRecords = tableDataSet1.getRowCount();
        jdbTextArea1.setText("Changes Saved");
        file = new File(f);
        fileName = f;
        file = null;
        return true;
        }

        catch(Exception ioe)
        {
        jdbTextArea1.setText("Error Saving "+fileName);
        ioe.printStackTrace();
        return false;
        }
     }


  boolean okToAbandon() {
       int value =  JOptionPane.showConfirmDialog(this, "Save Changes?",
                                             "RationalPeak: RP_GIS", JOptionPane.YES_NO_CANCEL_OPTION) ;
        switch (value) {
           case JOptionPane.YES_OPTION:
             return /*saveAsFile()*/saveChanges();
           case JOptionPane.NO_OPTION:
             return true;
           case JOptionPane.CANCEL_OPTION:
           default:
             // cancel
             return false;
        }
    }

  void saveAsButton_actionPerformed(ActionEvent e) {
    saveAsFile();
  }


}
