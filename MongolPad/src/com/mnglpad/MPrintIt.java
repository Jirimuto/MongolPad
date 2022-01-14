package com.mnglpad;

import java.awt.*;
import java.awt.print.*;
import javax.swing.*;
import javax.swing.text.*;

import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.MTextArea;
 
public class MPrintIt implements Printable {
  int        currentPage     = -1;
  double     pageEndX        = 0;
  double     pageStartX      = 0;
  boolean    scaleWidthToFit = false; 
  MTextArea  editArea; 
  PageFormat pFormat;
  PrinterJob pJob;
 
  public MPrintIt( String path ) {
	  pFormat = new PageFormat();
	  // Paper A4
	  Paper paper = new Paper();
	  paper.setSize(594.992125984252, 841.8897637795276);
	  paper.setImageableArea(45, 60, 504, 700);
	  pFormat.setPaper(paper);
	  pJob    = PrinterJob.getPrinterJob();
	  if( path != null )
	  pJob.setJobName( path );
  }
 
  private Document getDocument() {
    if ( editArea != null ) {
      return editArea.getDocument();
    }
    else {
      return null;
    }
  }
  private boolean getScaleWidthToFit() {
    return scaleWidthToFit;
  }
 
  private void pageDialog() {
	  
	  pFormat = pJob.pageDialog( pFormat );
	  
  }
 
