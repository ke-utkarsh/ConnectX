/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   4/2/20 4:25 PM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * Validator : This Activity is initializes the application
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h

import org.junit.runner.RunWith
import org.junit.runners.Suite
import ymsli.com.ea1h.validations.*
import ymsli.com.ea1h.viewmodeltests.SplashViewModelTest

@RunWith(Suite::class)
@Suite.SuiteClasses(EmailValidate::class,NameValidate::class
    ,PasswordValidate::class,ChassisNumberValidate::class,
    OTPValidate::class) //, SplashViewModelTest::class
class TestSuite