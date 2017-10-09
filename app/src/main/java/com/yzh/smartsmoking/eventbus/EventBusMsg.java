package com.yzh.smartsmoking.eventbus;


public class EventBusMsg {

    public static final int MSG_REFRESH_HOME_DATE = 8001;
    public static final int MSG_REFRESH_POWER_VALUE = 8002;


    private int MsgType;

    private int value;
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMsgType() {
        return MsgType;
    }

    public void setMsgType(int msgType) {
        MsgType = msgType;
    }




    private EventMsgType eventMsgType;
    public EventMsgType getEventMsgType() {
        return eventMsgType;
    }

    public void setEventMsgType(EventMsgType eventMsgType) {
        this.eventMsgType = eventMsgType;
    }




    private boolean judgeLogin;
    public boolean isJudgeLogin() {
        return judgeLogin;
    }

    public void setJudgeLogin(boolean judgeLogin) {
        this.judgeLogin = judgeLogin;
    }





    public enum EventMsgType {
        JUDGE_IS_LOGIN_ORNOT,
        MSG_REFRESH
    }
}
