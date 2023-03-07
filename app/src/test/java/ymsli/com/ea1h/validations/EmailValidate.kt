/*
 *
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date   30/1/20 11:59 AM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * EmailValidate : This class contains all the unit test cases for email
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.validations

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import ymsli.com.ea1h.R
import ymsli.com.ea1h.model.LoginRequestModel
import ymsli.com.ea1h.utils.common.*

class EmailValidate {

    @Test
    fun nullEmailCheck() {
        performTest(Email.NULL, Resource.error(R.string.empty_field))
    }

    @Test
    fun emptyEmailCheck() {
        performTest(Email.EMPTY, Resource.error(R.string.empty_field))
    }

    @Test
    fun invalidEmailCheck() {
        performTest(Email.INVALID, Resource.error(R.string.invalid_email))
    }

    @Test
    fun validEmailCheck() {
        performTest(Email.VALID, Resource.success())
    }

    /**
     * This function executes a specific test by validating the given values
     * and then comparing the result with the expected result.
     *
     * @param email value of email field
     * @param resource resource field in expected result
     * @author VE00YM023
     */
    private fun performTest(email: Email, resource: Resource<Int>){
        val requestModel = getRequestModelForTest(email)
        val result = Validator.validateEmail(email.value)
        val expectedResult = getExpectedResult(resource)
        MatcherAssert.assertThat(result, Matchers.contains(expectedResult))
    }

    /**
     * Returns a LoginRequestModel populated with all the required properties
     * for a specific email validation test.
     * @param email value of email field for a specific test case, must be an enum value
     * @author VE00YM023
     */
    private fun getRequestModelForTest(email: Email): LoginRequestModel {
        return LoginRequestModel(
            email.value, null, RequestType.USER_LOGIN.code,
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
        Validation(Validation.Field.EMAIL, resource)

    /** Enum class for different values of email field for all the different test cases */
    private enum class Email(var value: String?) {
        NULL(null), EMPTY(""),
        VALID("someone@gmail.com"),
        INVALID("someone@")
    }
}