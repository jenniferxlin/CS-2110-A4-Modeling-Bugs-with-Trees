import static org.junit.Assert.*;
import static common.JUnitUtil.*;

import java.util.Arrays;
import java.util.function.Function;

import org.junit.BeforeClass;
import org.junit.Test;

public class BugTreeTest {

    private static Network n;
    private static Human[] humans;

    @BeforeClass
    public static void setup(){
        n= new Network();
        humans= new Human[]{new Human("A", n, 0), new Human("B", n, 0), new Human("C", n, 0),
                new Human("D", n, 0), new Human("E", n, 0), new Human("F", n, 0),
                new Human("G", n, 0), new Human("H", n, 0), new Human("I", n, 0),
                new Human("J", n, 0), new Human("K", n, 0), new Human("L", n, 0)
        };
    }

    @Test
    public void testBuiltInGetters(){
        BugTree dt= new BugTree(humans[1]);
        assertEquals("B", toStringBrief(dt));
    }

    @Test
    public void testAdd() {
        BugTree dt= new BugTree(humans[1]); 

        //Test add to root
        BugTree dt2= dt.add(humans[1], humans[2]);
        assertEquals("B[C]", toStringBrief(dt));

        //Test add to non-root
        BugTree dt3= dt.add(humans[2], humans[3]);
        assertEquals("B[C[D]]", toStringBrief(dt));

        //Test add second child
        BugTree dt4= dt.add(humans[2], humans[0]);
        assertEquals("B[C[A D]]", toStringBrief(dt));
    }
    
    @Test
    public void testDepthOf() {
    	
    	//Test depth of root//
    	BugTree dt= new BugTree(humans[0]);
    	assertEquals(0, dt.depthOf(humans[0]));
    	
    	//Test depth of non-existent root//
    	assertEquals(-1, dt.depthOf(humans[1]));
    	
    	//Test child of root//
    	dt.add(humans[0], humans[2]);
    	assertEquals(1, dt.depthOf(humans[2]));
    	
    	//Test depth of children of root's direct child//
    	dt.add(humans[2], humans[3]);
    	assertEquals(2, dt.depthOf(humans[3]));
    	
    	dt.add(humans[2], humans[5]);
    	assertEquals(2, dt.depthOf(humans[5]));
    	
    	//Multiple depths//
    	dt.add(humans[3], humans[7]);
    	assertEquals(3, dt.depthOf(humans[7]));
    	
    	dt.add(humans[3], humans[4]);
    	assertEquals(3, dt.depthOf(humans[4]));
    		
    }
    
    @Test
    public void testWidthAtDepth() {
    	BugTree dt_width = new BugTree(humans[0]);
    	assertEquals(1, dt_width.widthAtDepth(0));
    	
    	dt_width.add(humans[0], humans[8]);
    	assertEquals(1, dt_width.widthAtDepth(1));
    	
    	dt_width.add(humans[8], humans[9]);
    	dt_width.add(humans[8], humans[6]);
    	dt_width.add(humans[8], humans[5]);
    	assertEquals(3, dt_width.widthAtDepth(2));
    	
    	dt_width.add(humans[9], humans[3]);
    	dt_width.add(humans[9], humans[2]);
    	dt_width.add(humans[5], humans[10]);
    	dt_width.add(humans[6], humans[11]);
    	assertEquals(4, dt_width.widthAtDepth(3));
    }
    
    @Test
    public void testBugRouteTo() {
    	BugTree dt = new BugTree(humans[0]);
    	BugTree dt2 = dt.add(humans[0], humans[1]);
    	dt.add(humans[0], humans[2]);
    	assertEquals(Arrays.asList(humans[0]), dt.bugRouteTo(humans[0]));
    	assertEquals(Arrays.asList(humans[0], humans[2]), dt.bugRouteTo(humans[2]));
    	assertEquals(Arrays.asList(humans[0], humans[1]), dt.bugRouteTo(humans[1]));
    	
    	dt.add(humans[1], humans[3]);
    	assertEquals(Arrays.asList(humans[1], humans[3]), dt2.bugRouteTo(humans[3]));
    	assertEquals(Arrays.asList(humans[0], humans[2]), dt.bugRouteTo(humans[2]));
    	
    	
    }
    
