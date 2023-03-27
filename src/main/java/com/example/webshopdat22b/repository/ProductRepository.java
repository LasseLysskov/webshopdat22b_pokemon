package com.example.webshopdat22b.repository;
import com.example.webshopdat22b.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {

    //database-properties

    @Value("${spring.datasource.url}")
    private String DB_URL;
    @Value("${spring.datasource.username}")
    private String UID;
    @Value("${spring.datasource.password}")
    private String PWD;

    public List<Product> getAll() {
        List<Product> productList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            Statement statement = connection.createStatement();
            final String SQL_QUERY = "SELECT * FROM Products";
            ResultSet resultSet = statement.executeQuery(SQL_QUERY);
            while(resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                double price = resultSet.getDouble(3);
                Product product = new Product(id, name, price);
                productList.add(product);
                System.out.println(product);
            }
        }
        catch(SQLException e)
        {
           System.out.println("Could not query database");
           e.printStackTrace();
        }

        return productList;
    }

    public void addProduct(Product product) {
        try {
            //connect to database
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            final String CREATE_QUERY = "INSERT INTO Products(name, price) VALUES(?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_QUERY);

            //set attributer i prepared statement
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());

            //execute statement
            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            System.out.println("Could not create product");
            e.printStackTrace();
        }
    }
    public void updateProduct(Product product) {
        try {
            //SQL statement
            final String UPDATE_QUERY = "UPDATE Products SET name = ?, price = ? WHERE id = ?";

            //Connect to DB
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);

            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);

            //set parameters
            String name = product.getName();
            double price = product.getPrice();
            int id = product.getId();
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, price);
            preparedStatement.setInt(3, id);

            //execute query
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
           System.out.println("Could not update the product");
           e.printStackTrace();
        }
    }

    public Product findProductById(int id) {
        Product foundProduct = new Product();
        foundProduct.setId(id);
        try {
            //SQL statement
            final String SQL_QUERY = "SELECT * FROM Products WHERE id = ?";

            //Connect to database
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);

            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY);

            //set parameters
            preparedStatement.setInt(1, id);

            //execute statement
            ResultSet resultSet = preparedStatement.executeQuery();

            //få product ud af resultset
            resultSet.next();
            String name = resultSet.getString(2);
            double price = resultSet.getDouble(3);
            foundProduct.setName(name);
            foundProduct.setPrice(price);
        }
        catch(SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        return foundProduct;
    }

    public void deleteProduct(int id) {
        try {
            //SQL statement
            final String DELETE_QUERY = "DELETE FROM Products WHERE id = ?";
            //Connect to database
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            //set parameters
            preparedStatement.setInt(1, id);
            //execute statement
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.out.println("Could not delete the product");
            e.printStackTrace();
        }
    }
}
