package com.brand.prelaunch.service;

import org.springframework.stereotype.Service;

@Service
public class GlobalStateService {
    private boolean isDropLive = true; // Default to Live

    public boolean isDropLive() {
        return isDropLive;
    }

    public void setDropLive(boolean dropLive) {
        isDropLive = dropLive;
    }
}