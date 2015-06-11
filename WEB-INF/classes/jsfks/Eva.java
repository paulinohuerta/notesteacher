package jsfks;
import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
 
public class Eva implements Serializable {

    private List<ItemEva> listItem;

    public Eva() {
       this.listItem = new ArrayList<ItemEva>();
    }
    public Eva(List<ItemEva> listItem) {
      this.listItem=listItem;
    }
     
    public boolean  addItem(ItemEva unItem) {
        listItem.add(unItem);
        return true;
    }

    public ItemEva getPrimero() {
        return this.listItem.get(0);
    }

    public List<ItemEva> getListItem() {
        return this.listItem;
    }
     
    public void setListItem(List<ItemEva> list) {
        this.listItem=list;
    }
}
