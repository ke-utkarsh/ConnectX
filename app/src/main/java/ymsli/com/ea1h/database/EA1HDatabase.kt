/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   30/1/20 1:24 PM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * Validator : This Activity is initializes the application
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ymsli.com.ea1h.database.dao.*
import ymsli.com.ea1h.database.entity.*
import javax.inject.Singleton

@Singleton
@Database(entities = [TripEntity::class,UserEntity::class,BikeEntity::class,MiscDataEntity::class,
    BTCommandsEntity::class,FilterBikeEntity::class,BTCommandsLogEntity::class,
    SliderImage::class, DAPIoTFileEntity::class, TripDetailEntity::class,
    LatLongEntity::class, GyroEntity::class, AccelerometerEntity::class,AdvPacketEntity::class],version = 2,exportSchema = false)

abstract class EA1HDatabase : RoomDatabase(){
    abstract fun tripDao():TripDao
    abstract fun userDao():UserDao
    abstract fun bikeDao(): BikeDao
    abstract fun btCommandsDao(): BTCommandsDao
    abstract fun miscDataDao(): MiscDataDao
    abstract fun filterBikeDao(): FilterBikeDao
    abstract fun btCommandsLogDao():BTCommandsLogDao
    abstract fun dapIoTFileDao():DAPIoTFileDao
    abstract fun tripDetailDao():TripDetailDao
    abstract fun latLongDao():LatLongDao
    abstract fun gyroDao(): GyroDao
    abstract fun accelerometerDao(): AccelerometerDao
    abstract fun advPacketDao(): AdvPacketDao
}