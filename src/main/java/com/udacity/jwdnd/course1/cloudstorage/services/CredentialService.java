package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public void addNewCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credentialMapper.insert(new Credential(null, credential.getUrl(), credential.getUsername(), encodedKey, encryptedPassword, credential.getUserId()));
    }

    public void updateCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credentialMapper.update(new Credential(credential.getCredentialId(), credential.getUrl(), credential.getUsername(), encodedKey, encryptedPassword, credential.getUserId()));
    }

    public List<Credential> getAllCredentialsByUserId(Integer userId) {
        return credentialMapper.getAllCredentialsByUserId(userId);
    }

    public void deleteCredential(Integer credentialId, Integer userId) {
        credentialMapper.delete(credentialId, userId);
    }
}
