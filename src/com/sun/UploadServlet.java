package com.sun;


import java.io.*;
import java.util.*;
 
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;

public class UploadServlet extends HttpServlet {
   
   private boolean isMultipart;
   private String filePath;
   private int maxFileSize = 1024*1024*10; // 10 MB
   private int maxMemSize = 1024*1024*10;
   private File file ;

   public void init( ){
      filePath = getServletContext().getInitParameter("file-upload"); 
   }
   public void doPost(HttpServletRequest request, HttpServletResponse response) 
		   throws ServletException, java.io.IOException {
      isMultipart = ServletFileUpload.isMultipartContent(request);
      response.setContentType("text/html");
      if( !isMultipart ){
    	  request.setAttribute("is_file_uploaded", false);
          request.getRequestDispatcher("upload-success.jsp").forward(request, response);
         return;
      }
      
      DiskFileItemFactory factory = new DiskFileItemFactory();
      factory.setSizeThreshold(maxMemSize);
      // Location to save data that is larger than maxMemSize.
      factory.setRepository(new File("\temp"));

      // Create a new file upload handler
      ServletFileUpload upload = new ServletFileUpload(factory);
      upload.setSizeMax( maxFileSize );

      try{ 
      // Parse the request to get file items.
      List fileItems = upload.parseRequest(request);
      Iterator i = fileItems.iterator();
      while ( i.hasNext() ) 
      {
         FileItem fi = (FileItem)i.next();
         if ( !fi.isFormField () ){
            
        	 
        	 // by this way we can get file properties
            String fieldName = fi.getFieldName();
            String fileName = fi.getName();
            String contentType = fi.getContentType();
            boolean isInMemory = fi.isInMemory();
            long sizeInBytes = fi.getSize();
            
            
            //Write the file
            if( fileName.lastIndexOf("\\") >= 0 ){
               file = new File( filePath + 
               fileName.substring( fileName.lastIndexOf("\\"))) ;
            } else {
               file = new File( filePath + 
               fileName.substring(fileName.lastIndexOf("\\")+1)) ;
            }
            fi.write( file );
            request.setAttribute("file_name", fileName);
            request.setAttribute("is_file_uploaded", true);
            request.getRequestDispatcher("upload-success.jsp").forward(request, response);
         }
      }
   } catch(Exception ex) {
       System.out.println(ex);
       PrintWriter pw = response.getWriter();
       pw.println("Error occured :: "+ ex.getMessage());
   }
   }
   public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, java.io.IOException {
        throw new ServletException("GET method is not supported, POST method required.");
   }
}