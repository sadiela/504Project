package com.example.a504languagechecker;//package com.example.a504languagechecker.TextProcessor;

public class TextProcessor {

    public TextProcessor (){ }


    public String clean(String text, String language) {
        String keep = "";
        String lowerLang = language.toLowerCase();

        if(lowerLang.equals("english")) {
            keep = "[^a-zA-Z0-9 ]";
        }
        else if (lowerLang.equals("spanish")) {
            keep = "[^a-zA-Z0-9 ]";
        }
        else if(lowerLang.equals("french")) {
            keep = "[^a-zA-Z0-9 çéâêîôûàèùëïü]";
        }
        else if(lowerLang.equals("italian")) {
            keep = "[^a-zA-Z0-9 àèéòù]";
        }
        else{
            System.out.println("not supported Language");
            return null;
        }


        return text.toLowerCase().replaceAll(keep, "");
    }


    public String clean(String text, String c, String language) {
        String keep = "";
        String lowerLang = language.toLowerCase();
        if(lowerLang.equals("english")) {
            keep = "[^a-zA-Z0-9 " + c + "]";
        }
        else if (lowerLang.equals("spanish")) {
            keep = "[^a-zA-Z0-9 " + c + "áéíóúüñ" + "]";
        }
        else if(lowerLang.equals("french")) {
            keep = "[^a-zA-Z0-9 " + c + "çéâêîôûàèùëïü" + "]";
        }
        else if(lowerLang.equals("italian")) {
            keep = "[^a-zA-Z0-9 " + c + "àèéòù" + "]";
        }
        else{
            System.out.println("not supported Language");
            return null;
        }


        return text.toLowerCase().replaceAll(keep, "");
    }
}
