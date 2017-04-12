package com.qualcomm.qti.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ZeroBalanceHelper;
import android.os.AsyncResult;
import android.os.Bundle;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.ConfigResourceUtil;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.ServiceStateTracker;
import com.qti.internal.telephony.QtiPlmnOverride;
import com.qualcomm.qcrilhook.QmiOemHookConstants;
import com.qualcomm.qcrilhook.QmiPrimitiveTypes;

public class QtiServiceStateTracker extends ServiceStateTracker {
    private static final String ACTION_MANAGED_ROAMING_IND =
                                 "codeaurora.intent.action.ACTION_MANAGED_ROAMING_IND";
    private static final boolean DBG = true;
    private static final String LOG_TAG = "QtiServiceStateTracker";
    private static final boolean VDBG = false;
    private final String ACTION_RAC_CHANGED = "qualcomm.intent.action.ACTION_RAC_CHANGED";
    private ConfigResourceUtil mConfigResUtil = new ConfigResourceUtil();
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("qualcomm.intent.action.ACTION_RAC_CHANGED")) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    QtiServiceStateTracker.this.mRac = extras.getInt("rac");
                    QtiServiceStateTracker.this.mRat = extras.getInt("rat");
                    QtiServiceStateTracker.this.enableBackgroundData();
                }
            }
        }
    };
    private QtiPlmnOverride mQtiPlmnOverride = new QtiPlmnOverride();
    private int mRac;
    private final String mRacChange = "rac";
    private int mRat;
    private final String mRatInfo = "rat";
    private int mTac = -1;

    public QtiServiceStateTracker(GsmCdmaPhone gsmCdmaPhone, CommandsInterface commandsInterface) {
        super(gsmCdmaPhone, commandsInterface);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("qualcomm.intent.action.ACTION_RAC_CHANGED");
        gsmCdmaPhone.getContext().registerReceiver(this.mIntentReceiver, intentFilter);
    }

    private void enableBackgroundData() {
        ZeroBalanceHelper zeroBalanceHelper = new ZeroBalanceHelper();
        if (zeroBalanceHelper.getFeatureConfigValue() && zeroBalanceHelper.getBgDataProperty().equals("true")) {
            Log.i("zerobalance", "Enabling the background data on LAU/RAU");
            zeroBalanceHelper.setBgDataProperty("false");
        }
    }

    private void setOperatorConsideredDomesticRoaming(ServiceState serviceState) {
        int i = 0;
        int subId = this.mPhone.getSubId();
        String operatorNumeric = serviceState.getOperatorNumeric();
        String[] stringArray = SubscriptionManager.getResourcesForSubId(this.mPhone.getContext(), subId).
                       getStringArray(com.android.internal.R.array.config_operatorConsideredDomesticRoaming);
        String[] stringArray2 = SubscriptionManager.getResourcesForSubId(this.mPhone.getContext(), subId).
                       getStringArray(com.android.internal.R.array.config_operatorConsideredDomesticRoamingExceptions);
        if (stringArray != null && stringArray.length != 0 && !TextUtils.isEmpty(operatorNumeric)) {
            for (String startsWith : stringArray) {
                if (operatorNumeric.startsWith(startsWith)) {
                    serviceState.setVoiceRoamingType(2);
                    subId = 1;
                    break;
                }
            }
            subId = 0;
            if (stringArray2.length != 0 && subId != 0) {
                int length = stringArray2.length;
                while (i < length) {
                    if (operatorNumeric.startsWith(stringArray2[i])) {
                        serviceState.setVoiceRoamingType(3);
                        break;
                    }
                    i++;
                }
            }
            if (subId == 0) {
                serviceState.setVoiceRoamingType(3);
            }
        }
    }

    protected void handlePollStateResultMessage(int i, AsyncResult asyncResult) {
        String[] strArr;
        switch (i) {
            case QmiPrimitiveTypes.SIZE_OF_INT /*4*/:
                super.handlePollStateResultMessage(i, asyncResult);
                if (this.mPhone.isPhoneTypeGsm()) {
                    int parseInt;
                    strArr = (String[]) asyncResult.result;
                    if (strArr.length > 0) {
                        try {
                            parseInt = Integer.parseInt(strArr[0]);
                        } catch (NumberFormatException e) {
                            loge("error parsing RegistrationState: " + e);
                            parseInt = 4;
                        }
                    } else {
                        parseInt = 4;
                    }
                    if ((parseInt == 3 || parseInt == 13) && strArr.length >= 14) {
                        try {
                            if (Integer.parseInt(strArr[13]) == 10) {
                                log(" Posting Managed roaming intent sub = " + this.mPhone.getSubId());
                                Intent intent = new Intent(ACTION_MANAGED_ROAMING_IND);
                                intent.putExtra("subscription", this.mPhone.getSubId());
                                this.mPhone.getContext().sendBroadcast(intent);
                                return;
                            }
                            return;
                        } catch (NumberFormatException e2) {
                            loge("error parsing regCode: " + e2);
                            return;
                        }
                    }
                    return;
                }
                return;
            case QmiOemHookConstants.RESPONSE_BUFFER /*6*/:
                super.handlePollStateResultMessage(i, asyncResult);
                if (this.mPhone.isPhoneTypeGsm()) {
                    strArr = (String[]) asyncResult.result;
                    if (strArr != null && strArr.length >= 3) {
                        String operatorBrandOverride = this.mUiccController.getUiccCard(getPhoneId()) != null ? this.mUiccController.getUiccCard(getPhoneId()).getOperatorBrandOverride() : null;
                        if (operatorBrandOverride != null) {
                            log("EVENT_POLL_STATE_OPERATOR: use brandOverride=" + operatorBrandOverride);
                            this.mNewSS.setOperatorName(operatorBrandOverride, operatorBrandOverride, strArr[2]);
                            return;
                        }
                        if (this.mQtiPlmnOverride.containsCarrier(strArr[2])) {
                            ConfigResourceUtil configResourceUtil = this.mConfigResUtil;
                            if (ConfigResourceUtil.getBooleanValue(this.mPhone.getContext(), "config_plmn_name_override_enabled")) {
                                log("EVENT_POLL_STATE_OPERATOR: use plmnOverride");
                                this.mNewSS.setOperatorName(this.mQtiPlmnOverride.getPlmn(strArr[2]), strArr[1], strArr[2]);
                                return;
                            }
                        }
                        this.mNewSS.setOperatorName(strArr[0], strArr[1], strArr[2]);
                        return;
                    }
                    return;
                }
                return;
            default:
                super.handlePollStateResultMessage(i, asyncResult);
                return;
        }
    }

    protected void setRoamingType(ServiceState serviceState) {
        Object obj = null;
        super.setRoamingType(serviceState);
        if (serviceState.getVoiceRegState() == 0) {
            obj = 1;
        }
        if (obj != null && serviceState.getVoiceRoaming() && this.mPhone.isPhoneTypeGsm()) {
            setOperatorConsideredDomesticRoaming(serviceState);
        }
    }
}
