package core;

import database.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
            String ctags = request.getParameter("ctags");

            tags = new ArrayList<>();
            captions = new ArrayList<>();

            StringTokenizer st = new StringTokenizer(ctags, ",");
            while (st.hasMoreTokens()) {
                tags.add(st.nextToken());
            }

            String current_tag = getRandomItem(tags);
            captions = getCaption(current_tag);

            String jsonText = jsonResponse(tags, captions, current_tag);
            out.print(jsonText);

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

    public static String jsonResponse(ArrayList<String> common_tags, ArrayList<String> dbcaptions, String current_tag) throws IOException {
        // send common tags and captions to client
        String jsonText;
        JSONObject mainJO = new JSONObject();
        JSONArray ctags = new JSONArray();
        JSONArray captions = new JSONArray();

        for (String x : common_tags) {
            ctags.add(x);
        }

        for (String x : dbcaptions) {
            captions.add(x);
        }

        mainJO.put("ctags", ctags);
        mainJO.put("captions", captions);
        mainJO.put("current_tag", current_tag);

        StringWriter out = new StringWriter();
        mainJO.writeJSONString(out);
        jsonText = out.toString();

        return jsonText;
    }
}
