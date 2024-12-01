package com.PawPal_Clinic.Backend.service;

import com.PawPal_Clinic.Backend.dto.CommandeDto;
import com.PawPal_Clinic.Backend.model.Commande;
import com.PawPal_Clinic.Backend.repository.CommandeRepository;
import com.PawPal_Clinic.Backend.repository.UtilisateurRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<CommandeDto> getAllCommandes() {
        return commandeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CommandeDto> getCommandeById(Integer id) {
        return commandeRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional
    public CommandeDto createCommande(CommandeDto commandeDto) {
        Commande commande = convertToEntity(commandeDto);
        Commande savedCommande = commandeRepository.save(commande);
        entityManager.refresh(savedCommande);
        return convertToDto(savedCommande);
    }

    @Transactional
    public Optional<CommandeDto> updateCommande(Integer id, CommandeDto commandeDto) {
        if (!commandeRepository.existsById(id)) {
            return Optional.empty();
        }
        Commande commande = convertToEntity(commandeDto);
        commande.setId(id);
        Commande updatedCommande = commandeRepository.save(commande);
        return Optional.of(convertToDto(updatedCommande));
    }

    @Transactional
    public boolean deleteCommande(Integer id) {
        if (commandeRepository.existsById(id)) {
            commandeRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Transactional(readOnly = true)
    public List<CommandeDto> getCommandesByUserId(Integer userId) {
        return commandeRepository.findByProprietaireId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommandeDto convertToDto(Commande commande) {
        return new CommandeDto(commande.getId(), commande.getProprietaire().getId(), commande.getDateCommande(), commande.getStatut());
    }

    private Commande convertToEntity(CommandeDto commandeDto) {
        Commande commande = new Commande();
        commande.setProprietaire(utilisateurRepository.getOne(commandeDto.getProprietaireId()));
        commande.setDateCommande(commandeDto.getDateCommande());
        commande.setStatut(commandeDto.getStatut());
        return commande;
    }
}