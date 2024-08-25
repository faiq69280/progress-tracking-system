package com.creativeuncommons.ProgressTrackingSystem.security;

import java.util.List;

public record AuthenticationResponse(String jwt,
                                     List<String> roles,
                                     String userName) {
}
