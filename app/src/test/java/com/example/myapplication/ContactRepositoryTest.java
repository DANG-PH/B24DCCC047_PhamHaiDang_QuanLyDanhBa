package com.example.myapplication;

import org.junit.Test;
import static org.junit.Assert.*;

public class ContactRepositoryTest {
    @Test public void sampleContactsAndFavoritesMatchAssignment() {
        ContactRepository repo = new ContactRepository();
        assertEquals(3, repo.getContacts().size());
        assertEquals(1, repo.getFavoriteCount());
        assertEquals("Nguyễn Văn An", repo.getContacts().get(0).getFullName());
        assertEquals("A", repo.getContacts().get(0).getInitial());
    }
    @Test public void validatesPhoneAndName() {
        assertTrue(ContactValidator.isValidPhone("0901234567"));
        for (String phone : new String[]{"", "9012345678", "0123", "09012345678", "+84901234567", "090abc4567", "０９０１２３４５６７"}) {
            assertFalse(phone, ContactValidator.isValidPhone(phone));
        }
        assertFalse(ContactValidator.isValidName("  "));
        assertTrue(ContactValidator.isValidName(" Nguyễn An "));
    }
    @Test public void duplicateNumbersRejectedAndStatisticsStayInSync() {
        ContactRepository repo = new ContactRepository();
        assertFalse(repo.add(new Contact("Trùng", "0901234567", "Khác", "Khác", false, "")));
        assertEquals(3, repo.getContacts().size());
        Contact added = new Contact("Phạm Hải Đăng", "0966666666", "Bạn bè", "Nam", true, "Ghi chú");
        assertTrue(repo.add(added));
        assertEquals(4, repo.getContacts().size());
        assertEquals(2, repo.getFavoriteCount());
        repo.findById(added.getId()).setFavorite(false);
        assertEquals(1, repo.getFavoriteCount());
        repo.delete(added.getId());
        assertEquals(3, repo.getContacts().size());
        assertNull(repo.findById(added.getId()));
        assertFalse(repo.containsPhone("0966666666"));
    }
}
