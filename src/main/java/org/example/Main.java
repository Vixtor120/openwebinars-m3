package org.example;

import org.example.dao.CategoriaDao;
import org.example.dao.CategoriaDaoImpl;
import org.example.dao.ProductoDao;
import org.example.dao.ProductoDaoImpl;
import org.example.model.Categoria;
import org.example.model.Producto;
import org.example.pool.MyDataSource;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Victor Hidalgo
 */
public class Main {
    public static void main(String... param) {
//        // Verificamos la estructura de la base de datos
//        mostrarEstructuraDB();

        // Ejecutamos el menú interactivo
        Menu menu = new Menu();
        menu.init();
    }
}