package homeScreen.Model;

public class ValidInputException {



    public boolean verifyVarChar10(String stringToVerify){
        return stringToVerify.length() <= 10 && stringToVerify.length() != 0;

    }

    public boolean verifyVarChar20(String stringToVerify ){
        return stringToVerify.length() <= 20 && stringToVerify.length() != 0;

    }

    public boolean verifyVarChar40(String stringToVerify){
        return stringToVerify.length() <= 40 && stringToVerify.length() != 0;

    }

    public boolean verifyVarChar45(String stringToVerify){
        return stringToVerify.length() <= 45 && stringToVerify.length() != 0;

    }

    public boolean verifyVarChar50(String stringToVerify){
        return stringToVerify.length() <= 50 && stringToVerify.length() != 0;

    }
    public boolean verifyVarChar50withNull(String stringToVerify){
        return stringToVerify.length() <= 50;

    }
    public boolean verifyVarChar255(String stringToVerify){
        return stringToVerify.length() <= 255 && stringToVerify.length() != 0;

    }

}
