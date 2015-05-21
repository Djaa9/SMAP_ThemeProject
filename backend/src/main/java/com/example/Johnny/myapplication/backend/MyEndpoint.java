/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.Johnny.myapplication.backend;




import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.utils.SystemProperty;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An endpoint class we are exposing
 */
@Api(name = "myApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.myapplication.Johnny.example.com", ownerName = "backend.myapplication.Johnny.example.com", packagePath = ""))
public class MyEndpoint {

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) throws ClassNotFoundException, SQLException {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        insertWalk();

        return response;
    }

    private void insertWalk() throws ClassNotFoundException, SQLException {

        String url = null;
        if (SystemProperty.environment.value() ==
                SystemProperty.Environment.Value.Production) {
            // Connecting from App Engine.
            // Load the class that provides the "jdbc:google:mysql://"
            // prefix.
            Class.forName("com.mysql.jdbc.GoogleDriver");
            url =
                    "jdbc:google:mysql://articulate-fort-94709:nebuchadnezzar?user=root";
        } else {
            // Connecting from an external network.
            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://173.194.249.188:3306?user=root";
        }


        Connection conn = DriverManager.getConnection(url);
        ResultSet rs = conn.createStatement().executeQuery(
                "INSERT INTO walksTable (fbID, length) VALUES (1448625556,2500.25);");

    }



}
