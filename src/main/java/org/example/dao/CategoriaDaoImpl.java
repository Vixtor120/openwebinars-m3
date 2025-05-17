package org.example.dao;

import org.example.model.Categoria;
import org.example.pool.MyDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDaoImpl implements CategoriaDao {

    private static CategoriaDaoImpl instance;

    static {
        instance = new CategoriaDaoImpl();
    }

    private CategoriaDaoImpl() {}

    public static CategoriaDaoImpl getInstance() {
        return instance;
    }

    @Override
    public int add(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria (nombre) VALUES (?)";
        int result = 0;
        int generatedId = 0;

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setString(1, categoria.getNombre());
            result = pstm.executeUpdate();

            if (result > 0) {
                try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                        categoria.setId_categoria(generatedId);
                    }
                }
            }
        }

        return generatedId;
    }

    @Override
    public Categoria getById(int id) throws SQLException {
        Categoria result = null;
        String sql = "SELECT * FROM categoria WHERE id_categoria = ?";

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    result = new Categoria();
                    result.setId_categoria(rs.getInt("id_categoria"));
                    result.setNombre(rs.getString("nombre"));
                }
            }
        }

        return result;
    }

    @Override
    public List<Categoria> getAll() throws SQLException {
        String sql = "SELECT * FROM categoria";
        List<Categoria> result = new ArrayList<>();

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            Categoria categoria;

            while (rs.next()) {
                categoria = new Categoria();
                categoria.setId_categoria(rs.getInt("id_categoria"));
                categoria.setNombre(rs.getString("nombre"));
                result.add(categoria);
            }
        }

        return result;
    }

    @Override
    public int update(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria SET nombre = ? WHERE id_categoria = ?";
        int result;

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, categoria.getNombre());
            pstm.setInt(2, categoria.getId_categoria());

            result = pstm.executeUpdate();
        }

        return result;
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);
            pstm.executeUpdate();
        }
    }

    @Override
    public List<Categoria> findByName(String nombre) throws SQLException {
        String sql = "SELECT * FROM categoria WHERE nombre LIKE ?";
        List<Categoria> result = new ArrayList<>();

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, "%" + nombre + "%");

            try (ResultSet rs = pstm.executeQuery()) {
                Categoria categoria;

                while (rs.next()) {
                    categoria = new Categoria();
                    categoria.setId_categoria(rs.getInt("id_categoria"));
                    categoria.setNombre(rs.getString("nombre"));
                    result.add(categoria);
                }
            }
        }

        return result;
    }
}