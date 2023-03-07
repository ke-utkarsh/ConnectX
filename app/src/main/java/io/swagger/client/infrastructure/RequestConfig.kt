package io.swagger.client.infrastructure

/**
 * Defines a config object for a given request.
 * NOTE: This object doesn't include 'body' because it
 *       allows for caching of the constructed object
 *       for many request definitions.
 * NOTE: Headers is a Map<String,String> because rfc2616 defines
 *       multi-valued headers as csv-only.
 */
data class RequestConfig(
        val method: RequestMethod,
        val path: String,
        val headers: Map<String, String> = mapOf(),
        val query: Map<String, List<String>> = mapOf()
)