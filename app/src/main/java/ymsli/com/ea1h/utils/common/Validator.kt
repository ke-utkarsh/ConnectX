/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   30/1/20 10:02 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * Validator : This class is responsible for UI validation of user inputs
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  * (VE00YM023)   31/1/2020           Added validator methods for name, email
 *                                             and password fields.
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.utils.common

import ymsli.com.ea1h.R

object Validator {

    private const val MIN_PASSWORD_LENGTH = 8
    private const val INDIAN_PHONE_NUMBER_REGEX = "^[6-9]\\d{9}\$"

    /**
     * This function validates all the fields in sign up form,
     * it returns an error ValidationResponse as soon as a field validation fails.
     * if all the fields pass the validations, then it returns the Success ValidationResponse.
     * consumers of this function must use the value of Status field to decide if validations
     * succeeded or not.
     *
     * @param validationData contains input values for all the fields in sign up form
     * @author VE00YM023
     *
     */
    fun validateSignUpForm(validationData: SignUpValidationModel): ValidationResponse{
        var validationResponse = ValidationResponse(Status.SUCCESS, null)
        var fieldValidation = validateName(validationData.name)[0]
        if(fieldValidation.resource.status != Status.SUCCESS){
            return ValidationResponse(Status.ERROR, fieldValidation)
        }

        fieldValidation = validateEmail(validationData.email)[0]
        if(fieldValidation.resource.status != Status.SUCCESS){
            return ValidationResponse(Status.ERROR, fieldValidation)
        }

        fieldValidation = validatePassword(validationData.password)[0]
        if(fieldValidation.resource.status != Status.SUCCESS){
            return ValidationResponse(Status.ERROR, fieldValidation)
        }

        fieldValidation = validatePhoneNumber(validationData.phone)[0]
        if(fieldValidation.resource.status != Status.SUCCESS){
            return ValidationResponse(Status.ERROR, fieldValidation)
        }

        return validationResponse
    }


    /**
     * This function validates all the fields in change password form,
     * it returns an error ValidationResponse as soon as a field validation fails.
     * if all the fields pass the validations, then it returns the Success ValidationResponse.
     * consumers of this function must use the value of Status field to decide if validations
     * succeeded or not.
     *
     * @param validationData contains input values for all the fields in change password form
     * @author VE00YM023
     *
     */
    fun validateChangePasswordForm(validationData: ChangePasswordValidationModel): ValidationResponse{

        /** Validate new password field value */
        var fieldValidation = validatePassword(validationData.newPassword)[0]
        if(fieldValidation.resource.status != Status.SUCCESS){
            fieldValidation = Validation(Validation.Field.NEW_PASSWORD, fieldValidation.resource)
            return ValidationResponse(Status.ERROR, fieldValidation)
        }

        /** Validate confirm new password field value */
        fieldValidation = validatePassword(validationData.newPasswordConfirmed)[0]
        if(fieldValidation.resource.status != Status.SUCCESS){
            fieldValidation = Validation(Validation.Field.NEW_PASSWORD_CONFIRMED, fieldValidation.resource)
            return ValidationResponse(Status.ERROR, fieldValidation)
        }

        /** Validated that new password and confirm new password have same value */
        if(validationData.newPassword != validationData.newPasswordConfirmed){
            fieldValidation = Validation(Validation.Field.NEW_PASSWORD_CONFIRMED,
                Resource.error(R.string.mismatch_new_password))
            return ValidationResponse(Status.ERROR, fieldValidation)
        }

        return ValidationResponse(Status.SUCCESS, null)
    }

    /**
     * This function validates the email address.
     * it performs following validations on the email.
     *      1. null or empty check
     *      2. email is in valid format
     *
     * @author VE00YM023
     * @return list of failed validations
     */
    fun validateEmail(email: String?): List<Validation> =
        ArrayList<Validation>().apply {
        when{
            email.isNullOrBlank() -> {
                add(Validation(Validation.Field.EMAIL, Resource.error(R.string.empty_field)))
            }
            !email.isValidEmail() ->{
                add(Validation(Validation.Field.EMAIL, Resource.error(R.string.invalid_email)))
            }
            else ->{
                add(Validation(Validation.Field.EMAIL, Resource.success()))
            }
        }
    }

    /**
     * This function validates the phone number.
     * it performs following validations on the phone.
     *      1. null or empty check
     *      2. phone number is in valid format
     *
     */
    fun validatePhoneNumber(phone: String?): List<Validation> =
        ArrayList<Validation>().apply {
            when{
                phone.isNullOrBlank() -> {
                    add(Validation(Validation.Field.PHONE_NUMBER, Resource.error(R.string.empty_field)))
                }
                !phone.isValidPhone() ->{
                    add(Validation(Validation.Field.PHONE_NUMBER, Resource.error(R.string.invalid_phone)))
                }
                else ->{
                    add(Validation(Validation.Field.PHONE_NUMBER, Resource.success()))
                }
            }
        }

    /**
     * This function validates the name of the user.
     * it performs following validations on the name.
     *      1. null or empty check
     *      2. leading space check
     *      3. trailing space check
     *      4. alphabetic check
     *
     * @author VE00YM023
     * @return list of failed validations
     */
    fun validateName(fullName: String?): List<Validation> =
        ArrayList<Validation>().apply {
         when{
             fullName.isNullOrBlank() -> {
                 add(Validation(Validation.Field.NAME, Resource.error(R.string.empty_field)))
             }
             fullName.hasLeadingSpace() ->{
                 add(Validation(Validation.Field.NAME, Resource.error(R.string.leading_space)))
             }
             fullName.hasTrailingSpace() ->{
                 add(Validation(Validation.Field.NAME, Resource.error(R.string.trailing_space)))
             }
             else ->{
                 add(Validation(Validation.Field.NAME, Resource.success()))
             }
         }
    }

