package jsfks;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import java.io.Serializable;
 
@ManagedBean
@SessionScoped
public class InfoService implements Serializable {
     
    public TreeNode createInfo(List<String> list, List<List<String>> prof) {
       /* List<List<String>> prof = new ArrayList<List<String>>();
        ArrayList<String> grup = new ArrayList<String>();
        ArrayList<String> list = new ArrayList<String>();
        list.add("diego");
        list.add("clara");
        list.add("conchi");
        grup.add("DAW2"); 
        grup.add("SMR1"); 
        grup.add("SMR2"); 
        prof.add(grup);
        grup = new ArrayList<String>();
        //grup.clear();
        grup.add("DAW1"); 
        grup.add("SMR1"); 
        grup.add("SMR2"); 
        prof.add(grup);
        grup = new ArrayList<String>();
        //grup.clear();
        grup.add("DAW1"); 
        grup.add("ASIR2"); 
        prof.add(grup);
       */
     
        TreeNode root = new DefaultTreeNode(new Document("Profesores", "-", "Folder"), null);
         
    //    TreeNode profesores = new DefaultTreeNode(new Document("Diego", "-", "Folder"), root);
        List<TreeNode> profesores = new ArrayList<TreeNode>();
        for(String st : list) {
             profesores.add(new DefaultTreeNode(new Document(st, "-", "Folder"), root));
        }   
        List<TreeNode> grupos = new ArrayList<TreeNode>();
        int i=0;
        for(List<String> lst : prof) {
          for(String st : lst) {
             grupos.add(new DefaultTreeNode(new Document(st, "-", "Folder"), profesores.get(i)));
          }
          i++;
      //  TreeNode grupo = new DefaultTreeNode(new Document("DAW1", "-", "Folder"), profesores);
        } 
        //Documents 
/*
        TreeNode ev0 = new DefaultTreeNode("document", new Document("Primera Ev", "30 KB", "Word Document"), grupo);
        TreeNode ev1 = new DefaultTreeNode("document", new Document("Segunda Ev", "10 KB", "Word Document"), grupo);
        TreeNode ev2 = new DefaultTreeNode("document", new Document("Tercera Ev", "40 KB", "Pages Document"), grupo);
*/                
        i=0;
        for(List<String> lst : prof) {
          for(String st : lst) {
            TreeNode ev0 = new DefaultTreeNode("document", new Document("Primera Ev", "30 KB", "Word Document"), grupos.get(i));
            TreeNode ev1 = new DefaultTreeNode("document", new Document("Segunda Ev", "10 KB", "Word Document"), grupos.get(i));
            TreeNode ev2 = new DefaultTreeNode("document", new Document("Tercera Ev", "40 KB", "Pages Document"), grupos.get(i));
            i++;
           }
         }
         return root;
    }
}
