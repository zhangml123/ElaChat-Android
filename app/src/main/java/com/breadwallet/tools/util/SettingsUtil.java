package com.breadwallet.tools.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import com.eladapp.elachat.R;

//import com.breadwallet.presenter.activities.EsignActivity;
//import com.breadwallet.presenter.activities.InputPinActivity;
//import com.breadwallet.presenter.activities.ManageWalletsActivity;
//import com.breadwallet.presenter.activities.did.DidAuthListActivity;
//import com.breadwallet.presenter.activities.did.ProfileEditActivity;
/*import com.breadwallet.presenter.activities.intro.WriteDownActivity;
import com.breadwallet.presenter.activities.settings.AboutActivity;
import com.breadwallet.presenter.activities.settings.DisplayCurrencyActivity;
import com.breadwallet.presenter.activities.settings.ElaNodeActivity;
import com.breadwallet.presenter.activities.settings.FingerprintActivity;
import com.breadwallet.presenter.activities.settings.ImportActivity;
import com.breadwallet.presenter.activities.settings.NodesActivity;
import com.breadwallet.presenter.activities.settings.SettingsActivity;
import com.breadwallet.presenter.activities.settings.SpendLimitActivity;
import com.breadwallet.presenter.activities.settings.SyncBlockchainActivity;
import com.breadwallet.presenter.activities.settings.UnlinkActivity;*/
import com.breadwallet.presenter.entities.BRSettingsItem;
//import com.breadwallet.presenter.interfaces.BRAuthCompletion;
//import com.breadwallet.tools.animation.UiUtils;
//import com.breadwallet.tools.security.AuthManager;
//import com.breadwallet.wallet.wallets.bitcoin.WalletBchManager;
//import com.breadwallet.wallet.wallets.bitcoin.WalletBitcoinManager;

import java.util.ArrayList;
import java.util.List;

