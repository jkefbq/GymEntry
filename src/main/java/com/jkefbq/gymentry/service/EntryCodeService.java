package com.jkefbq.gymentry.service;

public interface EntryCodeService {
    String generateUserEntryCode(String email);
    String getEmailByCode(String code);
}
