package jsfks;
import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
 
public class Diario implements Serializable {

    private List<ItemDiario> listItem;

    public Diario() {
       this.listItem = new ArrayList<ItemDiario>();
    }
    public Diario(List<ItemDiario> listItem) {
      this.listItem=listItem;
    }
     
    public boolean  addItem(ItemDiario unItem) {
        listItem.add(unItem);
        return true;
    }

    public List<ItemDiario> getListItem() {
        return this.listItem;
    }
     
    public void setListItem(List<ItemDiario> list) {
        this.listItem=list;
    }
}
