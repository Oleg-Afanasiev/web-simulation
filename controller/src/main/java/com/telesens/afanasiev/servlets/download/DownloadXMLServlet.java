package com.telesens.afanasiev.servlets.download;

import java.io.*;

import com.telesens.afanasiev.DaoFactory;
import com.telesens.afanasiev.Map;
import com.telesens.afanasiev.MapDAO;
import com.telesens.afanasiev.impl.jaxb.MapDAOJaxbImpl;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class DownloadXMLServlet extends PersistServlet {

    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String target = req.getParameter("target");

        if (target != null) {
            if (target.equals("map")) {
                downloadMap(req, resp, getServletContext().getRealPath("/" + "map.xml"));
            }
        }
    }

    private void downloadMap(HttpServletRequest req, HttpServletResponse resp, String fileName) {

        long mapId;

        try {
            mapId = Long.parseLong(req.getParameter("id"));
        } catch(NumberFormatException exc) {
            logger.debug("Can't download data");
            return;
        }

        DaoFactory daoFactory = DaoFactory.getInstance();
        MapDAO mapDAO = daoFactory.getMapDAO();
        Map map = mapDAO.getById(mapId);

        MapDAO mapDAOJaxb = new MapDAOJaxbImpl(fileName);
        mapDAOJaxb.insertOrUpdate(map);

        openDialogSaveFile(resp);
    }

    private void openDialogSaveFile(HttpServletResponse resp) {

        // Use a relative path to context root:
        String relativePath = getServletContext().getRealPath("/map.xml");

        File downloadFile = new File(relativePath);

        FileInputStream inStream;
        try {
            inStream = new FileInputStream(downloadFile);
        } catch(FileNotFoundException exc ) {
            return;
        }

        // obtains ServletContext
        ServletContext context = getServletContext();

        // gets MIME type of the file
        String mimeType = context.getMimeType(relativePath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }

        // modifies response
        resp.setContentType(mimeType);
        resp.setContentLength((int) downloadFile.length());

        // forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        resp.setHeader(headerKey, headerValue);

        try {
            // obtains response's output stream
            OutputStream outStream = resp.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inStream.close();
            outStream.close();
        } catch(IOException exc) {
            logger.debug("Can't download 'map'");
        }
    }
}
