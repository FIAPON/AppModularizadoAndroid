package br.com.heiderlopes.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.heiderlopes.data.local.model.ProductCache

@Database(version = 1, entities = [ProductCache::class])
abstract class ProductDataBase: RoomDatabase() {
    abstract fun productDao(): ProductsDao

    companion object {
        fun createDataBase(context: Context): ProductsDao {
            return Room
                .databaseBuilder(context, ProductDataBase::class.java, "Products.db")
                .build()
                .productDao()
        }
    }
}
