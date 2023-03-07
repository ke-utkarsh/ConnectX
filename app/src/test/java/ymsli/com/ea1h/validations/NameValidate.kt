/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author  (VE00YM023)
 *  * @date   30/1/20 5:17 PM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * NameValidate : This class contains all the unit test cases for user name field
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  * -----------------------------------------------------------------------------------
 *
 */
package ymsli.com.ea1h.validations


import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import ymsli.com.ea1h.R
import ymsli.com.ea1h.model.User
import ymsli.com.ea1h.utils.common.*

class NameValidate {

    @Test
    fun nullNameCheck() {
        performTest(Name.NULL, Resource.error(R.string.empty_field))
    }

    @Test
    fun emptyNameCheck() {
        performTest(Name.EMPTY, Resource.error(R.string.empty_field))
    }

    @Test
    fun leadingSpaceNameCheck() {
        performTest(Name.LEADING_SPACE, Resource.error(R.string.leading_space))
    }

    @Test
    fun trailingSpaceNameCheck() {
        performTest(Name.TRAILING_SPACE, Resource.error(R.string.trailing_space))
    }

    @Test
    fun invalidNameCheck() {
        performTest(Name.INVALID, Resource.error(R.string.invalid_name))
    }

    @Test
    fun validNameCheck() {
        performTest(Name.VALID, Resource.success())
    }

    /**
     * This function executes a specific test by validating the given values
     * and then comparing the result with the expected result.
     *
     * @param name value of name field
     * @param resource resource field in expected result
     * @author VE00YM023
     */
    private fun performTest(name: Name, resource: Resource<Int>){
        val requestModel = getRequestModelForTest(name)
        val result = Validator.validateName(name.value)
        val expectedResult = getExpectedResult(resource)
        MatcherAssert.assertThat(result, Matchers.contains(expectedResult))
    }

    /**
     * Returns a User model populated with all the required properties
     * for a specific name validation test.
     * @param name value of name field for a specific test case, must be an enum value
     * @author VE00YM023
     */
    private fun getRequestModelForTest(name: Name) = User(name.value, null)

    /**
     * Returns the expected result for a specific test case.
     * @param resource resource value for a specific test case's expected validation
     * @author VE00YM023
     */
    private fun getExpectedResult(resource: Resource<Int>) =
        Validation(Validation.Field.NAME, resource)

    /** Enum class for different values of name field for all the different test cases */
    private enum class Name(var value: String?) {
        NULL(null), EMPTY(""), LEADING_SPACE("  Rajkumar Rao"),
        TRAILING_SPACE("Rajkumar Rao  "), VALID("Rajkumar Rao"),
        INVALID("someone@")
    }
}
