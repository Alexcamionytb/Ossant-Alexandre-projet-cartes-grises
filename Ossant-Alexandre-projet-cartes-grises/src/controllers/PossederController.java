package controllers;

import models.Posseder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import database.DatabaseConnection;

public class PossederController {

    // Méthode pour récupérer tous les enregistrements de la table POSSEDER
    public List<Posseder> getAllPosseder() {
        List<Posseder> possederList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();  // Connexion à la base de données
             Statement stmt = conn.createStatement()) {  // Création d'un Statement pour exécuter une requête
            String query = "SELECT * FROM POSSEDER";  // Requête pour obtenir toutes les données de la table POSSEDER
            ResultSet rs = stmt.executeQuery(query);  // Exécution de la requête et récupération des résultats

            // Parcours des résultats et ajout de chaque enregistrement à la liste possederList
            while (rs.next()) {
                Posseder posseder = new Posseder(
                    rs.getInt("id_proprietaire"),
                    rs.getInt("id_vehicule"),
                    rs.getDate("date_debut_propriete"),
                    rs.getDate("date_fin_propriete")
                );
                possederList.add(posseder);  // Ajout de l'objet Posseder à la liste
            }
        } catch (Exception e) {
            e.printStackTrace();  // En cas d'erreur, l'exception est affichée
        }
        return possederList;  // Retour de la liste des possesseurs
    }

    // Méthode pour ajouter un nouvel enregistrement dans la table POSSEDER
    public void addPosseder(Posseder posseder) {
        try (Connection conn = DatabaseConnection.getConnection()) {  // Connexion à la base de données
            String query = "INSERT INTO POSSEDER (id_proprietaire, id_vehicule, date_debut_propriete, date_fin_propriete) VALUES (?, ?, ?, ?)";
            // Préparation de la requête d'insertion avec des paramètres
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, posseder.getIdProprietaire());  // Définition du paramètre pour id_proprietaire
            pstmt.setInt(2, posseder.getIdVehicule());  // Définition du paramètre pour id_vehicule
            pstmt.setDate(3, new java.sql.Date(posseder.getDateDebutPropriete().getTime()));  // Définition de la date_debut_propriete
            pstmt.setDate(4, posseder.getDateFinPropriete() != null ? new java.sql.Date(posseder.getDateFinPropriete().getTime()) : null);  // Définition de la date_fin_propriete (peut être null)
            pstmt.executeUpdate();  // Exécution de la requête d'insertion
        } catch (Exception e) {
            e.printStackTrace();  // Affichage de l'exception en cas d'erreur
        }
    }

    // Méthode pour mettre à jour un enregistrement existant dans la table POSSEDER
    public void updatePosseder(Posseder posseder) {
        try (Connection conn = DatabaseConnection.getConnection()) {  // Connexion à la base de données
            String query = "UPDATE POSSEDER SET date_debut_propriete = ?, date_fin_propriete = ? WHERE id_proprietaire = ? AND id_vehicule = ?";
            // Préparation de la requête de mise à jour avec des paramètres
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, new java.sql.Date(posseder.getDateDebutPropriete().getTime()));  // Mise à jour de la date_debut_propriete
            pstmt.setDate(2, posseder.getDateFinPropriete() != null ? new java.sql.Date(posseder.getDateFinPropriete().getTime()) : null);  // Mise à jour de la date_fin_propriete (peut être null)
            pstmt.setInt(3, posseder.getIdProprietaire());  // Condition sur id_proprietaire
            pstmt.setInt(4, posseder.getIdVehicule());  // Condition sur id_vehicule
            pstmt.executeUpdate();  // Exécution de la requête de mise à jour
        } catch (Exception e) {
            e.printStackTrace();  // Affichage de l'exception en cas d'erreur
        }
    }

    // Méthode pour supprimer un enregistrement de la table POSSEDER
    public void deletePosseder(int idProprietaire, int idVehicule) {
        try (Connection conn = DatabaseConnection.getConnection()) {  // Connexion à la base de données
            String query = "DELETE FROM POSSEDER WHERE id_proprietaire = ? AND id_vehicule = ?";
            // Préparation de la requête de suppression avec des paramètres
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idProprietaire);  // Paramètre pour id_proprietaire
            pstmt.setInt(2, idVehicule);  // Paramètre pour id_vehicule
            pstmt.executeUpdate();  // Exécution de la requête de suppression
        } catch (Exception e) {
            e.printStackTrace();  // Affichage de l'exception en cas d'erreur
        }
    }

    // Méthode pour obtenir l'ID du propriétaire en fonction de son nom
    public int getIdProprietaire(String nomProprietaire) {
        try (Connection conn = DatabaseConnection.getConnection()) {  // Connexion à la base de données
            String query = "SELECT id_proprietaire FROM PROPRIETAIRE WHERE nom = ?";
            // Préparation de la requête pour récupérer l'id du propriétaire par son nom
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, nomProprietaire);  // Paramètre pour le nom du propriétaire
            ResultSet rs = pstmt.executeQuery();  // Exécution de la requête et récupération du résultat
            if (rs.next()) {
                return rs.getInt("id_proprietaire");  // Retour de l'id du propriétaire
            }
        } catch (Exception e) {
            e.printStackTrace();  // Affichage de l'exception en cas d'erreur
        }
        return -1;  // Retour -1 si le propriétaire n'est pas trouvé
    }

    // Méthode pour obtenir l'ID du véhicule en fonction du modèle du véhicule
    public int getIdVehiculeParModele(String nomModele) {
        try (Connection conn = DatabaseConnection.getConnection()) {  // Connexion à la base de données
            String query = "SELECT v.id_vehicule FROM VEHICULE v JOIN MODELE m ON v.id_modele = m.id_modele WHERE m.nom_modele = ?";
            // Préparation de la requête pour récupérer l'id du véhicule en fonction du modèle
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, nomModele);  // Paramètre pour le nom du modèle
            ResultSet rs = pstmt.executeQuery();  // Exécution de la requête et récupération du résultat
            if (rs.next()) {
                return rs.getInt("id_vehicule");  // Retour de l'id du véhicule
            }
        } catch (Exception e) {
            e.printStackTrace();  // Affichage de l'exception en cas d'erreur
        }
        return -1;  // Retour -1 si le véhicule n'est pas trouvé
    }

    // Méthode pour obtenir le nom du propriétaire à partir de son ID
    public String getNomProprietaire(int idProprietaire) {
        try (Connection conn = DatabaseConnection.getConnection()) {  // Connexion à la base de données
            String query = "SELECT nom FROM PROPRIETAIRE WHERE id_proprietaire = ?";
            // Préparation de la requête pour récupérer le nom du propriétaire en fonction de son id
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idProprietaire);  // Paramètre pour id_proprietaire
            ResultSet rs = pstmt.executeQuery();  // Exécution de la requête et récupération du résultat
            if (rs.next()) {
                return rs.getString("nom");  // Retour du nom du propriétaire
            }
        } catch (Exception e) {
            e.printStackTrace();  // Affichage de l'exception en cas d'erreur
        }
        return "Inconnu";  // Retour d'une valeur par défaut si le propriétaire n'est pas trouvé
    }

    // Méthode pour obtenir le nom du modèle du véhicule en fonction de son ID
    public String getNomModele(int idVehicule) {
        try (Connection conn = DatabaseConnection.getConnection()) {  // Connexion à la base de données
            String query = "SELECT m.nom_modele FROM MODELE m JOIN VEHICULE v ON m.id_modele = v.id_modele WHERE v.id_vehicule = ?";
            // Préparation de la requête pour récupérer le nom du modèle en fonction de l'id du véhicule
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idVehicule);  // Paramètre pour id_vehicule
            ResultSet rs = pstmt.executeQuery();  // Exécution de la requête et récupération du résultat
            if (rs.next()) {
                return rs.getString("nom_modele");  // Retour du nom du modèle
            }
        } catch (Exception e) {
            e.printStackTrace();  // Affichage de l'exception en cas d'erreur
        }
        return "Inconnu";  // Retour d'une valeur par défaut si le modèle n'est pas trouvé
    }
}
