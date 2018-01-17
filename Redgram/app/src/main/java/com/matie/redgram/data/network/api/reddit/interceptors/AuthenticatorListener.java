package com.matie.redgram.data.network.api.reddit.interceptors;


public interface AuthenticatorListener {
    // // TODO: 2018-01-16 use launcher instead of using context.startActivity
    void onAuthenticationRequest();
}