    /**
     * This function validates the password of the user.
     * it performs following validations on the password.
     *      1. null or empty check
     *      2. password has minimum length if new user registration is being done
     *
     * @author VE00YM023
     * @return list of failed validations
     */
    fun validatePassword(password: String?): List<Validation> =
        ArrayList<Validation>().apply {
        when{
            password.isNullOrBlank() -> {
                add(Validation(Validation.Field.PASSWORD, Resource.error(R.string.empty_password)))
            }
            /* Check password for minimum length, only if user registration is being done. */
             password.hasInvalidPasswordLength() ->{
                add(Validation(Validation.Field.PASSWORD, Resource.error(R.string.invalid_password_length)))
            }
            else ->{
                add(Validation(Validation.Field.PASSWORD, Resource.success()))
            }
        }
    }

    /**
     * This function validates the chassis number entered by the user.
     * The chassis number should be alpha numeric.
     */
    fun validateChassisNumber(chassisNumber: String?): List<Validation> =
        ArrayList<Validation>().apply {
            when {
                chassisNumber.isNullOrBlank() -> {
                    add(Validation(Validation.Field.CHASSIS_NUMBER, Resource.error(R.string.empty_chassis)))
                }
                /* Check password for minimum length, only if user registration is being done. */
                !chassisNumber.isValidChassisNumber() -> {
                    add(Validation(Validation.Field.CHASSIS_NUMBER, Resource.error(R.string.invalid_chassis_number)))
                }
                else -> {
                    add(Validation(Validation.Field.CHASSIS_NUMBER, Resource.success()))
                }
            }
        }

    /**
     * Validates the complete OTP Input, unlike the function below which validates
     * each input digit individually.
     * @author VE00YM023
     */
    fun validateOTPInput(otp: String?): List<Validation> = ArrayList<Validation>().apply {
        when {
            otp.isNullOrBlank() -> {
                add(Validation(Validation.Field.OTP, Resource.error(R.string.empty_otp)))
            }
            !otp.isValidOTP() -> {
                add(Validation(Validation.Field.OTP, Resource.error(R.string.otp_digit_invalid)))
            }
            else -> {
                add(Validation(Validation.Field.OTP, Resource.success()))
            }
        }
    }

    /**
     * responsible for validating OTP entered in the edit text
     * this is only client side validation of user input
     */
    fun validateEnteredOTP(digit1: String?,digit2: String?,digit3: String?,digit4: String?): List<Validation> =
        ArrayList<Validation>().apply {
            when{
                digit1.isNullOrBlank() -> {
                    add(
                        Validation(
                            Validation.Field.OTP_DIGIT1,
                            Resource.error(R.string.otp_digit_empty)
                        )
                    )
                }

                digit2.isNullOrBlank() -> {
                    add(
                        Validation(
                            Validation.Field.OTP_DIGIT2,
                            Resource.error(R.string.otp_digit_empty)
                        )
                    )
                }

                digit3.isNullOrBlank() -> {
                    add(
                        Validation(
                            Validation.Field.OTP_DIGIT3,
                            Resource.error(R.string.otp_digit_empty)
                        )
                    )
                }

                digit4.isNullOrBlank() -> {
                    add(
                        Validation(
                            Validation.Field.OTP_DIGIT4,
                            Resource.error(R.string.otp_digit_empty)
                        )
                    )
                }

                !digit1.isValidOTPDigit() -> {
                    add(
                        Validation(
                            Validation.Field.OTP_DIGIT1,
                            Resource.error(R.string.otp_digit_invalid)
                        )
                    )
                }

                !digit2.isValidOTPDigit() -> {
                    add(
                        Validation(
                            Validation.Field.OTP_DIGIT2,
                            Resource.error(R.string.otp_digit_invalid)
                        )
                    )
                }

                !digit3.isValidOTPDigit() -> {
                    add(
                        Validation(
                            Validation.Field.OTP_DIGIT3,
                            Resource.error(R.string.otp_digit_invalid)
                        )
                    )
                }

                !digit4.isValidOTPDigit() -> {
                    add(
                        Validation(
                            Validation.Field.OTP_DIGIT4,
                            Resource.error(R.string.otp_digit_invalid)
                        )
                    )
                }
            }
        }
}

data class Validation(val field: Field, val resource: Resource<Int>) {
    enum class Field {
        EMAIL, NAME, PASSWORD, CHASSIS_NUMBER, NEW_PASSWORD, NEW_PASSWORD_CONFIRMED,
        OTP_DIGIT1,OTP_DIGIT2,OTP_DIGIT3,OTP_DIGIT4, OTP,PHONE_NUMBER
    }
}

/**
 * This class is used to pass the sign up validation data from view model to the validator.
 * @author VE00YM023
 */
class SignUpValidationModel(val name: String?, val email: String?,
                             val password: String?,val phone: String?)

class ChangePasswordValidationModel(val newPassword: String?,
                                    val newPasswordConfirmed: String?)

class ValidationResponse(val responseCode: Status, val validation: Validation?)

