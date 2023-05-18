package me.madex.ads.demo;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import me.madex.ads.Madex;
import me.madex.ads.MadexConfiguration;
import me.madex.ads.MadexInterstitialListener;
import me.madex.ads.MadexRewardedListener;
import me.madex.ads.mediation.MadexAdapterCustomKeys;

public class MainActivity extends AppCompatActivity {
    private TextView logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtons();
        setListeners();

        initSDK();
    }

    private void setButtons() {
        findViewById(R.id.load_interstitial_button).setOnClickListener(v -> Madex.loadAd(this, Madex.INTERSTITIAL));
        findViewById(R.id.show_interstitial_button).setOnClickListener(v -> Madex.showAd(this, Madex.INTERSTITIAL));
        findViewById(R.id.destroy_interstitial_button).setOnClickListener(v -> Madex.destroyAd(Madex.INTERSTITIAL));

        findViewById(R.id.load_rewarded_button).setOnClickListener(v -> Madex.loadAd(this, Madex.REWARDED));
        findViewById(R.id.show_rewarded_button).setOnClickListener(v -> Madex.showAd(this, Madex.REWARDED));
        findViewById(R.id.destroy_rewarded_button).setOnClickListener(v -> Madex.destroyAd(Madex.REWARDED));

        findViewById(R.id.show_consent_button).setOnClickListener(v -> {});

        logger = findViewById(R.id.events);
    }

    private void initSDK() {
        Madex.setCustomParams(MadexAdapterCustomKeys.yandexInterstitialID, BuildConfig.YANDEX_INTERSTITIAL_ID);
        Madex.setCustomParams(MadexAdapterCustomKeys.yandexRewardedID, BuildConfig.YANDEX_REWARDED_ID);

        Madex.setCustomParams(MadexAdapterCustomKeys.mintegralAppID, BuildConfig.MINTEGRAL_APP_ID);
        Madex.setCustomParams(MadexAdapterCustomKeys.mintegralApiKey, BuildConfig.MINTEGRAL_API_KEY);
        Madex.setCustomParams(MadexAdapterCustomKeys.mintegralInterstitialPlacementId, BuildConfig.MINTEGRAL_INTERSTITIAL_PLACEMENT_ID);
        Madex.setCustomParams(MadexAdapterCustomKeys.mintegralInterstitialUnitId, BuildConfig.MINTEGRAL_INTERSTITIAL_ID);
        Madex.setCustomParams(MadexAdapterCustomKeys.mintegralRewardedPlacementId, BuildConfig.MINTEGRAL_REWARDED_PLACEMENT_ID);
        Madex.setCustomParams(MadexAdapterCustomKeys.mintegralRewardedUnitId, BuildConfig.MINTEGRAL_REWARDED_ID);
        Madex.setCustomParams(MadexAdapterCustomKeys.ironSourceAppID, BuildConfig.IRON_SOURCE_APP_ID);
        Madex.setCustomParams(MadexAdapterCustomKeys.ironSourceInterstitialPlacementID, BuildConfig.IRON_SOURCE_INTERSTITIAL_ID);
        Madex.setCustomParams(MadexAdapterCustomKeys.ironSourceRewardedPlacementID, BuildConfig.IRON_SOURCE_REWARDED_ID);

        Madex.enableDebug(true);
        final MadexConfiguration config = new MadexConfiguration(BuildConfig.MADEX_PUBLISHER_ID, BuildConfig.MADEX_INTERSTITIAL_ID, BuildConfig.MADEX_REWARDED_ID);

        Madex.setUserConsent(true);

        Madex.initialize(config);

        logEvent("Madex initialized");
    }

    private void setListeners() {
        Madex.setInterstitialListener(new MadexInterstitialListener() {
            @Override
            public void onInterstitialLoaded() {
                logEvent("onInterstitialLoaded");
            }

            @Override
            public void onInterstitialLoadFail(String error) {
                logEvent("onInterstitialLoadFail: " + error);
            }

            @Override
            public void onInterstitialShown() {
                logEvent("onInterstitialShown");
            }

            @Override
            public void onInterstitialShowFailed(String error) {
                logEvent("onInterstitialShowFailed: " + error);
            }

            @Override
            public void onInterstitialClosed() {
                logEvent("onInterstitialClosed");
            }
        });

        Madex.setRewardedListener(new MadexRewardedListener() {
            @Override
            public void onRewardedLoaded() {
                logEvent("onRewardedLoaded");
            }

            @Override
            public void onRewardedLoadFail(String error) {
                logEvent("onRewardedLoadFail: " + error);
            }

            @Override
            public void onRewardedShown() {
                logEvent("onRewardedShown");
            }

            @Override
            public void onRewardedShowFailed(String error) {
                logEvent("onRewardedShowFailed: " + error);
            }

            @Override
            public void onRewardedClosed() {
                logEvent("onRewardedClosed");
            }

            @Override
            public void onRewardedFinished() {
                logEvent("onRewardedFinished");
            }
        });
    }

    private void logEvent(String message) {
        final String text = logger.getText().toString();
        logger.setText(String.format("%s\n%s", text, message));
    }
}