package com.example.milsabores.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.milsabores.data.local.dao.BlogDao
import com.example.milsabores.data.local.dao.CarritoDao
import com.example.milsabores.data.local.dao.ProductoDao
import com.example.milsabores.data.local.dao.UsuarioDao
import com.example.milsabores.data.local.entity.Blog
import com.example.milsabores.data.local.entity.ItemCarrito
import com.example.milsabores.data.local.entity.Producto
import com.example.milsabores.data.local.entity.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

@Database(
    entities = [Producto::class, Usuario::class, Blog::class, ItemCarrito::class],
    version = 2,
    exportSchema = false
)
abstract class PasteleriaDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun blogDao(): BlogDao

    companion object {
        @Volatile
        private var INSTANCIA: PasteleriaDatabase? = null

        fun obtenerInstancia(contexto: Context): PasteleriaDatabase {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    contexto.applicationContext,
                    PasteleriaDatabase::class.java,
                    "pasteleria_db"
                )
                    // --- ¡AÑADIMOS EL CALLBACK AQUÍ! ---
                    .addCallback(DatabaseCallback(contexto, CoroutineScope(Dispatchers.IO)))
                    .build()

                INSTANCIA = instancia
                instancia
            }
        }
    }
}

// --- ESTA ES LA LÓGICA NUEVA PARA CARGAR EL JSON ---
private class DatabaseCallback(
    private val context: Context,
    private val scope: CoroutineScope
) : RoomDatabase.Callback() {

    // 'onCreate' se llama UNA SOLA VEZ, cuando la BD se crea por primera vez
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch {
            // Obtenemos los DAOs
            val db = PasteleriaDatabase.obtenerInstancia(context)
            val productoDao = db.productoDao()
            val usuarioDao = db.usuarioDao()
            val blogDao = db.blogDao()

            // Leemos el archivo JSON desde 'assets'
            val jsonString = context.assets.open("data.json").bufferedReader().use { it.readText() }
            val json = JSONObject(jsonString)

            // 1. Cargamos los Productos
            val productosArray = json.getJSONArray("productos")
            val listaProductos = mutableListOf<Producto>()
            for (i in 0 until productosArray.length()) {
                val p = productosArray.getJSONObject(i)
                listaProductos.add(
                    Producto(
                        id = p.getInt("id"),
                        codigo = p.getString("codigo"),
                        categoria = p.getString("categoria"),
                        nombre = p.getString("nombre"),
                        precio = p.getInt("precio"),
                        descripcion = p.getString("descripcion"),
                        imagen = p.getString("imagen"),
                        icono = p.getString("icono")
                    )
                )
            }
            // Insertamos todos los productos en la BD
            productoDao.insertarTodos(listaProductos)

            // 2. Cargamos los Usuarios
            val usuariosArray = json.getJSONArray("usuarios")
            val listaUsuarios = mutableListOf<Usuario>()
            for (i in 0 until usuariosArray.length()) {
                val u = usuariosArray.getJSONObject(i)
                listaUsuarios.add(
                    Usuario(
                        id = u.getInt("id"),
                        email = u.getString("email"),
                        password = u.getString("password"),
                        rol = u.getString("rol")
                    )
                )
            }
            usuarioDao.insertarUsuarios(listaUsuarios)

            // 3. Cargamos el Blog
            val blogArray = json.getJSONArray("blog")
            val listaBlog = mutableListOf<Blog>()
            for (i in 0 until blogArray.length()) {
                val b = blogArray.getJSONObject(i)
                listaBlog.add(
                    Blog(
                        id = b.getInt("id"),
                        categoria = b.getString("categoria"), // <-- NUEVO
                        titulo = b.getString("titulo"),
                        resumen = b.getString("resumen"),
                        fecha = b.getString("fecha"),
                        autor = b.getString("autor"), // <-- NUEVO
                        imagen = b.getString("imagen"),
                        contenido = b.getString("contenido") // <-- NUEVO
                    )
                )
            }
            blogDao.insertarTodas(listaBlog)
        }
    }
}
