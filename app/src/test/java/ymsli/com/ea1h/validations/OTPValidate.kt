package ymsli.com.ea1h.validations

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import ymsli.com.ea1h.R
import ymsli.com.ea1h.utils.common.Resource
import ymsli.com.ea1h.utils.common.Validation
import ymsli.com.ea1h.utils.common.Validator

class OTPValidate {

    @Test
    fun nullAllDigitCheck(){
        val validations = Validator.validateEnteredOTP(null,null,null,null)
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.OTP_DIGIT1,
                    Resource.error(R.string.otp_digit_empty)
                )
            )
        )
    }

    @Test
    fun nullDigit1Check(){
        val validations = Validator.validateEnteredOTP(null,"3","5","8")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.OTP_DIGIT1,
                    Resource.error(R.string.otp_digit_empty)
                )
            )
        )
    }

    @Test
    fun nullDigit2Check(){
        val validations = Validator.validateEnteredOTP("4",null,"5","1")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.OTP_DIGIT2,
                    Resource.error(R.string.otp_digit_empty)
                )
            )
        )
    }

    @Test
    fun nullDigit3Check(){
        val validations = Validator.validateEnteredOTP("6","3",null,"0")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.OTP_DIGIT3,
                    Resource.error(R.string.otp_digit_empty)
                )
            )
        )
    }

    @Test
    fun nullDigit4Check(){
        val validations = Validator.validateEnteredOTP("9","1","5",null)
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.OTP_DIGIT4,
                    Resource.error(R.string.otp_digit_empty)
                )
            )
        )
    }

    @Test
    fun digit1AlphaCheck(){
        val validations = Validator.validateEnteredOTP("a","3","5","8")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.OTP_DIGIT1,
                    Resource.error(R.string.otp_digit_invalid)
                )
            )
        )
    }

    @Test
    fun digit3SpecialCharCheck(){
        val validations = Validator.validateEnteredOTP("1","3","#","8")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.OTP_DIGIT3,
                    Resource.error(R.string.otp_digit_invalid)
                )
            )
        )
    }

    @Test
    fun digit2WhiteSpaceCheck(){
        val validations = Validator.validateEnteredOTP("4"," ","5","8")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.OTP_DIGIT2,
                    Resource.error(R.string.otp_digit_empty)
                )
            )
        )
    }
}