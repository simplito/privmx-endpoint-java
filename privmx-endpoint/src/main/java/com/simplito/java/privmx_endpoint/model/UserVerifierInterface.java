package com.simplito.java.privmx_endpoint.model;

import java.util.List;

public interface UserVerifierInterface {
    List<Boolean> verify(List<VerificationRequest> request);
}