/**
 * BreadWallet
 * <p/>
 * Created by Mihail Gutan on <mihail@breadwallet.com> 6/27/18.
 * Copyright (c) 2018 breadwallet LLC
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
public final class SettingsUtil {

    private static final String MARKET_URI = "market://details?id=com.breadwallet";
    private static final String GOOGLE_PLAY_URI = "https://play.google.com/store/apps/details?id=com.breadwallet";
    private static final String APP_STORE_PACKAGE = "com.android.vending";

    public static final int NONE = -1;
    public static final int IS_NEW = 0;
    public static final int IS_PENDING = 1;
    public static final int IS_SAVING = 2;
    public static final int IS_COMPLETED = 3;
    public static final String KYC_FROME_KEY = "KYC_FROM";
    public static final int KYC_FROME_NICKNAME = 0;
    public static final int KYC_FROME_EMAIL = 1;
    public static final int KYC_FROME_MOBILE = 2;
    public static final int KYC_FROME_ID = 3;

    private SettingsUtil() {
    }

   /* public static List<BRSettingsItem> getMainSettings(final Activity activity) {
        List<BRSettingsItem> settingsItems = new ArrayList<>();
        final BaseWalletManager walletManager = WalletsMaster.getInstance(activity).getCurrentWallet(activity);

        settingsItems.add(new BRSettingsItem(activity.getString(R.string.MenuButton_authorizations), "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DidAuthListActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, R.drawable.ic_auth));

        settingsItems.add(new BRSettingsItem(activity.getString(R.string.MenuButton_manageWallets), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ManageWalletsActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, R.drawable.ic_wallet));

        settingsItems.add(new BRSettingsItem(activity.getString(R.string.Settings_preferences), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SettingsActivity.class);
                intent.putExtra(SettingsActivity.EXTRA_MODE, SettingsActivity.MODE_PREFERENCES);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, R.drawable.ic_preferences));

        settingsItems.add(new BRSettingsItem(activity.getString(R.string.MenuButton_security), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SettingsActivity.class);
                intent.putExtra(SettingsActivity.EXTRA_MODE, SettingsActivity.MODE_SECURITY);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, R.drawable.ic_security_settings));

        settingsItems.add(new BRSettingsItem(activity.getString(R.string.MenuButton_e_sign), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EsignActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, R.drawable.ic_e_sign));

//        settingsItems.add(new BRSettingsItem(activity.getString(R.string.Did_Create_Ela_Red_Package), "", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "https://redpacket.elastos.org";
//                UiUtils.openUrlByBrowser(activity, url);
//            }
//        }, false, R.drawable.ic_red_package));

        settingsItems.add(new BRSettingsItem(activity.getString(R.string.Upgrade_title), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Beta.checkUpgrade(true, false);
            }
        }, false, R.drawable.ic_upgrade));

        settingsItems.add(new BRSettingsItem(activity.getString(R.string.About_title), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AboutActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, R.drawable.ic_about));
        return settingsItems;
    }
*/
 /*   public static List<BRSettingsItem> getPreferencesSettings(final Activity activity) {
        List<BRSettingsItem> items = new ArrayList<>();

        String currentFiatCode = BRSharedPrefs.getPreferredFiatIso(activity);
        items.add(new BRSettingsItem(activity.getString(R.string.Settings_currency), currentFiatCode, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DisplayCurrencyActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

            }
        }, false, 0));

        final WalletElaManager elaManager = WalletElaManager.getInstance(activity);
        String elaSettingsLabel = String.format("%s %s", elaManager.getName(), activity.getString(R.string.Settings_title));
        items.add(new BRSettingsItem(elaSettingsLabel, currentFiatCode, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BRSharedPrefs.putCurrentWalletIso(activity, elaManager.getIso());
                startCurrencySettings(activity);
            }
        }, false, 0));

        final WalletBitcoinManager walletBitcoinManager = WalletBitcoinManager.getInstance(activity);
        String bitcoinSettingsLabel = String.format("%s %s", walletBitcoinManager.getName(), activity.getString(R.string.Settings_title));
        items.add(new BRSettingsItem(bitcoinSettingsLabel, currentFiatCode, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BRSharedPrefs.putCurrentWalletIso(activity, walletBitcoinManager.getIso());
                startCurrencySettings(activity);
            }
        }, false, 0));
        final WalletBchManager walletBchManager = WalletBchManager.getInstance(activity);
        String bchSettingsLabel = String.format("%s %s", walletBchManager.getName(), activity.getString(R.string.Settings_title));

        items.add(new BRSettingsItem(bchSettingsLabel, currentFiatCode, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BRSharedPrefs.putCurrentWalletIso(activity, walletBchManager.getIso());
                startCurrencySettings(activity);
            }
        }, false, 0));
        return items;
    }
*/
   /* private static void startCurrencySettings(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        intent.putExtra(SettingsActivity.EXTRA_MODE, SettingsActivity.MODE_CURRENCY_SETTINGS);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }
*/
   /* public static List<BRSettingsItem> getSecuritySettings(final Activity activity) {
        List<BRSettingsItem> items = new ArrayList<>();
        items.add(new BRSettingsItem(activity.getString(R.string.TouchIdSettings_switchLabel_android), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FingerprintActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

            }
        }, false, 0));
        items.add(new BRSettingsItem(activity.getString(R.string.UpdatePin_updateTitle), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, InputPinActivity.class);
                intent.putExtra(InputPinActivity.EXTRA_PIN_MODE_UPDATE, true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivityForResult(intent, InputPinActivity.SET_PIN_REQUEST_CODE);
                activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, 0));
        items.add(new BRSettingsItem(activity.getString(R.string.SecurityCenter_paperKeyTitle), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WriteDownActivity.class);
                intent.putExtra(WriteDownActivity.EXTRA_VIEW_REASON, WriteDownActivity.ViewReason.SETTINGS.getValue());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter_from_bottom, R.anim.fade_down);
            }
        }, false, 0));
        items.add(new BRSettingsItem(activity.getString(R.string.Settings_wipe), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UnlinkActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, 0));
        return items;
    }*/

    public static List<BRSettingsItem> getElastosSettings(Context activity) {
        List<BRSettingsItem> items = new ArrayList<>();
        items.add(new BRSettingsItem("ELA_NodeSelector_title", "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity currentActivity = (Activity) v.getContext();
               // Intent intent = new Intent(currentActivity, ElaNodeActivity.class);
                //currentActivity.startActivity(intent);
                currentActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, 0));
        return items;
    }

    /*public static List<BRSettingsItem> getBitcoinSettings(final Context activity) {
        List<BRSettingsItem> items = new ArrayList<>();
        if (AuthManager.isFingerPrintAvailableAndSetup(activity)) {
            items.add(new BRSettingsItem(activity.getString(R.string.Settings_touchIdLimit_android), "", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Activity currentActivity = (Activity) v.getContext();
                    AuthManager.getInstance().authPrompt(currentActivity, null,
                            currentActivity.getString(R.string.VerifyPin_continueBody), true, false, new BRAuthCompletion() {
                                @Override
                                public void onComplete() {
                                    Intent intent = new Intent(currentActivity, SpendLimitActivity.class);
                                    currentActivity.startActivity(intent);
                                    currentActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                                }

                                @Override
                                public void onCancel() {

                                }
                            });

                }
            }, false, 0));
        }
        items.add(new BRSettingsItem(activity.getString(R.string.Settings_importTitle), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UiUtils.isClickAllowed()) return;
                Activity currentActivity = (Activity) v.getContext();
                Intent intent = new Intent(currentActivity, ImportActivity.class);
                currentActivity.startActivity(intent);
                currentActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, 0));

        items.add(new BRSettingsItem(activity.getString(R.string.ReScan_header), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UiUtils.isClickAllowed()) return;
                Activity currentActivity = (Activity) v.getContext();
                Intent intent = new Intent(currentActivity, SyncBlockchainActivity.class);
                currentActivity.startActivity(intent);
                currentActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, 0));

        //add that for all currencies
        items.add(new BRSettingsItem(activity.getString(R.string.NodeSelector_title), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity currentActivity = (Activity) v.getContext();
                Intent intent = new Intent(currentActivity, NodesActivity.class);
                currentActivity.startActivity(intent);
                currentActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, 0));
        return items;
    }*/

   /* public static List<BRSettingsItem> getBitcoinCashSettings(final Context activity) {
        List<BRSettingsItem> items = new ArrayList<>();
        items.add(new BRSettingsItem(activity.getString(R.string.Settings_importTitle), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UiUtils.isClickAllowed()) return;
                Activity currentActivity = (Activity) v.getContext();
                Intent intent = new Intent(currentActivity, ImportActivity.class);
                currentActivity.startActivity(intent);
                currentActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, 0));

        items.add(new BRSettingsItem(activity.getString(R.string.ReScan_header), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UiUtils.isClickAllowed()) return;
                Activity currentActivity = (Activity) v.getContext();
                Intent intent = new Intent(currentActivity, SyncBlockchainActivity.class);
                currentActivity.startActivity(intent);
                currentActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, 0));

        return items;
    }
*/
    /*public static List<BRSettingsItem> getProfileSettings(final Context activity){
        List<BRSettingsItem> items = new ArrayList<>();
        items.add(new BRSettingsItem(activity.getString(R.string.My_Profile_Nickname), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity currentActivity = (Activity) v.getContext();
                Intent intent = new Intent(currentActivity, ProfileEditActivity.class);
                intent.putExtra(KYC_FROME_KEY, KYC_FROME_NICKNAME);
                currentActivity.startActivityForResult(intent, BRConstants.PROFILE_REQUEST_NICKNAME);
                currentActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, R.drawable.ic_profile_nickname, BRSharedPrefs.getProfileState(activity, BRSharedPrefs.NICKNAME_STATE)));

        items.add(new BRSettingsItem(activity.getString(R.string.My_Profile_Email), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity currentActivity = (Activity) v.getContext();
                Intent intent = new Intent(currentActivity, ProfileEditActivity.class);
                intent.putExtra(KYC_FROME_KEY, KYC_FROME_EMAIL);
                currentActivity.startActivityForResult(intent, BRConstants.PROFILE_REQUEST_EMAIL);
                currentActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, R.drawable.ic_profile_email, BRSharedPrefs.getProfileState(activity, BRSharedPrefs.EMAIL_STATE)));

        items.add(new BRSettingsItem(activity.getString(R.string.My_Profile_Mobile), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity currentActivity = (Activity) v.getContext();
                Intent intent = new Intent(currentActivity, ProfileEditActivity.class);
                intent.putExtra(KYC_FROME_KEY, KYC_FROME_MOBILE);
                currentActivity.startActivityForResult(intent, BRConstants.PROFILE_REQUEST_MOBILE);
                currentActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, R.drawable.ic_profile_mobile, BRSharedPrefs.getProfileState(activity, BRSharedPrefs.MOBILE_STATE)));

        items.add(new BRSettingsItem(activity.getString(R.string.My_Profile_ID), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity currentActivity = (Activity) v.getContext();
                Intent intent = new Intent(currentActivity, ProfileEditActivity.class);
                intent.putExtra(KYC_FROME_KEY, KYC_FROME_ID);
                currentActivity.startActivityForResult(intent, BRConstants.PROFILE_REQUEST_ID);
                currentActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }, false, R.drawable.ic_profile_id, BRSharedPrefs.getProfileState(activity, BRSharedPrefs.ID_STATE)));

        return items;
    }*/

}
