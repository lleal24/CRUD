/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author lorena leal
 */
public class Conexion {

    //metodo publico conexion

    Connection con;

    public Conexion() {
        try {
            //driver
            Class.forName("com.mysql.jdbc.Driver");
            //variable conexion
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/Agenda", "root", "");

        } catch (Exception e) {
            System.err.println("Error: " + e);

        }

    }

    public static void main(String[] args) {
        //instancear
        Conexion cn = new Conexion();
        Statement st;
        ResultSet rs;
        try {
            st = cn.con.createStatement();
            rs = st.executeQuery("select * from contactos");
            while (rs.next()) {
                System.out.println(rs.getInt("id_contacto") + " " + rs.getString("nombre")
                        + " " + rs.getString("telefono") + " " + rs.getString("email"));
            }
            cn.con.close();
        } catch (Exception e) {

        }

    }

}
