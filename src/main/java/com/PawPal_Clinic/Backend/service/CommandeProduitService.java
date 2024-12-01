package com.PawPal_Clinic.Backend.service;

import com.PawPal_Clinic.Backend.dto.CommandeProduitDto;
import com.PawPal_Clinic.Backend.model.CommandeProduit;
import com.PawPal_Clinic.Backend.repository.CommandeProduitRepository;
import com.PawPal_Clinic.Backend.repository.CommandeRepository;
import com.PawPal_Clinic.Backend.repository.ProduitRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommandeProduitService {

    @Autowired
    private CommandeProduitRepository commandeProduitRepository;
    @Autowired
    private CommandeRepository commandeRepository;
    @Autowired
    private ProduitRepository produitRepository;
    @PersistenceContext
    private EntityManager entityManager;


    @Transactional(readOnly = true)
    public List<CommandeProduitDto> getAllCommandeProduits() {
        return commandeProduitRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CommandeProduitDto> getCommandeProduitById(Integer id) {
        return commandeProduitRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional
    public CommandeProduitDto createCommandeProduit(CommandeProduitDto commandeProduitDto) {
        CommandeProduit commandeProduit = convertToEntity(commandeProduitDto);
        CommandeProduit savedCommandeProduit = commandeProduitRepository.save(commandeProduit);
        entityManager.refresh(savedCommandeProduit);
        return convertToDto(savedCommandeProduit);
    }

    @Transactional
    public Optional<CommandeProduitDto> updateCommandeProduit(Integer id, CommandeProduitDto commandeProduitDto) {
        if (!commandeProduitRepository.existsById(id)) {
            return Optional.empty();
        }
        CommandeProduit commandeProduit = convertToEntity(commandeProduitDto);
        commandeProduit.setId(id);
        CommandeProduit updatedCommandeProduit = commandeProduitRepository.save(commandeProduit);
        return Optional.of(convertToDto(updatedCommandeProduit));
    }

    @Transactional
    public boolean deleteCommandeProduit(Integer id) {
        if (commandeProduitRepository.existsById(id)) {
            commandeProduitRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Transactional(readOnly = true)
    public List<CommandeProduitDto> getCommandeProduitsByCommandeId(Integer commandeId) {
        return commandeProduitRepository.findByCommandeId(commandeId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private CommandeProduitDto convertToDto(CommandeProduit commandeProduit) {
        return new CommandeProduitDto(commandeProduit.getId(), commandeProduit.getCommande().getId(), commandeProduit.getProduit().getId(), commandeProduit.getQuantite());
    }

    private CommandeProduit convertToEntity(CommandeProduitDto commandeProduitDto) {
        CommandeProduit commandeProduit = new CommandeProduit();
        commandeProduit.setCommande(commandeRepository.getOne(commandeProduitDto.getCommandeId()));
        commandeProduit.setProduit(produitRepository.getOne(commandeProduitDto.getProduitId()));
        commandeProduit.setQuantite(commandeProduitDto.getQuantite());
        return commandeProduit;
    }
}