  public int print( Graphics graphics, PageFormat pageFormat, int pageIndex ) {
    double     scale       = 1.0;
    Graphics2D graphics2D;
    graphics2D             = (Graphics2D) graphics;
    View       rootView;
    
    graphics2D.setFont(editArea.getFont());
    FontMetrics defaultMetrics = graphics2D.getFontMetrics();

    Paper paper = new Paper();
    int maxLines = (int)(pageFormat.getImageableWidth()/defaultMetrics.getHeight());
    paper.setImageableArea(	pageFormat.getImageableX(), 
    						pageFormat.getImageableY(), 
    						defaultMetrics.getHeight()*maxLines, 
    						pageFormat.getImageableHeight());
    pageFormat.setPaper(paper);
    
    editArea.setSize( (int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableHeight() );
    Dimension size = editArea.getPreferredSize();
    size.setSize((int) pageFormat.getWidth(), (int) pageFormat.getHeight());
    editArea.setPreferredSize( size );
    editArea.validate();
    
    rootView = editArea.getUI().getRootView( editArea );
    if  ( ( scaleWidthToFit )
     &&   (editArea.getMinimumSize().getWidth() > pageFormat.getImageableWidth() ) ) {
      scale = pageFormat.getImageableWidth() / editArea.getMinimumSize().getWidth();
      graphics2D.scale( scale, scale );
    }
    graphics2D.setClip( (int) ( pageFormat.getImageableX()      / scale ),
                        (int) ( pageFormat.getImageableY()      / scale ),
                        (int) ( pageFormat.getImageableWidth()  / scale ),
                        (int) ( pageFormat.getImageableHeight() / scale ) );
    
    if ( pageIndex > currentPage ) {
      currentPage = pageIndex;
    }
    pageStartX = (currentPage)*graphics2D.getClipBounds().getWidth();
    pageEndX    = graphics2D.getClipBounds().getWidth();
    
    graphics2D.translate( graphics2D.getClipBounds().getX(),
                          graphics2D.getClipBounds().getY() );
    
    Rectangle allocation = new Rectangle( (int) -pageStartX,  0, 
                                             (int) ( editArea.getMinimumSize().getHeight() ),
                                             (int) ( editArea.getPreferredSize().getWidth() ) );
    
    if (printView( graphics2D,allocation,rootView ) ) {
    	
    	// Rectangle r = new Rectangle( 0, 0, (int)(pageFormat.getImageableWidth()), (int)(pageFormat.getImageableHeight()));
    	// graphics2D.draw(r);
    	
    	return Printable.PAGE_EXISTS;
    }
    else {
      pageStartX = 0;
      pageEndX = 0;
      currentPage = -1;
      return Printable.NO_SUCH_PAGE;
    }
  }
 
  public void print( JTextArea area ) {
    setDocument( area );
    printDialog();
  }
 
  private void printDialog() {
	  
    if ( pJob.printDialog() ) {
    
      pJob.setPrintable( this, pFormat );
      try {
        pJob.print();
      }
      catch (PrinterException printerException) {
        pageStartX = 0;
        pageEndX = 0;
        currentPage = -1;
        System.out.println( "Error Printing Document" );
      }
    }
  }
 
  private boolean printView( Graphics2D graphics2D, Shape allocation, View view ) {
    boolean   pageExists       = false;
    Rectangle clipRectangle    = graphics2D.getClipBounds();
    Shape     childAllocation;
    View      childView;
    if ( view.getViewCount() > 0 ) {
      for ( int i = 0; i < view.getViewCount(); i++ ) {
        childAllocation = view.getChildAllocation( i, allocation );
        Rectangle r = (childAllocation instanceof Rectangle) ?
                (Rectangle) childAllocation : childAllocation.getBounds();
        int tmp = r.x;
        r.x = r.y;
        r.y = tmp;
        
        tmp = r.width;
        r.width = r.height;
        r.height = tmp;
        
        childAllocation = r;
        
        if ( childAllocation != null ) {
          childView = view.getView(i);
          if ( printView( graphics2D, childAllocation ,childView ) ) {
            pageExists = true;
          }
        }
      }
    }
    else {
      if ( allocation.getBounds().getMaxX() >= clipRectangle.getX() ) {
        pageExists = true;
        if  ( ( allocation.getBounds().getWidth() > clipRectangle.getWidth() )
         &&   ( allocation.intersects( clipRectangle ) ) ) {
        	
            Rectangle r = new Rectangle ();
            r.x = (int) allocation.getBounds().getY();
            r.y =  (int) allocation.getBounds().getX() ;
            r.width =(int) allocation.getBounds().getHeight();
            r.height = (int) allocation.getBounds().getWidth();
            
        	view.paint( graphics2D, r );
        }
        else {
          if ( allocation.getBounds().getX() >= clipRectangle.getX() ) {
            if ( allocation.getBounds().getMaxX() <= clipRectangle.getMaxX() ) {
            	
                Rectangle r = new Rectangle ();
                r.x = (int) allocation.getBounds().getY();
                r.y =  (int) allocation.getBounds().getX() ;
                r.width =(int) allocation.getBounds().getHeight();
                r.height = (int) allocation.getBounds().getWidth();
                
                view.paint( graphics2D, r );
            }
            else {
              if ( allocation.getBounds().getX() < clipRectangle.getMaxX() ) {
                  Rectangle r = new Rectangle ();
                  r.x = (int) allocation.getBounds().getY();
                  r.y =  (int) allocation.getBounds().getX() ;
                  r.width =(int) clipRectangle.getMaxX() - (int)allocation.getBounds().getX() ;
                  r.height = (int) allocation.getBounds().getWidth();
                  
                  view.paint( graphics2D, r );
                  
                  // pageEndX = allocation.getBounds().getX();
              }
            }
          } else {
        	  
                  Rectangle r = new Rectangle ();
                  r.x = (int) allocation.getBounds().getY();
                  r.y =  (int) allocation.getBounds().getX() ;
                  r.width =(int) allocation.getBounds().getHeight();
                  r.height = (int) allocation.getBounds().getWidth();
                  
                  view.paint( graphics2D, r );
          }
        }
      }
    }
    return pageExists;
  }
   
  private void setDocument( JTextArea area ) { 
	  
	  editArea = new MTextArea();
	  editArea.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
	  editArea.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
	  editArea.setFont( area.getFont() );
		
	  editArea.setText( area.getText() );
	  editArea.setLineWrap(true);
	  editArea.setWrapStyleWord(true);
  }
  
  private void setScaleWidthToFit( boolean scaleWidth ) {
	  scaleWidthToFit = scaleWidth;
  }
}

