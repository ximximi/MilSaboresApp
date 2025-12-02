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
    version = 5,
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
                    .addCallback(DatabaseCallback(contexto, CoroutineScope(Dispatchers.IO)))
                    // ¡OJO! Como subiste a version 2, si ya tenías la 1 instalada
                    // esto fallará. Añade .fallbackToDestructiveMigration()
                    // si estás en desarrollo para que se recree la BD.
                    .fallbackToDestructiveMigration() // <-- ¡AÑADE ESTO!
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

            // 1. Cargamos los Productos (Tu código - sin cambios)
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
            productoDao.insertarTodos(listaProductos)

            // --- ¡¡INICIO DE LA CORRECCIÓN!! ---

            // 2. Cargamos los Usuarios (¡CORREGIDO!)
            val usuariosArray = json.getJSONArray("usuarios")
            val listaUsuarios = mutableListOf<Usuario>()
            for (i in 0 until usuariosArray.length()) {
                val u = usuariosArray.getJSONObject(i)
                listaUsuarios.add(
                    Usuario(
                        id = u.getInt("id"),
                        email = u.getString("email"),
                        password = u.getString("password"),
                        // ¡Estos campos ahora coinciden con Usuario.kt y data.json!
                        nombre = u.getString("nombre"),
                        direccion = if (u.isNull("direccion")) null else u.getString("direccion")
                    )
                )
            }
            // ¡Llamamos a la función DAO correcta!
            usuarioDao.insertarTodos(listaUsuarios)

            // --- ¡¡FIN DE LA CORRECCIÓN!! ---


            // 3. Cargamos el Blog (Tu código - sin cambios)
            val blogArray = json.getJSONArray("blog")
            val listaBlog = mutableListOf<Blog>()
            for (i in 0 until blogArray.length()) {
                val b = blogArray.getJSONObject(i)
                listaBlog.add(
                    Blog(
                        id = b.getInt("id"),
                        categoria = b.getString("categoria"),
                        titulo = b.getString("titulo"),
                        resumen = b.getString("resumen"),
                        fecha = b.getString("fecha"),
                        autor = b.getString("autor"),
                        imagen = b.getString("imagen"),
                        contenido = b.getString("contenido")
                    )
                )
            }
            blogDao.insertarTodas(listaBlog)
        }
    }
}