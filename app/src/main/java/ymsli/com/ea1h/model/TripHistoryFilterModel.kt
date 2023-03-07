package ymsli.com.ea1h.model

import ymsli.com.ea1h.adapters.VehicleListAdapter

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM023
 * @date   SEP 29, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TripHistoryFilterModel : Encapsulates all the filter state (Predicates)
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

data class TripHistoryFilterModel(
    var vehicles: ArrayList<VehicleListAdapter.VehicleModel>? = null,
    var date: Pair<Long, Long> = Pair(0, 0),
    var distance: Pair<Int, Int> = Pair(0, 100),
    var brakeCount: Pair<Int, Int> = Pair(0, 100),
    var avgSpeed: Pair<Int, Int> = Pair(0, 100)
){
    fun clone(): TripHistoryFilterModel{
        val list = vehicles?.map{VehicleListAdapter.VehicleModel(it.regNo, it.chassisNo, it.selected)}
            ?.toCollection(arrayListOf())
        return TripHistoryFilterModel(list,
            Pair(date.from, date.to),
            Pair(distance.from, distance.to),
            Pair(brakeCount.from, brakeCount.to),
            Pair(avgSpeed.from, avgSpeed.to))
    }

    /** Reset this filter model to its default state */
    fun resetToDefault(){
        date = Pair(0, 0)
        distance = Pair(0, 100)
        brakeCount = Pair(0, 100)
        avgSpeed = Pair(0, 100)
        vehicles?.forEach { it.selected = false }
    }

    /** Return true if at least one property's value is set */
    fun isSet(): Boolean {
        return date.isDateSet() || distance.isSet() || brakeCount.isSet()
                || avgSpeed.isSet() || vehicles?.find { it.selected } != null
    }

    /**
     * Override default equals implementation by equating two
     * models on property basis.
     * @author VE00YM023
     */
    override fun equals(other: Any?): Boolean {
        if(other as? TripHistoryFilterModel == null) return false
        (other as TripHistoryFilterModel)
        val vehiclesEqual = (this.vehicles?.find {
            it.selected != other.vehicles?.find { other -> other.regNo == it.regNo }?.selected
        }) == null

        return vehiclesEqual && this.date == other.date &&
                this.distance == other.distance &&
                this.brakeCount == other.brakeCount &&
                this.avgSpeed == other.avgSpeed
    }
}

data class Pair<T, U>(var from: T, var to: U)
fun Pair<Long, Long>.isDateSet() = from != 0L || to != 0L
fun Pair<Int, Int>.isSet() =  from != 0 || to != 100