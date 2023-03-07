/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author  (VE00YM023)
 *  * @date   30/1/20 6:08 PM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * PasswordValidate : This class contains all the unit test cases for password field
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.validations

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import ymsli.com.ea1h.R
import ymsli.com.ea1h.model.LoginRequestModel
import ymsli.com.ea1h.utils.common.*

class PasswordValidate {

    @Test
    fun nullPasswordCheck() {
        performTest(Password.NULL, Resource.error(R.string.empty_password))
    }

    @Test
    fun emptyPasswordCheck() {
        performTest(Password.EMPTY, Resource.error(R.string.empty_password))
    }

    /* Invalid length check is only performed when user registration is being done */
    @Test
    fun invalidPasswordLengthCheck() {
        performTest(Password.INVALID_LENGTH, Resource.error(R.string.invalid_password_length))
    }

    @Test
    fun validPasswordCheck() {
        performTest(Password.VALID, Resource.success())
    }

    /**
     * This function executes a specific test by validating the given values
     * and then comparing the result with the expected result.
     *
     * @param password value of password field
     * @param resource resource field in expected result
     * @author VE00YM023
     */
    private fun performTest(password: Password, resource: Resource<Int>){
        val requestModel = getRequestModelForTest(password)
        val result = Validator.validatePassword(password.value)
        val expectedResult = getExpectedResult(resource)
        MatcherAssert.assertThat(result, Matchers.contains(expectedResult))
    }

    /**
     * Returns a LoginRequestModel model populated with all the required properties
     * for a specific password validation test.
     * @param password value of password field for a specific test case, must be an enum value
     * @author VE00YM023
     */
    private fun getRequestModelForTest(password: Password): LoginRequestModel {
        return LoginRequestModel(
             null, password.value, password.requestType.code,
            "1234324", Source.EMAIL.sourceCode, null, null,
            null, null
        )
    }

    /**
     * Returns the expected result for a specific test case.
     * @param resource resource value for a specific test case's expected validation
     * @author VE00YM023
     */
    private fun getExpectedResult(resource: Resource<Int>) =
        Validation(Validation.Field.PASSWORD, resource)

    /** Enum class for different values of password field and request type field
     *  for all the different test cases, request type is required to perform the
     *  invalid length check.
     *  Because invalid length check is only performed if request type is USER_REGISTRATION.
     */
    private enum class Password(var value: String?, var requestType: RequestType) {
        NULL(null, RequestType.USER_LOGIN),
        EMPTY("", RequestType.USER_LOGIN),
        INVALID_LENGTH("1234", RequestType.USER_REGISTRATION),
        VALID("1234#1231232abcdef@", RequestType.USER_LOGIN);
    }
}
