package com.uqudo.android.sample.jwk;

import android.util.Log;

import com.auth0.jwk.JwkProvider;

import java.security.Key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolver;

public class JwkKeyResolver implements SigningKeyResolver {

    private static final String TAG = "JwkKeyResolver";

    private final JwkProvider keyStore;

    public JwkKeyResolver(JwkProvider keyStore) {
        this.keyStore = keyStore;
    }

    @Override
    public Key resolveSigningKey(JwsHeader header, Claims claims) {
        return this.getKey(header);
    }

    @Override
    public Key resolveSigningKey(JwsHeader header, String plaintext) {
        return this.getKey(header);
    }

    private Key getKey(JwsHeader header) {
        try {
            String keyId = header.getKeyId();
            return keyStore.get(keyId).getPublicKey();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

}
