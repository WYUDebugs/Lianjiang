package com.example.sig.lianjiang.bean;
import com.contrarywind.interfaces.IPickerViewData;
/**
 * Created by sig on 2018/9/11.
 */

public class CardBean implements IPickerViewData{
    int id;
    String cardNo;

    public CardBean(int id, String cardNo) {
        this.id = id;
        this.cardNo = cardNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Override
    public String getPickerViewText() {
        return cardNo;
    }
}
