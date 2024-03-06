package connections;

import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Registrarse {


    // Metodo que realiza la conexion a la base de datos
    public static boolean registrarUsuario(String correo, String nombre, String contrasegna, int edad, String genero) {
        Connection connection = null;
        try {
            // Configurar la conexión
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://databases.cjec0q406ejw.eu-west-3.rds.amazonaws.com:5432/databasebpm";
            String username = "barbacil";
            String password = "admin777";

            // Establecer la conexión
            connection = DriverManager.getConnection(url, username, password);

            // Insertar un nuevo usuario
            String insertQuery = "INSERT INTO usuario (email, nombre, password, edad, genero, esAdmin, salario, puntuacion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                // Establecer los valores del nuevo usuario
                preparedStatement.setString(1, correo);
                preparedStatement.setString(2, nombre);
                preparedStatement.setString(3, contrasegna);
                preparedStatement.setInt(4, edad);
                preparedStatement.setString(5, genero);
                preparedStatement.setBoolean(6, false);
                preparedStatement.setDouble(7, 0.0);
                preparedStatement.setDouble(8, 0.0);

                // Ejecutar la inserción
                preparedStatement.executeUpdate();
            }

            // Cerrar la conexión
            connection.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al registrar un nuevo usuario en el servidor");

            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
