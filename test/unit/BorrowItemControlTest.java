package unit;

import library.borrowitem.BorrowItemControl;
import library.borrowitem.BorrowItemUI;
import library.entities.*;
import library.returnItem.ReturnItemControl;
import library.returnItem.ReturnItemUI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

class BorrowItemControlTest {

    @AfterEach
    void teardown() {
        // Reset library state.
        Path path = Paths.get("library.obj");
        if (Files.exists(path)) {
            File file = path.toFile();
            file.delete();
        }
    }

    @Test
    void canBorrow2Items() {
        Library library = Library.getInstance();

        // Create borrow control.
        BorrowItemUI borrowItemUI = mock(BorrowItemUI.class);
        BorrowItemControl borrowItemControl = new BorrowItemControl();
        borrowItemControl.setUI(borrowItemUI);

        // Create return item control.
        ReturnItemUI returnItemUI = mock(ReturnItemUI.class);
        ReturnItemControl returnItemControl = new ReturnItemControl();
        returnItemControl.setUi(returnItemUI);

        // Create state.
        library.addItem("jane", "doe", "1", ItemType.BOOK);
        library.addItem("jack", "davidson", "2", ItemType.BOOK);
        library.addPatron("john", "doe", "john@example.com", 1234);

        // Create loan.
        borrowItemControl.cardSwiped(1);
        borrowItemControl.itemScanned(1);
        borrowItemControl.itemScanned(2);

        // Verify UI is finalised.
        verify(borrowItemUI, times(1)).setFinalising();
    }

    @Test
    void cannotBorrow3Items() {
        try {
            Library library = Library.getInstance();

            // Create borrow control.
            BorrowItemUI borrowItemUI = mock(BorrowItemUI.class);
            BorrowItemControl borrowItemControl = new BorrowItemControl();
            borrowItemControl.setUI(borrowItemUI);

            // Create return item control.
            ReturnItemUI returnItemUI = mock(ReturnItemUI.class);
            ReturnItemControl returnItemControl = new ReturnItemControl();
            returnItemControl.setUi(returnItemUI);

            // Create state.
            library.addItem("jane", "doe", "1", ItemType.BOOK);
            library.addItem("jack", "davidson", "2", ItemType.BOOK);
            library.addItem("jill", "daniels", "3", ItemType.BOOK);
            library.addPatron("john", "doe", "john@example.com", 1234);

            // Create loan.
            borrowItemControl.cardSwiped(1);
            borrowItemControl.itemScanned(1);
            borrowItemControl.itemScanned(2);
            borrowItemControl.itemScanned(3);

            fail("Should not allow 3 borrow items");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

}