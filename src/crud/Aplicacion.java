/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


/**
 * https://www.javamexico.org/blogs/joseguru/procedimientos_almacenados_agregareditareliminarmostrar_sqljava
 *
 * @author lorena leal
 */
public class Aplicacion {

    private static Scanner leo = new Scanner(System.in);
    private static int id;
    private static String nombre;
    private static String telefono;
    private static String email;
    private static int opcion;

    public static void main(String[] args) throws SQLException {
        //ciclo do while haga mientras que la opcion sea diferente a 5
        do {
            System.out.println("Elija una opcion");
            System.out.println("1.Insertar");
            System.out.println("2.Modificar");
            System.out.println("3.Borrar");
            System.out.println("4.Mostar Datos");
            System.out.println("5.Salir");
            //se lee y guarda la opcion 
            opcion = leo.nextInt();
            //switch case para cada caso
            switch (opcion) {
                case 1:
                    insertarProcedimiento();
                    break;
                case 2:
                    modificarProcedimiento();
                    break;
                case 3:
                    borrarProcedimiento();
                    break;
                case 4:
                    mostrarProcedimiento();
                    break;
            }
        } while (opcion != 5);
    }

    private static void mostrarProcedimiento() throws SQLException {

        Connection conectar = null;
        ResultSet rs; //conjunto de resultados obtendos de una consulta SQL
        try {
            //crear la conexion
            conectar = Conexion.conectar();
            conectar.setAutoCommit(false);

            //callablestatement para llamar procedimientos almacenados
            CallableStatement prcProcedimientoAlmacenado = conectar.prepareCall("{call Leer()}");
            rs = prcProcedimientoAlmacenado.executeQuery();

            while (rs.next()) {
                System.out.println("Nombre" + "->" + rs.getString(1));
                System.out.println("Telefono" + "->" + rs.getString(2));
                System.out.println("Email" + "->" + rs.getString(3));
                System.out.println("ID" + "->" + rs.getInt(4));
            }
            //confirma la transaccion
            conectar.commit();

        } catch (Exception e) {
            //en caso de ocurrir un error hacer rollback para no afectar la tabla
            conectar.rollback();
            e.printStackTrace();
        } finally {
            //cerrar la conexion
            conectar.close();
        }

    }

    public static void insertarProcedimiento() throws SQLException {
        Connection conectar = null;

        try {
            conectar = Conexion.conectar();
            //desactivar el auto commit
            conectar.setAutoCommit(false);

            //pedir y capturar datos
            System.out.println("Ingrese el nombre del contacto");
            nombre = leo.next();
            System.out.println("Ingrese telefono");
            telefono = leo.next();
            System.out.println("Ingrese email");
            email = leo.next();
            System.out.println("Ingrese el id");
            id = leo.nextInt();

            //se realiza el llamado al procedimiento con los tres parametros de entada
            CallableStatement prcProcedimientoAlmacenado = conectar.prepareCall("{call Insertar(?,?,?,?)}");

            //cargar los parametros
            prcProcedimientoAlmacenado.setString(1, nombre);
            prcProcedimientoAlmacenado.setString(2, telefono);
            prcProcedimientoAlmacenado.setString(3, email);
            prcProcedimientoAlmacenado.setInt(4, id);

            //ejecucion del procedimiento
            prcProcedimientoAlmacenado.execute();

            conectar.commit();
            System.out.println("Insertado con exito");

        } catch (Exception e) {
            conectar.rollback();
            e.printStackTrace();

        } finally {
            conectar.close();
        }
    }

    public static void borrarProcedimiento() throws SQLException {
        Connection conectar = null;

        try {
            //conexion
            conectar = Conexion.conectar();
            conectar.setAutoCommit(false);

            System.out.println("Introduzca el id a eliminar");
            int n = leo.nextInt();

            CallableStatement prcProcedimientoAlmacenado = conectar.prepareCall("{call Borrar(?)}");
            
            prcProcedimientoAlmacenado.setInt(4, n);

            prcProcedimientoAlmacenado.execute();
            conectar.commit();
            System.out.println("Registro eliminado exitosamente");

        } catch (Exception e) {
            conectar.rollback();
            e.printStackTrace();
        } finally {
            conectar.close();
        }
    }

    public static void modificarProcedimiento() throws SQLException {
        Connection conectar = null;
        try {
            conectar = Conexion.conectar();
            conectar.setAutoCommit(false);

            System.out.println("Introduzca el numevo nombre");
            nombre = leo.next();
            System.out.println("Introduzca un nuevo telefono");
            telefono = leo.next();
            System.out.println("Introduzca un nuevo email");
            email = leo.next();
            System.out.println("Introduzca el id a modificar");
            int n = leo.nextInt();

            CallableStatement prcProcedimientoAlmacenado = conectar.prepareCall("{call Actualizar(?,?,?,?)}");

            prcProcedimientoAlmacenado.setString(1, nombre);
            prcProcedimientoAlmacenado.setString(2, telefono);
            prcProcedimientoAlmacenado.setString(3, email);
            prcProcedimientoAlmacenado.setInt(4, n);

            prcProcedimientoAlmacenado.execute();
            conectar.commit();
            System.out.println("Registro actualizado");

        } catch (Exception e) {
            conectar.rollback();
            e.printStackTrace();
        } finally {
            conectar.close();
        }

    }
}
