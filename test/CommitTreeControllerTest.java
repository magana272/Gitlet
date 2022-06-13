package test;

import org.junit.Test;

import utils.CommitTreeController;
import utils.StagingAreaController;
import utils.DataStruct.CommitTree;
import utils.DataStruct.StageingArea;

public class CommitTreeControllerTest {
    @Test
    public void testAdd() {
        CommitTreeController.init();
        CommitTreeController.add("test.txt");
        CommitTree mytree = CommitTreeController.getTree();
        StageingArea stage = StagingAreaController.getStageingArea();
        
    }

    @Test
    public void testBranch() {

    }

    @Test
    public void testCheckout() {

    }

    @Test
    public void testCommit() {

    }

    @Test
    public void testFind() {

    }

    @Test
    public void testInit() {

    }

    @Test
    public void testLog() {

    }

    @Test
    public void testMerge() {

    }

    @Test
    public void testReset() {

    }

    @Test
    public void testRm() {

    }

    @Test
    public void testRm_branch() {

    }

    @Test
    public void testSha1() {
        

    }

    @Test
    public void testStatus() {

    }
}
