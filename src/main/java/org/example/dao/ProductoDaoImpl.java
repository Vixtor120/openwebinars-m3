package org.example.dao;

import org.example.model.Categoria;
import org.example.model.Producto;
import org.example.pool.MyDataSource;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoDaoImpl implements ProductoDao {

    private static ProductoDaoImpl instance;
    private final CategoriaDao categoriaDao;

    static {
        instance = new ProductoDaoImpl();
    }

    private ProductoDaoImpl() {
        categoriaDao = CategoriaDaoImpl.getInstance();
    }

    public static ProductoDaoImpl getInstance() {
        return instance;
    }

    @Override
    public int add(Producto producto) throws SQLException {
        String sql = "INSERT INTO producto (nombre, precio, id_categoria) VALUES (?, ?, ?)";
        int result = 0;
        int generatedId = 0;

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setString(1, producto.getNombre());
            pstm.setBigDecimal(2, producto.getPrecio());
            pstm.setInt(3, producto.getId_categoria());
            result = pstm.executeUpdate();

            if (result > 0) {
                try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                        producto.setId_producto(generatedId);
                    }
                }
            }
        }

        return generatedId;
    }

    @Override
    public Producto getById(int id) throws SQLException {
        Producto result = null;
        String sql = "SELECT * FROM producto WHERE id_producto = ?";

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    result = new Producto();
                    result.setId_producto(rs.getInt("id_producto"));
                    result.setNombre(rs.getString("nombre"));
                    result.setPrecio(rs.getBigDecimal("precio"));
                    result.setId_categoria(rs.getInt("id_categoria"));

                    // Obtener el objeto Categoria relacionado
                    Categoria categoria = categoriaDao.getById(result.getId_categoria());
                    result.setCategoria(categoria);
                }
            }
        }

        return result;
    }

    @Override
    public List<Producto> getAll() throws SQLException {
        String sql = "SELECT * FROM producto";
        List<Producto> result = new ArrayList<>();

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            Producto producto;

            while (rs.next()) {
                producto = new Producto();
                producto.setId_producto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getBigDecimal("precio"));
                producto.setId_categoria(rs.getInt("id_categoria"));

                // Obtener el objeto Categoria relacionado
                Categoria categoria = categoriaDao.getById(producto.getId_categoria());
                producto.setCategoria(categoria);

                result.add(producto);
            }
        }

        return result;
    }

    @Override
    public int update(Producto producto) throws SQLException {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, id_categoria = ? WHERE id_producto = ?";
        int result;

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, producto.getNombre());
            pstm.setBigDecimal(2, producto.getPrecio());
            pstm.setInt(3, producto.getId_categoria());
            pstm.setInt(4, producto.getId_producto());

            result = pstm.executeUpdate();
        }

        return result;
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id_producto = ?";

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);
            pstm.executeUpdate();
        }
    }

    @Override
    public List<Producto> findByName(String nombre) throws SQLException {
        String sql = "SELECT * FROM producto WHERE nombre LIKE ?";
        List<Producto> result = new ArrayList<>();

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, "%" + nombre + "%");

            try (ResultSet rs = pstm.executeQuery()) {
                Producto producto;

                while (rs.next()) {
                    producto = new Producto();
                    producto.setId_producto(rs.getInt("id_producto"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setPrecio(rs.getBigDecimal("precio"));
                    producto.setId_categoria(rs.getInt("id_categoria"));

                    // Obtener el objeto Categoria relacionado
                    Categoria categoria = categoriaDao.getById(producto.getId_categoria());
                    producto.setCategoria(categoria);

                    result.add(producto);
                }
            }
        }

        return result;
    }

    @Override
    public List<Producto> findByCategoria(int categoriaId) throws SQLException {
        String sql = "SELECT * FROM producto WHERE id_categoria = ?";
        List<Producto> result = new ArrayList<>();

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, categoriaId);

            try (ResultSet rs = pstm.executeQuery()) {
                Producto producto;
                Categoria categoria = null;

                // Obtener la categoría una sola vez
                if (categoriaId > 0) {
                    categoria = categoriaDao.getById(categoriaId);
                }

                while (rs.next()) {
                    producto = new Producto();
                    producto.setId_producto(rs.getInt("id_producto"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setPrecio(rs.getBigDecimal("precio"));
                    producto.setId_categoria(rs.getInt("id_categoria"));
                    producto.setCategoria(categoria);
                    result.add(producto);
                }
            }
        }

        return result;
    }

    @Override
    public List<Producto> findByPriceRange(BigDecimal minPrecio, BigDecimal maxPrecio) throws SQLException {
        String sql = "SELECT * FROM producto WHERE precio BETWEEN ? AND ?";
        List<Producto> result = new ArrayList<>();

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setBigDecimal(1, minPrecio);
            pstm.setBigDecimal(2, maxPrecio);

            try (ResultSet rs = pstm.executeQuery()) {
                Producto producto;

                while (rs.next()) {
                    producto = new Producto();
                    producto.setId_producto(rs.getInt("id_producto"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setPrecio(rs.getBigDecimal("precio"));
                    producto.setId_categoria(rs.getInt("id_categoria"));

                    // Obtener el objeto Categoria relacionado
                    Categoria categoria = categoriaDao.getById(producto.getId_categoria());
                    producto.setCategoria(categoria);

                    result.add(producto);
                }
            }
        }

        return result;
    }
}
