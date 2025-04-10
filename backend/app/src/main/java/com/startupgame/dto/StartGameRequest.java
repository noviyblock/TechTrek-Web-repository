package com.startupgame.dto;

public class StartGameRequest {
    private Long missionId;
    private String companyName;
    private Long userId;

    public StartGameRequest(Long missionId, String companyName, Long userId) {
        this.missionId = missionId;
        this.companyName = companyName;
        this.userId = userId;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
