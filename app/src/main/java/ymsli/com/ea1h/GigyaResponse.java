package ymsli.com.ea1h;

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    13/02/2020 2:00 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * GigyaResponse : This is the Gigya SDK response and POJO is created here
 * as directed in the SDK documentation of Gigya.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import com.gigya.android.sdk.account.models.GigyaAccount;

public class GigyaResponse extends GigyaAccount {
    private MyData data;

    public MyData getData() {
        return data;
    }

    private static class MyData {

        private String comment;
        private Boolean subscribe;
        private Boolean terms;

        public String getComment() {
            return comment;
        }

        public Boolean getSubscribe() {
            return subscribe;
        }

        public Boolean getTerms() {
            return terms;
        }
    }
}
