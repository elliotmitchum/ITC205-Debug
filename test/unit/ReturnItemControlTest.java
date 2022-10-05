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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReturnItemControlTest {

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
    void shouldRun() {
        Calendar calendar = Calendar.getInstance();
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
        library.addItem("jane doe", "example title", "1", ItemType.BOOK);
        library.addPatron("john", "doe", "john@example.com", 1234);

        // Create loan.
        borrowItemControl.cardSwiped(1);
        borrowItemControl.itemScanned(1);
        borrowItemControl.borrowingCompleted();
        borrowItemControl.commitLoans();

        // Set calendar to +3 days in the future.
        calendar.incrementDate(3);

        // Discharge the loan.
        returnItemControl.itemScanned(1);
        library.updateCurrentLoanStatus();
        returnItemControl.dischargeLoan(false);

        // Assert the fee.
        Patron patron = library.getPatron(1);
        assertEquals(1.00, patron.finesOwed());
    }

}