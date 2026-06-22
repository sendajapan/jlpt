package com.scholarlyapps.pathlingo.ui.utils;

import android.content.Context;

import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import java.util.concurrent.Executors;

public class GoogleAuthClient {

    private final Context context;
    private final String webClientId;
    private final CredentialManager credentialManager;

    public GoogleAuthClient(Context context, String webClientId) {
        this.context = context;
        this.webClientId = webClientId;
        this.credentialManager = CredentialManager.create(context);
    }

    public void signIn(GoogleSignInCallback callback) {
        GetSignInWithGoogleOption option = new GetSignInWithGoogleOption.Builder(webClientId).build();
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(option)
                .build();

        credentialManager.getCredentialAsync(
                context,
                request,
                null,
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        if (result.getCredential() instanceof CustomCredential) {
                            CustomCredential credential = (CustomCredential) result.getCredential();
                            if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
                                try {
                                    GoogleIdTokenCredential tokenCredential =
                                            GoogleIdTokenCredential.createFrom(credential.getData());
                                    callback.onSuccess(tokenCredential.getIdToken());
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                    }
                }
        );
    }
}