    @Test
    public void testSharedAncestorOf() {
    	BugTree dt = new BugTree(humans[0]);
    	dt.add(humans[0], humans[1]);
    	dt.add(humans[0], humans[2]);
    	//root to itself = root//
    	assertEquals(humans[0], dt.sharedAncestorOf(humans[0],humans[0]));
    	
    	//root to direct child = root//
    	assertEquals(humans[0], dt.sharedAncestorOf(humans[0],humans[1]));
    	
    	//child to itself = child//
    	assertEquals(humans[1], dt.sharedAncestorOf(humans[1],humans[1]));
    	
    	//child and root = root//
    	assertEquals(humans[0], dt.sharedAncestorOf(humans[2],humans[0]));
    	
    	//direct children of root = root//
    	assertEquals(humans[0], dt.sharedAncestorOf(humans[1],humans[2]));
    	
    	dt.add(humans[1], humans[3]);
    	dt.add(humans[1], humans[4]);
    	
    	BugTree dt2 = dt.getTree(humans[1]);
    	
    	dt.add(humans[2], humans[5]);
    	dt.add(humans[2], humans[6]);
    	//non-direct children share root//
    	assertEquals(humans[1], dt.sharedAncestorOf(humans[3],humans[4]));
    	
    	assertEquals(humans[2], dt.sharedAncestorOf(humans[5],humans[6]));
    	
    	//leaves share original root//
    	assertEquals(humans[0], dt.sharedAncestorOf(humans[4],humans[6]));
    	
    	assertEquals(humans[1], dt.sharedAncestorOf(humans[3],humans[4]));
    	
    	assertEquals(humans[1], dt.sharedAncestorOf(humans[4],humans[1]));
    	
    	assertEquals(null, dt2.sharedAncestorOf(humans[1],humans[5]));
    	
    	assertEquals(null, dt2.sharedAncestorOf(humans[3],humans[5]));
    	
    	assertEquals(null, dt2.sharedAncestorOf(humans[0],humans[1]));
    	
    	//non-existent child//
    	assertEquals(null, dt.sharedAncestorOf(humans[0],humans[9]));
    	
    	//null child//
    	assertEquals(null, dt.sharedAncestorOf(humans[0],null));
    	
    	assertEquals(humans[0], dt.sharedAncestorOf(humans[3],humans[6]));
    
    }
    
    @Test
    public void testEquals() {
    	BugTree dt = new BugTree(humans[0]);
    	
    	//same object//
    	assertEquals(true, dt.equals(dt));
    	
    	BugTree dt2 = dt.add(humans[0], humans[1]);
    	dt.add(humans[0], humans[2]);
    	dt.add(humans[0], humans[3]);
    	
    	BugTree dt3 = new BugTree(humans[0]);
    	BugTree dt4 = dt3.add(humans[0], humans[1]);
    	dt3.add(humans[0], humans[3]);
    	dt3.add(humans[0], humans[2]);
    	
    	//same root human and same child size//
    	assertEquals(true, dt2.equals(dt4));
    	assertEquals(true, dt4.equals(dt2));
    	assertEquals(true, dt.equals(dt3));
    	assertEquals(false, dt2.equals(dt));
    }
    /** Return a representation of this tree. This representation is:
     * (1) the name of the Human at the root, followed by
     * (2) the representations of the children (in alphabetical
     *     order of the children's names).
     * There are two cases concerning the children.
     *
     * No children? Their representation is the empty string.
     * Children? Their representation is the representation of each child, with
     * a blank between adjacent ones and delimited by "[" and "]".
     * Examples:
     * One-node tree: "A"
     * root A with children B, C, D: "A[B C D]"
     * root A with children B, C, D and B has a child F: "A[B[F] C D]"
     */
    public static String toStringBrief(BugTree t) {
        String res= t.getRoot().getName();

        Object[] childs= t.copyOfChildren().toArray();
        if (childs.length == 0) return res;
        res= res + "[";
        selectionSort1(childs);

        for (int k= 0; k < childs.length; k= k+1) {
            if (k > 0) res= res + " ";
            res= res + toStringBrief(((BugTree)childs[k]));
        }
        return res + "]";
    }

    /** Sort b --put its elements in ascending order.
     * Sort on the name of the Human at the root of each BugTree
     * Throw a cast-class exception if b's elements are not BugTrees */
    public static void selectionSort1(Object[] b) {
        int j= 0;
        // {inv P: b[0..j-1] is sorted and b[0..j-1] <= b[j..]}
        // 0---------------j--------------- b.length
        // inv : b | sorted, <= | >= |
        // --------------------------------
        while (j != b.length) {
            // Put into p the index of smallest element in b[j..]
            int p= j;
            for (int i= j+1; i != b.length; i++) {
                String bi= ((BugTree)b[i]).getRoot().getName();
                String bp= ((BugTree)b[p]).getRoot().getName();
                if (bi.compareTo(bp) < 0) {
                    p= i;

                }
            }
            // Swap b[j] and b[p]
            Object t= b[j]; b[j]= b[p]; b[p]= t;
            j= j+1;
        }
    }

}
