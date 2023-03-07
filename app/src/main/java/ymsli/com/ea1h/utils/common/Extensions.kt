/*
 **
 ** Project Name : EA1H
 ** @company YMSLI
 ** @author  (VE00YM023)
 ** @date   3/2/20 9:47 AM
 ** Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 **
 ** Description
 ** -----------------------------------------------------------------------------------
 ** This file contains all the extension functions and properties for different types.
 *
 *  Currently this file serves as a container for all extension functions and properties.
 *  may be in the future we can separate these extensions into different files based on the
 *  type. EX. (StringExtensions, DateExtensions etc)
 ** -----------------------------------------------------------------------------------
 **
 ** Revision History
 ** -----------------------------------------------------------------------------------
 ** Modified By          Modified On         Description
 ** -----------------------------------------------------------------------------------
 */

package ymsli.com.ea1h.utils.common

/** Regex and pattern fields for email validations */
private const val VALID_EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"
private val VALID_EMAIL_ADDRESS_REGEX = Regex(VALID_EMAIL_PATTERN, RegexOption.IGNORE_CASE)

/** Regex and pattern fields for name validations */
private const val VALID_NAME_PATTERN = "^([a-zA-Z])+(\\s([a-zA-Z])+)?$"
private val VALID_NAME_REGEX = Regex(VALID_NAME_PATTERN)
private const val MINIMUM_PASSWORD_LENGTH = 8

private const val VALID_CHASSIS_PATTERN = "^[a-zA-Z0-9]+\$"
private val VALID_CHASSIS_REGEX = Regex(VALID_CHASSIS_PATTERN)

private const val VALID_INPUT_OTP_DIGIT_PATTERN = "[0-9]"
private val VALID_INPUT_OTP_DIGIT_REGEX = Regex(VALID_INPUT_OTP_DIGIT_PATTERN)

private const val VALID_INPUT_OTP_PATTERN = "^[0-9]{4}$"
private val VALID_INPUT_OTP_REGEX = Regex(VALID_INPUT_OTP_PATTERN)

private const val VALID_PHONE_PATTERN = "[0-9]{10}"
private val VALID_PHONE_REGEX = Regex(VALID_PHONE_PATTERN)
/**
 * Checks if a given string represents a valid name.
 * we define valid name to be in form (someone something).
 * here second part is optional.
 *
 * @author VE00YM023
 */
fun String.isValidName() = matches(VALID_NAME_REGEX)

/**
 * Checks if given string has any leading space characters.
 * @author VE00YM023
 */
fun String.hasLeadingSpace() = isNotEmpty() && Character.isSpaceChar(this[0])

/**
 * Checks if given string has any trailing space characters.
 * @author VE00YM023
 */
fun String.hasTrailingSpace() = isNotEmpty() && Character.isSpaceChar(this[this.length-1])

/**
 * Checks if length of given string is less than required for a valid password.
 * @return true if length of this string is less than minimum length for a valid password
 * @author VE00YM023
 */
fun String.hasInvalidPasswordLength() = length < MINIMUM_PASSWORD_LENGTH

/**
 * Checks if given string represents a valid email.
 * @return true if this string is a valid email address
 * @author VE00YM023
 */
fun String.isValidEmail() = matches(VALID_EMAIL_ADDRESS_REGEX)

fun String.isValidPhone() = matches(VALID_PHONE_REGEX)

fun String.isValidChassisNumber() = matches(VALID_CHASSIS_REGEX)

fun String.isValidOTPDigit() = matches(VALID_INPUT_OTP_DIGIT_REGEX)

fun String.isValidOTP() = matches(VALID_INPUT_OTP_REGEX)

