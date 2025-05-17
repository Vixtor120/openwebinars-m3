package org.example;

import org.example.dao.CategoriaDao;
import org.example.dao.CategoriaDaoImpl;
import org.example.dao.ProductoDao;
import org.example.dao.ProductoDaoImpl;
import org.example.model.Categoria;
import org.example.model.Producto;

import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

public class Menu {

    private KeyboardReader reader;
    private ProductoDao productoDao;
    private CategoriaDao categoriaDao;

    public Menu() {
        reader = new KeyboardReader();
        productoDao = ProductoDaoImpl.getInstance();
        categoriaDao = CategoriaDaoImpl.getInstance();
    }

    public void init() {

        int opcion;

        do {
            menuPrincipal();
            opcion = reader.nextInt();

            switch (opcion) {
                case 1: submenuProductos();
                    break;
                case 2: submenuCategorias();
                    break;
                case 0:
                    System.out.println("\nSaliendo del programa...\n");
                    break;
                default:
                    System.err.println("\nEl número introducido no se corresponde con una operación válida\n\n");
            }

        } while (opcion != 0);
    }

    public void menuPrincipal() {
        System.out.println("SISTEMA DE GESTIÓN DE PRODUCTOS Y CATEGORÍAS");
        System.out.println("==========================================\n");
        System.out.println("\n-> Introduzca una opción de entre las siguientes\n");
        System.out.println("0: Salir");
        System.out.println("1: Gestionar Productos");
        System.out.println("2: Gestionar Categorías");
        System.out.print("\nOpción: ");
    }

    // ===== SUBMENU PRODUCTOS =====

    public void submenuProductos() {
        int opcion;

        do {
            menuProductos();
            opcion = reader.nextInt();

            switch (opcion) {
                case 1: listarProductos();
                    break;
                case 2: listarProductoPorId();
                    break;
                case 3: insertarProducto();
                    break;
                case 4: actualizarProducto();
                    break;
                case 5: eliminarProducto();
                    break;
                case 6: buscarProductoPorNombre();
                    break;
                case 7: buscarProductoPorCategoria();
                    break;
                case 8: buscarProductoPorRangoPrecio();
                    break;
                case 0:
                    System.out.println("\nVolviendo al menú principal...\n");
                    break;
                default:
                    System.err.println("\nEl número introducido no se corresponde con una operación válida\n\n");
            }

        } while (opcion != 0);
    }

    public void menuProductos() {
        System.out.println("GESTIÓN DE PRODUCTOS");
        System.out.println("==================\n");
        System.out.println("\n-> Introduzca una opción de entre las siguientes\n");
        System.out.println("0: Volver al menú principal");
        System.out.println("1: Listar todos los productos");
        System.out.println("2: Listar un producto por su ID");
        System.out.println("3: Insertar un nuevo producto");
        System.out.println("4: Actualizar un producto");
        System.out.println("5: Eliminar un producto");
        System.out.println("6: Buscar productos por nombre");
        System.out.println("7: Buscar productos por categoría");
        System.out.println("8: Buscar productos por rango de precio");
        System.out.print("\nOpción: ");
    }

