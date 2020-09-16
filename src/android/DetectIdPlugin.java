package com.todo1.plugins.detectid;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import net.easysol.did.DetectID;
//import net.easysol.did.application.DeviceFingerprintValidate;
//import net.easysol.did.application.utils.SharedPreferencesBuilder;
import net.easysol.did.common.account.Account;
//import net.easysol.did.common.account.AccountController;
import net.easysol.did.common.model.contract.InitParams;
import net.easysol.did.common.registration.listener.DeviceRegistrationServerResponseListener;
//import net.easysol.did.otp_auth.TokenController;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;
import java.util.List;

public class DetectIdPlugin extends CordovaPlugin implements DeviceRegistrationServerResponseListener {
    final String NO_INIT_ERROR = "SDK not initialized";
    private final String TAG = "DetectIdPlugin";
    private CallbackContext callbackContext;
    String codeServerUrl = "https://otp.bancolombia.com/detect/public/registration/mobileServices.htm?code=";

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        DetectID detectIDSdk = DetectID.sdk(cordova.getActivity());

        detectIDSdk.INBOX_API.automaticResetBadgeNumber(false);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (this.callbackContext != null && !this.callbackContext.isFinished()) {
            this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Callback canceled"));
        }
        this.callbackContext = callbackContext;
        Action ac = Action.fromString(action);
        try {
            if (isValidArguments(ac, args)) {
                switch (ac) {
                    case INIT:
                        initSdk(callbackContext, args);
                        return true;
                    case REGISTER_DEVICE_BY_CODE:
                        registerDeviceByCode(callbackContext, args.getString(0));
                        return true;
                    case ENABLE_REGISTRATION_ALERT:
                        enableRegistrationAlert(callbackContext, args.getBoolean(0));
                        return true;
                    case GET_ACCOUNTS:
                        getAccounts(callbackContext);
                        return true;
                    case HAS_ACCOUNTS:
                        hasAccounts(callbackContext);
                        return true;
                    case SET_ACCOUNT_USERNAME:
                        setAccountUsername(callbackContext, args.getString(0), args.getString(1));
                        return true;
                    case REMOVE_ACCOUNT:
                        removeAccount(callbackContext, args.getString(0));
                        return true;
                    case GET_DEVICE_ID:
                        getDeviceId(callbackContext);
                        return true;
                    case GET_SHARED_DEVICE_ID:
                        getSharedDeviceId(callbackContext);
                        return true;
                    case OTP_GET_TOKEN_TIMELEFT:
                        getTokenTimeleft(callbackContext, args.getString(0));
                        return true;
                    case OTP_GET_TOKEN_VALUE:
                        getTokenValue(callbackContext, args.getString(0));
                        return true;

                    default:
                        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
                        return false;
                }
            } else {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION, "INVALID ARGUMENTS"));
                return false;
            }
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
            return false;
        }
    }

    private boolean isValidArguments(Action ac, JSONArray args) {
        try {
            switch (ac) {
                case INIT:
                case REGISTER_DEVICE_BY_CODE:
                case ENABLE_REGISTRATION_ALERT:
                    return args.length() == 1 &&
                            args.getString(0).length() > 0;
                case GET_ACCOUNTS:
                case HAS_ACCOUNTS:
                case GET_DEVICE_ID:
                case GET_SHARED_DEVICE_ID:
                    return args.length() == 0;
                case SET_ACCOUNT_USERNAME:
                    return args.length() == 2 && (args.getString(0) != null) && (args.getString(1) != null);
                case REMOVE_ACCOUNT:
                case OTP_GET_TOKEN_TIMELEFT:
                case OTP_GET_TOKEN_VALUE:
                    return args.length() == 1 &&
                            args.getString(0).length() > -1;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


    //ASYNC METHOD
    private void initSdk(final CallbackContext callbackContext, JSONArray args) throws JSONException {
        String serverURL = args.getString(0);
        Log.d(TAG, "Initializing DetectId SDK with serverKey: " + serverURL);

       /* InitParams initParams = new InitParams.InitParamsBuilder() // TODO deprecated
                .addDidUrl(serverURL)
                .build();
        DetectID detectIDSdk = DetectID.sdk(cordova.getActivity());*/

       // detectIDSdk.initDIDServerWithParams(initParams);
        //initServerWithUrl(serverURL);

        DetectID.sdk(cordova.getActivity()).didInit();

        callbackContext.success();
    }

    /**
     * This is a workaround method to the typical initDIDServerWithParams(initParams).
     * This was needed to prevent the call of the Get request that validates if the sdk is supported. I think this url fails to resolve, giving timeout after a really long period.
     *
     * @param serverUrl
     *//**
     * This is a workaround method to the typical initDIDServerWithParams(initParams).
     * This was needed to prevent the call of the Get request that validates if the sdk is supported. I think this url fails to resolve, giving timeout after a really long period.
     *®
     * @param //serverUrl
     */
    /*private void initServerWithUrl(String serverUrl) {
        Context myContext = cordova.getActivity();
        // validationDeviceFingerprint
        if (!SharedPreferencesBuilder.containsPreferenceKey(myContext, "fingerprint_valid")) {
            DeviceFingerprintValidate deviceFingerprintValidate = new DeviceFingerprintValidate(myContext);
            deviceFingerprintValidate.validateAccounts();
        }
        // setSdkVersionTag
        SharedPreferencesBuilder.setPreferenceValue(myContext, "sdk_version", "7.2.0");

        // setLegacyAccounts()
        if (!SharedPreferencesBuilder.getPreferencesValue(myContext, "convert_legacy_accounts", false)) {
            TokenController.getInstance(myContext).protectLegacySeeds();
            AccountController.getInstance(myContext).convertLegacyAccounts();
            SharedPreferencesBuilder.setPreferenceValue(myContext, "convert_legacy_accounts", true);
        }

        // setLegacyAccountsV135()
        if (!SharedPreferencesBuilder.getPreferencesValue(myContext, "convert_legacy_accounts_v135", false)) {
            AccountController.getInstance(myContext).convertLegacyAccountsV135();
            SharedPreferencesBuilder.setPreferenceValue(myContext, "convert_legacy_accounts_v135", true);
        }

        // validateSdkSupportVersion
        SharedPreferencesBuilder.setPreferenceValue(myContext, "IS_SDK_SUPPORTED_VERSION", true);
        SharedPreferencesBuilder.setPreferenceValue(myContext, "server_did", serverUrl);
    }*/

    //ASYNC METHOD
    private void registerDeviceByCode(CallbackContext callbackContext, String code) throws JSONException {
        if (!isSdkInitialized()) {
            callbackContext.error(NO_INIT_ERROR);
            return;
        }
        DetectID detectIDSdk = DetectID.sdk(cordova.getActivity());
        detectIDSdk.setDeviceRegistrationServerResponseListener(this);
        //detectIDSdk.enableRegistrationServerResponseAlerts(false); TODO: deprecated
        //detectIDSdk.deviceRegistrationByCode(code);  TODO: deprecated and replace for new method didRegistration
        detectIDSdk.didRegistration(codeServerUrl + code);
        PluginResult pr = new PluginResult(PluginResult.Status.NO_RESULT);
        pr.setKeepCallback(true);
        callbackContext.sendPluginResult(pr);
    }

    private boolean isSdkInitialized() {
        return true; //!SharedPreferencesBuilder.getPreferencesValue(cordova.getActivity(), "server_did", "").isEmpty(); // TODO question for DetectID
    }

    //SYNC METHOD
    private void enableRegistrationAlert(CallbackContext callbackContext, boolean enable) {
        DetectID detectIDSdk = DetectID.sdk(cordova.getActivity());
       // detectIDSdk.enableRegistrationServerResponseAlerts(enable); // TODO: pending...
        callbackContext.success();
    }

    //SYNC METHOD
    private void getSharedDeviceId(CallbackContext callbackContext) {
        DetectID detectIDSdk = DetectID.sdk(cordova.getActivity());
        callbackContext.success(detectIDSdk.getSharedDeviceID());
    }

    private void getTokenValue(CallbackContext callbackContext, String username) {
        DetectID detectIDSdk = DetectID.sdk(cordova.getActivity());
        Account acc = getAccount(username);
        if (acc != null) {
            callbackContext.success(detectIDSdk.OTP_API.getTokenValue(acc));
        } else {
            callbackContext.error("Account not found");
        }
    }

    private void getTokenTimeleft(CallbackContext callbackContext, String username) {
        DetectID detectIDSdk = DetectID.sdk(cordova.getActivity());
        Account acc = getAccount(username);
        if (acc != null) {
            callbackContext.success(detectIDSdk.OTP_API.getTokenTimeStepValue(acc));
        } else {
            callbackContext.error("Account not found");
        }
    }

    Account getAccount(String username) {
        DetectID detectIDSdk = DetectID.sdk(cordova.getActivity());
        List<Account> accounts = detectIDSdk.getAccounts();
        for (Account acc : accounts) {
            if (username.equals(acc.username) || (acc.username == null && username.isEmpty())) {
                return acc;
            }
        }
        return null;
    }

    //SYNC METHOD
    private void getAccounts(CallbackContext callbackContext) {
        if (!isSdkInitialized()) {
            callbackContext.error(NO_INIT_ERROR);
            return;
        }
        DetectID detectIDSdk = DetectID.sdk(cordova.getActivity());
        JSONArray jsonArray = new JSONArray();
        List<Account> accounts = detectIDSdk.getAccounts();
        for (Account acc : accounts) {
            jsonArray.put(acc.username);
        }
        callbackContext.success(jsonArray);
    }

    //SYNC METHOD
    private void hasAccounts(CallbackContext callbackContext) {
        if (!isSdkInitialized()) {
            callbackContext.error(NO_INIT_ERROR);
            return;
        }
        DetectID detectIDSdk = DetectID.sdk(cordova.getActivity());
        callbackContext.success("" + detectIDSdk.existAccounts());
    }

    //SYNC METHOD
    private void setAccountUsername(CallbackContext callbackContext, String oldName, String newName) {
        if (!isSdkInitialized()) {
            callbackContext.error(NO_INIT_ERROR);
            return;
        }
        Account acc = getAccount(oldName);
        if (acc != null) {
            DetectID detectIDSdk = DetectID.sdk(cordova.getActivity());
            detectIDSdk.setAccountUsername(acc, newName);
            callbackContext.success();
        } else {
            callbackContext.error("Account with '" + oldName + "' username not found");
        }
    }

    //SYNC METHOD
    private void removeAccount(CallbackContext callbackContext, String jsonAccount) {
        if (!isSdkInitialized()) {
            callbackContext.error(NO_INIT_ERROR);
            return;
        }
        Account acc = getAccount(jsonAccount);
        if (acc != null) {
            DetectID detectIDSdk = DetectID.sdk(cordova.getActivity());
            detectIDSdk.removeAccount(acc);
            callbackContext.success();
        } else {
            callbackContext.error("Account with empty username not found");
        }
    }

    //SYNC METHOD
    private void getDeviceId(CallbackContext callbackContext) {
        DetectID detectIDSdk = DetectID.sdk(cordova.getActivity());
        callbackContext.success(detectIDSdk.getDeviceID());
    }

    @Override
    public void onRegistrationServerResponse(String s) {
        if (callbackContext != null && !callbackContext.isFinished()) {
            callbackContext.success(s);
        }
    }

    public enum Action {
        INIT,
        REGISTER_DEVICE_BY_CODE,
        ENABLE_REGISTRATION_ALERT,

        GET_SHARED_DEVICE_ID,
        GET_DEVICE_ID,
        GET_ACCOUNTS,
        SET_ACCOUNT_USERNAME,
        REMOVE_ACCOUNT,
        HAS_ACCOUNTS,

        OTP_GET_TOKEN_VALUE,
        OTP_GET_TOKEN_TIMELEFT,

        INVALID;

        @NonNull
        public static Action fromString(String action) {
            try {
                return Action.valueOf(action);
            } catch (IllegalArgumentException e) {
                return INVALID;
            }
        }
    }
}
