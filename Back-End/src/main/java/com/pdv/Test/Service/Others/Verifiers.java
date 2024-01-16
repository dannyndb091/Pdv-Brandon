package com.pdv.Test.Service.Others;

import com.pdv.Test.Models.DTOs.Inventario.DcrCloseDoc;
import com.pdv.Test.Models.DTOs.Ventas.VtaCloseSell;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@NoArgsConstructor
public class Verifiers {

    public boolean verifyMail(String mail){
        return isMailCorrect(mail);
    }

    private boolean isMailCorrect(String mail) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mail);

        return matcher.matches();
    }

    public boolean verifyCellphone(String cellphone){
        return isCellphoneCorrect(cellphone);
    }

    private boolean isCellphoneCorrect(String cellphone) {
        String regex = "\\+\\d{1,3} \\(\\d{3}\\) \\d{3} \\d{4}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cellphone);

        return matcher.matches();
    }

    public boolean verifyClCode(String clCode) {
        return isClCodeCorrect(clCode);
    }

    private boolean isClCodeCorrect(String clCode) {
        String regex = "^[a-zA-z0-9]{4,12}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(clCode);

        return matcher.matches();
    }

    public boolean verifyClName(String clName) {
        return isClNameCorrect(clName);
    }

    private boolean isClNameCorrect(String clName) {
        String regex = "^(?=.{6,50}$)[a-zA-ZñÑ]+(?: [a-zA-ZñÑ]+)*$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(clName);

        return matcher.matches();
    }


    public void verifyProSku(String proSku) {
        if (proSku == null) throw new RuntimeException("«ERR-P02»");

        String regex = "^[a-zA-z0-9]{5,64}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(proSku);

        if (!matcher.matches()) throw new RuntimeException("«ERR-P02»");
    }

    public void verifyProName(String proName) {
        if (proName == null) throw new RuntimeException("«ERR-P03»");

        String regex = "^(?=.{3,50}$)[a-zA-ZñÑ0-9.]+(?: [a-zA-ZñÑ0-9.]+)*$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(proName);

        if (!matcher.matches()) throw new RuntimeException("«ERR-P03»");
    }

    public void verifyProType(Integer proType) {
        if (proType == null || !(proType >= 0 && proType <= 1)) throw new RuntimeException("«ERR-P04»");
    }

    public void verifyProUnit(String proUnit) {
        if (proUnit == null) throw new RuntimeException("«ERR-P05»");

        String regex = "^[a-zA-z]{1,3}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(proUnit);

        if (!matcher.matches()) throw new RuntimeException("«ERR-P05»");
    }

    public void verifyProPrice(BigDecimal proPrice) {
        if(proPrice == null || BigDecimal.valueOf(0.01).compareTo(proPrice) > 0) throw new RuntimeException("«ERR-P06»");
    }

    public void verifyProFinalPrice(BigDecimal proPrice, BigDecimal proFinalPrice) {
        if (proPrice == null || proFinalPrice == null) throw new RuntimeException("«ERR-P07»");

        BigDecimal resultPrice = proPrice.add(proPrice.multiply(BigDecimal.valueOf(0.16)));

        if (resultPrice.compareTo(proFinalPrice) != 0) throw new RuntimeException("«ERR-P07»");
    }

    public void verifyProMaxPercDisc(BigDecimal proMaxPercDisc) {
        if (proMaxPercDisc == null || BigDecimal.valueOf(0.9).compareTo(proMaxPercDisc) <= 0) throw new RuntimeException("«ERR-P08»");
        if (BigDecimal.valueOf(0).compareTo(proMaxPercDisc) > 0) throw new RuntimeException("«ERR-P10»");
    }

    public void verifyProQty(Integer proQty) {
        if (proQty == null || proQty <= 0) throw new RuntimeException("«ERR-V34»");
    }

    public void verifyBigDecimalDataSuperiorCero(BigDecimal movPU) {
        if(movPU == null || BigDecimal.valueOf(0.01).compareTo(movPU) > 0) throw new RuntimeException("«ERR-V35»");
    }

    public void verifyBigDecimalData(BigDecimal movPU) {
        if(movPU == null || BigDecimal.valueOf(0).compareTo(movPU) > 0) throw new RuntimeException("«ERR-V35»");
    }

    public void verifyCloseSellData(VtaCloseSell closeSell) {
        if(closeSell.getInvoice() == null || closeSell.getDocNet() == null || closeSell.getDocDisc() == null ||
                closeSell.getDocSubtotal() == null || closeSell.getDocTax() == null || closeSell.getDocTotal() == null ||
                closeSell.getPayCash() == null || closeSell.getPayDigital() == null) throw new RuntimeException("«ERR-V51»");
        verifyBigDecimalData(closeSell.getPayCash());
        verifyBigDecimalData(closeSell.getPayDigital());
        verifyBigDecimalDataSuperiorCero(closeSell.getDocNet());
        verifyBigDecimalData(closeSell.getDocDisc());
        verifyBigDecimalDataSuperiorCero(closeSell.getDocSubtotal());
        verifyBigDecimalData(closeSell.getDocTax());
        verifyBigDecimalDataSuperiorCero(closeSell.getDocTotal());
    }

    public void verifyCloseDecrease(DcrCloseDoc closeSell) {
        if(closeSell.getInvoice() == null || closeSell.getDocTotal() == null)
            throw new RuntimeException("«ERR-I18»");
        verifyBigDecimalDataSuperiorCero(closeSell.getDocTotal());
    }
}
