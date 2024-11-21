package com.example.nicolaskaplan_comp304lab_ex1.networkServices

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.nicolaskaplan_comp304lab_ex1.dataClasses.FavoriteLocation

@Dao
interface FavoriteLocationDao {
    @Insert
    suspend fun insertLocation(location: FavoriteLocation)

    @Query("SELECT * FROM favorite_locations")
    suspend fun getAllLocations(): List<FavoriteLocation>

    @Delete
    suspend fun deleteLocation(location: FavoriteLocation)

    @Update
    suspend fun updateLocation(location: FavoriteLocation)
}