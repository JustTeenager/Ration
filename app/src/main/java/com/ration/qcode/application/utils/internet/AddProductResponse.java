package com.ration.qcode.application.utils.internet;

/**
 * Created on 13.07.2017.
 *
 * @author Влад.
 */

public class AddProductResponse {

    public AddProductResponse(){
    }

    private String status;
    private String answer;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