    public void insertarProducto() {
        System.out.println("\nINSERCIÓN DE UN NUEVO PRODUCTO");
        System.out.println("-----------------------------\n");

        try {
            System.out.print("Introduzca el nombre del producto: ");
            String nombre = reader.nextLine();

            System.out.print("Introduzca el precio del producto: ");
            BigDecimal precio = new BigDecimal(reader.nextLine());

            // Mostrar las categorías disponibles
            List<Categoria> categorias = categoriaDao.getAll();
            if (categorias.isEmpty()) {
                System.out.println("No hay categorías disponibles. Debe crear una categoría primero.");
                return;
            }

            System.out.println("Categorías disponibles:");
            for (Categoria cat : categorias) {
                System.out.println(cat.getId_categoria() + ": " + cat.getNombre());
            }

            System.out.print("Introduzca el ID de la categoría: ");
            int idCategoria = reader.nextInt();

            Categoria categoria = categoriaDao.getById(idCategoria);
            if (categoria == null) {
                System.out.println("La categoría seleccionada no existe.");
                return;
            }

            Producto producto = new Producto(nombre, precio, categoria);
            int id = productoDao.add(producto);

            System.out.println("Nuevo producto registrado con ID: " + id);

        } catch (SQLException ex) {
            System.err.println("Error insertando el nuevo registro en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        } catch (NumberFormatException ex) {
            System.err.println("Error: El precio o ID de categoría no tiene el formato correcto.");
        }

        System.out.println("");
    }

    public void listarProductos() {
        System.out.println("\nLISTADO DE TODOS LOS PRODUCTOS");
        System.out.println("-----------------------------\n");

        try {
            List<Producto> productos = productoDao.getAll();

            if (productos.isEmpty())
                System.out.println("No hay productos registrados en la base de datos");
            else {
                printCabeceraTablaProducto();
                productos.forEach(this::printProducto);
                System.out.println("\n");
            }

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");
    }

    public void listarProductoPorId() {
        System.out.println("\nBÚSQUEDA DE PRODUCTOS POR ID");
        System.out.println("---------------------------\n");

        try {
            System.out.print("Introduzca el ID del producto a buscar: ");
            int id = reader.nextInt();

            Producto producto = productoDao.getById(id);

            if (producto == null)
                System.out.println("No hay productos registrados en la base de datos con ese ID");
            else {
                printCabeceraTablaProducto();
                printProducto(producto);
                System.out.println("\n");
            }

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");
    }

    public void actualizarProducto() {
        System.out.println("\nACTUALIZACIÓN DE UN PRODUCTO");
        System.out.println("---------------------------\n");

        try {
            System.out.print("Introduzca el ID del producto a actualizar: ");
            int id = reader.nextInt();

            Producto producto = productoDao.getById(id);

            if (producto == null) {
                System.out.println("No hay productos registrados en la base de datos con ese ID");
                return;
            }

            printCabeceraTablaProducto();
            printProducto(producto);
            System.out.println("\n");

            System.out.printf("Introduzca el nombre del producto (%s): ", producto.getNombre());
            String nombre = reader.nextLine();
            nombre = (nombre.isBlank()) ? producto.getNombre() : nombre;

            System.out.printf("Introduzca el precio del producto (%s): ", producto.getPrecio());
            String strPrecio = reader.nextLine();
            BigDecimal precio = (strPrecio.isBlank()) ? producto.getPrecio() : new BigDecimal(strPrecio);

            // Mostrar las categorías disponibles
            List<Categoria> categorias = categoriaDao.getAll();
            System.out.println("Categorías disponibles:");
            for (Categoria cat : categorias) {
                System.out.println(cat.getId_categoria() + ": " + cat.getNombre());
            }

            System.out.printf("Introduzca el ID de la categoría (%s - %s): ",
                    producto.getId_categoria(),
                    (producto.getCategoria() != null ? producto.getCategoria().getNombre() : "Desconocida"));

            String strIdCategoria = reader.nextLine();
            int idCategoria = (strIdCategoria.isBlank()) ? producto.getId_categoria() : Integer.parseInt(strIdCategoria);

            Categoria categoria = categoriaDao.getById(idCategoria);
            if (categoria == null) {
                System.out.println("La categoría seleccionada no existe. No se actualizará la categoría.");
                categoria = producto.getCategoria();
                idCategoria = producto.getId_categoria();
            }

            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setId_categoria(idCategoria);
            producto.setCategoria(categoria);

            productoDao.update(producto);

            System.out.println("");
            System.out.printf("Producto con ID %s actualizado", producto.getId_producto());
            System.out.println("");

        } catch (SQLException ex) {
            System.err.println("Error actualizando el producto en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        } catch (NumberFormatException ex) {
            System.err.println("Error: El precio o ID de categoría no tiene el formato correcto.");
        }

        System.out.println("");
    }

    public void eliminarProducto() {
        System.out.println("\nBORRADO DE UN PRODUCTO");
        System.out.println("---------------------\n");

        try {
            System.out.print("Introduzca el ID del producto a borrar: ");
            int id = reader.nextInt();

            System.out.printf("¿Está usted seguro de querer eliminar el producto con ID=%s? (s/n): ", id);
            String borrar = reader.nextLine();

            if (borrar.equalsIgnoreCase("s")) {
                productoDao.delete(id);
                System.out.printf("El producto con ID %s se ha borrado\n", id);
            }

        } catch (SQLException ex) {
            System.err.println("Error eliminando el producto en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");
    }

    public void buscarProductoPorNombre() {
        System.out.println("\nBÚSQUEDA DE PRODUCTOS POR NOMBRE");
        System.out.println("-------------------------------\n");

        try {
            System.out.print("Introduzca el nombre o parte del nombre a buscar: ");
            String nombre = reader.nextLine();

            List<Producto> productos = productoDao.findByName(nombre);

            if (productos.isEmpty())
                System.out.println("No hay productos que coincidan con ese criterio de búsqueda");
            else {
                printCabeceraTablaProducto();
                productos.forEach(this::printProducto);
                System.out.println("\n");
            }

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");
    }

    public void buscarProductoPorCategoria() {
        System.out.println("\nBÚSQUEDA DE PRODUCTOS POR CATEGORÍA");
        System.out.println("---------------------------------\n");

        try {
            // Mostrar las categorías disponibles
            List<Categoria> categorias = categoriaDao.getAll();
            if (categorias.isEmpty()) {
                System.out.println("No hay categorías disponibles.");
                return;
            }

            System.out.println("Categorías disponibles:");
            for (Categoria cat : categorias) {
                System.out.println(cat.getId_categoria() + ": " + cat.getNombre());
            }

            System.out.print("Introduzca el ID de la categoría: ");
            int idCategoria = reader.nextInt();

            List<Producto> productos = productoDao.findByCategoria(idCategoria);

            if (productos.isEmpty())
                System.out.println("No hay productos que pertenezcan a esa categoría");
            else {
                printCabeceraTablaProducto();
                productos.forEach(this::printProducto);
                System.out.println("\n");
            }

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");
    }

    public void buscarProductoPorRangoPrecio() {
        System.out.println("\nBÚSQUEDA DE PRODUCTOS POR RANGO DE PRECIO");
        System.out.println("---------------------------------------\n");

        try {
            System.out.print("Introduzca el precio mínimo: ");
            BigDecimal minPrecio = new BigDecimal(reader.nextLine());

            System.out.print("Introduzca el precio máximo: ");
            BigDecimal maxPrecio = new BigDecimal(reader.nextLine());

            if (minPrecio.compareTo(maxPrecio) > 0) {
                System.out.println("Error: El precio mínimo no puede ser mayor que el precio máximo.");
                return;
            }

            List<Producto> productos = productoDao.findByPriceRange(minPrecio, maxPrecio);

            if (productos.isEmpty())
                System.out.println("No hay productos dentro de ese rango de precios");
            else {
                printCabeceraTablaProducto();
                productos.forEach(this::printProducto);
                System.out.println("\n");
            }

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        } catch (NumberFormatException ex) {
            System.err.println("Error: Los valores de precio no tienen el formato correcto.");
        }

        System.out.println("");
    }

    private void printCabeceraTablaProducto() {
        System.out.printf("%3s %30s %10s %20s", "ID", "NOMBRE", "PRECIO", "CATEGORÍA");
        System.out.println("");
        IntStream.range(1, 80).forEach(x -> System.out.print("-"));
        System.out.println("\n");
    }

    private void printProducto(Producto prod) {
        System.out.printf("%3d %30s %10.2f %20s\n",
                prod.getId_producto(),
                prod.getNombre(),
                prod.getPrecio(),
                (prod.getCategoria() != null ? prod.getCategoria().getNombre() : "Sin categoría"));
    }

    // ===== SUBMENU CATEGORÍAS =====

    public void submenuCategorias() {
        int opcion;

        do {
            menuCategorias();
            opcion = reader.nextInt();

            switch (opcion) {
                case 1: listarCategorias();
                    break;
                case 2: listarCategoriaPorId();
                    break;
                case 3: insertarCategoria();
                    break;
                case 4: actualizarCategoria();
                    break;
                case 5: eliminarCategoria();
                    break;
                case 6: buscarCategoriaPorNombre();
                    break;
                case 0:
                    System.out.println("\nVolviendo al menú principal...\n");
                    break;
                default:
                    System.err.println("\nEl número introducido no se corresponde con una operación válida\n\n");
            }

        } while (opcion != 0);
    }

    public void menuCategorias() {
        System.out.println("GESTIÓN DE CATEGORÍAS");
        System.out.println("===================\n");
        System.out.println("\n-> Introduzca una opción de entre las siguientes\n");
        System.out.println("0: Volver al menú principal");
        System.out.println("1: Listar todas las categorías");
        System.out.println("2: Listar una categoría por su ID");
        System.out.println("3: Insertar una nueva categoría");
        System.out.println("4: Actualizar una categoría");
        System.out.println("5: Eliminar una categoría");
        System.out.println("6: Buscar categorías por nombre");
        System.out.print("\nOpción: ");
    }

    public void insertarCategoria() {
        System.out.println("\nINSERCIÓN DE UNA NUEVA CATEGORÍA");
        System.out.println("-------------------------------\n");

        try {
            System.out.print("Introduzca el nombre de la categoría: ");
            String nombre = reader.nextLine();

            Categoria categoria = new Categoria(nombre);
            int id = categoriaDao.add(categoria);

            System.out.println("Nueva categoría registrada con ID: " + id);

        } catch (SQLException ex) {
            System.err.println("Error insertando el nuevo registro en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");
    }

    public void listarCategorias() {
        System.out.println("\nLISTADO DE TODAS LAS CATEGORÍAS");
        System.out.println("------------------------------\n");

        try {
            List<Categoria> categorias = categoriaDao.getAll();

            if (categorias.isEmpty())
                System.out.println("No hay categorías registradas en la base de datos");
            else {
                printCabeceraTablaCategoria();
                categorias.forEach(this::printCategoria);
                System.out.println("\n");
            }

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");
    }

    public void listarCategoriaPorId() {
        System.out.println("\nBÚSQUEDA DE CATEGORÍAS POR ID");
        System.out.println("----------------------------\n");

        try {
            System.out.print("Introduzca el ID de la categoría a buscar: ");
            int id = reader.nextInt();

            Categoria categoria = categoriaDao.getById(id);

            if (categoria == null)
                System.out.println("No hay categorías registradas en la base de datos con ese ID");
            else {
                printCabeceraTablaCategoria();
                printCategoria(categoria);
                System.out.println("\n");
            }

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");
    }

    public void actualizarCategoria() {
        System.out.println("\nACTUALIZACIÓN DE UNA CATEGORÍA");
        System.out.println("-----------------------------\n");

        try {
            System.out.print("Introduzca el ID de la categoría a actualizar: ");
            int id = reader.nextInt();

            Categoria categoria = categoriaDao.getById(id);

            if (categoria == null) {
                System.out.println("No hay categorías registradas en la base de datos con ese ID");
                return;
            }

            printCabeceraTablaCategoria();
            printCategoria(categoria);
            System.out.println("\n");

            System.out.printf("Introduzca el nombre de la categoría (%s): ", categoria.getNombre());
            String nombre = reader.nextLine();
            nombre = (nombre.isBlank()) ? categoria.getNombre() : nombre;

            categoria.setNombre(nombre);

            categoriaDao.update(categoria);

            System.out.println("");
            System.out.printf("Categoría con ID %s actualizada", categoria.getId_categoria());
            System.out.println("");

        } catch (SQLException ex) {
            System.err.println("Error actualizando la categoría en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");
    }

    public void eliminarCategoria() {
        System.out.println("\nBORRADO DE UNA CATEGORÍA");
        System.out.println("----------------------\n");

        try {
            System.out.print("Introduzca el ID de la categoría a borrar: ");
            int id = reader.nextInt();

            System.out.printf("¿Está usted seguro de querer eliminar la categoría con ID=%s? (s/n): ", id);
            String borrar = reader.nextLine();

            if (borrar.equalsIgnoreCase("s")) {
                try {
                    categoriaDao.delete(id);
                    System.out.printf("La categoría con ID %s se ha borrado\n", id);
                } catch (SQLException ex) {
                    if (ex.getMessage().contains("foreign key constraint")) {
                        System.out.println("No se puede eliminar la categoría porque tiene productos asociados.");
                        System.out.println("Elimine primero los productos de esta categoría e inténtelo de nuevo.");
                    } else {
                        throw ex;
                    }
                }
            }

        } catch (SQLException ex) {
            System.err.println("Error eliminando la categoría en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");
    }

    public void buscarCategoriaPorNombre() {
        System.out.println("\nBÚSQUEDA DE CATEGORÍAS POR NOMBRE");
        System.out.println("--------------------------------\n");

        try {
            System.out.print("Introduzca el nombre o parte del nombre a buscar: ");
            String nombre = reader.nextLine();

            List<Categoria> categorias = categoriaDao.findByName(nombre);

            if (categorias.isEmpty())
                System.out.println("No hay categorías que coincidan con ese criterio de búsqueda");
            else {
                printCabeceraTablaCategoria();
                categorias.forEach(this::printCategoria);
                System.out.println("\n");
            }

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");
    }

    private void printCabeceraTablaCategoria() {
        System.out.printf("%3s %30s", "ID", "NOMBRE");
        System.out.println("");
        IntStream.range(1, 40).forEach(x -> System.out.print("-"));
        System.out.println("\n");
    }

    private void printCategoria(Categoria cat) {
        System.out.printf("%3d %30s\n",
                cat.getId_categoria(),
                cat.getNombre());
    }

    static class KeyboardReader {

        BufferedReader br;
        StringTokenizer st;

        public KeyboardReader() {
            br = new BufferedReader(
                    new InputStreamReader(System.in)
            );
        }

        String next() {

            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException ex) {
                    System.err.println("Error leyendo del teclado");
                    ex.printStackTrace();
                }
            }
            return st.nextToken();

        }

        int nextInt() { return Integer.parseInt(next()); }

        double nextDouble() { return Double.parseDouble(next()); }

        String nextLine() {

            String str = "";
            try {
                if (st != null && st.hasMoreElements())
                    str = st.nextToken("\n");
                else
                    str = br.readLine();
            } catch (IOException ex) {
                System.err.println("Error leyendo del teclado");
                ex.printStackTrace();
            }
            return str;
        }
    }
}