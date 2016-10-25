package ucai.cn.day_filicenter.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/13.
 */
public class Result implements Serializable {

    private int retCode;
    private boolean retMsg;
    private RetData retData;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public boolean isRetMsg() {
        return retMsg;
    }

    public void setRetMsg(boolean retMsg) {
        this.retMsg = retMsg;
    }

    public RetData getRetData() {
        return retData;
    }

    public void setRetData(RetData retData) {
        this.retData = retData;
    }

    public Result() {
    }

    public Result(int retCode, boolean retMsg, RetData retData) {
        this.retCode = retCode;
        this.retMsg = retMsg;
        this.retData = retData;
    }

    @Override
    public String toString() {
        return "{" +
                "retCode:" + retCode +
                ", retMsg:" + retMsg +
                ", retData:" + retData +
                '}';
    }
}
