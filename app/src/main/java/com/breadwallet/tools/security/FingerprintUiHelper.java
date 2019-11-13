
package com.breadwallet.tools.security;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;
public class FingerprintUiHelper extends FingerprintManager.AuthenticationCallback {
    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private static final long SUCCESS_DELAY_MILLIS = 1300;
    private final FingerprintManager mFingerprintManager;
    private final Callback mCallback;
    private Context mContext;
    /**
     * Builder class for {@link FingerprintUiHelper} in which injected fields from Dagger
     * holds its fields and takes other arguments in the {@link #build} method.
     */
    public static class FingerprintUiHelperBuilder {
        private final FingerprintManager mFingerPrintManager;

        public FingerprintUiHelperBuilder(FingerprintManager fingerprintManager) {
            mFingerPrintManager = fingerprintManager;

        }

        public FingerprintUiHelper build(  Callback callback, Context context) {
            return new FingerprintUiHelper(mFingerPrintManager,
                    callback, context);
        }
    }
    /**
     * Constructor for {@link FingerprintUiHelper}. This method is expected to be called from
     * only the {@link FingerprintUiHelperBuilder} class.
     */
    private FingerprintUiHelper(FingerprintManager fingerprintManager,
                                 Callback callback, Context context) {
        mFingerprintManager = fingerprintManager;
        mCallback = callback;
        this.mContext = context;
    }
    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        mCallback.onError();

    }
    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

    }
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        mCallback.onAuthenticated();
    }
    @Override
    public void onAuthenticationFailed() {

    }
    public interface Callback {
        void onAuthenticated();
        void onError();
    }

}