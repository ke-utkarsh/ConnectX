/**
 * EA1H API
 * \"REST API for EA1H Application\"
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package io.swagger.client.models


/**
 *
 * @param bikeIds
 * @param imei
 */
data class RemoveBikeMappingRequest (

    val bikeIds: kotlin.Array<kotlin.Long>? = null,
    val imei: kotlin.String? = null
) {
}