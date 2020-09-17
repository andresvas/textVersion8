package com.todo1.plugins.detectid;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import net.easysol.did.DetectID;
//import net.easysol.did.application.DeviceFingerprintValidate;
//import net.easysol.did.application.utils.SharedPreferencesBuilder;
import net.easysol.did.common.account.Account;
//import net.easysol.did.common.account.AccountController;
import net.easysol.did.common.model.contract.InitParams;
import net.easysol.did.common.registration.listener.DeviceRegistrationServerResponseListener;
import net.easysol.did.common.transaction.TransactionInfo;
import net.easysol.did.push_auth.transaction.listener.PushTransactionOpenListener;
import net.easysol.did.push_auth.transaction.listener.PushTransactionReceivedListener;
import net.easysol.did.push_auth.transaction.listener.PushTransactionServerResponseListener;
import net.easysol.did.push_auth.transaction.views.PushTransactionViewProperties;
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

import static com.appsflyer.AppsFlyerLibCore.LOG_TAG;

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
        DetectID.sdk(cordova.getContext()).PUSH_API.enablePushTransactionDefaultDialog (true); //TODO: enable if notification is managed default or custom
        DetectID.sdk(cordova.getActivity()).PUSH_API.enablePushTransactionServerResponseAlerts(true); // TODO: enable or disable if show alerts type TOAST when server response

        callbackContext.success();
    }


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


    // ------------------- Start PUSH ID - autenticacion por mensajeria push ------------------------- //

    /**
     * this method custom the notification
     */
    private void customNotification() {
        PushTransactionViewProperties properties = DetectID.sdk(cordova.getContext()).getPushTransactionViewPropertiesInstance();
        // properties.NOTIFICATION_ICON_RESOURCE = android.R.drawable.stat_sys_warning; properties.CONFIRM = "Confirm";
        // properties.DECLINE = "Decline"; TODO: so is how is defined in document

        properties.setConfirmIconResource(android.R.drawable.stat_sys_warning);
        properties.setConfirm("Aceptar");
        properties.setDecline("Rechazar");
    }

    /**
     * this method contain the listener of the response from server
     */
    private void responseFromServerNotification() {
        DetectID.sdk(cordova.getContext()).PUSH_API.setPushTransactionServerResponseListener(new PushTransactionServerResponseListener() {
            @Override
            public void onPushTransactionServerResponse(String s) {
                String message = "1020: " + "Code response: onPushTransactionServerResponse";
                Toast.makeText(cordova.getContext(), message, Toast.LENGTH_SHORT).show();
                //showAuthenticationMessage(message);
            }
        });
    }

    /**
     * this method is run when the app received the notification
     */
    private void authenticationReceivedProcess() {
        DetectID.sdk(cordova.getContext()).PUSH_API.setPushTransactionReceivedListener(new PushTransactionReceivedListener() {
            @Override
            public void onPushTransactionReceived(TransactionInfo transactionInfo) {
                Log.i(LOG_TAG, "Notification received");
            }
        });
    }

    /**
     * this method is helpful when it want implement a message and actions with information about transaction
     */
    private void authenticationNotificationOpenProcess() {
        DetectID.sdk(cordova.getContext()).PUSH_API.setPushTransactionOpenListener(new PushTransactionOpenListener() {
            @Override
            public void onPushTransactionOpen(TransactionInfo info) {
                final TransactionInfo transaction = info;
                Context context = cordova.getContext();
                final AlertDialog.Builder builder = new AlertDialog.Builder(context); builder.setTitle(buildTitle(transaction));
                builder.setMessage(buildMessage(transaction));
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() { @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DetectID.sdk(context).PUSH_API.confirmPushTransactionAction(transaction); }
                        });
                builder.setNegativeButton("Decline",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DetectID.sdk(context).PUSH_API.declinePushTransactionAction(transaction); }
                        }); builder.create();
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    /**
     * got message from object info
     *
     * @param transactionInfo transactionInfo
     * @return message
     */
    private String buildTitle(TransactionInfo transactionInfo) {
        if (transactionInfo != null) {
            return transactionInfo.account.organizationName + "\n";
        } else {
            return "No title...";
        }
    }

    /**
     * this method of example
     * @param transactionInfo info push
     * @return message
     */
    private String buildMessage(TransactionInfo transactionInfo) {
        if (transactionInfo != null) {
            StringBuilder message = new StringBuilder();
            message.append("message :");
            message.append(transactionInfo.message);
            return message.toString();
        } else {
            return "not included message";
        }
    }


    /**
     * this method is for add information extra of the notification
     */
    public void addInformation() { // TODO: pending review
        // DetectID.sdk(cordova.getActivity()).PUSH_API.setPushAuthenticationResponseAdditionalInfo(Map<String , String> additionalInfo);
    }




    // ------------------- End PUSH ID - autenticacion por mensajeria push ------------------------- //



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
