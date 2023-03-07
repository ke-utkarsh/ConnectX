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
package io.swagger.client.apis

import io.swagger.client.models.ModelAndView

import io.swagger.client.infrastructure.*

class BasicErrorControllerApi(basePath: kotlin.String = "http://iymconnected-api-dev.ap-south-1.elasticbeanstalk.com/") : ApiClient(basePath) {

    /**
     * errorHtml
     * 
     * @param authorization  
     * @return ModelAndView
     */
    @Suppress("UNCHECKED_CAST")
    fun errorHtmlUsingDELETE(authorization: kotlin.String): ModelAndView {
        
        val localVariableHeaders: kotlin.collections.Map<kotlin.String, kotlin.String> = mapOf("Authorization" to authorization)
        val localVariableConfig = RequestConfig(
                RequestMethod.DELETE,
                "/error", headers = localVariableHeaders
        )
        val response = request<ModelAndView>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as ModelAndView
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * errorHtml
     * 
     * @param authorization  
     * @return ModelAndView
     */
    @Suppress("UNCHECKED_CAST")
    fun errorHtmlUsingGET(authorization: kotlin.String): ModelAndView {
        
        val localVariableHeaders: kotlin.collections.Map<kotlin.String, kotlin.String> = mapOf("Authorization" to authorization)
        val localVariableConfig = RequestConfig(
                RequestMethod.GET,
                "/error", headers = localVariableHeaders
        )
        val response = request<ModelAndView>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as ModelAndView
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * errorHtml
     * 
     * @param authorization  
     * @return ModelAndView
     */
    @Suppress("UNCHECKED_CAST")
    fun errorHtmlUsingHEAD(authorization: kotlin.String): ModelAndView {
        
        val localVariableHeaders: kotlin.collections.Map<kotlin.String, kotlin.String> = mapOf("Authorization" to authorization)
        val localVariableConfig = RequestConfig(
                RequestMethod.HEAD,
                "/error", headers = localVariableHeaders
        )
        val response = request<ModelAndView>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as ModelAndView
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * errorHtml
     * 
     * @param authorization  
     * @return ModelAndView
     */
    @Suppress("UNCHECKED_CAST")
    fun errorHtmlUsingOPTIONS(authorization: kotlin.String): ModelAndView {
        
        val localVariableHeaders: kotlin.collections.Map<kotlin.String, kotlin.String> = mapOf("Authorization" to authorization)
        val localVariableConfig = RequestConfig(
                RequestMethod.OPTIONS,
                "/error", headers = localVariableHeaders
        )
        val response = request<ModelAndView>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as ModelAndView
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * errorHtml
     * 
     * @param authorization  
     * @return ModelAndView
     */
    @Suppress("UNCHECKED_CAST")
    fun errorHtmlUsingPATCH(authorization: kotlin.String): ModelAndView {
        
        val localVariableHeaders: kotlin.collections.Map<kotlin.String, kotlin.String> = mapOf("Authorization" to authorization)
        val localVariableConfig = RequestConfig(
                RequestMethod.PATCH,
                "/error", headers = localVariableHeaders
        )
        val response = request<ModelAndView>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as ModelAndView
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * errorHtml
     * 
     * @param authorization  
     * @return ModelAndView
     */
    @Suppress("UNCHECKED_CAST")
    fun errorHtmlUsingPOST(authorization: kotlin.String): ModelAndView {
        
        val localVariableHeaders: kotlin.collections.Map<kotlin.String, kotlin.String> = mapOf("Authorization" to authorization)
        val localVariableConfig = RequestConfig(
                RequestMethod.POST,
                "/error", headers = localVariableHeaders
        )
        val response = request<ModelAndView>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as ModelAndView
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * errorHtml
     * 
     * @param authorization  
     * @return ModelAndView
     */
    @Suppress("UNCHECKED_CAST")
    fun errorHtmlUsingPUT(authorization: kotlin.String): ModelAndView {
        
        val localVariableHeaders: kotlin.collections.Map<kotlin.String, kotlin.String> = mapOf("Authorization" to authorization)
        val localVariableConfig = RequestConfig(
                RequestMethod.PUT,
                "/error", headers = localVariableHeaders
        )
        val response = request<ModelAndView>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as ModelAndView
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
}