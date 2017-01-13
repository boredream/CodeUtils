package parse.diandianbo;

import parse.Pointer;

public class Book extends Pointer {
    public int musicType;
    public String surfaceImg;
    public String surfaceSleepImg;
    public int tagType;
    public String musicUrl;
    public String musicUrlEng;
    public int bookSetType;
    public int ageRange;
    public float price;
    public int categoryId;
    public int languageStatus;
    public Pointer parentBookSet;
    public String name;
    public String engName;
    public String bookSetDesc;

    public String getDisplayName() {
        return languageStatus <= 2 ? name : engName;
    }
}
