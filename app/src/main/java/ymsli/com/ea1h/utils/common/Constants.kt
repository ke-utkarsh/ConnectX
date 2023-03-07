package ymsli.com.ea1h.utils.common

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM129)
 * @date    18/07/2020 11:45 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * Constants : This stores constant values for single storage and code reusability
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

object Constants {

    const val WEB_URL = "https://www.yamaha-motor-india.com/"
    const val UNREGISTERED_USER_URL = "https://y-id.yamaha-motor.co.jp/index.html"
    const val NOTIFICATION_DATA_KEY = "requestData"
    const val FCM_TOPIC_ANDROID = "ANDROID"
    const val JSON_KEY_TRIP_ID = "tripId"
    const val DONE = "Done"
    const val NULL = "null"
    const val DIRECTORY = "EA1H_DATA"
    const val APP_SETTINGS_ERROR = "Cannot open app settings"
    const val ACTION_OK = "OK"
    const val ACTION_CANCEL = "CANCEL"

    //realtime shared prefs listener keys
    const val SHARED_PREF_REALTIME = "ymsli.com.ea1h.hellosharedprefs"
    const val BRAKE_APPLIED = "BRAKE_APPLIED"
    const val BATTERY_VALUE = "BATTERY_VALUE"
    const val CONNECTED_MFECU = "CONNECTED"
    const val ELOCK_SET = "ELOCK_SET"
    const val ELOCK_GET = "ELOCK_GET"

    //file uploading constants
    const val REQUEST_MEDIA_TYPE_VALUE = "application/octet-stream"

    const val SOMETHING_WRONG = "Something went wrong!!"
    const val ERROR_TRY_AGAIN = "Something went wrong, Please try again."

    const val JSON_KEY_BIKE_OBJECT = "bikes"
    const val JSON_KEY_USER_OBJECT = "user"
    const val JSON_KEY_TRIP_OBJECT = "trips"
    const val JSON_KEY_TRIP_DETAILS = "tripCrdnts"
    const val JSON_KEY_APP_STATUS = "appStatus"
    const val JSON_KEY_API_NAME = "apiName"
    const val AUTH_TOKEN_NOT_AVAILABLE_ERROR = "Auth Token not available"
    const val NA = "NA"
    const val INTENT_KEY_CHASSIS_NO = "CHASSIS_NUMBER_TAG"
    const val INTENT_KEY_QR_CODE = "QR_CODE_TAG"
    const val INTENT_KEY_REQUEST_TYPE = "REQUEST_TYPE"
    const val INTENT_KEY_PHONE_NO = "PHONE_NUMBER_TAG"
    const val MINUS_ONE = -1
    const val DUMMY_AUTH = "abcd"
    const val ANDROID = "ANDROID"
    const val MISC_KEY_HOME_HEADER_IMAGE_URL = "HOME_HEAD_CONTENT"
    const val MISC_KEY_SCRN_SHOT_ENB = "SCRN_SHOT_ENB"
    const val MISC_KEY_OTP_INTERVAL = "OTP_INTERVAL"
    const val MISC_KEY_BLE_LOG_LIMIT = "BLE_LOG_LIMIT"
    const val MISC_KEY_LOG_TBL_MX_SIZE = "LOG_TBL_MX_SIZE"
    const val API_MESSAGE_OTP_INVALID = "No Data Dound"
    const val LOGIN_URL = "login"
    const val RE_LOGIN_FAILURE = "Swagger API Clint: Re Login failed"
    const val HEADER_KEY_AUTH = "Authorization"
    const val UTF8 = "UTF8"
    const val AES = "AES"
    const val DEFAULT_BLE_LOG_LIMIT = 50
    const val DEFAULT_LOG_TBL_MX_SIZE = 500

    const val CONTENT_PRIVACY_POLICY = "Privacy Policy\n" +
            "1.\tYamaha Motor Corporation built the EA1H connectivity app as a Free app. This SERVICE is provided by Yamaha Motors Solutions Pvt. Ltd. and is intended for use as is. This page is used to inform application visitors regarding our policies with the collection, use, and disclosure of Personal Information if anyone decided to use our Service. \n" +
            "2.\tIf you choose to use our Service, then you agree to the collection and use of information in relation to this policy. The Personal Information that we collect is used for providing and improving the Service. We will not use or share your information with anyone except as described in this Privacy Policy. \n" +
            "3.\tThe terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which is accessible at Yamaha connectivity unless otherwise defined in this Privacy Policy.\n" +
            "4.\tWe may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes. \n" +
            "Information Collection and Use\n" +
            "For a better experience, while using our Service, we may require you to provide us with certain personally identifiable information, including but not limited to Location, Bluetooth, Device ID, Social media platform preferences. The information that we request is will be retained by us and used as described in this privacy policy. \n" +
            "The Information We Collect\n" +
            "\n" +
            "Information You Provide To Us. There are portions of Yamaha Motors where We may need to collect personal information from You for a specific purpose. For e.g. You can register and login, such as: name, address, e-mail address, telephone number, your bikes chassis number.\n" +
            "\n" +
            "Security\n" +
            "We value your trust in providing us your Personal Information, thus we are striving to use commercially acceptable means of protecting it. But remember that no method of transmission over the internet, or method of electronic storage is 100% secure and reliable, and we cannot guarantee its absolute security.\n" +
            "Children Privacy\n" +
            "These Services do not address anyone under the age of 13. We do not knowingly collect personally identifiable information from children under 13. In the case we discover that a child under 13 has provided us with personal information, we immediately delete this from our servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact us so that we will be able to do necessary actions.\n" +
            "Contact Us\n" +
            "If you have any questions or suggestions about our Privacy Policy, do not hesitate to contact us.\n"
    const val COMMA = ","
    const val NULL_STRING = "null"
    const val API_FAILED_MESSAGE = "API Call failed"
    const val ELOCK_PATTERN_READ_COMMAND = "68, 71, 0, 0, 0, 0, 0, 0"
    const val ERROR_UNSUPPORTED_OPERATION = "No application found to perform this action"
    const val SUCCESS = "SUCCESS"

    //restrict user parameters of API
    const val LOGOUT_RESTRICT = "logout"
    const val NETWORK_RESTRICT = "restNet"
    const val CHECK_VERSION = "checkVer"
    const val MIN_STABLE_VERSION = "minStabVer"
    const val TARGET_VERSION = "target"
    const val ALERT_TYPE = "type"
    const val ALERT_KEY = "ALERT"
    const val SILENT_KEY = "SILENT"
    const val TITLE_KEY = "title"
    const val MESSAGE_KEY = "message"
    const val SCOOTER_BG_KEY = "HOME_BG_SCOOTER"
    const val BIKE_DP_KEY = "BIKE_DP"
    const val BIKE_DP_ANDROID_KEY = "BDP_ANDROID"
    const val BIKE_STRING = "BIKE"
    const val SCOOTER_STRING = "SCOOTER"
}
