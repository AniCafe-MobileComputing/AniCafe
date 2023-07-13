package com.example.anigram.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.anigram.ui.MainActivity;
import com.example.anigram.R;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import dev.katsute.mal4j.Authorization;
import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.MyAnimeListAuthenticator;

//Inspiration aus: https://stackoverflow.com/questions/69686648/oauth2-authorization-to-my-anime-list-not-working
//Service um sich um die WebView und das anmelden/autorisieren mit der MyAnimeList API zu kümmern.
public class AuthorizationMALService extends AppCompatActivity {

    //Deklarierung der Variablen
    private static final String REDIRECT_URL = "http://localhost/oauth";
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_webview);

        String codeChallenge = null;
        try {
            codeChallenge = generateCodeChallange(generateCodeVerifier());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        webview = findViewById(R.id.login_webview);
        webview.getSettings().setJavaScriptEnabled(true);
        String finalCodeChallenge = codeChallenge;
        webview.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
                Uri url = request.getUrl();
                if(url.toString().contains(REDIRECT_URL)){
                    String authorizationCode = url.getQueryParameter("code");
                    webview.setVisibility(View.GONE);
                    getUserAccessToken(authorizationCode, finalCodeChallenge);
                }
                return false;
            }
        });
        authenticateMAL(codeChallenge);
    }

    private void getUserAccessToken(String authorizationCode, String codeChallenge) {

        MyAnimeListAuthenticator authenticator = new MyAnimeListAuthenticator(new Authorization(GlobalVariables.CLIENT_ID, null, authorizationCode, codeChallenge));
        GlobalVariables.mal = MyAnimeList.withToken(authenticator.getAccessToken().getToken());

        SharedPreferences preferences = getSharedPreferences("Anigram" ,Context.MODE_PRIVATE);
        preferences.edit().putString("OAuthToken", authenticator.getAccessToken().getToken()).apply();

        Intent intent = new Intent(AuthorizationMALService.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    private void authenticateMAL(String codeChallenge) {

        String authorizationUrl = MyAnimeListAuthenticator.getAuthorizationURL(GlobalVariables.CLIENT_ID, codeChallenge);
        webview.loadUrl(authorizationUrl);
    }


    String generateCodeVerifier(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
    }

    String generateCodeChallange(String codeVerifier) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes("US-ASCII");
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }

}