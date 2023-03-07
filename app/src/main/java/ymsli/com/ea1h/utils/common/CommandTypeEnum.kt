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
 * CommandTypeEnum : This enum stores enum of command type
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

enum class CommandTypeEnum(val cmdCode: Int) {
    ANSWER_BACK(1),
    LOCATE_MY_BIKE(2),
    HAZARD_ACTIVATION(3),
    HAZARD_DEACTIVATION(4)
}