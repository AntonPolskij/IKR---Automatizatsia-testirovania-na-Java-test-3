import com.max.BookingService;
import com.max.CantBookException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class BookingServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(BookingServiceTest.class);
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingService = spy(BookingService.class);
    }

    @Test
    void testBookPositive() throws CantBookException {
        String userId = "user1";
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plusHours(1);

        doReturn(true).when(bookingService).checkTimeInBD(from, to);
        doReturn(true).when(bookingService).createBook(userId, from, to);

        boolean result = bookingService.book(userId, from, to);

        assertTrue(result, "Booking should be successful");
        logger.info("Booking successful for user {}, from {}, to {}", userId, from, to);
    }

    @Test
    void testBookNegative() {
        String userId = "user2";
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plusHours(1);

        doReturn(false).when(bookingService).checkTimeInBD(from, to);

        try {
            bookingService.book(userId, from, to);
        } catch (CantBookException e) {
            logger.error("Can't book for user {}, from {}, to {}", userId, from, to);
            return;
        }

        assertFalse(true);
    }
}
