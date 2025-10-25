import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {

  private static final ArrayList<Producto> productosDB = obtenerProductosTecnologicos();
  private static Scanner entrada = new Scanner(System.in);

  public static void main(String[] args) {
    System.out.println("Te damos la bienvenida a la app de compras 🛒");
    label:
    while (true) {
      System.out.println("""
                    Ingrese el número equivalente a la opción:
                    0 - Finaliza el programa
                    1 - Crea un Producto
                    2 - Listar Productos
                    3 - Búsqueda por nombre
                    4 - Editar nombre producto
                    5 - Borrar producto
                    """);
      int opcionUsuario = entrada.nextInt();

      switch (opcionUsuario) {
        case 1 -> crearProducto(productosDB);
        case 2 -> listarProductos(productosDB);
        case 3 -> buscarProductoPorNombre(productosDB);
        case 4 -> editarProducto(productosDB);
        case 5 -> borrarProducto(productosDB);
        case 6 -> filtroPorPrecio(productosDB);
        case 0 -> {
          System.out.println("Gracias por usar la app!");
          break label; // corta el bucle donde se ejecuta
        }
        default -> System.out.println("Opción incorrecta, intente de nuevo");
      }
    }
  }

  public static void crearProducto(ArrayList<Producto> productos) {
    System.out.println("Creando Nuevo Producto");
    System.out.print("Ingrese el nombre del nuevo producto: ");
    String nombre = entrada.nextLine();
    double precio = entrada.nextDouble();
    String descripcion = entrada.nextLine();

    String categoria = entrada.nextLine();

    // TODO: cambiarlo cuando veamos static
    productos.add(new Producto(nombre, precio, descripcion, categoria));

    // TODO: agregar un mensaje de confirmación cuando se crea el producto
    pausa();
  }

  public static void listarProductos(ArrayList<Producto> productos) {
    System.out.println("==============================================================================================");
    System.out.println("                                  LISTA DE PRODUCTOS TECNOLÓGICOS                             ");
    System.out.println("==============================================================================================");

    if (productos == null || productos.isEmpty()) {
      System.out.println("⚠️  No hay productos para mostrar.");
    } else {
      System.out.printf("| %-3s | %-35s | %-10s | %-20s | %-15s |%n",
          "ID", "Nombre", "Precio", "Categoría", "Descripción");
      System.out.println("----------------------------------------------------------------------------------------------");

      for (Producto producto : productos) {
        System.out.printf("| %3d | %-35s | $%9.2f | %-20s | %-15s |%n",
            producto.getId(),
            producto.getNombre(),
            producto.getPrecio(),
            producto.getCategoria(),
            acortarDescripcion(producto.getDescripcion(), 15));
      }
    }

    System.out.println("==============================================================================================");
    pausa();
  }

  private static String acortarDescripcion(String descripcion, int maxLength) {
    if (descripcion.length() <= maxLength) {
      return descripcion;
    } else {
      return descripcion.substring(0, maxLength - 3) + "...";
    }
  }


  public static void buscarProductoPorNombre(ArrayList<Producto> productos) {
    System.out.println("Ingrese un nombre de un producto: ");
    String busqueda = entrada.nextLine();
    ArrayList<Producto> productoEncontrados = new ArrayList<>();

    for (Producto producto : productos) {
      if (estaIncluido(producto.getNombre(), busqueda)) {
        productoEncontrados.add(producto);
      }
    }

    listarProductos(productoEncontrados);
  }

  public static void editarProducto(List<Producto> productos) {
    // el listado de productos tiene las direcciones de memoria de los productos originales
    // aca obtenemos la direccion de memoria que nos permite modificar el objeto original
    // que es uno de los que esta en el listado
    Producto producto = obtenerProductoPorId(productos);
    // TODO: validar que encontramos el producto
    if (producto == null) {
      System.out.println("No se puede editar el producto.");
      pausa();
      return; // cuando hacemos el return en una funcion void, estamos cortando la ejecucion de la funcion
    }

    String nombreOriginal = producto.getNombre();
    System.out.println("Producto a editar:");
    System.out.println(nombreOriginal);
    // TODO: validar que el usuario quiere editar el producto que se encontro
    System.out.print("Ingrese el nuevo nombre: ");
    String nuevoNombre = entrada.nextLine();
    producto.setNombre(nuevoNombre);

    System.out.printf("El nombre del producto cambio de %s a %s", nombreOriginal, nuevoNombre);
  }

  public static void borrarProducto(List<Producto> productos) {
    Producto producto = obtenerProductoPorId(productos);
    // TODO: validar que encontramos el producto
    if (producto == null) {
      System.out.println("No pudimos borrar el producto");
      pausa();
      return; //
    }
    String nombreOriginal = producto.getNombre();
    System.out.println("Producto a borrar:");
    System.out.println(nombreOriginal);
    // TODO: validar que el usuario quiere borrar el producto que se encontro

    // aca borramos el producto
    productos.remove(producto);
    System.out.println("Borrado exitosamente!");
  }

  public static void filtroPorPrecio(List<Producto> productos) {
    double precioFiltro = entrada.nextDouble();

    ArrayList<Producto> productosFiltrados = new ArrayList<>();

    for (Producto producto : productos) {
      if (producto.getPrecio() <= precioFiltro) {
        productosFiltrados.add(producto);
      }
    }

    listarProductos(productosFiltrados);
  }

  /* UTILIDADES */
  /* Busqueda por id - ahora mismo solo funciona con el indice, en el futuro se va a cambiar */
  public static Producto obtenerProductoPorId(List<Producto> productos) {
    // TODO: validacion de datos
    System.out.println("Ingrese el id del producto: ");
    int idBusqueda = entrada.nextInt();

    for (Producto producto : productos) {
      if (producto.coincideId(idBusqueda)) {
        return producto;
      }
    }

    System.out.println("No pudimos encontrar el producto con el id: " + idBusqueda);
    return null; // el null representa que no encontramos el producto
  }

  public static boolean estaIncluido(String nombreCompleto, String nombreParcial) {
    String nombreCompletoFormateado = formatoBusqueda(nombreCompleto);

    return nombreCompletoFormateado.contains(formatoBusqueda(nombreParcial));
  }

  public static String formatoBusqueda(String texto) {
    return texto.trim().toLowerCase();
  }

  public static void pausa() {
    System.out.println("Pulse ENTER para continuar...");
    entrada = new Scanner(System.in);
    entrada.nextLine();
    for (int i = 0; i < 20; ++i) {
      System.out.println();
    }
    // TODO: limpiar la pantalla de la consola
  }

  public static ArrayList<Producto> obtenerProductosTecnologicos() {
    ArrayList<Producto> productos = new ArrayList<>();

    productos.add(new Producto(
        "Pala Head Graphene 360 Alpha Pro",
        320.00,
        "Pala de pádel con forma de diamante para máxima potencia y tacto excepcional.",
        "Palas Gama Alta"));

    productos.add(new Producto(
        "Paletero Adidas Multigame 2.0",
        95.50,
        "Bolso grande con compartimentos térmicos para palas y ventilación para zapatillas.",
        "Paleteros"));

    productos.add(new Producto(
        "Zapatillas Babolat Jet Premura 2",
        115.99,
        "Calzado específico para pádel, ligero y flexible, con excelente agarre en pista.",
        "Calzado"));

    productos.add(new Producto(
        "Pelotas Wilson Padel Speed 3 und.",
        6.75,
        "Bote de tres pelotas oficiales con excelente durabilidad y bote rápido.",
        "Pelotas"));

    productos.add(new Producto(
        "Grip Varlion Overgrip Comfort",
        4.99,
        "Cinta de agarre para la empuñadura, alta absorción de sudor y máximo confort.",
        "Accesorios"));

    productos.add(new Producto(
        "Muñequera StarVie Corta (Par)",
        8.50,
        "Muñequera de algodón para absorción de sudor y soporte de la articulación.",
        "Accesorios"));

    productos.add(new Producto(
        "Pala Nox ML10 Pro Cup",
        289.99,
        "Pala de control con formato redondo, muy manejable y amplia superficie de golpeo.",
        "Palas Gama Media"));

    productos.add(new Producto(
        "Mochila Bullpadel BPP-23001",
        45.00,
        "Mochila ligera con compartimento para pala y espacio para ropa y accesorios.",
        "Mochilas"));

    productos.add(new Producto(
        "Protector de Pala transparente",
        3.50,
        "Adhesivo protector para el marco de la pala, evita roturas y rasguños.",
        "Accesorios"));

    productos.add(new Producto(
        "Ropa técnica Siux Camiseta Dry",
        39.99,
        "Camiseta transpirable con tecnología Dry Comfort, ideal para partidos intensos.",
        "Indumentaria"));

    return productos;
  }
}