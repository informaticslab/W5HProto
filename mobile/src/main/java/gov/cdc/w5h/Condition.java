package gov.cdc.w5h;

import java.util.ArrayList;
import java.util.List;

public class Condition {

    public int id;
    public int parentId;
    public String title;
    public String regimensPage;
    public String dxtxPage;
    public List<String> breadcrumbs;
    public List<Condition> childrenConditions;
    private ArrayList<String> childrenConditionTitles;
    private ArrayList<Integer> childrenConditionIds;

    public ArrayList<String> getChildrenConditionTitles() {
        return childrenConditionTitles;
    }

    public ArrayList<Integer> getChildrenConditionIds() {
        return childrenConditionIds;
    }

    public Condition(int id, int parentId, String title, String regimensPage, String dxtxPage,
                     List<Condition> children, List<String> breadcrumbs) {

        this.id = id;
        this.parentId = parentId;
        this.title = title;
        this.childrenConditions = children;
        this.regimensPage = regimensPage;
        this.dxtxPage = dxtxPage;
        this.breadcrumbs = breadcrumbs;
        childrenConditionTitles = new ArrayList<>();
        childrenConditionIds = new ArrayList<>();
        if(children.size() !=0){
            for (Condition c : children) {
                childrenConditionTitles.add(c.title);
                childrenConditionIds.add(c.id);
            }
        }
        //Log.d("Condition", "Condition object created with name = " + title + " has " + String.valueOf(numberOfChildren()) + " children.");

    }

    public int numberOfChildren() {
        return childrenConditions.size();
    }

    public boolean isRootCondition() {

        if (id == 0)
            return true;
        return false;
    }


}

