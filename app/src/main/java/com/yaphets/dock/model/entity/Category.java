package com.yaphets.dock.model.entity;


import com.yaphets.storage.annotation.Id;

public class Category {
    @Id
    private int id;
    private String name;
    private String full_name;
    private String chinese_name;

    public Category() {

    }

    public Category(int id, String name, String fullName, String chineseName) {
        this.id = id;
        this.name = name;
        this.full_name = fullName;
        this.chinese_name = chineseName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return full_name;
    }

    public void setFullName(String full_Name) {
        this.full_name = full_Name;
    }

    public String getChineseName() {
        return chinese_name;
    }

    public void setChineseName(String chineseName) {
        this.chinese_name = chineseName;
    }

    @Override
    public String toString() {
        return chinese_name.substring(0, chinese_name.length() - 2);
    }


    /*private static Map<Integer, Category> mBaseCategories = new HashMap<>();

    public static Category createInstance(int permarykey) throws SQLException {
        Category ctg = mBaseCategories.get(permarykey);

        if (ctg == null) {
            ctg = new Category();
            ctg.setId(permarykey);
            ctg = GenericDAO.find(ctg);

            mBaseCategories.put(permarykey, ctg);
        }

        return ctg;
    }*/
}
