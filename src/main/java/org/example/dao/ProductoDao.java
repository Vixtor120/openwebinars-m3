package org.example.dao;

import org.example.model.Producto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface ProductoDao {

    /**
     * Añade un nuevo producto a la base de datos
     * @param producto El producto a añadir
     * @return El ID del producto añadido
     * @throws SQLException Si ocurre algún error en la base de datos
     */
    public int add(Producto producto) throws SQLException;

    /**
     * Obtiene un producto por su ID
     * @param id El ID del producto a obtener
     * @return El producto con el ID especificado o null si no existe
     * @throws SQLException Si ocurre algún error en la base de datos
     */
    public Producto getById(int id) throws SQLException;

    /**
     * Obtiene todos los productos de la base de datos
     * @return Lista con todos los productos
     * @throws SQLException Si ocurre algún error en la base de datos
     */
    public List<Producto> getAll() throws SQLException;

    /**
     * Actualiza los datos de un producto
     * @param producto El producto con los datos actualizados
     * @return El número de filas afectadas (normalmente 1)
     * @throws SQLException Si ocurre algún error en la base de datos
     */
    public int update(Producto producto) throws SQLException;

    /**
     * Elimina un producto por su ID
     * @param id El ID del producto a eliminar
     * @throws SQLException Si ocurre algún error en la base de datos
     */
    public void delete(int id) throws SQLException;

    /**
     * Busca productos por nombre
     * @param nombre El nombre o parte del nombre a buscar
     * @return Lista de productos que coinciden con el criterio de búsqueda
     * @throws SQLException Si ocurre algún error en la base de datos
     */
    public List<Producto> findByName(String nombre) throws SQLException;

    /**
     * Obtiene productos por categoría
     * @param categoriaId El ID de la categoría
     * @return Lista de productos que pertenecen a la categoría especificada
     * @throws SQLException Si ocurre algún error en la base de datos
     */
    public List<Producto> findByCategoria(int categoriaId) throws SQLException;

    /**
     * Busca productos por rango de precio
     * @param minPrecio Precio mínimo
     * @param maxPrecio Precio máximo
     * @return Lista de productos dentro del rango de precio especificado
     * @throws SQLException Si ocurre algún error en la base de datos
     */
    public List<Producto> findByPriceRange(BigDecimal minPrecio, BigDecimal maxPrecio) throws SQLException;
}