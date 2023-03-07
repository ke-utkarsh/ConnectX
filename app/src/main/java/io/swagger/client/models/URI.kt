/**
 * Hela API
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
 * @param absolute 
 * @param authority 
 * @param fragment 
 * @param host 
 * @param opaque 
 * @param path 
 * @param port 
 * @param query 
 * @param rawAuthority 
 * @param rawFragment 
 * @param rawPath 
 * @param rawQuery 
 * @param rawSchemeSpecificPart 
 * @param rawUserInfo 
 * @param scheme 
 * @param schemeSpecificPart 
 * @param userInfo 
 */
data class URI (

    val absolute: kotlin.Boolean? = null,
    val authority: kotlin.String? = null,
    val fragment: kotlin.String? = null,
    val host: kotlin.String? = null,
    val opaque: kotlin.Boolean? = null,
    val path: kotlin.String? = null,
    val port: kotlin.Int? = null,
    val query: kotlin.String? = null,
    val rawAuthority: kotlin.String? = null,
    val rawFragment: kotlin.String? = null,
    val rawPath: kotlin.String? = null,
    val rawQuery: kotlin.String? = null,
    val rawSchemeSpecificPart: kotlin.String? = null,
    val rawUserInfo: kotlin.String? = null,
    val scheme: kotlin.String? = null,
    val schemeSpecificPart: kotlin.String? = null,
    val userInfo: kotlin.String? = null
) {
}