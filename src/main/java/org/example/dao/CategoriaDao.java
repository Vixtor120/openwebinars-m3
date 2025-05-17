package org.example.dao;

import org.example.model.Categoria;

import java.sql.SQLException;
import java.util.List;

public interface CategoriaDao {

    /**
     * Añade una nueva categoría a la base de datos
     * @param categoria La categoría a añadir
     * @return El ID de la categoría añadida
     * @throws SQLException Si ocurre algún error en la base de datos
     */
    public int add(Categoria categoria) throws SQLException;

    /**
     * Obtiene una categoría por su ID
     * @param id El ID de la categoría a obtener
     * @return La categoría con el ID especificado o null si no existe
     * @throws SQLException Si ocurre algún error en la base de datos
     */
    public Categoria getById(int id) throws SQLException;

    /**
     * Obtiene todas las categorías de la base de datos
     * @return Lista con todas las categorías
     * @throws SQLException Si ocurre algún error en la base de datos
     */
    public List<Categoria> getAll() throws SQLException;

    /**
     * Actualiza los datos de una categoría
     * @param categoria La categoría con los datos actualizados
     * @return El número de filas afectadas (normalmente 1)
     * @throws SQLException Si ocurre algún error en la base de datos
     */
    public int update(Categoria categoria) throws SQLException;

    /**
     * Elimina una categoría por su ID
     * @param id El ID de la categoría a eliminar
     * @throws SQLException Si ocurre algún error en la base de datos
     */
    public void delete(int id) throws SQLException;

    /**
     * Busca categorías por nombre
     * @param nombre El nombre o parte del nombre a buscar
     * @return Lista de categorías que coinciden con el criterio de búsqueda
     * @throws SQLException Si ocurre algún error en la base de datos
     */
    public List<Categoria> findByName(String nombre) throws SQLException;
}