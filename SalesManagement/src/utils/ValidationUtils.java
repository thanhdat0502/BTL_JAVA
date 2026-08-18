package utils;

import java.util.regex.Pattern;

public final class ValidationUtils {
    private static final Pattern EMAIL=Pattern.compile("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE=Pattern.compile("^(0|\\+84)[0-9]{9,10}$");
    private ValidationUtils(){}
    public static String required(String value,String label){if(value==null||value.trim().isEmpty())throw new IllegalArgumentException(label+" không được để trống.");return value.trim();}
    public static long positiveLong(String value,String label){try{long n=Long.parseLong(value.trim());if(n<=0)throw new NumberFormatException();return n;}catch(NumberFormatException e){throw new IllegalArgumentException(label+" phải là số lớn hơn 0.");}}
    public static int nonNegativeInt(String value,String label){try{int n=Integer.parseInt(value.trim());if(n<0)throw new NumberFormatException();return n;}catch(NumberFormatException e){throw new IllegalArgumentException(label+" phải là số nguyên không âm.");}}
    public static void email(String value){if(!EMAIL.matcher(required(value,"Email")).matches())throw new IllegalArgumentException("Email không đúng định dạng.");}
    public static void phone(String value){if(!PHONE.matcher(required(value,"Số điện thoại").replace(" ","")).matches())throw new IllegalArgumentException("Số điện thoại không đúng định dạng.");}
}
