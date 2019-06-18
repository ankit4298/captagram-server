package core;

import database.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ANKIT
 */
public class RefreshCaptions extends HttpServlet {

    static Connection con;
    static Statement stmt;
    static ResultSet rs;
    static PreparedStatement ps;
    
    ArrayList<String> tags;
    ArrayList<String> captions;
    
   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            // get common tags from client
            String ctags=request.getParameter("ctags");
            
            tags=new ArrayList<>();
            captions=new ArrayList<>();
            
            StringTokenizer st = new StringTokenizer(ctags, ",");
            while (st.hasMoreTokens()) {
                tags.add(st.nextToken());
            }
            
            String x=getRandomItem(tags);
            captions = getCaption(x);
            out.print("<hr><b>"+x+"</b> captions<hr>" + captions);
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private static ArrayList<String> getCaption(String one_tag) {

        ArrayList<String> al = new ArrayList<>();
        try {

            con = DBConnection.getDBConnection();

            String sql = "select caption from captions where tag=? order by RAND() limit 3";
            ps = con.prepareStatement(sql);
            ps.setString(1, one_tag);
            rs = ps.executeQuery();

            while (rs.next()) {
                al.add(rs.getString("caption"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(GenCaption.class.getName()).log(Level.SEVERE, null, ex);
        }

        return al;
    }

    public static String getRandomItem(ArrayList<String> al) {
        return al.get(new Random().nextInt(al.size()));
    }
    
    public String jsonResponse() throws IOException {

        // send captions to client
        
        return null;
    }
}
