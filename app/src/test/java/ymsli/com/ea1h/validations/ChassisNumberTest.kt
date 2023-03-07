package ymsli.com.ea1h.validations

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import ymsli.com.ea1h.R
import ymsli.com.ea1h.utils.common.Resource
import ymsli.com.ea1h.utils.common.Validation
import ymsli.com.ea1h.utils.common.Validator

class ChassisNumberTest {

    @Test
    fun validChassisNumberCheck1(){
        val validations = Validator.validateChassisNumber("CA34SV2323K")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.CHASSIS_NUMBER,
                    Resource.success()
                )
            )
        )
    }

    @Test
    fun validChassisNumberCheck2(){
        val validations = Validator.validateChassisNumber("123324546")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.CHASSIS_NUMBER,
                    Resource.success()
                )
            )
        )
    }

    @Test
    fun validChassisNumberCheck3(){
        val validations = Validator.validateChassisNumber("KJHDSFBJHSB")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.CHASSIS_NUMBER,
                    Resource.success()
                )
            )
        )
    }

    @Test
    fun nullChassisNumberCheck(){
        val validations = Validator.validateChassisNumber(null)
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.CHASSIS_NUMBER,
                    Resource.error(R.string.empty_chassis)
                )
            )
        )
    }

    @Test
    fun invalidChassisNumberCheck1(){
        val validations = Validator.validateChassisNumber("KJH-DSF-BJHSB")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.CHASSIS_NUMBER,
                    Resource.error(R.string.invalid_chassis_number)
                )
            )
        )
    }

    @Test
    fun invalidChassisNumberCheck2(){
        val validations = Validator.validateChassisNumber("KJH#DSFBJHSB")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.CHASSIS_NUMBER,
                    Resource.error(R.string.invalid_chassis_number)
                )
            )
        )
    }

    @Test
    fun invalidChassisNumberCheck3(){
        val validations = Validator.validateChassisNumber("KJH@DSFBJHSB")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.CHASSIS_NUMBER,
                    Resource.error(R.string.invalid_chassis_number)
                )
            )
        )
    }

    @Test
    fun invalidChassisNumberCheck4(){
        val validations = Validator.validateChassisNumber("KJH++DSFBJHSB")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.CHASSIS_NUMBER,
                    Resource.error(R.string.invalid_chassis_number)
                )
            )
        )
    }

    @Test
    fun invalidChassisNumberCheck5(){
        val validations = Validator.validateChassisNumber("KJH=DSFBJHSB")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.CHASSIS_NUMBER,
                    Resource.error(R.string.invalid_chassis_number)
                )
            )
        )
    }

    @Test
    fun invalidChassisNumberCheck6(){
        val validations = Validator.validateChassisNumber("KJH**DSFBJHSB")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.CHASSIS_NUMBER,
                    Resource.error(R.string.invalid_chassis_number)
                )
            )
        )
    }

    @Test
    fun invalidChassisNumberCheck7(){
        val validations = Validator.validateChassisNumber("KJH)(DSFBJHSB")
        MatcherAssert.assertThat(
            validations,
            Matchers.contains(
                Validation(
                    Validation.Field.CHASSIS_NUMBER,
                    Resource.error(R.string.invalid_chassis_number)
                )
            )
        )
    }
}